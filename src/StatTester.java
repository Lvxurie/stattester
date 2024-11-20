import javax.swing.*;
import java.awt.*;

public class StatTester extends JFrame {
    public StatTester(){
        super("Stat Tester");
        //Top input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6,5));
        //Row 1
        JLabel b1 = new JLabel();
        inputPanel.add(b1);
        JLabel baseGrowth = new JLabel("Base growth stats");
        inputPanel.add(baseGrowth);
        JLabel phases = new JLabel("Phases");
        inputPanel.add(phases);
        JLabel statGrowthrate = new JLabel("Stat growth rate");
        inputPanel.add(statGrowthrate);
        JLabel xpRate = new JLabel("XP Rate");
        inputPanel.add(xpRate);
        //Row 2
        JLabel hp = new JLabel("HP");
        inputPanel.add(hp);
        JTextField hpStat = new JTextField("0");
        inputPanel.add(hpStat);
        JLabel phase1 = new JLabel("Phase 1");
        inputPanel.add(phase1);
        //Phases ComboBox
        String[] p1Option = {"Very slow", "Slow", "neutral", "Fast", "Very fast"};
        JComboBox<String> phase1Combo = new JComboBox<>(p1Option);
        inputPanel.add(phase1Combo);
        //Stat growth Combobox
        String[] xpOptions = {"Very slow", "Slow", "neutral", "Fast", "Very fast"};
        JComboBox<String> xpGrowthComboBox = new JComboBox<>(xpOptions);
        inputPanel.add(xpGrowthComboBox);
        //Row 3
        JLabel attack = new JLabel("ATTACK");
        inputPanel.add(attack);
        JTextField attackStat = new JTextField("0");
        inputPanel.add(attackStat);
        JLabel phase2 = new JLabel("Phase 2");
        inputPanel.add(phase2);
        //Phases ComboBox
        String[] p2Option = {"Very slow", "Slow", "neutral", "Fast", "Very fast"};
        JComboBox<String> phase2Combo = new JComboBox<>(p2Option);
        inputPanel.add(phase2Combo);
        JLabel b2 = new JLabel();
        inputPanel.add(b2);
        //Row 3
        JLabel defense = new JLabel("DEFENSE");
        inputPanel.add(defense);
        JTextField defenseStat = new JTextField("0");
        inputPanel.add(defenseStat);
        JLabel phase3 = new JLabel("Phase 3");
        inputPanel.add(phase3);
        //Phases ComboBox
        String[] p3Option = {"Very slow", "Slow", "neutral", "Fast", "Very fast"};
        JComboBox<String> phase3Combo = new JComboBox<>(p3Option);
        inputPanel.add(phase3Combo);
        JLabel b3 = new JLabel();
        inputPanel.add(b3);
        //Row 4
        JLabel special = new JLabel("SPECIAL");
        inputPanel.add(special);
        JTextField specialStat = new JTextField("0");
        inputPanel.add(specialStat);
        JLabel phase4 = new JLabel("Phase 4");
        inputPanel.add(phase4);
        //Phases ComboBox
        String[] p4Option = {"Very slow", "Slow", "neutral", "Fast", "Very fast"};
        JComboBox<String> phase4Combo = new JComboBox<>(p4Option);
        inputPanel.add(phase4Combo);
        JLabel b4 = new JLabel();
        inputPanel.add(b4);
        //Row 5
        JLabel speed = new JLabel("SPEED");
        inputPanel.add(speed);
        JTextField speedStat = new JTextField("0");
        inputPanel.add(speedStat);
        setSize(350,350);
        add(inputPanel);
        setVisible(true);
        //Bottom display panel
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new GridLayout(6,6));
    }

    public static void main(String[] args){
        new StatTester();
    }
}
