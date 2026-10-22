# Technical Stack Specifications

## 1. Core Platform & Runtime
* **Target Operating System:** Android 8.0 (API Level 26) and higher.
* **Language:** **Java** (Standard language for native Android development).
* **Build System:** Gradle (Groovy DSL).
* **Architecture Pattern:** MVVM (Model-View-ViewModel) + Repository Pattern.

---

## 2. Libraries & SDKs

| Category | Component / Library | Purpose |
| --- | --- | --- |
| **Maps & Spatial** | `com.google.android.gms:play-services-maps:18.2.0` | Rendering map tiles, routes, and custom bus markers. |
| **Location Engine** | `com.google.android.gms:play-services-location:21.1.0` | Powering high-accuracy GPS polling via `FusedLocationProviderClient`. |
| **Backend / Database** | `com.google.firebase:firebase-database` | Real-time WebSocket data synchronization between Beacon and Student client. |
| **UI Components** | `com.google.android.material:material:1.11.0` | Providing Material Design 3 elements (Cards, BottomSheet, FABs, TopAppBar). |
| **Asynchronous Logic** | `java.util.concurrent.ExecutorService` | Executing non-blocking background location updates and UI rendering. |

---

## 3. Hardware & Sensor API Utilization

* **GPS / GNSS Receiver:** High-accuracy location provider mode for fetching Latitude, Longitude, Bearing, and Speed.
* **Telephony Manager:** `Intent.ACTION_DIAL` trigger for hardware dialer interaction.
* **Android System Services:**
  * `NotificationManager`: Handles ongoing persistent foreground notifications.
  * `PowerManager`: Manages wake-locks to prevent CPU sleep during GPS beacon broadcasting.

---

## 4. Cloud Infrastructure

+-------------------------------------------------------------------+
|                   Google Firebase Infrastructure                  |
|                                                                   |
|   +-----------------------------------------------------------+   |
|   |                 Firebase Realtime Database                |   |
|   |  - Protocol: WebSockets (wss://)                          |   |
|   |  - Latency: < 100ms sync speed                            |   |
|   |  - Data Format: Hierarchical JSON                         |   |
|   +-----------------------------------------------------------+   |
+-------------------------------------------------------------------+