# 🚄 GreedyRails

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blueviolet?logo=kotlin)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-Jetpack%20Compose-3DDC84?logo=android)](https://developer.android.com/jetpack/compose)
[![Project Type](https://img.shields.io/badge/University%20Project-Yes-blue)]()

**GreedyRails** is a mobile application developed as a **university project** for the course **Web and Mobile Programming**.  
It serves as a **train travel planner for Italy**, allowing users to:

- Search for rail routes  
- View train schedules  
- Find nearby stations  
- Plan optimized itineraries  

---

## 📱 Tech Stack

- **Language**: Kotlin  
- **Framework**: Jetpack Compose  
- **Architecture**: MVVM  
- **Algorithm**: Custom implementation of Dijkstra's algorithm  

---

## 🔍 Core Features

- Search for optimal train routes between stations in Italy  
- View real-time departure and arrival times  
- Receive itinerary suggestions based on weighted factors  
- Smooth and responsive UI built with Jetpack Compose  
- Efficient and reactive data flow management  

---

## 🧠 Custom Graph-Based Algorithm

At the heart of the application lies a **graph-based model** of the Italian railway network.  
We use a **custom version of Dijkstra's algorithm** where edge weights are dynamically calculated based on:

- Departure times  
- Travel durations  
- Transfers and layovers  

Given two station nodes, the app finds the **most time-efficient route** between them.

---

## 👥 Development Team

- **[Your Name]** – Developed the **frontend**, UI, and layout using Jetpack Compose  
- **M.M.** – Responsible for **data flow management** and **graph algorithm implementation**  

---

## 🎓 Academic Context

This app was developed as part of the **Web and Mobile Programming** course during the academic year **20XX/20XX**.  
It demonstrates the application of modern Android development practices, algorithmic planning, and data-driven UIs.

---

## 📸 Screenshots

*(You can add screenshots or GIFs of the app here)*

---

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/GreedyRails.git
