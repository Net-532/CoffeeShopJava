
package util;

public class Validator {
    public Validator() {
    }

    public static boolean isValidPrice(double price) {
        return price > 0.0;
    }
}
