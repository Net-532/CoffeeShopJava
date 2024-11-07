import jakarta.persistence.*;
import model.Product;
import service.CoffeeShopService;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        // Підключення до EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("coffeeShopPU");
        EntityManager em = emf.createEntityManager();

        // Створення сервісу для маніпуляцій з продуктами
        CoffeeShopService service = new CoffeeShopService();

        try {
            // Викликаємо методи сервісу один за одним
            Product product1 = new Product("Coffee", BigDecimal.valueOf(5.99), true);
            service.addProduct(em, product1);  // Додаємо перший продукт

            Product product2 = new Product("Treat", BigDecimal.valueOf(3.49), false);
            service.addProduct(em, product2);  // Додаємо другий продукт

        } finally {
            em.close();  // Завершуємо EntityManager
            emf.close();  // Завершуємо EntityManagerFactory
        }
    }
}
