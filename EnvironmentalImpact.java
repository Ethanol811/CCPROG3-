// EnvironmentalImpact.java
/**
 * EnvironmentalImpact.java
 *
 * Represents an environmental impact event with name and modifier.
 * Follows Single Responsibility Principle by handling only impact data.
 * Immutable design pattern for thread safety and data integrity.
 *
 * MCO2 - Green Property Exchange
 * @author Group 23
 * @version 2.0
 */
public class EnvironmentalImpact {
    private final String name;
    private final double modifier;
    
    /**
     * Constructs a new EnvironmentalImpact with specified name and modifier.
     *
     * @param name the name of the environmental impact (e.g., "Earth Day")
     * @param modifier the environmental modifier (0.8-1.2)
     */
    public EnvironmentalImpact(String name, double modifier) {
        this.name = name;
        this.modifier = modifier;
    }
    
    /**
     * Returns the name of the environmental impact.
     *
     * @return the name of the environmental impact
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the environmental modifier.
     *
     * @return the environmental modifier
     */
    public double getModifier() {
        return modifier;
    }
    
    /**
     * Creates a new EnvironmentalImpact with updated modifier.
     * Follows Immutable Pattern - returns new instance instead of modifying existing one.
     *
     * @param modifier the new environmental modifier (0.8-1.2)
     * @return new EnvironmentalImpact instance with updated modifier
     */
    public EnvironmentalImpact withModifier(double modifier) {
        return new EnvironmentalImpact(this.name, modifier);
    }
    
    /**
     * Creates a new EnvironmentalImpact with updated name.
     * Follows Immutable Pattern - returns new instance instead of modifying existing one.
     *
     * @param name the new name of the environmental impact
     * @return new EnvironmentalImpact instance with updated name
     */
    public EnvironmentalImpact withName(String name) {
        return new EnvironmentalImpact(name, this.modifier);
    }
    
    /**
     * Returns a string representation of the environmental impact.
     *
     * @return string representation of the environmental impact
     */
    @Override
    public String toString() {
        return name + " (" + (modifier * 100) + "%)";
    }
    
    /**
     * Checks if this environmental impact is equal to another object.
     *
     * @param obj the object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        EnvironmentalImpact that = (EnvironmentalImpact) obj;
        return Double.compare(that.modifier, modifier) == 0 &&
               name.equals(that.name);
    }
    
    /**
     * Returns the hash code of this environmental impact.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + Double.hashCode(modifier);
        return result;
    }
    
    
}