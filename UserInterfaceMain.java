import java.util.Scanner;

public class UserInterfaceMain
{
    public static void main(String[] args)
    {
        Scanner keyboard = new Scanner(System.in);

        int choice;

        do
        {
            System.out.println();
            System.out.println("==== Fitness Tracker ====");
            System.out.println("1. Pretty print step statistics");
            System.out.println("2. Convert distance walked into steps");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            choice = keyboard.nextInt();

            System.out.println();

            if (choice == 1)
            {
                int thousands;
                int hundreds;
                int tens;

                System.out.print("Enter thousands: ");
                thousands = keyboard.nextInt();

                System.out.print("Enter hundreds: ");
                hundreds = keyboard.nextInt();

                System.out.print("Enter tens: ");
                tens = keyboard.nextInt();

                StepStats stats;

                stats = new StepStats(thousands, hundreds, tens);

                System.out.println();
                System.out.print("Step Statistics: ");

                stats.prettyPrint();
            }

            else if (choice == 2)
            {
                double kilometres;

                System.out.print("Enter distance in kilometres: ");
                kilometres = keyboard.nextDouble();

                Distance distance;

                distance = new Distance(kilometres);

                StepStats stats;

                stats = distance.convert();

                System.out.println();
                System.out.print("Converted Steps: ");

                stats.prettyPrint();
            }

            else if (choice == 3)
            {
                System.out.println("Program ended.");
            }

            else
            {
                System.out.println("Invalid choice. Please try again.");
            }

        }
        while (choice != 3);

        keyboard.close();
    }
}