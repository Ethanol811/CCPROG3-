/**
 * Property.java
 * 
 * Represents a property listing in the Green Property Exchange system.
 * Each property contains its name, base price per night, a list of available dates,
 * and a list of reservations (initially empty). 
 * 
 * MCO1 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten ,Julian Nicos Reyes
 * @version 1.0
 */

import java.util.ArrayList;

public class Property {
    private String name;
    private double basePrice;
    private ArrayList<Date> dates;
    private ArrayList<Reservation> reservations;

    /** 
     * Default base price for all properties (PHP 1,500.00 per night).
     * Initializes empty lists for dates and reservations.
     * @param name  property name
     */
    public Property(String name) {
        if (name == null || name.trim().isEmpty()) {
            this.name = "Unnamed Property";
        } else {
            this.name = name.trim();
        }
        this.basePrice = 1500.00;
        this.dates = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    // -------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------

    /**
     * @return property name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the property's name.
     * Validation for unique names should be handled in SystemManager.
     * @param newName new property name
     */
    public void setName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            System.out.println("Invalid name. Property name cannot be blank.");
        } else {
            this.name = newName.trim();
            System.out.println("Property name successfully changed to: " + this.name);
        }
    }

    /**
     * @return base price per night
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * Updates the base price for all dates.
     * Can only be changed if there are no reservations.
     * @param newPrice new base price (must be >= 100 PHP)
     */
    public void setBasePrice(double newPrice) {
        if (newPrice < 100) {
            System.out.println("New price must be at least PHP 100.00.");
        } else if (!reservations.isEmpty()) {
            System.out.println("Cannot change base price while reservations exist.");
        } else {
            this.basePrice = newPrice;
            for (Date d : dates) {
                d.setPricePerNight(newPrice);
            }
            System.out.println("Base price successfully updated to PHP " + newPrice);
        }
    }

    /**
     * @return list of Date objects
     */
    public ArrayList<Date> getDates() {
        return dates;
    }

    /**
     * @return list of Reservation objects
     */
    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    // -------------------------------------------------------
    // Core Property Methods
    // -------------------------------------------------------

    /**
     * Adds a new available date to the property (max 30).
     * @param dayNumber the day number (1 – 30)
     */
    public void addDate(int dayNumber) {
        if (dates.size() >= 30) {
            System.out.println("Cannot add more than 30 dates.");
            return;
        }
        if (dayNumber < 1 || dayNumber > 30) {
            System.out.println("Invalid day number. Must be 1–30.");
            return;
        }

        // check for duplicates
        for (Date d : dates) {
            if (d.getDayNumber() == dayNumber) {
                System.out.println("Day " + dayNumber + " already exists in this property.");
                return;
            }
        }

        dates.add(new Date(dayNumber, basePrice));
        System.out.println("Added date " + dayNumber + " with price ₱" + basePrice);
    }

    /**
     * Removes a date by its number.
     * @param dayNumber day number to remove
     */
    public void removeDate(int dayNumber) {
        for (int i = 0; i < dates.size(); i++) {
            if (dates.get(i).getDayNumber() == dayNumber) {
                dates.remove(i);
                System.out.println("Removed date " + dayNumber);
                return;
            }
        }
        System.out.println("Date " + dayNumber + " not found.");
    }

    /**
     * Calculates total earnings for all reservations.
     * @return total revenue for the property
     */
    public double calculateEarnings() {
        double total = 0;
        for (Reservation r : reservations) {
            total += r.getTotalPrice();
        }
        return total;
    }

    /**
     * Displays summary info for the property.
     */
    public void displayInfo() {
        System.out.println("\n-- PROPERTY INFO --");
        System.out.println("Name: " + name);
        System.out.println("Base Price: ₱" + basePrice);
        System.out.println("Available Dates: " + dates.size());
        System.out.println("Total Earnings: ₱" + calculateEarnings());
    }
}

