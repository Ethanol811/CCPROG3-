// EcoGlampingFeeCalc.java
/**
 * EcoGlampingFeeCalc.java
 *
 * Represents an eco-glamping property type in the Green Property Exchange system.
 * Implements PropertyType interface following Strategy Pattern.
 * This class calculates the final rate for eco-glamping properties by applying
 * a 50% markup to the base price (1.5 multiplier).
 *
 * MC02 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 2.0
 */

public class EcoGlampingFeeCalc extends Property implements PropertyType {

    /**
     * Constructs a new EcoGlampingFeeCalc instance with the specified name.
     * Initializes the property type as "Eco-Glamping".
     *
     * @param name the name of the eco-glamping property
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
     */
    @Override
    public double calculateFinalRate(double basePrice){
        return calculateRate(basePrice);
    }
    
    /**
     * Calculates the rate for eco-glamping property type.
     * Implements PropertyType interface following Strategy Pattern.
     *
     * @param basePrice the base price of the property per night
     * @return the final rate calculated as basePrice * 1.5
     */
    @Override
    public double calculateRate(double basePrice) {
        return basePrice * 1.50;
    }
    
    /**
     * Returns the display name of the property type.
     * @return the property type name
     */
    @Override
    public String getTypeName() {
        return "Eco-Glamping";
    }
}