/**
 * SystemManager.java
 * 
 * Handles the main program logic for Green Property Exchange (MCO1).
 * This class manages property creation, viewing, modification, and booking.
 * Ensures unique property names and enforces system rules.
 * 
 * MCO1 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten ,Julian Nicos Reyes
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Scanner;

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
        System.out.println("\n-- CREATE PROPERTY LISTING --");
        System.out.print("Enter Property Name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Property name cannot be blank.");
            return;
        }

        if (findProperty(name) != null) {
            System.out.println("Property name must be unique.");
            return;
        }

        Property newProp = new Property(name);

        System.out.print("Enter number of available dates (1–30): ");
        int numDates = getValidatedInt(1, 30);
        for (int i = 1; i <= numDates; i++) {
            newProp.addDate(i);
        }

        properties.add(newProp);
        System.out.println("Property '" + name + "' successfully created!");
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
            System.out.println("No properties available to view.");
            return;
        }

        System.out.println("\n-- VIEW PROPERTY --");
        listProperties();
        System.out.print("Enter property name to view: ");
        String name = sc.nextLine().trim();
        Property prop = findProperty(name);

        if (prop == null) {
            System.out.println("Property not found.");
            return;
        }

        prop.displayInfo();
        System.out.println("\n(Calendar view and detailed info will be shown once Date & Reservation classes are complete.)");
    }

    // -------------------------------------------------------
    // MANAGE PROPERTY
    // -------------------------------------------------------

    /**
     * Allows user to modify existing property details.
     */
    public void manageProperty() {
        if (properties.isEmpty()) {
            System.out.println("No properties to manage.");
            return;
        }

        System.out.println("\n-- MANAGE PROPERTY --");
        listProperties();
        System.out.print("Enter property name to manage: ");
        String name = sc.nextLine().trim();
        Property prop = findProperty(name);

        if (prop == null) {
            System.out.println("Property not found.");
            return;
        }

        int choice;
        do {
            System.out.println("\nManaging: " + prop.getName());
            System.out.println("[1] Change Property Name");
            System.out.println("[2] Change Price per Night");
            System.out.println("[3] Remove this Property");
            System.out.println("[4] Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = getValidatedInt(1, 4);

            switch (choice) {
                case 1:
                    System.out.print("Enter new property name: ");
                    String newName = sc.nextLine().trim();
                    if (findProperty(newName) != null) {
                        System.out.println("Another property already uses that name.");
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
                    if (!prop.getReservations().isEmpty()) {
                        System.out.println("Cannot remove property with active reservations.");
                    } else {
                        properties.remove(prop);
                        System.out.println("Property '" + name + "' removed successfully.");
                        return;
                    }
                    break;

                case 4:
                    System.out.println("Returning to main menu...");
                    break;
            }
        } while (choice != 4);
    }

    // -------------------------------------------------------
    // SIMULATE BOOKING
    // -------------------------------------------------------

    /**
     * Placeholder for simulate booking logic (implemented after Reservation.java).
     */
    public void simulateBooking() {
        System.out.println("\n-- SIMULATE BOOKING --");
        if (properties.isEmpty()) {
            System.out.println("❌ No properties available to book.");
            return;
        }

        System.out.println("(Feature available after Reservation class implementation.)");
    }

    // -------------------------------------------------------
    // UTILITY METHODS
    // -------------------------------------------------------

    /**
     * Lists all property names.
     */
    public void listProperties() {
        System.out.println("\nCurrent Properties:");
        for (Property p : properties) {
            System.out.println(" - " + p.getName());
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
                    System.out.print("Enter a number between " + min + " and " + max + ": ");
                    continue;
                }
                return num;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
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
                    System.out.print("Enter a valid price (" + min + "–" + max + "): ");
                    continue;
                }
                return num;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }
}


