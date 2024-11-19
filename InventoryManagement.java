import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class InventoryManagement {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory";
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = ""; // Replace with your MySQL password

    public static void main(String[] args) {
        // Test the database connection
        testConnection();

        // Create GUI
        JFrame frame = new JFrame("Combined Inventory Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new GridLayout(10, 2, 5, 5)); // Grid layout with padding

        // Input fields
        JLabel nameLabel = new JLabel("Item Name:");
        JTextField nameField = new JTextField();
        JLabel qtyLabel = new JLabel("Item Quantity:");
        JTextField qtyField = new JTextField();

        // Action buttons
        JButton addBtn = new JButton("Add to Main Inventory (inv)");
        JButton updateBtn = new JButton("Update Main Inventory (inv)");
        JButton searchBtn = new JButton("Search Main Inventory (inv)");
        JButton removeBtn = new JButton("Remove from Main Inventory (inv)");
        JButton showBtn = new JButton("Show Main Inventory (inv)");

        JButton addInv1Btn = new JButton("Add to Additional Inventory (inv1)");
        JButton removeInv1Btn = new JButton("Remove from Additional Inventory (inv1)");
        JButton showInv1Btn = new JButton("Show Additional Inventory (inv1)");
        JButton showFinalBtn = new JButton("Show Final Inventory");

        // Table for displaying results
        JTable resultTable = new JTable(new DefaultTableModel(new String[]{"Item Name", "Quantity"}, 0));
        JScrollPane scrollPane = new JScrollPane(resultTable);

        // Adding components to the frame
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(qtyLabel);
        frame.add(qtyField);

        frame.add(addBtn);
        frame.add(updateBtn);
        frame.add(searchBtn);
        frame.add(removeBtn);
        frame.add(showBtn);

        frame.add(new JLabel("Additional Inventory Management:"));
        frame.add(new JLabel());
        frame.add(addInv1Btn);
        frame.add(removeInv1Btn);
        frame.add(showInv1Btn);

        frame.add(new JLabel("Final Inventory:"));
        frame.add(showFinalBtn);
        frame.add(scrollPane);

        // Action Listeners for Buttons
        addBtn.addActionListener(e -> addToDatabase("inv", nameField, qtyField, resultTable));
        updateBtn.addActionListener(e -> updateDatabase("inv", nameField, qtyField, resultTable));
        searchBtn.addActionListener(e -> searchDatabase("inv", nameField, resultTable));
        removeBtn.addActionListener(e -> removeFromDatabase("inv", nameField, resultTable));
        showBtn.addActionListener(e -> showTable("inv", resultTable));

        addInv1Btn.addActionListener(e -> addToDatabase("inv1", nameField, qtyField, resultTable));
        removeInv1Btn.addActionListener(e -> removeFromDatabase("inv1", nameField, resultTable));
        showInv1Btn.addActionListener(e -> showTable("inv1", resultTable));

        showFinalBtn.addActionListener(e -> calculateAndShowFinalInventory(resultTable));

        // Display the frame
        frame.setVisible(true);
    }

    // Singleton DB Connection
    static class DBConnection {
        private static Connection connection;

        private DBConnection() {}

        public static Connection getConnection() throws SQLException {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
            return connection;
        }
    }

    // Test the database connection
    private static void testConnection() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    // Method to validate fields
    private static boolean validateFields(JTextField nameField, JTextField qtyField, JTable resultTable) {
        if (nameField.getText().isEmpty()) {
            showError("Item name cannot be empty.", resultTable);
            return false;
        }
        if (qtyField.getText().isEmpty()) {
            showError("Quantity cannot be empty.", resultTable);
            return false;
        }
        try {
            Integer.parseInt(qtyField.getText());
        } catch (NumberFormatException e) {
            showError("Quantity must be a valid number.", resultTable);
            return false;
        }
        return true;
    }

    // Method to show error messages
    private static void showError(String message, JTable resultTable) {
        DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
        model.setRowCount(0); // Clear table
        model.addRow(new Object[]{message, ""});
    }

    // Add item to the database
    private static void addToDatabase(String tableName, JTextField nameField, JTextField qtyField, JTable resultTable) {
        if (!validateFields(nameField, qtyField, resultTable)) return;

        String itemName = nameField.getText();
        int itemQty = Integer.parseInt(qtyField.getText());
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO " + tableName + " (item_name, item_quantity) VALUES (?, ?) " +
                             "ON DUPLICATE KEY UPDATE item_quantity = item_quantity + ?")) {
            stmt.setString(1, itemName);
            stmt.setInt(2, itemQty);
            stmt.setInt(3, itemQty);
            stmt.executeUpdate();
            showTable(tableName, resultTable);
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage(), resultTable);
        }
    }

    // Update item in the database
    private static void updateDatabase(String tableName, JTextField nameField, JTextField qtyField, JTable resultTable) {
        if (!validateFields(nameField, qtyField, resultTable)) return;

        String itemName = nameField.getText();
        int itemQty = Integer.parseInt(qtyField.getText());
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE " + tableName + " SET item_quantity = ? WHERE item_name = ?")) {
            stmt.setInt(1, itemQty);
            stmt.setString(2, itemName);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                showError("Item not found in " + tableName + ".", resultTable);
            } else {
                showTable(tableName, resultTable);
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage(), resultTable);
        }
    }

    // Search for an item in the database
    private static void searchDatabase(String tableName, JTextField nameField, JTable resultTable) {
        String itemName = nameField.getText();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT item_name, item_quantity FROM " + tableName + " WHERE item_name = ?")) {
            stmt.setString(1, itemName);
            try (ResultSet rs = stmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
                model.setRowCount(0); // Clear table
                if (rs.next()) {
                    model.addRow(new Object[]{rs.getString("item_name"), rs.getInt("item_quantity")});
                } else {
                    showError("Item not found in " + tableName + ".", resultTable);
                }
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage(), resultTable);
        }
    }

    // Show the contents of a table
    private static void showTable(String tableName, JTable resultTable) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT item_name, item_quantity FROM " + tableName);
             ResultSet rs = stmt.executeQuery()) {
            DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
            model.setRowCount(0); // Clear table
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("item_name"), rs.getInt("item_quantity")});
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage(), resultTable);
        }
    }

    // Calculate and show final inventory
    private static void calculateAndShowFinalInventory(JTable resultTable) {
        String sql = "SELECT i.item_name, COALESCE(i.item_quantity, 0) - COALESCE(s.item_quantity, 0) AS final_quantity " +
                     "FROM inv i LEFT JOIN inv1 s ON i.item_name = s.item_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
            model.setRowCount(0); // Clear table
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("item_name"), rs.getInt("final_quantity")});
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage(), resultTable);
        }
    }

    // Remove item from database
    private static void removeFromDatabase(String tableName, JTextField nameField, JTable resultTable) {
        String itemName = nameField.getText();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM " + tableName + " WHERE item_name = ?")) {
            stmt.setString(1, itemName);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                showError("Item not found in " + tableName + ".", resultTable);
            } else {
                showTable(tableName, resultTable);
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage(), resultTable);
        }
    }
}
