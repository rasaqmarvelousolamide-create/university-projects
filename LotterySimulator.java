import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class LotterySimulator {

    public static void main(String[] args) {
        System.out.println("=== Welcome to the Lottery Simulator ===\n");

        int[] userNumbers = new int[6];
        int[] drawnNumbers = new int[6];

        userNumbers = getUserNumbers();
        drawnNumbers = generateLotteryNumbers();

        System.out.println("\nYour selected numbers: " + arrayToString(userNumbers));
        System.out.println("Lottery drawn numbers: " + arrayToString(drawnNumbers));

        writeToFile(userNumbers, drawnNumbers);

        System.out.println("\n--- Reading results from file ---");
        readFromFileAndCompare();
    }

    public static int[] generateLotteryNumbers() {
        Random rand = new Random();
        int[] numbers = new int[6];
        int count = 0;

        while (count < 6) {
            int num = rand.nextInt(50) + 1;
            if (!existsInArray(numbers, num, count)) {
                numbers[count] = num;
                count++;
            }
        }
        return numbers;
    }

    public static int[] getUserNumbers() {
        Scanner scanner = new Scanner(System.in);
        int[] numbers = new int[6];
        int count = 0;

        System.out.println("Please enter your 6 unique lottery numbers (1-50):");

        while (count < 6) {
            System.out.print("Number " + (count + 1) + ": ");
            if (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                if (num >= 1 && num <= 50 && !existsInArray(numbers, num, count)) {
                    numbers[count] = num;
                    count++;
                } else {
                    System.out.println("Invalid! Must be 1-50 and unique. Try again.");
                }
            } else {
                System.out.println("Please enter a number.");
                scanner.next();
            }
        }
        return numbers;
    }

    private static boolean existsInArray(int[] arr, int num, int length) {
        for (int i = 0; i < length; i++) {
            if (arr[i] == num) {
                return true;
            }
        }
        return false;
    }

    public static int countMatches(int[] arr1, int[] arr2) {
        int matches = 0;
        for (int num1 : arr1) {
            for (int num2 : arr2) {
                if (num1 == num2) {
                    matches++;
                    break;
                }
            }
        }
        return matches;
    }

    public static void writeToFile(int[] user, int[] drawn) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("lottery_results.txt"))) {
            writer.println("USER:" + arrayToString(user));
            writer.println("DRAWN:" + arrayToString(drawn));
            System.out.println("Results successfully written to lottery_results.txt");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void readFromFileAndCompare() {
        int[] userRead = new int[6];
        int[] drawnRead = new int[6];

        try (Scanner fileScanner = new Scanner(new File("lottery_results.txt"))) {
            String userLine = fileScanner.nextLine();
            if (userLine.startsWith("USER:")) {
                String[] parts = userLine.substring(5).trim().split(" ");
                for (int i = 0; i < 6; i++) {
                    userRead[i] = Integer.parseInt(parts[i]);
                }
            }

            String drawnLine = fileScanner.nextLine();
            if (drawnLine.startsWith("DRAWN:")) {
                String[] parts = drawnLine.substring(6).trim().split(" ");
                for (int i = 0; i < 6; i++) {
                    drawnRead[i] = Integer.parseInt(parts[i]);
                }
            }

            int matches = countMatches(userRead, drawnRead);
            System.out.println("Your numbers : " + arrayToString(userRead));
            System.out.println("Drawn numbers: " + arrayToString(drawnRead));
            System.out.println("You have " + matches + " matching number(s)!");

        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int num : arr) {
            sb.append(num).append(" ");
        }
        return sb.toString().trim();
    }
}
