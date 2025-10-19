/**
 * Driver.java
 * 
 * Main entry point for the Green Property Exchange console simulation.
 * Provides navigation for creating, viewing, managing, and booking properties.
 * 
 * MCO1 - Green Property Exchange
 * @author Group 23
 * @version 1.0
 */

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SystemManager manager = new SystemManager();

        int choice;
        do {
            System.out.println("\n==================================");
            System.out.println(" GREEN PROPERTY EXCHANGE (MCO1)");
            System.out.println("==================================");
            System.out.println("1. Create Property");
            System.out.println("2. View Property");
            System.out.println("3. Manage Property");
            System.out.println("4. Simulate Booking");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number (1–5): ");
                sc.next(); // discard invalid input
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    manager.createProperty();
                    break;
                case 2:
                    manager.viewProperty();
                    break;
                case 3:
                    manager.manageProperty();
                    break;
                case 4:
                    manager.simulateBooking();
                    break;
                case 5:
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1–5.");
            }
        } while (choice != 5);

        sc.close();
    }
}
