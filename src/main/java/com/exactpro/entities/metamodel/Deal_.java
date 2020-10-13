package com.exactpro.entities.metamodel;

import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;

@StaticMetamodel(Deal.class)
public abstract class Deal_ {

	public static volatile SingularAttribute<Deal, Product> product;
	public static volatile SingularAttribute<Deal, Integer> dealID;
	public static volatile SingularAttribute<Deal, BigDecimal> price;
	public static volatile SingularAttribute<Deal, Long> dealDate;
	public static volatile SingularAttribute<Deal, BigDecimal> discount;
	public static volatile SingularAttribute<Deal, Customer> customer;

	public static final String PRODUCT = "product";
	public static final String DEAL_ID = "dealID";
	public static final String PRICE = "price";
	public static final String DEAL_DATE = "dealDate";
	public static final String DISCOUNT = "discount";
	public static final String CUSTOMER = "customer";

}

