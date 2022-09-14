import java.awt.*;
import java.util.*;
import javax.swing.*;

import java.awt.event.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.swing.Timer;
import javax.sound.sampled.*;

// Some of the code was referenced from https://zetcode.com/javagames/spaceinvaders/
// But was edited to fit the assignment brief
// Extra features were also added to the code

public class SpaceGame extends JPanel {

    private Dimension dim;
    private List<Enemy> enemies;
    private List<Asteroids> asteroids;
    private Player player;
    private Bullet bullet;
    private final Font smallFont = new Font("Helvetica", Font.BOLD, 20); //font for start screen

    private boolean inGame = false;
    private String message = "Game Over";

    private int direction = -1;
    private int enemyOutOfLives = 0;
    private int livesLeft = 3;
    private Timer timer;

    long start;
    long end;
    float sec;

    int score, endScore, endScore1;

    private Image explosion;

    String explosionFile = "src/assets/pictures/explosion.gif";
    //String livesFile = "src/assets/pics/player.png";

    //image for background
    Image img = Toolkit.getDefaultToolkit().getImage("src/assets/pictures/background6.jpg");



    //Initialising the game, the board, the background music and image error handling
    public SpaceGame(){
        initialiseGame();
        initialiseBoard();
        run();
        imageErrorHandling(explosionFile);
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


    private void initialiseBoard() {

        addKeyListener(new Controller());
        setFocusable(true);
        dim = new Dimension(Variables.boardWidth, Variables.boardHeight);
        setBackground(Color.black);

        //Starts counting time when the game starts
        start = System.currentTimeMillis();

        for (int i = 0; i <5; i++) {
            try {
                Thread.sleep(60);
            }
            catch(Exception ex){
                System.exit(1);
            }
        }

    }


    private void initialiseGame() {

        enemies = new ArrayList<>();
        asteroids = new ArrayList<>();

        //positioning enemies
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 13; j++) {

                var enemy = new Enemy(Variables.enemy_x + 50 * j, Variables.enemy_y + 30 * i);
                enemies.add(enemy);
            }
        }

