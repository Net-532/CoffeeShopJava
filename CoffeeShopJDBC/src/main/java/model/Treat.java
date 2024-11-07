package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "treat")  // Якщо хочете, щоб таблиця мала іншу назву
public class Treat extends Product {
    private boolean glutenFree;

    public Treat(String name, BigDecimal price, boolean glutenFree) {
        super(name, price, glutenFree);
        this.glutenFree = glutenFree;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    @Override
    public String toString() {
        return String.format("%s (Treat, Gluten-Free: %s) - %s", getName(), glutenFree ? "Yes" : "No", getPrice());
    }
}
