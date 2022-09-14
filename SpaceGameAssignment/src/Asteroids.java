import javax.swing.*;
import java.awt.*;

public class Asteroids extends Sprites{

    private Image asteroid, powerup;

    String asteroidFile = "src/assets/pictures/asteroid.png";
    String powerFile = "src/assets/pictures/powerup.png";

    private Asteroids.PowerUp powerUp;


    public Asteroids(int x, int y) {
        initialiseAsteroid(x, y);
        imageErrorHandling(asteroidFile);
        imageErrorHandling(powerFile);
    }

    //initialising the enemy and adding image to it
    private void initialiseAsteroid(int x, int y) {

        this.x = x;
        this.y = y;

        powerUp = new Asteroids.PowerUp(x, y);

        asteroid = new ImageIcon(asteroidFile).getImage();
        setImage(asteroid);
    }

    //Used to position the asteroids in a horizontal direction
    public void dir(int direction) {

        this.x += direction;
    }


    //Used for when the asteroid is going to drop a powerup
    public PowerUp getPowerUp() {

        return powerUp;
    }

    //Space mine class for each enemy
    public class PowerUp extends Sprites {


        private boolean powerUp;

        public PowerUp(int x, int y) {
            initialisePowerUp(x, y);
        }

        private void initialisePowerUp(int x, int y) {

            setPowerUp(true);

            this.x = x;
            this.y = y;


            powerup = new ImageIcon(powerFile).getImage();
            setImage(powerup);
        }

        public void setPowerUp(boolean powerUp) {
            this.powerUp = powerUp;
        }

        public boolean isPowerUp() {
            return powerUp;
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
