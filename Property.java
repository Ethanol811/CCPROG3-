/**
 * Property.java
 * 
 * Represents a property listing in the Green Property Exchange system.
 * Each property contains its name, base price per night, a list of available dates,
 * and a list of reservations (initially empty). 
 * 
 * MCO1 - Green Property Exchange
 * @author Group 23
 * @version 3
 */

import java.util.ArrayList;

public abstract class Property {
    private String name;
    private double basePrice;
    private ArrayList<Date> dates;
    private ArrayList<Reservation> reservations;
    private String propertyType;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public Property(String name){
        if (name == null || name.trim().isEmpty()) {
            this.name = "Unnamed Property";
        } else {
            this.name = name.trim();
        }
        this.basePrice = 1500.0;
        this.dates = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    // -------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------
    public String getName(){
        return name;
    }

    public void setName(String newName){
        if (newName == null || newName.trim().isEmpty()){
            System.out.println("[ERROR] Property name cannot be blank.");
        } else {
            this.name = newName.trim();
            System.out.println("[SUCCESS] Property name updated to: " + this.name);
        }
    }

    public double getBasePrice(){
        return basePrice;
    }

    public void setBasePrice(double newPrice){
        if (newPrice < 100){
            System.out.println("[ERROR] Base price must be at least PHP 100.");
            return;
        }
        if (!reservations.isEmpty()){
            System.out.println("[ERROR] Cannot change base price while reservations exist.");
            return;
        }
        this.basePrice = newPrice;

        // Update all dates' prices based on modifier
        for (Date d : dates){
            d.setPricePerNight(calculateModifiedPrice(basePrice, d.getModifier()));
        }
        System.out.println("[SUCCESS] Base price updated.");
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String newType){
        if (newType == null || newType.trim().isEmpty()){
            System.out.println("[ERROR] Property type cannot be blank.");
            return;
        }
        if (!reservations.isEmpty()){
            System.out.println("[ERROR] Cannot change property type while reservations exist.");
            return;
        }
        if (!(newType.equals("Eco-Apartment") || newType.equals("Sustainable House") ||
                newType.equals("Green Resort") || newType.equals("Eco-Glamping"))){
            System.out.println("[ERROR] Invalid property type.");
            return;
        }
        this.propertyType = newType;
        System.out.println("[SUCCESS] Property type updated to: " + this.propertyType);
    }

    public ArrayList<Date> getDates(){
        return dates;
    }

    public ArrayList<Reservation> getReservations(){
        return reservations;
    }

    // -------------------------------------------------------
    // Environmental Modifier Logic
    // -------------------------------------------------------
    public double calculateModifiedPrice(double basePrice, double modifier){
        return basePrice * modifier;
    }

    public void setEnvironmentalModifier(int dayNumber, double modifier){
        Date d = findDate(dayNumber);
        if (d == null){
            System.out.println("[ERROR] Day not found.");
            return;
        }
        if (modifier < 0.8 || modifier > 1.2){
            System.out.println("[ERROR] Modifier must be between 0.8 and 1.2.");
            return;
        }
        d.setModifier(modifier);
        d.setPricePerNight(calculateModifiedPrice(basePrice, modifier));
        System.out.println("[SUCCESS] Modifier for day " + dayNumber + " set to " + modifier);
    }

    // -------------------------------------------------------
    // Core Property Methods
    // -------------------------------------------------------
    public void addDate(int dayNumber, double modifier){
        if (dates.size() >= 30){
            System.out.println("[ERROR] Cannot add more than 30 dates.");
            return;
        }
        if (dayNumber < 1 || dayNumber > 30){
            System.out.println("[ERROR] Invalid day number. Must be 1–30.");
            return;
        }
        if (findDate(dayNumber) != null){
            System.out.println("[ERROR] Day " + dayNumber + " already exists.");
            return;
        }

        Date newDate = new Date(dayNumber, calculateModifiedPrice(basePrice, modifier), modifier);
        dates.add(newDate);
        System.out.println("[SUCCESS] Added Day " + dayNumber +
                " | Base Price: PHP " + String.format("%.2f", basePrice) +
                " | Modifier: " + modifier +
                " | Final Price: PHP " + String.format("%.2f", newDate.getPricePerNight()));
    }

    public void removeDate(int dayNumber){
        for (int i = 0; i < dates.size(); i++){
            Date d = dates.get(i);
            if (d.getDayNumber() == dayNumber){
                if (d.isBooked()){
                    System.out.println("[ERROR] Cannot remove a booked date.");
                    return;
                }
                dates.remove(i);
                System.out.println("[SUCCESS] Removed day " + dayNumber);
                return;
            }
        }
        System.out.println("[ERROR] Day not found.");
    }

    public Date findDate(int dayNumber){
        for (Date d : dates){
            if (d.getDayNumber() == dayNumber){
                return d;
            }
        }
        return null;
    }

    public boolean areDatesAvailable(int checkIn, int checkOut){
        ArrayList<Integer> unavailable = new ArrayList<>();
        for (int day = checkIn; day < checkOut; day++){
            Date d = findDate(day);
            if (d == null || d.isBooked()){
                unavailable.add(day);
            }
        }
        if (!unavailable.isEmpty()){
            System.out.println("[ERROR] Unavailable days: " + unavailable);
            return false;
        }
        return true;
    }

    public void bookDates(int checkIn, int checkOut){
        for (int day = checkIn; day < checkOut; day++){
            Date d = findDate(day);
            if (d != null) d.book();
        }
    }

    public void addReservation(Reservation reservation){
        reservations.add(reservation);
        reservation.calculateTotal(dates);
    }

    public double calculateEarnings(){
        double total = 0;
        for (Reservation r : reservations){
            total += r.getTotalPrice();
        }
        return total;
    }

    public int getAvailableDateCount(){
        int count = 0;
        for (Date d : dates){
            if (!d.isBooked()) count++;
        }
        return count;
    }

    public int getBookedDateCount(){
        int count = 0;
        for (Date d : dates) if (d.isBooked()) count++;
        return count;
    }

    // -------------------------------------------------------
    // Display Methods
    // -------------------------------------------------------
    public void displayInfo(){
        System.out.println("\n=== PROPERTY INFORMATION ===");
        System.out.println("-----------------------------------");
        System.out.println("Property Name: " + name);
        System.out.println("Property Type: " + propertyType);
        System.out.println("Base Price: PHP " + String.format("%.2f", basePrice) + " per night");
        System.out.println("Total Dates Listed: " + dates.size());
        System.out.println("Available Dates: " + getAvailableDateCount());
        System.out.println("Booked Dates: " + getBookedDateCount());
        System.out.println("Total Reservations: " + reservations.size());
        System.out.println("Total Earnings: PHP " + String.format("%.2f", calculateEarnings()));
        System.out.println("-----------------------------------");
    }

    public void displayCalendar(){
        System.out.println("\n=== CALENDAR ===");
        System.out.println("Colors: Green = 80–89%, White = 100%, Yellow = 101–120% (G/W/Y)");
        for (Date d : dates){
            double modPct = d.getModifier() * 100;
            String color = modPct < 100 ? "G" : (modPct == 100 ? "W" : "Y");
            System.out.println("Day " + d.getDayNumber() +
                    " | Price: PHP " + String.format("%.2f", d.getPricePerNight()) +
                    " | Mod: " + String.format("%.0f", modPct) + "% | " + color +
                    (d.isBooked() ? " | BOOKED" : ""));
        }
    }

    public void displayDateInfo(int dayNumber){
        Date d = findDate(dayNumber);
        if (d == null){
            System.out.println("[ERROR] Day not found in this property.");
            return;
        }

        System.out.println("\n=== DATE DETAILS ===");
        System.out.println("Day Number: " + dayNumber);
        System.out.println("Price per night: PHP " + String.format("%.2f", d.getPricePerNight()));
        System.out.println("Modifier: " + String.format("%.2f", d.getModifier()));
        System.out.println("Status: " + (d.isBooked() ? "BOOKED" : "AVAILABLE"));

        for (Reservation r : reservations){
            if (dayNumber >= r.getCheckIn() && dayNumber < r.getCheckOut()){
                System.out.println("Booked by: " + r.getGuestName() +
                        " | Reservation: Day " + r.getCheckIn() + " to Day " + r.getCheckOut());
                break;
            }
        }
    }
}
