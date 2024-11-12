# Training Planner

The **Training Planner** is a Java-based desktop application designed to help users track their weekly workout progress. With features for analyzing sets, volume, and RPE (Rate of Perceived Exertion) per muscle group, this application makes it easy to monitor training performance and achieve fitness goals.

## Table of Contents

1. [Features](#features)
2. [Installation](#installation)
3. [Usage](#usage)
4. [Contributing](#contributing)

---

## Features

- **CSV Import**: Load exercise data from a CSV file for quick setup.
- **Exercise Analysis**:
  - View the number of sets completed per muscle group.
  - See total volume and average RPE per muscle group for each week.
- **Progress Tracking**: 
  - Track your progress week-by-week.
  - Update and view completion percentages and sets targets.
- **Filtered Views**:
  - Select a muscle group to filter the displayed exercises and view relevant data only.
- **Data Export**: Manually export your weekly training data to ensure your progress is saved.

---

## Installation

### Prerequisites

- Java Development Kit (JDK) 8 or later
- [Maven](https://maven.apache.org/) (for managing dependencies)
- [JFreeChart Library](https://sourceforge.net/projects/jfreechart/) (for charting) – You can add it to your project by including the appropriate Maven dependency or adding the JAR to your project.

### Steps

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/training-planner.git
   cd training-planner
   ```

2. Install dependencies (if using Maven):
   ```bash
   mvn install
   ```

3. Compile and run the application:
   ```bash
   mvn exec:java -Dexec.mainClass="MyFrame"
   ```

4. To package the application:
   ```bash
   mvn package
   ```

---

## Usage

### Loading Data

1. Launch the application. 
2. Click the **"Upload CSV"** button to select a CSV file containing your exercises. The CSV format should look like this:
   ```
   ExerciseName;MuscleGroup;SetCount;Weight;Reps;RPE
   Bench Press;CHEST;3;100;10,10,10;8,8,8
   ```
3. The loaded data will populate the main window with analysis information.

### Filtering by Muscle Group

1. Select a muscle group from the drop-down menu to filter displayed exercises to that muscle group only.

### Exporting Data

1. Once ready, click the **"Export"** button to save your weekly training data for future reference.

---

## Contributing

We welcome contributions to improve this project! Here's how you can help:

1. **Fork** the repository.
2. **Create a branch** for your feature or bug fix:
   ```bash
   git checkout -b feature-name
   ```
3. **Commit** your changes:
   ```bash
   git commit -m "Add feature"
   ```
4. **Push** the branch:
   ```bash
   git push origin feature-name
   ```
5. Open a **Pull Request** with a description of your changes.

---

## Acknowledgements

- **FlatLaf** for a modern, flat look and feel.
- **JFreeChart** for the charting functionality.

