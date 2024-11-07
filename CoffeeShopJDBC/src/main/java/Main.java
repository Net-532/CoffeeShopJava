import model.Coffee;
import model.Treat;
import service.CoffeeShopService;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        CoffeeShopService coffeeShopService = new CoffeeShopService();

        // Додаємо тестові дані в базу даних
        coffeeShopService.addProduct(new Coffee("Latte", new BigDecimal("3.50"), Coffee.CupSize.MEDIUM));
        coffeeShopService.addProduct(new Treat("Muffin", new BigDecimal("2.00"), true));
        coffeeShopService.addProduct(new Coffee("Espresso", new BigDecimal("2.00"), Coffee.CupSize.LARGE));
        coffeeShopService.addProduct(new Treat("Brownie", new BigDecimal("2.50"), false));

        // Запускаємо головне меню
        MainMenu mainMenu = new MainMenu(coffeeShopService);
        mainMenu.displayMenu();
    }
}
