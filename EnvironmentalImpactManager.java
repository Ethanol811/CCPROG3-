import java.util.HashMap;
import java.util.Map;

/**
 * EnvironmentalImpactManager.java
 *
 * Manages environmental impact events for specific dates in a month.
 * Provides preset environmental impacts and allows custom impact management.
 *
 * MCO2 - Green Property Exchange
 * @author Group 23
 * @version 2.0
 */
public class EnvironmentalImpactManager {
    private final Map<Integer, EnvironmentalImpact> impactEvents;
    
    /**
     * Constructs a new EnvironmentalImpactManager with preset impacts.
     */
    public EnvironmentalImpactManager() {
        this.impactEvents = new HashMap<>();
        initializePresetImpacts();
    }
    
    /**
     * Initializes the manager with ONLY 4 preset environmental impacts based on Philippine events.
     * Follows Open/Closed Principle - easy to modify preset impacts without changing other code.
     */
    private void initializePresetImpacts() {
        // Clear any existing impacts first
        impactEvents.clear();
        
        // Add ONLY 4 Philippine environmental impacts - no weekend peaks
        addImpact(1, new EnvironmentalImpact("Labor Day", 1.15));       // May 1st
        addImpact(22, new EnvironmentalImpact("Earth Day", 0.8));      // April 22nd
        addImpact(26, new EnvironmentalImpact("Arbor Day", 0.85));     // April 26th
        // Note: Weekend peaks have been removed as requested
    }
    
    /**
     * Adds an environmental impact for a specific day.
     *
     * @param day the day number (1-30)
     * @param impact the environmental impact to add
     */
    public void addImpact(int day, EnvironmentalImpact impact) {
        if (!validateDay(day)) {
            return;
        }
        impactEvents.put(day, impact);
    }
    
    /**
     * Validates day number.
     * Follows Single Responsibility Principle - validation logic separated.
     *
     * @param day the day number to validate
     * @return true if valid, false otherwise
     */
    private boolean validateDay(int day) {
        if (day < 1 || day > 30) {
            System.out.println("[ERROR] Day must be between 1 and 30");
            return false;
        }
        return true;
    }
    
    /**
     * Returns the environmental impact for the specified day, or null if none exists.
     *
     * @param day the day number (1-30)
     * @return the environmental impact for the day, or null if not found
     */
    public EnvironmentalImpact getImpact(int day) {
        return impactEvents.get(day);
    }
    
    /**
     * Removes the environmental impact for the specified day.
     *
     * @param day the day number (1-30)
     */
    public void removeImpact(int day) {
        impactEvents.remove(day);
    }
    
    /**
     * Checks if a day has an environmental impact.
     *
     * @param day the day number (1-30)
     * @return true if the day has an environmental impact, false otherwise
     */
    public boolean hasImpact(int day) {
        return impactEvents.containsKey(day);
    }
    
    /**
     * Returns all environmental impact events.
     * Returns a defensive copy to maintain encapsulation.
     *
     * @return map of all environmental impact events
     */
    public Map<Integer, EnvironmentalImpact> getAllImpacts() {
        return new HashMap<>(impactEvents);
    }
    
    /**
     * Applies environmental impacts to all dates in a property.
     * Uses Strategy Pattern to apply impacts.
     * Follows Dependency Inversion Principle - depends on abstraction (Property) not concrete classes.
     *
     * @param property the property to apply impacts to
     */
    public void applyImpactsToProperty(Property property) {
        for (Map.Entry<Integer, EnvironmentalImpact> entry : impactEvents.entrySet()) {
            int day = entry.getKey();
            EnvironmentalImpact impact = entry.getValue();
        
            Date date = property.findDate(day);
            // Apply impacts to ALL dates, regardless of booking status
            if (date != null) {
                property.setEnvironmentalModifier(day, impact.getModifier(), impact.getName());
            }
        }
    }
    
    /**
     * Resets all environmental impacts to the preset values.
     */
    public void resetToPresetImpacts() {
        initializePresetImpacts();
    }
    
    /**
     * Returns the number of environmental impact events.
     *
     * @return the number of environmental impact events
     */
    public int getImpactCount() {
        return impactEvents.size();
    }
    
    /**
     * Checks if there are any environmental impacts defined.
     *
     * @return true if there are environmental impacts, false otherwise
     */
    public boolean hasImpacts() {
        return !impactEvents.isEmpty();
    }
}