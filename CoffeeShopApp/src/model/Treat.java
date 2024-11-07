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
        return String.format("%s (Без глютену: %s) - %.2f грн", getName(), isGlutenFree() ? "так" : "ні", getPrice());
    }
}
