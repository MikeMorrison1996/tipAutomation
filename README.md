
# TipAutomation

A Java desktop application that calculates tip shares among employees and saves results locally using SQLite.

## Features

- Simple Swing-based GUI for entering names, hours worked, and total tips  
- Calculates each person’s proportional tip share and displays it in a table  
- Persists each calculation in a local SQLite database (`TipsJune2025` table)  
- Modular architecture separating GUI and database logic  

## Requirements

- Java 11 or newer  
- `sqlite-jdbc` driver added to your project classpath  

## Setup & Run

1. Clone the repository using:
   ```bash
   git clone https://github.com/MikeMorrison1996/tipAutomation.git
````

2. Add the SQLite JDBC driver JAR via your IDE’s Project Structure → Libraries
3. Compile and launch the app:

   ```bash
   javac -cp .;sqlite-jdbc-*.jar TipApp.java TipCalculatorGUI.java DBConnection.java
   java -cp .;sqlite-jdbc-*.jar TipApp
   ```

The application will open a GUI. Enter employee data, calculate shares, and save the entries to `tipAutomation.db`.

## Project Structure


├── TipApp.java            # Entry point launching the GUI
├── TipCalculatorGUI.java  # Handles UI and business logic
├── DBConnection.java      # Manages SQLite database operations
└── tipAutomation.db       # SQLite DB file (created at first run)

