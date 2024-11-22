import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StatTester extends JFrame {
    private List<Double> baseStats = new ArrayList<>();
    private List<Double> calculatedStats = new ArrayList<>();
    private List<String> phaseRates = new ArrayList<>();
    private List<JTextField> baseStatFields = new ArrayList<>();
    private List<JTextField> statFields = new ArrayList<JTextField>();
    private List<JComboBox<String>> statGrowthComboboxes = new ArrayList<JComboBox<String>>();
    public StatTester() {
        super("Stat Tester");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout()); // Use GridBagLayout for the main frame
        GridBagConstraints gbc = new GridBagConstraints();



        // Top input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 5));
        inputPanel.setBorder(BorderFactory.createLineBorder(Color.black,2));


        // Row 1
        JLabel b1 = new JLabel();
        inputPanel.add(b1);
        JLabel baseGrowth = new JLabel("Base growth");
        baseGrowth.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(baseGrowth);
        JLabel phases = new JLabel("Phases");
        phases.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(phases);
        JLabel statGrowthrate = new JLabel("Growth rate");
        statGrowthrate.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(statGrowthrate);
        JLabel xpRate = new JLabel("XP Rate");
        xpRate.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(xpRate);

        // Row 2
        JLabel hp = new JLabel("HP");
        hp.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(hp);
        JTextField hpStat = new JTextField("0");
        inputPanel.add(hpStat);
        baseStatFields.add(hpStat);
        JLabel phase1 = new JLabel("Phase 1");
        phase1.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(phase1);

        // Phases ComboBox
        String[] p1Option = {"Very slow", "Slow", "Neutral", "Fast", "Very fast"};
        JComboBox<String> phase1Combo = new JComboBox<>(p1Option);
        inputPanel.add(phase1Combo);
        statGrowthComboboxes.add(phase1Combo);

        // Stat growth Combobox
        String[] xpOptions = {"Very slow", "Slow", "Neutral", "Fast", "Very fast"};
        JComboBox<String> xpGrowthComboBox = new JComboBox<>(xpOptions);
        inputPanel.add(xpGrowthComboBox);

        // Row 3
        JLabel attack = new JLabel("ATTACK");
        attack.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(attack);
        JTextField attackStat = new JTextField("0");
        inputPanel.add(attackStat);
        baseStatFields.add(attackStat);
        JLabel phase2 = new JLabel("Phase 2");
        phase2.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(phase2);

        // Phases ComboBox
        String[] p2Option = {"Very slow", "Slow", "Neutral", "Fast", "Very fast"};
        JComboBox<String> phase2Combo = new JComboBox<>(p2Option);
        inputPanel.add(phase2Combo);
        statGrowthComboboxes.add(phase2Combo);
        JLabel b2 = new JLabel();
        inputPanel.add(b2);

        // Row 4
        JLabel defense = new JLabel("DEFENSE");
        defense.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(defense);
        JTextField defenseStat = new JTextField("0");
        inputPanel.add(defenseStat);
        baseStatFields.add(defenseStat);
        JLabel phase3 = new JLabel("Phase 3");
        phase3.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(phase3);

        // Phases ComboBox
        String[] p3Option = {"Very slow", "Slow", "Neutral", "Fast", "Very fast"};
        JComboBox<String> phase3Combo = new JComboBox<>(p3Option);
        inputPanel.add(phase3Combo);
        statGrowthComboboxes.add(phase3Combo);
        JLabel b3 = new JLabel();
        inputPanel.add(b3);

        // Row 5
        JLabel special = new JLabel("SPECIAL");
        special.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(special);
        JTextField specialStat = new JTextField("0");
        inputPanel.add(specialStat);
        baseStatFields.add(specialStat);
        JLabel phase4 = new JLabel("Phase 4");
        phase4.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(phase4);

        // Phases ComboBox
        String[] p4Option = {"Very slow", "Slow", "Neutral", "Fast", "Very fast"};
        JComboBox<String> phase4Combo = new JComboBox<>(p4Option);
        inputPanel.add(phase4Combo);
        statGrowthComboboxes.add(phase4Combo);
        JLabel b4 = new JLabel();
        inputPanel.add(b4);

        // Row 6
        JLabel speed = new JLabel("SPEED");
        speed.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(speed);
        JTextField speedStat = new JTextField("0");
        inputPanel.add(speedStat);
        baseStatFields.add(speedStat);
        JLabel b5 = new JLabel();
        inputPanel.add(b5);
        JLabel b6 = new JLabel();
        inputPanel.add(b6);
        JButton calculate = new JButton("Calculate");
        calculate.setHorizontalAlignment(JLabel.CENTER);

        /*
        *
        * submit button
        *
        * */
        calculate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setBaseStats();
                setPhaseRates();
                CalculateStats.calculate(baseStats, phaseRates);
            }
        });
        inputPanel.add(calculate);


        // Add the input panel to the NORTH section
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.5; // Allocate space for the top panel
        gbc.fill = GridBagConstraints.BOTH;
        add(inputPanel, gbc);

