import jakarta.persistence.*;
import model.Product;
import service.CoffeeShopService;

import java.math.BigDecimal;
import util.SpringContextInitializer;

public class Main {
    public static void main(String[] args) {
        CoffeeShopService coffeeShopService = SpringContextInitializer.getBean(CoffeeShopService.class);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("coffeeShopPU");
        EntityManager em = emf.createEntityManager();
        CoffeeShopService service = new CoffeeShopService();

        try {
            Product product1 = new Product("Coffee", BigDecimal.valueOf(5.99), true);
            service.addProduct(em, product1);

            Product product2 = new Product("Treat", BigDecimal.valueOf(3.49), false);
            service.addProduct(em, product2);

        } finally {
            em.close();
            emf.close();
        }
    }
}
