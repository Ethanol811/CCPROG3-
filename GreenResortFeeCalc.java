/**
 * GreenResortFeeCalc.java
 *
 * Represents a green resort property type in the Green Property Exchange system.
 * This class calculates the final rate for green resort properties by applying
 * a 35% markup to the base price (1.35 multiplier).
 *
 * Green resorts typically include extensive amenities and sustainable features
 * that justify the premium pricing.
 *
 * MC02 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 1.0
 */

public class GreenResortFeeCalc extends Property {

    /**
     * Constructs a new GreenResortFeeCalc instance with the specified name.
     * Initializes the property type as "Green Resort".
     *
     * @param name the name of the green resort property
     * @throws IllegalArgumentException if the name is null or empty
     */
    public GreenResortFeeCalc(String name){
        super(name);
        setPropertyType("Green Resort");
    }

    /**
     * Calculates the final rate for a green resort property.
     * Green resorts use a 1.35 multiplier, applying a 35% premium
     * to the base price due to their extensive amenities and facilities.
     *
     * @param basePrice the base price of the property per night
     * @return the final rate calculated as basePrice * 1.35
     * @throws IllegalArgumentException if basePrice is negative
     */
    @Override
    public double calculateFinalRate(double basePrice){
        return basePrice * 1.35;
    }
}