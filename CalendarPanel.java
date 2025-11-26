// CalendarPanel.java
/**
 * CalendarPanel.java
 *
 * Enhanced calendar view with environmental impact pricing modifiers for the Green Property Exchange system.
 * Displays color-coded dates based on environmental modifiers and provides interactive date management.
 * Environmental impacts are always visible regardless of property selection.
 *
 * MCO2 - Green Property Exchange
 * @author Group 23
 * @version 4.3
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CalendarPanel extends JPanel {
    private Property currentProperty;
    private Date[][] calendarGrid;
    private JLabel[][] dateLabels;
    private JPanel calendarPanel;
    private JLabel infoLabel;
    private EnvironmentalImpactManager environmentalImpactManager;

    // Colors for environmental modifiers
    private final Color GREEN_COLOR = new Color(144, 238, 144);    // 80-89%
    private final Color WHITE_COLOR = Color.WHITE;                 // 100%
    private final Color YELLOW_COLOR = new Color(255, 255, 153);   // 101-120%
    private final Color BOOKED_COLOR = new Color(255, 200, 200);   // Booked dates
    private final Color IMPACT_ONLY_COLOR = new Color(200, 230, 255); // Environmental impact only (no property)

    /**
     * Constructs a new CalendarPanel with default initialization.
     * Sets up the calendar layout, legend, and interactive components.
     */
    public CalendarPanel() {
        this.environmentalImpactManager = new EnvironmentalImpactManager();
        setLayout(new BorderLayout());
        initializeCalendar();
    }

    /**
     * Sets the current property to display in the calendar.
     * Updates the calendar display to reflect the property's dates and reservations.
     *
     * @param property the property to display, or null to clear the calendar
     */
    public void setProperty(Property property) {
        this.currentProperty = property;
        updateCalendarDisplay();
    }

    /**
     * Initializes the calendar components including the grid layout, legend panel,
     * header with day names, and interactive date cells.
     * Sets up mouse listeners for date interactions.
     */
    private void initializeCalendar() {
        // Create main container with legend on left and calendar on right
        JPanel mainContainer = new JPanel(new BorderLayout());

        // Legend panel
        JPanel legendPanel = createLegendPanel();

        // Calendar container (header + grid)
        JPanel calendarContainer = new JPanel(new BorderLayout());

        // Header with day names - add empty border for alignment with legend
        String[] dayNames = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        JPanel headerPanel = new JPanel(new GridLayout(1, 7));
        headerPanel.setBackground(new Color(220, 220, 220));

        // Add left margin to header to align with calendar grid under legend
        int legendWidth = 180; // Match the legend panel width
        int cellWidth = 60;    // Match calendar cell width
        int leftMargin = legendWidth - (3 * cellWidth); // Calculate margin needed

        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, leftMargin, 0, 0));

        for (String dayName : dayNames) {
            JLabel dayLabel = new JLabel(dayName, JLabel.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
            dayLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            headerPanel.add(dayLabel);
        }

        // Calendar grid
        calendarPanel = new JPanel(new GridLayout(5, 7, 2, 2));
        calendarPanel.setBackground(Color.DARK_GRAY);
        calendarGrid = new Date[5][7];
        dateLabels = new JLabel[5][7];

        // Initialize calendar cells
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 7; col++) {
                JLabel dateLabel = new JLabel("", JLabel.CENTER);
                dateLabel.setOpaque(true);
                dateLabel.setBackground(Color.WHITE);
                dateLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                dateLabel.setPreferredSize(new Dimension(60, 60));

                final int dayNumber = row * 7 + col + 1;
                if (dayNumber <= 30) {
                    dateLabel.setText(String.valueOf(dayNumber));

                    // Add mouse listener for interactions
                    dateLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            handleDateClick(dayNumber);
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            dateLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            dateLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        }
                    });
                } else {
                    dateLabel.setBackground(Color.LIGHT_GRAY);
                    dateLabel.setEnabled(false);
                }

                calendarPanel.add(dateLabel);
                dateLabels[row][col] = dateLabel;
            }
        }

        // Info panel
        infoLabel = new JLabel("Environmental impacts are always visible. Select a property to view availability.", JLabel.CENTER);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Add components to calendar container
        calendarContainer.add(headerPanel, BorderLayout.NORTH);
        calendarContainer.add(calendarPanel, BorderLayout.CENTER);
        calendarContainer.add(infoLabel, BorderLayout.SOUTH);

        // Add legend and calendar to main container
        mainContainer.add(legendPanel, BorderLayout.WEST);
        mainContainer.add(calendarContainer, BorderLayout.CENTER);

        // Add main container to this panel
        add(mainContainer, BorderLayout.CENTER);
        
        // Initial display of environmental impacts
        updateCalendarDisplay();
    }

    /**
     * Creates and returns the legend panel explaining the color coding system.
     * The legend shows color representations for different environmental impact levels
     * and booking status.
     *
     * @return JPanel containing the complete legend with color codes and descriptions
     */
    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        legendPanel.setBorder(BorderFactory.createTitledBorder("Legend"));
        legendPanel.setPreferredSize(new Dimension(180, 240));

        // Environmental Impact Only (no property)
        JPanel impactOnlyLegend = createLegendItem(IMPACT_ONLY_COLOR, "Impact Only", "Environmental Impact (No Property)");

        // Available - Green modifier
        JPanel greenLegend = createLegendItem(GREEN_COLOR, "Green: 80-89%", "Reduced Impact");

        // Available - Normal modifier
        JPanel whiteLegend = createLegendItem(WHITE_COLOR, "White: 100%", "Standard Impact");

        // Available - Yellow modifier
        JPanel yellowLegend = createLegendItem(YELLOW_COLOR, "Yellow: 101-120%", "Increased Impact");

        // Booked
        JPanel bookedLegend = createLegendItem(BOOKED_COLOR, "Booked", "Reserved Date");

        // Empty
        JPanel emptyLegend = createLegendItem(Color.LIGHT_GRAY, "Not Listed", "Not Available");

        legendPanel.add(impactOnlyLegend);
        legendPanel.add(greenLegend);
        legendPanel.add(whiteLegend);
        legendPanel.add(yellowLegend);
        legendPanel.add(bookedLegend);
        legendPanel.add(emptyLegend);

        return legendPanel;
    }

    /**
     * Creates an individual legend item with color box and descriptive text.
     *
     * @param color the background color for the legend item
     * @param title the main title text for the legend item
     * @param description the detailed description text
     * @return JPanel containing the complete legend item
     */
    private JPanel createLegendItem(Color color, String title, String description) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        itemPanel.setBackground(Color.WHITE);

        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 10));

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 8));

        textPanel.add(titleLabel);
        textPanel.add(descLabel);

        itemPanel.add(colorBox);
        itemPanel.add(textPanel);

        return itemPanel;
    }

    /**
     * Updates the calendar display to reflect environmental impacts and property state.
     * Environmental impacts are always visible regardless of property selection.
     * Colors each date cell based on environmental modifiers, availability, and booking status.
     * Sets tooltips with detailed information for each date.
     */
    private void updateCalendarDisplay() {
        // Update info label based on property selection
        if (currentProperty == null) {
            infoLabel.setText("Environmental impacts are always visible. Select a property to view availability.");
        } else {
            infoLabel.setText("Property: " + currentProperty.getName() +
                    " | Type: " + currentProperty.getPropertyType() +
                    " | Base Price: PHP " + String.format("%.2f", currentProperty.getBasePrice()) +
                    " | Property Rate: PHP " + String.format("%.2f", currentProperty.getPropertyRate()));
        }

        // Update all dates - environmental impacts are always visible
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 7; col++) {
                int dayNumber = row * 7 + col + 1;
                JLabel dateLabel = dateLabels[row][col];

                if (dayNumber > 30) {
                    dateLabel.setBackground(Color.LIGHT_GRAY);
                    dateLabel.setToolTipText("Day not in calendar");
                    continue;
                }

                // Check for environmental impact first (always visible)
                EnvironmentalImpact impact = environmentalImpactManager.getImpact(dayNumber);
                
                if (currentProperty == null) {
                    // No property selected - show only environmental impacts
                    if (impact != null) {
                        // Date has environmental impact
                        double modifier = impact.getModifier();
                        if (modifier < 0.9) {
                            dateLabel.setBackground(GREEN_COLOR);
                        } else if (modifier <= 1.0) {
                            dateLabel.setBackground(WHITE_COLOR);
                        } else {
                            dateLabel.setBackground(YELLOW_COLOR);
                        }
                        dateLabel.setToolTipText("Day " + dayNumber + ": " + impact.getName() + 
                                " (" + String.format("%.0f", impact.getModifier() * 100) + "%)" +
                                " - No property selected");
                    } else {
                        // No environmental impact and no property
                        dateLabel.setBackground(IMPACT_ONLY_COLOR);
                        dateLabel.setToolTipText("Day " + dayNumber + ": No environmental impact - No property selected");
                    }
                } else {
                    // Property is selected - show property status with environmental impacts
                    Date date = currentProperty.findDate(dayNumber);

                    if (date == null) {
                        // Date not listed in property, but may have environmental impact
                        if (impact != null) {
                            dateLabel.setBackground(IMPACT_ONLY_COLOR);
                            dateLabel.setToolTipText("Day " + dayNumber + ": " + impact.getName() + 
                                    " (" + String.format("%.0f", impact.getModifier() * 100) + "%)" +
                                    " - Not available in " + currentProperty.getName());
                        } else {
                            dateLabel.setBackground(Color.LIGHT_GRAY);
                            dateLabel.setToolTipText("Day " + dayNumber + ": Not available in " + currentProperty.getName());
                        }
                    } else if (date.isBooked()) {
                        // Booked date
                        dateLabel.setBackground(BOOKED_COLOR);
                        dateLabel.setToolTipText("Day " + dayNumber + ": BOOKED - PHP " +
                                String.format("%.2f", date.getFinalPrice()) +
                                " (Property Rate: PHP " + String.format("%.2f", date.getBasePrice()) +
                                " × " + String.format("%.0f", date.getModifier() * 100) + "%)" +
                                " - " + date.getEnvironmentalImpactName());
                    } else {
                        // Available date - color code by environmental modifier
                        double modifier = date.getModifier();
                        if (modifier < 0.9) {
                            dateLabel.setBackground(GREEN_COLOR);
                        } else if (modifier <= 1.0) {
                            dateLabel.setBackground(WHITE_COLOR);
                        } else {
                            dateLabel.setBackground(YELLOW_COLOR);
                        }

                        dateLabel.setToolTipText("Day " + dayNumber +
                                ": Available - PHP " + String.format("%.2f", date.getFinalPrice()) +
                                " (Property Rate: PHP " + String.format("%.2f", date.getBasePrice()) +
                                " × " + String.format("%.0f", modifier * 100) + "%)" +
                                " - " + date.getEnvironmentalImpactName());
                    }
                }
            }
        }

        repaint();
    }

    /**
     * Handles mouse click events on calendar dates.
     * Shows environmental impact information and allows modification when property is selected.
     * Environmental impacts are always visible regardless of property selection.
     *
     * @param dayNumber the day number that was clicked (1-30)
     */
    private void handleDateClick(int dayNumber) {
        // Always show environmental impact information
        EnvironmentalImpact impact = environmentalImpactManager.getImpact(dayNumber);
        
        if (currentProperty == null) {
            // No property selected - show environmental impact info only
            if (impact != null) {
                JOptionPane.showMessageDialog(this,
                        "Environmental Impact - Day " + dayNumber + "\n" +
                        "Impact: " + impact.getName() + "\n" +
                        "Modifier: " + String.format("%.0f", impact.getModifier() * 100) + "%\n" +
                        "Status: No property selected\n\n" +
                        "Select a property to view availability and modify impacts.",
                        "Environmental Impact",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Day " + dayNumber + "\n" +
                        "No environmental impact set\n" +
                        "Standard modifier: 100%\n" +
                        "Status: No property selected\n\n" +
                        "Select a property to view availability.",
                        "No Environmental Impact",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Property is selected - show property-specific information and allow modification
            Date date = currentProperty.findDate(dayNumber);
            
            if (date == null) {
                // Date not available in property
                if (impact != null) {
                    JOptionPane.showMessageDialog(this,
                            "Day " + dayNumber + " - Not Available\n" +
                            "Environmental Impact: " + impact.getName() + "\n" +
                            "Modifier: " + String.format("%.0f", impact.getModifier() * 100) + "%\n" +
                            "Property: " + currentProperty.getName() + "\n\n" +
                            "This date is not available in the selected property.\n" +
                            "Use 'Manage Property' to add dates.",
                            "Date Not Available",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Day " + dayNumber + " - Not Available\n" +
                            "No environmental impact set\n" +
                            "Standard modifier: 100%\n" +
                            "Property: " + currentProperty.getName() + "\n\n" +
                            "This date is not available in the selected property.\n" +
                            "Use 'Manage Property' to add dates.",
                            "Date Not Available",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (!date.isBooked()) {
                // Available date - show details and allow environmental impact modification
                showDateDetailsAndModifyImpact(dayNumber, date);
            } else {
                // Booked date - show reservation info
                showBookedDateInfo(dayNumber, date);
            }
        }
    }

    /**
     * Shows detailed information about an available date and allows environmental impact modification.
     *
     * @param dayNumber the day number being viewed
     * @param date the date object containing the details
     */
    private void showDateDetailsAndModifyImpact(int dayNumber, Date date) {
        String message = "Day " + dayNumber + " Details:\n" +
                "Property: " + currentProperty.getName() + "\n" +
                "Property Rate: PHP " + String.format("%.2f", date.getBasePrice()) + "\n" +
                "Environmental Impact: " + date.getEnvironmentalImpactName() + "\n" +
                "Environmental Modifier: " + String.format("%.0f", date.getModifier() * 100) + "%\n" +
                "Final Price: PHP " + String.format("%.2f", date.getFinalPrice()) + "\n" +
                "Status: Available";

        // Create custom panel for modifier and name input
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Environmental Impact Name:"));
        JTextField nameField = new JTextField(date.getEnvironmentalImpactName());
        panel.add(nameField);
        panel.add(new JLabel("Modifier (0.8 - 1.2):"));
        JTextField modifierField = new JTextField(String.valueOf(date.getModifier()));
        panel.add(modifierField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Update Environmental Impact - Day " + dayNumber,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double modifier = Double.parseDouble(modifierField.getText());
                String impactName = nameField.getText().trim();
                
                if (impactName.isEmpty()) {
                    impactName = "Custom Impact";
                }
                
                if (modifier >= 0.8 && modifier <= 1.2) {
                    currentProperty.setEnvironmentalModifier(dayNumber, modifier, impactName);
                    updateCalendarDisplay();
                    Date updatedDate = currentProperty.findDate(dayNumber);
                    JOptionPane.showMessageDialog(this,
                            "Environmental impact updated successfully!\n" +
                                    "Impact: " + impactName + "\n" +
                                    "Modifier: " + String.format("%.0f", modifier * 100) + "%\n" +
                                    "New Final Price: PHP " + String.format("%.2f", updatedDate.getFinalPrice()),
                            "Impact Updated",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Modifier must be between 0.8 and 1.2",
                            "Invalid Modifier",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid number for modifier",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Shows information about a booked date.
     *
     * @param dayNumber the day number being viewed
     * @param date the date object containing the details
     */
    private void showBookedDateInfo(int dayNumber, Date date) {
        // Find which reservation this date belongs to
        String reservationInfo = "No reservation details found";
        for (Reservation reservation : currentProperty.getReservations()) {
            if (dayNumber >= reservation.getCheckIn() && dayNumber < reservation.getCheckOut()) {
                reservationInfo = "Guest: " + reservation.getGuestName() + 
                                "\nCheck-in: Day " + reservation.getCheckIn() + 
                                "\nCheck-out: Day " + reservation.getCheckOut();
                break;
            }
        }

        JOptionPane.showMessageDialog(this,
                "Day " + dayNumber + " is BOOKED\n\n" +
                        "Property: " + currentProperty.getName() + "\n" +
                        "Property Rate: PHP " + String.format("%.2f", date.getBasePrice()) + "\n" +
                        "Environmental Impact: " + date.getEnvironmentalImpactName() + "\n" +
                        "Environmental Modifier: " + String.format("%.0f", date.getModifier() * 100) + "%\n" +
                        "Final Price: PHP " + String.format("%.2f", date.getFinalPrice()) + "\n\n" +
                        reservationInfo,
                "Booked Date - Day " + dayNumber,
                JOptionPane.INFORMATION_MESSAGE);
    }



}