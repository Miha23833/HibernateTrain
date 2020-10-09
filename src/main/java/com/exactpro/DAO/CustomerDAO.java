//package com.exactpro.DAO;
//
//import com.exactpro.entities.Customer;
//import com.exactpro.entities.Deal;
//import org.hibernate.Hibernate;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.cfg.Configuration;
////TODO: сделать логирование
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;
//import java.sql.SQLException;
//import java.util.Collection;
//import java.util.List;
//
//
//public class CustomerDAO {
//    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//
//    public void addCustomer(Customer customer) throws SQLException {
//        try(Session session = sessionFactory.openSession()){
//            Transaction transaction = session.beginTransaction();
//            session.save(customer);
//            transaction.commit();
//        }
//    }
//
//    public void updateCustomer(Customer customer) throws SQLException {
//        try(Session session = sessionFactory.openSession()){
//            Transaction transaction = session.beginTransaction();
//            session.update(customer);
//            transaction.commit();
//        }
//    }
//
//    public Customer getCustomerByID(Long customerID) throws SQLException {
//        try(Session session = sessionFactory.openSession()){
//            return session.load(Customer.class, customerID);
//        }
//    }
//
//    public List<Customer> gelAllCustomers() throws SQLException {
//        try(Session session = sessionFactory.openSession()){
//            CriteriaBuilder builder = session.getCriteriaBuilder();
//            CriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
//            Root<Customer> customerRoot = criteriaQuery.from(Customer.class);
//            criteriaQuery.select(customerRoot);
//            return session.createQuery(criteriaQuery).getResultList();
//        }
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
