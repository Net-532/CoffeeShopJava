package service;

import model.Order;
import model.Product;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderService {

    private Connection conn;

    public Order createOrder(List<Product> products) {
        Order order = new Order();
        order.addItems(products);
        saveOrderToDatabase(order);
        return order;
    }


    private void saveOrderToDatabase(Order order) {
        String sql = "INSERT INTO orders (product_id, quantity) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); // Start transaction
            for (Order.OrderItem entry : order.getItems()) { // Iterate through OrderItems
                Product product = entry.getProduct(); // Get Product from OrderItem
                int quantity = entry.getQuantity(); // Get Quantity from OrderItem
                int productId = getProductIdByName(product.getName()); // Retrieve Product ID
                System.out.println("Inserting product ID: " + productId + ", quantity: " + quantity);
                pstmt.setInt(1, productId);
                pstmt.setInt(2, quantity);
                pstmt.addBatch(); // Add to batch
            }
            pstmt.executeBatch(); // Execute batch insert
            conn.commit(); // Commit transaction
            System.out.println("Transaction committed successfully.");
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
            try {
                conn.rollback(); // Rollback transaction in case of an error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }

    }

    private int getProductIdByName(String name) throws SQLException {
        String sql = "SELECT id FROM products WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (var rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    System.out.println("Product not found: " + name); // Debugging line
                }
            }
        }
        throw new SQLException("Product not found: " + name);
    }

}
