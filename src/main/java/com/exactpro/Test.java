package com.exactpro;


import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.exactpro.entities.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


class Factory{
    private static Customer customer;
    private static Deal deal;
    private static Product product;
    private static Factory instance;

    public static synchronized Factory getInstance(){
        if(instance == null)
            instance = new Factory();
        return instance;
    }
}


public class Test {

    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public static void main(String[] args) {

        List<Customer> customers = new ArrayList<>();
        Customer customer = new Customer();
        customer.setName("M");
        customer.setSurname("K");
        customer.setAge((short) 15);
        customers.add(customer);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setPrice(new BigDecimal(2));
        product.setProductName("Жвачка по рублу");
        product.setDescription("Да");
        products.add(product);

        Deal deal = new Deal();
        deal.setDealDate(new Date(System.currentTimeMillis()));
        deal.setCustomerID(customers);
        deal.setProductID(products);
        deal.setDiscount(new BigDecimal(0));
        deal.setPrice(new BigDecimal("2"));

        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(product);
            transaction.commit();
        }

    }
}
