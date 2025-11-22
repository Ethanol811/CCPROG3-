/**
 * Property.java
 *
 * Represents an abstract property listing in the Green Property Exchange system.
 * This class serves as the base for all property types and provides common functionality
 * for managing dates, reservations, pricing, and environmental modifiers.
 *
 * MCO1 - Green Property Exchange
 * @author Group 23
 * @version 4.0
 */

import java.util.ArrayList;
import java.util.List;

public abstract class Property {
    /** Minimum allowed base price for a property */
    private static final double MIN_BASE_PRICE = 100.0;
    /** Maximum allowed base price for a property */
    private static final double MAX_BASE_PRICE = 999999.0;
    /** Maximum number of dates a property can have */
    private static final int MAX_DATES = 30;
    /** Minimum environmental modifier value */
    private static final double MIN_MODIFIER = 0.8;
    /** Maximum environmental modifier value */
    private static final double MAX_MODIFIER = 1.2;

    private String name;
    private double basePrice;
    private ArrayList<Date> dates;
    private ArrayList<Reservation> reservations;
    private String propertyType;

    /**
     * Constructs a new Property with the specified name.
     * @param name the name of the property
     */
    protected Property(String name) {
        validateName(name);
        this.name = name.trim();
        this.basePrice = 1500.0;
        this.dates = new ArrayList<Date>();
        this.reservations = new ArrayList<Reservation>();
        this.propertyType = "";
    }

