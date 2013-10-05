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

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import wof.game.WheelOfFortuneGame;

public class WheelOfFortunePuzzlePanel extends JPanel {
    private static final String SOUNDS_DIR = "/wof/sounds/";

    private static final int SQUARE_WIDTH = 35, SQUARE_HEIGHT = 45,
            SPACE_WIDTH = 2, PUZZLE_WIDTH = 12 * SQUARE_WIDTH,
            PUZZLE_HEIGHT = 4 * SQUARE_HEIGHT;

    private final AudioClip CATEGORY_CLIP;

    private WheelOfFortuneGame game;

    private JLabel categoryLabel;

    public WheelOfFortunePuzzlePanel(WheelOfFortuneGame game) {
        super();

        this.game = game;

        CATEGORY_CLIP =
            Applet.newAudioClip(getClass().getResource(
                SOUNDS_DIR + "category.wav"));

        categoryLabel = new JLabel();
        categoryLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));

        Box labelBox = Box.createVerticalBox();
        labelBox.add(Box.createVerticalStrut(215));
        labelBox.add(categoryLabel);

        add(labelBox);
        setPreferredSize(new Dimension(900, 250));
    }

    public void newGame() {
        game.newPhrase();
        categoryLabel.setText(game.getCategory());
        CATEGORY_CLIP.play();

        // Repaint to show the new puzzle
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        String phrase = game.getPhrase();

        // Draw each letter box
        for (int i = 0; i < phrase.length(); ++i) {
            int row = i / 12, col = i % 12;

            paintLetterBox(g, row, col, phrase.charAt(i) == ' ');

            // Draw letter in this box if it has been revealed
            if (game.getGuessedLetters().contains(phrase.charAt(i))) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Bitstream Vera Sans Mono", Font.PLAIN, 46));
                drawLetter(g, ("" + phrase.charAt(i)).toUpperCase(), row, col);
            }
        }
    }

    private void paintLetterBox(Graphics g, int row, int col, boolean b) {
        g.setColor(b ? Color.GREEN.darker() : Color.WHITE);
        g.fillRect((getWidth() - PUZZLE_WIDTH) / 2 + col
            * (SQUARE_WIDTH + SPACE_WIDTH), (getHeight() - PUZZLE_HEIGHT) / 3
            + row * (SQUARE_HEIGHT + SPACE_WIDTH), SQUARE_WIDTH, SQUARE_HEIGHT);
    }

    private void drawLetter(Graphics g, String str, int row, int col) {
        g.drawString(str, (getWidth() - PUZZLE_WIDTH) / 2 + col
            * (SQUARE_WIDTH + SPACE_WIDTH) + SQUARE_WIDTH / 8,
            (getHeight() - PUZZLE_HEIGHT) / 3 + (row + 1)
                * (SQUARE_HEIGHT + SPACE_WIDTH) - SQUARE_HEIGHT / 6);
    }
}
