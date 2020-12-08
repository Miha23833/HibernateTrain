SELECT customer_id as `Customer id`, name as `Name`, surname as `Surname`, age as `Age`, favourite_product as `Favourite product`
FROM customers
WHERE
(:customer_id IS NULL OR customer_id = :customer_id)
AND (:name IS NULL OR name like :name)
AND (:surname IS NULL OR surname = :surname)
AND (:age IS NULL OR age = :age)
AND (:favourite_product IS NULL OR favourite_product = :favourite_product)
