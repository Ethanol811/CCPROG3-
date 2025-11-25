/**
 * MainFrame.java
 *
 * Main GUI window for the Green Property Exchange MCO2 application.
 * Provides a comprehensive user interface for property management, booking simulation,
 * and calendar viewing using a card layout for screen navigation.
 *
 * Features include:
 * - Property creation and management
 * - Interactive calendar with environmental modifiers
 * - Booking simulation and reservation processing
 * - Mouse-controlled inputs and visual feedback
 *
 * MCO2 - Green Property Exchange
 * @author Group 23
 * @version 2.3
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private SystemManager manager;

    // Panel identifiers for card layout navigation
    private static final String MAIN_MENU = "MAIN_MENU";
    private static final String CREATE_PROPERTY = "CREATE_PROPERTY";
    private static final String VIEW_PROPERTY = "VIEW_PROPERTY";
    private static final String MANAGE_PROPERTY = "MANAGE_PROPERTY";
    private static final String SIMULATE_BOOKING = "SIMULATE_BOOKING";
    private static final String CALENDAR_VIEW = "CALENDAR_VIEW";

    /**
     * Constructs a new MainFrame and initializes the GUI components.
     * Sets up the SystemManager and creates all interface panels.
     */
    public MainFrame() {
        // Initialize manager FIRST before any GUI components
        this.manager = new SystemManager();
        initializeGUI();
    }

    /**
     * Initializes the main GUI components including window properties,
     * card layout system, and all functional panels.
     * Sets up the main application window with proper sizing and positioning.
     */
    private void initializeGUI() {
        try {
            // Basic window setup
            setTitle("Green Property Exchange - MCO2");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1200, 800);
            setLocationRelativeTo(null);

            // Initialize CardLayout for screen management
            cardLayout = new CardLayout();
            mainPanel = new JPanel(cardLayout);

            // Create and add panels
            mainPanel.add(createMainMenuPanel(), MAIN_MENU);
            mainPanel.add(createPropertyCreationPanel(), CREATE_PROPERTY);
            mainPanel.add(createCalendarPanel(), CALENDAR_VIEW);
            mainPanel.add(createManagePropertyPanel(), MANAGE_PROPERTY);
            mainPanel.add(createSimulateBookingPanel(), SIMULATE_BOOKING);

            add(mainPanel);

            // Show main menu first
            cardLayout.show(mainPanel, MAIN_MENU);

        } catch (Exception e) {
            System.err.println("GUI initialization error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize GUI", e);
        }
    }

    /**
     * Creates and returns the main menu panel with navigation buttons.
     * Provides access to all major system functions through a clean, organized interface.
     *
     * @return JPanel containing the main menu with all navigation options
     */
    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 255, 240));

        // Header
        JLabel headerLabel = new JLabel("GREEN PROPERTY EXCHANGE", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(0, 100, 0));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 50, 100));
        buttonPanel.setBackground(new Color(240, 255, 240));

        // Create menu buttons
        String[] buttonLabels = {
                "Create Property",
                "View Property",
                "Manage Property",
                "Simulate Booking",
                "Exit System"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.setBackground(new Color(144, 238, 144));
            button.setFocusPainted(false);

            button.addActionListener(new MenuButtonListener(label));
            buttonPanel.add(button);
        }

        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates and returns the property creation panel.
     * Allows users to create new property listings with name, type, and base price.
     * Validates input and provides feedback on creation success/failure.
     *
     * @return JPanel containing the property creation form
     */
    private JPanel createPropertyCreationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Back button
        JButton backButton = new JButton("← Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, MAIN_MENU));

        // Header
        JLabel headerLabel = new JLabel("Create Property Listing", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Property Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Property Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Property Type
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Property Type:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{
                "Eco-Apartment", "Sustainable House", "Green Resort", "Eco-Glamping"
        });
        formPanel.add(typeComboBox, gbc);

        // Base Price
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Base Price:"), gbc);
        gbc.gridx = 1;
        JTextField priceField = new JTextField("1500.0", 20);
        formPanel.add(priceField, gbc);

        // Create Button
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton createButton = new JButton("Create Property");
        createButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String type = (String) typeComboBox.getSelectedItem();
            String priceText = priceField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Property name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double price = Double.parseDouble(priceText);
                if (price < 100 || price > 999999) {
                    JOptionPane.showMessageDialog(this, "Price must be between 100 and 999999!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = manager.createPropertyGUI(name, type, price);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Property created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    nameField.setText("");
                    priceField.setText("1500.0");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create property. Name may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid price!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        formPanel.add(createButton, gbc);

        panel.add(backButton, BorderLayout.NORTH);
        panel.add(headerLabel, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates and returns the calendar view panel.
     * Displays an interactive calendar with environmental impact indicators
     * and allows property selection for viewing availability and modifiers.
     *
     * @return JPanel containing the calendar view with property selector
     */
    private JPanel createCalendarPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Back button
        JButton backButton = new JButton("← Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, MAIN_MENU));

        // Calendar component
        CalendarPanel calendarPanel = new CalendarPanel();

        // Property selector
        JPanel selectorPanel = new JPanel(new FlowLayout());
        JLabel selectorLabel = new JLabel("Select Property: ");
        JComboBox<String> propertyComboBox = new JComboBox<>();
        JButton refreshButton = new JButton("Refresh");

        refreshButton.addActionListener(e -> refreshPropertyComboBox(propertyComboBox));

        propertyComboBox.addActionListener(e -> {
            String selectedName = (String) propertyComboBox.getSelectedItem();
            if (selectedName != null && manager != null) {
                Property prop = manager.findProperty(selectedName);
                calendarPanel.setProperty(prop);
            }
        });

        selectorPanel.add(selectorLabel);
        selectorPanel.add(propertyComboBox);
        selectorPanel.add(refreshButton);

        // Initial refresh
        refreshPropertyComboBox(propertyComboBox);

        panel.add(backButton, BorderLayout.NORTH);
        panel.add(selectorPanel, BorderLayout.CENTER);
        panel.add(calendarPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates and returns the property management panel.
     * Provides comprehensive property editing capabilities including:
     * - Name, type, and base price modification
     * - Date management (add/remove available dates)
     * - Property removal (with validation)
     *
     * @return JPanel containing all property management controls
     */
    private JPanel createManagePropertyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Back button
        JButton backButton = new JButton("← Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, MAIN_MENU));

        // Header
        JLabel headerLabel = new JLabel("Manage Property", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Main content panel - using GridBagLayout for better control
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Property selector
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel selectorLabel = new JLabel("Select Property: ");
        JComboBox<String> propertyComboBox = new JComboBox<>();
        JButton refreshButton = new JButton("Refresh");
        selectorPanel.add(selectorLabel);
        selectorPanel.add(propertyComboBox);
        selectorPanel.add(refreshButton);
        contentPanel.add(selectorPanel, gbc);

        // Property Information Section
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel infoLabel = new JLabel("Property Information:");
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(infoLabel, gbc);

        // Property Name
        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(new JLabel("Property Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        contentPanel.add(nameField, gbc);

        // Property Type
        gbc.gridx = 0; gbc.gridy = 3;
        contentPanel.add(new JLabel("Property Type:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{
                "Eco-Apartment", "Sustainable House", "Green Resort", "Eco-Glamping"
        });
        contentPanel.add(typeComboBox, gbc);

        // Base Price
        gbc.gridx = 0; gbc.gridy = 4;
        contentPanel.add(new JLabel("Base Price:"), gbc);
        gbc.gridx = 1;
        JTextField priceField = new JTextField(20);
        contentPanel.add(priceField, gbc);

        // Update buttons for property info
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel updateButtonPanel = new JPanel(new FlowLayout());
        JButton updateNameButton = new JButton("Update Name");
        JButton updatePriceButton = new JButton("Update Price");
        JButton updateTypeButton = new JButton("Update Type");
        updateButtonPanel.add(updateNameButton);
        updateButtonPanel.add(updatePriceButton);
        updateButtonPanel.add(updateTypeButton);
        contentPanel.add(updateButtonPanel, gbc);

        // Date Management Section
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 1;
        JLabel dateLabel = new JLabel("Date Management:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(dateLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        JPanel datePanel = new JPanel(new FlowLayout());
        datePanel.add(new JLabel("Day (1-30):"));
        JTextField dateField = new JTextField(5);
        datePanel.add(dateField);
        JButton addDateButton = new JButton("Add Date");
        JButton removeDateButton = new JButton("Remove Date");
        datePanel.add(addDateButton);
        datePanel.add(removeDateButton);
        contentPanel.add(datePanel, gbc);

        // Remove Property Section
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        JLabel removeLabel = new JLabel("Remove Property:");
        removeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(removeLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 9;
        gbc.gridwidth = 2;
        JButton removePropertyButton = new JButton("Remove This Property");
        removePropertyButton.setBackground(new Color(255, 200, 200));
        contentPanel.add(removePropertyButton, gbc);

        // Action listeners
        refreshButton.addActionListener(e -> refreshPropertyComboBox(propertyComboBox));

        propertyComboBox.addActionListener(e -> {
            String selectedName = (String) propertyComboBox.getSelectedItem();
            if (selectedName != null && manager != null) {
                Property prop = manager.findProperty(selectedName);
                if (prop != null) {
                    nameField.setText(prop.getName());
                    priceField.setText(String.format("%.2f", prop.getBasePrice()));
                    typeComboBox.setSelectedItem(prop.getPropertyType());
                }
            }
        });

        updateNameButton.addActionListener(e -> {
            String selectedName = (String) propertyComboBox.getSelectedItem();
            if (selectedName != null && manager != null) {
                Property prop = manager.findProperty(selectedName);
                if (prop != null) {
                    String newName = nameField.getText().trim();
                    if (!newName.isEmpty()) {
                        prop.setName(newName);
                        JOptionPane.showMessageDialog(this, "Property name updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshPropertyComboBox(propertyComboBox);
                        propertyComboBox.setSelectedItem(newName);
                    }
                }
            }
        });

        updatePriceButton.addActionListener(e -> {
            String selectedName = (String) propertyComboBox.getSelectedItem();
            if (selectedName != null && manager != null) {
                Property prop = manager.findProperty(selectedName);
                if (prop != null) {
                    try {
                        double newPrice = Double.parseDouble(priceField.getText().trim());
                        prop.setBasePrice(newPrice);
                        JOptionPane.showMessageDialog(this,
                                "Base price updated successfully!\nNew Property Rate: PHP " +
                                        String.format("%.2f", prop.getPropertyRate()),
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Please enter a valid price!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        updateTypeButton.addActionListener(e -> {
            String selectedName = (String) propertyComboBox.getSelectedItem();
            if (selectedName != null && manager != null) {
                Property prop = manager.findProperty(selectedName);
                if (prop != null) {
                    String newType = (String) typeComboBox.getSelectedItem();
                    prop.setPropertyType(newType);
                    JOptionPane.showMessageDialog(this,
                            "Property type updated successfully!\nNew Property Rate: PHP " +
                                    String.format("%.2f", prop.getPropertyRate()),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        addDateButton.addActionListener(e -> {
            String selectedName = (String) propertyComboBox.getSelectedItem();
            if (selectedName != null && manager != null) {
                Property prop = manager.findProperty(selectedName);
                if (prop != null) {
                    try {
                        int day = Integer.parseInt(dateField.getText().trim());
                        prop.addDate(day);
                        JOptionPane.showMessageDialog(this, "Date added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dateField.setText("");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Please enter a valid day number (1-30)!", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        removeDateButton.addActionListener(e -> {
            String selectedName = (String) propertyComboBox.getSelectedItem();
            if (selectedName != null && manager != null) {
                Property prop = manager.findProperty(selectedName);
                if (prop != null) {
                    try {
                        int day = Integer.parseInt(dateField.getText().trim());
                        prop.removeDate(day);
                        JOptionPane.showMessageDialog(this, "Date removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dateField.setText("");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Please enter a valid day number (1-30)!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        removePropertyButton.addActionListener(e -> {
            String selectedName = (String) propertyComboBox.getSelectedItem();
            if (selectedName != null && manager != null) {
                Property prop = manager.findProperty(selectedName);
                if (prop != null) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to remove property '" + selectedName + "'?",
                            "Confirm Removal", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (manager.removeProperty(prop)) {
                            JOptionPane.showMessageDialog(this, "Property removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            refreshPropertyComboBox(propertyComboBox);
                            // Clear fields
                            nameField.setText("");
                            priceField.setText("");
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Cannot remove property with active reservations!",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        panel.add(backButton, BorderLayout.NORTH);
        panel.add(headerLabel, BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.SOUTH);

        // Initial refresh
        refreshPropertyComboBox(propertyComboBox);

        return panel;
    }

    /**
     * Creates and returns the booking simulation panel.
     * Allows users to simulate property bookings with comprehensive validation
     * and price calculation including environmental modifiers.
     *
     * @return JPanel containing the booking simulation interface
     */
    private JPanel createSimulateBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Back button
        JButton backButton = new JButton("← Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, MAIN_MENU));

        // Header
        JLabel headerLabel = new JLabel("Simulate Booking", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        contentPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Property selection
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(new JLabel("Select Property:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> propertyComboBox = new JComboBox<>();
        JButton refreshButton = new JButton("Refresh");
        JPanel propertyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        propertyPanel.add(propertyComboBox);
        propertyPanel.add(refreshButton);
        contentPanel.add(propertyPanel, gbc);

        // Guest information
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(new JLabel("Guest Name:"), gbc);
        gbc.gridx = 1;
        JTextField guestNameField = new JTextField(20);
        contentPanel.add(guestNameField, gbc);

        // Check-in date
        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(new JLabel("Check-in Day (1-29):"), gbc);
        gbc.gridx = 1;
        JTextField checkInField = new JTextField(5);
        contentPanel.add(checkInField, gbc);

        // Check-out date
        gbc.gridx = 0; gbc.gridy = 3;
        contentPanel.add(new JLabel("Check-out Day (2-30):"), gbc);
        gbc.gridx = 1;
        JTextField checkOutField = new JTextField(5);
        contentPanel.add(checkOutField, gbc);

        // Booking summary
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        JTextArea summaryArea = new JTextArea(10, 40);
        summaryArea.setEditable(false);
        summaryArea.setBorder(BorderFactory.createTitledBorder("Booking Summary"));
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        contentPanel.add(summaryScroll, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton checkAvailabilityButton = new JButton("Check Availability");
        JButton calculatePriceButton = new JButton("Calculate Price");
        JButton confirmBookingButton = new JButton("Confirm Booking");
        confirmBookingButton.setBackground(new Color(144, 238, 144));

        buttonPanel.add(checkAvailabilityButton);
        buttonPanel.add(calculatePriceButton);
        buttonPanel.add(confirmBookingButton);
        contentPanel.add(buttonPanel, gbc);

        // Action listeners
        refreshButton.addActionListener(e -> refreshPropertyComboBox(propertyComboBox));

        checkAvailabilityButton.addActionListener(e -> {
            String selectedName = (String) propertyComboBox.getSelectedItem();
            if (selectedName != null && manager != null) {
                Property prop = manager.findProperty(selectedName);
                if (prop != null) {
                    try {
                        int checkIn = Integer.parseInt(checkInField.getText().trim());
                        int checkOut = Integer.parseInt(checkOutField.getText().trim());

                        if (prop.areDatesAvailable(checkIn, checkOut)) {
                            summaryArea.setText("✓ Dates are AVAILABLE for booking!\n\n");
                            summaryArea.append("Property: " + prop.getName() + "\n");
                            summaryArea.append("Check-in: Day " + checkIn + "\n");
                            summaryArea.append("Check-out: Day " + checkOut + "\n");
                            summaryArea.append("Total Nights: " + (checkOut - checkIn) + "\n");
                        } else {
                            summaryArea.setText("✗ Dates are NOT AVAILABLE for booking.\n\n");
                            ArrayList<Integer> unavailable = prop.getUnavailableDays(checkIn, checkOut);
                            summaryArea.append("Unavailable days: " + unavailable + "\n");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Please enter valid check-in and check-out days!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        calculatePriceButton.addActionListener(e -> {
            String selectedName = (String) propertyComboBox.getSelectedItem();
            if (selectedName != null && manager != null) {
                Property prop = manager.findProperty(selectedName);
                if (prop != null) {
                    try {
                        int checkIn = Integer.parseInt(checkInField.getText().trim());
                        int checkOut = Integer.parseInt(checkOutField.getText().trim());

                        // Create temporary reservation to calculate price
                        Reservation tempReservation = new Reservation("TEMP", checkIn, checkOut);
                        tempReservation.calculateTotal(prop.getDates());

                        summaryArea.setText("=== PRICE CALCULATION ===\n\n");
                        summaryArea.append("Property: " + prop.getName() + "\n");
                        summaryArea.append("Property Type: " + prop.getPropertyType() + "\n");
                        summaryArea.append("Property Rate: PHP " + String.format("%.2f", prop.getPropertyRate()) + " per night\n");
                        summaryArea.append("Check-in: Day " + checkIn + "\n");
                        summaryArea.append("Check-out: Day " + checkOut + "\n");
                        summaryArea.append("Total Nights: " + (checkOut - checkIn) + "\n");
                        summaryArea.append("Total Price: PHP " + String.format("%.2f", tempReservation.getTotalPrice()) + "\n\n");

                        summaryArea.append("PRICE BREAKDOWN:\n");
                        ArrayList<Double> breakdown = tempReservation.getBreakdown();
                        for (int i = 0; i < breakdown.size(); i++) {
                            int day = checkIn + i;
                            Date date = prop.findDate(day);
                            double modifier = date != null ? date.getModifier() : 1.0;
                            summaryArea.append(String.format("  Day %2d: PHP %8.2f (Modifier: %.0f%%)%n",
                                    day, breakdown.get(i), modifier * 100));
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Please enter valid check-in and check-out days!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        confirmBookingButton.addActionListener(e -> {
            String selectedName = (String) propertyComboBox.getSelectedItem();
            String guestName = guestNameField.getText().trim();

            if (selectedName == null || guestName.isEmpty() || manager == null) {
                JOptionPane.showMessageDialog(this, "Please select a property and enter guest name!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Property prop = manager.findProperty(selectedName);
            if (prop != null) {
                try {
                    int checkIn = Integer.parseInt(checkInField.getText().trim());
                    int checkOut = Integer.parseInt(checkOutField.getText().trim());

                    if (!prop.areDatesAvailable(checkIn, checkOut)) {
                        JOptionPane.showMessageDialog(this, "Selected dates are not available!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Create and confirm reservation
                    Reservation reservation = new Reservation(guestName, checkIn, checkOut);
                    reservation.calculateTotal(prop.getDates());

                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Confirm booking for " + guestName + "?\n" +
                                    "Property: " + prop.getName() + "\n" +
                                    "Dates: Day " + checkIn + " to Day " + checkOut + "\n" +
                                    "Total Price: PHP " + String.format("%.2f", reservation.getTotalPrice()),
                            "Confirm Booking", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        prop.bookDates(checkIn, checkOut);
                        prop.addReservation(reservation);

                        JOptionPane.showMessageDialog(this,
                                "Booking confirmed successfully!\n\n" +
                                        "Guest: " + guestName + "\n" +
                                        "Property: " + prop.getName() + "\n" +
                                        "Dates: Day " + checkIn + " to Day " + checkOut + "\n" +
                                        "Total: PHP " + String.format("%.2f", reservation.getTotalPrice()),
                                "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

                        // Clear fields
                        guestNameField.setText("");
                        checkInField.setText("");
                        checkOutField.setText("");
                        summaryArea.setText("");
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid check-in and check-out days!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(backButton, BorderLayout.NORTH);
        panel.add(headerLabel, BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.SOUTH);

        // Initial refresh
        refreshPropertyComboBox(propertyComboBox);

        return panel;
    }

    /**
     * Refreshes the property combo box with current properties from the system manager.
     * Clears existing items and repopulates with all available property names.
     * Handles null manager cases gracefully.
     *
     * @param comboBox the JComboBox to refresh with current property names
     */
    private void refreshPropertyComboBox(JComboBox<String> comboBox) {
        if (manager == null) {
            System.err.println("SystemManager is null during refreshPropertyComboBox");
            return;
        }

        comboBox.removeAllItems();
        String[] propertyNames = manager.getPropertyNames();
        for (String name : propertyNames) {
            comboBox.addItem(name);
        }
    }

    /**
     * Inner class for handling menu button actions.
     * Provides navigation between different application panels based on button labels.
     */
    private class MenuButtonListener implements ActionListener {
        private String buttonLabel;

        /**
         * Constructs a new MenuButtonListener with the specified button label.
         *
         * @param label the label of the button this listener is associated with
         */
        public MenuButtonListener(String label) {
            this.buttonLabel = label;
        }

        /**
         * Handles button click events and navigates to the appropriate panel.
         * For exit button, shows confirmation dialog before terminating the application.
         *
         * @param e the ActionEvent triggered by button click
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (buttonLabel) {
                case "Create Property":
                    cardLayout.show(mainPanel, CREATE_PROPERTY);
                    break;
                case "View Property":
                    cardLayout.show(mainPanel, CALENDAR_VIEW);
                    break;
                case "Manage Property":
                    cardLayout.show(mainPanel, MANAGE_PROPERTY);
                    break;
                case "Simulate Booking":
                    cardLayout.show(mainPanel, SIMULATE_BOOKING);
                    break;
                case "Exit System":
                    int confirm = JOptionPane.showConfirmDialog(MainFrame.this,
                            "Are you sure you want to exit?", "Confirm Exit",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                    break;
            }
        }
    }

    /**
     * Main method to launch the Green Property Exchange GUI application.
     * Sets the system look and feel and initializes the application on the Event Dispatch Thread.
     *
     * @param args command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame().setVisible(true);
            } catch (Exception e) {
                System.err.println("Failed to create MainFrame: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}