/* -------------------------------------------------------------------------------------------------*/

        // Bottom display panel
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new GridLayout(6, 6));
        //Row 1
        JLabel level = new JLabel("Level");
        level.setHorizontalAlignment(JLabel.CENTER);
        displayPanel.add(level);
        JLabel level1 = new JLabel("1");
        level1.setHorizontalAlignment(JLabel.CENTER);
        displayPanel.add(level1);
        JLabel level30 = new JLabel("30");
        level30.setHorizontalAlignment(JLabel.CENTER);
        displayPanel.add(level30);
        JLabel level70 = new JLabel("70");
        level70.setHorizontalAlignment(JLabel.CENTER);
        displayPanel.add(level70);
        JLabel level110 = new JLabel("110");
        level110.setHorizontalAlignment(JLabel.CENTER);
        displayPanel.add(level110);
        JLabel level150 = new JLabel("150");
        level150.setHorizontalAlignment(JLabel.CENTER);
        displayPanel.add(level150);
        //Row 2
        JLabel hpModLabel = new JLabel("HP");
        hpModLabel.setHorizontalAlignment(JLabel.CENTER);
        displayPanel.add(hpModLabel);
        JTextField hp1 = new JTextField("0");
        displayPanel.add(hp1);
        statFields.add(hp1);
        JTextField hp40 = new JTextField("0");
        displayPanel.add(hp40);
        statFields.add(hp40);
        JTextField hp80 = new JTextField("0");
        displayPanel.add(hp80);
        statFields.add(hp80);
        JTextField hp120 = new JTextField("0");
        displayPanel.add(hp120);
        statFields.add(hp120);
        JTextField hp150 = new JTextField("0");
        displayPanel.add(hp150);
        statFields.add(hp150);
        //Row 3
        JLabel attackModLabel = new JLabel("ATTACK");
        attackModLabel.setHorizontalAlignment(JLabel.CENTER);
        displayPanel.add(attackModLabel);
        JTextField attack1 = new JTextField("0");
        displayPanel.add(attack1);
        statFields.add(attack1);
        JTextField attack40 = new JTextField("0");
        displayPanel.add(attack40);
        statFields.add(attack40);
        JTextField attack80 = new JTextField("0");
        displayPanel.add(attack80);
        statFields.add(attack80);
        JTextField attack120 = new JTextField("0");
        displayPanel.add(attack120);
        statFields.add(attack120);
        JTextField attack150 = new JTextField("0");
        displayPanel.add(attack150);
        statFields.add(attack150);
        //Row 4
        JLabel defenseModLabel = new JLabel("DEFENSE");
        defenseModLabel.setHorizontalAlignment(JLabel.CENTER);
        displayPanel.add(defenseModLabel);
        JTextField defense1 = new JTextField("0");
        displayPanel.add(defense1);
        statFields.add(defense1);
        JTextField defense40 = new JTextField("0");
        displayPanel.add(defense40);
        statFields.add(defense40);
        JTextField defense80 = new JTextField("0");
        displayPanel.add(defense80);
        statFields.add(defense80);
        JTextField defense120 = new JTextField("0");
        displayPanel.add(defense120);
        statFields.add(defense120);
        JTextField defense150 = new JTextField("0");
        displayPanel.add(defense150);
        statFields.add(defense150);
        //Row 5
        JLabel specialModLabel = new JLabel("SPECIAL");
        specialModLabel.setHorizontalAlignment(JLabel.CENTER);
        displayPanel.add(specialModLabel);
        JTextField special1 = new JTextField("0");
        displayPanel.add(special1);
        statFields.add(special1);
        JTextField special40 = new JTextField("0");
        displayPanel.add(special40);
        statFields.add(special40);
        JTextField special80 = new JTextField("0");
        displayPanel.add(special80);
        statFields.add(special80);
        JTextField special120 = new JTextField("0");
        displayPanel.add(special120);
        statFields.add(special120);
        JTextField special150 = new JTextField("0");
        displayPanel.add(special150);
        statFields.add(special150);
        //Row 6
        JLabel speedModLabel = new JLabel("SPEED");
        speedModLabel.setHorizontalAlignment(JLabel.CENTER);
        displayPanel.add(speedModLabel);
        JTextField speed1 = new JTextField("0");
        displayPanel.add(speed1);
        statFields.add(speed1);
        JTextField speed40 = new JTextField("0");
        displayPanel.add(speed40);
        statFields.add(speed40);
        JTextField speed80 = new JTextField("0");
        displayPanel.add(speed80);
        statFields.add(speed80);
        JTextField speed120 = new JTextField("0");
        displayPanel.add(speed120);
        statFields.add(speed120);
        JTextField speed150 = new JTextField("0");
        displayPanel.add(speed150);
        statFields.add(speed150);


        // Add the display panel to the SOUTH section
        gbc.gridy = 1;
        gbc.weighty = 0.5; // Allocate space for the bottom panel
        add(displayPanel, gbc);

        // Finalize frame setup
        setSize(550, 300);
        setVisible(true);
    }

    private void setPhaseRates() {
        for(JComboBox<String> phase : statGrowthComboboxes){
            //can not be null
            phaseRates.add(phase.getSelectedItem().toString());
        }
    }

    private void setBaseStats() {
        for(JTextField field : baseStatFields){
            baseStats.add(Double.parseDouble(field.getText()));
        }
    }

    public static void main(String[] args) {
        new StatTester();
    }
}
