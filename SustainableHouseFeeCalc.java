// SustainableHouseFeeCalc.java
/**
 * SustainableHouseFeeCalc.java
 *
 * Represents a sustainable house property type in the Green Property Exchange system.
 * Implements PropertyType interface following Strategy Pattern.
 * This class calculates the final rate for sustainable house properties by applying
 * a 20% markup to the base price (1.2 multiplier).
 *
 * Sustainable houses typically feature eco-friendly construction and energy-efficient
 * systems that justify moderate premium pricing.
 *
 * MC02 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 2.0
 */

public class SustainableHouseFeeCalc extends Property implements PropertyType {

    /**
     * Constructs a new SustainableHouseFeeCalc instance with the specified name.
     * Initializes the property type as "Sustainable House".
     *
     * @param name the name of the sustainable house property
     */
    public SustainableHouseFeeCalc(String name){
        super(name);
        setPropertyType("Sustainable House");
    }

    /**
     * Calculates the final rate for a sustainable house property.
     * Sustainable houses use a 1.2 multiplier, applying a 20% premium
     * to the base price due to their eco-friendly features and construction.
     *
     * @param basePrice the base price of the property per night
     * @return the final rate calculated as basePrice * 1.2
     */
    @Override
    public double calculateFinalRate(double basePrice){
        return calculateRate(basePrice);
    }
    
    /**
     * Calculates the rate for sustainable house property type.
     * Implements PropertyType interface following Strategy Pattern.
     *
     * @param basePrice the base price of the property per night
     * @return the final rate calculated as basePrice * 1.2
     */
    @Override
    public double calculateRate(double basePrice) {
        return basePrice * 1.20;
    }
    
    /**
     * Returns the display name of the property type.
     * @return the property type name
     */
    @Override
    public String getTypeName() {
        return "Sustainable House";
    }
}