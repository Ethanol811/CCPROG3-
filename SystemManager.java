import java.util.ArrayList;
import java.util.Scanner;

/**
 * SystemManager.java
 * 
 * Handles the main program logic for Green Property Exchange (MCO1).
 * This class manages property creation, viewing, modification, and booking.
 * Ensures unique property names and enforces system rules.
 * 
 * MCO1 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten ,Julian Nicos Reyes
 * @version 1.8
 */
public class SystemManager {
    private ArrayList<Property> properties;
    private Scanner sc;

    /** 
     * Initializes the SystemManager with an empty list of properties.
     */
    public SystemManager() {
        properties = new ArrayList<>();
        sc = new Scanner(System.in);
    }

    // -------------------------------------------------------
    // CREATE PROPERTY
    // -------------------------------------------------------

    /**
     * Handles property creation based on MCO1 specifications.
     * Ensures the property name is unique and initializes dates.
     */
    public void createProperty() {
        System.out.println("\n=== CREATE PROPERTY LISTING ===");
        System.out.println("-----------------------------------");
        System.out.print("Enter Property Name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("[ERROR] Property name cannot be blank.");
            return;
        }

        if (findProperty(name) != null) {
            System.out.println("[ERROR] Property name must be unique. '" + name + "' already exists.");
            return;
        }

        Property newProp = new Property(name);

        System.out.print("Enter number of available dates (1-30): ");
        int numDates = getValidatedInt(1, 30);
        
        System.out.println("\nEnter the specific day numbers (1-30) for available dates:");
        for (int i = 0; i < numDates; i++) {
            System.out.print("Date " + (i + 1) + ": ");
            int dayNumber = getValidatedInt(1, 30);
            
            // Check if this day is already added to avoid duplicates
            boolean alreadyExists = false;
            for (Date date : newProp.getDates()) {
                if (date.getDayNumber() == dayNumber) {
                    System.out.println("[ERROR] Day " + dayNumber + " is already added. Please choose a different day.");
                    alreadyExists = true;
                    i--; // Retry this iteration
                    break;
                }
            }
            
            if (!alreadyExists) {
                newProp.addDate(dayNumber);
            }
        }

        properties.add(newProp);
        System.out.println("[SUCCESS] Property '" + name + "' successfully created with " + numDates + " available dates!");
    }

    // -------------------------------------------------------
    // VIEW PROPERTY
    // -------------------------------------------------------

    /**
     * Displays existing property information.
     * Shows list of properties and prompts user to select one.
     */
    public void viewProperty() {
        if (properties.isEmpty()) {
            System.out.println("[ERROR] No properties available to view.");
            return;
        }

        System.out.println("\n=== VIEW PROPERTY ===");
        System.out.println("-----------------------------------");
        listProperties();
        System.out.print("Enter property name to view: ");
        String name = sc.nextLine().trim();
        Property prop = findProperty(name);

        if (prop == null) {
            System.out.println("[ERROR] Property '" + name + "' not found.");
            return;
        }

        int choice;
        do {
            System.out.println("\n=== VIEWING: " + prop.getName() + " ===");
            System.out.println("-----------------------------------");
            System.out.println("1. High-level Information");
            System.out.println("2. Calendar View");
            System.out.println("3. Date Information");
            System.out.println("4. Reservation Information");
            System.out.println("5. All Reservations");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = getValidatedInt(1, 6);

            switch (choice) {
                case 1:
                    prop.displayInfo();
                    break;
                    
                case 2:
                    prop.displayCalendar();
                    break;
                    
                case 3:
                    System.out.print("Enter day number to view (1-30): ");
                    int dayNumber = getValidatedInt(1, 30);
                    prop.displayDateInfo(dayNumber);
                    break;
                    
                case 4:
                    System.out.print("Enter start day of range (1-30): ");
                    int startDay = getValidatedInt(1, 30);
                    System.out.print("Enter end day of range (" + startDay + "-30): ");
                    int endDay = getValidatedInt(startDay, 30);
                    prop.displayReservationInfo(startDay, endDay);
                    break;
                    
                case 5:
                    if (prop.getReservations().isEmpty()) {
                        System.out.println("[INFO] No reservations for this property.");
                    } else {
                        System.out.println("\n=== ALL RESERVATIONS ===");
                        System.out.println("-----------------------------------");
                        for (Reservation reservation : prop.getReservations()) {
                            reservation.displayReservation();
                        }
                    }
                    break;
                    
                case 6:
                    System.out.println("Returning to main menu...");
                    break;
            }
        } while (choice != 6);
    }

    // -------------------------------------------------------
    // MANAGE PROPERTY
    // -------------------------------------------------------

