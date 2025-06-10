import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TipCalculatorGUI extends JFrame {
    private JTextField nameField;
    private JTextField hoursField;
    private JTextField totalTipsField;
    private DefaultTableModel tableModel;
    private List<Double> hoursList;

    public TipCalculatorGUI() {
        setTitle("Tip Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        hoursList = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        JButton addButton = new JButton("Add Person");
        inputPanel.add(addButton);

        inputPanel.add(new JLabel("Hours:"));
        hoursField = new JTextField();
        inputPanel.add(hoursField);
        inputPanel.add(new JLabel()); // empty placeholder

        inputPanel.add(new JLabel("Total Tips:"));
        totalTipsField = new JTextField();
        inputPanel.add(totalTipsField);
        JButton calcButton = new JButton("Calculate & Save");
        inputPanel.add(calcButton);

        tableModel = new DefaultTableModel(new Object[]{"Name", "Hours", "Share"}, 0);
        JTable table = new JTable(tableModel);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String hoursText = hoursField.getText();
                if (name.isEmpty() || hoursText.isEmpty()) return;
                try {
                    double hours = Double.parseDouble(hoursText);
                    hoursList.add(hours);
                    tableModel.addRow(new Object[]{name, hours, ""});
                    nameField.setText("");
                    hoursField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TipCalculatorGUI.this, "Invalid hours");
                }
            }
        });

        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipsText = totalTipsField.getText();
                if (tipsText.isEmpty()) return;
                try {
                    double totalTips = Double.parseDouble(tipsText);
                    double totalHours = hoursList.stream().mapToDouble(Double::doubleValue).sum();
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        double hours = (double) tableModel.getValueAt(i, 1);
                        double share = (hours / totalHours) * totalTips;
                        tableModel.setValueAt(String.format("%.2f", share), i, 2);
                        saveRecord(tableModel.getValueAt(i, 0).toString(), hours, share, totalTips);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TipCalculatorGUI.this, "Invalid total tips");
                }
            }
        });
    }

    private void saveRecord(String name, double hours, double share, double totalTips) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("TipAutomation");
            MongoCollection<Document> collection = database.getCollection("tip_records");
            Document doc = new Document("name", name)
                    .append("hours", hours)
                    .append("share", share)
                    .append("totalTips", totalTips)
                    .append("timestamp", System.currentTimeMillis());
            collection.insertOne(doc);
        } catch (Exception ex) {
            // handle connection errors gracefully
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TipCalculatorGUI().setVisible(true);
        });
    }
}
