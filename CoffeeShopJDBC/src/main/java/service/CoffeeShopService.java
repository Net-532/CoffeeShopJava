package service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Product;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CoffeeShopService {

    public List<Product> loadMenu(EntityManager em) {
        return em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }

    public boolean addProduct(EntityManager em, Product product) {
        EntityTransaction transaction = em.getTransaction();

        try {
            // Перевірка, чи не активна вже транзакція
            if (!transaction.isActive()) {
                transaction.begin();  // Починаємо нову транзакцію
            }

            em.persist(product);
            transaction.commit();  // Завершаємо транзакцію
            System.out.println("Product added successfully.");
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();  // Откочується зміни при помилці
            }
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProductByName(EntityManager em, String name) {
        EntityTransaction transaction = em.getTransaction();
        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }

            Product product = em.createQuery("SELECT p FROM Product p WHERE LOWER(p.name) = LOWER(:name)", Product.class)
                    .setParameter("name", name)
                    .getSingleResult();
            em.remove(product);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(EntityManager em, Product updatedProduct) {
        EntityTransaction transaction = em.getTransaction();

        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
            em.merge(updatedProduct);
            transaction.commit();
            System.out.println("Product updated successfully.");
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}
