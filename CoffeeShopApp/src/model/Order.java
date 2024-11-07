package model;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Order {
    private Map<Product, Integer> items;
    public Order() {
        this.items = new HashMap<>();
    }
    public void addItems(List<Product> products) {
        for (var item : products) {
            this.addItem(item, 1);
        }
    }
    public void addItem(Product product, int quantity) {
        this.items.put(product, this.items.getOrDefault(product, 0) + quantity);
    }
    public Map<Product, Integer> getItems() {
        return this.items;
    }
    public double getTotalPrice() {
        return this.items.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice().doubleValue() * entry.getValue())
                .sum();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Замовлення:\n");

        for (Map.Entry<Product, Integer> entry : this.items.entrySet()) {
            sb.append(entry.getKey().getName())
                    .append(" - Кількість: ").append(entry.getValue())
                    .append(" - Сума: ").append(entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue()))).append(" грн\n");
        }

        sb.append("Загальна сума: ").append(BigDecimal.valueOf(this.getTotalPrice())).append(" грн");
        return sb.toString();
    }
}
