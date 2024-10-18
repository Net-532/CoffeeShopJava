
package menu;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.Coffee;
import model.Order;
import model.Product;
import model.Treat;
import service.CoffeeShopService;

public class MainMenu implements State {
    private CoffeeShopService coffeeShopService;
    private Scanner scanner;

    public MainMenu(CoffeeShopService coffeeShopService) {
        this.coffeeShopService = coffeeShopService;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("=== Головне меню ===");
            System.out.println("1. Показати меню");
            System.out.println("2. Додати нову каву");
            System.out.println("3. Додати новий смаколик");
            System.out.println("4. Видалити продукт");
            System.out.println("5. Оновити продукт");
            System.out.println("6. Замовити");
            System.out.println("7. Вийти");
            choice = this.getValidInt("Ваш вибір: ", 1, 7);
            switch (choice) {
                case 1:
                    this.showMenu();
                    break;
                case 2:
                    this.addNewCoffee();
                    break;
                case 3:
                    this.addNewTreat();
                    break;
                case 4:
                    this.deleteProduct();
                    break;
                case 5:
                    this.updateProduct();
                    break;
                case 6:
                    this.makeOrder();
                    break;
                case 7:
                    System.out.println("Вихід...");
                    break;
                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        } while(choice != 7);

    }

    private void showMenu() {
        System.out.println("=== Меню ===");
        if (this.coffeeShopService.getMenu().isEmpty()) {
            System.out.println("Меню порожнє. Додайте продукти.");
        } else {
            for(int i = 0; i < this.coffeeShopService.getMenu().size(); ++i) {
                Product product = (Product)this.coffeeShopService.getMenu().get(i);
                System.out.printf("%d. %s - %.2f грн\n", i + 1, product.getName(), product.getPrice());
            }
        }

    }

    private void addNewCoffee() {
        String name = this.getValidString("Введіть назву кави: ");
        if (this.coffeeShopService.isProductExists(name)) {
            System.out.println("Продукт з такою назвою вже існує. Спробуйте іншу назву.");
        } else {
            double price = this.getValidDouble("Введіть ціну кави: ");
            String size = this.getValidString("Введіть розмір кави (Small, Medium, Large): ");
            this.coffeeShopService.addProduct(new Coffee(name, price, size));
            System.out.println("Кава успішно додана!");
        }
    }

    private void addNewTreat() {
        String name = this.getValidString("Введіть назву смаколика: ");
        if (this.coffeeShopService.isProductExists(name)) {
            System.out.println("Продукт з такою назвою вже існує. Спробуйте іншу назву.");
        } else {
            double price = this.getValidDouble("Введіть ціну смаколика: ");
            this.coffeeShopService.addProduct(new Treat(name, price));
            System.out.println("Смаколик успішно доданий!");
        }
    }

    private void deleteProduct() {
        int index = this.getValidInt("Введіть номер продукту для видалення: ", 1, this.coffeeShopService.getMenu().size());
        Product product = (Product)this.coffeeShopService.getMenu().get(index - 1);
        boolean result = this.coffeeShopService.deleteProductByName(product.getName());
        if (result) {
            System.out.println("Продукт успішно видалений!");
        } else {
            System.out.println("Не вдалося видалити продукт!");
        }

    }

    private void updateProduct() {
        int index = this.getValidInt("Введіть номер продукту для оновлення: ", 1, this.coffeeShopService.getMenu().size());
        Product product = (Product)this.coffeeShopService.getMenu().get(index - 1);
        double price = this.getValidDouble("Введіть нову ціну: ");
        product.setPrice(price);
        if (product instanceof Coffee) {
            String size = this.getValidString("Введіть новий розмір: ");
            ((Coffee)product).setSize(size);
        }

        System.out.println("Продукт успішно оновлено!");
    }

    private void makeOrder() {
        Order order = new Order();

        int choice;
        do {
            System.out.println("=== Меню для замовлення ===");
            this.showMenu();
            System.out.println("0. Підтвердити замовлення");
            choice = this.getValidInt("Оберіть продукт за номером (або 0 для завершення): ", 0, this.coffeeShopService.getMenu().size());
            if (choice > 0) {
                Product product = (Product)this.coffeeShopService.getMenu().get(choice - 1);
                int quantity = this.getValidInt("Введіть кількість: ", 1, 100);
                order.addItem(product, quantity);
                PrintStream var10000 = System.out;
                String var10001 = product.getName();
                var10000.println("Додано до замовлення: " + var10001 + ", Кількість: " + quantity);
            }
        } while(choice != 0);

        if (!order.getItems().isEmpty()) {
            System.out.println(order);
            System.out.println("Замовлення виконано! Повернення до головного меню.");
        } else {
            System.out.println("Замовлення скасовано.");
        }

    }

    private int getValidInt(String prompt, int min, int max) {
        int input = -1;

        do {
            try {
                System.out.print(prompt);
                input = this.scanner.nextInt();
                if (input < min || input > max) {
                    System.out.println("Введіть значення в діапазоні від " + min + " до " + max);
                }
            } catch (InputMismatchException var6) {
                System.out.println("Невірний формат. Введіть число.");
                this.scanner.next();
            }
        } while(input < min || input > max);

        return input;
    }

    private String getValidString(String prompt) {
        String input = "";

        do {
            try {
                System.out.print(prompt);
                input = this.scanner.next();
            } catch (Exception var4) {
                System.out.println("Невірний формат. Спробуйте ще раз.");
            }
        } while(input.trim().isEmpty());

        return input;
    }

    private double getValidDouble(String prompt) {
        double input = -1.0;

        do {
            try {
                System.out.print(prompt);
                input = this.scanner.nextDouble();
                if (input < 0.0) {
                    System.out.println("Значення повинне бути позитивним.");
                }
            } catch (InputMismatchException var5) {
                System.out.println("Невірний формат. Введіть число.");
                this.scanner.next();
            }
        } while(input < 0.0);

        return input;
    }
}
