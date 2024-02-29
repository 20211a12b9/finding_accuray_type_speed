import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

class TypingSpeedAccuracy {

    private JFrame frame = new JFrame("Typing Speed & Accuracy Test");
    private JPanel typingPanel = new JPanel();
    private JTextArea sentenceToType = new JTextArea(5, 30);
    private JTextArea userInputArea = new JTextArea(5, 30);
    private JButton startButton = new JButton("Start Test");
    private JButton resetButton = new JButton("Reset");
    private JLabel accuracyLabel = new JLabel("Accuracy: ");
    private JLabel wpmLabel = new JLabel("Words per Minute: ");

    private Timer timer;
    private TimerTask task;
    private boolean testStarted = false;
    private int correctCharCount = 0;
    private int totalCharCount = 0;
    private int typedWordCount = 0;
    private int elapsedTimeInSeconds = 0;

    public TypingSpeedAccuracy() {
        typingPanel.setLayout(new GridLayout(0, 1));
        typingPanel.add(new JLabel("Type the given sentence below:"));
        typingPanel.add(sentenceToType);
        typingPanel.add(new JLabel("Your typing:"));
        typingPanel.add(userInputArea);
        typingPanel.add(startButton);
        typingPanel.add(resetButton);
        typingPanel.add(accuracyLabel);
        typingPanel.add(wpmLabel);

        resetButton.setEnabled(false);
        userInputArea.setEditable(false);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTest();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTest();
            }
        });

        frame.getContentPane().add(typingPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void startTest() {
        if (!testStarted) {
            testStarted = true;
            startButton.setEnabled(false);
            resetButton.setEnabled(true);
            userInputArea.setEditable(true);
            sentenceToType.setText(getRandomSentence());
            userInputArea.requestFocus();

            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    elapsedTimeInSeconds++;
                    updateStats();
                }
            };
            timer.scheduleAtFixedRate(task, 1000, 1000);
        }
    }

    private void resetTest() {
        testStarted = false;
        startButton.setEnabled(true);
        resetButton.setEnabled(false);
        userInputArea.setEditable(false);
        sentenceToType.setText("");
        userInputArea.setText("");
        elapsedTimeInSeconds = 0;
        correctCharCount = 0;
        totalCharCount = 0;
        typedWordCount = 0;
        timer.cancel();
        updateStats();
    }

    private String getRandomSentence() {
        String[] sentences = {
                "The quick brown fox jumps over the lazy dog.",
                "Jackdaws love my big sphinx of quartz.",
                "How razorback-jumping frogs can level six piqued gymnasts!",
                "Pack my box with five dozen liquor jugs.",
                "Mr. Jock, TV quiz PhD, bags few lynx.",
                "Cwm fjord bank glyphs vext quiz."
        };
        Random rand = new Random();
        return sentences[rand.nextInt(sentences.length)];
    }

    private void updateStats() {
        String typedText = userInputArea.getText();
        totalCharCount = typedText.length();
        correctCharCount = 0;

        String[] typedWords = typedText.split("\\s+");
        typedWordCount = typedWords.length;

        String[] sentenceWords = sentenceToType.getText().split("\\s+");
        int minWordCount = Math.min(typedWordCount, sentenceWords.length);

        for (int i = 0; i < minWordCount; i++) {
            if (typedWords[i].equals(sentenceWords[i])) {
                correctCharCount += typedWords[i].length();
            }
        }

        int accuracy = (int) (((double) correctCharCount / totalCharCount) * 100);
        accuracyLabel.setText("Accuracy: " + accuracy + "%");

        int wpm = (int) (((double) typedWordCount / elapsedTimeInSeconds) * 60);
        wpmLabel.setText("Words per Minute: " + wpm);
    }

    public static void main(String[] args) {
        new TypingSpeedAccuracy();
    }
}
