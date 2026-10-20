package com.shopsphere.swing;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import com.shopsphere.model.User;
import com.shopsphere.service.AdminProductService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboardFrame extends JFrame {

    private final User admin;
    private final ProductDAO productDAO = new ProductDAO();
    private final AdminProductService productService = new AdminProductService();

    private final String[] columns = {"ID", "Name", "Brand", "Price (Rs.)", "Discount %", "Stock", "Status"};
    private final DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
        // Make the cells read-only; changes go through the buttons
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);
    private final JLabel statusLabel = new JLabel(" ");

    public AdminDashboardFrame(User admin) {
        super("ShopSphere Admin - Products");
        this.admin = admin;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton refreshButton = new JButton("Refresh");
        JButton toggleButton = new JButton("Enable / Disable selected");
        JButton lowStockButton = new JButton("Show low stock");

        refreshButton.addActionListener(e -> loadProducts(false));
        lowStockButton.addActionListener(e -> loadProducts(true));
        toggleButton.addActionListener(e -> toggleSelected());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.add(refreshButton);
        toolbar.add(lowStockButton);
        toolbar.add(toggleButton);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JLabel header = new JLabel("Logged in as " + admin.getName());
        header.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(toolbar, BorderLayout.CENTER);
        bottom.add(statusLabel, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);

        setSize(820, 480);
        setLocationRelativeTo(null);
        loadProducts(false);
    }

    // Loads all products, or only products with stock of 5 or less
    private void loadProducts(boolean lowStockOnly) {
        try {
            List<Product> products = lowStockOnly
                    ? productDAO.findLowStock(5)
                    : productDAO.findAllForAdmin();

            tableModel.setRowCount(0);
            for (Product p : products) {
                tableModel.addRow(new Object[]{
                        p.getProductId(),
                        p.getName(),
                        p.getBrand(),
                        String.format("%.2f", p.getPrice()),
                        p.getDiscount(),
                        p.getStock(),
                        p.isStatus() ? "Active" : "Disabled"
                });
            }
            statusLabel.setText(products.size() + " product(s) shown" + (lowStockOnly ? " (low stock)" : ""));
        } catch (SQLException ex) {
            statusLabel.setText("Could not load products: " + ex.getMessage());
        }
    }

    // Enables a disabled product, or disables an active one
    private void toggleSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a product first.", "No selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int productId = (Integer) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int choice = JOptionPane.showConfirmDialog(this,
                "Change the status of \"" + name + "\"?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            productService.toggleStatus(productId);
            loadProducts(false);
            statusLabel.setText("Status updated for " + name + ".");
        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}