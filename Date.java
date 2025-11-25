/**
 * Date.java
 *
 * Represents a date in the Green Property Exchange System.
 * Each date contains information about its day number, property rate,
 * environmental modifier, final calculated price, and booking status.
 *
 * MC02- Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 3.0
 */

public class Date {
    private int dayNumber;
    private double propertyRate;  // Base price × property type multiplier
    private double modifier;      // Environmental modifier (0.8-1.2)
    private double finalPrice;    // propertyRate × modifier
    private boolean isBooked;

    /**
     * Constructs a new Date with the specified parameters.
     * @param dayNumber the day number (1-30)
     * @param propertyRate the property rate (already includes property type multiplier)
     * @param modifier the environmental modifier (0.8-1.2)
     */
    public Date(int dayNumber, double propertyRate, double modifier) {
        this.dayNumber = dayNumber;
        this.propertyRate = propertyRate;
        this.modifier = modifier;
        this.finalPrice = propertyRate * modifier;
        this.isBooked = false;
    }

    // Getters

    /**
     * Returns the day number of this date.
     * @return the day number
     */
    public int getDayNumber() {
        return dayNumber;
    }

    /**
     * Returns the booking status of this date.
     * @return true if the date is booked, false otherwise
     */
    public boolean isBooked() {
        return isBooked;
    }

    /**
     * Returns the property rate of this date (includes property type multiplier).
     * @return the property rate
     */
    public double getBasePrice() {
        return propertyRate;
    }

    /**
     * Returns the final price of this date (property rate × environmental modifier).
     * @return the final price
     */
    public double getFinalPrice() {
        return finalPrice;
    }

    /**
     * Returns the environmental modifier of this date.
     * @return the environmental modifier
     */
    public double getModifier() {
        return modifier;
    }

    /**
     * Returns the price per night (final price).
     * @return the price per night
     */
    public double getPricePerNight() {
        return finalPrice;
    }

    // Setters

    /**
     * Sets the property rate and recalculates the final price.
     * @param newPropertyRate the new property rate (includes property type multiplier)
     */
    public void updatePrice(double newPropertyRate) {
        if (newPropertyRate < 0) {
            System.out.println("[ERROR] Property rate cannot be negative");
            return;
        }
        this.propertyRate = newPropertyRate;
        this.finalPrice = newPropertyRate * modifier;
    }

    /**
     * Sets the environmental modifier and recalculates the final price.
     * @param modifier the new environmental modifier
     */
    public void setModifier(double modifier) {
        if (modifier < 0.8 || modifier > 1.2) {
            System.out.println("[ERROR] Modifier must be between 0.8 and 1.2");
            return;
        }
        this.modifier = modifier;
        this.finalPrice = propertyRate * modifier;
    }

    /**
     * Sets the price per night directly.
     * @param pricePerNight the new price per night
     */
    public void setPricePerNight(double pricePerNight) {
        this.finalPrice = pricePerNight;
    }

    // Booking Methods

    /**
     * Books this date.
     */
    public void book() {
        if (isBooked) {
            System.out.println("[ERROR] Date is already booked");
            return;
        }
        isBooked = true;
    }

    /**
     * Unbooks this date.
     */
    public void unbook() {
        if (!isBooked) {
            System.out.println("[ERROR] Date is not booked");
            return;
        }
        isBooked = false;
    }

    /**
     * Returns a string representation of the date.
     * @return string representation of the date
     */
    @Override
    public String toString() {
        return "Date{day=" + dayNumber + ", propertyRate=" + propertyRate + ", modifier=" + modifier +
                ", finalPrice=" + finalPrice + ", booked=" + isBooked + "}";
    }
}