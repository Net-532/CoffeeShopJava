package service;
import model.Product;
import java.util.ArrayList;
import java.util.List;

public class CoffeeShopService {
    private List<Product> menu;
    public CoffeeShopService() {
        this.menu = new ArrayList<>();
    }
    public List<Product> getMenu() {
        return menu;
    }
    public boolean addProduct(Product product) {
        if (isProductExists(product.getName())) {
            return false; // Продукт з такою назвою вже існує
        }
        menu.add(product);
        return true;
    }
    public boolean isProductExists(String productName) {
        for (Product product : menu) {
            if (product.getName().equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
    }
    public boolean deleteProductByName(String name) {
        return menu.removeIf(product -> product.getName().equalsIgnoreCase(name));
    }
}
