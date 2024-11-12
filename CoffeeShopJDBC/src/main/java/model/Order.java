package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id; // Унікальний ідентифікатор для замовлення
    private List<OrderItem> items;

    // Конструктор без параметрів
    public Order() {
        this.items = new ArrayList<>();
    }

    // Конструктор з ідентифікатором
    public Order(Long id) {
        this.id = id;
        this.items = new ArrayList<>();
    }

    // Геттери та сеттери для id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Додавання продукту до замовлення
    public void addItem(Product product, int quantity) {
        items.add(new OrderItem(product, quantity));
    }

    // Додавання списку продуктів (за замовчуванням із кількістю 1)
    public void addItems(List<Product> products) {
        for (Product product : products) {
            addItem(product, 1);
        }
    }

    // Отримання списку продуктів у замовленні
    public List<OrderItem> getItems() {
        return items;
    }

    // Обчислення загальної вартості замовлення
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

    // Внутрішній клас OrderItem
    public static class OrderItem {
        private final Product product;
        private final int quantity;

        // Конструктор для елемента замовлення
        public OrderItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        // Геттери
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
