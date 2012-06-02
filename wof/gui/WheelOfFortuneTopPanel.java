/**
 * @author Nikita Kouevda, Jenny Shen
 * @date 2012/06/02
 */

package wof.gui;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import wof.game.WheelOfFortuneGame;

public class WheelOfFortuneTopPanel extends JPanel {
    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private WheelOfFortuneGame myGame;

    private JLabel scoreLabel, turnsLabel;

    private JTextField scoreField, turnsField;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public WheelOfFortuneTopPanel(WheelOfFortuneGame game) {
        super();

        myGame = game;

        // Construct labels
        scoreLabel = new JLabel("Score:", JLabel.RIGHT);
        turnsLabel = new JLabel("Turns left:", JLabel.RIGHT);

        // Construct text fields and set their properties
        scoreField = new JTextField("$" + myGame.getScore());
        scoreField.setEditable(false);

        turnsField = new JTextField("" + myGame.getTurnsLeft());
        turnsField.setEditable(false);

        // Set layout and add items to this
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createHorizontalStrut(150));
        add(turnsLabel);
        add(Box.createHorizontalStrut(10));
        add(turnsField);
        add(Box.createHorizontalStrut(200));
        add(scoreLabel);
        add(Box.createHorizontalStrut(10));
        add(scoreField);
        add(Box.createHorizontalStrut(150));
        setPreferredSize(new Dimension(900, 25));
    }

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------

    public void addScore(int score) {
        myGame.addScore(score);
        scoreField.setText("$" + myGame.getScore());
    }

    public void resetScore() {
        myGame.resetScore();
        scoreField.setText("$" + myGame.getScore());
    }

    public void resetValues() {
        myGame.resetValues();
        turnsField.setText("" + myGame.getTurnsLeft());
        scoreField.setText("$" + myGame.getScore());
    }

    public void subtractTurn() {
        myGame.subtractTurn();
        turnsField.setText("" + myGame.getTurnsLeft());
    }
}
