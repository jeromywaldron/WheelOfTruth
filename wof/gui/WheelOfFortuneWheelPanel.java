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

    private JButton spinWheel, solvePuzzle, newGame, howToPlay, about;

    private JTextArea statusArea;

    static {
        // Store the image names for image construction later
        IMAGE_NAMES =
            new String[] {"300.png", "750.png", "500.png", "loseATurn.png",
                "1000.png", "600.png", "350.png", "950.png", "800.png",
                "550.png", "450.png", "700.png", "bankrupt.png", "650.png",
                "250.png", "900.png", "400.png", "850.png"};

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
        WHEEL_COLORS.put("bankrupt", Color.BLACK);
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
            letterButtons[i] = new JButton("" + (char)(i + 65));
            letterButtons[i].addActionListener(buttonListener);
            letterButtons[i].setEnabled(false);
        }

        lettersPanel = new JPanel();
        lettersPanel.setPreferredSize(new Dimension(100, 200));
        lettersPanel.setLayout(new GridLayout(6, 5, 2, 2));

        // Vowel buttons are red, consonant buttons are blue
        for (int i = 0; i < letterButtons.length; i++) {
            letterButtons[i].setBackground((i == 0 || i == 4 || i == 8
                || i == 14 || i == 20) ? Color.RED : Color.BLUE);
            lettersPanel.add(letterButtons[i]);
        }

        spinWheel = new JButton("Spin Wheel");
        spinWheel.addActionListener(buttonListener);

        solvePuzzle = new JButton("Solve Puzzle");
        solvePuzzle.addActionListener(buttonListener);

        newGame = new JButton("New Game");
        newGame.addActionListener(buttonListener);

        howToPlay = new JButton("How to Play");
        howToPlay.addActionListener(buttonListener);

        about = new JButton("About");
        about.addActionListener(buttonListener);

        statusArea = new JTextArea();
        statusArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
        statusArea.setEditable(false);
        statusArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);

        Box optionButtonsBox = Box.createVerticalBox();
        optionButtonsBox.add(spinWheel);
        optionButtonsBox.add(Box.createVerticalStrut(15));
        optionButtonsBox.add(solvePuzzle);
        optionButtonsBox.add(Box.createVerticalStrut(60));
        optionButtonsBox.add(newGame);
        optionButtonsBox.add(Box.createVerticalStrut(15));
        optionButtonsBox.add(howToPlay);
        optionButtonsBox.add(Box.createVerticalStrut(15));
        optionButtonsBox.add(about);
        optionButtonsBox.add(Box.createVerticalStrut(250));

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
        outsideBox.setPreferredSize(new Dimension(900, 500));

        add(outsideBox);
        setPreferredSize(new Dimension(900, 300));

        newGame();
    }

    public void newGame() {
        statusArea.setText("Welcome to Wheel of Fortune!\n"
            + "You may spin the wheel or solve the puzzle.");
        topPanel.resetValues();

        puzzlePanel.newGame();

        imageNames = new ArrayList<String>();

        for (String name : IMAGE_NAMES) {
            imageNames.add(name);
        }

        setEnabledConsonants(false);
        setEnabledVowels(false);
        spinWheel.setEnabled(true);

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D)g.create();

        // Draw each space
        for (int i = 0, degrees = 0; i < imageNames.size() / 2; ++i) {
            g2D.setColor(WHEEL_COLORS.get(imageNames.get(i).substring(0,
                imageNames.get(i).indexOf('.'))));
            g2D.fillArc(150, 45, 480, 480, degrees, DEGREES_EACH);
            degrees += DEGREES_EACH;
        }

        // Set the origin and rotate before drawing the images
        g2D.translate(390, 285);
        g2D.rotate(Math.toRadians(-100));

        // Draw wheel spaces
        for (int i = 0; i < imageNames.size() / 2; ++i) {
            g2D.drawImage(IMAGES.get(imageNames.get(i)), -42, 0, this);
            g2D.rotate(Math.toRadians(-DEGREES_EACH));
        }

        // Reset origin
        g2D.translate(-390, -285);

        // Draw the arrow to indicate where the wheel stopped
        g.drawImage(IMAGES.get("arrow.png"), 370, 10, this);
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

    private void handleWin() {
        game.revealPuzzle();

        JOptionPane.showMessageDialog(null,
            "Congratulations, you win $" + game.getScore() + "!\n\n",
            "You Win!", JOptionPane.INFORMATION_MESSAGE);

        newGame();
    }

    private void handleLoss(String message) {
        game.revealPuzzle();

        JOptionPane.showMessageDialog(null, message + "\nSorry, you lose.",
            "You Lose!", JOptionPane.INFORMATION_MESSAGE);

        newGame();
    }

    private void showHowToPlay() {
        JOptionPane.showMessageDialog(null,
            "To guess a consonant, you must first spin the wheel (Press "
                + "\"Spin Wheel\" to\nspin it, and \"Stop Wheel\" to "
                + "stop it). You will be awarded the amount on the\n"
                + "space times the number of appearances of the "
                + "consonant in the puzzle. You\nlose a turn if the "
                + "consonant does not appear in the puzzle.\n\nYou may "
                + "buy a vowel at any time if you have at least $250. "
                + "Vowels cost a flat\nrate of $250; extra money will "
                + "not be deducted for multiple occurrences of the\n"
                + "vowel in the puzzle. If the vowel does not appear "
                + "in the puzzle, you lose a turn\nin addition to the "
                + "$250.\n\nThere are two non-cash amount spaces on "
                + "the wheel: \"Bankrupt\" and \"Lose a\nTurn\", both "
                + "of which make you lose a turn. In addition, "
                + "\"Bankrupt\" brings your\nscore down to 0.\n\nIn "
                + "order to win the game, you must solve the puzzle "
                + "within 7 turns. If you fail\nto do this, you lose. "
                + "You may solve the puzzle at any time during the "
                + "game,\nbut you lose if your guess is incorrect.",
            "How to Play", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(null,
            "Created by Nikita Kouevda and Jenny Shen\nOctober 5, 2013",
            "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton)e.getSource();

            for (int c = 0; c < letterButtons.length; c++) {
                if (source == letterButtons[c]) {
                    int occurrences = game.revealLetter((char)(c + 65));

                    if (c == 0 || c == 4 || c == 8 || c == 14 || c == 20) {
                        // Subtract $250 (flat rate for vowels)
                        topPanel.addScore(-250);

                        if (occurrences > 0) {
                            GOOD_GUESS_CLIP.play();

                            statusArea.setText("There "
                                + (occurrences == 1 ? "is" : "are") + " "
                                + occurrences + " " + (char)(c + 65)
                                + (occurrences == 1 ? "" : "'s")
                                + " in the puzzle! Please spin the"
                                + "wheel or buy another vowel.");
                        } else {
                            BAD_GUESS_CLIP.play();

                            topPanel.subtractTurn();

                            statusArea.setText("Sorry, there are no "
                                + (char)(c + 65) + "'s in the puzzle. "
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
                        spinWheel.setEnabled(true);

                        if (occurrences > 0) {
                            GOOD_GUESS_CLIP.play();

                            int amount =
                                Integer.parseInt(spaceLanded.substring(0,
                                    spaceLanded.indexOf('.')));

                            topPanel.addScore(amount * occurrences);

                            statusArea.setText("There "
                                + (occurrences == 1 ? "is" : "are") + " "
                                + occurrences + " " + (char)(c + 65)
                                + (occurrences == 1 ? "" : "'s")
                                + " in the puzzle! You earn $" + amount
                                * occurrences
                                + "! Please spin the wheel again "
                                + "or buy a vowel.");
                        } else {
                            BAD_GUESS_CLIP.play();
                            topPanel.subtractTurn();

                            statusArea.setText("Sorry, there are no "
                                + (char)(c + 65) + "'s in the puzzle. "
                                + "Please spin the wheel again or buy a "
                                + "vowel.");
                        }
                    }

                    setEnabledConsonants(false);
                    setEnabledVowels(game.getScore() >= 250);
                    setEnabledGuessedLetters(false);
                }
            }

            if (source == spinWheel) {
                String cmd = e.getActionCommand();

                if (cmd.equals("Spin Wheel")) {
                    SPINNING_WHEEL_CLIP.loop();

                    solvePuzzle.setEnabled(false);
                    setEnabledVowels(false);

                    wheelTimer.start();
                    statusArea.setText("The wheel is spinning...");
                    spinWheel.setText("Stop Wheel");
                } else if (cmd.equals("Stop Wheel")) {
                    SPINNING_WHEEL_CLIP.stop();
                    wheelTimer.stop();
                    solvePuzzle.setEnabled(true);
                    spinWheel.setText("Spin Wheel");
                    spaceLanded = imageNames.get(4);

                    if (spaceLanded.equals("loseATurn.png")) {
                        topPanel.subtractTurn();
                        statusArea.setText("Sorry, you lose a turn.");

                        setEnabledConsonants(false);
                        setEnabledVowels(game.getScore() >= 250);
                        setEnabledGuessedLetters(false);
                    } else if (spaceLanded.equals("bankrupt.png")) {
                        BANKRUPT_CLIP.play();

                        topPanel.subtractTurn();
                        topPanel.resetScore();
                        statusArea.setText("Sorry, you lose a turn, and "
                            + "your score has been brought down to 0.");

                        setEnabledConsonants(false);
                        setEnabledVowels(game.getScore() >= 250);
                        setEnabledGuessedLetters(false);
                    } else {
                        spinWheel.setEnabled(false);
                        statusArea.setText("Please select a consonant.");

                        setEnabledConsonants(true);
                        setEnabledVowels(false);
                        setEnabledGuessedLetters(false);
                    }
                }
            } else if (source == solvePuzzle) {
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
                        handleWin();
                    } else {
                        handleLoss("That is incorrect.");
                    }
                }
            } else if (source == newGame) {
                newGame();
            } else if (source == howToPlay) {
                showHowToPlay();
            } else if (source == about) {
                showAbout();
            }

            if (game.getTurnsLeft() == 0) {
                handleLoss("You have no turns left.");
            } else if (game.isSolved()) {
                handleWin();
            }

            puzzlePanel.repaint();
        }
    }
}
