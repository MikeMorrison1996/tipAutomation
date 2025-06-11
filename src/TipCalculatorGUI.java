import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TipCalculatorGUI extends JFrame {
    private final JTextField nameField = new JTextField();
    private final JTextField hoursField = new JTextField();
    private final JTextField totalTipsField = new JTextField();
    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Name","Hours","Share"}, 0);
    private final List<Double> hoursList = new ArrayList<>();
    private final DBConnection db = DBConnection.getInstance();

    public TipCalculatorGUI() {
        super("Tip Calculator (Modern)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
        pack();
        setLocationRelativeTo(null);  // center
    }

    private void initUI() {
        // Top input panel
        JPanel input = new JPanel(new GridLayout(3, 3, 10, 10));
        input.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        input.add(new JLabel("Name:"));          input.add(nameField);      input.add(createButton("Add Person", this::onAddPerson));
        input.add(new JLabel("Hours:"));         input.add(hoursField);     input.add(new JLabel());
        input.add(new JLabel("Total Tips:"));    input.add(totalTipsField); input.add(createButton("Calculate & Save", this::onCalculate));
        add(input, BorderLayout.NORTH);

        // Table area
        JTable table = new JTable(tableModel);
        table.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        add(scroll, BorderLayout.CENTER);
    }

    private JButton createButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setPreferredSize(new Dimension(140, 30));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private void onAddPerson() {
        String name = nameField.getText().trim();
        String hrsText = hoursField.getText().trim();
        if (name.isEmpty() || hrsText.isEmpty()) return;
        try {
            double hrs = Double.parseDouble(hrsText);
            hoursList.add(hrs);
            tableModel.addRow(new Object[]{name, hrs, ""});
            nameField.setText("");
            hoursField.setText("");
        } catch (NumberFormatException e) {
            showError("Invalid hours input.");
        }
    }

    private void onCalculate() {
        String tipsText = totalTipsField.getText().trim();
        if (tipsText.isEmpty()) return;
        try {
            double totalTips = Double.parseDouble(tipsText);
            double totalHours = hoursList.stream().mapToDouble(Double::doubleValue).sum();

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String name = (String) tableModel.getValueAt(i, 0);
                double hrs = ((Number) tableModel.getValueAt(i, 1)).doubleValue();
                double share = hrs / totalHours * totalTips;
                tableModel.setValueAt(String.format("%.2f", share), i, 2);
                db.save(name, hrs, share, totalTips);
            }
            JOptionPane.showMessageDialog(this, "Records saved successfully!");
        } catch (NumberFormatException e) {
            showError("Invalid total tips input.");
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
