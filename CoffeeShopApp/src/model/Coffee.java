
package model;

public class Coffee extends Product {
    private String size;

    public Coffee(String name, double price, String size) {
        super(name, price);
        this.size = size;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String toString() {
        String var10000 = super.toString();
        return var10000 + ", Розмір: " + this.size;
    }
}
