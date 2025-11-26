// PropertyFactory.java
/**
 * PropertyFactory.java
 *
 * Factory class for creating property instances.
 * Follows Factory Pattern and Open/Closed Principle - new property types can be added without modifying existing code.
 *
 * MCO2 - Green Property Exchange
 * @author Group 23
 * @version 1.0
 */
public class PropertyFactory {
    
    /**
     * Creates a property instance based on the specified type.
     * @param name the property name
     * @param type the property type
     * @return a new Property instance, or null if type is invalid
     */
    public Property createProperty(String name, String type) {
        if (type == null) {
            return null;
        }
        
        switch (type.toLowerCase()) {
            case "eco-apartment":
                return new EcoApartmentFeeCalc(name);
            case "sustainable house":
                return new SustainableHouseFeeCalc(name);
            case "green resort":
                return new GreenResortFeeCalc(name);
            case "eco-glamping":
                return new EcoGlampingFeeCalc(name);
            default:
                return null;
        }
    }
    
    /**
     * Returns all available property types.
     * @return array of available property type names
     */
    public String[] getAvailablePropertyTypes() {
        return new String[] {
            "Eco-Apartment",
            "Sustainable House", 
            "Green Resort",
            "Eco-Glamping"
        };
    }
}