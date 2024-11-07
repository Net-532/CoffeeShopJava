package model;

import java.math.BigDecimal;

public class Treat extends Product {
    private boolean glutenFree;

    public Treat(String name, BigDecimal price, boolean glutenFree) {
        super(name, price);
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
