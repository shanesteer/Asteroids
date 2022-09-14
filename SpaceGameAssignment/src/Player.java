import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class Player extends Sprites{

    private Image player;

    String playerFile = "src/assets/pictures/player2.png";


    private int width;
    private int height;

    public Player() {
        initialisePlayer();
        imageErrorHandling(playerFile);
    }

    private void initialisePlayer() {

        player = new ImageIcon(playerFile).getImage();
        width = player.getWidth(null);
        height = player.getHeight(null);
        setImage(player);

        //Setting the starting position of the player when the game is initialised
        int startingXPosition = 220;
        setX(startingXPosition);

        int startingYPosition = 450;
        setY(startingYPosition);
    }

    //setting up the players movements
    public void dir() {

        x += dx;
        y += dy;

        //player can move left or right
        if (x <= 2) {
            x = 2;
        }

        if (x >= Variables.boardWidth - 2 * width) {
            x = Variables.boardWidth - 2 * width;
        }

        //player can move up or down
        if (y <= 2) {
            y = 2;
        }

        if (y >= Variables.boardHeight - 2 * height) {
            y = Variables.boardHeight - 2 * height;
        }

    }

    //the player sprite moves left or right depending on which cursor key is pressed
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 2;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 2;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -2;
        }
    }

    //setting dx to 0 so that the player sprite stops moving when the cursor keys are released
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }
    }

    //A function for image error handling
    private void imageErrorHandling(String filename){
        boolean check = false;
        Image image = new ImageIcon(filename).getImage();
        //checks id the width of the image is -1.
        if(!(image.getWidth(null) == -1)){
            check = true;
        }
        String errorMessage = filename + " cannot be found. Please check the assets folder to ensure the image is present.\n" + "Exiting program....";
        //if the width of the image is -1, it means the image does not exist and an error message saying which image does not exist pops up
        if(!check){
            JOptionPane.showMessageDialog(null, errorMessage, "Image Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }
}
