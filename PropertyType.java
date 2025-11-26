// PropertyType.java
/**
 * PropertyType.java
 *
 * Interface defining the contract for property type rate calculations.
 * Follows Strategy Pattern and Open/Closed Principle - new property types can be added without modifying existing code.
 *
 * MCO2 - Green Property Exchange
 * @author Group 23
 * @version 1.0
 */
public interface PropertyType {
    /**
     * Calculates the final rate for this property type based on the base price.
     * @param basePrice the base price to calculate from
     * @return the final rate for this property type
     */
    double calculateRate(double basePrice);
    
    /**
     * Returns the display name of the property type.
     * @return the property type name
     */
    String getTypeName();
}