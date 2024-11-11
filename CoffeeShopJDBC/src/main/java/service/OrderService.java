package service;

import model.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final List<Order> orders = new ArrayList<>();
    private long nextId = 1;

    public List<Order> getAllOrders() {
        return orders;
    }

    public Order getOrderById(Long id) {
        Optional<Order> order = orders.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
        return order.orElse(null);
    }

    public void saveOrder(Order order) {
        order.setId(nextId++);
        orders.add(order);
    }

    public void updateOrder(Order order) {
        orders.replaceAll(o -> o.getId().equals(order.getId()) ? order : o);
    }

    public void deleteOrder(Long id) {
        orders.removeIf(o -> o.getId().equals(id));
    }
}
