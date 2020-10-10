package com.exactpro;


import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

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

    }
}
