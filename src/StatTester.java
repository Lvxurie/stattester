import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter; // Import for FileWriter
import java.io.IOException; // Import for IOException
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    // Added variables to store calculated data
    private List<Double> totalStats = new ArrayList<>();
    private Map<Integer, CalculateStats.XPData> xpSystem;

    public StatTester() {
        setTitle("Stat Tester");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Adjusted window size
        setLayout(new BorderLayout(10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Base Stats Panel
        JPanel baseStatsPanel = new JPanel(new GridLayout(6, 2, 5, 5)); // Updated to 6 rows
        baseStatsPanel.setBorder(BorderFactory.createTitledBorder("Base Stats"));
        String[] stats = {"HP", "Attack", "Armor", "Special", "Shield", "Speed"};
        for (String stat : stats) {
            baseStatsPanel.add(new JLabel(stat + ":"));
            JTextField statField = new JTextField("100"); // Default value
            baseStatFields.add(statField);
            baseStatsPanel.add(statField);
        }

        // Base Growths Panel
        JPanel baseGrowthsPanel = new JPanel(new GridLayout(6, 2, 5, 5)); // Updated to 6 rows
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
        JPanel calculatedStatsPanel = new JPanel(new GridLayout(10, 6, 5, 5)); // Adjusted to 9 rows
        calculatedStatsPanel.setBorder(BorderFactory.createTitledBorder("Calculated Stats"));

        // Headers
        calculatedStatsPanel.add(new JLabel("Stat"));
        calculatedStatsPanel.add(new JLabel("Level 1"));
        calculatedStatsPanel.add(new JLabel("Level 30"));
        calculatedStatsPanel.add(new JLabel("Level 70"));
        calculatedStatsPanel.add(new JLabel("Level 110"));
        calculatedStatsPanel.add(new JLabel("Level 150"));

        // Updated stats array to include BST and XP data
        String[] allStats = {"HP", "Attack", "Armor", "Special", "Shield", "Speed", "BST", "XP to Next Level", "Cumulative XP"};

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

        // Bottom Panel for XP Curve and Buttons
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
                totalStats = CalculateStats.calculate(baseStats, baseGrowths, phases, selectedCurve);

                double bst = baseStats.stream().mapToDouble(Double::doubleValue).sum();
                int maxLevel = 150;
                xpSystem = CalculateStats.calculateXPSystem(bst, selectedCurve, maxLevel);

                // Update calculated stats
                updateCalculatedStats(totalStats, xpSystem);
            }
        });

        // Export Button
        JButton exportButton = new JButton("Export");
        exportButton.setPreferredSize(new Dimension(100, 25));
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToCSV();
            }
        });

        // Add XP Curve Panel and Buttons to Bottom Panel
        bottomPanel.add(xpCurvePanel);
        bottomPanel.add(calculateButton);
        bottomPanel.add(exportButton);

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
        int numStats = 6; // Number of stats: HP, Attack, Armor, Shield, Special, Speed

        // Clear previous results
        for (JTextField field : statFields) {
            field.setText("");
        }

        // Update stat values
        // Store BST values for each level
        double[] bstValues = new double[testLevels.length];

        for (int statIndex = 0; statIndex < numStats; statIndex++) {
            int baseIndex = statIndex * testLevels.length;

            for (int i = 0; i < testLevels.length; i++) {
                double statValue = totalStats.get(statIndex * testLevels.length + i);

                JTextField levelField = statFields.get(baseIndex + i);
                levelField.setText(String.valueOf((int) Math.round(statValue)));

                // Add to BST
                bstValues[i] += statValue;
            }
        }

        // Update BST row
        int bstRowIndex = numStats * testLevels.length;
        for (int i = 0; i < testLevels.length; i++) {
            JTextField bstField = statFields.get(bstRowIndex + i);
            bstField.setText(String.valueOf((int) Math.round(bstValues[i])));
        }

        // Update XP data
        int totalStatRows = (numStats + 1) * testLevels.length; // Updated numStats + 1 for BST
        for (int i = 0; i < testLevels.length; i++) {
            int level = testLevels[i];
            CalculateStats.XPData data = xpSystem.get(level);

            if (data != null) {
                // XP to Next Level
                JTextField xpNextLevelField = statFields.get(totalStatRows + i);
                xpNextLevelField.setText(String.valueOf((int) Math.round(data.getXpToNextLevel())));

                // Cumulative XP
                JTextField cumulativeXPField = statFields.get(totalStatRows + testLevels.length + i);
                cumulativeXPField.setText(String.valueOf((int) Math.round(data.getCumulativeXP())));
            }
        }
    }

    private void exportToCSV() {
        if (totalStats == null || totalStats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please calculate stats before exporting.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Ensure the file has a .csv extension
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
            }

            try (FileWriter writer = new FileWriter(filePath)) {
                // Write headers
                writer.append("Base Stats:\n");
                String[] stats = {"HP", "Attack", "Armor", "Shield", "Special", "Speed"};
                for (int i = 0; i < stats.length; i++) {
                    writer.append(stats[i]).append(",").append(String.valueOf(baseStats.get(i))).append("\n");
                }

                writer.append("\nBase Growths:\n");
                for (int i = 0; i < stats.length; i++) {
                    writer.append(stats[i]).append(",").append(String.valueOf(baseGrowths.get(i))).append("\n");
                }

                writer.append("\nPhase Growth Rates:\n");
                String[] phasesArray = {"Phase 1", "Phase 2", "Phase 3", "Phase 4"};
                for (int i = 0; i < phasesArray.length; i++) {
                    writer.append(phasesArray[i]).append(",").append(phases.get(i)).append("\n");
                }

                writer.append("\nXP Curve:\n");
                writer.append(selectedCurve.toString()).append("\n");

                writer.append("\nCalculated Stats:\n");
                writer.append("Stat,Level 1,Level 30,Level 70,Level 110,Level 150\n");

                int[] testLevels = {1, 30, 70, 110, 150};
                // Write stat values
                int numStats = 6; // Number of stats
                for (int statIndex = 0; statIndex < numStats; statIndex++) {
                    writer.append(stats[statIndex]);
                    for (int i = 0; i < testLevels.length; i++) {
                        double statValue = totalStats.get(statIndex * testLevels.length + i);
                        writer.append(",").append(String.valueOf((int) Math.round(statValue)));
                    }
                    writer.append("\n");
                }

                // Write BST values
                writer.append("BST");
                for (int i = 0; i < testLevels.length; i++) {
                    double bstValue = 0;
                    for (int statIndex = 0; statIndex < numStats; statIndex++) {
                        bstValue += totalStats.get(statIndex * testLevels.length + i);
                    }
                    writer.append(",").append(String.valueOf((int) Math.round(bstValue)));
                }
                writer.append("\n");

                // Write XP data
                writer.append("XP to Next Level");
                for (int i = 0; i < testLevels.length; i++) {
                    int level = testLevels[i];
                    CalculateStats.XPData data = xpSystem.get(level);
                    writer.append(",").append(String.valueOf((int) Math.round(data.getXpToNextLevel())));
                }
                writer.append("\n");

                writer.append("Cumulative XP");
                for (int i = 0; i < testLevels.length; i++) {
                    int level = testLevels[i];
                    CalculateStats.XPData data = xpSystem.get(level);
                    writer.append(",").append(String.valueOf((int) Math.round(data.getCumulativeXP())));
                }
                writer.append("\n");

                writer.flush();
                JOptionPane.showMessageDialog(this, "Data exported successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new StatTester();
    }
}