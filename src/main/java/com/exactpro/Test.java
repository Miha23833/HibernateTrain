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

        Customer customer = new Customer();
        customer.setAge((short) 20);
        customer.setName("Mikhail");
        customer.setSurname("Korepanov");

        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(customer);
            transaction.commit();
        }

    }
}
