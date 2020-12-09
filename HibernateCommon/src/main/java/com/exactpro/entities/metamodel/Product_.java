package com.exactpro.entities.metamodel;

import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;

import java.math.BigDecimal;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Product.class)
public abstract class Product_ {

	public static volatile SingularAttribute<Product, Integer> productID;
	public static volatile SingularAttribute<Product, BigDecimal> price;
	public static volatile SetAttribute<Product, Deal> deals;
	public static volatile SingularAttribute<Product, String> description;
	public static volatile SingularAttribute<Product, String> productName;

	public static final String PRODUCT_ID = "productID";
	public static final String PRICE = "price";
	public static final String DEALS = "deals";
	public static final String DESCRIPTION = "description";
	public static final String PRODUCT_NAME = "productName";

}

