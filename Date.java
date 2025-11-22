/**
 *
 * Date.java
 *
 * Represents a date to be used by Green Property Exchange System
 * Each date has a day number, the price per night, and if the date has been booked already
 *
 * MC01- Green Property Exchange
 * @author Group 23 - John Ethan Chiuten ,Julian Nicos Reyes
 * @version 1.3
 */

public class Date {
    private int dayNumber;
    private double basePrice;
    private double modifier;
    private double finalPrice;
    private boolean isBooked;

    /**
     * Initializes empty day for price and booked status
     * @param dayNumber  The exact day number
     * @param pricePerNight  Price per night in a specific day
     */
    public Date(int dayNumber, double basePrice, double modifier){
        this.dayNumber = dayNumber;
        this.basePrice = basePrice;
        this.modifier = modifier;
        this.finalPrice = basePrice * modifier;
        this.isBooked = false;
    }

    // Getters

    /**
     *
     * @return Day Number
     */
    public int getDayNumber(){
        return dayNumber;
    }

    /**
     *
     * @return If a date is booked
     */
    public boolean isBooked(){
        return isBooked;
    }

    /**
     * Gets the base price per night.
     * @return Base price per night
     */
    public double getBasePrice(){
        return basePrice;
    }

    /**
     * Returns final price with environmental modifier in consideration.
     * @return Final price with modifier applied
     */
    public double getFinalPrice(){
        return finalPrice;
    }

    /**
     *
     * @return Environmental modifier of a specific day
     */
    public double getModifier(){
        return modifier;
    }

    // Setters

    /**
     *
     * @param newBasePrice New base price after system management
     */
    public void updatePrice(double newBasePrice) {
        this.basePrice = newBasePrice;
        this.finalPrice = newBasePrice * modifier;
    }

    // Booking Methods
    /**
     * Updates isBooked to true after a successful booking
     */
    public void book(){
        isBooked = true;
    }

    /**
     * Updates isBooked to false if a booking fails
     */
    public void unbook(){
        isBooked = false;
    }
}


