SELECT deal_date, customer_id, discount, product_id, price, deal_id
FROM deals
WHERE
    ((:deal_date_before IS NULL OR :deal_date_after IS NULL)
        OR (deal_date >= :deal_date_before AND deal_date <= :deal_date_after )) AND
    (:customer_id IS NULL OR customer_id = :customer_id) AND
    (:discount IS NULL OR discount = :discount) AND
    (:product_id IS NULL OR product_id = :product_id) AND
    ((:min_price IS NULL OR :max_price IS NULL)
        OR (price >= :min_price AND price <= :max_price )) AND
    (:deal_id IS NULL OR deal_id = :deal_id)