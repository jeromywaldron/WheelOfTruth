/**
 * @author Nikita Kouevda, Jenny Shen
 * @date 2012/06/02
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
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import wof.game.WheelOfFortuneGame;

public class WheelOfFortuneWheelPanel extends JPanel {
    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private static final String IMAGES_DIR = "/wof/images/",
            SOUNDS_DIR = "/wof/sounds/";

    private static final String[] IMAGE_NAMES;

    private static final HashMap<String, Color> WHEEL_COLORS;

    private static final int DEGREES_EACH = 20;

    private final HashMap<String, Image> IMAGES;

    private final AudioClip SPINNING_WHEEL_CLIP, GOOD_GUESS_CLIP,
            BAD_GUESS_CLIP, BANKRUPT_CLIP, NO_MORE_VOWELS_CLIP;

    private WheelOfFortuneGame myGame;

    private WheelOfFortuneTopPanel myTopPanel;

    private WheelOfFortunePuzzlePanel myPuzzlePanel;

    private Timer myWheelTimer;

    private ButtonListener myButtonListener;

    private ArrayList<String> myImageNames;

    private String mySpaceLanded;

    private JPanel lettersPanel;

    private JButton[] letterButtons;

    private JButton spinWheel, solvePuzzle, newGame, howToPlay, about;

    private JTextArea statusArea;

    // -------------------------------------------------------------------------
    // Initializers
    // -------------------------------------------------------------------------

    static {
        // Store the image names for image construction later
        IMAGE_NAMES =
                new String[]{"300.png", "750.png", "500.png", "loseATurn.png",
                    "1000.png", "600.png", "350.png", "950.png", "800.png",
                    "550.png", "450.png", "700.png", "bankrupt.png", "650.png",
                    "250.png", "900.png", "400.png", "850.png"};

        // Construct and initialize the map of names to colors
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

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public WheelOfFortuneWheelPanel(WheelOfFortuneGame game,
            WheelOfFortuneTopPanel topPanel,
            WheelOfFortunePuzzlePanel puzzlePanel) {
        super();

        myGame = game;
        myTopPanel = topPanel;
        myPuzzlePanel = puzzlePanel;

        // Load the sound clips
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

        // Store the toolkit for easier access and less calls
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();

        // Construct and initialize the map of names to images
        IMAGES = new HashMap<String, Image>();

        for (String imageName : IMAGE_NAMES)
            IMAGES.put(imageName, defaultToolkit.getImage(getClass()
                    .getResource(IMAGES_DIR + imageName)));

        IMAGES.put("arrow.png", defaultToolkit.getImage(getClass().getResource(
                IMAGES_DIR + "arrow.png")));

        // Construct the wheel timer
        myWheelTimer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Sends first space value of ArrayList to end
                String value = myImageNames.get(0);
                myImageNames.remove(0);
                myImageNames.add(value);

                // Repaint the wheel
                repaint();
            }
        });

        // Initialize letter buttons
        letterButtons = new JButton[26];

        // Initialize the button listener
        myButtonListener = new ButtonListener();

        // Construct the letter buttons
        for (int i = 0; i < letterButtons.length; i++) {
            letterButtons[i] = new JButton("" + (char)(i + 65));
            letterButtons[i].addActionListener(myButtonListener);
            letterButtons[i].setEnabled(false);
        }

        // Construct the panel of letters
        lettersPanel = new JPanel();
        lettersPanel.setPreferredSize(new Dimension(100, 200));
        lettersPanel.setLayout(new GridLayout(6, 5, 2, 2));

        // Set the color of the buttons and add them to the panel
        for (int i = 0; i < letterButtons.length; i++) {
            // Vowel buttons are red; consonant buttons are blue
            if (i == 0 || i == 4 || i == 8 || i == 14 || i == 20)
                letterButtons[i].setBackground(Color.RED);
            else
                letterButtons[i].setBackground(Color.BLUE);

            // Add this button to letters panel
            lettersPanel.add(letterButtons[i]);
        }

        // Construct the other buttons
        spinWheel = new JButton("Spin Wheel");
        spinWheel.addActionListener(myButtonListener);

        solvePuzzle = new JButton("Solve Puzzle");
        solvePuzzle.addActionListener(myButtonListener);

        newGame = new JButton("New Game");
        newGame.addActionListener(myButtonListener);

        howToPlay = new JButton("How to Play");
        howToPlay.addActionListener(myButtonListener);

        about = new JButton("About");
        about.addActionListener(myButtonListener);

        // Construct the status area and and set its properties
        statusArea = new JTextArea();
        statusArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
        statusArea.setEditable(false);
        statusArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);

        // Add all the buttons on the left side to a box
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

        // Add the panel of letters and the status area to a box
        Box letterButtonsBox = Box.createVerticalBox();
        letterButtonsBox.add(lettersPanel);
        letterButtonsBox.add(Box.createVerticalStrut(10));
        letterButtonsBox.add(statusArea);
        letterButtonsBox.add(Box.createVerticalStrut(235));

        // Add everything to a larger box
        Box outsideBox = Box.createHorizontalBox();
        outsideBox.add(Box.createHorizontalStrut(20));
        outsideBox.add(optionButtonsBox);
        outsideBox.add(Box.createHorizontalStrut(550));
        outsideBox.add(letterButtonsBox);
        outsideBox.add(Box.createHorizontalStrut(20));
        outsideBox.setPreferredSize(new Dimension(900, 500));

        // Add the outside box to this panel and set the preferred size
        add(outsideBox);
        setPreferredSize(new Dimension(900, 300));

        // Start a new game
        newGame();
    }

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------

    public void newGame() {
        // Reset text of status area
        statusArea.setText("Welcome to Wheel of Fortune!\n"
                + "You may spin the wheel or solve the puzzle.");

        // Reset score and turns
        myTopPanel.resetValues();

        // Create a new puzzle
        myPuzzlePanel.newGame();

        // Initialize the list of image names
        myImageNames = new ArrayList<String>();

        for (String name : IMAGE_NAMES)
            myImageNames.add(name);

        // Disable the letter buttons
        setEnabledConsonants(false);
        setEnabledVowels(false);

        // Enable the spin wheel button
        spinWheel.setEnabled(true);

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Create Graphics2D from Graphics
        Graphics2D g2D = (Graphics2D)g.create();

        // Where each space starts on the wheel
        int degrees = 0;

        // Draw each space
        for (int i = 0; i < myImageNames.size() / 2; i++) {
            g2D.setColor(WHEEL_COLORS.get(myImageNames.get(i).substring(0,
                    myImageNames.get(i).indexOf('.'))));
            g2D.fillArc(150, 45, 480, 480, degrees, DEGREES_EACH);
            degrees += DEGREES_EACH;
        }

        // Set the origin and rotate before drawing the images
        g2D.translate(390, 285);
        g2D.rotate(Math.toRadians(-100));

        // Draw wheel spaces
        for (int i = 0; i < myImageNames.size() / 2; i++) {
            g2D.drawImage(IMAGES.get(myImageNames.get(i)), -42, 0, this);
            g2D.rotate(Math.toRadians(-DEGREES_EACH));
        }

        // Set origin back to original location
        g2D.translate(-390, -285);

        // Draw the arrow to indicate which space the wheel stopped at
        g.drawImage(IMAGES.get("arrow.png"), 370, 10, this);
    }

    private void setEnabledConsonants(boolean b) {
        for (int i = 0; i < letterButtons.length; i++)
            if (!(i == 0 || i == 4 || i == 8 || i == 14 || i == 20))
                letterButtons[i].setEnabled(b);
    }

    private void setEnabledVowels(boolean b) {
        letterButtons[0].setEnabled(b);
        letterButtons[4].setEnabled(b);
        letterButtons[8].setEnabled(b);
        letterButtons[14].setEnabled(b);
        letterButtons[20].setEnabled(b);
    }

    private void setEnabledGuessedLetters(boolean b) {
        // Get guessed letters from puzzle panel
        ArrayList<Character> guessedLetters = myGame.getGuessedLetters();

        // Enable/disable each button
        for (int i = 0; i < guessedLetters.size(); i++)
            letterButtons[guessedLetters.get(i) - 65].setEnabled(b);
    }

    private void handleWin() {
        myGame.revealPuzzle();

        JOptionPane.showMessageDialog(null, "Congratulations, you win $"
                + myGame.getScore() + "!\n\n", "You Win!",
                JOptionPane.INFORMATION_MESSAGE);

        newGame();
    }

    private void handleLoss(String message) {
        myGame.revealPuzzle();

        JOptionPane.showMessageDialog(null, message + "\nSorry, you lose.",
                "You Lose!", JOptionPane.INFORMATION_MESSAGE);

        newGame();
    }

    private void showHowToPlay() {
        JOptionPane.showMessageDialog(
                null,
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
                "Created by Nikita Kouevda and Jenny Shen\nJune 2, 2012",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }

    // -------------------------------------------------------------------------
    // Classes
    // -------------------------------------------------------------------------

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton)e.getSource();

            // If the button pressed is a letter button
            for (int c = 0; c < letterButtons.length; c++)
                if (source == letterButtons[c]) {
                    // Number of appearances of letter in the puzzle
                    int appearances = myGame.revealLetter((char)(c + 65));

                    if (c == 0 || c == 4 || c == 8 || c == 14 || c == 20) {
                        // Subtract $250 (flat rate for vowels)
                        myTopPanel.addScore(-250);

                        if (appearances > 0) {
                            statusArea.setText("There "
                                    + (appearances == 1 ? "is" : "are") + " "
                                    + appearances + " " + (char)(c + 65)
                                    + (appearances == 1 ? "" : "'s")
                                    + " in the puzzle! Please spin the"
                                    + "wheel or buy another vowel.");

                            // Play the good guess sound
                            GOOD_GUESS_CLIP.play();
                        } else {
                            // Subtract a turn
                            myTopPanel.subtractTurn();

                            statusArea.setText("Sorry, there are no "
                                    + (char)(c + 65) + "'s in the puzzle. "
                                    + "Please spin the wheel or buy another "
                                    + "vowel.");

                            // Play the bad guess sound
                            BAD_GUESS_CLIP.play();
                        }

                        // Disable vowels if there are no more left
                        if (myGame.isAllVowelsGuessed()) {
                            // Disable vowel buttons
                            myGame.disableVowels();

                            // Play the no more vowels sound
                            NO_MORE_VOWELS_CLIP.play();

                            // Display no more vowels dialog
                            JOptionPane.showMessageDialog(null,
                                    "There are no more vowels left in "
                                            + "the puzzle.", "No More Vowels!",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        // Enable spin wheel button
                        spinWheel.setEnabled(true);

                        if (appearances > 0) {
                            int amount =
                                    Integer.parseInt(mySpaceLanded.substring(0,
                                            mySpaceLanded.indexOf('.')));

                            // Add proper amount to score
                            myTopPanel.addScore(amount * appearances);

                            statusArea.setText("There "
                                    + (appearances == 1 ? "is" : "are") + " "
                                    + appearances + " " + (char)(c + 65)
                                    + (appearances == 1 ? "" : "'s")
                                    + " in the puzzle! You earn $" + amount
                                    * appearances
                                    + "! Please spin the wheel again "
                                    + "or buy a vowel.");

                            // Play the good guess sound
                            GOOD_GUESS_CLIP.play();
                        } else {
                            // Subtract a turn
                            myTopPanel.subtractTurn();

                            statusArea.setText("Sorry, there are no "
                                    + (char)(c + 65) + "'s in the puzzle. "
                                    + "Please spin the wheel again or buy a "
                                    + "vowel.");

                            // Play the bad guess sound
                            BAD_GUESS_CLIP.play();
                        }
                    }

                    // Enable/disable letter buttons
                    setEnabledConsonants(false);
                    setEnabledVowels(myGame.getScore() >= 250);
                    setEnabledGuessedLetters(false);
                }

            // Act according to the button pressed
            if (source == spinWheel) {
                String cmd = e.getActionCommand();

                if (cmd.equals("Spin Wheel")) {
                    // Start the timer
                    myWheelTimer.start();

                    // Set text of status area
                    statusArea.setText("The wheel is spinning...");

                    // Set text of button
                    spinWheel.setText("Stop Wheel");

                    // Disable solve puzzle button
                    solvePuzzle.setEnabled(false);

                    // Disable vowel buttons
                    setEnabledVowels(false);

                    // Loop the spinning wheel sound
                    SPINNING_WHEEL_CLIP.loop();
                } else if (cmd.equals("Stop Wheel")) {
                    // Stop spinning wheel sound
                    SPINNING_WHEEL_CLIP.stop();

                    // Stop timer and set text of status area
                    myWheelTimer.stop();

                    // Set text of button
                    spinWheel.setText("Spin Wheel");

                    // Enable solve puzzle button
                    solvePuzzle.setEnabled(true);

                    // Get space landed on
                    mySpaceLanded = myImageNames.get(4);

                    if (mySpaceLanded.equals("loseATurn.png")) {
                        // Decrease number of turns left and set text
                        myTopPanel.subtractTurn();
                        statusArea.setText("Sorry, you lose a turn.");

                        // Enable/disable letter buttons
                        setEnabledConsonants(false);
                        setEnabledVowels(myGame.getScore() >= 250);
                        setEnabledGuessedLetters(false);
                    } else if (mySpaceLanded.equals("bankrupt.png")) {
                        // Decrease number of turns left and reset the
                        // score
                        myTopPanel.subtractTurn();
                        myTopPanel.resetScore();
                        statusArea.setText("Sorry, you lose a turn, and "
                                + "your score has been brought down to 0.");

                        // Enable/disable letter buttons
                        setEnabledConsonants(false);
                        setEnabledVowels(myGame.getScore() >= 250);
                        setEnabledGuessedLetters(false);

                        // Play the bankrupt sound
                        BANKRUPT_CLIP.play();
                    } else {
                        // Disable spin wheel button and set text
                        spinWheel.setEnabled(false);
                        statusArea.setText("Please select a consonant.");

                        // Enable/disable letter buttons
                        setEnabledConsonants(true);
                        setEnabledVowels(false);
                        setEnabledGuessedLetters(false);
                    }
                }
            } else if (source == solvePuzzle) {
                // Show input dialog to solve puzzle
                String solveAttempt =
                        JOptionPane.showInputDialog(null,
                                "Please solve the puzzle:", "Solve the Puzzle",
                                JOptionPane.PLAIN_MESSAGE);

                // Get phrase from puzzle phrase
                String phrase = myGame.getPhrase();

                // Trimmed strings
                String trimmedPhrase = "", trimmedAttempt = "";

                // Manually trim the phrase
                for (int i = 0; i < phrase.length(); i++)
                    if (phrase.charAt(i) != ' ')
                        trimmedPhrase += phrase.charAt(i);

                // Manually trim attempt
                if (solveAttempt != null)
                    for (int i = 0; i < solveAttempt.length(); i++)
                        if (solveAttempt.charAt(i) != ' ')
                            trimmedAttempt += solveAttempt.charAt(i);

                // Indicate win or loss with message dialog unless null
                if (trimmedAttempt != "")
                    if (trimmedAttempt.compareToIgnoreCase(trimmedPhrase) == 0)
                        handleWin();
                    else
                        handleLoss("That is incorrect.");
            } else if (source == newGame)
                newGame();
            else if (source == howToPlay)
                showHowToPlay();
            else if (source == about)
                showAbout();

            // Indicate win/loss if necessary
            if (myGame.getTurnsLeft() == 0)
                handleLoss("You have no turns left.");
            else if (myGame.isSolved())
                handleWin();

            myPuzzlePanel.repaint();
        }
    }
}
