package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<OrderItem> items;

    public Order() {
        this.items = new ArrayList<>();
    }

    public void addItem(Product product, int quantity) {
        items.add(new OrderItem(product, quantity));
    }

    public void addItems(List<Product> products) {
        for (Product product : products) {
            addItem(product, 1);  // Add each product with quantity 1
        }
    }

    // Make the items list accessible
    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Order Summary:\n");
        for (OrderItem item : items) {
            sb.append(item).append("\n");
        }
        sb.append("Total Price: ").append(getTotalPrice());
        return sb.toString();
    }

    // Inner OrderItem class with public getters
    public static class OrderItem {
        private final Product product;
        private final int quantity;

        public OrderItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        @Override
        public String toString() {
            return String.format("%s x%d", product, quantity);
        }
    }
}
