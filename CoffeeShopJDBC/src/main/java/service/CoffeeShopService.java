package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Product;
import model.Coffee;
import model.Treat;
import util.DatabaseConnection;
import java.math.BigDecimal;

public class CoffeeShopService {
    private List<Product> menu;

    public CoffeeShopService() {
        this.menu = loadMenuFromDatabase();
    }

    public List<Product> getMenu() {
        return menu;
    }

    // Метод для завантаження меню з бази даних
    private List<Product> loadMenuFromDatabase() {
        List<Product> menu = new ArrayList<>();
        String sql = "SELECT name, price, type, size, gluten_free FROM products";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                BigDecimal price = rs.getBigDecimal("price");
                String type = rs.getString("type");

                if ("COFFEE".equalsIgnoreCase(type)) {
                    Coffee.CupSize size = Coffee.CupSize.valueOf(rs.getString("size"));
                    menu.add(new Coffee(name, price, size));
                } else if ("TREAT".equalsIgnoreCase(type)) {
                    boolean glutenFree = rs.getBoolean("gluten_free");
                    menu.add(new Treat(name, price, glutenFree));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menu;
    }

    // Додавання продукту в базу даних
    public boolean addProduct(Product product) {
        if (isProductExists(product.getName())) {
            return false;
        }

        String sql = "INSERT INTO products (name, price, type, size, gluten_free) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setBigDecimal(2, product.getPrice());
            pstmt.setString(3, product instanceof Coffee ? "COFFEE" : "TREAT");
            pstmt.setString(4, ((Coffee) product).getSize().name());


            if (product instanceof Coffee) {
                pstmt.setString(4, ((Coffee) product).getSize().name());
            } else {
                pstmt.setNull(4, Types.VARCHAR);
            }

            pstmt.setBoolean(5, product instanceof Treat && ((Treat) product).isGlutenFree());
            pstmt.executeUpdate();
            menu.add(product); // Оновлення локального списку меню

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Перевірка існування продукту в базі даних
    public boolean isProductExists(String productName) {
        String sql = "SELECT COUNT(*) FROM products WHERE LOWER(name) = LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Видалення продукту з бази даних
    public boolean deleteProductByName(String name) {
        String sql = "DELETE FROM products WHERE LOWER(name) = LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                menu.removeIf(product -> product.getName().equalsIgnoreCase(name)); // Оновлення локального списку
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Оновлення продукту в базі даних
    public boolean updateProduct(int index, Product updatedProduct) {
        if (index < 0 || index >= menu.size()) {
            return false;
        }

        String sql = "UPDATE products SET price = ?, size = ?, gluten_free = ? WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, updatedProduct.getPrice());

            if (updatedProduct instanceof Coffee) {
                pstmt.setString(2, ((Coffee) updatedProduct).getSize().name());
                pstmt.setNull(3, Types.BOOLEAN);
            } else if (updatedProduct instanceof Treat) {
                pstmt.setNull(2, Types.VARCHAR);
                pstmt.setBoolean(3, ((Treat) updatedProduct).isGlutenFree());
            }

            pstmt.setString(4, updatedProduct.getName());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                menu.set(index, updatedProduct); // Оновлення локального списку меню
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Optional<Product> getProductByIndex(int index) {
        if (index >= 0 && index < menu.size()) {
            return Optional.of(menu.get(index));
        }
        return Optional.empty();
    }
}
