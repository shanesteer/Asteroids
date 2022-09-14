import java.awt.*;
import javax.swing.*;

public class SpaceGameMain extends JFrame{

    public SpaceGameMain(){

        SpaceGUI();
    }

    private void SpaceGUI(){

        add(new SpaceGame());

        //Setting the title and size of the game window
        setTitle("Space Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(Variables.boardWidth, Variables.boardHeight);
        //Used to make sure that the window appears in the centre of the screen
        setLocationRelativeTo(null);

    }

    public static void main(String[] args) {

        //calls Space GameMain using a variable and set the visibility of the main to true
        SpaceGameMain banana = new SpaceGameMain();
        banana.setVisible(true);

        //using event queue so that the rules pop up after the intro screen so players can read it before  starting the game.
        EventQueue.invokeLater(() ->{
            //Using a JOption pane to show the rules
            JOptionPane.showMessageDialog(null,
                    "1. You have to destroy all the enemies and asteroids to win the game.\n" +
                            "2. High scores are calculated based on the amount of time to kill all the enemies and asteroids.\n" +
                            "3. The player can move the ship in the up, down, left and right direction.\n" +
                            "4. If you hit an asteroid, you lose a life.\n" +
                            "5. If you hit an enemy, you lose a life.\n" +
                            "6. When you pick up a power up, you gain a life and shoot faster.\n" +
                            "7. You can press the escape button to exit the game or the pause button to pause the game.",
                    "RULES", JOptionPane.INFORMATION_MESSAGE);


        });
    }


}
