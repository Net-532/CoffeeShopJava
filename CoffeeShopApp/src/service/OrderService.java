package service;

import model.Order;
import model.Product;

import java.util.List;

public class OrderService {
    public Order createOrder(List<Product> products) {
        Order order = new Order();
        order.addItems(products); // Додаємо список продуктів до замовлення
        return order;
    }

    public void addProductToOrder(Order order, Product product, int quantity) {
        order.addItem(product, quantity); // Додаємо конкретний продукт із кількістю
    }

    public double calculateTotalPrice(Order order) {
        return order.getTotalPrice(); // Повертаємо загальну вартість
    }
}
