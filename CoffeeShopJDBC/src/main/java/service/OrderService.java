package service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Order;
import model.Product;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public Order createOrder(EntityManager em, List<Product> products) {
        EntityTransaction transaction = em.getTransaction();
        Order order = new Order();
        order.addItems(products);

        try {
            transaction.begin();
            em.persist(order);
            transaction.commit();
            System.out.println("Order created successfully.");
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        }
        return order;
    }

    public List<Order> getAllOrders(EntityManager em) {
        return em.createQuery("SELECT o FROM Order o", Order.class).getResultList();
    }
}
