/**
 * GreenResortFeeCalc.java
 *
 * Calculates the fee for any green resort property type given any base price
 *
 * MC02 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 1.0
 */

public class GreenResortFeeCalc extends Property {

    public GreenResortFeeCalc(String name){
        super(name);
        setPropertyType("Green Resort");
    }

    @Override
    public double calcFinalRate(double basePrice){
        return basePrice * 1.35;
    }
}