// GreenResortFeeCalc.java
/**
 * GreenResortFeeCalc.java
 *
 * Represents a green resort property type in the Green Property Exchange system.
 * Implements PropertyType interface following Strategy Pattern.
 * This class calculates the final rate for green resort properties by applying
 * a 35% markup to the base price (1.35 multiplier).
 *
 * Green resorts typically include extensive amenities and sustainable features
 * that justify the premium pricing.
 *
 * MC02 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 2.0
 */

public class GreenResortFeeCalc extends Property implements PropertyType {

    /**
     * Constructs a new GreenResortFeeCalc instance with the specified name.
     * Initializes the property type as "Green Resort".
     *
     * @param name the name of the green resort property
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
     */
    @Override
    public double calculateFinalRate(double basePrice){
        return calculateRate(basePrice);
    }
    
    /**
     * Calculates the rate for green resort property type.
     * Implements PropertyType interface following Strategy Pattern.
     *
     * @param basePrice the base price of the property per night
     * @return the final rate calculated as basePrice * 1.35
     */
    @Override
    public double calculateRate(double basePrice) {
        return basePrice * 1.35;
    }
    
    /**
     * Returns the display name of the property type.
     * @return the property type name
     */
    @Override
    public String getTypeName() {
        return "Green Resort";
    }
}