/**
 * @author Nikita Kouevda, Jenny Shen
 * @date 2013/10/05
 */

package wof.gui;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import wof.game.WheelOfFortuneGame;

public class WheelOfFortuneFrame extends JFrame {
    public WheelOfFortuneFrame() {
        super("Wheel of Fortune");

        WheelOfFortuneGame game = new WheelOfFortuneGame();

        WheelOfFortuneTopPanel topPanel = new WheelOfFortuneTopPanel(game);
        WheelOfFortunePuzzlePanel puzzlePanel =
            new WheelOfFortunePuzzlePanel(game);
        WheelOfFortuneWheelPanel wheelPanel =
            new WheelOfFortuneWheelPanel(game, topPanel, puzzlePanel);

        setLayout(new FlowLayout());
        add(topPanel);
        add(puzzlePanel);
        add(wheelPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            // Use system-specific UI if possible
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Proceed without system-specific UI
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WheelOfFortuneFrame();
            }
        });
    }
}
