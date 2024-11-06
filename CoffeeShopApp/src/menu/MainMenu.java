package menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
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
                case 1 -> this.showMenu();
                case 2 -> this.addNewCoffee();
                case 3 -> this.addNewTreat();
                case 4 -> this.deleteProduct();
                case 5 -> this.updateProduct();
                case 6 -> this.makeOrder();
                case 7 -> System.out.println("Вихід...");
                default -> System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        } while (choice != 7);
    }

    private void showMenu() {
        System.out.println("=== Меню ===");
        if (this.coffeeShopService.getMenu().isEmpty()) {
            System.out.println("Меню порожнє. Додайте продукти.");
        } else {
            for (int i = 0; i < this.coffeeShopService.getMenu().size(); i++) {
                Product product = this.coffeeShopService.getMenu().get(i);
                System.out.printf("%d. %s\n", i + 1, product);

            }
        }
    }

    private void addNewCoffee() {
        String name = this.getValidString("Введіть назву кави: ");
        if (this.coffeeShopService.isProductExists(name)) {
            System.out.println("Продукт з такою назвою вже існує. Спробуйте іншу назву.");
        } else {
            BigDecimal price = this.getValidBigDecimal("Введіть ціну кави: ");
            Coffee.CupSize size = this.getValidCupSize(); // Використання правильного типу
            this.coffeeShopService.addProduct(new Coffee(name, price, size));
            System.out.println("Кава успішно додана!");
        }
    }

    private Coffee.CupSize getValidCupSize() {
        System.out.println("Оберіть розмір кави:");
        for (int i = 0; i < Coffee.CupSize.values().length; i++) {
            System.out.printf("%d. %s\n", i + 1, Coffee.CupSize.values()[i]);
        }
        int choice = this.getValidInt("Ваш вибір: ", 1, Coffee.CupSize.values().length);
        return Coffee.CupSize.values()[choice - 1];
    }

    private void addNewTreat() {
        String name = this.getValidString("Введіть назву смаколика: ");
        if (this.coffeeShopService.isProductExists(name)) {
            System.out.println("Продукт з такою назвою вже існує. Спробуйте іншу назву.");
        } else {
            BigDecimal price = this.getValidBigDecimal("Введіть ціну смаколика: ");
            boolean glutenFree = this.getValidBoolean("Без глютену? (так/ні): ");
            this.coffeeShopService.addProduct(new Treat(name, price, glutenFree));
            System.out.println("Смаколик успішно доданий!");
        }
    }

    private void deleteProduct() {
        int index = this.getValidInt("Введіть номер продукту для видалення: ", 1, this.coffeeShopService.getMenu().size());
        Product product = this.coffeeShopService.getMenu().get(index - 1);
        boolean result = this.coffeeShopService.deleteProductByName(product.getName());
        System.out.println(result ? "Продукт успішно видалений!" : "Не вдалося видалити продукт!");
    }

    private void updateProduct() {
        int index = this.getValidInt("Введіть номер продукту для оновлення: ", 1, this.coffeeShopService.getMenu().size());
        Product product = this.coffeeShopService.getMenu().get(index - 1);
        BigDecimal price = this.getValidBigDecimal("Введіть нову ціну: ");
        product.setPrice(price);
        if (product instanceof Coffee coffee) {
            coffee.setSize(this.getValidCupSize());
        } else if (product instanceof Treat treat) {
            boolean glutenFree = this.getValidBoolean("Оновити на без глютену? (так/ні): ");
            treat.setGlutenFree(glutenFree);
        }
        System.out.println("Продукт успішно оновлено!");
    }

    private void makeOrder() {
        Order order = new Order();
        List<Product> selectedProducts = new ArrayList<>();

        int choice;
        do {
            System.out.println("=== Меню для замовлення ===");
            this.showMenu();
            System.out.println("0. Підтвердити замовлення");
            choice = this.getValidInt("Оберіть продукт за номером (або 0 для завершення): ", 0, this.coffeeShopService.getMenu().size());
            if (choice > 0) {
                Product product = this.coffeeShopService.getMenu().get(choice - 1);
                int quantity = this.getValidInt("Введіть кількість: ", 1, 100);
                for (int i = 0; i < quantity; i++) {
                    selectedProducts.add(product);
                }
                System.out.printf("Додано до замовлення: %s, Кількість: %d\n", product.getName(), quantity);
            }
        } while (choice != 0);

        if (!selectedProducts.isEmpty()) {
            order.addItems(selectedProducts);
            System.out.println(order);
            System.out.println("Замовлення виконано! Повернення до головного меню.");
        } else {
            System.out.println("Замовлення скасовано.");
        }
    }

    private boolean getValidBoolean(String prompt) {
        String input;
        do {
            input = this.getValidString(prompt).toLowerCase();
            if (input.equals("так")) return true;
            if (input.equals("ні")) return false;
            System.out.println("Невірний вибір. Введіть 'так' або 'ні'.");
        } while (true);
    }

    private int getValidInt(String prompt, int min, int max) {
        int input = -1;
        do {
            try {
                System.out.print(prompt);
                input = scanner.nextInt();
                if (input < min || input > max) {
                    System.out.println("Введіть значення в діапазоні від " + min + " до " + max);
                }
            } catch (InputMismatchException e) {
                System.out.println("Невірний формат. Введіть число.");
                scanner.next();
            }
        } while (input < min || input > max);
        return input;
    }

    private String getValidString(String prompt) {
        System.out.print(prompt);
        scanner.nextLine(); // Очистка буфера
        return scanner.nextLine().trim();
    }

    private BigDecimal getValidBigDecimal(String prompt) {
        BigDecimal input = BigDecimal.ZERO;
        do {
            try {
                System.out.print(prompt);
                input = new BigDecimal(scanner.next());
                if (input.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Значення повинне бути позитивним.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Невірний формат. Введіть числове значення.");
                scanner.next();
            }
        } while (input.compareTo(BigDecimal.ZERO) <= 0);
        return input;
    }
}
