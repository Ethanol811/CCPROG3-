/**
 * InputValidator.java
 *
 * Centralized input validation utility for the Green Property Exchange system.
 * Provides comprehensive validation methods for various input types and domain-specific rules.
 * Implements the Strategy pattern for flexible validation rules.
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

    // -------------------------------------------------------
    // Functional Interface for Validators
    // -------------------------------------------------------

    /**
     * Functional interface for validation operations that may throw IllegalArgumentException.
     *
     * @param <T> the type of value being validated
     */
    @FunctionalInterface
    public interface Validator<T> {
        /**
         * Validates the given value.
         *
         * @param value the value to validate
         * @throws IllegalArgumentException if the value is invalid
         */
        void validate(T value) throws IllegalArgumentException;
    }

    // -------------------------------------------------------
    // Common Validators
    // -------------------------------------------------------

    /**
     * Validates that a string is not null or empty.
     *
     * @param value the string to validate
     * @throws IllegalArgumentException if the string is null or empty
     */
    public void validateNonEmpty(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Value cannot be empty");
        }
    }

    /**
     * Validates that a number is positive.
     *
     * @param value the number to validate
     * @throws IllegalArgumentException if the number is negative
     */
    public void validatePositiveNumber(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    /**
     * Validates that a number is within a specified range.
     *
     * @param value the number to validate
     * @param min the minimum allowed value (inclusive)
     * @param max the maximum allowed value (inclusive)
     * @throws IllegalArgumentException if the value is outside the range
     */
    public void validateRange(double value, double min, double max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    String.format("Value must be between %.1f and %.1f", min, max)
            );
        }
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
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(sc.nextLine().trim());
                if (value < min || value > max) {
                    System.out.printf("[ERROR] Enter a number between %d and %d: ", min, max);
                    continue;
                }
                return value;
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
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(sc.nextLine().trim());
                if (value < min || value > max) {
                    System.out.printf("[ERROR] Enter a number between %.1f and %.1f: ", min, max);
                    continue;
                }
                return value;
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
            try {
                System.out.print(prompt);
                String input = sc.nextLine().trim();
                if (validator.test(input)) {
                    return input;
                }
                System.out.println("[ERROR] " + errorMessage);
            } catch (Exception e) {
                System.out.println("[ERROR] Invalid input: " + e.getMessage());
            }
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
     * @throws IllegalArgumentException if the name is invalid
     */
    public void validatePropertyName(String name) {
        validateNonEmpty(name);
        if (name.length() < 2) {
            throw new IllegalArgumentException("Property name must be at least 2 characters long");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Property name cannot exceed 50 characters");
        }
        // Add more business rules as needed
    }

    /**
     * Validates a guest name according to system requirements.
     *
     * @param name the guest name to validate
     * @throws IllegalArgumentException if the name is invalid
     */
    public void validateGuestName(String name) {
        validateNonEmpty(name);
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("Guest name can only contain letters and spaces");
        }
    }

    /**
     * Validates a date range for booking operations.
     *
     * @param checkIn the check-in day
     * @param checkOut the check-out day
     * @throws IllegalArgumentException if the date range is invalid
     */
    public void validateDateRange(int checkIn, int checkOut) {
        if (checkIn < 1 || checkIn >= 30) {
            throw new IllegalArgumentException("Check-in must be between day 1 and 29");
        }
        if (checkOut <= checkIn || checkOut > 30) {
            throw new IllegalArgumentException("Check-out must be after check-in and before day 31");
        }
        if (checkOut - checkIn > 30) {
            throw new IllegalArgumentException("Maximum stay is 30 days");
        }
    }
}