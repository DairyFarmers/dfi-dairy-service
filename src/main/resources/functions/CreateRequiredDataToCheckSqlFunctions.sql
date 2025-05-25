--Insert the products to
SELECT FROM insert_item_details('Panner','30 Days');
SELECT FROM insert_item_details('Curd','10 Days');
SELECT FROM insert_item_details('Milk','02 Days');
SELECT FROM insert_item_details('Milk Chocolate','40 Days');
SELECT FROM insert_item_details('Gee','30 Days');
SELECT FROM insert_item_details('Cheese','100 Days');
SELECT FROM insert_item_details('Yogut','70 Days');


-- Insert the cities for the inventory/ shops
SELECT FROM insert_location_details('Colombo');
SELECT FROM insert_location_details('Jaffna');
SELECT FROM insert_location_details('Anuradhapura');
SELECT FROM insert_location_details('Gampaha');
SELECT FROM insert_location_details('Mannar');
SELECT FROM insert_location_details('Trincomalee');
SELECT FROM insert_location_details('Kandy');
SELECT FROM insert_location_details('Batticaloa');
SELECT FROM insert_location_details('Kurunegala');
SELECT FROM insert_location_details('Matara');


-- Insert the roles required to do the jobs
SELECT FROM insert_user_role('Supper-Admin');
SELECT FROM insert_user_role('Inventory-Manager');
SELECT FROM insert_user_role('Sales-Representative');
SELECT FROM insert_user_role('Farmer');
SELECT FROM insert_user_role('B2B Partner');



SELECT FROM insert_user_details('Anuruththan', 'Baskaran', 1, 'anuruththan@gmail.com', '0779613315', 1, '12345678@Anu');
SELECT FROM insert_user_details('Anuruththan', 'Baskaran', 2, 'tharsan@gmail.com', '0779613315', 1, '12345678@Tha');
SELECT FROM insert_user_details('Anuruththan', 'Baskaran', 3, 'santhos@gmail.com', '0779613315', 1, '12345678@San');
SELECT FROM insert_user_details('Anuruththan', 'Baskaran', 4, 'dirusan@gmail.com', '0779613315', 1, '12345678@Diru');
SELECT FROM insert_user_details('Anuruththan', 'Baskaran', 4, 'printhan@gmail.com', '0779613315', 1, '12345678@Prin');