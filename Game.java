import java.awt.*;
import javax.swing.*;

/**
 * Container Class to run Game
 * Displays Snake Class
 */
public class Game {
    public static void main(String[] args) {
        JFrame game = new JFrame();
        Snake snake = new Snake();
        game.setBounds(10, 10, 600, 650);
        game.setResizable(false);
        game.setBackground(new Color(137, 104, 153));
        game.setVisible(true);
        game.add(snake);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
