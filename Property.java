/**
 * Property.java
 *
 * Represents an abstract property listing in the Green Property Exchange system.
 * This class serves as the base for all property types and provides common functionality
 * for managing dates, reservations, pricing, and environmental modifiers.
 *
 * MCO2 - Green Property Exchange
 * @author Group 23
 * @version 5.1
 */

import java.util.ArrayList;

public abstract class Property {
    private static final double MIN_BASE_PRICE = 100.0;
    private static final double MAX_BASE_PRICE = 999999.0;
    private static final int MAX_DATES = 30;
    private static final double MIN_MODIFIER = 0.8;
    private static final double MAX_MODIFIER = 1.2;

    private String name;
    private double basePrice;
    private ArrayList<Date> dates;
    private ArrayList<Reservation> reservations;
    private String propertyType;
    private EnvironmentalImpactManager environmentalImpactManager;

    /**
     * Constructs a new Property with the specified name.
     * @param name the name of the property
     */
    protected Property(String name) {
        this.name = name.trim();
        this.basePrice = 1500.0;
        this.dates = new ArrayList<Date>();
        this.reservations = new ArrayList<Reservation>();
        this.propertyType = "";
        this.environmentalImpactManager = new EnvironmentalImpactManager();
        
        // Initialize with all 30 dates
        initializeAllDates();
    }



    /**
    * Initializes the property with all 30 dates by default.
    * Follows Single Responsibility Principle - only handles date initialization.
    */
    private void initializeAllDates() {
        for (int day = 1; day <= 30; day++) {
            // Create date directly without using addDate method to avoid validation conflicts
            Date newDate = new Date(day, getPropertyRate(), new EnvironmentalImpact("Standard", 1.0));
            dates.add(newDate);
        }
    }

    /**
     * Calculates the final rate for this property type based on the base price.
     * This is the template method that follows Template Method Pattern.
     * @param basePrice the base price to calculate from
     * @return the final rate for this property type
     */
    public abstract double calculateFinalRate(double basePrice);

