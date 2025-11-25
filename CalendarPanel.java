/**
 * CalendarPanel.java
 *
 * Enhanced calendar view with environmental impact pricing modifiers for the Green Property Exchange system.
 * Displays color-coded dates based on environmental modifiers and provides interactive date management.
 * Users can view property availability, modify environmental modifiers, and manage dates through mouse interactions.
 *
 * MCO2 - Green Property Exchange
 * @author Group 23
 * @version 3.1
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

    // Colors for environmental modifiers
    private final Color GREEN_COLOR = new Color(144, 238, 144);    // 80-89%
    private final Color WHITE_COLOR = Color.WHITE;                 // 100%
    private final Color YELLOW_COLOR = new Color(255, 255, 153);   // 101-120%
    private final Color BOOKED_COLOR = new Color(255, 200, 200);   // Booked dates

    /**
     * Constructs a new CalendarPanel with default initialization.
     * Sets up the calendar layout, legend, and interactive components.
     */
    public CalendarPanel() {
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
        infoLabel = new JLabel("Select a property to view calendar", JLabel.CENTER);
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
    }

    /**
     * Creates and returns the legend panel explaining the color coding system.
     * The legend shows color representations for different environmental impact levels
     * and booking status.
     *
     * @return JPanel containing the complete legend with color codes and descriptions
     */
    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        legendPanel.setBorder(BorderFactory.createTitledBorder("Legend"));
        legendPanel.setPreferredSize(new Dimension(180, 200));

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
     * Updates the calendar display to reflect the current property's state.
     * Colors each date cell based on availability, booking status, and environmental modifiers.
     * Sets tooltips with detailed pricing information for each date.
     */
    private void updateCalendarDisplay() {
        if (currentProperty == null) {
            infoLabel.setText("No property selected");
            return;
        }

        // Show both base price and property rate
        infoLabel.setText("Property: " + currentProperty.getName() +
                " | Type: " + currentProperty.getPropertyType() +
                " | Base Price: PHP " + String.format("%.2f", currentProperty.getBasePrice()) +
                " | Property Rate: PHP " + String.format("%.2f", currentProperty.getPropertyRate()));

        // Reset all dates
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 7; col++) {
                int dayNumber = row * 7 + col + 1;
                JLabel dateLabel = dateLabels[row][col];

                if (dayNumber > 30) {
                    dateLabel.setBackground(Color.LIGHT_GRAY);
                    dateLabel.setToolTipText("Day not in calendar");
                    continue;
                }

                Date date = currentProperty.findDate(dayNumber);

                if (date == null) {
                    // Date not listed in property
                    dateLabel.setBackground(Color.LIGHT_GRAY);
                    dateLabel.setToolTipText("Day " + dayNumber + ": Not available");
                } else if (date.isBooked()) {
                    // Booked date
                    dateLabel.setBackground(BOOKED_COLOR);
                    dateLabel.setToolTipText("Day " + dayNumber + ": BOOKED - PHP " +
                            String.format("%.2f", date.getFinalPrice()) +
                            " (Property Rate: PHP " + String.format("%.2f", date.getBasePrice()) +
                            " × " + String.format("%.0f", date.getModifier() * 100) + "%)");
                } else {
                    // Available date - color code by environmental modifier
                    double modifier = date.getModifier();
                    if (modifier < 0.9) {
                        dateLabel.setBackground(GREEN_COLOR);  // 80-89%
                    } else if (modifier == 1.0) {
                        dateLabel.setBackground(WHITE_COLOR);  // 100%
                    } else {
                        dateLabel.setBackground(YELLOW_COLOR); // 101-120%
                    }

                    dateLabel.setToolTipText("Day " + dayNumber +
                            ": Available - PHP " + String.format("%.2f", date.getFinalPrice()) +
                            " (Property Rate: PHP " + String.format("%.2f", date.getBasePrice()) +
                            " × " + String.format("%.0f", modifier * 100) + "%)");
                }
            }
        }

        repaint();
    }

    /**
     * Handles mouse click events on calendar dates.
     * Provides different functionality based on the date's current state:
     * - Unlisted dates: Option to add to property availability
     * - Available dates: Show details and allow environmental modifier modification
     * - Booked dates: Show reservation information
     *
     * @param dayNumber the day number that was clicked (1-30)
     */
    private void handleDateClick(int dayNumber) {
        if (currentProperty == null) return;

        Date date = currentProperty.findDate(dayNumber);

        if (date == null) {
            // Option to add this date
            int option = JOptionPane.showConfirmDialog(this,
                    "Add day " + dayNumber + " to property availability?",
                    "Add Date", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                currentProperty.addDate(dayNumber);
                updateCalendarDisplay();
                JOptionPane.showMessageDialog(this,
                        "Day " + dayNumber + " added successfully!\n" +
                                "Property Rate: PHP " + String.format("%.2f", currentProperty.getPropertyRate()) +
                                "\nFinal Price: PHP " + String.format("%.2f", currentProperty.findDate(dayNumber).getFinalPrice()),
                        "Date Added", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (!date.isBooked()) {
            // Show date details and allow modifier change
            String message = "Day " + dayNumber + " Details:\n" +
                    "Property Rate: PHP " + String.format("%.2f", date.getBasePrice()) + "\n" +
                    "Environmental Modifier: " + String.format("%.0f", date.getModifier() * 100) + "%\n" +
                    "Final Price: PHP " + String.format("%.2f", date.getFinalPrice()) + "\n" +
                    "Status: Available";

            String newModifier = JOptionPane.showInputDialog(this,
                    message + "\n\nEnter new environmental modifier (0.8 - 1.2):",
                    String.valueOf(date.getModifier()));

            if (newModifier != null) {
                try {
                    double modifier = Double.parseDouble(newModifier);
                    if (modifier >= 0.8 && modifier <= 1.2) {
                        currentProperty.setEnvironmentalModifier(dayNumber, modifier);
                        updateCalendarDisplay();
                        Date updatedDate = currentProperty.findDate(dayNumber);
                        JOptionPane.showMessageDialog(this,
                                "Environmental modifier updated!\n" +
                                        "New Final Price: PHP " + String.format("%.2f", updatedDate.getFinalPrice()),
                                "Modifier Updated", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Modifier must be between 0.8 and 1.2",
                                "Invalid Modifier", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter a valid number",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            // Booked date - show reservation info
            JOptionPane.showMessageDialog(this,
                    "Day " + dayNumber + " is BOOKED\n" +
                            "Property Rate: PHP " + String.format("%.2f", date.getBasePrice()) + "\n" +
                            "Environmental Modifier: " + String.format("%.0f", date.getModifier() * 100) + "%\n" +
                            "Final Price: PHP " + String.format("%.2f", date.getFinalPrice()),
                    "Booked Date", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}