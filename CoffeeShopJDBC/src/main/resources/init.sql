CREATE DATABASE coffee_shop;

USE coffee_shop;

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    type ENUM('COFFEE', 'TREAT') NOT NULL,
    size ENUM('LARGE', 'MEDIUM', 'XL') NULL,
    gluten_free BOOLEAN NULL
);

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
