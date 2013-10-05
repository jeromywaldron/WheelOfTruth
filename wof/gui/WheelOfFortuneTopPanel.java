/**
 * @author Nikita Kouevda, Jenny Shen
 * @date 2013/10/05
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
    private WheelOfFortuneGame game;

    private JLabel scoreLabel, turnsLabel;

    private JTextField scoreField, turnsField;

    public WheelOfFortuneTopPanel(WheelOfFortuneGame game) {
        super();

        this.game = game;

        scoreLabel = new JLabel("Score:", JLabel.RIGHT);
        turnsLabel = new JLabel("Turns left:", JLabel.RIGHT);

        scoreField = new JTextField("$" + this.game.getScore());
        scoreField.setEditable(false);

        turnsField = new JTextField("" + this.game.getTurnsLeft());
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

    public void addScore(int score) {
        game.addScore(score);
        scoreField.setText("$" + game.getScore());
    }

    public void resetScore() {
        game.resetScore();
        scoreField.setText("$" + game.getScore());
    }

    public void resetValues() {
        game.resetValues();
        turnsField.setText("" + game.getTurnsLeft());
        scoreField.setText("$" + game.getScore());
    }

    public void subtractTurn() {
        game.subtractTurn();
        turnsField.setText("" + game.getTurnsLeft());
    }
}
