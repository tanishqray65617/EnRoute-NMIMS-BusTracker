# Development & Implementation Workflow

This document breaks down the step-by-step engineering roadmap for building, testing, and demonstrating the **Campus Bus Live GPS Tracker**.

---

## Phase 1: Environment Setup & Architecture (Days 1–2)
* [x] **Project Initialization:** Create a modern Android Studio project using Java and Gradle (Groovy DSL).
* [x] **Google Cloud Console Configuration:**
  * Generate Google Maps API Key.
  * Enable Maps SDK for Android.
  * Configure API key restrictions (SHA-1 fingerprint & package name).
* [x] **Firebase Setup:**
  * Register Android app package in Firebase Console.
  * Connect Firebase Realtime Database.
  * Download and place `google-services.json` in the `/app` directory.

---

## Phase 2: Data Schema & Firebase Realtime Integration (Days 3–4)
* [x] Establish database security rules for development (Read/Write enabled for testing).
* [x] Implement database models in Java (`BusLocation.java`, `BusInfo.java`).
* [x] Test manual data pushing via Firebase Web Console to verify JSON structure.

---

## Phase 3: Beacon App Development - Phone A (Days 5–7)
* [x] **Location Engine:** Implement `FusedLocationProviderClient` with `Priority.PRIORITY_HIGH_ACCURACY`.
* [x] **Android Foreground Service:**
  * Build `BusLocationService.java` extending `Service()`.
  * Construct ongoing persistent notification (`NotificationCompat.Builder`).
  * Declare `FOREGROUND_SERVICE` and `ACCESS_FINE_LOCATION` permissions in `AndroidManifest.xml`.
* [x] **Payload Dispatcher:** Send updated Lat, Lng, Speed, Bearing, and Timestamp to Firebase every 3 seconds.

---

## Phase 4: Student UI & Map Implementation - Phone B (Days 8–10)
* [x] **Map Integration:** Initialize `SupportMapFragment` and implement `OnMapReadyCallback`.
* [x] **Real-Time Data Listener:**
  * Attach `ValueEventListener` to `/buses/{bus_id}/location`.
  * Parse incoming spatial telemetry dynamically.
* [x] **Animation Engine:**
  * Build a custom `MarkerAnimator` class using `ValueAnimator` and `TypeEvaluator<LatLng>` to smoothly glide the bus icon across map points instead of jumping.
* [x] **Bottom Sheet & Controls:** Implement Material `BottomSheetBehavior` for driver details and call triggers.

---

## Phase 5: Classroom Demo & Scale-Model Simulation (Days 11–12)
* [x] **High-Zoom Calibration:** Set default map camera zoom level to `18f` to magnify smaller physical movements during classroom testing.
* [x] **Field Testing:** Perform a simulated walking/running test on campus ground with Phone A broadcasting and Phone B rendering live updates.
* [x] **Edge Case Handling:** Add fallback UI for network loss or stale GPS signals (e.g., changing status badge to *"Offline"* if last update exceeds 30 seconds).