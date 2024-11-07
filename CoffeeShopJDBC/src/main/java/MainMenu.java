import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import model.Coffee;
import model.Order;
import model.Product;
import model.Treat;
import service.CoffeeShopService;

public class MainMenu {
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
        if (coffeeShopService.getMenu().isEmpty()) {
            System.out.println("Меню порожнє. Додайте продукти.");
        } else {
            for (int i = 0; i < coffeeShopService.getMenu().size(); i++) {
                Product product = coffeeShopService.getMenu().get(i);
                System.out.printf("%d. %s\n", i + 1, product);
            }
        }
    }

    private void addNewCoffee() {
        String name = this.getValidString("Введіть назву кави: ");
        if (coffeeShopService.isProductExists(name)) {
            System.out.println("Продукт з такою назвою вже існує. Спробуйте іншу назву.");
        } else {
            BigDecimal price = this.getValidBigDecimal("Введіть ціну кави: ");
            Coffee.CupSize size = this.getValidCupSize();
            coffeeShopService.addProduct(new Coffee(name, price, size));
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
        if (coffeeShopService.isProductExists(name)) {
            System.out.println("Продукт з такою назвою вже існує. Спробуйте іншу назву.");
        } else {
            BigDecimal price = this.getValidBigDecimal("Введіть ціну смаколика: ");
            boolean glutenFree = this.getValidBoolean("Без глютену? (так/ні): ");
            coffeeShopService.addProduct(new Treat(name, price, glutenFree));
            System.out.println("Смаколик успішно доданий!");
        }
    }

    private void deleteProduct() {
        int index = this.getValidInt("Введіть номер продукту для видалення: ", 1, coffeeShopService.getMenu().size()) - 1;
        Optional<Product> product = coffeeShopService.getProductByIndex(index);
        if (product.isPresent() && coffeeShopService.deleteProductByName(product.get().getName())) {
            System.out.println("Продукт успішно видалений!");
        } else {
            System.out.println("Не вдалося видалити продукт!");
        }
    }

    private void updateProduct() {
        int index = this.getValidInt("Введіть номер продукту для оновлення: ", 1, coffeeShopService.getMenu().size()) - 1;
        Optional<Product> product = coffeeShopService.getProductByIndex(index);

        if (product.isPresent()) {
            Product existingProduct = product.get();
            BigDecimal price = this.getValidBigDecimal("Введіть нову ціну: ");
            existingProduct.setPrice(price);

            if (existingProduct instanceof Coffee coffee) {
                coffee.setSize(this.getValidCupSize());
            } else if (existingProduct instanceof Treat treat) {
                boolean glutenFree = this.getValidBoolean("Оновити на без глютену? (так/ні): ");
                treat.setGlutenFree(glutenFree);
            }

            coffeeShopService.updateProduct(index, existingProduct);
            System.out.println("Продукт успішно оновлено!");
        } else {
            System.out.println("Продукт не знайдено.");
        }
    }

    private void makeOrder() {
        Order order = new Order();
        List<Product> selectedProducts = new ArrayList<>();

        int choice;
        do {
            System.out.println("=== Меню для замовлення ===");
            this.showMenu();
            System.out.println("0. Підтвердити замовлення");
            choice = this.getValidInt("Оберіть продукт за номером (або 0 для завершення): ", 0, coffeeShopService.getMenu().size());
            if (choice > 0) {
                Product product = coffeeShopService.getMenu().get(choice - 1);
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

    // Отримуємо булеве значення (так/ні)
    private boolean getValidBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("так") || input.equals("так")) {
                return true;
            } else if (input.equals("ні") || input.equals("ні")) {
                return false;
            } else {
                System.out.println("Будь ласка, введіть 'так' або 'ні'.");
            }
        }
    }

    // Отримуємо ціле число в межах [min, max]
    private int getValidInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Будь ласка, введіть число в межах від " + min + " до " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть коректне ціле число.");
            }
        }
    }

    // Отримуємо непустий рядок
    private String getValidString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Введіть непустий рядок.");
            }
        }
    }

    private BigDecimal getValidBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть коректну ціну (формат: число або число з десятковою точкою).");
            }
        }
    }
}
