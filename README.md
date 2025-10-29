# Training Planner

The **Training Planner** is a Java-based desktop application designed to help users track their weekly workout progress. With features for analyzing sets, volume, and RPE (Rate of Perceived Exertion) per muscle group, this application makes it easy to monitor training performance and achieve fitness goals.

## Table of Contents

1. [Features](#features)
2. [Installation](#installation)
3. [Configuration](#configuration)
4. [Usage](#usage)

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
- **Data Export**:
  - Manually export your weekly training data to a PostgreSQL database, ensuring progress is saved for long-term analysis.

---

## Installation

### Prerequisites

- Java Development Kit (JDK) 17 or later
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
3. Configure the Database (see Configuration below)

4. Compile and run the application:
   ```bash
   mvn exec:java -Dexec.mainClass="MyFrame"
   ```

---

## Configuration

### Database Configuration (for PostgreSQL Export)

1. **Set Up PostgreSQL**: Make sure PostgreSQL is running and create a database named `TrainingData`. You can create the necessary table with the following SQL command:
   ```sql
   CREATE TABLE training_data (
       week INT,
       exercise VARCHAR(50),
       muscle_group VARCHAR(50),
       sets INT,
       reps INT,
       rpe DOUBLE PRECISION
   );
   ```

2. **Secure Database Credentials**: Add your database password in a `.env` file located in the project root (same directory as `pom.xml`):
   ```plaintext
   DB_PASSWORD=your_database_password
   ```

3. **Environment Variable**: Ensure that `.env` is included in `.gitignore` to keep it secure:
   ```plaintext
   # Environment variables
   .env
   ```

4. **Load Environment Variable**: The application will read the `DB_PASSWORD` from the `.env` file, so make sure you have this file correctly configured before running the application.

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

### Exporting Data to PostgreSQL

1. Click the **"Export"** button to save your weekly training data to the PostgreSQL database. This stores data such as week number, exercises, sets, reps, and RPE.

---

## Acknowledgements

- **FlatLaf** for a modern, flat look and feel.
- **JFreeChart** for the charting functionality.
- **dotenv-java** for secure management of environment variables.
