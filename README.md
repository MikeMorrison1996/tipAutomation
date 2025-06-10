# Tip Automation

This simple Java application calculates tip share based on the hours worked by each employee. A small Swing GUI allows entering names and hours, while total tips for the week are used to distribute the tips proportionally. Results are stored in a local MongoDB database named `TipAutomation`.

## Features
* Add employee names and hours worked via a GUI.
* Input total tips for the week.
* Tip share is calculated with the formula:

```
share = (hours worked by employee / total hours worked by everyone) * total tips
```

* Each calculation is saved to MongoDB in a collection called `tip_records`.

## Running
1. Ensure a MongoDB instance is running locally and that the `TipAutomation` database exists.
2. Compile the program (MongoDB Java driver is required on the classpath):

```bash
javac -cp path/to/mongo-driver.jar:. src/TipCalculatorGUI.java
```

3. Run the application:

```bash
java -cp path/to/mongo-driver.jar:. src.TipCalculatorGUI
```

The original console-based tool remains in `TipCalculator.java` for quick use without a GUI.
