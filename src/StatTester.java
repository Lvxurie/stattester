import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class StatTester extends JFrame {
    private List<Double> baseStats = new ArrayList<>(); // Base Stats (HP, ATTACK, DEFENSE, SPECIAL, SPEED)
    private List<Double> baseGrowths = new ArrayList<>(); // Base Growths corresponding to each stat
    private List<Double> calculatedStats = new ArrayList<>(); // Calculated stats after applying growth rates
    private List<String> phaseRates = new ArrayList<>(); // Selected growth rates for each phase

    // Input fields for base stats and base growths
    private List<JTextField> baseStatInputFields = new ArrayList<>();
    private List<JTextField> baseGrowthInputFields = new ArrayList<>();
    
    // Display fields for calculated stats
    private List<JTextField> statFields = new ArrayList<>();
    
    // ComboBoxes for selecting growth rates for each phase
    private List<JComboBox<String>> statGrowthComboboxes = new ArrayList<>();

    // ComboBox for selecting the XP Rate
    private JComboBox<String> xpGrowthComboBox;

    public StatTester() {
        super("Stat Tester");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding around components

        /* -------------------------------------------------------------------------------------------------*/

        // Input Panel Setup
        JPanel inputPanel = new JPanel(new GridLayout(6, 6, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "Stat Inputs"));

        // Row 1 - Headers
        inputPanel.add(new JLabel()); // Empty for alignment
        inputPanel.add(createHeaderLabel("Base Stat"));
        inputPanel.add(createHeaderLabel("Base Growth"));
        inputPanel.add(createHeaderLabel("Phases"));
        inputPanel.add(createHeaderLabel("Growth Rate"));
        inputPanel.add(new JLabel()); // Empty for alignment

        // Define growth rate options
        String[] growthOptions = {"Very slow", "Slow", "Neutral", "Fast", "Very fast"};

        // Row 2 - HP
        inputPanel.add(createStatLabel("HP"));
        JTextField hpBaseStat = createTextField("100"); // Example initial value
        inputPanel.add(hpBaseStat);
        baseStatInputFields.add(hpBaseStat);

        JTextField hpBaseGrowth = createTextField("10"); // Example initial value
        inputPanel.add(hpBaseGrowth);
        baseGrowthInputFields.add(hpBaseGrowth);

        inputPanel.add(createPhaseLabel("Phase 1"));
        JComboBox<String> phase1Combo = createComboBox(growthOptions, "Neutral");
        inputPanel.add(phase1Combo);
        statGrowthComboboxes.add(phase1Combo);

        inputPanel.add(new JLabel()); // Empty for alignment

        // Row 3 - ATTACK
        inputPanel.add(createStatLabel("ATTACK"));
        JTextField attackBaseStat = createTextField("100"); // Example initial value
        inputPanel.add(attackBaseStat);
        baseStatInputFields.add(attackBaseStat);

        JTextField attackBaseGrowth = createTextField("10"); // Example initial value
        inputPanel.add(attackBaseGrowth);
        baseGrowthInputFields.add(attackBaseGrowth);

        inputPanel.add(createPhaseLabel("Phase 2"));
        JComboBox<String> phase2Combo = createComboBox(growthOptions, "Neutral");
        inputPanel.add(phase2Combo);
        statGrowthComboboxes.add(phase2Combo);

        inputPanel.add(new JLabel()); // Empty for alignment

        // Row 4 - DEFENSE
        inputPanel.add(createStatLabel("DEFENSE"));
        JTextField defenseBaseStat = createTextField("100"); // Example initial value
        inputPanel.add(defenseBaseStat);
        baseStatInputFields.add(defenseBaseStat);

        JTextField defenseBaseGrowth = createTextField("10"); // Example initial value
        inputPanel.add(defenseBaseGrowth);
        baseGrowthInputFields.add(defenseBaseGrowth);

        inputPanel.add(createPhaseLabel("Phase 3"));
        JComboBox<String> phase3Combo = createComboBox(growthOptions, "Neutral");
        inputPanel.add(phase3Combo);
        statGrowthComboboxes.add(phase3Combo);

        inputPanel.add(new JLabel()); // Empty for alignment

        // Row 5 - SPECIAL
        inputPanel.add(createStatLabel("SPECIAL"));
        JTextField specialBaseStat = createTextField("100"); // Example initial value
        inputPanel.add(specialBaseStat);
        baseStatInputFields.add(specialBaseStat);

        JTextField specialBaseGrowth = createTextField("10"); // Example initial value
        inputPanel.add(specialBaseGrowth);
        baseGrowthInputFields.add(specialBaseGrowth);

        inputPanel.add(createPhaseLabel("Phase 4"));
        JComboBox<String> phase4Combo = createComboBox(growthOptions, "Neutral");
        inputPanel.add(phase4Combo);
        statGrowthComboboxes.add(phase4Combo);

        inputPanel.add(new JLabel()); // Empty for alignment

        // Row 6 - SPEED
        inputPanel.add(createStatLabel("SPEED"));
        JTextField speedBaseStat = createTextField("100"); // Example initial value
        inputPanel.add(speedBaseStat);
        baseStatInputFields.add(speedBaseStat);

        JTextField speedBaseGrowth = createTextField("10"); // Example initial value
        inputPanel.add(speedBaseGrowth);
        baseGrowthInputFields.add(speedBaseGrowth);

        inputPanel.add(new JLabel()); // Empty label replacing Phase 4 label for SPEED
        inputPanel.add(new JLabel()); // Empty label replacing Phase 4 ComboBox for SPEED

        /* -------------------------------------------------------------------------------------------------*/

        // Button Panel with Calculate Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton calculate = new JButton("Calculate");
        calculate.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(calculate);

        // ActionListener for Calculate Button
        calculate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performCalculation();
            }
        });

        // Add Input Panel to Frame
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.6; // Allocate more space for input panel
        gbc.fill = GridBagConstraints.BOTH;
        add(inputPanel, gbc);

        /* -------------------------------------------------------------------------------------------------*/

        // XP Rate Panel
        JPanel xpRatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        xpRatePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "XP Rate"));

        JLabel xpRateLabel = new JLabel("XP Rate:");
        xpRatePanel.add(xpRateLabel);

        String[] xpOptions = {"Very slow", "Slow", "Neutral", "Fast", "Very fast"};
        xpGrowthComboBox = new JComboBox<>(xpOptions);
        xpGrowthComboBox.setSelectedItem("Neutral");
        xpRatePanel.add(xpGrowthComboBox);

        // Add XP Rate Panel to Frame
        gbc.gridy = 1;
        gbc.weighty = 0.1; // Allocate space for XP Rate panel
        add(xpRatePanel, gbc);

        /* -------------------------------------------------------------------------------------------------*/

        // Add Button Panel to Frame
        gbc.gridy = 2;
        gbc.weighty = 0.1; // Allocate space for button panel
        add(buttonPanel, gbc);

        /* -------------------------------------------------------------------------------------------------*/

        // Display Panel for Calculated Stats
        JPanel displayPanel = new JPanel(new GridLayout(6, 6, 5, 5));
        displayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "Calculated Stats"));

        // Row 1 - Level Headers
        displayPanel.add(createHeaderLabel("Level"));
        displayPanel.add(createHeaderLabel("1"));
        displayPanel.add(createHeaderLabel("30"));
        displayPanel.add(createHeaderLabel("70"));
        displayPanel.add(createHeaderLabel("110"));
        displayPanel.add(createHeaderLabel("150")); // Reintroduced Level 150

        // Rows 2-6 - Stats (HP, ATTACK, DEFENSE, SPECIAL, SPEED)
        String[] stats = {"HP", "ATTACK", "DEFENSE", "SPECIAL", "SPEED"};
        for (String stat : stats) {
            displayPanel.add(createStatLabel(stat));

            // Add JTextFields for each level
            for (int i = 0; i < 5; i++) { // 5 test levels
                JTextField field = new JTextField("0");
                field.setEditable(false);
                field.setHorizontalAlignment(JTextField.CENTER);
                displayPanel.add(field);
                statFields.add(field);
            }
        }

        // Add Display Panel to Frame
        gbc.gridy = 3;
        gbc.weighty = 0.3; // Allocate space for the display panel
        add(displayPanel, gbc);

        /* -------------------------------------------------------------------------------------------------*/

        // Finalize Frame
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);

        // Attach ItemListeners to ComboBoxes for automatic recalculations
        addPhaseComboBoxListeners();
    }

    /**
     * Creates a header JLabel with centered text.
     */
    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    /**
     * Creates a stat JLabel with centered text.
     */
    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    /**
     * Creates a phase JLabel with centered text.
     */
    private JLabel createPhaseLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    /**
     * Creates a JTextField with specified initial text.
     */
    private JTextField createTextField(String initialText) {
        JTextField textField = new JTextField(initialText);
        textField.setHorizontalAlignment(JTextField.CENTER);
        return textField;
    }

    /**
     * Creates a JComboBox with specified options and sets the default selected item.
     */
    private JComboBox<String> createComboBox(String[] options, String defaultSelection) {
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setSelectedItem(defaultSelection);
        return comboBox;
    }

    /**
     * Adds ItemListeners to all phase ComboBoxes and the XP Rate ComboBox to trigger recalculations
     * whenever a selection changes.
     */
    private void addPhaseComboBoxListeners() {
        // Add listeners to all phase ComboBoxes
        for (JComboBox<String> comboBox : statGrowthComboboxes) {
            comboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    // Only respond to selection events, not deselection
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        performCalculation(); // Automatically perform calculation on selection change
                    }
                }
            });
        }

        // Add listener to the XP Rate ComboBox as well
        xpGrowthComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Only respond to selection events, not deselection
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    performCalculation(); // Automatically perform calculation on XP Rate change
                }
            }
        });
    }

    /**
     * Encapsulates the calculation and display update logic.
     */
    private void performCalculation() {
        clearStatFields(); // Reset display fields
        setBaseStats(); // Retrieve base stats from input fields
        setBaseGrowths(); // Retrieve base growths from input fields
        String xpRate = getXPRate(); // Get selected XP Rate
        setPhaseRates(); // Retrieve selected phase growth rates
        calculatedStats = CalculateStats.calculate(baseStats, baseGrowths, phaseRates, xpRate); // Perform calculations
        fillStatsTable(calculatedStats); // Update display fields with calculated stats
    }

    /**
     * Retrieves and sets the base stats from the input fields.
     */
    private void setBaseStats() {
        baseStats.clear();
        try {
            for (JTextField field : baseStatInputFields) {
                double value = Double.parseDouble(field.getText());
                if (value < 0) throw new NumberFormatException("Negative value");
                baseStats.add(value);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid non-negative numeric values for base stats.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retrieves and sets the base growths from the input fields.
     */
    private void setBaseGrowths() {
        baseGrowths.clear();
        try {
            for (JTextField field : baseGrowthInputFields) {
                double value = Double.parseDouble(field.getText());
                if (value < 0) throw new NumberFormatException("Negative value");
                baseGrowths.add(value);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid non-negative numeric values for base growths.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retrieves and sets the phase growth rates from the ComboBoxes.
     */
    private void setPhaseRates() {
        phaseRates.clear();
        for (JComboBox<String> phase : statGrowthComboboxes) {
            phaseRates.add(phase.getSelectedItem().toString());
        }
    }

    /**
     * Retrieves the selected XP Rate.
     *
     * @return The selected XP Rate as a String.
     */
    private String getXPRate() {
        return (String) xpGrowthComboBox.getSelectedItem();
    }

    /**
     * Populates the display table with the calculated stats.
     *
     * @param stats The list of calculated stats.
     */
    private void fillStatsTable(List<Double> stats) {
        if (stats.size() != statFields.size()) {
            JOptionPane.showMessageDialog(this, "Mismatch between calculated stats and display fields.", "Calculation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int numLevels = 5; // Levels: 1, 30, 70, 110, 150
        int numStats = 5; // HP, ATTACK, DEFENSE, SPECIAL, SPEED

        // The statFields list is ordered by stat first, then by level.
        // Iterate through each stat and then each level.
        for (int statIndex = 0; statIndex < numStats; statIndex++) {
            for (int levelIndex = 0; levelIndex < numLevels; levelIndex++) {
                int dataIndex = (statIndex * numLevels) + levelIndex;
                int fieldIndex = (statIndex * numLevels) + levelIndex;

                double statValue = stats.get(dataIndex);
                int intStatValue = (int) Math.round(statValue); // Round to nearest integer
                String formattedValue = String.valueOf(intStatValue); // Convert to String without decimals
                statFields.get(fieldIndex).setText(formattedValue);
            }
        }
    }

    /**
     * Clears all the display fields by resetting them to "0".
     */
    private void clearStatFields() {
        for (JTextField field : statFields) {
            field.setText("0");
        }
    }

    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StatTester();
            }
        });
    }
}