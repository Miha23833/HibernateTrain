package com.exactpro.entities.metamodel;

import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Customer.class)
public abstract class Customer_ {

	public static volatile SingularAttribute<Customer, Integer> favouriteProduct;
	public static volatile SingularAttribute<Customer, String> surname;
	public static volatile SetAttribute<Customer, Deal> deals;
	public static volatile SingularAttribute<Customer, Integer> customerID;
	public static volatile SingularAttribute<Customer, String> name;
	public static volatile SingularAttribute<Customer, Short> age;

	public static final String FAVOURITE_PRODUCT = "favouriteProduct";
	public static final String SURNAME = "surname";
	public static final String DEALS = "deals";
	public static final String CUSTOMER_ID = "customerID";
	public static final String NAME = "name";
	public static final String AGE = "age";

}

