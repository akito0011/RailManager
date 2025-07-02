# 🚄 GreedyRails

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blueviolet?logo=kotlin)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-Jetpack%20Compose-3DDC84?logo=android)](https://developer.android.com/jetpack/compose)
[![Retrofit](https://img.shields.io/badge/Retrofit-2.9-brightgreen)](https://square.github.io/retrofit/)
[![Room](https://img.shields.io/badge/Room-Database-yellow)](https://developer.android.com/training/data-storage/room)
[![Project Type](https://img.shields.io/badge/University%20Project-Yes-blue)]()

**GreedyRails** is a mobile application developed as a **university project** for the course **Web and Mobile Programming**.  
It functions as a **train travel assistant for Italy**, allowing users to:

- Search train routes  
- View real-time schedules  
- Locate nearby stations  
- Plan efficient travel itineraries  

---

## 📱 Tech Stack

### 🧩 Frontend (Android)
- **Language**: Kotlin  
- **UI Framework**: Jetpack Compose  
- **Architecture**: MVVM  
- **HTTP Client**: Retrofit  
- **Local Database**: Room (SQLite-based persistence)  

### 🖥 Backend
- **Language**: Python  
- **API**: RESTful endpoints providing station, train, and schedule data  
- *(The backend serves as a data provider for the Android app via Retrofit integration)*

---

## 🔍 Core Features

- Search for optimal train routes between Italian railway stations  
- Real-time display of departure and arrival times  
- Itinerary suggestions based on time, distance, and other travel constraints  
- Offline storage of recent searches and itineraries via Room  
- Clean, responsive, and intuitive UI with Jetpack Compose  
- Communication with a Python-based backend via Retrofit  

---

## 🧠 Custom Graph-Based Algorithm

The app models the Italian railway network as a **weighted directed graph**, where each station is a node and each train route is an edge.

A **custom implementation of Dijkstra's algorithm** is used to compute the optimal route between two stations, considering dynamic weights such as:

- Departure time  
- Travel duration  
- Waiting time between connections  
- Number of transfers  

---

## 👥 Development Team

- **[Akito0011]** – UI/UX design and frontend development using Jetpack Compose  
- **M.M.** – Backend integration, data flow architecture, and graph algorithm implementation  

---

## 🎓 Academic Context

This app was created as part of the course **Web and Mobile Programming**, academic year **2023/2024** in **University of Palermo**.  
Its goal was to apply modern mobile development practices and integrate backend services and graph-based routing algorithms.

---

## 🚀 Getting Started

### 🔧 Prerequisites
- Android Studio (Electric Eel or later)
- Android 8+ emulator or physical device

### 📦 Clone the repository
```bash
git clone https://github.com/your-username/GreedyRails.git
