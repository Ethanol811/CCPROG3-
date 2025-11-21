/**
 * SustainableHouseFeeCalc.java
 *
 * Calculates the fee for any sustainable house property type given any base price
 *
 * MC02 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 1.0
 */

public class SustainableHouseFeeCalc extends Property {

    public SustainableHouseFeeCalc(String name){
        super(name);
        setPropertyType("Sustainable House");
    }

    @Override
    public double calcFinalRate(double basePrice){
        return basePrice * 1.20;
    }
}