import javax.sound.sampled.AudioInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.io.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * The snake class displays the main game
 */
public class Snake extends JPanel implements KeyListener, ActionListener {
    // Storing Snake Positions
    private int[] snakeX = new int[750]; // 750 = Maximum Length
    private int[] snakeY = new int[750]; // 750 = Maximum Length

    private Direction direction = Direction.RIGHT; // Direction of Snake

    private boolean hasLost = false;
    private boolean hasWritten = false;

    // Images for painting
    private ImageIcon facingRight = new ImageIcon("facingRight.png");
    private ImageIcon facingLeft = new ImageIcon("facingLeft.png");
    private ImageIcon facingUp = new ImageIcon("facingUp.png");
    private ImageIcon facingDown = new ImageIcon("facingDown.png");
    private ImageIcon snakeBody = new ImageIcon("snakeBody.png");
    private ImageIcon title = new ImageIcon("snakebg.jpg");
    private ImageIcon fruit = new ImageIcon("fruit.png");
    private ImageIcon gameOver = new ImageIcon("gameOver.png");

    Clip clip; // Music

    private int moves = 0;
    private int length = 3;

    private Timer timer;

    private int fruitX;
    private int fruitY;
    private int fruitNum = 0;

    /**
     * Initializes default conditions
     * Adds KeyListeners and configures keyboard settings
     * Configures Timer
     * Adds Music
     */
    public Snake() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Timer for repainting every 100 ms
        timer = new Timer(100, this);
        timer.start();

        // Initial X Positions
        snakeX[2] = 50;
        snakeX[1] = 75;
        snakeX[0] = 100;

        // Initial Y Positions
        snakeY[2] = 100;
        snakeY[1] = 100;
        snakeY[0] = 100;

