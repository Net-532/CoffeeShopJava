package model;
import java.math.BigDecimal;
public class Coffee extends Product {
    private CupSize size;
    public Coffee(String name, BigDecimal price, CupSize size) {
        super(name, price);
        this.size = size;
    }
    public CupSize getSize() {
        return this.size;
    }
    public void setSize(CupSize size) {
        this.size = size;
    }
    @Override
    public String toString() {
        return String.format("%s (Розмір: %s) - %.2f грн", getName(), getSize(), getPrice());
    }
    public enum CupSize {
        LARGE,
        MEDIUM,
        XL
    }
}
