package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BusLocationService extends Service {
    private static final String TAG = "BusLocationService";
    private static final String CHANNEL_ID = "BusLocationChannel";
    private static final String FIREBASE_DB_URL = "https://campus-bus-tracker-5ba8e-default-rtdb.firebaseio.com";

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private DatabaseReference locationRef;
    private String busId = "BUS_01";

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createNotificationChannel();

        try {
            locationRef = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("buses").child(busId).child("location");
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize Firebase in service: " + e.getMessage());
        }

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    updateLocationToFirebase(location);
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("BUS_ID")) {
            busId = intent.getStringExtra("BUS_ID");
            try {
                locationRef = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("buses").child(busId).child("location");
            } catch (Exception e) {
                Log.e(TAG, "Failed to update busId locationRef: " + e.getMessage());
            }
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("EnRoute Beacon Broadcaster")
                .setContentText("Broadcasting live telemetry for " + busId)
                .setSmallIcon(R.drawable.ic_sensors)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
            } else {
                startForeground(1, notification);
            }
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException starting foreground service: " + e.getMessage());
            stopSelf();
            return START_NOT_STICKY;
        }

        startLocationUpdates();

        return START_STICKY;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "Location permission missing in service");
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
                .setMinUpdateIntervalMillis(2000)
                .build();

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        Log.d(TAG, "Location updates started for bus: " + busId);
    }

    private void updateLocationToFirebase(Location location) {
        if (location == null) return;

        BusLocation busLocation = new BusLocation(
                location.getLatitude(),
                location.getLongitude(),
                location.getBearing(),
                location.getSpeed() * 3.6f, // Convert m/s to km/h
                System.currentTimeMillis()
        );

        if (locationRef != null) {
            locationRef.setValue(busLocation)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Telemetry synced to Firebase: " + busId))
                    .addOnFailureListener(e -> Log.e(TAG, "Firebase sync failed: " + e.getMessage()));
        }

        // Broadcast local update to BeaconActivity UI
        Intent intent = new Intent("BusLocationUpdate");
        intent.setPackage(getPackageName());
        intent.putExtra("lat", location.getLatitude());
        intent.putExtra("lng", location.getLongitude());
        intent.putExtra("accuracy", location.getAccuracy());
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        Log.d(TAG, "BusLocationService destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "EnRoute Bus Broadcast Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            serviceChannel.setDescription("Shows persistent status while broadcasting campus bus GPS");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
