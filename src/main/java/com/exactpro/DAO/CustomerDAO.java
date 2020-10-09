//package com.exactpro.DAO;
//
//import com.exactpro.entities.Customer;
//import com.exactpro.entities.Deal;
//import com.mysql.cj.Session;
//import org.hibernate.Hibernate;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;
////TODO: сделать логирование
//import java.sql.SQLException;
//import java.util.Collection;
//
//
//public class CustomerDAO {
//    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//
//    public void addCustomer(Customer customer) throws SQLException {
//        sessionFactory.openSession()
//    }
//
//    public void updateCustomer(Long customerID, Customer customer) throws SQLException {
//
//    }
//
//    public Customer getCustomerByID(Long customerID) throws SQLException {
//
//    }
//
//    public Collection gelAllCustomers() throws SQLException {
//
//    }
//
//    public void deleteCustomer(Customer customer) throws SQLException {
//
//    }
//
//    public Customer getCustomerByDeal(Deal deal) throws SQLException {
//
//    }
//
//}
