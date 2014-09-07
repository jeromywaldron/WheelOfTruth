/**
 * @author Nikita Kouevda, Jenny Shen
 * @date 2013/10/05
 */

package wof.gui;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import wof.game.WheelOfFortuneGame;

public class WheelOfFortuneWheelPanel extends JPanel {
    private static final String IMAGES_DIR = "/wof/images/",
            SOUNDS_DIR = "/wof/sounds/";

    private static final String[] IMAGE_NAMES;

    private static final Map<String, Color> WHEEL_COLORS;

    private static final int DEGREES_EACH = 20;

    private final Map<String, Image> IMAGES;

    private final AudioClip SPINNING_WHEEL_CLIP, GOOD_GUESS_CLIP,
            BAD_GUESS_CLIP, BANKRUPT_CLIP, NO_MORE_VOWELS_CLIP;

    private WheelOfFortuneGame game;

    private WheelOfFortuneTopPanel topPanel;

    private WheelOfFortunePuzzlePanel puzzlePanel;

    private Timer wheelTimer;

    private ButtonListener buttonListener;

    private List<String> imageNames;

    private String spaceLanded;

    private JPanel lettersPanel;

    private JButton[] letterButtons;

    private JButton spinWheel1, spinWheel2, solvePuzzle1, solvePuzzle2, buyVowel1, buyVowel2, newGame;

    private JTextArea statusArea;

    private boolean spinFlag1;

    static {
        // Store the image names for image construction later
        IMAGE_NAMES =
                new String[]{"300.png", "750.png", "500.png", "loseATurn.png", "250.png",
                        "1000.png", "600.png", "350.png", "950.png", "800.png",
                        "550.png", "450.png", "700.png", "650.png",
                        "900.png", "400.png", "850.png", "250.png"
                        // "bankrupt.png",
                        };

        WHEEL_COLORS = new HashMap<String, Color>();
        WHEEL_COLORS.put("300", Color.BLUE);
        WHEEL_COLORS.put("750", Color.RED);
        WHEEL_COLORS.put("500", Color.ORANGE);
        WHEEL_COLORS.put("loseATurn", Color.WHITE);
        WHEEL_COLORS.put("1000", Color.MAGENTA);
        WHEEL_COLORS.put("600", new Color(255, 107, 36));
        WHEEL_COLORS.put("350", new Color(192, 192, 192));
        WHEEL_COLORS.put("950", new Color(128, 64, 0));
        WHEEL_COLORS.put("800", new Color(128, 0, 255));
        WHEEL_COLORS.put("550", new Color(0, 128, 128));
        WHEEL_COLORS.put("450", new Color(255, 0, 128));
        WHEEL_COLORS.put("700", new Color(0, 128, 0));
        // WHEEL_COLORS.put("bankrupt", Color.BLACK);
        WHEEL_COLORS.put("650", Color.YELLOW);
        WHEEL_COLORS.put("250", Color.GREEN);
        WHEEL_COLORS.put("900", Color.PINK);
        WHEEL_COLORS.put("400", Color.GRAY);
        WHEEL_COLORS.put("850", Color.CYAN);
    }

