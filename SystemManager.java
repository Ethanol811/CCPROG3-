// SystemManager.java
/**
 * SystemManager.java
 *
 * Main controller class for the Green Property Exchange system.
 * Updated for MCO2 with GUI support, proper price calculations, and environmental impact management.
 * Follows Single Responsibility Principle - manages the system and coordinates between components.
 * Follows Dependency Inversion Principle - depends on abstractions (Property) not concrete implementations.
 *
 * MCO2 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 6.0
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SystemManager {
    private final List<Property> properties;
    private final Scanner sc;
    private final PropertyFactory propertyFactory;

    /**
     * Constructs a new SystemManager with empty property list.
     * @param scanner the Scanner for input (can be null for GUI mode)
     */
    public SystemManager(Scanner scanner) {
        this.properties = new ArrayList<Property>();
        this.sc = scanner;
        this.propertyFactory = new PropertyFactory();
    }

    /**
     * Constructs a new SystemManager for GUI mode (no Scanner needed).
     */
    public SystemManager() {
        this.properties = new ArrayList<Property>();
        this.sc = null;
        this.propertyFactory = new PropertyFactory();
    }

    // GUI Helper Methods following Interface Segregation Principle

    /**
     * Gets all properties in the system for GUI display.
     * Returns defensive copy for encapsulation.
     * @return list of all properties
     */
    public List<Property> getAllProperties() {
        return new ArrayList<>(properties);
    }

    /**
     * Gets property names for GUI dropdowns.
     * @return array of property names
     */
    public String[] getPropertyNames() {
        String[] names = new String[properties.size()];
        for (int i = 0; i < properties.size(); i++) {
            names[i] = properties.get(i).getName();
        }
        return names;
    }

    /**
     * Creates a property with GUI parameters.
     * Follows Single Responsibility Principle - only handles property creation.
     * @param name the property name
     * @param propertyType the type of property
     * @param basePrice the base price
     * @return true if creation successful, false otherwise
     */
