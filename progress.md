# EnRoute: NMIMS Campus Transit - Project Progress Log

**Last Updated:** October 22, 2026  
**Build Status:** Clean Build Verified (`BUILD SUCCESSFUL in 11s` via `./gradlew assembleDebug`)  
**Active APK:** `E:\MyProjects\APP1730\EnRoute-Campus-Tracker-debug.apk` (~8.33 MB)

---

## 📊 Executive Summary of Today's Work (October 22, 2026)

Today's development session accomplished five major milestones:
1. **Eliminated Google Maps Billing Blocker:** Transitioned student map to a fully offline-bundled Leaflet & OpenStreetMap vector engine.
2. **Multi-Route & Fleet Architecture:** Implemented real-time multi-bus route switching across both Student and Driver apps.
3. **Map Navigation & Telemetry Controls:** Added interactive camera controls (Recenter on Bus, My Location, Fit Bounds) and three-tier status pulse telemetry (Live, Idle, Offline).
4. **Resolved "California GPS" Issue:** Diagnosed Android Studio Emulator default GPS antenna coordinates transmitting Google HQ location to Firebase.
5. **UI/UX Master Redesign (Strictly UI-Only, Java):** Modernized Splash and Role Selection screens with a premium mobility palette, native entrance animations, standardized buttons, and dynamic system-bar inset handling.

---

## 🛠️ Detailed Progress Log

