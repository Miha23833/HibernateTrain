SELECT customer_id, name, surname, age, favourite_product
FROM customers
WHERE
(:customer_id IS NULL OR customer_id = :customer_id)
AND (:name IS NULL OR name like :name)
AND (:surname IS NULL OR surname = :surname)
AND (:age IS NULL OR age = :age)
AND (:favourite_product IS NULL OR favourite_product = :favourite_product)
