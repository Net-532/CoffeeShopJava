package model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Coffee extends Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Size size;

    // Конструктор без параметрів (необхідно для Hibernate)
    public Coffee() {
        super();  // Викликає конструктор суперкласу без параметрів
    }

    // Конструктор з параметрами
    public Coffee(String name, BigDecimal price, Size size) {
        super(name, price, false);  // Приклад для виклику конструктора суперкласу
        this.name = name;
        this.price = price;
        this.size = size;
    }

    // Геттери та Сеттери
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public enum Size {
        SMALL, MEDIUM, LARGE
    }
}
