import java.awt.*;

public class Sprites {

    private boolean visible;
    private boolean die;
    private Image image;

    int x, y;
    int dx = 0;
    int dy = 0;

    public Sprites(){
        visible = true;
    }

    public boolean isVisible() {
        return visible;
    }

    protected void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void dead(){
        visible = false;
    }

    public void setDead(boolean die) {
        this.die = die;
    }

    public boolean isDead() {
        return this.die;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

}
