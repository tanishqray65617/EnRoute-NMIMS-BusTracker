package com.example.myapplication;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Locale;

public class StudentMapActivity extends AppCompatActivity {
    private static final String TAG = "StudentMapActivity";
    private static final String FIREBASE_DB_URL = "https://campus-bus-tracker-5ba8e-default-rtdb.firebaseio.com";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private WebView mapWebView;
    private DatabaseReference locationRef;
    private ValueEventListener locationListener;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private BusRoute currentRoute;
    private boolean isMapReady = false;
    private BusLocation pendingBusLocation = null;
    private boolean isInitialBusFitDone = false;
    private boolean hasBusLocation = false;
    private boolean hasUserLocation = false;

    // UI elements
    private TextView tvSpeed;
    private TextView tvStatus;
    private TextView tvEta;
    private TextView tvCurrentStop;
    private TextView tvNextStop;
    private TextView tvRouteName;
    private TextView tvBusBadge;
    private TextView tvRouteBadge;
    private TextView tvTopRoutePill;
    private TextView tvDriverName;
    private TextView tvBusIdPlate;
    private View viewStatusDot;
    private LinearLayout layoutRouteChips;
    private FloatingActionButton fabCall;
    private ImageButton btnBack;

    // Floating map controls
    private MaterialCardView btnCenterBus;
    private MaterialCardView btnCenterUser;
    private MaterialCardView btnFitBounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_map);

        currentRoute = BusRouteRepository.getRoutes().get(0);

        tvSpeed = findViewById(R.id.tvSpeed);
        tvStatus = findViewById(R.id.tvStatus);
        tvEta = findViewById(R.id.tvEta);
        tvCurrentStop = findViewById(R.id.tvCurrentStop);
        tvNextStop = findViewById(R.id.tvNextStop);
        tvRouteName = findViewById(R.id.tvRouteName);
        tvBusBadge = findViewById(R.id.tvBusBadge);
        tvRouteBadge = findViewById(R.id.tvRouteBadge);
        tvTopRoutePill = findViewById(R.id.tvTopRoutePill);
        tvDriverName = findViewById(R.id.tvDriverName);
        tvBusIdPlate = findViewById(R.id.tvBusIdPlate);
        viewStatusDot = findViewById(R.id.viewStatusDot);
        layoutRouteChips = findViewById(R.id.layoutRouteChips);
        fabCall = findViewById(R.id.fabCall);
        btnBack = findViewById(R.id.btnBack);
        mapWebView = findViewById(R.id.mapWebView);

        btnCenterBus = findViewById(R.id.btnCenterBus);
        btnCenterUser = findViewById(R.id.btnCenterUser);
        btnFitBounds = findViewById(R.id.btnFitBounds);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        View cardTopBar = findViewById(R.id.cardTopBar);
        View scrollRoutes = findViewById(R.id.scrollRoutes);
        View layoutMapControls = findViewById(R.id.layoutMapControls);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            if (cardTopBar != null && cardTopBar.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) cardTopBar.getLayoutParams();
                lp.topMargin = systemBars.top + dpToPx(12);
                cardTopBar.setLayoutParams(lp);
            }
            if (scrollRoutes != null && scrollRoutes.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.LayoutParams lpRoutes = (CoordinatorLayout.LayoutParams) scrollRoutes.getLayoutParams();
                lpRoutes.topMargin = systemBars.top + dpToPx(76);
                scrollRoutes.setLayoutParams(lpRoutes);
            }
            if (layoutMapControls != null && layoutMapControls.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.LayoutParams lpControls = (CoordinatorLayout.LayoutParams) layoutMapControls.getLayoutParams();
                lpControls.bottomMargin = systemBars.bottom + dpToPx(200);
                layoutMapControls.setLayoutParams(lpControls);
            }
            return insets;
        });

        setupMapWebView();
        setupMapControls();
        setupDialer();
        setupRouteSelectorChips();
        bindRouteData(currentRoute);

        connectToBusStream(currentRoute.busId);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setupUserLocation();
    }

    private void setupMapWebView() {
        if (mapWebView == null) return;
        WebSettings webSettings = mapWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36 EnRoute/1.0");

        mapWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isMapReady = true;
                Log.d(TAG, "Map asset loaded successfully: " + url);
                view.evaluateJavascript("javascript:resizeMap();", null);
                if (pendingBusLocation != null) {
                    updateMap(pendingBusLocation);
                    pendingBusLocation = null;
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.w(TAG, "Map asset load error: " + error.toString());
            }
        });

        mapWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("LeafletMapConsole", consoleMessage.message() + " -- Line " + consoleMessage.lineNumber());
                return true;
            }
        });

        mapWebView.loadUrl("file:///android_asset/map.html");
    }

    private void setupMapControls() {
        if (btnCenterBus != null) {
            btnCenterBus.setOnClickListener(v -> {
                if (hasBusLocation) {
                    mapWebView.evaluateJavascript("javascript:centerOnBus();", null);
                } else {
                    Toast.makeText(StudentMapActivity.this, "Bus has not broadcasted its position yet.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnCenterUser != null) {
            btnCenterUser.setOnClickListener(v -> {
                if (hasUserLocation) {
                    mapWebView.evaluateJavascript("javascript:centerOnUser();", null);
                } else {
                    Toast.makeText(StudentMapActivity.this, "Acquiring your GPS location...", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnFitBounds != null) {
            btnFitBounds.setOnClickListener(v -> {
                mapWebView.evaluateJavascript("javascript:fitBothMarkers();", null);
            });
        }
    }

    private void setupDialer() {
        if (fabCall != null) {
            fabCall.setOnClickListener(v -> {
                String phone = currentRoute.driverPhone;
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(StudentMapActivity.this, "Driver contact: " + phone + " (No dialer app)", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG, "Error launching dialer", e);
                    Toast.makeText(StudentMapActivity.this, "Driver contact: " + phone, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupRouteSelectorChips() {
        if (layoutRouteChips == null) return;
        layoutRouteChips.removeAllViews();

        List<BusRoute> routes = BusRouteRepository.getRoutes();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (BusRoute route : routes) {
            View chipView = inflater.inflate(R.layout.item_route_chip, layoutRouteChips, false);
            MaterialCardView cardChip = chipView.findViewById(R.id.cardChip);
            TextView tvChipBusBadge = chipView.findViewById(R.id.tvChipBusBadge);
            TextView tvChipRouteName = chipView.findViewById(R.id.tvChipRouteName);

            tvChipBusBadge.setText(route.busNumber);
            tvChipRouteName.setText(route.routeCode + " • " + route.routeName);

            boolean isSelected = route.busId.equals(currentRoute.busId);
            updateChipStyle(cardChip, tvChipBusBadge, tvChipRouteName, isSelected);

            cardChip.setOnClickListener(v -> selectRoute(route));

            layoutRouteChips.addView(chipView);
        }
    }

    private void updateChipStyle(MaterialCardView card, TextView badge, TextView name, boolean isSelected) {
        if (isSelected) {
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary));
            card.setStrokeColor(ContextCompat.getColor(this, R.color.primary));
            badge.setBackgroundResource(R.drawable.bg_pill_live);
            badge.setTextColor(ContextCompat.getColor(this, R.color.on_secondary_container));
            name.setTextColor(ContextCompat.getColor(this, R.color.on_primary));
        } else {
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.surface_container_lowest));
            card.setStrokeColor(ContextCompat.getColor(this, R.color.subtle_divider));
            badge.setBackgroundResource(R.drawable.bg_pill_route);
            badge.setTextColor(ContextCompat.getColor(this, R.color.on_primary));
            name.setTextColor(ContextCompat.getColor(this, R.color.primary));
        }
    }

    private void selectRoute(BusRoute newRoute) {
        if (newRoute.busId.equals(currentRoute.busId)) return;

        // Disconnect old listener
        if (locationRef != null && locationListener != null) {
            locationRef.removeEventListener(locationListener);
        }

        currentRoute = newRoute;
        hasBusLocation = false;
        pendingBusLocation = null;
        isInitialBusFitDone = false;

        // Clear marker from map
        mapWebView.evaluateJavascript("javascript:clearBusMarker();", null);

        // Update UI
        bindRouteData(newRoute);
        setupRouteSelectorChips();

        // Connect new stream
        connectToBusStream(newRoute.busId);
    }

    private void bindRouteData(BusRoute route) {
        if (tvBusBadge != null) tvBusBadge.setText(route.busNumber);
        if (tvRouteBadge != null) tvRouteBadge.setText(route.routeCode);
        if (tvTopRoutePill != null) tvTopRoutePill.setText(route.routeCode);
        if (tvRouteName != null) tvRouteName.setText(route.routePath);
        if (tvCurrentStop != null) tvCurrentStop.setText(route.currentStop);
        if (tvNextStop != null) tvNextStop.setText(route.nextStop);
        if (tvDriverName != null) tvDriverName.setText(route.driverName);
        if (tvBusIdPlate != null) {
            tvBusIdPlate.setText(String.format("Bus ID: %s • Driver: %s", route.busId, route.driverName));
        }
        if (tvStatus != null) {
            tvStatus.setText("SEARCHING SIGNAL...");
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.outline));
        }
        if (viewStatusDot != null) {
            viewStatusDot.setBackgroundResource(R.drawable.bg_status_dot_idle);
        }
    }

    private void connectToBusStream(String busId) {
        try {
            locationRef = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("buses").child(busId).child("location");
            locationListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    BusLocation location = snapshot.getValue(BusLocation.class);
                    if (location != null) {
                        hasBusLocation = true;
                        updateMap(location);
                    } else {
                        setBusOffline();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Database error: " + error.getMessage());
                }
            };
            locationRef.addValueEventListener(locationListener);
        } catch (Exception e) {
            Log.e(TAG, "Error connecting to Firebase Database: " + e.getMessage());
        }
    }

    private void updateMap(BusLocation location) {
        long timeDiff = System.currentTimeMillis() - location.timestamp;
        final String statusString;

        if (timeDiff < 25000) {
            statusString = "live";
            if (tvStatus != null) {
                tvStatus.setText("LIVE / ON ROUTE");
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_live_text));
            }
            if (viewStatusDot != null) {
                viewStatusDot.setBackgroundResource(R.drawable.bg_status_dot_live);
            }
        } else if (timeDiff < 90000) {
            statusString = "idle";
            if (tvStatus != null) {
                tvStatus.setText("IDLE / STOPPED");
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.accent_orange));
            }
            if (viewStatusDot != null) {
                viewStatusDot.setBackgroundResource(R.drawable.bg_status_dot_idle);
            }
        } else {
            statusString = "offline";
            if (tvStatus != null) {
                tvStatus.setText("OFFLINE / PARKED");
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.outline));
            }
            if (viewStatusDot != null) {
                viewStatusDot.setBackgroundResource(R.drawable.bg_status_dot_idle);
            }
        }

        if (tvSpeed != null) {
            tvSpeed.setText(String.format(Locale.US, "%.0f km/h", location.speed));
        }

        if (tvEta != null) {
            if (location.speed > 5) {
                int estimatedMins = Math.max(3, (int) Math.round(30.0 / Math.max(location.speed, 10.0)));
                tvEta.setText(String.format(Locale.US, "%02d", estimatedMins));
            } else {
                tvEta.setText("08");
            }
        }

        if (!isMapReady) {
            pendingBusLocation = location;
            return;
        }

        runOnUiThread(() -> {
            String jsCode = String.format(Locale.US, "javascript:updateBusLocation(%f, %f, '%s', '%s');",
                    location.latitude, location.longitude, currentRoute.busNumber, statusString);
            mapWebView.evaluateJavascript(jsCode, null);

            if (!isInitialBusFitDone) {
                isInitialBusFitDone = true;
                if (hasUserLocation) {
                    mapWebView.evaluateJavascript("javascript:fitBothMarkers();", null);
                } else {
                    mapWebView.evaluateJavascript("javascript:centerOnBus();", null);
                }
            }
        });
    }

    private void setBusOffline() {
        runOnUiThread(() -> {
            if (tvStatus != null) {
                tvStatus.setText("OFFLINE / NO SIGNAL");
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.outline));
            }
            if (viewStatusDot != null) {
                viewStatusDot.setBackgroundResource(R.drawable.bg_status_dot_idle);
            }
            if (tvSpeed != null) {
                tvSpeed.setText("0 km/h");
            }
        });
    }

    private void setupUserLocation() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    hasUserLocation = true;
                    updateUserMapLocation(location);
                }
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(2000)
                .build();
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission is required to show your position on campus.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUserMapLocation(Location location) {
        if (!isMapReady) return;
        runOnUiThread(() -> {
            String jsCode = String.format(Locale.US, "javascript:updateUserLocation(%f, %f);",
                    location.getLatitude(), location.getLongitude());
            mapWebView.evaluateJavascript(jsCode, null);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationRef != null && locationListener != null) {
            locationRef.removeEventListener(locationListener);
        }
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
