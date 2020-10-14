package com.exactpro.DAO;

import com.exactpro.entities.Customer;
import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


public class CustomerDAO {

    private static final Logger logger = StaticLogger.infoLogger;

    public static List<Customer> getByName(Session session, String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
        Root<Customer> customer = criteriaQuery.from(Customer.class);

        Predicate predicate = builder.equal(customer.get("name"), name);
        criteriaQuery.select(customer).where(predicate);

        TypedQuery<Customer> query = session.createQuery(criteriaQuery);

        logger.info(String.format("Returned list of customers found by name '%s' ", name));

        return query.getResultList();
    }

    public static List<Customer> getBySurname(Session session, String surname) {
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
        Root<Customer> customer = criteriaQuery.from(Customer.class);

        Predicate predicate = builder.equal(customer.get("surname"), surname);
        criteriaQuery.select(customer).where(predicate);

        TypedQuery<Customer> query = session.createQuery(criteriaQuery);

        logger.info(String.format("Returned list of customers found by surname '%s' ", surname));

        return query.getResultList();
    }

    public static Customer getByID(Session session, int id) {
        return GenericDAO.selectByID(Customer.class, id);
    }

    public static List<Customer> getAllCustomers(Session session) {
        return GenericDAO.gelAllEntities(Customer.class);
    }

    public static List<Customer> getByAge(Session session, short age, ComparisonOperator operator) {


        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
        Root<Customer> customer = criteriaQuery.from(Customer.class);
        Predicate agePredicate;

        switch (operator) {
            case NOT_EQUAL:
                agePredicate = builder.notEqual(customer.get("age"), age);
                break;
            case GREATER_THAN:
                agePredicate = builder.greaterThan(customer.get("age"), age);
                break;
            case GREATER_THAN_OR_EQUAL:
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

        List<Customer> result = query.getResultList();
        logger.info(String.format("Returned list of customers found by age which %s %s ", operator, age));
        return result;
    }

    public static List<Customer> getWhoBoughtProduct(Session session, Integer productID) {
        // TODO: убрать нативную SQL и сделать джоином
        List<Customer> result = session.createNativeQuery(
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
        logger.info(String.format("Returned list of customers who bought product with id %s ", productID));
        return result;

    }

}

