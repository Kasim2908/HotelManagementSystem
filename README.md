🌹 Rose View Hotel - Java Hotel Management System
This is a desktop-based Hotel Management System built using Java Swing and AWT. It allows hotel staff to:

View available rooms

Book rooms for customers

Check out customers

View booked customer details

Enforce 10-digit mobile number validation

Display formatted information with styling

📸 Screenshot
(You can add a screenshot here of your app GUI once available)

✨ Features
🔍 View all available rooms

🧾 Book a room for a customer

📤 Check out from a room and calculate charges

🧑 View all current customers with contact, room, and price details

📵 Enforces exactly 10-digit mobile number input using real-time input filtering

💄 Styled interface using Java Swing (colored buttons, headers, formatted info)

📦 Project Structure
bash
Copy
Edit
HotelManagementSystem/
│
├── HotelManagementSystem.java   # Main GUI class
├── RoomManager.java             # Logic for managing rooms and bookings
├── Room.java                    # Model for a hotel room
├── Customer.java                # Model for customer details
├── README.md                    # Project documentation
✅ Requirements
Java JDK 8 or higher

No external libraries required (built with core Java AWT & Swing)

🚀 How to Run
Clone or download this repository.

Compile all .java files:

bash
Copy
Edit
javac *.java
Run the main class:

bash
Copy
Edit
java HotelManagementSystem
🛠 Technologies Used
Java SE (Swing, AWT)

Object-Oriented Programming (OOP)

Event-driven GUI design

📋 Notes
The mobile number is limited to exactly 10 digits. The user cannot type more or less.

All data is stored in memory (not persisted between runs unless connected to a database).

Designed for small hotel or educational project scenarios.

📚 Future Improvements
Connect to MySQL or another database for persistence

Add user login functionality

Add date/time and multi-day booking

Implement search/filter in customer viewer
