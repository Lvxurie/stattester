import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList; // Import for ArrayList
import java.util.List;      // Import for List
import java.util.Map;       // Import for Map

public class StatTester extends JFrame {

    private List<JTextField> baseStatFields = new ArrayList<>();
    private List<JTextField> baseGrowthFields = new ArrayList<>();
    private List<JComboBox<String>> phaseGrowthCombos = new ArrayList<>();
    private JComboBox<String> xpCurveCombo;
    private List<Double> baseStats = new ArrayList<>();
    private List<Double> baseGrowths = new ArrayList<>();
    private List<String> phases = new ArrayList<>();
    private CalculateStats.XP_Curve selectedCurve;
    private List<JTextField> statFields = new ArrayList<>();

    public StatTester() {
        setTitle("Stat Tester");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 500); // Reduced window size
        setLayout(new BorderLayout(10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Base Stats Panel
        JPanel baseStatsPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        baseStatsPanel.setBorder(BorderFactory.createTitledBorder("Base Stats"));
        String[] stats = {"HP", "Attack", "Defense", "Special", "Speed"};
        for (String stat : stats) {
            baseStatsPanel.add(new JLabel(stat + ":"));
            JTextField statField = new JTextField("100"); // Default value
            baseStatFields.add(statField);
            baseStatsPanel.add(statField);
        }

        // Base Growths Panel
        JPanel baseGrowthsPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        baseGrowthsPanel.setBorder(BorderFactory.createTitledBorder("Base Growths"));
        for (String stat : stats) {
            baseGrowthsPanel.add(new JLabel(stat + ":"));
            JTextField growthField = new JTextField("10"); // Default value
            baseGrowthFields.add(growthField);
            baseGrowthsPanel.add(growthField);
        }

        // Phases Panel
        JPanel phasesPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        phasesPanel.setBorder(BorderFactory.createTitledBorder("Phase Growth Rates"));
        String[] phasesArray = {"Phase 1", "Phase 2", "Phase 3", "Phase 4"};
        String[] growthOptions = {"Very slow", "Slow", "Neutral", "Fast", "Very fast"};
        for (String phase : phasesArray) {
            phasesPanel.add(new JLabel(phase + ":"));
            JComboBox<String> comboBox = new JComboBox<>(growthOptions);
            comboBox.setSelectedItem("Neutral"); // Default value
            phaseGrowthCombos.add(comboBox);
            phasesPanel.add(comboBox);
        }

        // Add panels to input panel
        inputPanel.add(baseStatsPanel);
        inputPanel.add(baseGrowthsPanel);
        inputPanel.add(phasesPanel);

        add(inputPanel, BorderLayout.NORTH);

        // Calculated Stats Panel
        JPanel calculatedStatsPanel = new JPanel(new GridLayout(8, 6, 5, 5)); // 8 rows, 6 columns
        calculatedStatsPanel.setBorder(BorderFactory.createTitledBorder("Calculated Stats"));

        // Headers
        calculatedStatsPanel.add(new JLabel("Stat"));
        calculatedStatsPanel.add(new JLabel("Level 1"));
        calculatedStatsPanel.add(new JLabel("Level 30"));
        calculatedStatsPanel.add(new JLabel("Level 70"));
        calculatedStatsPanel.add(new JLabel("Level 110"));
        calculatedStatsPanel.add(new JLabel("Level 150"));

        // Updated stats array to include Enemies Needed and XP Required
        String[] allStats = {"HP", "Attack", "Defense", "Special", "Speed", "Enemies Needed", "XP Required"};

        // Stat Rows
        for (String stat : allStats) {
            calculatedStatsPanel.add(new JLabel(stat));
            for (int i = 0; i < 5; i++) { // 5 test levels
                JTextField field = new JTextField();
                field.setEditable(false);
                statFields.add(field);
                calculatedStatsPanel.add(field);
            }
        }

        add(calculatedStatsPanel, BorderLayout.CENTER);

        // Bottom Panel for XP Curve and Calculate Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // XP Curve Panel
        JPanel xpCurvePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        xpCurvePanel.add(new JLabel("XP Curve:"));
        String[] xpCurveOptions = {"VERY_SLOW", "SLOW", "NEUTRAL", "FAST", "VERY_FAST"};
        xpCurveCombo = new JComboBox<>(xpCurveOptions);
        xpCurveCombo.setSelectedItem("NEUTRAL"); // Default value
        xpCurvePanel.add(xpCurveCombo);

        // Calculate Button
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setPreferredSize(new Dimension(100, 25));
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Read inputs
                readInputs();

                // Perform calculations
                List<Double> totalStats = CalculateStats.calculate(baseStats, baseGrowths, phases, selectedCurve);

                double bst = baseStats.stream().mapToDouble(Double::doubleValue).sum();
                int maxLevel = 150;
                Map<Integer, CalculateStats.XPData> xpSystem = CalculateStats.calculateXPSystem(bst, selectedCurve, maxLevel);

                // Update calculated stats
                updateCalculatedStats(totalStats, xpSystem);
            }
        });

        // Add XP Curve Panel and Calculate Button to Bottom Panel
        bottomPanel.add(xpCurvePanel);
        bottomPanel.add(calculateButton);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void readInputs() {
        baseStats.clear();
        for (JTextField field : baseStatFields) {
            baseStats.add(Double.parseDouble(field.getText()));
        }

        baseGrowths.clear();
        for (JTextField field : baseGrowthFields) {
            baseGrowths.add(Double.parseDouble(field.getText()));
        }

        phases.clear();
        for (JComboBox<String> comboBox : phaseGrowthCombos) {
            phases.add((String) comboBox.getSelectedItem());
        }

        selectedCurve = CalculateStats.XP_Curve.valueOf((String) xpCurveCombo.getSelectedItem());
    }

    private void updateCalculatedStats(List<Double> totalStats, Map<Integer, CalculateStats.XPData> xpSystem) {
        int[] testLevels = {1, 30, 70, 110, 150};
        int numStats = 5; // Number of stats: HP, Attack, Defense, Special, Speed

        // Clear previous results
        for (JTextField field : statFields) {
            field.setText("");
        }

        // Update stat values
        for (int statIndex = 0; statIndex < numStats; statIndex++) {
            int baseIndex = statIndex * testLevels.length;

            for (int i = 0; i < testLevels.length; i++) {
                double statValue = totalStats.get(statIndex * testLevels.length + i);

                JTextField levelField = statFields.get(baseIndex + i);
                levelField.setText(String.valueOf((int) Math.round(statValue)));
            }
        }

        // Update Enemies Needed and XP Required
        int totalStatRows = numStats * testLevels.length;
        for (int i = 0; i < testLevels.length; i++) {
            int level = testLevels[i];
            CalculateStats.XPData data = xpSystem.get(level);

            if (data != null) {
                // Enemies Needed
                JTextField enemiesField = statFields.get(totalStatRows + i);
                enemiesField.setText(String.valueOf((int) Math.round(data.getEnemiesNeeded())));

                // XP Required
                JTextField xpField = statFields.get(totalStatRows + testLevels.length + i);
                xpField.setText(String.valueOf((int) Math.round(data.getXpRequired())));
            }
        }
    }

    public static void main(String[] args) {
        new StatTester();
    }
}