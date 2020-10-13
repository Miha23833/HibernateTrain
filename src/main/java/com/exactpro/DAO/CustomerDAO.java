package com.exactpro.DAO;

import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
//TODO: сделать логирование
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CustomerDAO {

    public static List<Customer> getByName(String name) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
            Root<Customer> customer = criteriaQuery.from(Customer.class);

            Predicate predicate = builder.equal(customer.get("name"), name);
            criteriaQuery.select(customer).where(predicate);

            TypedQuery<Customer> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }

    public static List<Customer> getBySurname(String surname) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
            Root<Customer> customer = criteriaQuery.from(Customer.class);

            Predicate predicate = builder.equal(customer.get("surname"), surname);
            criteriaQuery.select(customer).where(predicate);

            TypedQuery<Customer> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }

    public static Customer getByID(int id){
        return GenericDAO.selectByID(Customer.class, id);
    }

    public static List<Customer> getAllCustomers(){
        return GenericDAO.gelAllEntities(Customer.class);
    }

    public static List<Customer> getByAge(short age, ComparisonOperator operator){

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
            Root<Customer> customer = criteriaQuery.from(Customer.class);
            Predicate agePredicate;

            switch (operator){
                case NOT_EQUAL:
                    agePredicate = builder.notEqual(customer.get("age"), age);
                    break;
                case GREATHER_THAN:
                    agePredicate = builder.greaterThan(customer.get("age"), age);
                    break;
                case GREATHER_THAN_OR_EQUAL:
                    agePredicate = builder.greaterThanOrEqualTo(customer.get("age"), age);
                    break;
                case LESS_THAN:
                    agePredicate = builder.lessThan(customer.get("age"), age);
                    break;
                case LESS_THAN_OR_EQUAL:
                    agePredicate = builder.lessThanOrEqualTo(customer.get("age"), age);
                    break;
                default:
                    agePredicate = builder.equal(customer.get("age"), age);
            }
            criteriaQuery.select(customer).where(agePredicate);

            TypedQuery<Customer> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }

    public static List<Customer> getWhoBoughtProduct(Integer productID){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery(
                    "SELECT DISTINCT\n" +
                            "customers.*\n" +
                        "FROM customers\n" +
                        "JOIN deals\n" +
                            "USING(customer_id)\n" +
                        "WHERE deals.product_id = ?\n" +
                            "GROUP BY 1,2,3,4,5",
                    Customer.class
            ).setParameter(1, productID)
             .list();
        }
    }

}

