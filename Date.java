/**
 * Date.java
 *
 * Represents a date in the Green Property Exchange System.
 * Each date contains information about its day number, base price,
 * environmental modifier, final calculated price, and booking status.
 *
 * MC01- Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 2.0
 */

public class Date {
    private int dayNumber;
    private double basePrice;
    private double modifier;
    private double finalPrice;
    private boolean isBooked;

    /**
     * Constructs a new Date with the specified parameters.
     * @param dayNumber the day number (1-30)
     * @param basePrice the base price before modifiers
     * @param modifier the environmental modifier (0.8-1.2)
     */
    public Date(int dayNumber, double basePrice, double modifier) {
        validateInputs(dayNumber, basePrice, modifier);
        this.dayNumber = dayNumber;
        this.basePrice = basePrice;
        this.modifier = modifier;
        this.finalPrice = basePrice * modifier;
        this.isBooked = false;
    }

    /**
     * Validates constructor inputs for a Date instance.
     * @param dayNumber the day number to validate
     * @param basePrice the base price to validate
     * @param modifier the modifier to validate
     */
    private void validateInputs(int dayNumber, double basePrice, double modifier) {
        if (dayNumber < 1 || dayNumber > 30) {
            throw new IllegalArgumentException("Day number must be between 1 and 30");
        }
        if (basePrice < 0) {
            throw new IllegalArgumentException("Base price cannot be negative");
        }
        if (modifier < 0.8 || modifier > 1.2) {
            throw new IllegalArgumentException("Modifier must be between 0.8 and 1.2");
        }
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
     * Returns the base price of this date.
     * @return the base price
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * Returns the final price of this date.
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
     * Returns the price per night.
     * @return the price per night
     */
    public double getPricePerNight() {
        return finalPrice;
    }

    // Setters

    /**
     * Sets the base price and recalculates the final price.
     * @param newBasePrice the new base price
     */
    public void updatePrice(double newBasePrice) {
        if (newBasePrice < 0) {
            throw new IllegalArgumentException("Base price cannot be negative");
        }
        this.basePrice = newBasePrice;
        this.finalPrice = newBasePrice * modifier;
    }

    /**
     * Sets the environmental modifier and recalculates the final price.
     * @param modifier the new environmental modifier
     */
    public void setModifier(double modifier) {
        if (modifier < 0.8 || modifier > 1.2) {
            throw new IllegalArgumentException("Modifier must be between 0.8 and 1.2");
        }
        this.modifier = modifier;
        this.finalPrice = basePrice * modifier;
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
     * @throws IllegalStateException if the date is already booked
     */
    public void book() {
        if (isBooked) {
            throw new IllegalStateException("Date is already booked");
        }
        isBooked = true;
    }

    /**
     * Unbooks this date.
     * @throws IllegalStateException if the date is not booked
     */
    public void unbook() {
        if (!isBooked) {
            throw new IllegalStateException("Date is not booked");
        }
        isBooked = false;
    }

    /**
     * Returns a string representation of the date.
     * @return string representation of the date
     */
    @Override
    public String toString() {
        return "Date{day=" + dayNumber + ", basePrice=" + basePrice + ", modifier=" + modifier +
                ", finalPrice=" + finalPrice + ", booked=" + isBooked + "}";
    }
}