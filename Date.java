// Date.java
/**
 * Date.java
 *
 * Represents a date in the Green Property Exchange System.
 * Enhanced with environmental impact naming support.
 * Each date contains information about its day number, property rate,
 * environmental impact, final calculated price, and booking status.
 * Follows Single Responsibility Principle - only handles date-related data and operations.
 *
 * MC02- Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 5.0
 */

public class Date {
    private int dayNumber;
    private double propertyRate;  // Base price × property type multiplier
    private EnvironmentalImpact environmentalImpact; // Environmental impact with name
    private double finalPrice;    // propertyRate × environmentalImpact.getModifier()
    private boolean isBooked;

    /**
     * Constructs a new Date with the specified parameters and default environmental impact.
     *
     * @param dayNumber the day number (1-30)
     * @param propertyRate the property rate (already includes property type multiplier)
     */
    public Date(int dayNumber, double propertyRate) {
        this(dayNumber, propertyRate, new EnvironmentalImpact("Standard", 1.0));
    }

    /**
     * Constructs a new Date with the specified parameters and environmental impact.
     *
     * @param dayNumber the day number (1-30)
     * @param propertyRate the property rate (already includes property type multiplier)
     * @param environmentalImpact the environmental impact with name and modifier
     */
    public Date(int dayNumber, double propertyRate, EnvironmentalImpact environmentalImpact) {
        this.dayNumber = dayNumber;
        this.propertyRate = propertyRate;
        this.environmentalImpact = environmentalImpact;
        this.finalPrice = propertyRate * environmentalImpact.getModifier();
        this.isBooked = false;
    }

    // Getters following Interface Segregation Principle

    /**
     * Returns the day number of this date.
     *
     * @return the day number
     */
    public int getDayNumber() {
        return dayNumber;
    }

    /**
     * Returns the booking status of this date.
     *
     * @return true if the date is booked, false otherwise
     */
    public boolean isBooked() {
        return isBooked;
    }

    /**
     * Returns the property rate of this date (includes property type multiplier).
     *
     * @return the property rate
     */
    public double getBasePrice() {
        return propertyRate;
    }

    /**
     * Returns the final price of this date (property rate × environmental modifier).
     *
     * @return the final price
     */
    public double getFinalPrice() {
        return finalPrice;
    }

    /**
     * Returns the environmental modifier of this date.
     *
     * @return the environmental modifier
     */
    public double getModifier() {
        return environmentalImpact.getModifier();
    }

    /**
     * Returns the environmental impact name of this date.
     *
     * @return the environmental impact name
     */
    public String getEnvironmentalImpactName() {
        return environmentalImpact.getName();
    }

    /**
     * Returns the complete environmental impact object.
     *
     * @return the environmental impact object
     */
    public EnvironmentalImpact getEnvironmentalImpact() {
        return environmentalImpact;
    }

    /**
     * Returns the price per night (final price).
     *
     * @return the price per night
     */
    public double getPricePerNight() {
        return finalPrice;
    }

    // Setters following Single Responsibility Principle

    /**
     * Sets the property rate and recalculates the final price.
     *
     * @param newPropertyRate the new property rate (includes property type multiplier)
     */
    public void updatePrice(double newPropertyRate) {
        if (newPropertyRate < 0) {
            System.out.println("[ERROR] Property rate cannot be negative");
            return;
        }
        this.propertyRate = newPropertyRate;
        this.finalPrice = newPropertyRate * environmentalImpact.getModifier();
    }

    /**
     * Sets the environmental impact and recalculates the final price.
     *
     * @param environmentalImpact the new environmental impact
     */
    public void setEnvironmentalImpact(EnvironmentalImpact environmentalImpact) {
        if (environmentalImpact == null) {
            System.out.println("[ERROR] Environmental impact cannot be null");
            return;
        }
        this.environmentalImpact = environmentalImpact;
        this.finalPrice = propertyRate * environmentalImpact.getModifier();
    }


    /**
    * Sets the environmental modifier with a default name.
    *
    * @param modifier the new environmental modifier
    */
    public void setModifier(double modifier) {
        // Create new EnvironmentalImpact instance instead of modifying existing one
        this.environmentalImpact = new EnvironmentalImpact(this.environmentalImpact.getName(), modifier);
        this.finalPrice = propertyRate * modifier;
    }

    /**
    * Sets the environmental impact name.
    *
    * @param name the new environmental impact name
    */
    public void setEnvironmentalImpactName(String name) {
        // Create new EnvironmentalImpact instance instead of modifying existing one
        this.environmentalImpact = new EnvironmentalImpact(name, this.environmentalImpact.getModifier());
    }

    /**
     * Sets the price per night directly.
     *
     * @param pricePerNight the new price per night
     */
    public void setPricePerNight(double pricePerNight) {
        this.finalPrice = pricePerNight;
    }

    // Booking Methods following Single Responsibility Principle

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
     *
     * @return string representation of the date
     */
    @Override
    public String toString() {
        return "Date{day=" + dayNumber + ", propertyRate=" + propertyRate + 
               ", environmentalImpact=" + environmentalImpact + 
               ", finalPrice=" + finalPrice + ", booked=" + isBooked + "}";
    }
}