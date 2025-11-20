/**
 *
 * Date.java
 *
 * Represents a date to be used by Green Property Exchange System
 * Each date has a day number, the price per night, and if the date has been booked already
 *
 * MC01- Green Property Exchange
 * @author Group 23 - John Ethan Chiuten ,Julian Nicos Reyes
 * @version 1.2
 */

public class Date {
    private int dayNumber;
    private double pricePerNight;
    private boolean isBooked;

    /**
     * Initializes empty day for price and booked status
     * @param dayNumber  The exact day number
     * @param pricePerNight  Price per night in a specific day
     */
    public Date(int dayNumber, double pricePerNight){
        this.dayNumber = dayNumber;
        this.pricePerNight = pricePerNight;
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
     * Gets the price per night.
     * @return Price per night
     */
    public double getPricePerNight() { 
        return pricePerNight;
    }

    /**
     * Updates the price per night (used when Property base price changes).
     * @param newPrice The new price per night
     */
    public void setPricePerNight(double newPrice) {
        this.pricePerNight = newPrice;
    }
    
    /**
     *
     * @return If a date is booked
     */
    public boolean isBooked(){
        return isBooked;
    }

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


