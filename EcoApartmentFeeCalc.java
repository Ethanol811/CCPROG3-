/**
 * EcoApartmentFeeCalc.java
 *
 * Calculates the fee for any eco-apartment property type given any base price
 *
 * MC02 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten, Julian Nicos Reyes
 * @version 1.0
 */

public class EcoApartmentFeeCalc extends Property {

    public EcoApartmentFeeCalc(String name){
        super(name);
        setPropertyType("Eco-Apartment");
    }

    @Override
    public double calcFinalRate(double basePrice){
        return basePrice * 1.0;
    }
}