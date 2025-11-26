// Reservation.java
/**
 * Reservation.java
 *
 * Represents a reservation made for a property in the Green Property Exchange system.
 * Each reservation stores:
 *  - The guest's name
 *  - Check-in and check-out dates
 *  - Total price
 *  - Nightly price breakdown
 * Follows Single Responsibility Principle - only handles reservation data and calculations.
 *
 * MCO1 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten ,Julian Nicos Reyes
 * @version 2.0
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reservation {
    private final String guestName;
    private final int checkIn;      // inclusive
    private final int checkOut;     // exclusive
    private double totalPrice;
    private ArrayList<Double> breakdown; // nightly price list

    /**
     * Constructs a new Reservation.
     * @param guestName  Name of the guest
     * @param checkIn    Check-in date (day number)
     * @param checkOut   Check-out date (day number)
     */
    public Reservation(String guestName, int checkIn, int checkOut) {
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.breakdown = new ArrayList<>();
        this.totalPrice = 0;
    }

    // -------------------------------------------------------
    // Getters following Interface Segregation Principle
    // -------------------------------------------------------

    /**
     * @return The guest's name
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * @return Check-in day number
     */
    public int getCheckIn() {
        return checkIn;
    }

    /**
     * @return Check-out day number
     */
    public int getCheckOut() {
        return checkOut;
    }

    /**
     * @return Total price of reservation
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * @return Unmodifiable list of nightly prices
     */
    public List<Double> getBreakdown() {
        return Collections.unmodifiableList(breakdown);
    }

    // -------------------------------------------------------
    // Core Methods following Single Responsibility Principle
    // -------------------------------------------------------

    /**
     * Calculates total price based on nightly rate from property's available dates.
     * @param dates The list of all property dates
     */
    public void calculateTotal(List<Date> dates) {
        totalPrice = 0;
        breakdown.clear();

        for (int day = checkIn; day < checkOut; day++) {
            for (Date d : dates) {
                if (d.getDayNumber() == day) {
                    totalPrice += d.getFinalPrice();
                    breakdown.add(d.getFinalPrice());
                    break;
                }
            }
        }
    }

    /**
     * Displays reservation details.
     */
    public void displayReservation() {
        System.out.println("\n=== RESERVATION DETAILS ===");
        System.out.println("-----------------------------------");
        System.out.println("Guest Name: " + guestName);
        System.out.println("Check-in Day: " + checkIn);
        System.out.println("Check-out Day: " + checkOut);
        System.out.println("Total Nights: " + (checkOut - checkIn));
        System.out.println("Total Price: PHP " + String.format("%.2f", totalPrice));

        System.out.println("\nPRICE BREAKDOWN:");
        for (int i = 0; i < breakdown.size(); i++) {
            System.out.printf("   Day %2d: PHP %8.2f%n", (checkIn + i), breakdown.get(i));
        }
        System.out.println("-----------------------------------");
    }
    
    /**
     * Returns the number of nights in this reservation.
     * @return number of nights
     */
    public int getNumberOfNights() {
        return checkOut - checkIn;
    }
    
    /**
     * Returns a string representation of the reservation.
     * @return string representation
     */
    @Override
    public String toString() {
        return "Reservation{guest='" + guestName + "', checkIn=" + checkIn + 
               ", checkOut=" + checkOut + ", total=" + totalPrice + "}";
    }
}