        //positioning asteroids
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 13; j++) {

                var asteroid = new Asteroids(Variables.asteroid_x + 50 * j, Variables.asteroid_y + 40 * i);
                asteroids.add(asteroid);
            }
        }



        player = new Player();
        bullet = new Bullet();

    }


    //tried to create a new level after this but did not manage to finish in time
    private void continueLevel(){
        /*for(int i = 0; i < Variables.noOfEnemies; i++){
            //positioning enemies
            for (int k = 0; k < 2; i++) {
                for (int j = 0; j < 15; j++) {

                    var enemy = new Enemy(Variables.enemy_x + 50 * j, Variables.enemy_y + 30 * i);
                    enemies.add(enemy);
                }
            }
        }

        for(int i = 0; i < Variables.noOfAsteroids; i++){
            //positioning enemies
            for (int k = 0; k < 3; i++) {
                for (int j = 0; j < 15; j++) {

                    var asteroid = new Asteroids(Variables.asteroid_x + 50 * j, Variables.asteroid_y + 40 * i);
                    asteroids.add(asteroid);
                }
            }
        }*/

        direction = -2;
    }

    //A screen that pops up at the end saying if the game is over or if you have won
    private void gameOver(Graphics2D g2d) {

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, Variables.boardWidth, Variables.boardHeight);

        //sets the position and colour of the box around the text
        g2d.setColor(new Color(0, 0, 0));
        g2d.fillRect(0, 0, Variables.boardWidth, Variables.boardHeight);
        g2d.setColor(new Color(255, 17, 0));
        g2d.drawRect(40, Variables.boardWidth / 2 - 180, Variables.boardWidth - 100, 90);

        var small = new Font("Helvetica", Font.BOLD, 20);
        var metric = this.getFontMetrics(small);

        //sets the position and colour of the text
        g2d.setColor(new Color(255, 17, 0));
        g2d.setFont(small);
        g2d.drawString(message, (850 - metric.stringWidth(message)) / 2, Variables.boardWidth / 2 - 125);
    }

    private void IntroScreen(Graphics2D g2d) {

        //When the game window  pops up, there will be an intro screen asking the user to press enter to start the game
        //Setting the background colour to black and the border colour to red
        g2d.setColor(new Color(0, 0, 0));
        g2d.fillRect(0, 0, Variables.boardWidth, Variables.boardHeight);
        g2d.setColor(new Color(255, 17, 0));
        g2d.drawRect(40, Variables.boardWidth / 2 - 180, Variables.boardWidth - 100, 90);


        //Starting text on the intro screen
        String s = "Press ENTER to start";
        Font small = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metric = this.getFontMetrics(small);

        //Setting the colour of the text to red
        g2d.setColor(new Color(255, 17, 0));
        g2d.setFont(small);
        g2d.drawString(s, (730 - metric.stringWidth(message)) / 2, Variables.boardWidth / 2 - 125);

    }

    //A function used to draw the amount of enemies and asteroids the player has destroyed
    private void drawScore(Graphics g) {

        int i;
        String s;

        //Displays the amount of enemies and asteroids the player has destroyed
        FontMetrics metric = this.getFontMetrics(smallFont);
        g.setColor(Color.white);
        s = "Kills: " + score;
        g.drawString(s, (1500 + metric.stringWidth(message)) / 2, Variables.boardWidth / 2 + 180);

        endScore1 = score;

        //Displays the amount of lives the player has left
        g.setColor(Color.white);
        s = "Lives Left: " + livesLeft;
        g.drawString(s, (-50 + metric.stringWidth(message)) / 2, Variables.boardWidth / 2 + 180);

    }




    //A function that gets the score of the player when all lives are lost
    private void outputScore(){
        Name name1 = new Name();

        //Checks to see if the player has destroyed all enemies and asteroids
        if(enemyOutOfLives == Variables.noOfAsteroids + Variables.noOfEnemies){
            message = "Game Won!";
            //stops counting time when all the enemies are dead
            end = System.currentTimeMillis();

            //calculates the elapse time in milliseconds and converts it to seconds
            sec = (end - start) / 1000F;

            //Calls the name class so a joptionpane pops up for the player to enter their name for scoring
            Name n = new Name();
            n.name();
            String name = name1.name;

            //When all the players lives have finished, the score is obtained and written to the Scores.txt file
            endScore = (int) sec;

            try {
                FileWriter writer = new FileWriter("src/Scores.txt", true);
                writer.write(name + ": " + endScore);
                writer.write("\r\n");   // write new line

                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }


            //Sorting the players scores from lowest to highest
            try {
                java.util.List<PlayerScores> playerScore;
                //Read all lines from the Score.txt file as a stream and puts them in a collection toList()
                playerScore = Files.lines(Path.of("src/Scores.txt")).map(PlayerScores::parseLine).collect(Collectors.toList());

                //Sorting the list in ascending order using the scores
                Collections.sort(playerScore, Comparator.comparingInt(PlayerScores::getScore));

                //Takes the sorted scores and writes them back to the Scores.txt file
                java.util.List<String> lines = playerScore.stream().map(PlayerScores::toLine).collect(Collectors.toList());
                Files.write(Path.of("src/Scores.txt"), lines);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //Calls the HighestScores function
            try {
                HighestScores();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    //A function for getting the high scores from the Scores.txt file and displaying it using a JOption pane when the game ends
    public void HighestScores() throws IOException{
        String input = "";
        int i =0;

        Scanner scanner = new Scanner(new File("src/Scores.txt"));

        //Reads through the file until there are no more lines to read in the file
        String line = "";
        while (scanner.hasNextLine() & i <5) {
            line = scanner.nextLine();

            //Add the line and then "\n" indicating a new line
            input += line + "\n";
            i+=1;


        }

        scanner.close();

        //A JOption pane then pops up with the 5 highest scores with the players names and when the ok button is pressed, the system closes
        int input_options = JOptionPane.showOptionDialog(null,input,"High Scores", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if(input_options == JOptionPane.OK_OPTION){
            System.exit(1);
        }


    }

    //Draws the enemy image and deletes it if the enemy dies
    private void drawEnemies(Graphics g) {

        for (Enemy enemy : enemies) {

            if (enemy.isVisible()) {

                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }

            if (enemy.isDead()) {

                enemy.dead();
            }
        }
    }

    //Draws the asteroid image and deletes it if the enemy dies
    private void drawAsteroids(Graphics g) {

        for (Asteroids asteroid : asteroids) {

            if (asteroid.isVisible()) {

                g.drawImage(asteroid.getImage(), asteroid.getX(), asteroid.getY(), this);
            }

            if (asteroid.isDead()) {

                asteroid.dead();
            }
        }
    }

    //Draws the player image and deletes it if the enemy dies
    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDead()) {

            player.dead();
            inGame = false;
        }
    }

    //Draws the bullet image
    private void drawBullet(Graphics g) {

        if (bullet.isVisible()) {

            g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
        }
    }

    //Draws the mine image and deletes it if the enemy dies
    private void drawMine(Graphics g) {

        for (Enemy e : enemies) {

            Enemy.SpaceMine sm = e.getMine();

            if (!sm.isBlownUp()) {

                g.drawImage(sm.getImage(), sm.getX(), sm.getY(), this);
            }
        }
    }

    //Draws the power up image and deletes it if the enemy dies
    private void drawPowerUp(Graphics g){
        for(Asteroids a : asteroids){

            Asteroids.PowerUp pu = a.getPowerUp();

            if (!pu.isPowerUp()) {

                g.drawImage(pu.getImage(), pu.getX(), pu.getY(), this);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        Graphics2D g2d = (Graphics2D) g;


        if(inGame){

            Drawing(g);

        }
        else if(livesLeft == 0 && !inGame){
            //calls the game over class when the player dies
            gameOver(g2d);
        }
        else if(enemyOutOfLives == Variables.noOfAsteroids + Variables.noOfEnemies && !inGame){
            //calls the game over class when all enemies are destroyed
            gameOver(g2d);
        }
        else{
            //Calls the intro screen class if inGame is false
            IntroScreen(g2d);

        }

    }

    private void Drawing(Graphics g) {

        g.drawImage(img, 0, 0, null);

        //set colour for the ground line
        //g.setColor(Color.black);

        //Draws all the sprites
        if (inGame) {

            //g.drawLine(0, Variables.ground, Variables.boardWidth, Variables.ground);

            drawEnemies(g);
            drawAsteroids(g);
            drawPlayer(g);
            drawBullet(g);
            drawMine(g);
            drawPowerUp(g);
            drawScore(g);

        } else {


                if (timer.isRunning()) {
                    timer.stop();

                }

        }

        //synchronizes the graphics state
        Toolkit.getDefaultToolkit().sync();
        //Disposes of this graphics context and releases any system resources that it is using
        g.dispose();
    }


    //A function that is called to end the game when the player loses all lives
    private void endGame(){

        //The output score function is called to get the score at the end of the game and write it to the Score.txt file
        outputScore();
    }



    private void update() {

        //checks to see if all the enemies and asteroids have been destroyed or if the player has lost all lives
        if (enemyOutOfLives == Variables.noOfEnemies + Variables.noOfAsteroids || livesLeft == 0) {

            //sets inGame to false, stops the timer and calls the endGame function
            inGame = false;
            timer.stop();
            endGame();


        }


        // player

        //Sets it so theat the player can move up, down, left and right
        player.dir();
        //Creates a rectangle around the player for collision detection
        Rectangle playerRec = new Rectangle(player.getX(), player.getY(),10,20);

        // bullet
        if (bullet.isVisible()) {

            //Creates a rectangle around the bullet for collision detection
            Rectangle bulletRect = new Rectangle(bullet.getX(), bullet.getY(),5,20);

            for (Enemy enemy : enemies) {

                //Creates a rectangle around the enemy for collision detection
                Rectangle enemyRec = new Rectangle(enemy.getX(), enemy.getY(), 20,20);

                if (enemy.isVisible() && bullet.isVisible()) {

                    //Checks to see if the rectangles of the bullet and enemy intersect
                    //Sets the enemy to dead if they do collide
                    if(bulletRect.intersects(enemyRec)){
                        explosion = new ImageIcon(explosionFile).getImage();
                        enemy.setImage(explosion);
                        enemy.setDead(true);
                        enemyOutOfLives++;
                        bullet.dead();
                        score++;
                        System.out.println(score);
                    }
                }

                for (Asteroids asteroid : asteroids) {

                    //Creates a rectangle around the asteroid for collision detection
                    Rectangle asteroidRec = new Rectangle(asteroid.getX(), asteroid.getY(), 20,20);


                    if (asteroid.isVisible() && bullet.isVisible()) {

                        //Checks to see if the rectangles of the bullet and asteroid intersect
                        //Sets the asteroid to dead if they do collide
                        if(bulletRect.intersects(asteroidRec)){
                            explosion = new ImageIcon(explosionFile).getImage();
                            asteroid.setImage(explosion);
                            asteroid.setDead(true);
                            enemyOutOfLives++;
                            bullet.dead();
                            score++;
                            System.out.println(score);
                        }
                    }

                }
            }

            //speed of bullet
            int y = bullet.getY();

            //Sets the speed of the bullet based on the amount of lives you have
            if(livesLeft == 3){
                y -= 30;
            }
            if(livesLeft < 3){
                y -= 15;
            }
            if(livesLeft > 3){
                y -= 40;
            }

            //sets the bullet to dead when it reaches the top of the screen
            if (y < 0) {
                bullet.dead();
            }

            else {
                bullet.setY(y);
            }
        }

        // Enemy ships

        for (Enemy enemy : enemies) {

            Rectangle enemyRec = new Rectangle(enemy.getX(), enemy.getY(), 20,20);
            int x = enemy.getX();

            //When the enemy reaches each side of the window it moves down once
            if (x >= Variables.boardWidth - Variables.boardRight && direction != -1) {

                direction = -1;

                Iterator<Enemy> iterator1 = enemies.iterator();

                while (iterator1.hasNext()) {

                    Enemy e1 = iterator1.next();
                    e1.setY(e1.getY() + Variables.drop);
                }
            }

            if (x <= Variables.boardLeft && direction != 1) {

                direction = 1;

                Iterator<Enemy> iterator2 = enemies.iterator();

                while (iterator2.hasNext()) {

                    Enemy e2 = iterator2.next();
                    e2.setY(e2.getY() + Variables.drop);
                }
            }

            //if the player collides with an enemy, they lose a life
            if(playerRec.intersects(enemyRec)){
                if(livesLeft <= 0){
                    explosion = new ImageIcon(explosionFile).getImage();
                    player.setImage(explosion);
                    player.setDead(true);
                }
                else{
                    livesLeft -= 1;
                    System.out.println("Lives left: " + livesLeft);
                }
            }
        }

        //if the enemy reaches the players ship, the invasion was successful
        Iterator<Enemy> iterator = enemies.iterator();

        while (iterator.hasNext()) {

            Enemy enemy = iterator.next();

            if (enemy.isVisible()) {

                int y = enemy.getY();

                if (y > Variables.ground - Variables.enemyHeight) {
                    inGame = false;
                    message = "Enemy Has Invaded!";
                }

                enemy.dir(direction);
            }

        }



        // mines
        var mineGenerator = new Random();

        //if the enemy gets hit by a bullet, it explodes
        for (Enemy enemy : enemies) {

            //sets the bounds for a random number chosen for mines to spawn
            int bullet = mineGenerator.nextInt(100);

            Enemy.SpaceMine sm = enemy.getMine();
            //Creates a rectangle around the mine for collision detection
            Rectangle smRec = new Rectangle(sm.getX(), sm.getY(), 20, 20);

            if (bullet == Variables.chances && enemy.isVisible() && sm.isBlownUp()) {

                sm.setBlownUp(false);
                sm.setX(enemy.getX());
                sm.setY(enemy.getY());
            }


            //if the space mine hits the player, the player loses a life
            if (player.isVisible() && !sm.isBlownUp()) {

                //if a space mine hits the player, they lose a life
                if(smRec.intersects(playerRec)){
                    sm.setBlownUp(true);

                    if(livesLeft <= 0){
                        explosion = new ImageIcon(explosionFile).getImage();
                        player.setImage(explosion);
                        player.setDead(true);
                    }
                    else{
                        livesLeft -= 1;
                        System.out.println("Lives left: " + livesLeft);
                        sm.setBlownUp(true);
                    }
                }

            }

            //makes sure that the mines reach the bottom of the screen before disappearing
            if (!sm.isBlownUp()) {

                sm.setY(sm.getY() + 1);

                if (sm.getY() >= Variables.ground - Variables.bombHeight) {

                    sm.setBlownUp(true);
                }
            }
        }


        //Asteroids

        for (Asteroids asteroid : asteroids) {

            //creates a rectangle around the asteroids for collision detection
            Rectangle asteroidRec = new Rectangle(asteroid.getX(), asteroid.getY(), 20,20);
            int x = asteroid.getX();

            //When the asteroid reaches each side of the window it moves down once
            if (x >= Variables.boardWidth - Variables.boardRight && direction != -1) {

                direction = -1;

                Iterator<Asteroids> iterator3 = asteroids.iterator();

                while (iterator3.hasNext()) {

                    Asteroids as1 = iterator3.next();
                    as1.setY(as1.getY() + Variables.drop);
                }
            }

            if (x <= Variables.boardLeft && direction != 1) {

                direction = 1;

                Iterator<Asteroids> iterator4 = asteroids.iterator();

                while (iterator4.hasNext()) {

                    Asteroids as2 = iterator4.next();
                    as2.setY(as2.getY() + Variables.drop);
                }
            }

            //if the player collides with an asteroid, they lose a life
            if(asteroid.isVisible() && player.isVisible()) {
                if (playerRec.intersects(asteroidRec)) {
                    if(livesLeft <= 0){

                        explosion = new ImageIcon(explosionFile).getImage();
                        player.setImage(explosion);
                        player.setDead(true);

                    }
                    else{
                        livesLeft -= 1;
                        System.out.println("Lives left: " + livesLeft);
                    }
                }
            }
        }

        //if the enemy reaches the players ship, the invasion was successful
        Iterator<Asteroids> iterator5 = asteroids.iterator();

        while (iterator5.hasNext()) {

            Asteroids asteroid = iterator5.next();

            if (asteroid.isVisible()) {

                int y = asteroid.getY();

                if (y > Variables.ground - Variables.enemyHeight) {
                    inGame = false;
                    message = "Earth has been destroyed!";
                }

                asteroid.dir(direction);
            }
        }

        // power ups
        var powerUpGenerator = new Random();

        //if the enemy gets hit by a bullet, it explodes
        for (Asteroids asteroid : asteroids) {

            //sets the bounds for a random number chosen for power ups to spawn
            int bullet = powerUpGenerator.nextInt(10000);
            Asteroids.PowerUp pu = asteroid.getPowerUp();

            //Creates a rectangle around the power up for collision detection
            Rectangle puRec = new Rectangle(pu.getX(), pu.getY(), 20, 20);

            if (bullet == Variables.chances && pu.isVisible() && pu.isPowerUp()) {

                pu.setPowerUp(false);
                pu.setX(asteroid.getX());
                pu.setY(asteroid.getY());
            }


            //if the player picks up a power up, their life increases by 1 and they shoot faster
            if (player.isVisible() && !pu.isPowerUp()) {


                if(puRec.intersects(playerRec)){
                    pu.setPowerUp(true);


                    if(livesLeft <= 3){
                        //System.out.println("Lives left: " + livesLeft);
                        livesLeft += 1;
                    }

                }

            }

            //makes sure that the power up reaches the bottom of the screen before disappearing
            if (!pu.isPowerUp()) {

                pu.setY(pu.getY() + 1);

                if (pu.getY() >= Variables.ground - Variables.bombHeight) {

                    pu.setPowerUp(false);
                }
            }
        }

    }




    //Updates and repaints the Game cycle
    private void doGameCycle() {

        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    //Adds background music to the game
    //calls the background music class and the .wav file path
   BackgroundMusic Music = new BackgroundMusic("src/assets/audio/GameAudio.wav");

    //Uses a run function that is added to the SpaceGame constructor
    public void run() {
        try {
            //Calls the loop function from the Background music class to loop the background music
            Music.loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private class Controller extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if(inGame) {
                if (key == KeyEvent.VK_SPACE) {

                    if (inGame) {

                        if (!bullet.isVisible()) {

                            bullet = new Bullet(x, y);
                        }
                    }
                }
                //When the escape key is pressed, the game ends
                else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                    System.exit(1);
                }

                //When the pause key is pressed, the game pauses
                else if (key == KeyEvent.VK_PAUSE) {
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                }

            }
            //When the enter key is pressed, the game starts
            else{
                if (key == KeyEvent.VK_ENTER) {
                    inGame = true;
                    initialiseGame();
                    timer = new Timer(Variables.delay, new GameCycle());
                    timer.start();

                }
            }

        }
    }
}
