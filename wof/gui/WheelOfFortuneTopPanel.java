
package wof.gui;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import wof.game.WheelOfFortuneGame;

public class WheelOfFortuneTopPanel extends JPanel {
    private WheelOfFortuneGame game;

    private JLabel score1Label, score2Label;

    private JTextField scoreField1 ,scoreField2, turnsField;

    public WheelOfFortuneTopPanel(WheelOfFortuneGame game) {
        super();

        this.game = game;

        score1Label = new JLabel("Team 1 Score:", JLabel.RIGHT);
        score2Label = new JLabel("Team 2 Score:", JLabel.RIGHT);

        scoreField1 = new JTextField("$" + this.game.getScore1());
        scoreField1.setEditable(false);

        scoreField2 = new JTextField("$" + this.game.getScore2());
        scoreField2.setEditable(false);

        // Set layout and add items to this
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createHorizontalStrut(50));
        add(score1Label);
        add(Box.createHorizontalStrut(10));
        add(scoreField1);
        add(Box.createHorizontalStrut(50));
        add(score2Label);
        add(Box.createHorizontalStrut(10));
        add(scoreField2);
        add(Box.createHorizontalStrut(50));
        setPreferredSize(new Dimension(1200, 100));
        score1Label.setFont(new Font("Tahoma", Font.PLAIN, 24));
        score2Label.setFont(new Font("Tahoma", Font.PLAIN, 24));
        scoreField1.setFont(new Font("Tahoma", Font.PLAIN, 48));
        scoreField2.setFont(new Font("Tahoma", Font.PLAIN, 48));

    }

    public void addScore1(int score1) {
        game.addScore1(score1);
        scoreField1.setText("$" + game.getScore1());
    }

    public void addScore2(int score2) {
        game.addScore2(score2);
        scoreField2.setText("$" + game.getScore2());
    }

    public void resetScore1() {
        game.resetScore1();
        scoreField1.setText("$" + game.getScore1());
    }

    public void resetScore2() {
        game.resetScore2();
        scoreField2.setText("$" + game.getScore2());
    }

    public void resetValues() {
        game.resetValues();
        scoreField1.setText("$" + game.getScore1());
        scoreField2.setText("$" + game.getScore2());
    }
}
