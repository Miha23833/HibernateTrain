package com.exactpro;


import com.exactpro.DAO.GenericDAO;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
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

        Customer customer = new Customer("ABRA", "CADABRA", (short) 0, 0);
        GenericDAO.insertEntity(customer);

        Product product = new Product();
        product.setProductName("Корарора");
        product.setDescription("Балдёж");
        product.setPrice(new BigDecimal(5000));
        GenericDAO.insertEntity(product);

        for (int i = 0; i < 10; i++) {
            Deal deal = new Deal();
            deal.setDealDate(Date.valueOf(LocalDate.now()));
            deal.setDiscount(new BigDecimal(0));
            deal.setPrice(new BigDecimal(10));
            deal.setCustomer(customer);
            deal.setProduct(product);
            GenericDAO.insertEntity(deal);
        }

        System.out.println(customer.getName());


        System.out.println(customer.getDeals().size());

    }
}