    /**
     * Allows user to modify existing property details.
     */
    public void manageProperty() {
        if (properties.isEmpty()) {
            System.out.println("[ERROR] No properties to manage.");
            return;
        }

        System.out.println("\n=== MANAGE PROPERTY ===");
        System.out.println("-----------------------------------");
        listProperties();
        System.out.print("Enter property name to manage: ");
        String name = sc.nextLine().trim();
        Property prop = findProperty(name);

        if (prop == null) {
            System.out.println("[ERROR] Property '" + name + "' not found.");
            return;
        }

        int choice;
        do {
            System.out.println("\n=== MANAGING: " + prop.getName() + " ===");
            System.out.println("-----------------------------------");
            System.out.println("1. Change Property Name");
            System.out.println("2. Change Price per Night");
            System.out.println("3. Add Date");
            System.out.println("4. Remove Date");
            System.out.println("5. Remove this Property");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = getValidatedInt(1, 6);

            switch (choice) {
                case 1:
                    System.out.print("Enter new property name: ");
                    String newName = sc.nextLine().trim();
                    if (findProperty(newName) != null && !newName.equalsIgnoreCase(prop.getName())) {
                        System.out.println("[ERROR] Another property already uses that name.");
                    } else {
                        prop.setName(newName);
                    }
                    break;

                case 2:
                    System.out.print("Enter new base price (>= 100): ");
                    double newPrice = getValidatedDouble(100, 999999);
                    prop.setBasePrice(newPrice);
                    break;

                case 3:
                    if (prop.getDates().size() >= 30) {
                        System.out.println("[ERROR] Cannot add more than 30 dates.");
                    } else {
                        System.out.print("Enter day number to add (1-30): ");
                        int dayToAdd = getValidatedInt(1, 30);
                        prop.addDate(dayToAdd);
                    }
                    break;

                case 4:
                    System.out.print("Enter day number to remove (1-30): ");
                    int dayToRemove = getValidatedInt(1, 30);
                    prop.removeDate(dayToRemove);
                    break;

                case 5:
                    if (!prop.getReservations().isEmpty()) {
                        System.out.println("[ERROR] Cannot remove property with active reservations.");
                    } else {
                        properties.remove(prop);
                        System.out.println("[SUCCESS] Property '" + name + "' removed successfully.");
                        return;
                    }
                    break;

                case 6:
                    System.out.println("Returning to main menu...");
                    break;
            }
        } while (choice != 6);
    }

    // -------------------------------------------------------
    // SIMULATE BOOKING
    // -------------------------------------------------------

    /**
     * Handles the booking simulation process.
     */
    public void simulateBooking() {
        System.out.println("\n=== SIMULATE BOOKING ===");
        System.out.println("-----------------------------------");
        if (properties.isEmpty()) {
            System.out.println("[ERROR] No properties available to book.");
            return;
        }

        listProperties();
        System.out.print("Enter property name to book: ");
        String propName = sc.nextLine().trim();
        Property prop = findProperty(propName);

        if (prop == null) {
            System.out.println("[ERROR] Property '" + propName + "' not found.");
            return;
        }

        // Show property calendar first
        System.out.println("\nChecking availability for: " + prop.getName());
        prop.displayCalendar();

        System.out.print("\nEnter guest name: ");
        String guestName = sc.nextLine().trim();
        if (guestName.isEmpty()) {
            System.out.println("[ERROR] Guest name cannot be blank.");
            return;
        }

        System.out.print("Enter check-in day (1-29): ");
        int checkIn = getValidatedInt(1, 29);
        
        System.out.print("Enter check-out day (" + (checkIn + 1) + "-30): ");
        int checkOut = getValidatedInt(checkIn + 1, 30);

        // Validate booking constraints from specifications
        if (checkOut == 1) {
            System.out.println("[ERROR] Cannot have check-out on day 1.");
            return;
        }
        
        if (checkIn == 30) {
            System.out.println("[ERROR] Cannot have check-in on day 30.");
            return;
        }

        // Check if dates are available
        if (!prop.areDatesAvailable(checkIn, checkOut)) {
            System.out.println("[ERROR] Selected dates are not available for booking.");
            return;
        }

        // Create and process reservation
        Reservation reservation = new Reservation(guestName, checkIn, checkOut);
        reservation.calculateTotal(prop.getDates());
        
        // Display booking summary
        System.out.println("\n=== BOOKING SUMMARY ===");
        System.out.println("-----------------------------------");
        System.out.println("Property: " + prop.getName());
        reservation.displayReservation();
        
        System.out.print("\nConfirm booking? (Y/N): ");
        String confirm = sc.nextLine().trim().toUpperCase();
        
        if (confirm.equals("Y")) {
            // Book the dates and add reservation
            prop.bookDates(checkIn, checkOut);
            prop.addReservation(reservation);
            System.out.println("[SUCCESS] Booking confirmed successfully!");
            
            // Show updated calendar
            System.out.println("\nUpdated Calendar for " + prop.getName() + ":");
            prop.displayCalendar();
        } else {
            System.out.println("[INFO] Booking cancelled.");
        }
    }

    // -------------------------------------------------------
    // UTILITY METHODS
    // -------------------------------------------------------

    /**
     * Lists all property names.
     */
    public void listProperties() {
        System.out.println("\nCURRENT PROPERTIES:");
        if (properties.isEmpty()) {
            System.out.println("   No properties available.");
        } else {
            for (int i = 0; i < properties.size(); i++) {
                System.out.println("   " + (i + 1) + ". " + properties.get(i).getName());
            }
        }
    }

    /**
     * Finds a property by name.
     * @param name property name
     * @return Property object or null if not found
     */
    private Property findProperty(String name) {
        for (Property p : properties) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Validates integer input between min and max.
     */
    private int getValidatedInt(int min, int max) {
        while (true) {
            try {
                int num = Integer.parseInt(sc.nextLine().trim());
                if (num < min || num > max) {
                    System.out.print("[ERROR] Enter a number between " + min + " and " + max + ": ");
                    continue;
                }
                return num;
            } catch (NumberFormatException e) {
                System.out.print("[ERROR] Invalid input. Enter a number: ");
            }
        }
    }

    /**
     * Validates double input within a range.
     */
    private double getValidatedDouble(double min, double max) {
        while (true) {
            try {
                double num = Double.parseDouble(sc.nextLine().trim());
                if (num < min || num > max) {
                    System.out.print("[ERROR] Enter a valid price (PHP " + min + " - " + max + "): ");
                    continue;
                }
                return num;
            } catch (NumberFormatException e) {
                System.out.print("[ERROR] Invalid input. Enter a number: ");
            }
        }
    }
}