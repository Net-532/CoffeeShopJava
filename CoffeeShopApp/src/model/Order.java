

package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Order {
    private Map<Product, Integer> items;

    public Order() {
        this.items = new HashMap();
    }

    public Order(List<Coffee> coffees, List<Treat> treats) {
        this();
        Iterator var3 = coffees.iterator();

        while(var3.hasNext()) {
            Coffee coffee = (Coffee)var3.next();
            this.addItem(coffee, 1);
        }

        var3 = treats.iterator();

        while(var3.hasNext()) {
            Treat treat = (Treat)var3.next();
            this.addItem(treat, 1);
        }

    }

    public void addItem(Product product, int quantity) {
        this.items.put(product, (Integer)this.items.getOrDefault(product, 0) + quantity);
    }

    public Map<Product, Integer> getItems() {
        return this.items;
    }

    public double getTotalPrice() {
        return this.items.entrySet().stream().mapToDouble((entry) -> {
            return ((Product)entry.getKey()).getPrice() * (double)(Integer)entry.getValue();
        }).sum();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Замовлення:\n");
        Iterator var2 = this.items.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<Product, Integer> entry = (Map.Entry)var2.next();
            sb.append(((Product)entry.getKey()).getName()).append(" - Кількість: ").append(entry.getValue()).append(" - Сума: ").append(((Product)entry.getKey()).getPrice() * (double)(Integer)entry.getValue()).append(" грн\n");
        }

        sb.append("Загальна сума: ").append(this.getTotalPrice()).append(" грн");
        return sb.toString();
    }
}
