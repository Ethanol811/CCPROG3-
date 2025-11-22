/**
 * SystemManager.java
 *
 * Main controller class for the Green Property Exchange system.
 * Handles property management, booking simulation, and user interaction.
 *
 * MCO1 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 2.0
 */

import java.util.ArrayList;
import java.util.Scanner;

public class SystemManager {
    private ArrayList<Property> properties;
    private Scanner sc;

    /**
     * Constructs a new SystemManager with empty property list.
     */
    public SystemManager() {
        this.properties = new ArrayList<Property>();
        this.sc = new Scanner(System.in);
    }

    /**
     * Handles the complete property creation process.
     */
    public void createProperty() {
        try {
            System.out.println("\n=== CREATE PROPERTY LISTING ===");
            System.out.println("-----------------------------------");

            System.out.print("Enter Property Name: ");
            String name = sc.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("[ERROR] Property name cannot be empty.");
                return;
            }

            if (findProperty(name) != null) {
                System.out.println("[ERROR] Property name must be unique. '" + name + "' already exists.");
                return;
            }

            System.out.print("Enter property type (Eco-Apartment, Sustainable House, Green Resort, Eco-Glamping): ");
            String propertyType = sc.nextLine().trim();

            if (propertyType.isEmpty()) {
                System.out.println("[ERROR] Property type cannot be empty.");
                return;
            }

            Property newProp = createPropertyByType(name, propertyType);
            if (newProp == null) {
                System.out.println("[ERROR] Invalid property type.");
                return;
            }

            newProp.setPropertyType(propertyType);
            setupPropertyDates(newProp);
            properties.add(newProp);

            System.out.println("[SUCCESS] Property '" + name + "' created successfully!");

        } catch (Exception e) {
            System.out.println("[ERROR] Error creating property: " + e.getMessage());
        }
    }

    /**
     * Handles property viewing with multiple display options.
     */
    public void viewProperty() {
        try {
            if (properties.isEmpty()) {
                System.out.println("[INFO] No properties available to view.");
                return;
            }

            Property prop = selectProperty("view");
            if (prop == null) return;

            boolean continueViewing = true;
            while (continueViewing) {
                System.out.println("\n=== VIEWING: " + prop.getName() + " ===");
                System.out.println("-----------------------------------");
                System.out.println("1. High-level Information");
                System.out.println("2. Calendar View");
                System.out.println("3. Date Information");
                System.out.println("4. Reservation Information");
                System.out.println("5. All Reservations");
                System.out.println("6. Back to Main Menu");
                System.out.print("Enter choice: ");

                int choice = getValidatedInt(1, 6);

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
                            for (Reservation reservation : prop.getReservations()) {
                                reservation.displayReservation();
                            }
                        }
                        break;
                    case 6:
                        continueViewing = false;
                        System.out.println("Returning to main menu...");
                        break;
                }
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Error viewing property: " + e.getMessage());
        }
    }

    /**
     * Manages property modifications.
     */
    public void manageProperty() {
        try {
            if (properties.isEmpty()) {
                System.out.println("[INFO] No properties to manage.");
                return;
            }

            Property prop = selectProperty("manage");
            if (prop == null) return;

            boolean continueManaging = true;
            while (continueManaging) {
                System.out.println("\n=== MANAGING: " + prop.getName() + " ===");
                System.out.println("-----------------------------------");
                System.out.println("1. Change Property Name");
                System.out.println("2. Change Price per Night");
                System.out.println("3. Change Property Type");
                System.out.println("4. Add Date");
                System.out.println("5. Remove Date");
                System.out.println("6. Set Environmental Modifier");
                System.out.println("7. Remove this Property");
                System.out.println("8. Back to Main Menu");
                System.out.print("Enter choice: ");

                int choice = getValidatedInt(1, 8);

                switch (choice) {
                    case 1:
                        System.out.print("Enter new property name: ");
                        String newName = sc.nextLine().trim();
                        try {
                            prop.setName(newName);
                            System.out.println("[SUCCESS] Property name updated.");
                        } catch (Exception e) {
                            System.out.println("[ERROR] " + e.getMessage());
                        }
                        break;
                    case 2:
                        System.out.print("Enter new base price (>= 100): ");
                        double newPrice = getValidatedDouble(100, 999999);
                        try {
                            prop.setBasePrice(newPrice);
                            System.out.println("[SUCCESS] Base price updated.");
                        } catch (Exception e) {
                            System.out.println("[ERROR] " + e.getMessage());
                        }
                        break;
                    case 3:
                        System.out.print("Enter new property type: ");
                        String newType = sc.nextLine().trim();
                        try {
                            prop.setPropertyType(newType);
                            System.out.println("[SUCCESS] Property type updated.");
                        } catch (Exception e) {
                            System.out.println("[ERROR] " + e.getMessage());
                        }
                        break;
                    case 4:
                        if (prop.getDates().size() >= 30) {
                            System.out.println("[ERROR] Cannot add more than 30 dates.");
                        } else {
                            System.out.print("Enter day number to add (1-30): ");
                            int dayToAdd = getValidatedInt(1, 30);
                            try {
                                prop.addDate(dayToAdd);
                                System.out.println("[SUCCESS] Date added.");
                            } catch (Exception e) {
                                System.out.println("[ERROR] " + e.getMessage());
                            }
                        }
                        break;
                    case 5:
                        System.out.print("Enter day number to remove (1-30): ");
                        int dayToRemove = getValidatedInt(1, 30);
                        try {
                            prop.removeDate(dayToRemove);
                            System.out.println("[SUCCESS] Date removed.");
                        } catch (Exception e) {
                            System.out.println("[ERROR] " + e.getMessage());
                        }
                        break;
                    case 6:
                        System.out.print("Enter day number to modify (1-30): ");
                        int dayToModify = getValidatedInt(1, 30);
                        System.out.print("Enter new environmental modifier (0.8 - 1.2): ");
                        double modifier = getValidatedDouble(0.8, 1.2);
                        try {
                            prop.setEnvironmentalModifier(dayToModify, modifier);
                            System.out.println("[SUCCESS] Environmental modifier updated.");
                        } catch (Exception e) {
                            System.out.println("[ERROR] " + e.getMessage());
                        }
                        break;
                    case 7:
                        try {
                            if (removeProperty(prop)) {
                                System.out.println("[SUCCESS] Property removed successfully.");
                                return;
                            }
                        } catch (Exception e) {
                            System.out.println("[ERROR] " + e.getMessage());
                        }
                        break;
                    case 8:
                        continueManaging = false;
                        System.out.println("Returning to main menu...");
                        break;
                }
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Error managing property: " + e.getMessage());
        }
    }

    /**
     * Processes booking simulation.
     */
    public void simulateBooking() {
        try {
            if (properties.isEmpty()) {
                System.out.println("[INFO] No properties available to book.");
                return;
            }

            Property prop = selectProperty("book");
            if (prop == null) return;

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

        } catch (Exception e) {
            System.out.println("[ERROR] Error processing booking: " + e.getMessage());
        }
    }

    // Utility Methods

    /**
     * Guides user through property selection.
     * @param action the action being performed
     * @return the selected property
     */
    private Property selectProperty(String action) {
        listProperties();
        System.out.print("Enter property name to " + action + ": ");
        String name = sc.nextLine().trim();

        Property prop = findProperty(name);
        if (prop == null) {
            System.out.println("[ERROR] Property '" + name + "' not found.");
        }
        return prop;
    }

    /**
     * Creates a property instance based on the specified type.
     * @param name the property name
     * @param type the property type
     * @return a new Property instance
     */
    private Property createPropertyByType(String name, String type) {
        if (type.equalsIgnoreCase("Eco-Apartment")) {
            return new EcoApartmentFeeCalc(name);
        } else if (type.equalsIgnoreCase("Sustainable House")) {
            return new SustainableHouseFeeCalc(name);
        } else if (type.equalsIgnoreCase("Green Resort")) {
            return new GreenResortFeeCalc(name);
        } else if (type.equalsIgnoreCase("Eco-Glamping")) {
            return new EcoGlampingFeeCalc(name);
        } else {
            return null;
        }
    }

    /**
     * Guides user through setting up available dates.
     * @param property the property to add dates to
     */
    private void setupPropertyDates(Property property) {
        System.out.print("Enter number of available dates (1-30): ");
        int numDates = getValidatedInt(1, 30);

        System.out.println("\nEnter the specific day numbers (1-30) for available dates:");
        for (int i = 0; i < numDates; i++) {
            System.out.print("Date " + (i + 1) + ": ");
            int dayNumber = getValidatedInt(1, 30);

            try {
                property.addDate(dayNumber);
            } catch (IllegalArgumentException e) {
                System.out.println("[ERROR] " + e.getMessage() + " Please choose a different day.");
                i--; // Retry this iteration
            }
        }
    }

    /**
     * Displays a formatted list of all properties.
     */
    public void listProperties() {
        System.out.println("\nCURRENT PROPERTIES:");
        if (properties.isEmpty()) {
            System.out.println("   No properties available.");
        } else {
            for (int i = 0; i < properties.size(); i++) {
                Property p = properties.get(i);
                System.out.println("   " + (i + 1) + ". " + p.getName() + " (" + p.getPropertyType() + ")");
            }
        }
    }

    /**
     * Finds a property by name.
     * @param name the property name to search for
     * @return the property if found, null otherwise
     */
    public Property findProperty(String name) {
        for (Property p : properties) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Removes a property from the system.
     * @param property the property to remove
     * @return true if removal was successful
     */
    public boolean removeProperty(Property property) {
        if (property == null) {
            return false;
        }

        // Check if property has any reservations
        if (!property.getReservations().isEmpty()) {
            System.out.println("[ERROR] Cannot remove property with active reservations.");
            return false;
        }

        return properties.remove(property);
    }

    /**
     * Gets a validated integer input within specified range.
     * @param min minimum allowed value
     * @param max maximum allowed value
     * @return validated integer
     */
    private int getValidatedInt(int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("[ERROR] Enter a number between %d and %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("[ERROR] Invalid input. Enter a whole number: ");
            }
        }
    }

    /**
     * Gets a validated double input within specified range.
     * @param min minimum allowed value
     * @param max maximum allowed value
     * @return validated double
     */
    private double getValidatedDouble(double min, double max) {
        while (true) {
            try {
                double value = Double.parseDouble(sc.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("[ERROR] Enter a number between %.1f and %.1f: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("[ERROR] Invalid input. Enter a number: ");
            }
        }
    }
}