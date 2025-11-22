/**
 * EcoGlampingFeeCalc.java
 *
 * Represents an eco-glamping property type in the Green Property Exchange system.
 * This class calculates the final rate for eco-glamping properties by applying
 * a 50% markup to the base price (1.5 multiplier).
 *
 * Eco-glamping properties typically command premium pricing due to their
 * unique outdoor experience and sustainable amenities.
 *
 * MC02 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 1.0
 */

public class EcoGlampingFeeCalc extends Property {

    /**
     * Constructs a new EcoGlampingFeeCalc instance with the specified name.
     * Initializes the property type as "Eco-Glamping".
     *
     * @param name the name of the eco-glamping property
     * @throws IllegalArgumentException if the name is null or empty
     */
    public EcoGlampingFeeCalc(String name){
        super(name);
        setPropertyType("Eco-Glamping");
    }

    /**
     * Calculates the final rate for an eco-glamping property.
     * Eco-glamping properties use a 1.5 multiplier, applying a 50% premium
     * to the base price due to their unique nature and amenities.
     *
     * @param basePrice the base price of the property per night
     * @return the final rate calculated as basePrice * 1.5
     * @throws IllegalArgumentException if basePrice is negative
     */
    @Override
    public double calculateFinalRate(double basePrice){
        return basePrice * 1.50;
    }
}