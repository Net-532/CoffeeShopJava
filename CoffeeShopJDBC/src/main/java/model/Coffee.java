package model;

import java.math.BigDecimal;

public class Coffee extends Product {
    public enum CupSize { SMALL, MEDIUM, LARGE }

    private CupSize size;

    public Coffee(String name, BigDecimal price, CupSize size) {
        super(name, price);
        this.size = size;
    }

    public CupSize getSize() {
        return size;
    }

    public void setSize(CupSize size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return String.format("%s (Coffee, Size: %s) - %s", getName(), size, getPrice());
    }
}
