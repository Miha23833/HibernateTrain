//package com.exactpro.DAO;
//
//import com.exactpro.entities.Customer;
//import com.exactpro.entities.Deal;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;
////TODO: сделать логирование
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;
//import java.sql.SQLException;
//
//
//public class CustomerDAO {
//
//    public static Customer getCustomerByDeal(Deal deal) throws SQLException {
//        final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//        try(Session session = sessionFactory.openSession()) {
//            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//            CriteriaQuery <Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
//            Root<Customer> root = criteriaQuery.from(Customer.class);
//            criteriaQuery.select(criteriaBuilder.construct(Customer.class, root.get("")));
//            return session;
//        }
//    }
//
//}
