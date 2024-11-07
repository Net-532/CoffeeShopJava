
package util;

public class ErrorHandler {
    public ErrorHandler() {
    }

    public static void handleException(Exception e) {
        System.out.println("Сталася помилка: " + e.getMessage());
    }
}
