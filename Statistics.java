import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * The statistics class handles writing the score and moves from a game. It also can compute the average/highest score and moves
 */

public class Statistics {
    private static File movesFile = new File("moves.txt");
    private static File scoresFile = new File("scores.txt");
    private static DecimalFormat format = new DecimalFormat("###.##");

    /**
     * Writes the inputted score to the scoresFile
     * @param score score to bet written to scoresFile
     */
    public static void writeScore(int score) {
        try {
            Writer fileWriter = new BufferedWriter(new FileWriter(scoresFile, true));
            fileWriter.append(Integer.toString(score)).append("\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the inputted move to the movesFile
     * @param move score to bet written to scoresFile
     */
    public static void writeMove(int move) {
        try {
            Writer fileWriter = new BufferedWriter(new FileWriter(movesFile, true));
            fileWriter.append(Integer.toString(move)).append("\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Computes average score of scores in scoresFile
     * @return Average Score
     */
    public static String computeAverageScore() {
        ArrayList<Integer> scores = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(scoresFile);
            while (scanner.hasNextLine()) {
                scores.add(Integer.parseInt(scanner.nextLine()));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return format.format((scores.stream().mapToDouble(score -> (double) score).average().orElse(0.0)));
    }

    /**
     * Computes Highest score of scores in scoresFile
     * @return Highest Score
     */
    public static String computeHighestScore() {
        ArrayList<Integer> scores = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(scoresFile);
            while (scanner.hasNextLine()) {
                scores.add(Integer.parseInt(scanner.nextLine()));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Integer.toString(scores.stream().mapToInt(score -> score).max().orElse(0));
    }

    /**
     * Computes Highest amount of moves of moves in movesFile
     * @return Highest amount of Moves
     */
    public static String computeAverageMoves() {
        ArrayList<Integer> moves = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(movesFile);
            while (scanner.hasNextLine()) {
                moves.add(Integer.parseInt(scanner.nextLine()));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return format.format(moves.stream().mapToDouble(score -> (double) score).average().orElse(0.0));
    }

    /**
     * Computes Average amount of moves of moves in movesFile
     * @return Average Moves
     */
    public static String computeHighestMoves() {
        ArrayList<Integer> moves = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(movesFile);
            while (scanner.hasNextLine()) {
                moves.add(Integer.parseInt(scanner.nextLine()));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Integer.toString(moves.stream().mapToInt(score -> score).max().orElse(0));
    }
}
