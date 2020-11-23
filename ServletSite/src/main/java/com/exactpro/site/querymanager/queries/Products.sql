SELECT product_name, description, price, product_id
FROM products
WHERE
    (:product_name IS NULL OR product_name = :product_name)
    AND (:description IS NULL OR description LIKE CONCAT('%',:description,'%'))
    AND (:price IS NULL OR price = :price)
    AND (:product_id IS NULL OR product_id = :product_id)