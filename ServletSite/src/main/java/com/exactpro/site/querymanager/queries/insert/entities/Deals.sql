INSERT INTO hibernate.deals
    (deal_date, customer_id, discount, product_id, price)
VALUES (UNIX_TIMESTAMP(:deal_date), :customer_id, :discount, :product_id, :price);
