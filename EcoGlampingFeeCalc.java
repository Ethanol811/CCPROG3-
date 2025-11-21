/**
 * EcoGlampingFeeCalc.java
 *
 * Calculates the fee for any eco-glamping property type given any base price
 *
 * MC02 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 1.0
 */

public class EcoGlampingFeeCalc extends Property {

    public EcoGlampingFeeCalc(String name){
        super(name);
        setPropertyType("Eco-Glamping");
    }

    @Override
    public double calcFinalRate(double basePrice){
        return basePrice * 1.50;
    }
}