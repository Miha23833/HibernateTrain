SELECT FROM_UNIXTIME(deal_date, ' %D %M %Y %h:%i:%s') AS `Date of deal`
     , customer_id `Customer`
     , discount `Discount`
     , product_id `Product ID`
     , price `Price`
     , deal_id `Deal ID`
FROM deals
WHERE
    (:deal_date_before IS NULL OR deal_date >= UNIX_TIMESTAMP(STR_TO_DATE(:deal_date_before, '%Y-%m-%d'))) AND
    (:deal_date_after IS NULL OR deal_date <= UNIX_TIMESTAMP(STR_TO_DATE(:deal_date_after, '%Y-%m-%d')) ) AND
    (:customer_id IS NULL OR customer_id = :customer_id) AND
    (:discount IS NULL OR discount = :discount) AND
    (:product_id IS NULL OR product_id = :product_id) AND
    ((:min_price IS NULL OR :max_price IS NULL)
        OR (price >= :min_price AND price <= :max_price )) AND
    (:deal_id IS NULL OR deal_id = :deal_id)