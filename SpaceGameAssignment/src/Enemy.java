import javax.swing.*;
import java.awt.*;

public class Enemy extends Sprites{
    private Image enemy, mine;

    String enemyFile = "src/assets/pictures/enemy.png";
    String mineFile = "src/assets/pictures/redlaser.png";
    String powerFile = "src/assets/pictures/redlaser.png";

    private SpaceMine spaceMine;
    //private PowerUp powerUp;

    public Enemy(int x, int y) {
        initialiseEnemy(x, y);
        imageErrorHandling(enemyFile);
        imageErrorHandling(mineFile);
    }

    //initialising the enemy and adding image to it
    private void initialiseEnemy(int x, int y) {

        this.x = x;
        this.y = y;

        spaceMine = new SpaceMine(x, y);

        enemy = new ImageIcon(enemyFile).getImage();
        setImage(enemy);
    }

    //Used to position the enemies in a horizontal direction
    public void dir(int direction) {

        this.x += direction;
    }

    //Used for when the enemy is going to drop a mine
    public SpaceMine getMine() {

        return spaceMine;
    }

    //Space mine class for each enemy
    public class SpaceMine extends Sprites {

        private boolean blownUp;

        public SpaceMine(int x, int y) {
            initialiseMine(x, y);
        }

        private void initialiseMine(int x, int y) {

            setBlownUp(true);

            this.x = x;
            this.y = y;

            mine = new ImageIcon(mineFile).getImage();
            setImage(mine);
        }

        public void setBlownUp(boolean blownUp) {
            this.blownUp = blownUp;
        }

        public boolean isBlownUp() {
            return blownUp;
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
