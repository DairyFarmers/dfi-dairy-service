--Insert the products to
SELECT
FROM insert_item_details('Panner', '30 Days', 100, 205.56); --1
SELECT
FROM insert_item_details('Curd', '10 Days', 256, 300);
SELECT
FROM insert_item_details('Milk', '02 Days', 654, 700);
SELECT
FROM insert_item_details('Milk Chocolate', '40 Days', 865, 900);
SELECT
FROM insert_item_details('Gee', '30 Days', 564, 600);
SELECT
FROM insert_item_details('Cheese', '100 Days', 568.56, 986.56);
SELECT
FROM insert_item_details('Yogut', '70 Days', 456.35, 753.65); --10


-- Insert the cities for the inventory/ shops

SELECT
FROM insert_location_details('Colombo');--1
SELECT
FROM insert_location_details('Guest Location');
SELECT
FROM insert_location_details('Jaffna');
SELECT
FROM insert_location_details('Anuradhapura');
SELECT
FROM insert_location_details('Gampaha');
SELECT
FROM insert_location_details('Mannar');
SELECT
FROM insert_location_details('Trincomalee');
SELECT
FROM insert_location_details('Kandy');
SELECT
FROM insert_location_details('Batticaloa');
SELECT
FROM insert_location_details('Kurunegala');
SELECT
FROM insert_location_details('Matara'); --10


-- Insert the roles required to do the jobs
SELECT
FROM insert_user_role('Admin'); --1
SELECT
FROM insert_user_role('Inventory-Manager'); --2
SELECT
FROM insert_user_role('Sales-Representative'); --3
SELECT
FROM insert_user_role('Farmer'); --4
SELECT
FROM insert_user_role('B2B Partner'); --5
SELECT
FROM insert_user_role('Normal Customer'); --6


SELECT
FROM insert_user_details('Anuruththan', 'Dass', 1, 'anuruththan@gmail.com', '0779613315', 1, '12345678@Anu');
SELECT
FROM insert_user_details('Tharasan', 'Mass', 2, 'tharsan@gmail.com', '0779613315', 1, '12345678@Tha');
SELECT
FROM insert_user_details('Santhos', 'Redhat', 3, 'santhos@gmail.com', '0779613315', 1, '12345678@San');
SELECT
FROM insert_user_details('Dirusan', 'Sathiya', 4, 'dirusan@gmail.com', '0779613315', 1, '12345678@Diru');
SELECT
FROM insert_user_details('Printhan', 'Psycho', 5, 'printhan@gmail.com', '0779613315', 1, '12345678@Prin');
SELECT
FROM insert_user_details('Guest', 'User', 6, 'GUEST USER', 'UNKNOWN', 2, 'Guest Password');


-- Sample purchase Details
SELECT FROM insert_purchase_details(1, 56.9, 2, 4, 5, 1);
SELECT FROM insert_purchase_details(1, 56.9, 5, 4, 2, 6);
SELECT FROM insert_purchase_details(1, 56.9, 1, 4, 4, 3);
SELECT FROM insert_purchase_details(1, 56.9, 3, 4, 9, 10);
SELECT FROM insert_purchase_details(1, 56.9, 4, 4, 8, 4);
SELECT FROM insert_purchase_details(1, 56.9, 1, 4, 6, 9);
SELECT FROM insert_purchase_details(1, 56.9, 5, 4, 3, 2);
SELECT FROM insert_purchase_details(1, 56.9, 3, 4, 7, 7);
SELECT FROM insert_purchase_details(1, 56.9, 2, 4, 1, 8);
SELECT FROM insert_purchase_details(1, 56.9, 4, 4, 10, 5);


-- Sample Sales Details
SELECT FROM insert_sales_details(1, 8, 7, 89.79, 3, false);
SELECT FROM insert_sales_details(2, 3, 7, 70.18, 3, false);
SELECT FROM insert_sales_details(1, 2, 4, 79.57, 3, false);
SELECT FROM insert_sales_details(5, 3, 3, 80.55, 3, false);
SELECT FROM insert_sales_details(3, 1, 2, 76.60, 3, false);
SELECT FROM insert_sales_details(2, 4, 8, 92.31, 3, false);
SELECT FROM insert_sales_details(4, 1, 4, 84.00, 3, false);
SELECT FROM insert_sales_details(1, 1, 6, 59.28, 3, false);
SELECT FROM insert_sales_details(3, 2, 5, 94.11, 3, false);
SELECT FROM insert_sales_details(5, 2, 10, 85.76, 3, false);