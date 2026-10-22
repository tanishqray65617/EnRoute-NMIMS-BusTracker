package com.example.myapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Locale;

public class BeaconActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    
    private TextView tvGpsStatus;
    private TextView tvLocation;
    private TextView tvBeaconModeBadge;
    private TextView tvBusId;
    private TextView tvAssignedRoute;
    private MaterialButton btnToggleTrip;
    private ImageButton btnBackBeacon;
    private LinearLayout layoutBeaconRouteChips;

    private BusRoute currentRoute;
    private boolean isBroadcasting = false;

    private final BroadcastReceiver locationUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("BusLocationUpdate")) {
                double lat = intent.getDoubleExtra("lat", 0.0);
                double lng = intent.getDoubleExtra("lng", 0.0);
                float accuracy = intent.getFloatExtra("accuracy", 0.0f);
                
                if (tvLocation != null) {
                    tvLocation.setText(String.format(Locale.US, "Lat: %.5f, Lng: %.5f", lat, lng));
                }
                if (tvGpsStatus != null) {
                    tvGpsStatus.setText(String.format(Locale.US, "GPS Lock: Strong (±%.1fm accuracy)", accuracy));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedBundleState) {
        super.onCreate(savedBundleState);
        setContentView(R.layout.activity_beacon);

        // System Bar Inset Handling
        View scrollBeacon = findViewById(R.id.scrollBeacon);
        View layoutBeaconContent = findViewById(R.id.layoutBeaconContent);
        if (scrollBeacon != null) {
            ViewCompat.setOnApplyWindowInsetsListener(scrollBeacon, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                if (layoutBeaconContent != null) {
                    layoutBeaconContent.setPadding(
                            layoutBeaconContent.getPaddingLeft(),
                            systemBars.top + dpToPx(16),
                            layoutBeaconContent.getPaddingRight(),
                            systemBars.bottom + dpToPx(24)
                    );
                }
                return insets;
            });
        }

        currentRoute = BusRouteRepository.getRoutes().get(0);

        tvGpsStatus = findViewById(R.id.tvGpsStatus);
        tvLocation = findViewById(R.id.tvLocation);
        tvBeaconModeBadge = findViewById(R.id.tvBeaconModeBadge);
        tvBusId = findViewById(R.id.tvBusId);
        tvAssignedRoute = findViewById(R.id.tvAssignedRoute);
        btnToggleTrip = findViewById(R.id.btnToggleTrip);
        btnBackBeacon = findViewById(R.id.btnBackBeacon);
        layoutBeaconRouteChips = findViewById(R.id.layoutBeaconRouteChips);

        if (btnBackBeacon != null) {
            btnBackBeacon.setOnClickListener(v -> finish());
        }

        bindRouteInfo(currentRoute);
        setupRouteChips();

        if (btnToggleTrip != null) {
            btnToggleTrip.setOnClickListener(v -> {
                if (!isBroadcasting) {
                    checkPermissionsAndStartService();
                } else {
                    stopBroadcasting();
                }
            });
        }
    }

    private void bindRouteInfo(BusRoute route) {
        if (tvBusId != null) {
            tvBusId.setText(String.format("%s • %s", route.busNumber, route.busId));
        }
        if (tvAssignedRoute != null) {
            tvAssignedRoute.setText(String.format("Assigned: %s (%s)", route.routeCode, route.routeName));
        }
    }

    private void setupRouteChips() {
        if (layoutBeaconRouteChips == null) return;
        layoutBeaconRouteChips.removeAllViews();

        List<BusRoute> routes = BusRouteRepository.getRoutes();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (BusRoute route : routes) {
            View chipView = inflater.inflate(R.layout.item_route_chip, layoutBeaconRouteChips, false);
            MaterialCardView cardChip = chipView.findViewById(R.id.cardChip);
            TextView tvChipBusBadge = chipView.findViewById(R.id.tvChipBusBadge);
            TextView tvChipRouteName = chipView.findViewById(R.id.tvChipRouteName);

            tvChipBusBadge.setText(route.busNumber);
            tvChipRouteName.setText(route.routeCode);

            boolean isSelected = route.busId.equals(currentRoute.busId);
            if (isSelected) {
                cardChip.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary));
                cardChip.setStrokeColor(ContextCompat.getColor(this, R.color.primary));
                tvChipBusBadge.setBackgroundResource(R.drawable.bg_pill_live);
                tvChipBusBadge.setTextColor(ContextCompat.getColor(this, R.color.on_secondary_container));
                tvChipRouteName.setTextColor(ContextCompat.getColor(this, R.color.on_primary));
            } else {
                cardChip.setCardBackgroundColor(ContextCompat.getColor(this, R.color.surface_container_lowest));
                cardChip.setStrokeColor(ContextCompat.getColor(this, R.color.subtle_divider));
                tvChipBusBadge.setBackgroundResource(R.drawable.bg_pill_route);
                tvChipBusBadge.setTextColor(ContextCompat.getColor(this, R.color.on_primary));
                tvChipRouteName.setTextColor(ContextCompat.getColor(this, R.color.primary));
            }

            cardChip.setOnClickListener(v -> {
                if (isBroadcasting) {
                    Toast.makeText(BeaconActivity.this, "Stop current trip before switching assigned vehicle.", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentRoute = route;
                bindRouteInfo(route);
                setupRouteChips();
            });

            layoutBeaconRouteChips.addView(chipView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(locationUpdateReceiver, new IntentFilter("BusLocationUpdate"), Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(locationUpdateReceiver, new IntentFilter("BusLocationUpdate"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(locationUpdateReceiver);
        } catch (IllegalArgumentException ignored) {}
    }

    private void checkPermissionsAndStartService() {
        String[] requiredPermissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requiredPermissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.POST_NOTIFICATIONS
            };
        } else {
            requiredPermissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
        }

        boolean allGranted = true;
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (allGranted) {
            startBroadcasting();
        } else {
            ActivityCompat.requestPermissions(this, requiredPermissions, PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                startBroadcasting();
            } else {
                Toast.makeText(this, "Permissions required to run Beacon broadcaster", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startBroadcasting() {
        Intent serviceIntent = new Intent(this, BusLocationService.class);
        serviceIntent.putExtra("BUS_ID", currentRoute.busId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        
        isBroadcasting = true;
        if (btnToggleTrip != null) {
            btnToggleTrip.setText("STOP BROADCASTING TRIP");
            btnToggleTrip.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.accent_red)));
        }
        if (tvBeaconModeBadge != null) {
            tvBeaconModeBadge.setText("BROADCASTING");
            tvBeaconModeBadge.setBackgroundResource(R.drawable.bg_pill_live);
            tvBeaconModeBadge.setTextColor(ContextCompat.getColor(this, R.color.on_secondary_container));
        }
        if (tvGpsStatus != null) {
            tvGpsStatus.setText("GPS Signal: Acquiring lock...");
        }
    }

    private void stopBroadcasting() {
        Intent serviceIntent = new Intent(this, BusLocationService.class);
        stopService(serviceIntent);
        
        isBroadcasting = false;
        if (btnToggleTrip != null) {
            btnToggleTrip.setText("START BROADCASTING TRIP");
            btnToggleTrip.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary)));
        }
        if (tvBeaconModeBadge != null) {
            tvBeaconModeBadge.setText("IDLE");
            tvBeaconModeBadge.setBackgroundResource(R.drawable.bg_pill_badge);
            tvBeaconModeBadge.setTextColor(ContextCompat.getColor(this, R.color.secondary));
        }
        if (tvGpsStatus != null) {
            tvGpsStatus.setText("GPS Signal: Standby / Idle");
        }
        if (tvLocation != null) {
            tvLocation.setText("Lat: --, Lng: --");
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