    /**
     * Gets the property rate (base price × property type multiplier).
     * @return the property rate per night
     */
    public double getPropertyRate() {
        return calculateFinalRate(basePrice);
    }

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
        if (!validateName(newName)) {
            return;
        }
        this.name = newName.trim();
    }

    /**
     * Validates property name according to business rules.
     * Follows Single Responsibility Principle - validation logic separated.
     * @param name the name to validate
     * @return true if valid, false otherwise
     */
    private boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("[ERROR] Property name cannot be null or empty");
            return false;
        }
        if (name.trim().length() < 2) {
            System.out.println("[ERROR] Property name must be at least 2 characters long");
            return false;
        }
        if (!reservations.isEmpty()) {
            System.out.println("[ERROR] Cannot modify property with active reservations");
            return false;
        }
        return true;
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
        if (!validatePrice(newPrice)) {
            return;
        }

        this.basePrice = newPrice;
        updateAllDatePrices();
    }

    /**
     * Validates price according to business rules.
     * Follows Single Responsibility Principle - validation logic separated.
     * @param price the price to validate
     * @return true if valid, false otherwise
     */
    private boolean validatePrice(double price) {
        if (price < MIN_BASE_PRICE || price > MAX_BASE_PRICE) {
            System.out.println("[ERROR] Price must be between PHP " + MIN_BASE_PRICE + " and PHP " + MAX_BASE_PRICE);
            return false;
        }
        if (!reservations.isEmpty()) {
            System.out.println("[ERROR] Cannot modify property with active reservations");
            return false;
        }
        return true;
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
        if (!validatePropertyType(newType)) {
            return;
        }
        this.propertyType = newType.trim();
    }

    /**
     * Validates property type according to business rules.
     * Follows Single Responsibility Principle - validation logic separated.
     * @param type the property type to validate
     * @return true if valid, false otherwise
     */
    private boolean validatePropertyType(String type) {
        if (type == null || type.trim().isEmpty()) {
            System.out.println("[ERROR] Property type cannot be null or empty");
            return false;
        }
        if (!reservations.isEmpty()) {
            System.out.println("[ERROR] Cannot modify property with active reservations");
            return false;
        }
        return true;
    }

    /**
     * Returns all dates for this property.
     * @return list of dates
     */
    public ArrayList<Date> getDates() {
        return new ArrayList<>(dates); // Return copy for encapsulation
    }

    /**
     * Returns all reservations for this property.
     * @return list of reservations
     */
    public ArrayList<Reservation> getReservations() {
        return new ArrayList<>(reservations); // Return copy for encapsulation
    }

    /**
     * Returns the environmental impact manager for this property.
     * @return the environmental impact manager
     */
    public EnvironmentalImpactManager getEnvironmentalImpactManager() {
        return environmentalImpactManager;
    }

    /**
     * Sets the environmental modifier and name for a specific date.
     * @param dayNumber the day number to modify
     * @param modifier the new environmental modifier
     * @param impactName the name of the environmental impact
     */
    public void setEnvironmentalModifier(int dayNumber, double modifier, String impactName) {
        if (!validateDateModification(dayNumber, modifier)) {
            return;
        }

        Date date = findDate(dayNumber);
        if (date == null) {
            System.out.println("[ERROR] Day " + dayNumber + " not found in property");
            return;
        }

        EnvironmentalImpact impact = new EnvironmentalImpact(impactName, modifier);
        date.setEnvironmentalImpact(impact);
        date.updatePrice(getPropertyRate());
    }

    /**
     * Validates date modification parameters.
     * Follows Single Responsibility Principle - validation logic separated.
     * @param dayNumber the day number to validate
     * @param modifier the modifier to validate
     * @return true if valid, false otherwise
     */
    private boolean validateDateModification(int dayNumber, double modifier) {
        if (dayNumber < 1 || dayNumber > MAX_DATES) {
            System.out.println("[ERROR] Day number must be between 1 and " + MAX_DATES);
            return false;
        }
        if (modifier < MIN_MODIFIER || modifier > MAX_MODIFIER) {
            System.out.println("[ERROR] Modifier must be between " + MIN_MODIFIER + " and " + MAX_MODIFIER);
            return false;
        }
        return true;
    }

    /**
     * Sets the environmental modifier for a specific date with default name.
     * Method overloading for convenience.
     * @param dayNumber the day number to modify
     * @param modifier the new environmental modifier
     */
    public void setEnvironmentalModifier(int dayNumber, double modifier) {
        setEnvironmentalModifier(dayNumber, modifier, "Custom Impact");
    }

    // Date Management Methods following Single Responsibility Principle

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
        if (!validateDateAddition(dayNumber, modifier)) {
            return;
        }

        Date newDate = new Date(dayNumber, getPropertyRate(), new EnvironmentalImpact("Standard", modifier));
        dates.add(newDate);
    }



    /**
     * Validates date addition parameters.
     * Follows Single Responsibility Principle - validation logic separated.
     * @param dayNumber the day number to validate
     * @param modifier the modifier to validate
     * @return true if valid, false otherwise
     */
    private boolean validateDateAddition(int dayNumber, double modifier) {
        if (dayNumber < 1 || dayNumber > MAX_DATES) {
            System.out.println("[ERROR] Day number must be between 1 and " + MAX_DATES);
            return false;
        }
        if (modifier < MIN_MODIFIER || modifier > MAX_MODIFIER) {
            System.out.println("[ERROR] Modifier must be between " + MIN_MODIFIER + " and " + MAX_MODIFIER);
            return false;
        }
        if (dates.size() >= MAX_DATES) {
            System.out.println("[ERROR] Cannot add more than " + MAX_DATES + " dates");
            return false;
        }
        return true;
    }

    /**
     * Removes a date from the property.
     * @param dayNumber the day number to remove
     */
    public void removeDate(int dayNumber) {
        if (!validateDateRemoval(dayNumber)) {
            return;
        }

        for (int i = 0; i < dates.size(); i++) {
            Date date = dates.get(i);
            if (date.getDayNumber() == dayNumber) {
                dates.remove(i);
                System.out.println("[SUCCESS] Date removed successfully");
                return;
            }
        }
        System.out.println("[ERROR] Day " + dayNumber + " not found in property");
    }

    /**
     * Validates date removal parameters.
     * Follows Single Responsibility Principle - validation logic separated.
     * @param dayNumber the day number to validate
     * @return true if valid, false otherwise
     */
    private boolean validateDateRemoval(int dayNumber) {
        if (dayNumber < 1 || dayNumber > MAX_DATES) {
            System.out.println("[ERROR] Day number must be between 1 and " + MAX_DATES);
            return false;
        }

        Date date = findDate(dayNumber);
        if (date != null && date.isBooked()) {
            System.out.println("[ERROR] Cannot remove booked date: " + dayNumber);
            return false;
        }
        return true;
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
        if (!validateDateRange(checkIn, checkOut)) {
            return false;
        }

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
        ArrayList<Integer> unavailable = new ArrayList<Integer>();
        if (!validateDateRange(checkIn, checkOut)) {
            return unavailable;
        }

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
     * @return true if valid, false otherwise
     */
    private boolean validateDateRange(int checkIn, int checkOut) {
        if (checkIn < 1 || checkIn >= checkOut || checkOut > MAX_DATES + 1) {
            System.out.println("[ERROR] Invalid date range: checkIn=" + checkIn + ", checkOut=" + checkOut);
            return false;
        }
        if (checkIn == MAX_DATES) {
            System.out.println("[ERROR] Cannot check-in on day " + MAX_DATES);
            return false;
        }
        return true;
    }

    /**
     * Books a range of dates for a reservation.
     * @param checkIn the check-in day
     * @param checkOut the check-out day
     */
    public void bookDates(int checkIn, int checkOut) {
        if (!areDatesAvailable(checkIn, checkOut)) {
            ArrayList<Integer> unavailable = getUnavailableDays(checkIn, checkOut);
            System.out.println("[ERROR] Dates not available: " + unavailable);
            return;
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
            System.out.println("[ERROR] Reservation cannot be null");
            return;
        }
        reservations.add(reservation);
        reservation.calculateTotal(dates);
    }

    // Calculation Methods following Single Responsibility Principle

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
     * Updates all date prices based on the current base price and property multiplier.
     */
    private void updateAllDatePrices() {
        double propertyRate = getPropertyRate();
        for (Date date : dates) {
            date.updatePrice(propertyRate);
        }
    }

    // Display Methods following Interface Segregation Principle

    /**
     * Displays comprehensive information about the property.
     */
    public void displayInfo() {
        System.out.println("\n=== PROPERTY INFORMATION ===");
        System.out.println("-----------------------------------");
        System.out.println("Property Name: " + name);
        System.out.println("Property Type: " + propertyType);
        System.out.println("Base Price: PHP " + String.format("%.2f", basePrice) + " per night");
        System.out.println("Property Rate: PHP " + String.format("%.2f", getPropertyRate()) + " per night");
        System.out.println("Total Dates Listed: " + dates.size());
        System.out.println("Available Dates: " + getAvailableDateCount());
        System.out.println("Booked Dates: " + getBookedDateCount());
        System.out.println("Occupancy Rate: " + String.format("%.1f%%", getOccupancyRate() * 100));
        System.out.println("Total Reservations: " + reservations.size());
        System.out.println("Total Earnings: PHP " + String.format("%.2f", calculateEarnings()));
        System.out.println("-----------------------------------");
    }

    /**
     * Displays a calendar view of all dates with proper weekday alignment.
     */
    public void displayCalendar() {
        System.out.println("\n+-----------------------------+");
        System.out.println("|        MONTH CALENDAR       |");
        System.out.println("+-----------------------------+");
        System.out.println("| SUN MON TUE WED THU FRI SAT |");
        System.out.println("+-----------------------------+");

        int startDayOfWeek = 0; // 0 = Sunday
        int currentDay = 1;

        // Print leading spaces for the first week
        for (int i = 0; i < startDayOfWeek; i++) {
            System.out.print("     ");
        }

        for (int day = 1; day <= 30; day++) {
            Date date = findDate(day);
            String status = "  "; // Default: 2 spaces for empty

            if (date != null) {
                if (date.isBooked()) {
                    status = "B ";
                } else {
                    status = "A ";
                }

                double modPct = date.getModifier() * 100;
                if (modPct < 90) {
                    status += "G";
                } else if (modPct == 100) {
                    status += "W";
                } else {
                    status += "Y";
                }
            }

            System.out.printf("%3s%s", day, status);

            // Move to next line after Saturday
            if ((day + startDayOfWeek) % 7 == 0) {
                System.out.println();
            }
        }

        System.out.println("\n+-----------------------------+");
        System.out.println("Legend: A=Available, B=Booked");
        System.out.println("Color: G=Green(80-89%), W=White(100%), Y=Yellow(101-120%)");
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
        System.out.println("Property Rate: PHP " + String.format("%.2f", date.getBasePrice()));
        System.out.println("Final Price: PHP " + String.format("%.2f", date.getFinalPrice()));
        System.out.println("Environmental Impact: " + date.getEnvironmentalImpactName());
        System.out.println("Modifier: " + String.format("%.0f", date.getModifier() * 100) + "%");
        System.out.println("Status: " + (date.isBooked() ? "BOOKED" : "AVAILABLE"));

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
        if (!validateDateRange(startDay, endDay + 1)) {
            return;
        }

        System.out.println("\n=== RESERVATIONS FOR DAYS " + startDay + " TO " + endDay + " ===");

        boolean found = false;
        for (Reservation reservation : reservations) {
            if (reservation.getCheckIn() <= endDay && reservation.getCheckOut() > startDay) {
                reservation.displayReservation();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No reservations found in the specified date range.");
        }
    }

    /**
     * Returns a string representation of the property.
     * @return string representation of the property
     */
    @Override
    public String toString() {
        return "Property{name='" + name + "', type='" + propertyType + "', basePrice=" + basePrice +
                ", propertyRate=" + String.format("%.2f", getPropertyRate()) +
                ", dates=" + dates.size() + ", reservations=" + reservations.size() + "}";
    }
}