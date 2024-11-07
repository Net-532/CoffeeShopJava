import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Coffee;
import model.Product;
import model.Treat;
import service.CoffeeShopService;
import service.OrderService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("coffeeShopPU");
    private final CoffeeShopService coffeeShopService;
    private final OrderService orderService;

    public MainMenu() {
        coffeeShopService = new CoffeeShopService();
        orderService = new OrderService();
    }

    public void displayMenu() {
        try (Scanner scanner = new Scanner(System.in); EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            while (true) {
                System.out.println("=== Головне меню ===");
                System.out.println("1. Показати меню");
                System.out.println("2. Додати нову каву");
                System.out.println("3. Додати новий смаколик");
                System.out.println("4. Видалити продукт");
                System.out.println("5. Оновити продукт");
                System.out.println("6. Замовити");
                System.out.println("7. Вийти");
                System.out.print("Ваш вибір: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // очистка буфера

                switch (choice) {
                    case 1 -> showMenu(em);
                    case 2 -> addNewCoffee(em, scanner);
                    case 3 -> addNewTreat(em, scanner);
                    case 4 -> deleteProduct(em, scanner);
                    case 5 -> updateProduct(em, scanner);
                    case 6 -> placeOrder(em, scanner);
                    case 7 -> {
                        ENTITY_MANAGER_FACTORY.close();
                        return;
                    }
                    default -> System.out.println("Невірний вибір.");
                }
            }
        }
    }

    private void showMenu(EntityManager em) {
        List<Product> menu = coffeeShopService.loadMenu(em);
        if (menu.isEmpty()) {
            System.out.println("Меню порожнє.");
        } else {
            for (Product product : menu) {
                System.out.println(product);
            }
        }
    }

    private void addNewCoffee(EntityManager em, Scanner scanner) {
        System.out.print("Введіть назву кави: ");
        String name = scanner.nextLine();
        System.out.print("Введіть ціну кави: ");
        BigDecimal price = scanner.nextBigDecimal();
        System.out.print("Оберіть розмір кави (1 - SMALL, 2 - MEDIUM, 3 - LARGE): ");
        int sizeChoice = scanner.nextInt();
        Coffee.Size size = switch (sizeChoice) {
            case 1 -> Coffee.Size.SMALL;
            case 2 -> Coffee.Size.MEDIUM;
            case 3 -> Coffee.Size.LARGE;
            default -> throw new IllegalArgumentException("Невірний розмір.");
        };

        // Створюємо об'єкт Coffee, а не Product
        Coffee coffee = new Coffee(name, price, size);
        coffeeShopService.addProduct(em, coffee);
    }


    private void addNewTreat(EntityManager em, Scanner scanner) {
        System.out.print("Введіть назву смаколика: ");
        String name = scanner.nextLine();
        System.out.print("Введіть ціну смаколика: ");
        BigDecimal price = scanner.nextBigDecimal();
        System.out.print("Глютен-фрі? (1 - так, 0 - ні): ");
        boolean glutenFree = scanner.nextInt() == 1;

        // Створюємо об'єкт Treat
        Treat treat = new Treat(name, price, glutenFree);
        coffeeShopService.addProduct(em, treat);
    }

    private void deleteProduct(EntityManager em, Scanner scanner) {
        System.out.print("Введіть назву продукту для видалення: ");
        String name = scanner.nextLine();
        if (coffeeShopService.deleteProductByName(em, name)) {
            System.out.println("Продукт видалено.");
        } else {
            System.out.println("Продукт не знайдено.");
        }
    }

    private void updateProduct(EntityManager em, Scanner scanner) {
        System.out.print("Введіть назву продукту для оновлення: ");
        String name = scanner.nextLine();
        List<Product> menu = coffeeShopService.loadMenu(em);
        Product product = menu.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);

        if (product == null) {
            System.out.println("Продукт не знайдено.");
            return;
        }

        System.out.print("Введіть нову ціну: ");
        BigDecimal price = scanner.nextBigDecimal();
        product.setPrice(price);

        if (product instanceof Treat treat) {
            System.out.print("Глютен-фрі? (1 - так, 0 - ні): ");
            treat.setGlutenFree(scanner.nextInt() == 1);
        }
        coffeeShopService.updateProduct(em, product);
    }

    private void placeOrder(EntityManager em, Scanner scanner) {
        List<Product> menu = coffeeShopService.loadMenu(em);
        List<Product> selectedProducts = new ArrayList<>();

        System.out.println("Оберіть продукти для замовлення (введіть номер або 0 для завершення):");
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i));
        }

        while (true) {
            System.out.print("Номер продукту: ");
            int choice = scanner.nextInt();
            if (choice == 0) break;
            if (choice > 0 && choice <= menu.size()) {
                selectedProducts.add(menu.get(choice - 1));
            } else {
                System.out.println("Невірний номер продукту.");
            }
        }

        if (!selectedProducts.isEmpty()) {
            orderService.createOrder(em, selectedProducts);
        } else {
            System.out.println("Замовлення не створено.");
        }
    }
}
