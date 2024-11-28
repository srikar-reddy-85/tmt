package com.tmt.mkt;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final String ANSI_RED = "\u001B[31m";
  private static final String CLEAR_SCREEN = "\033[H\033[2J";

  private Screen screen;
  private WindowBasedTextGUI textGUI;
  private BasicWindow window;
  private List<String> words;
  private String targetText;
  private char[] userInput;
  private int currentPosition;
  private long startTime;
  private boolean testRunning;
  private int testDurationSeconds;
  private boolean includeSpecialChars;
  private boolean includeUppercase;
  private boolean includePunctuation;
  private boolean includeLowercase;

  public App() throws IOException {
    DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
    screen = new TerminalScreen(terminalFactory.createTerminal());
    screen.setCursorPosition(new TerminalPosition(0, 0));
    screen.startScreen();
    textGUI = new MultiWindowTextGUI(screen);
    window = new BasicWindow("Typing Test");
    words = loadWords();
  }

  private List<String> loadWords() {
    return Arrays.asList("the", "be", "to", "of", "and", "a", "in", "that",
        "have", "I", "it", "for", "not", "on", "with", "he",
        "as", "you", "do", "at", "this", "but", "his", "by",
        "from", "they", "we", "say", "her", "she");
  }

  public void start() throws IOException {
    Panel panel = new Panel();
    panel.setLayoutManager(new GridLayout(2));

    Label label = new Label("Enter test duration in seconds (or 'q' to quit): ");
    final TextBox textBox = new TextBox();

    panel.addComponent(label);
    panel.addComponent(textBox.withBorder(Borders.singleLine("Input")));

    Button button = new Button("Start", () -> {
      String input = textBox.getText();
      if ("q".equalsIgnoreCase(input)) {
        try {
          screen.stopScreen();
        } catch (Exception e) {
          e.printStackTrace();
        }
        System.exit(0);
      }

      try {
        testDurationSeconds = Integer.parseInt(input);
        showOptionsDialog();
      } catch (NumberFormatException e) {
        new MessageDialogBuilder()
            .setTitle("Error")
            .setText("Invalid input. Please enter a number.")
            .addButton(MessageDialogButton.Close)
            .build()
            .showDialog(textGUI);
      }
    });

    panel.addComponent(new EmptySpace(new TerminalSize(0, 0)));
    panel.addComponent(button);

    window.setComponent(panel);
    textGUI.addWindowAndWait(window);
  }

  private void showOptionsDialog() {
    Panel optionsPanel = new Panel();
    optionsPanel.setLayoutManager(new GridLayout(2));

    CheckBox specialCharsCheckBox = new CheckBox("Include special characters");
    CheckBox uppercaseCheckBox = new CheckBox("Include uppercase letters");
    CheckBox punctuationCheckBox = new CheckBox("Include punctuation");
    CheckBox lowercaseCheckBox = new CheckBox("Include lowercase letters");

    optionsPanel.addComponent(specialCharsCheckBox);
    optionsPanel.addComponent(uppercaseCheckBox);
    optionsPanel.addComponent(punctuationCheckBox);
    optionsPanel.addComponent(lowercaseCheckBox);

    Button startButton = new Button("Start Test", () -> {
      includeSpecialChars = specialCharsCheckBox.isChecked();
      includeUppercase = uppercaseCheckBox.isChecked();
      includePunctuation = punctuationCheckBox.isChecked();
      includeLowercase = lowercaseCheckBox.isChecked();
      try {
        runTest();
        showResults();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    optionsPanel.addComponent(new EmptySpace(new TerminalSize(0, 0)));
    optionsPanel.addComponent(startButton);

    BasicWindow optionsWindow = new BasicWindow("Test Options");
    optionsWindow.setComponent(optionsPanel);
    textGUI.addWindowAndWait(optionsWindow);
  }

  private void runTest() throws IOException {
    generateText(30);
    userInput = new char[targetText.length()];
    Arrays.fill(userInput, '\0');
    currentPosition = 0;
    testRunning = true;
    startTime = System.currentTimeMillis();

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.schedule(() -> {
      testRunning = false;
      try {
        screen.readInput();
      } catch (IOException e) {
      }
    }, testDurationSeconds, TimeUnit.SECONDS);
    //
    // while (testRunning) {
    // clearScreen();
    // displayText();
    //
    // KeyStroke keyStroke = screen.readInput();
    // if (keyStroke != null) {
    // char ch = keyStroke.getCharacter();
    // if (ch == 3)
    // break; // Ctrl+C
    // else if (ch == 127 && currentPosition > 0) { // Backspace
    // currentPosition--;
    // userInput[currentPosition] = '\0';
    // } else if (ch >= 32 && ch <= 126 && currentPosition < targetText.length()) {
    // userInput[currentPosition] = ch;
    // currentPosition++;
    // }
    // }
    // }
    while (testRunning) {
      clearScreen();
      displayText();

      KeyStroke keyStroke = screen.readInput();
      if (keyStroke != null) {
        Character ch = keyStroke.getCharacter();
        switch (keyStroke.getKeyType()) {
          case Backspace:
            if (currentPosition > 0) {
              currentPosition--;
              userInput[currentPosition] = '\0';
            }
            break;
          case Character:
            if (ch != null && ch >= 32 && ch <= 126 && currentPosition < targetText.length()) {
              userInput[currentPosition] = ch;
              currentPosition++;
            }
            break;
          case EOF:
            testRunning = false;
            break;
          default:
            break;
        }
      }
    }

    executor.shutdownNow();
  }

  private void displayText() {
    long remainingTime = Math.max(0, testDurationSeconds -
        (System.currentTimeMillis() - startTime) / 1000);
    System.out.println("Time remaining: " + remainingTime + "s\n");

    for (int i = 0; i < targetText.length(); i++) {
      if (userInput[i] != '\0') {
        if (userInput[i] == targetText.charAt(i)) {
          System.out.print(ANSI_GREEN + userInput[i] + ANSI_RESET);
        } else {
          System.out.print(ANSI_RED + userInput[i] + ANSI_RESET);
        }
      } else if (i == currentPosition) {
        System.out.print("▌");
      } else {
        System.out.print(targetText.charAt(i));
      }
    }

    System.out.println("\n\nCurrent WPM: " + calculateWPM());
  }

  private int countCorrectChars() {
    int correct = 0;
    for (int i = 0; i < currentPosition; i++) {
      if (userInput[i] == targetText.charAt(i)) {
        correct++;
      }
    }
    return correct;
  }

  private void showResults() {
    int correctChars = countCorrectChars();
    double accuracy = currentPosition > 0 ? (double) correctChars / currentPosition * 100 : 0;

    new MessageDialogBuilder()
        .setTitle("Final Results")
        .setText("WPM: " + calculateWPM() + "\n" +
            "Accuracy: " + String.format("%.1f", accuracy) + "%\n" +
            "Characters typed: " + currentPosition + "\n" +
            "Correct characters: " + correctChars)
        .addButton(MessageDialogButton.Close)
        .build()
        .showDialog(textGUI);

    showExitDialog();
  }

  private void showExitDialog() {
    MessageDialogButton result = new MessageDialogBuilder()
        .setTitle("Exit")
        .setText("Do you want to exit the application?")
        .addButton(MessageDialogButton.Yes)
        .addButton(MessageDialogButton.No)
        .build()
        .showDialog(textGUI);

    if (result == MessageDialogButton.Yes) {
      try {
        screen.stopScreen();
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.exit(0);
    } else if (result == MessageDialogButton.No) {
      try {
        start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private int calculateWPM() {
    long elapsedTimeSeconds = Math.min(testDurationSeconds,
        (System.currentTimeMillis() - startTime) / 1000);
    if (elapsedTimeSeconds == 0)
      return 0;
    return (int) ((countCorrectChars() / 5.0) / (elapsedTimeSeconds / 60.0));
  }

  private void generateText(int wordCount) {
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < wordCount; i++) {
      String word = words.get(random.nextInt(words.size()));
      if (includeUppercase) {
        word = word.toUpperCase();
      } else if (includeLowercase) {
        word = word.toLowerCase();
      }
      if (includeSpecialChars) {
        word = addSpecialChars(word);
      }
      if (includePunctuation) {
        word = addPunctuation(word);
      }
      sb.append(word).append(" ");
    }
    targetText = sb.toString().trim();
  }

  private String addSpecialChars(String word) {
    Random random = new Random();
    char[] specialChars = { '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '=', '+', '[', ']', '{', '}', ';',
        ':', '<', '>', '.', ',' };
    StringBuilder sb = new StringBuilder(word);
    for (int i = 0; i < word.length(); i++) {
      if (random.nextBoolean()) {
        sb.insert(i, specialChars[random.nextInt(specialChars.length)]);
      }
    }
    return sb.toString();
  }

  private String addPunctuation(String word) {
    Random random = new Random();
    char[] punctuation = { '.', ',', '!', '?', ';', ':', '-', '–', '—', '(', ')', '[', ']', '{', '}', '<', '>', '/',
        '\\', '|', '#', '@', '$', '%', '^', '&', '*', '_', '~' };
    StringBuilder sb = new StringBuilder(word);
    for (int i = 0; i < word.length(); i++) {
      if (random.nextBoolean()) {
        sb.insert(i, punctuation[random.nextInt(punctuation.length)]);
      }
    }
    return sb.toString();
  }

  private void clearScreen() {
    System.out.print(CLEAR_SCREEN);
    System.out.flush();
  }

  public static void main(String[] args) {
    try {
      new App().start();
    } catch (IOException e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
}
