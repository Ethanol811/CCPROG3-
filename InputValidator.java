/**
 * InputValidator.java
 *
 * Centralized input validation utility for the Green Property Exchange system.
 * Provides comprehensive validation methods for various input types and domain-specific rules.
 *
 * MCO1 - Green Property Exchange
 * @author Group 23
 * @version 1.0
 */

import java.util.Scanner;
import java.util.function.Predicate;

public class InputValidator {
    private final Scanner sc;

    /**
     * Constructs a new InputValidator with a scanner for user input.
     */
    public InputValidator() {
        this.sc = new Scanner(System.in);
    }

    /**
     * Validates that a string is not null or empty.
     *
     * @param value the string to validate
     * @return true if valid, false otherwise
     */
    public boolean validateNonEmpty(String value) {
        if (value == null || value.trim().isEmpty()) {
            System.out.println("[ERROR] Value cannot be empty");
            return false;
        }
        return true;
    }

    /**
     * Validates that a number is positive.
     *
     * @param value the number to validate
     * @return true if valid, false otherwise
     */
    public boolean validatePositiveNumber(double value) {
        if (value < 0) {
            System.out.println("[ERROR] Value must be positive");
            return false;
        }
        return true;
    }

    /**
     * Validates that a number is within a specified range.
     *
     * @param value the number to validate
     * @param min the minimum allowed value (inclusive)
     * @param max the maximum allowed value (inclusive)
     * @return true if valid, false otherwise
     */
    public boolean validateRange(double value, double min, double max) {
        if (value < min || value > max) {
            System.out.printf("[ERROR] Value must be between %.1f and %.1f%n", min, max);
            return false;
        }
        return true;
    }

    // -------------------------------------------------------
    // Validated Input Methods
    // -------------------------------------------------------

    /**
     * Gets a validated integer input from the user within a specified range.
     *
     * @param prompt the prompt to display to the user
     * @param min the minimum allowed value (inclusive)
     * @param max the maximum allowed value (inclusive)
     * @return the validated integer input
     */
    public int getValidatedInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("[ERROR] Enter a number between %d and %d%n", min, max);
            } catch (NumberFormatException e) {
                System.out.print("[ERROR] Invalid input. Enter a whole number: ");
            }
        }
    }

    /**
     * Gets a validated double input from the user within a specified range.
     *
     * @param prompt the prompt to display to the user
     * @param min the minimum allowed value (inclusive)
     * @param max the maximum allowed value (inclusive)
     * @return the validated double input
     */
    public double getValidatedDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                double value = Double.parseDouble(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("[ERROR] Enter a number between %.1f and %.1f%n", min, max);
            } catch (NumberFormatException e) {
                System.out.print("[ERROR] Invalid input. Enter a number: ");
            }
        }
    }

    /**
     * Gets a validated string input from the user using a custom predicate.
     *
     * @param prompt the prompt to display to the user
     * @param validator the predicate to validate the input
     * @param errorMessage the error message to display on validation failure
     * @return the validated string input
     */
    public String getValidatedString(String prompt, Predicate<String> validator, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            if (validator.test(input)) {
                return input;
            }
            System.out.println("[ERROR] " + errorMessage);
        }
    }

    /**
     * Gets a validated boolean input from the user (Y/N).
     *
     * @param prompt the prompt to display to the user
     * @return true for yes/affirmative, false for no/negative
     */
    public boolean getValidatedBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = sc.nextLine().trim().toUpperCase();

            if (input.equals("Y") || input.equals("YES")) {
                return true;
            } else if (input.equals("N") || input.equals("NO")) {
                return false;
            } else {
                System.out.print("[ERROR] Please enter Y or N: ");
            }
        }
    }

    // -------------------------------------------------------
    // Specific Domain Validators
    // -------------------------------------------------------

    /**
     * Validates a property name according to system requirements.
     *
     * @param name the property name to validate
     * @return true if valid, false otherwise
     */
    public boolean validatePropertyName(String name) {
        if (!validateNonEmpty(name)) {
            return false;
        }
        if (name.length() < 2) {
            System.out.println("[ERROR] Property name must be at least 2 characters long");
            return false;
        }
        if (name.length() > 50) {
            System.out.println("[ERROR] Property name cannot exceed 50 characters");
            return false;
        }
        return true;
    }

    /**
     * Validates a guest name according to system requirements.
     *
     * @param name the guest name to validate
     * @return true if valid, false otherwise
     */
    public boolean validateGuestName(String name) {
        if (!validateNonEmpty(name)) {
            return false;
        }
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            System.out.println("[ERROR] Guest name can only contain letters and spaces");
            return false;
        }
        return true;
    }

    /**
     * Validates a date range for booking operations.
     *
     * @param checkIn the check-in day
     * @param checkOut the check-out day
     * @return true if valid, false otherwise
     */
    public boolean validateDateRange(int checkIn, int checkOut) {
        if (checkIn < 1 || checkIn >= 30) {
            System.out.println("[ERROR] Check-in must be between day 1 and 29");
            return false;
        }
        if (checkOut <= checkIn || checkOut > 30) {
            System.out.println("[ERROR] Check-out must be after check-in and before day 31");
            return false;
        }
        if (checkOut - checkIn > 30) {
            System.out.println("[ERROR] Maximum stay is 30 days");
            return false;
        }
        return true;
    }
}