public boolean createPropertyGUI(String name, String propertyType, double basePrice) {
    try {
        if (!validatePropertyCreation(name, propertyType, basePrice)) {
            return false;
        }

        Property newProp = propertyFactory.createProperty(name, propertyType);
        if (newProp == null) {
            return false;
        }

        newProp.setPropertyType(propertyType);
        newProp.setBasePrice(basePrice);
        properties.add(newProp);

        // Apply preset environmental impacts AFTER property is fully created
        newProp.getEnvironmentalImpactManager().applyImpactsToProperty(newProp);

        System.out.println("[SUCCESS] Property '" + name + "' created with rate: PHP " +
                String.format("%.2f", newProp.getPropertyRate()));
        System.out.println("All 30 dates have been added and preset environmental impacts applied.");
        return true;

    } catch (Exception e) {
        System.out.println("Error creating property: " + e.getMessage());
        return false;
    }
}

    /**
     * Validates property creation parameters.
     * Follows Single Responsibility Principle - validation logic separated.
     * @param name the property name
     * @param propertyType the property type
     * @param basePrice the base price
     * @return true if valid, false otherwise
     */
    private boolean validatePropertyCreation(String name, String propertyType, double basePrice) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (findProperty(name) != null) {
            return false;
        }
        if (propertyType == null || propertyType.trim().isEmpty()) {
            return false;
        }
        if (basePrice < 100 || basePrice > 999999) {
            return false;
        }
        return true;
    }

    /**
     * Adds dates to a property for GUI.
     * @param propertyName the property name
     * @param dates array of day numbers to add
     * @return true if successful, false otherwise
     */
    public boolean addDatesToPropertyGUI(String propertyName, int[] dates) {
        Property prop = findProperty(propertyName);
        if (prop == null) return false;

        try {
            for (int day : dates) {
                prop.addDate(day);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if dates are available for booking (GUI version).
     * @param propertyName the property name
     * @param checkIn the check-in day
     * @param checkOut the check-out day
     * @return true if dates are available, false otherwise
     */
    public boolean areDatesAvailableGUI(String propertyName, int checkIn, int checkOut) {
        Property prop = findProperty(propertyName);
        if (prop == null) return false;
        return prop.areDatesAvailable(checkIn, checkOut);
    }

    /**
     * Gets unavailable days for a date range (GUI version).
     * @param propertyName the property name
     * @param checkIn the check-in day
     * @param checkOut the check-out day
     * @return list of unavailable days
     */
    public List<Integer> getUnavailableDaysGUI(String propertyName, int checkIn, int checkOut) {
        Property prop = findProperty(propertyName);
        if (prop == null) return new ArrayList<Integer>();
        return prop.getUnavailableDays(checkIn, checkOut);
    }

    /**
     * Calculates price for a booking (GUI version).
     * @param propertyName the property name
     * @param checkIn the check-in day
     * @param checkOut the check-out day
     * @return total price, or -1 if property not found
     */
    public double calculateBookingPriceGUI(String propertyName, int checkIn, int checkOut) {
        Property prop = findProperty(propertyName);
        if (prop == null) return -1;

        Reservation tempReservation = new Reservation("TEMP", checkIn, checkOut);
        tempReservation.calculateTotal(prop.getDates());
        return tempReservation.getTotalPrice();
    }

    /**
     * Processes a booking (GUI version).
     * @param propertyName the property name
     * @param guestName the guest name
     * @param checkIn the check-in day
     * @param checkOut the check-out day
     * @return true if booking successful, false otherwise
     */
    public boolean processBookingGUI(String propertyName, String guestName, int checkIn, int checkOut) {
        Property prop = findProperty(propertyName);
        if (prop == null) return false;

        if (!prop.areDatesAvailable(checkIn, checkOut)) {
            return false;
        }

        Reservation reservation = new Reservation(guestName, checkIn, checkOut);
        reservation.calculateTotal(prop.getDates());
        prop.bookDates(checkIn, checkOut);
        prop.addReservation(reservation);

        return true;
    }

    // Console Methods following Single Responsibility Principle

    /**
     * Handles the complete property creation process with environmental impacts.
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

            Property newProp = propertyFactory.createProperty(name, propertyType);
            if (newProp == null) {
                System.out.println("[ERROR] Invalid property type.");
                return;
            }

            newProp.setPropertyType(propertyType);
            
            // Apply preset environmental impacts
            newProp.getEnvironmentalImpactManager().applyImpactsToProperty(newProp);
            
            properties.add(newProp);

            System.out.println("[SUCCESS] Property '" + name + "' created successfully!");
            System.out.println("Property Rate: PHP " + String.format("%.2f", newProp.getPropertyRate()) + " per night");
            System.out.println("All 30 dates have been added and preset environmental impacts applied.");

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
                System.out.println("6. Environmental Impacts");
                System.out.println("7. Back to Main Menu");
                System.out.print("Enter choice: ");

                int choice = getValidatedInt(1, 7);

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
                        displayAllReservations(prop);
                        break;
                    case 6:
                        displayEnvironmentalImpacts(prop);
                        break;
                    case 7:
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
     * Displays all reservations for a property.
     * Follows Single Responsibility Principle - only handles reservation display.
     * @param prop the property to display reservations for
     */
    private void displayAllReservations(Property prop) {
        if (prop.getReservations().isEmpty()) {
            System.out.println("[INFO] No reservations for this property.");
        } else {
            System.out.println("\n=== ALL RESERVATIONS ===");
            for (Reservation reservation : prop.getReservations()) {
                reservation.displayReservation();
            }
        }
    }

    /**
     * Displays environmental impacts for a property.
     * @param prop the property to display impacts for
     */
    private void displayEnvironmentalImpacts(Property prop) {
        System.out.println("\n=== ENVIRONMENTAL IMPACTS ===");
        System.out.println("-----------------------------------");
        
        boolean hasImpacts = false;
        for (Date date : prop.getDates()) {
            if (!date.getEnvironmentalImpactName().equals("Standard")) {
                System.out.println("Day " + date.getDayNumber() + ": " + 
                        date.getEnvironmentalImpactName() + " (" + 
                        String.format("%.0f", date.getModifier() * 100) + "%) - PHP " + 
                        String.format("%.2f", date.getFinalPrice()));
                hasImpacts = true;
            }
        }
        
        if (!hasImpacts) {
            System.out.println("No custom environmental impacts set.");
            System.out.println("All dates use standard pricing (100% modifier).");
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
                System.out.println("2. Change Base Price");
                System.out.println("3. Change Property Type");
                System.out.println("4. Add Date");
                System.out.println("5. Remove Date");
                System.out.println("6. Set Environmental Impact");
                System.out.println("7. Remove this Property");
                System.out.println("8. Back to Main Menu");
                System.out.print("Enter choice: ");

                int choice = getValidatedInt(1, 8);

                switch (choice) {
                    case 1:
                        changePropertyName(prop);
                        break;
                    case 2:
                        changeBasePrice(prop);
                        break;
                    case 3:
                        changePropertyType(prop);
                        break;
                    case 4:
                        addDateToProperty(prop);
                        break;
                    case 5:
                        removeDateFromProperty(prop);
                        break;
                    case 6:
                        setEnvironmentalImpact(prop);
                        break;
                    case 7:
                        if (removeProperty(prop)) {
                            return;
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
     * Changes property name with validation.
     * Follows Single Responsibility Principle - only handles name change.
     * @param prop the property to modify
     */
    private void changePropertyName(Property prop) {
        System.out.print("Enter new property name: ");
        String newName = sc.nextLine().trim();
        try {
            prop.setName(newName);
            System.out.println("[SUCCESS] Property name updated.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    /**
     * Changes base price with validation.
     * Follows Single Responsibility Principle - only handles price change.
     * @param prop the property to modify
     */
    private void changeBasePrice(Property prop) {
        System.out.print("Enter new base price (>= 100): ");
        double newPrice = getValidatedDouble(100, 999999);
        try {
            prop.setBasePrice(newPrice);
            System.out.println("[SUCCESS] Base price updated to PHP " + String.format("%.2f", newPrice));
            System.out.println("New property rate: PHP " + String.format("%.2f", prop.getPropertyRate()));
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    /**
     * Changes property type with validation.
     * Follows Single Responsibility Principle - only handles type change.
     * @param prop the property to modify
     */
    private void changePropertyType(Property prop) {
        System.out.print("Enter new property type: ");
        String newType = sc.nextLine().trim();
        try {
            prop.setPropertyType(newType);
            System.out.println("[SUCCESS] Property type updated.");
            System.out.println("Property rate: PHP " + String.format("%.2f", prop.getPropertyRate()));
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    /**
     * Adds date to property with validation.
     * Follows Single Responsibility Principle - only handles date addition.
     * @param prop the property to modify
     */
    private void addDateToProperty(Property prop) {
        if (prop.getDates().size() >= 30) {
            System.out.println("[ERROR] Cannot add more than 30 dates. Property already has all dates.");
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
    }

    /**
     * Removes date from property with validation.
     * Follows Single Responsibility Principle - only handles date removal.
     * @param prop the property to modify
     */
    private void removeDateFromProperty(Property prop) {
        System.out.print("Enter day number to remove (1-30): ");
        int dayToRemove = getValidatedInt(1, 30);
        try {
            prop.removeDate(dayToRemove);
            System.out.println("[SUCCESS] Date removed.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    /**
     * Sets environmental impact for a specific date.
     * @param prop the property to modify
     */
    private void setEnvironmentalImpact(Property prop) {
        System.out.print("Enter day number to modify (1-30): ");
        int dayNumber = getValidatedInt(1, 30);
        
        Date date = prop.findDate(dayNumber);
        if (date == null) {
            System.out.println("[ERROR] Day " + dayNumber + " not found in property.");
            return;
        }
        
        if (date.isBooked()) {
            System.out.println("[ERROR] Cannot modify environmental impact for booked date.");
            return;
        }
        
        System.out.println("\nCurrent environmental impact for day " + dayNumber + ":");
        System.out.println("Impact Name: " + date.getEnvironmentalImpactName());
        System.out.println("Modifier: " + String.format("%.0f", date.getModifier() * 100) + "%");
        System.out.println("Final Price: PHP " + String.format("%.2f", date.getFinalPrice()));
        
        System.out.print("\nEnter new environmental impact name: ");
        String impactName = sc.nextLine().trim();
        if (impactName.isEmpty()) {
            impactName = "Custom Impact";
        }
        
        System.out.print("Enter new environmental modifier (0.8-1.2): ");
        double modifier = getValidatedDouble(0.8, 1.2);
        
        try {
            prop.setEnvironmentalModifier(dayNumber, modifier, impactName);
            System.out.println("[SUCCESS] Environmental impact updated!");
            System.out.println("New final price: PHP " + String.format("%.2f", date.getFinalPrice()));
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
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
            System.out.println("Property Type: " + prop.getPropertyType());
            System.out.println("Property Rate: PHP " + String.format("%.2f", prop.getPropertyRate()));
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

    // Utility Methods following Single Responsibility Principle

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
     * Displays a formatted list of all properties.
     */
    public void listProperties() {
        System.out.println("\nCURRENT PROPERTIES:");
        if (properties.isEmpty()) {
            System.out.println("   No properties available.");
        } else {
            for (int i = 0; i < properties.size(); i++) {
                Property p = properties.get(i);
                System.out.println("   " + (i + 1) + ". " + p.getName() + " (" + p.getPropertyType() +
                        ") - Rate: PHP " + String.format("%.2f", p.getPropertyRate()) +
                        " - Dates: " + p.getDates().size() + "/30");
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

        boolean removed = properties.remove(property);
        if (removed) {
            System.out.println("[SUCCESS] Property removed successfully.");
        }
        return removed;
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

    /**
     * Returns the number of properties in the system.
     * @return number of properties
     */
    public int getPropertyCount() {
        return properties.size();
    }

    /**
     * Returns true if the system has no properties.
     * @return true if no properties, false otherwise
     */
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    /**
     * Clears all properties from the system.
     * Use with caution - mainly for testing purposes.
     */
    public void clearAllProperties() {
        properties.clear();
        System.out.println("[INFO] All properties cleared from system.");
    }
}