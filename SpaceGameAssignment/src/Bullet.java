import java.awt.*;
import javax.swing.*;

public class Bullet extends Sprites{

    private Image bullet;
    String bulletFile = "src/assets/pictures/bluelaser.png";

    public Bullet() {
        imageErrorHandling(bulletFile);
    }

    public Bullet(int x, int y) {

        initialiseBullet(x, y);
    }

    private void initialiseBullet(int x, int y) {

        //setting the image of the player bullet
        bullet = new ImageIcon(bulletFile).getImage();
        setImage(bullet);

        //used to position the bullet fired accurately
        int banana1 = 6;
        setX(x + banana1);

        int banana2 = 1;
        setY(y - banana2);

        //Adding the sound file to the background music class
        BackgroundMusic Music1 = new BackgroundMusic("src/assets/audio/laserbeamsound.wav");

        //Using a new thread so that the sounds play separately instead of overlapping one another
        new Thread(()->{
            try{
                //plays the sound using the start method created in the background music file
                Music1.start();
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }).start();
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
