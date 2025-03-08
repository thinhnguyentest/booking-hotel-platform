-- Insert dữ liệu vào bảng Roles
INSERT INTO Roles (role_name) VALUES
('customer'),
('admin'),
('business_owner');

-- Insert dữ liệu vào bảng Users
INSERT INTO Users (username, password, email) VALUES
('user1', 'hash1', 'user1@example.com'),
('user2', 'hash2', 'user2@example.com'),
('user3', 'hash3', 'user3@example.com');

-- Insert dữ liệu vào bảng UserRoles
INSERT INTO UserRoles (user_id, role_id) VALUES
(1, 1), -- user1 là customer
(2, 2), -- user2 là admin
(3, 3); -- user3 là business_owner

-- Insert dữ liệu vào bảng Hotels
INSERT INTO Hotels (name, address, city, country, description, owner_id) VALUES
('Hotel A', '123 Main St', 'Hanoi', 'Vietnam', 'A luxurious hotel in the heart of the city.', 3),
('Hotel B', '456 Elm St', 'Ho Chi Minh City', 'Vietnam', 'A cozy hotel with great amenities.', 3);

-- Insert dữ liệu vào bảng Rooms
INSERT INTO Rooms (hotel_id, room_number, room_type, price) VALUES
(1, '101', 'Standard', 100.00),
(1, '102', 'Deluxe', 150.00),
(2, '201', 'Standard', 120.00),
(2, '202', 'Suite', 200.00);

-- Insert dữ liệu vào bảng Bookings
INSERT INTO Bookings (customer_id, room_id, check_in_date, check_out_date, total_price, status) VALUES
(1, 1, '2025-03-10', '2025-03-15', 500.00, 'confirmed'),
(2, 3, '2025-03-12', '2025-03-14', 240.00, 'pending');

-- Insert dữ liệu vào bảng Reviews
INSERT INTO Reviews (hotel_id, customer_id, rating, comment) VALUES
(1, 1, 5, 'Excellent service and comfortable rooms.'),
(2, 2, 4, 'Great location and friendly staff.');

-- Insert dữ liệu vào bảng Payments
INSERT INTO Payments (booking_id, amount, payment_method, payment_status) VALUES
(1, 500.00, 'credit_card', 'completed'),
(2, 240.00, 'paypal', 'pending');

-- Insert dữ liệu vào bảng Images
INSERT INTO Images (hotel_id, room_id, image_url) VALUES
(1, NULL, 'https://example.com/hotel_a.jpg'),
(NULL, 1, 'https://example.com/room_101.jpg'),
(2, NULL, 'https://example.com/hotel_b.jpg'),
(NULL, 3, 'https://example.com/room_201.jpg');
