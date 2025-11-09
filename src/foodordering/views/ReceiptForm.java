package foodordering.views;

import foodordering.models.MenuItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ReceiptForm extends JFrame {

    public ReceiptForm(Map<MenuItem, Integer> cart) {
        setTitle("Order Receipt");
        setSize(500, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents(cart);
    }

    private void initComponents(Map<MenuItem, Integer> cart) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Item", "Qty", "Price", "Total"}, 0);
        JTable receiptTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(receiptTable);

        double total = 0;
        for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
            MenuItem item = entry.getKey();
            int qty = entry.getValue();
            double price = item.getPrice();
            double subtotal = price * qty;
            total += subtotal;
            model.addRow(new Object[]{item.getName(), qty, price, subtotal});
        }

        JLabel totalLabel = new JLabel("Total: Rs. " + total);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel dateTimeLabel = new JLabel("Time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        JButton printButton = new JButton("Print Receipt");
        printButton.addActionListener(e -> {
            try {
                receiptTable.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));
        bottomPanel.add(dateTimeLabel);
        bottomPanel.add(totalLabel);
        bottomPanel.add(printButton);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
