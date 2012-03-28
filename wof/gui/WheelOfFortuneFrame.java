/**
 * @author Nikita Kouevda, Jenny Shen
 * @date 2012/03/27
 */

package wof.gui;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.UIManager;
import wof.game.WheelOfFortuneGame;

public class WheelOfFortuneFrame extends JFrame {
    // -----------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------
    
    public WheelOfFortuneFrame() {
        super("Wheel of Fortune");
        
        // Construct the game
        WheelOfFortuneGame game = new WheelOfFortuneGame();
        
        // Construct the panels
        WheelOfFortuneTopPanel topPanel =
                new WheelOfFortuneTopPanel(game);
        WheelOfFortunePuzzlePanel puzzlePanel =
                new WheelOfFortunePuzzlePanel(game);
        WheelOfFortuneWheelPanel wheelPanel =
                new WheelOfFortuneWheelPanel(game, topPanel,
                        puzzlePanel);
        
        // Set the layout and add the panels to the content pane
        setLayout(new FlowLayout());
        add(topPanel);
        add(puzzlePanel);
        add(wheelPanel);
    }
    
    // -----------------------------------------------------------------
    // Methods
    // -----------------------------------------------------------------
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager
                    .getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Ignore the exception
        }
        
        JFrame window = new WheelOfFortuneFrame();
        window.setSize(900, 600);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}
