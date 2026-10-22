# 🚌 EnRoute: NMIMS Campus Transit

> **Real-time campus transit tracking, multi-route switching, and automated driver beacon broadcasting for NMIMS University.**

EnRoute is a native Android mobility system designed for students, commuters, and fleet coordinators. It connects vehicle GPS hardware with student smartphones using Firebase Realtime Database and an offline-bundled Leaflet & OpenStreetMap engine.

---

## 🌟 Key Features

### 🎓 1. Student Live Map
* **Real-Time GPS Tracking:** Live bus markers updated every 3 seconds with smooth animations.
* **Multi-Route Carousel:** Instant one-tap switching between campus routes without reloading the screen:
  * **Bus 12 (`BUS_01`):** Route 01 - City & Metro Express
  * **Bus 08 (`BUS_02`):** Route 02 - Hostel & Campus Shuttle
  * **Bus 05 (`BUS_03`):** Route 03 - Tech Park & Labs Loop
* **Interactive Camera Controls:**
  * **Recenter on Bus:** Smoothly pans and zooms directly to the bus position.
  * **My Location:** Smoothly centers onto student GPS coordinates.
  * **Fit Route View:** Fits both the student and the bus into the viewport simultaneously.
* **Three-Tier Status Telemetry:**
  * **Live (< 25s):** Pulsing green radar wave and indicator.
  * **Idle (25s - 90s):** Amber pulse for vehicles stopped at campus stations.
  * **Offline (> 90s):** Muted gray badge indicating vehicle beacon is off-duty.
* **Direct Driver Dialer:** One-tap calling to the active driver with safe fallback handling.

### 📡 2. Driver Beacon Kiosk
* **Zero-Touch GPS Broadcasting:** Kiosk-style interface with a single toggle for drivers.
* **Vehicle Assignment Selector:** Driver selects assigned vehicle (`Bus 12`, `Bus 08`, `Bus 05`) before departure.
* **Background Foreground Service:** Persistent GPS broadcast (`FOREGROUND_SERVICE_LOCATION`) streaming high-accuracy fixes even when the screen is locked or the app is minimized.
* **Broadcast Safety Lock:** Route selection locks during an active trip to prevent cross-stream conflicts.

---

## 🛠️ Architecture & Tech Stack

* **Platform:** Native Android (100% Java)
* **Design System:** Material 3 with unified Warm Off-White (`#FAF9F6`), Deep Navy (`#102333`), and Muted Sage (`#D7E7D5`) palette
* **Map Engine:** Embedded [Leaflet.js](https://leafletjs.com/) & OpenStreetMap vector tiles (fully bundled offline in `assets/`, zero external CDN dependencies, zero API key or billing required)
* **Real-Time Database:** [Firebase Realtime Database](https://firebase.google.com/docs/database)
* **Location Services:** Google Play Services `FusedLocationProviderClient` (`PRIORITY_HIGH_ACCURACY`)
* **System-Bar Insets:** Dynamic `WindowInsetsCompat` handling for seamless camera cutout/notch and gesture navigation support across all devices

---

## 🚀 Building & Running Locally

### Prerequisites
* Android Studio Ladybug / Meerkat (or newer)
* JDK 17+ (e.g., Android Studio bundled JetBrains Runtime JBR)
* Android SDK 34+ (compileSdk 36)

### Firebase Configuration
1. Place your `google-services.json` in the `app/app/` directory (see `app/app/google-services.json.example` for reference).
2. Note: `google-services.json` is gitignored to prevent sensitive API keys from being leaked to version control.

### Build APK
Clone the repository and run:
```bash
cd app
./gradlew assembleDebug
```
The output APK will be generated at:
`app/app/build/outputs/apk/debug/app-debug.apk`

---

## 👨‍💻 Author

**Tanishq Ray**  
* GitHub: [@tanishqray65617](https://github.com/tanishqray65617)  
* Email: `tanishqray65617@gmail.com`
