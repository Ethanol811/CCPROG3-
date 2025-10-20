/**
 * Reservation.java
 *
 * Represents a reservation made for a property in the Green Property Exchange system.
 * Each reservation stores:
 *  - The guest's name
 *  - Check-in and check-out dates
 *  - Total price
 *  - Nightly price breakdown
 *
 * MCO1 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten ,Julian Nicos Reyes
 * @version 1.0
 */

import java.util.ArrayList;

public class Reservation {
    private String guestName;
    private int checkIn;      // inclusive
    private int checkOut;     // exclusive
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
    // Getters
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
     * @return List of nightly prices
     */
    public ArrayList<Double> getBreakdown() {
        return breakdown;
    }

    // -------------------------------------------------------
    // Core Methods
    // -------------------------------------------------------

    /**
     * Calculates total price based on nightly rate from property's available dates.
     * @param dates The list of all property dates
     */
    public void calculateTotal(ArrayList<Date> dates) {
        totalPrice = 0;
        breakdown.clear();

        for (Date d : dates) {
            if (d.getDayNumber() >= checkIn && d.getDayNumber() < checkOut) {
                totalPrice += d.getPricePerNight();
                breakdown.add(d.getPricePerNight());
            }
        }
    }

    /**
     * Displays reservation details.
     */
    public void displayReservation() {
        System.out.println("\n-- RESERVATION DETAILS --");
        System.out.println("Guest Name: " + guestName);
        System.out.println("Check-in Day: " + checkIn);
        System.out.println("Check-out Day: " + checkOut);
        System.out.println("Total Price: ₱" + totalPrice);

        System.out.println("\nPrice Breakdown:");
        for (int i = 0; i < breakdown.size(); i++) {
            System.out.printf(" Day %d: ₱%.2f%n", (checkIn + i), breakdown.get(i));
        }
    }
}