    /**
     * Validates the property name meets system requirements.
     * @param name the name to validate
     */
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Property name cannot be null or empty");
        }
        if (name.trim().length() < 2) {
            throw new IllegalArgumentException("Property name must be at least 2 characters long");
        }
    }

    /**
     * Validates that a price is within acceptable bounds.
     * @param price the price to validate
     */
    private void validatePrice(double price) {
        if (price < MIN_BASE_PRICE || price > MAX_BASE_PRICE) {
            throw new IllegalArgumentException("Price must be between PHP " + MIN_BASE_PRICE + " and PHP " + MAX_BASE_PRICE);
        }
    }

    /**
     * Validates that a day number is within the 1-30 range.
     * @param dayNumber the day number to validate
     */
    private void validateDayNumber(int dayNumber) {
        if (dayNumber < 1 || dayNumber > MAX_DATES) {
            throw new IllegalArgumentException("Day number must be between 1 and " + MAX_DATES);
        }
    }

    /**
     * Validates that an environmental modifier is within acceptable bounds.
     * @param modifier the modifier to validate
     */
    private void validateModifier(double modifier) {
        if (modifier < MIN_MODIFIER || modifier > MAX_MODIFIER) {
            throw new IllegalArgumentException("Modifier must be between " + MIN_MODIFIER + " and " + MAX_MODIFIER);
        }
    }

    /**
     * Validates that no reservations exist on the property.
     */
    private void validateReservationConstraints() {
        if (!reservations.isEmpty()) {
            throw new IllegalStateException("Cannot modify property with active reservations");
        }
    }

    /**
     * Calculates the final rate for this property type based on the base price.
     * @param basePrice the base price to calculate from
     * @return the final rate for this property type
     */
    public abstract double calculateFinalRate(double basePrice);

    // Getters and Setters

    /**
     * Returns the property name.
     * @return the property name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name for the property with validation.
     * @param newName the new name for the property
     */
    public void setName(String newName) {
        validateName(newName);
        validateReservationConstraints();
        this.name = newName.trim();
    }

    /**
     * Returns the base price of the property.
     * @return the base price
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * Sets a new base price for the property with validation.
     * @param newPrice the new base price
     */
    public void setBasePrice(double newPrice) {
        validatePrice(newPrice);
        validateReservationConstraints();

        this.basePrice = newPrice;
        updateAllDatePrices();
    }

    /**
     * Returns the property type.
     * @return the property type
     */
    public String getPropertyType() {
        return propertyType;
    }

    /**
     * Sets the property type with validation.
     * @param newType the new property type
     */
    public void setPropertyType(String newType) {
        if (newType == null || newType.trim().isEmpty()) {
            throw new IllegalArgumentException("Property type cannot be null or empty");
        }
        validateReservationConstraints();
        this.propertyType = newType.trim();
    }

    /**
     * Returns all dates for this property.
     * @return list of dates
     */
    public ArrayList<Date> getDates() {
        return dates;
    }

    /**
     * Returns all reservations for this property.
     * @return list of reservations
     */
    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Sets the environmental modifier for a specific date.
     * @param dayNumber the day number to modify
     * @param modifier the new environmental modifier
     */
    public void setEnvironmentalModifier(int dayNumber, double modifier) {
        validateDayNumber(dayNumber);
        validateModifier(modifier);

        Date date = findDate(dayNumber);
        if (date == null) {
            throw new IllegalArgumentException("Day " + dayNumber + " not found in property");
        }

        date.setModifier(modifier);
        date.updatePrice(calculateFinalRate(basePrice));
    }

    // Date Management Methods

    /**
     * Adds a new date to the property with default modifier.
     * @param dayNumber the day number to add
     */
    public void addDate(int dayNumber) {
        addDate(dayNumber, 1.0);
    }

    /**
     * Adds a new date to the property with specified environmental modifier.
     * @param dayNumber the day number to add
     * @param modifier the environmental modifier
     */
    public void addDate(int dayNumber, double modifier) {
        validateDayNumber(dayNumber);
        validateModifier(modifier);

        if (dates.size() >= MAX_DATES) {
            throw new IllegalStateException("Cannot add more than " + MAX_DATES + " dates");
        }

        if (findDate(dayNumber) != null) {
            throw new IllegalArgumentException("Day " + dayNumber + " already exists in property");
        }

        double finalPrice = calculateFinalRate(basePrice) * modifier;
        Date newDate = new Date(dayNumber, calculateFinalRate(basePrice), modifier);
        dates.add(newDate);
    }

    /**
     * Removes a date from the property.
     * @param dayNumber the day number to remove
     */
    public void removeDate(int dayNumber) {
        validateDayNumber(dayNumber);

        for (int i = 0; i < dates.size(); i++) {
            Date date = dates.get(i);
            if (date.getDayNumber() == dayNumber) {
                if (date.isBooked()) {
                    throw new IllegalStateException("Cannot remove booked date: " + dayNumber);
                }
                dates.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("Day " + dayNumber + " not found in property");
    }

    /**
     * Finds a date by its day number.
     * @param dayNumber the day number to find
     * @return the date if found, null otherwise
     */
    public Date findDate(int dayNumber) {
        for (Date date : dates) {
            if (date.getDayNumber() == dayNumber) {
                return date;
            }
        }
        return null;
    }

    /**
     * Checks if all dates in a range are available for booking.
     * @param checkIn the check-in day
     * @param checkOut the check-out day
     * @return true if all dates are available, false otherwise
     */
    public boolean areDatesAvailable(int checkIn, int checkOut) {
        validateDateRange(checkIn, checkOut);

        for (int day = checkIn; day < checkOut; day++) {
            Date date = findDate(day);
            if (date == null || date.isBooked()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a list of unavailable days within a date range.
     * @param checkIn the check-in day
     * @param checkOut the check-out day
     * @return list of unavailable day numbers
     */
    public ArrayList<Integer> getUnavailableDays(int checkIn, int checkOut) {
        validateDateRange(checkIn, checkOut);

        ArrayList<Integer> unavailable = new ArrayList<Integer>();
        for (int day = checkIn; day < checkOut; day++) {
            Date date = findDate(day);
            if (date == null || date.isBooked()) {
                unavailable.add(day);
            }
        }
        return unavailable;
    }

    /**
     * Validates that a date range is acceptable for booking.
     * @param checkIn the check-in day
     * @param checkOut the check-out day
     */
    private void validateDateRange(int checkIn, int checkOut) {
        if (checkIn < 1 || checkIn >= checkOut || checkOut > MAX_DATES + 1) {
            throw new IllegalArgumentException("Invalid date range: checkIn=" + checkIn + ", checkOut=" + checkOut);
        }
        if (checkIn == MAX_DATES) {
            throw new IllegalArgumentException("Cannot check-in on day " + MAX_DATES);
        }
        if (checkOut == 1) {
            throw new IllegalArgumentException("Cannot check-out on day 1");
        }
    }

    /**
     * Books a range of dates for a reservation.
     * @param checkIn the check-in day
     * @param checkOut the check-out day
     */
    public void bookDates(int checkIn, int checkOut) {
        if (!areDatesAvailable(checkIn, checkOut)) {
            ArrayList<Integer> unavailable = getUnavailableDays(checkIn, checkOut);
            throw new IllegalStateException("Dates not available: " + unavailable);
        }

        for (int day = checkIn; day < checkOut; day++) {
            Date date = findDate(day);
            if (date != null) {
                date.book();
            }
        }
    }

    /**
     * Adds a reservation to the property.
     * @param reservation the reservation to add
     */
    public void addReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        reservations.add(reservation);
        reservation.calculateTotal(dates);
    }

    // Calculation Methods

    /**
     * Calculates total earnings from all reservations.
     * @return total earnings from reservations
     */
    public double calculateEarnings() {
        double total = 0;
        for (Reservation reservation : reservations) {
            total += reservation.getTotalPrice();
        }
        return total;
    }

    /**
     * Returns the count of available (unbooked) dates.
     * @return number of available dates
     */
    public int getAvailableDateCount() {
        int count = 0;
        for (Date date : dates) {
            if (!date.isBooked()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the count of booked dates.
     * @return number of booked dates
     */
    public int getBookedDateCount() {
        int count = 0;
        for (Date date : dates) {
            if (date.isBooked()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Calculates the occupancy rate of the property.
     * @return occupancy rate as a decimal
     */
    public double getOccupancyRate() {
        if (dates.isEmpty()) return 0.0;
        return (double) getBookedDateCount() / dates.size();
    }

    /**
     * Updates all date prices based on the current base price.
     */
    private void updateAllDatePrices() {
        for (Date date : dates) {
            date.updatePrice(calculateFinalRate(basePrice));
        }
    }

    // Display Methods

    /**
     * Displays comprehensive information about the property.
     */
    public void displayInfo() {
        System.out.println("\n=== PROPERTY INFORMATION ===");
        System.out.println("-----------------------------------");
        System.out.println("Property Name: " + name);
        System.out.println("Property Type: " + propertyType);
        System.out.println("Base Price: PHP " + String.format("%.2f", basePrice) + " per night");
        System.out.println("Property Rate: PHP " + String.format("%.2f", calculateFinalRate(basePrice)) + " per night");
        System.out.println("Total Dates Listed: " + dates.size());
        System.out.println("Available Dates: " + getAvailableDateCount());
        System.out.println("Booked Dates: " + getBookedDateCount());
        System.out.println("Occupancy Rate: " + String.format("%.1f%%", getOccupancyRate() * 100));
        System.out.println("Total Reservations: " + reservations.size());
        System.out.println("Total Earnings: PHP " + String.format("%.2f", calculateEarnings()));
        System.out.println("-----------------------------------");
    }

    /**
     * Displays a calendar view of all dates.
     */
    public void displayCalendar() {
        System.out.println("\n=== CALENDAR VIEW ===");
        System.out.println("Legend: [G]reen=80-89%  [W]hite=100%  [Y]ellow=101-120%  [B]ooked");
        System.out.println("-----------------------------------");

        if (dates.isEmpty()) {
            System.out.println("No dates available for this property.");
            return;
        }

        for (Date date : dates) {
            double modPct = date.getModifier() * 100;
            String colorCode = getColorCode(modPct);
            String status = date.isBooked() ? "BOOKED" : "AVAILABLE";

            System.out.printf("Day %2d | Price: PHP %8.2f | Mod: %3.0f%% | %s | %s%n",
                    date.getDayNumber(),
                    date.getFinalPrice(),
                    modPct,
                    colorCode,
                    status
            );
        }
    }

    /**
     * Determines the color code for environmental impact display.
     * @param modifierPercentage the environmental modifier as a percentage
     * @return color code string
     */
    private String getColorCode(double modifierPercentage) {
        if (modifierPercentage < 90) return "G";
        if (modifierPercentage == 100) return "W";
        return "Y";
    }

    /**
     * Displays detailed information about a specific date.
     * @param dayNumber the day number to display information for
     */
    public void displayDateInfo(int dayNumber) {
        Date date = findDate(dayNumber);
        if (date == null) {
            System.out.println("[ERROR] Day " + dayNumber + " not found");
            return;
        }

        System.out.println("\n=== DATE DETAILS ===");
        System.out.println("Day Number: " + dayNumber);
        System.out.println("Base Price: PHP " + String.format("%.2f", date.getBasePrice()));
        System.out.println("Final Price: PHP " + String.format("%.2f", date.getFinalPrice()));
        System.out.println("Modifier: " + String.format("%.2f", date.getModifier()));
        System.out.println("Status: " + (date.isBooked() ? "BOOKED" : "AVAILABLE"));

        // Find reservation for this date
        for (Reservation reservation : reservations) {
            if (dayNumber >= reservation.getCheckIn() && dayNumber < reservation.getCheckOut()) {
                System.out.println("Booked by: " + reservation.getGuestName());
                System.out.println("Reservation: Day " + reservation.getCheckIn() + " to Day " + reservation.getCheckOut());
                break;
            }
        }
    }

    /**
     * Displays reservation information for a specific date range.
     * @param startDay the start day of the range
     * @param endDay the end day of the range
     */
    public void displayReservationInfo(int startDay, int endDay) {
        validateDateRange(startDay, endDay + 1);

        System.out.println("\n=== RESERVATIONS FOR DAYS " + startDay + " TO " + endDay + " ===");

        boolean found = false;
        for (Reservation reservation : reservations) {
            if (overlapsWithRange(reservation, startDay, endDay)) {
                reservation.displayReservation();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No reservations found in the specified date range.");
        }
    }

    /**
     * Checks if a reservation overlaps with a date range.
     * @param reservation the reservation to check
     * @param startDay the start day of the range
     * @param endDay the end day of the range
     * @return true if the reservation overlaps with the range
     */
    private boolean overlapsWithRange(Reservation reservation, int startDay, int endDay) {
        return (reservation.getCheckIn() <= endDay && reservation.getCheckOut() > startDay);
    }

    /**
     * Returns a string representation of the property.
     * @return string representation of the property
     */
    @Override
    public String toString() {
        return "Property{name='" + name + "', type='" + propertyType + "', basePrice=" + basePrice +
                ", dates=" + dates.size() + ", reservations=" + reservations.size() + "}";
    }
}