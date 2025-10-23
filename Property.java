import java.util.ArrayList;

/**
 * Property.java
 * 
 * Represents a property listing in the Green Property Exchange system.
 * Each property contains its name, base price per night, a list of available dates,
 * and a list of reservations (initially empty). 
 * 
 * MCO1 - Green Property Exchange
 * @author Group 23 - John Ethan Chiuten ,Julian Nicos Reyes
 * @version 1.5
 */
public class Property {
    private String name;
    private double basePrice;
    private ArrayList<Date> dates;
    private ArrayList<Reservation> reservations;

    /** 
     * Default base price for all properties (PHP 1,500.00 per night).
     * Initializes empty lists for dates and reservations.
     * @param name  property name
     */
    public Property(String name) {
        if (name == null || name.trim().isEmpty()) {
            this.name = "Unnamed Property";
        } else {
            this.name = name.trim();
        }
        this.basePrice = 1500.00;
        this.dates = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    // -------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------

    /**
     * Gets the property name.
     * @return property name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the property's name.
     * Validation for unique names should be handled in SystemManager.
     * @param newName new property name
     */
    public void setName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            System.out.println("[ERROR] Invalid name. Property name cannot be blank.");
        } else {
            this.name = newName.trim();
            System.out.println("[SUCCESS] Property name successfully changed to: " + this.name);
        }
    }

    /**
     * Gets the base price per night.
     * @return base price per night
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * Updates the base price for all dates.
     * Can only be changed if there are no reservations.
     * @param newPrice new base price (must be >= 100 PHP)
     */
    public void setBasePrice(double newPrice) {
        if (newPrice < 100) {
            System.out.println("[ERROR] New price must be at least PHP 100.00.");
        } else if (!reservations.isEmpty()) {
            System.out.println("[ERROR] Cannot change base price while reservations exist.");
        } else {
            this.basePrice = newPrice;
            for (Date d : dates) {
                d.setPricePerNight(newPrice);
            }
            System.out.println("[SUCCESS] Base price successfully updated to PHP " + String.format("%.2f", newPrice));
        }
    }

    /**
     * Gets the list of available dates.
     * @return list of Date objects
     */
    public ArrayList<Date> getDates() {
        return dates;
    }

    /**
     * Gets the list of reservations.
     * @return list of Reservation objects
     */
    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    // -------------------------------------------------------
    // Core Property Methods
    // -------------------------------------------------------

    /**
     * Adds a new available date to the property (max 30).
     * @param dayNumber the day number (1 – 30)
     */
    public void addDate(int dayNumber) {
        if (dates.size() >= 30) {
            System.out.println("[ERROR] Cannot add more than 30 dates.");
            return;
        }
        if (dayNumber < 1 || dayNumber > 30) {
            System.out.println("[ERROR] Invalid day number. Must be between 1-30.");
            return;
        }

        // check for duplicates
        for (Date d : dates) {
            if (d.getDayNumber() == dayNumber) {
                System.out.println("[ERROR] Day " + dayNumber + " already exists in this property.");
                return;
            }
        }

        dates.add(new Date(dayNumber, basePrice));
        System.out.println("[SUCCESS] Added date " + dayNumber + " with price PHP " + String.format("%.2f", basePrice));
    }

    /**
     * Removes a date by its number.
     * @param dayNumber day number to remove
     */
    public void removeDate(int dayNumber) {
        for (int i = 0; i < dates.size(); i++) {
            Date date = dates.get(i);
            if (date.getDayNumber() == dayNumber) {
                if (date.isBooked()) {
                    System.out.println("[ERROR] Cannot remove date " + dayNumber + " because it is currently booked.");
                    return;
                }
                dates.remove(i);
                System.out.println("[SUCCESS] Removed date " + dayNumber);
                return;
            }
        }
        System.out.println("[ERROR] Date " + dayNumber + " not found in this property.");
    }

    /**
     * Finds a date by day number.
     * @param dayNumber the day number to find
     * @return Date object or null if not found
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
     * Checks if dates are available for booking.
     * @param checkIn check-in day
     * @param checkOut check-out day
     * @return true if all dates are available, false otherwise
     */
    public boolean areDatesAvailable(int checkIn, int checkOut) {
        ArrayList<Integer> unavailableDays = new ArrayList<>();
        
        for (int day = checkIn; day < checkOut; day++) {
            Date date = findDate(day);
            if (date == null) {
                unavailableDays.add(day);
            } else if (date.isBooked()) {
                unavailableDays.add(day);
            }
        }
        
        if (!unavailableDays.isEmpty()) {
            System.out.println("[ERROR] The following days are unavailable: " + unavailableDays);
            if (unavailableDays.size() == 1) {
                Date problemDate = findDate(unavailableDays.get(0));
                if (problemDate != null && problemDate.isBooked()) {
                    System.out.println("   Day " + unavailableDays.get(0) + " is already booked.");
                } else {
                    System.out.println("   Day " + unavailableDays.get(0) + " is not available in this property.");
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Books dates for a reservation.
     * @param checkIn check-in day
     * @param checkOut check-out day
     */
    public void bookDates(int checkIn, int checkOut) {
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
        reservations.add(reservation);
        // Recalculate total price for this reservation
        reservation.calculateTotal(dates);
    }

    /**
     * Calculates total earnings for all reservations.
     * @return total revenue for the property
     */
    public double calculateEarnings() {
        double total = 0;
        for (Reservation r : reservations) {
            total += r.getTotalPrice();
        }
        return total;
    }

    /**
     * Gets count of available (not booked) dates.
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
     * Gets count of booked dates.
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
     * Displays summary info for the property.
     */
    public void displayInfo() {
        System.out.println("\n=== PROPERTY INFORMATION ===");
        System.out.println("-----------------------------------");
        System.out.println("Property Name: " + name);
        System.out.println("Base Price: PHP " + String.format("%.2f", basePrice) + " per night");
        System.out.println("Total Dates Listed: " + dates.size());
        System.out.println("Available Dates: " + getAvailableDateCount());
        System.out.println("Booked Dates: " + getBookedDateCount());
        System.out.println("Total Reservations: " + reservations.size());
        System.out.println("Total Earnings: PHP " + String.format("%.2f", calculateEarnings()));
        System.out.println("-----------------------------------");
    }

    /**
     * Displays calendar view of dates in a grid format similar to a planner.
     */
    public void displayCalendar() {
        System.out.println("\n=== PROPERTY CALENDAR VIEW ===");
        System.out.println("Base Price: PHP " + String.format("%.2f", basePrice) + " per night");
        System.out.println("\n+-----------------------------+");
        System.out.println("|        MONTH CALENDAR       |");
        System.out.println("+-----------------------------+");
        System.out.println("| SUN MON TUE WED THU FRI SAT |");
        System.out.println("+-----------------------------+");
        
        // Start with day 1 (assuming it's Sunday for simplicity)
        int startDay = 1;
        
        // Print leading spaces for the first week
        for (int i = 1; i < startDay; i++) {
            System.out.print("     ");
        }
        
        // Print the calendar grid
        for (int day = 1; day <= 30; day++) {
            Date date = findDate(day);
            String status = " ";
            
            if (date != null) {
                if (date.isBooked()) {
                    status = "B"; // Booked
                } else {
                    status = "A"; // Available
                }
            } else {
                status = "-"; // Not available in property
            }
            
            System.out.printf(" %2d%s ", day, status);
            
            // New line after Saturday
            if ((day + startDay - 1) % 7 == 0) {
                System.out.println();
            }
        }
        System.out.println("\n+-----------------------------+");
        System.out.println("| A = Available              |");
        System.out.println("| B = Booked                 |");
        System.out.println("| - = Not in property        |");
        System.out.println("+-----------------------------+");
    }

    /**
     * Displays detailed information about a specific date.
     * @param dayNumber the day number to display details for
     */
    public void displayDateInfo(int dayNumber) {
        Date date = findDate(dayNumber);
        if (date == null) {
            System.out.println("[ERROR] Day " + dayNumber + " is not available in this property.");
            return;
        }
        
        System.out.println("\n=== DATE DETAILS ===");
        System.out.println("-----------------------------------");
        System.out.println("Day Number: " + dayNumber);
        System.out.println("Price per night: PHP " + String.format("%.2f", date.getPricePerNight()));
        System.out.println("Status: " + (date.isBooked() ? "BOOKED" : "AVAILABLE"));
        
        // Find which reservation booked this date
        for (Reservation reservation : reservations) {
            if (dayNumber >= reservation.getCheckIn() && dayNumber < reservation.getCheckOut()) {
                System.out.println("Booked by: " + reservation.getGuestName());
                System.out.println("Reservation: Day " + reservation.getCheckIn() + " to " + reservation.getCheckOut());
                break;
            }
        }
        System.out.println("-----------------------------------");
    }

    /**
     * Displays reservation information for a selected date range.
     * @param startDay start day of range
     * @param endDay end day of range
     */
    public void displayReservationInfo(int startDay, int endDay) {
        System.out.println("\n=== RESERVATION INFORMATION ===");
        System.out.println("-----------------------------------");
        System.out.println("Date Range: Day " + startDay + " to Day " + endDay);
        
        int availableCount = 0;
        int bookedCount = 0;
        int notAvailableCount = 0;
        
        for (int day = startDay; day <= endDay; day++) {
            Date date = findDate(day);
            if (date != null) {
                if (date.isBooked()) {
                    bookedCount++;
                } else {
                    availableCount++;
                }
            } else {
                notAvailableCount++;
            }
        }
        
        System.out.println("Available dates: " + availableCount);
        System.out.println("Booked dates: " + bookedCount);
        System.out.println("Not in property: " + notAvailableCount);
        
        // Show reservations that overlap with this range
        boolean foundReservations = false;
        for (Reservation reservation : reservations) {
            if (reservation.getCheckIn() <= endDay && reservation.getCheckOut() >= startDay) {
                if (!foundReservations) {
                    System.out.println("\nOVERLAPPING RESERVATIONS:");
                    foundReservations = true;
                }
                reservation.displayReservation();
            }
        }
        
        if (!foundReservations) {
            System.out.println("\n[INFO] No reservations in this date range.");
        }
        System.out.println("-----------------------------------");
    }
}