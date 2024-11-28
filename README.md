# TMT: Terminal MonkeyType

**TMT** is a terminal-based typing test application designed to improve your typing speed and accuracy while offering a fun and customizable experience. Inspired by MonkeyType, this project is built with Java and uses the Lanterna library to create a user-friendly terminal interface.

---

## Features 🎯

### Current Features:
1. **Customizable Typing Tests**:
   - Set test duration in seconds.
   - Choose to include special characters, uppercase letters, punctuation, or lowercase letters in the test text.

2. **Typing Test Execution**:
   - Real-time feedback on typed text, with:
     - **Green color** for correct characters.
     - **Red color** for incorrect characters.
     - A live caret to indicate your typing position.
   - Displays current Words Per Minute (WPM) during the test.

3. **Detailed Results**:
   - Final WPM calculation.
   - Accuracy percentage.
   - Total characters typed and correct characters count.

4. **Interactive Dialogs**:
   - Error handling for invalid inputs.
   - Options to restart or exit the application gracefully.

---

## Planned Features 🌟
Help us improve the project by contributing to the following: 
- currently it only runs on linux systems properly i want it to run on any terminal 
- **Improved UI**: Add more aesthetic terminal decorations, dynamic themes, or animations.
- **Leaderboard Integration**: Track and save scores locally or online to compete with friends.
- **Multiplayer Typing Test**: Challenge others in real-time.
- **Custom Text Import**: Allow users to upload custom text files for the typing test.
- **Support for Different Languages**: Extend support to include typing tests in multiple languages.
- **Progress Tracking**: Save user progress and display typing improvements over time.
- **Keyboard Layout Support**: Offer tests tailored to different keyboard layouts (e.g., QWERTY, Dvorak, AZERTY).

---

## Prerequisites 🛠️

- **Java Development Kit (JDK)**: Version 11 or later.
- Maven.
- **Lanterna Library**: Already included in the project dependencies.

---

## How to Run Locally 💻

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/srikar-reddy-85/tmt.git
   cd tmt
   ```

2. **Compile the Project**:
   Make sure you have the `lanterna` library installed or included in your classpath. Then, compile the Java files:
   ```bash
   mvn --version //if not installed install maven
   mvn package
   ```

3. **Run the Application**:
   ```bash
   java -jar target/mkt-1.0-SNAPSHOT.jar
   ```

   Replace `path/to/lanterna.jar` with the actual path to the Lanterna library.

4. **Follow On-Screen Instructions**:
   - Set test duration and choose typing test options.
   - Type as fast and accurately as possible to achieve your best WPM score.

---

## Contributing 🤝

We’re open to contributions! Here’s how you can help:
1. **Improve the UI**:
   - Suggestions for better terminal layout and styling.
   - Adding themes or dynamic visuals.
2. **Enhance Features**:
   - Implement planned features listed above.
   - Suggest and develop new functionality.

3. **Submit Issues**:
   - Report bugs or inconsistencies.
   - Share feedback for further improvements.

---

## Asking for Help ❓

If you encounter any issues or have feature requests, feel free to:
- Open a GitHub issue.
- Contact the project maintainer directly.

---

## Author ✍️
Created by **[SRIKAR]**  
Feel free to contribute and make **TMT** the best typing test application for terminal enthusiasts!

---

Enjoy testing and improving your typing skills with **TMT: Terminal MonkeyType**! 🚀
