# Product Specification: Campus Bus Live GPS Tracker

## 1. Executive Summary
The **Campus Bus Live GPS Tracker** is a real-time transport tracking system designed to eliminate uncertainty and waiting times for college students commuting via campus buses. The system utilizes low-cost Android smartphones as dedicated vehicle beacons, transmitting real-time telemetry (GPS, speed, heading) to a cloud database, which is rendered instantly on a custom map interface for students.

---

## 2. Problem Statement & Solution

* **The Problem:** Campus buses run on flexible or delay-prone schedules. Students waste time at bus stops, miss buses due to lack of visibility, or struggle to contact drivers during off-schedule runs.
* **Operational Constraint:** Bus drivers are often tech-averse or lack smartphones, making driver-operated apps impractical.
* **The Solution:** A **zero-driver-interaction system**. A low-cost Android device is mounted inside the bus as an automated hardware beacon. Students track the moving bus in real time via an intuitive, RedBus-style mobile app.

---

## 3. Core Features & Capabilities

### 3.1 Student Portal (Subscriber Mode)
* **Live Map Visualization:** Google Maps interface displaying real-time movement of selected campus buses with custom map markers.
* **Smooth Marker Interpolation:** Marker position updates smoothly using linear animation between GPS pings rather than jumping abruptly.
* **Bus & Driver Details:** Collapsible BottomSheet containing Driver Name, Direct Call Trigger, Bus Number Plate, and Capacity.
* **Live Telemetry Dashboard:** Displays current bus speed (km/h) and real-time connectivity status ("Updated 2s ago").
* **Multi-Bus Selection:** Dropdown selector to switch between different campus bus routes (`BUS_01`, `BUS_02`, etc.).

### 3.2 Bus Beacon Portal (Publisher Mode)
* **Kiosk Mode UI:** One-tap toggle button ("START TRIP" / "STOP TRIP") requiring zero navigation.
* **Background Foreground Service:** Persistent Android service ensuring unbroken GPS transmission even when the device screen is locked or idle.
* **GPS Health Readout:** Displays current latitude, longitude, satellite accuracy (meters), and packet transmission status.

---

## 4. UI/UX Architecture & Layouts

### 4.1 Screen Structure & Flow

+-----------------------+
               |  Role Selector Screen |
               +-----------+-----------+
                           |
           +---------------+---------------+
           |                               |
           v                               v
+--------------------+           +--------------------+
|   Student View     |           |   Beacon View      |
|  (Google Maps SDK) |           |  (Kiosk Broadcaster)|
+--------------------+           +--------------------+

### 4.2 UI Wireframe Specifications

#### Screen 1: Student Live Map (`activity_student_map.xml`)
* **Top App Bar:** Material CardView overlay containing a Spinner/Dropdown to select Bus Routes.
* **Center Screen:** Full-screen `com.google.android.gms.maps.MapView`.
* **Bottom Sheet (`BottomSheetBehavior`):**
  * **Header:** Bold typography showing Route Name (e.g., *"Route 1 - Main City to Campus"*).
  * **Middle Row:** Driver Avatar, Driver Name (*"Ramesh Kumar"*), and a prominent FloatingActionButton with a Call Icon (`Intent.ACTION_DIAL`).
  * **Footer Bar:** Chip indicators showing `Speed: 24 km/h` and `Status: LIVE`.

#### Screen 2: Bus Beacon Kiosk (`activity_beacon.xml`)
* **Center Screen:** Large circular Toggle Button (Green = Active Broadcasting, Red = Idle).
* **Information Card:** Material Card displaying assigned `Bus ID`, `Current Lat/Lng`, and `Last Payload Sent Timestamp`.
* **Top Status Indicator:** Badge showing GPS Accuracy (e.g., *"GPS Signal: Strong (±3m)"*).