        generateFruit();
        // Opening Music
        try {
            clip = AudioSystem.getClip();
            AudioInputStream music = AudioSystem.getAudioInputStream(new File("Music.wav"));
            clip.open(music);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This class paints all graphics for gameplay
     * @param g Graphics Object that is used to perform paint operations
     */
    public void paint(Graphics g) {
        // Paints Background
        g.setColor(new Color(0, 0, 94));
        g.fillRect(0, 0, 600, 650);

        // Paints Outline
        g.setColor(new Color(255, 242, 0));
        g.fillRect(45, 95, 510, 510);

        title.paintIcon(this, g, 88, 35);

        // Paints Grid Cells
        g.setColor(new Color(0, 0, 0));
        g.fillRect(48, 98, 502, 502);

        // Paints Grid Lines
        g.setColor(new Color(0, 0, 170));
        for (int x = 49; x <= 550; x += 25) {
            g.drawLine(x, 98, x, 600);
        }
        for (int y = 99; y < 600; y += 25) {
            g.drawLine(48, y, 550, y);
        }

        // Paints Fruit Number
        g.setColor(new Color(255, 242, 0));
        g.drawString("Fruit eaten: " + fruitNum, 500, 25);

        // Paints Game Over Screen
        if (hasLost) {
            gameOver.paintIcon(this, g, 150, 300);

            if (!hasWritten) { // Only Writes once
                Statistics.writeScore(((length - 3) * 15));
                Statistics.writeMove(moves);
                hasWritten = true;
            }

            g.setColor(new Color(255, 242, 0));
            g.drawString("High Score: " + Statistics.computeHighestScore(), 160, 460);
            g.drawString("Score: " + (length - 3) * 15, 280, 460);
            g.drawString("Avg Score: " + Statistics.computeAverageScore(), 350, 460);
            g.drawString("Highest Moves: " + Statistics.computeHighestMoves(), 160, 490);
            g.drawString("Moves: " + moves, 280, 490);
            g.drawString("Avg Moves: " + Statistics.computeAverageMoves(), 350, 490);

            g.setColor(new Color(23, 95, 23));
            g.setFont(new Font("default", Font.BOLD, 16));
            g.drawString("Press Enter to Restart", 220, 390);
            return;
        }
        fruit.paintIcon(this, g, fruitX, fruitY);

        // Painting Snake
        for (int a = 0; a < length; a++) { // Loops though each part of snake
            // Painting Head by Direction
            if (a == 0) {
                switch (direction) {
                    case RIGHT:
                        facingRight.paintIcon(this, g, snakeX[a], snakeY[a]);
                        break;
                    case LEFT:
                        facingLeft.paintIcon(this, g, snakeX[a], snakeY[a]);
                        break;
                    case UP:
                        facingUp.paintIcon(this, g, snakeX[a], snakeY[a]);
                        break;
                    case DOWN:
                        facingDown.paintIcon(this, g, snakeX[a], snakeY[a]);
                        break;
                }
            } else { // Painting Body
                snakeBody.paintIcon(this, g, snakeX[a], snakeY[a]);
            }
            if ((snakeX[0] == snakeX[a + 1]) && (snakeY[0] == snakeY[a + 1])) { // Checks if snake hit any other part of snake
                hasLost = true;
                repaint();
            }
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) { // Moves Snake
        timer.start();
        switch (direction) { // Checking which direction to move snake
            case RIGHT:
                for (int r = length; r >= 0; r--) {
                    if (r == 0) {
                        snakeX[r] = snakeX[r] + 25;
                    } else {
                        snakeX[r] = snakeX[r - 1];
                        snakeY[r] = snakeY[r - 1];
                    }
                    if (snakeX[r] >= 548) {
                        hasLost = true;
                    }
                }
                break;
            case LEFT:
                for (int r = length; r >= 0; r--) {
                    if (r == 0) {
                        snakeX[r] = snakeX[r] - 25;
                    } else {
                        snakeX[r] = snakeX[r - 1];
                        snakeY[r] = snakeY[r - 1];
                    }
                    if (snakeX[r] < 48) {
                        hasLost = true;
                    }
                }
                break;
            case DOWN:
                for (int r = length; r >= 0; r--) {
                    if (r == 0) {
                        snakeY[r] = snakeY[r] + 25;
                    } else {
                        snakeX[r] = snakeX[r - 1];
                        snakeY[r] = snakeY[r - 1];
                    }
                    if (snakeY[r] > 599) {
                        hasLost = true;
                    }
                }
                break;
            case UP:
                for (int r = length; r >= 0; r--) {
                    if (r == 0) {
                        snakeY[r] = snakeY[r] - 25;
                    } else {
                        snakeY[r] = snakeY[r - 1];
                        snakeX[r] = snakeX[r - 1];
                    }
                    if (snakeY[r] < 98) {
                        hasLost = true;
                    }
                }
                break;
        }
        repaint();


        if ((snakeX[0] == fruitX) && (snakeY[0] == fruitY)) { // Checks if snake hit fruit; Increments Length
            length = length + 1;

            if (snakeX[length - 2] > snakeX[length - 3])  {
                snakeX[length - 1] = snakeX[length - 3] + 25;
            }

            if (snakeX[length - 2] < snakeX[length - 3]) {
                snakeX[length - 1] = snakeX[length - 2] - 25;
            }

            if (snakeY[length - 2] > snakeY[length - 3]) {
                snakeY[length - 1] = snakeY[length - 2] + 25;
            }

            if (snakeY[length - 2] < snakeY[length - 3]) {
                snakeY[length - 1] = snakeY[length - 2] - 25;
            }

            // Generates new Fruit
            generateFruit();
            fruitNum++;
            repaint();
        }
    }

    /**
     * Responds to keyboard events
     * Changes Directions or Restarts Game
     * @param e - Event which tells which key was Clicked
     */
    @Override
    public void keyPressed(KeyEvent e) { // Switches Direction; Increments Moves
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                if (hasLost) {
                    clip.stop();
                    Game.main(null);
                    break;
                }
            case KeyEvent.VK_RIGHT:
                direction = direction == Direction.LEFT ? Direction.LEFT : Direction.RIGHT;
                break;
            case KeyEvent.VK_LEFT:
                direction = direction == Direction.RIGHT ? Direction.RIGHT : Direction.LEFT;
                break;
            case KeyEvent.VK_DOWN:
                direction = direction == Direction.UP ? Direction.UP : Direction.DOWN;
                break;
            case KeyEvent.VK_UP:
                direction = direction == Direction.DOWN ? Direction.DOWN : Direction.UP;
                break;
        }
        moves = moves + 1;
    }

    /**
     * Ignored method that is implemented in KeyListener Interface
     * @param e Key Event
     */
    @Override
    public void keyTyped(KeyEvent e) { }

    /**
     * Ignored method that is implemented in KeyListener Interface
     * @param e Key Event
     */
    @Override
    public void keyReleased(KeyEvent e) { }

    /**
     * Generates random position for fruit
     * Used in constructor and when snakes eats a fruit
     */
    private void generateFruit() {
        fruitX = (int) (Math.random() * 20) * 25 + 50;
        fruitY = (int) (Math.random() * 20) * 25 + 100;
    }
}

  
