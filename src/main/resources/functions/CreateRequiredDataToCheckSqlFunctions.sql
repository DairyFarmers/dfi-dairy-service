--Insert the products to
SELECT
FROM insert_item_details('Panner', '30 Days', 100, 205.56, 150); --1
SELECT
FROM insert_item_details('Curd', '10 Days', 256, 300, 280);
SELECT
FROM insert_item_details('Milk', '02 Days', 654, 700, 690);
SELECT
FROM insert_item_details('Milk Chocolate', '40 Days', 865, 900, 880);
SELECT
FROM insert_item_details('Gee', '30 Days', 564, 600, 580);
SELECT
FROM insert_item_details('Cheese', '100 Days', 568.56, 986.56, 700);
SELECT
FROM insert_item_details('Yogut', '70 Days', 456.35, 753.65, 600); --10


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
SELECT FROM insert_sales_details(1, 8, 7, 89.79, 5, true);
SELECT FROM insert_sales_details(2, 3, 7, 70.18, 5, true);
SELECT FROM insert_sales_details(1, 2, 4, 79.57, 5, true);
SELECT FROM insert_sales_details(5, 3, 3, 80.55, 5, true);
SELECT FROM insert_sales_details(3, 1, 2, 76.60, 5, true);
SELECT FROM insert_sales_details(2, 4, 8, 92.31, 5, true);
SELECT FROM insert_sales_details(4, 1, 4, 84.00, 5, true);
SELECT FROM insert_sales_details(1, 1, 6, 59.28, 5, true);
SELECT FROM insert_sales_details(3, 2, 5, 94.11, 5, true);
SELECT FROM insert_sales_details(5, 2, 10, 85.76, 5, true);

----------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- B2B Status Details
INSERT INTO "B2bRequestStatus"("B2bRequestStatusName")
VALUES ('Request can be processed by the sellers in the same location'); ---------------------------------------------------------------------------1
INSERT INTO "B2bRequestStatus"("B2bRequestStatusName")
VALUES ('Request can not be processed low price quotation');----------------------------------------------------------------------------------------2
INSERT INTO "B2bRequestStatus"("B2bRequestStatusName")
VALUES ('Request can be processed by getting from our organization distributor'); ------------------------------------------------------------------3
INSERT INTO "B2bRequestStatus"("B2bRequestStatusName")
VALUES ('Request Completed'); ----------------------------------------------------------------------------------------------------------------------4
INSERT INTO "B2bRequestStatus"("B2bRequestStatusName")
VALUES ('Request Distributed to the farmers under the organization'); ------------------------------------------------------------------------------5
INSERT INTO "B2bRequestStatus"("B2bRequestStatusName")
VALUES ('Request can not be done by the organization'); --------------------------------------------------------------------------------------------6
INSERT INTO "B2bRequestStatus"("B2bRequestStatusName")
VALUES ('Request is processing'); ------------------------------------------------------------------------------------------------------------------7
INSERT INTO "B2bRequestStatus"("B2bRequestStatusName")
VALUES ('Request is cancelled by B2b'); ------------------------------------------------------------------------------------------------------------8
----------------------------------------------------------------------------------------------------------------------------------------------------------------------