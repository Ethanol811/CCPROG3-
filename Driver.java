/**
 * Driver.java
 *
 * Main entry point for the Green Property Exchange MCO2 GUI application.
 * Launches the Swing-based GUI interface instead of the console version.
 *
 * MCO2 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 2.0
 */

import javax.swing.*;

public class Driver {

    /**
     * Main method that serves as the entry point for the Green Property Exchange MCO2 GUI application.
     * Launches the Swing-based user interface with enhanced calendar view and mouse controls.
     *
     * @param args command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // Set system look and feel for native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Warning: Could not set system look and feel. Using default.");
        }

        // Launch the GUI application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Create and display the main application frame
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                
                System.out.println("Green Property Exchange MCO2 GUI launched successfully!");
                
            } catch (Exception e) {
                // Fallback to console version if GUI fails
                System.err.println("GUI initialization failed: " + e.getMessage());
                System.err.println("Falling back to console version...");
                
                // Launch console version as backup
                launchConsoleVersion();
            }
        });
    }

    /**
     * Fallback method to launch the console version if GUI initialization fails.
     */
    private static void launchConsoleVersion() {
        System.out.println("\n=== LAUNCHING CONSOLE VERSION ===");
        
        java.util.Scanner sc = new java.util.Scanner(System.in);
        SystemManager manager = new SystemManager(sc);

        int choice;
        do {
            System.out.println("\n==================================");
            System.out.println("    GREEN PROPERTY EXCHANGE");
            System.out.println("     MCO2 - CONSOLE VERSION");
            System.out.println("==================================");
            System.out.println("1. Create Property");
            System.out.println("2. View Property");
            System.out.println("3. Manage Property");
            System.out.println("4. Simulate Booking");
            System.out.println("5. Exit System");
            System.out.println("==================================");
            System.out.print("Choose an option (1-5): ");

            while (!sc.hasNextInt()) {
                System.out.print("[ERROR] Invalid input. Enter a number (1-5): ");
                sc.next();
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
                    System.out.println("\nThank you for using Green Property Exchange!");
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please select 1-5.");
            }
        } while (choice != 5);

        sc.close();
    }
}