    public WheelOfFortuneWheelPanel(WheelOfFortuneGame game,
                                    WheelOfFortuneTopPanel topPanel,
                                    WheelOfFortunePuzzlePanel puzzlePanel) {
        super();

        this.game = game;
        this.topPanel = topPanel;
        this.puzzlePanel = puzzlePanel;
        spinFlag1 = false;

        SPINNING_WHEEL_CLIP =
                Applet.newAudioClip(getClass().getResource(
                        SOUNDS_DIR + "spinningWheel.wav"));
        GOOD_GUESS_CLIP =
                Applet.newAudioClip(getClass().getResource(
                        SOUNDS_DIR + "goodGuess.wav"));
        BAD_GUESS_CLIP =
                Applet.newAudioClip(getClass().getResource(
                        SOUNDS_DIR + "badGuess.wav"));
        BANKRUPT_CLIP =
                Applet.newAudioClip(getClass().getResource(
                        SOUNDS_DIR + "bankrupt.wav"));
        NO_MORE_VOWELS_CLIP =
                Applet.newAudioClip(getClass().getResource(
                        SOUNDS_DIR + "noMoreVowels.wav"));

        // Store the toolkit for easier access and fewer calls
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();

        IMAGES = new HashMap<String, Image>();

        for (String imageName : IMAGE_NAMES) {
            IMAGES.put(
                    imageName,
                    defaultToolkit.getImage(getClass().getResource(
                            IMAGES_DIR + imageName)));
        }

        IMAGES.put(
                "arrow.png",
                defaultToolkit.getImage(getClass().getResource(
                        IMAGES_DIR + "arrow.png")));

        wheelTimer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = imageNames.get(0);
                imageNames.remove(0);
                imageNames.add(value);

                repaint();
            }
        });

        letterButtons = new JButton[26];
        buttonListener = new ButtonListener();

        for (int i = 0; i < letterButtons.length; i++) {
            letterButtons[i] = new JButton("" + (char) (i + 65));
            letterButtons[i].addActionListener(buttonListener);
            letterButtons[i].setEnabled(false);
        }

        lettersPanel = new JPanel();
        lettersPanel.setPreferredSize(new Dimension(300, 300));
        lettersPanel.setLayout(new GridLayout(6, 5, 2, 2));

        // Vowel buttons are red, consonant buttons are blue
        for (int i = 0; i < letterButtons.length; i++) {
            letterButtons[i].setBackground((i == 0 || i == 4 || i == 8
                    || i == 14 || i == 20) ? Color.RED : Color.BLUE);
            letterButtons[i].setFont(new Font("Tahoma", Font.PLAIN, 24));
            lettersPanel.add(letterButtons[i]);
        }


        spinWheel1 = new JButton("Spin: Team 1");
        spinWheel1.setFont(new Font("Tahoma", Font.PLAIN, 24));
        spinWheel1.addActionListener(buttonListener);

        spinWheel2 = new JButton("Spin: Team 2");
        spinWheel2.setFont(new Font("Tahoma", Font.PLAIN, 24));
        spinWheel2.addActionListener(buttonListener);

        solvePuzzle1 = new JButton("Solve: Team 1");
        solvePuzzle1.setFont(new Font("Tahoma", Font.PLAIN, 24));
        solvePuzzle1.addActionListener(buttonListener);

        solvePuzzle2 = new JButton("Solve: Team 2");
        solvePuzzle2.setFont(new Font("Tahoma", Font.PLAIN, 24));
        solvePuzzle2.addActionListener(buttonListener);

        buyVowel1 = new JButton("Vowel: Team 1");
        buyVowel1.setFont(new Font("Tahoma", Font.PLAIN, 24));
        buyVowel1.addActionListener(buttonListener);

        buyVowel2 = new JButton("Vowel: Team 2");
        buyVowel2.setFont(new Font("Tahoma", Font.PLAIN, 24));
        buyVowel2.addActionListener(buttonListener);

        newGame = new JButton("New Game");
        newGame.setFont(new Font("Tahoma", Font.PLAIN, 24));
        newGame.addActionListener(buttonListener);


        statusArea = new JTextArea();
        statusArea.setFont(new Font("Tahoma", Font.PLAIN, 24));
        statusArea.setEditable(false);
        statusArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);

        Box optionButtonsBox = Box.createVerticalBox();
        optionButtonsBox.add(spinWheel1);
        optionButtonsBox.add(Box.createVerticalStrut(15));
        optionButtonsBox.add(spinWheel2);
        optionButtonsBox.add(Box.createVerticalStrut(15));
        optionButtonsBox.add(solvePuzzle1);
        optionButtonsBox.add(Box.createVerticalStrut(15));
        optionButtonsBox.add(solvePuzzle2);
        optionButtonsBox.add(Box.createVerticalStrut(15));
        optionButtonsBox.add(buyVowel1);
        optionButtonsBox.add(Box.createVerticalStrut(15));
        optionButtonsBox.add(buyVowel2);
        optionButtonsBox.add(Box.createVerticalStrut(30));
        optionButtonsBox.add(newGame);
        optionButtonsBox.add(Box.createVerticalStrut(100));

        Box letterButtonsBox = Box.createVerticalBox();
        letterButtonsBox.add(lettersPanel);
        letterButtonsBox.add(Box.createVerticalStrut(10));
        letterButtonsBox.add(statusArea);
        letterButtonsBox.add(Box.createVerticalStrut(235));

        Box outsideBox = Box.createHorizontalBox();
        outsideBox.add(Box.createHorizontalStrut(20));
        outsideBox.add(optionButtonsBox);
        outsideBox.add(Box.createHorizontalStrut(550));
        outsideBox.add(letterButtonsBox);
        outsideBox.add(Box.createHorizontalStrut(20));
        outsideBox.setPreferredSize(new Dimension(1200, 500));

        add(outsideBox);
        setPreferredSize(new Dimension(1200, 500));

        newGame();
    }

    public void newGame() {
        statusArea.setText("Welcome to Wheel of Truth!\n"
                + "You may spin the wheel or solve the puzzle.");
        topPanel.resetValues();

        puzzlePanel.newGame();

        imageNames = new ArrayList<String>();

        for (String name : IMAGE_NAMES) {
            imageNames.add(name);
        }

        setEnabledConsonants(false);
        setEnabledVowels(false);
        spinWheel1.setEnabled(true);
        spinWheel2.setEnabled(true);
        solvePuzzle1.setEnabled(false);
        solvePuzzle2.setEnabled(false);

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g.create();

        // Draw each space
        for (int i = 0, degrees = 0; i < imageNames.size() / 2; ++i) {
            g2D.setColor(WHEEL_COLORS.get(imageNames.get(i).substring(0,
                    imageNames.get(i).indexOf('.'))));
            g2D.fillArc(260, 45, 480, 480, degrees, DEGREES_EACH);
            degrees += DEGREES_EACH;
        }

        // Set the origin and rotate before drawing the images
        g2D.translate(500, 285);
        g2D.rotate(Math.toRadians(-100));

        // Draw wheel spaces
        for (int i = 0; i < imageNames.size() / 2; ++i) {
            g2D.drawImage(IMAGES.get(imageNames.get(i)), -42, 0, this);
            g2D.rotate(Math.toRadians(-DEGREES_EACH));
        }

        // Reset origin
        g2D.translate(-390, -285);

        // Draw the arrow to indicate where the wheel stopped
        g.drawImage(IMAGES.get("arrow.png"), 480, 10, this);
    }

    private void setEnabledConsonants(boolean b) {
        for (int i = 0; i < letterButtons.length; ++i) {
            if (!(i == 0 || i == 4 || i == 8 || i == 14 || i == 20)) {
                letterButtons[i].setEnabled(b);
            }
        }
    }

    private void setEnabledVowels(boolean b) {
        letterButtons[0].setEnabled(b);
        letterButtons[4].setEnabled(b);
        letterButtons[8].setEnabled(b);
        letterButtons[14].setEnabled(b);
        letterButtons[20].setEnabled(b);
    }

    private void setEnabledGuessedLetters(boolean b) {
        for (char c : game.getGuessedLetters()) {
            letterButtons[c - 65].setEnabled(b);
        }
    }

    private void handleWin1() {
        game.revealPuzzle();
        puzzlePanel.repaint();

        JOptionPane.showMessageDialog(null,
                "Congratulations, you win $" + game.getScore1() + "!\n\n",
                "You Win!", JOptionPane.INFORMATION_MESSAGE);

        newGame();
    }

    private void handleWin2() {
        game.revealPuzzle();
        puzzlePanel.repaint();

        JOptionPane.showMessageDialog(null,
                "Congratulations, you win $" + game.getScore2() + "!\n\n",
                "You Win!", JOptionPane.INFORMATION_MESSAGE);

        newGame();
    }

    private void handleLoss(String message) {
        game.revealPuzzle();
        puzzlePanel.repaint();

        JOptionPane.showMessageDialog(null, message + "\nSorry, you lose.",
                "You Lose!", JOptionPane.INFORMATION_MESSAGE);

        newGame();
    }

    private void enableSpinWheel() {
        spinWheel1.setEnabled(true);
        spinWheel2.setEnabled(true);
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();

            for (int c = 0; c < letterButtons.length; c++) {
                if (source == letterButtons[c]) {
                    int occurrences = game.revealLetter((char) (c + 65));


                    if (c == 0 || c == 4 || c == 8 || c == 14 || c == 20) {
                        // Subtract $200 (flat rate for vowels)
                        if (spinFlag1) {
                            topPanel.addScore1(-200);
                        }
                        else {
                            topPanel.addScore2(-200);
                        }

                        if (occurrences > 0) {
                            game.revealLetter((char) (c + 65));
                            GOOD_GUESS_CLIP.play();

                            statusArea.setText("There "
                                    + (occurrences == 1 ? "is" : "are") + " "
                                    + occurrences + " " + (char) (c + 65)
                                    + (occurrences == 1 ? "" : "'s")
                                    + " in the puzzle! Please spin the"
                                    + "wheel or buy another vowel.");
                        } else {
                            BAD_GUESS_CLIP.play();

                            statusArea.setText("Sorry, there are no "
                                    + (char) (c + 65) + "'s in the puzzle. "
                                    + "Please spin the wheel or buy another "
                                    + "vowel.");
                        }

                        if (game.isAllVowelsGuessed()) {
                            NO_MORE_VOWELS_CLIP.play();

                            game.disableVowels();

                            JOptionPane.showMessageDialog(null,
                                    "There are no more vowels left in "
                                            + "the puzzle.", "No More Vowels!",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        spinWheel1.setEnabled(true);
                        spinWheel2.setEnabled(true);

                        if (occurrences > 0) {
                            game.revealLetter((char) (c + 65));
                            GOOD_GUESS_CLIP.play();

                            int amount =
                                    Integer.parseInt(spaceLanded.substring(0,
                                            spaceLanded.indexOf('.')));
                            if (spinFlag1) {
                                topPanel.addScore1(amount * occurrences);
                            }
                            else {
                                topPanel.addScore2(amount * occurrences);
                            }

                            statusArea.setText("There "
                                    + (occurrences == 1 ? "is" : "are") + " "
                                    + occurrences + " " + (char) (c + 65)
                                    + (occurrences == 1 ? "" : "'s")
                                    + " in the puzzle! You earn $" + amount
                                    * occurrences
                                    + "! Please spin the wheel again "
                                    + "or buy a vowel.");
                        } else {
                            BAD_GUESS_CLIP.play();

                            statusArea.setText("Sorry, there are no "
                                    + (char) (c + 65) + "'s in the puzzle. "
                                    + "Please spin the wheel again or buy a "
                                    + "vowel.");
                        }
                    }

                    setEnabledConsonants(false);
                }
            }

            if (source == buyVowel1) {
                spinFlag1 = true;
                setEnabledVowels(true);
                setEnabledGuessedLetters(false);
            }

            if (source == buyVowel2) {
                spinFlag1 = false;
                setEnabledVowels(true);
                setEnabledGuessedLetters(false);
            }


            if (source == spinWheel1) {
                String cmd = e.getActionCommand();

                if (cmd.equals("Spin: Team 1")) {
                    spinFlag1 = true;

                    SPINNING_WHEEL_CLIP.loop();

                    solvePuzzle1.setEnabled(false);
                    solvePuzzle2.setEnabled(false);
                    setEnabledVowels(false);

                    wheelTimer.start();
                    statusArea.setText("The wheel is spinning...");
                    spinWheel1.setText("Stop Wheel");
                } else if (cmd.equals("Stop Wheel")) {
                    SPINNING_WHEEL_CLIP.stop();
                    wheelTimer.stop();
                    solvePuzzle1.setEnabled(true);
                    spinWheel1.setText("Spin: Team 1");
                    spaceLanded = imageNames.get(4);

                    if (spaceLanded.equals("loseATurn.png")) {
                        statusArea.setText("Sorry, you lose a turn.");
                        enableSpinWheel();
                        setEnabledConsonants(false);
                        setEnabledGuessedLetters(false);
                    } else if (spaceLanded.equals("bankrupt.png")) {
                        BANKRUPT_CLIP.play();

                        topPanel.resetScore1();
                        statusArea.setText("Sorry, you lose a turn, and "
                                + "your score has been brought down to 0.");
                        enableSpinWheel();
                        setEnabledConsonants(false);
                        setEnabledGuessedLetters(false);
                    } else {
                        spinWheel1.setEnabled(false);
                        spinWheel2.setEnabled(false);
                        statusArea.setText("Please select a consonant team 1.");

                        setEnabledConsonants(true);
                        setEnabledGuessedLetters(false);
                    }
                }
            } else if (source == spinWheel2) {
                String cmd = e.getActionCommand();

                if (cmd.equals("Spin: Team 2")) {
                    spinFlag1 = false;

                    SPINNING_WHEEL_CLIP.loop();

                    solvePuzzle1.setEnabled(false);
                    solvePuzzle2.setEnabled(false);
                    setEnabledVowels(false);

                    wheelTimer.start();
                    statusArea.setText("The wheel is spinning...");
                    spinWheel2.setText("Stop Wheel");
                } else if (cmd.equals("Stop Wheel")) {
                    SPINNING_WHEEL_CLIP.stop();
                    wheelTimer.stop();
                    solvePuzzle2.setEnabled(true);
                    spinWheel2.setText("Spin: Team 2");
                    spaceLanded = imageNames.get(4);

                    if (spaceLanded.equals("loseATurn.png")) {
                        statusArea.setText("Sorry, you lose a turn.");
                        enableSpinWheel();
                        setEnabledConsonants(false);
                        setEnabledGuessedLetters(false);
                    } else if (spaceLanded.equals("bankrupt.png")) {
                        BANKRUPT_CLIP.play();

                        topPanel.resetScore2();
                        statusArea.setText("Sorry, you lose a turn, and "
                                + "your score has been brought down to 0.");
                        enableSpinWheel();
                        setEnabledConsonants(false);
                        setEnabledGuessedLetters(false);
                    } else {
                        spinWheel1.setEnabled(false);
                        spinWheel2.setEnabled(false);
                        statusArea.setText("Please select a consonant team 2");

                        setEnabledConsonants(true);
                        setEnabledVowels(false);
                        setEnabledGuessedLetters(false);
                    }
                }
            } else if (source == solvePuzzle1) {
                String solveAttempt =
                        JOptionPane.showInputDialog(null,
                                "Please solve the puzzle:", "Solve the Puzzle",
                                JOptionPane.PLAIN_MESSAGE);
                StringBuilder trimmedAttempt = new StringBuilder();

                String phrase = game.getPhrase();
                StringBuilder trimmedPhrase = new StringBuilder();

                for (int i = 0; i < phrase.length(); ++i) {
                    if (phrase.charAt(i) != ' ') {
                        trimmedPhrase.append(phrase.charAt(i));
                    }
                }

                if (solveAttempt != null) {
                    for (int i = 0; i < solveAttempt.length(); ++i) {
                        if (solveAttempt.charAt(i) != ' ') {
                            trimmedAttempt.append(solveAttempt.charAt(i));
                        }
                    }
                }

                if (trimmedAttempt.toString() != "") {
                    if (trimmedAttempt.toString().compareToIgnoreCase(
                            trimmedPhrase.toString()) == 0) {
                        handleWin1();
                    } else {
                        handleLoss("That is incorrect.");
                    }
                }
            } else if (source == solvePuzzle2) {
                String solveAttempt =
                        JOptionPane.showInputDialog(null,
                                "Please solve the puzzle:", "Solve the Puzzle",
                                JOptionPane.PLAIN_MESSAGE);
                StringBuilder trimmedAttempt = new StringBuilder();

                String phrase = game.getPhrase();
                StringBuilder trimmedPhrase = new StringBuilder();

                for (int i = 0; i < phrase.length(); ++i) {
                    if (phrase.charAt(i) != ' ') {
                        trimmedPhrase.append(phrase.charAt(i));
                    }
                }

                if (solveAttempt != null) {
                    for (int i = 0; i < solveAttempt.length(); ++i) {
                        if (solveAttempt.charAt(i) != ' ') {
                            trimmedAttempt.append(solveAttempt.charAt(i));
                        }
                    }
                }

                if (trimmedAttempt.toString() != "") {
                    if (trimmedAttempt.toString().compareToIgnoreCase(
                            trimmedPhrase.toString()) == 0) {
                        handleWin2();
                    } else {
                        handleLoss("That is incorrect.");
                    }
                }

            } else if (source == newGame) {
                newGame();
            }

                if (game.isSolved()) {
                    handleWin1();
                }

                puzzlePanel.repaint();
            }
        }
    }

