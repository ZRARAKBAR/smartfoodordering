-- 1. Create the database
CREATE DATABASE IF NOT EXISTS food_order_system;
USE food_order_system;

-- 2. Admins Table
CREATE TABLE IF NOT EXISTS admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- 3. Categories Table
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- 4. Menu Items Table
CREATE TABLE IF NOT EXISTS menu_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category_id INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    image_path VARCHAR(255),
    FOREIGN KEY (category_id) REFERENCES categories(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 5. Orders Table
CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('Pending', 'Completed') DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Order Items Table
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id)
        ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
        ON DELETE CASCADE
);

-- 7. Sample Data for Categories (insert BEFORE menu_items)
INSERT INTO categories (name) VALUES ('Pizza'), ('Burger'), ('Drinks'), ('Bar BQ');

-- 8. Sample Data for Menu Items (ensure matching category_id)
-- Check actual IDs from categories
SELECT * FROM categories;

-- Let's assume Pizza = 1, Burger = 2, Drinks = 3, Bar BQ = 4
INSERT INTO menu_items (name, category_id, price, image_path) VALUES
('Zinger Burger', 2, 350, 'assets/zinger.png'),
('Sprite', 3, 80, 'assets/sprite.png'),
('Chicken Tikka', 4, 400, 'assets/tikka.png'),
('Pizza Fajita', 1, 500, 'assets/fajita.png');

-- ✅ View Data
SELECT * FROM admins;
SELECT * FROM categories;
SELECT * FROM menu_items;
SELECT * FROM orders;
SELECT * FROM order_items;


