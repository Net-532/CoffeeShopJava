

package service;

import java.util.List;
import model.Coffee;
import model.Order;
import model.Treat;

public class OrderService {
    public OrderService() {
    }

    public Order createOrder(List<Coffee> coffees, List<Treat> treats) {
        return new Order(coffees, treats);
    }
}