### 1. Offline Leaflet Map Engine (Google Maps Blocker Solved)
* **Problem:** Previous Google Maps SDK implementation was blocked by Google Cloud Billing requirements and showed a blank screen on the student side.
* **Solution:**
  * Downloaded and packaged [leaflet.js](file:///e:/MyProjects/APP1730/app/app/src/main/assets/leaflet.js) (147 KB) and [leaflet.css](file:///e:/MyProjects/APP1730/app/app/src/main/assets/leaflet.css) (14 KB) directly into `app/src/main/assets/`.
  * Removed external CDN dependencies (`unpkg.com`) that triggered Android WebView cross-origin `file://` security blocks.
  * Added `android:usesCleartextTraffic="true"` in [AndroidManifest.xml](file:///e:/MyProjects/APP1730/app/app/src/main/AndroidManifest.xml).
  * Enabled `setAllowFileAccessFromFileURLs(true)` and `setAllowUniversalAccessFromFileURLs(true)` in [StudentMapActivity.java](file:///e:/MyProjects/APP1730/app/app/src/main/java/com/example/myapplication/StudentMapActivity.java).
  * Implemented WebView lifecycle readiness handling (`onPageFinished` triggers `map.invalidateSize()` and flushes queued bus positions).
  * Connected free, global OpenStreetMap vector tiles (`tile.openstreetmap.org`) with CartoDB Voyager backup.

---

### 2. Multi-Route & Fleet Architecture
* **Repository & Data Models:**
  * Created [BusRoute.java](file:///e:/MyProjects/APP1730/app/app/src/main/java/com/example/myapplication/BusRoute.java) and [BusRouteRepository.java](file:///e:/MyProjects/APP1730/app/app/src/main/java/com/example/myapplication/BusRouteRepository.java) defining campus fleet routes:
    * **Bus 12 (`BUS_01`):** Route 01 - City & Metro Express (Bhawarkua Metro ⇄ NMIMS Campus)
    * **Bus 08 (`BUS_02`):** Route 02 - Hostel & Campus Shuttle (Hostels ⇄ Annapurna ⇄ NMIMS Campus)
    * **Bus 05 (`BUS_03`):** Route 03 - Tech Park & Labs Loop (Super Corridor ⇄ IT Park ⇄ Engineering Block)
* **Student Route Switcher:**
  * Added horizontal chip carousel ([item_route_chip.xml](file:///e:/MyProjects/APP1730/app/app/src/main/res/layout/item_route_chip.xml)) directly beneath the top navigation card.
  * Tapping any route dynamically switches Firebase listener (`/buses/{busId}/location`), updates driver phone, and updates stop details without reloading the activity.
* **Driver Vehicle Assignment:**
  * Added route selector in [BeaconActivity.java](file:///e:/MyProjects/APP1730/app/app/src/main/java/com/example/myapplication/BeaconActivity.java) allowing drivers to choose their assigned bus.
  * Selector automatically locks during active broadcasting to prevent mid-trip stream collision.

---

### 3. Student Map UX & Controls
* **Floating Map Controls:**
  * **Center on Bus:** Smoothly pans and zooms camera directly to the bus marker.
  * **My Location:** Smoothly centers the camera onto the student's live GPS fix.
  * **Fit Route View:** Fits both the student and the bus into the viewport simultaneously.
* **Free Exploration:** Removed forced camera snapping on every 3-second ping so students can freely pan, pinch, and inspect campus stops.
* **Status Pulse Telemetry:**
  * **Live (< 25s):** Pulsing green radar wave and live dot on both the map marker and bottom sheet.
  * **Idle (25s - 90s):** Amber pulse indicating the vehicle is stopped at a station.
  * **Offline (> 90s):** Muted gray styling indicating the bus beacon is parked or off-duty.
* **Safe Dialer FAB:** Added fallback exception handling to prevent crashes on non-telephony devices or emulators.

---

### 4. Diagnosis: Emulator GPS vs India Location
* **Finding:** Investigated report of bus appearing in "America". Queried Firebase Realtime Database and verified coordinates:
  `latitude: 37.4219983, longitude: -122.084` (Google Headquarters, Mountain View, California).
* **Cause:** Running Driver / Beacon mode on an Android Studio Emulator defaults simulated GPS antenna to Google HQ.
* **Resolution Documented:**
  * Override emulator GPS via Extended Controls (`...` > Location > Set to `22.7196, 75.8577`).
  * Run Beacon mode on physical phone to transmit real Indian hardware GPS coordinates.

---

### 5. UI/UX Master Redesign & Polish (Java-Only, Strict UI Scope)
* **Master Palette ([colors.xml](file:///e:/MyProjects/APP1730/app/app/src/main/res/values/colors.xml) & [themes.xml](file:///e:/MyProjects/APP1730/app/app/src/main/res/values/themes.xml)):**
  * Warm Off-White (`#FAF9F6`), Deep Navy (`#102333`), Dark Charcoal (`#252A2D`), Muted Sage Green (`#D7E7D5`), Sage Accent (`#3E8050`), Secondary Text (`#697179`), Subtle Dividers (`#E7E5DF`).
  * Completely eliminated all rainbow gradients, oversized pills, and neon colors.
* **System-Bar & Safe Area Inset Resolution:**
  * Implemented `ViewCompat.setOnApplyWindowInsetsListener` across:
    * [SplashActivity.java](file:///e:/MyProjects/APP1730/app/app/src/main/java/com/example/myapplication/SplashActivity.java)
    * [MainActivity.java](file:///e:/MyProjects/APP1730/app/app/src/main/java/com/example/myapplication/MainActivity.java)
    * [BeaconActivity.java](file:///e:/MyProjects/APP1730/app/app/src/main/java/com/example/myapplication/BeaconActivity.java)
    * [StudentMapActivity.java](file:///e:/MyProjects/APP1730/app/app/src/main/java/com/example/myapplication/StudentMapActivity.java)
  * Prevents top notch/camera cutout clipping and bottom gesture/3-button navigation bar overlap dynamically on all device sizes.
* **Polished Splash Experience ([activity_splash.xml](file:///e:/MyProjects/APP1730/app/app/src/main/res/layout/activity_splash.xml)):**
  * Centered composition, proportional logo card, bold `ENROUTE` wordmark (`#102333`, 30sp), refined subtitle (`NMIMS CAMPUS TRANSPORT`), subtle sage status badge, and a linear loading progress indicator.
  * Staggered `ViewPropertyAnimator` entrance sequence with clean alpha and translation transitions.
* **Refined Role Selection Screen ([activity_main.xml](file:///e:/MyProjects/APP1730/app/app/src/main/res/layout/activity_main.xml)):**
  * Compact header with crisp logo, `ENROUTE` brand mark, and understated `NMIMS` badge.
  * Live GPS Network Active banner in restrained sage tone.
  * Matching 16dp rounded role cards with 1dp subtle borders, circular icon containers, double-click debouncing, and institutional footer.
* **Standardized Button System:**
  * Replaced oversized circular button in Beacon mode with a 56dp full-width high-touch primary button in solid deep navy `#102333` (idle) and restrained red `#BA1A1A` (active trip).

---

## 📦 Deliverables & File Locations

* **Direct Export APK:** [EnRoute-Campus-Tracker-debug.apk](file:///e:/MyProjects/APP1730/EnRoute-Campus-Tracker-debug.apk) *(Updated in project root, ~8.33 MB)*
* **Build Path:** [app-debug.apk](file:///e:/MyProjects/APP1730/app/app/build/outputs/apk/debug/app-debug.apk)
* **Configuration:** [gradle.properties](file:///e:/MyProjects/APP1730/app/gradle.properties) configured with Android Studio bundled JDK 17+ (JBR) for reliable builds.

---

## 🚀 Next Steps
1. Test on physical devices across different screen aspect ratios, camera cutouts, and gesture navigation modes.
2. Verify real-time dual-device tracking in the field (one device as Beacon, one as Student).
