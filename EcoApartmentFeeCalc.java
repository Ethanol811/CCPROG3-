/**
 * EcoApartmentFeeCalc.java
 *
 * Represents an eco-apartment property type in the Green Property Exchange system.
 * Implements PropertyType interface following Strategy Pattern.
 * This class calculates the final rate for eco-apartment properties by applying
 * no additional markup to the base price (1.0 multiplier).
 *
 * MC02 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 2.0
 */

public class EcoApartmentFeeCalc extends Property implements PropertyType {

    /**
     * Constructs a new EcoApartmentFeeCalc instance with the specified name.
     * Initializes the property type as "Eco-Apartment".
     *
     * @param name the name of the eco-apartment property
     */
    public EcoApartmentFeeCalc(String name){
        super(name);
        setPropertyType("Eco-Apartment");
    }

    /**
     * Calculates the final rate for an eco-apartment property.
     * Eco-apartments use a 1.0 multiplier, meaning the final rate
     * equals the base price with no additional markup.
     *
     * @param basePrice the base price of the property per night
     * @return the final rate calculated as basePrice * 1.0
     */
    @Override
    public double calculateFinalRate(double basePrice){
        return calculateRate(basePrice);
    }
    
    /**
     * Calculates the rate for eco-apartment property type.
     * Implements PropertyType interface following Strategy Pattern.
     *
     * @param basePrice the base price of the property per night
     * @return the final rate calculated as basePrice * 1.0
     */
    @Override
    public double calculateRate(double basePrice) {
        return basePrice * 1.0;
    }
    
    /**
     * Returns the display name of the property type.
     * @return the property type name
     */
    @Override
    public String getTypeName() {
        return "Eco-Apartment";
    }
}