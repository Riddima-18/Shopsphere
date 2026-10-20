INSERT INTO categories (name, description) VALUES
('Electronics', 'Gadgets and devices'),
('Clothing', 'Apparel and fashion');

INSERT INTO products (category_id, name, brand, description, price, discount, stock, image_url) VALUES
(1, 'Wireless Earbuds', 'SoundX', 'Bluetooth 5.3 earbuds', 1999.00, 10, 25, 'images/earbuds.jpg'),
(1, 'Smart Watch', 'TimeTech', 'Fitness tracking watch', 3499.00, 15, 10, 'images/watch.jpg'),
(2, 'Cotton T-Shirt', 'StyleCo', 'Plain cotton tee', 499.00, 5, 50, 'images/tshirt.jpg');s