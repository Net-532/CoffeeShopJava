
package menu;

import model.Coffee;
import model.Treat;
import service.CoffeeShopService;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        CoffeeShopService coffeeShopService = new CoffeeShopService();
        coffeeShopService.addProduct(new Coffee("Espresso", 50.0, "Small"));
        coffeeShopService.addProduct(new Coffee("Latte", 70.0, "Medium"));
        coffeeShopService.addProduct(new Treat("Croissant", 30.0));
        MainMenu menu = new MainMenu(coffeeShopService);
        menu.displayMenu();
    }
}
