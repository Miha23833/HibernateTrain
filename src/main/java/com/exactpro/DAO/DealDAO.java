package com.exactpro.DAO;

import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import com.exactpro.entities.metamodel.Customer_;
import com.exactpro.entities.metamodel.Deal_;
import com.exactpro.entities.metamodel.Product_;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

//TODO: раз уж я знаю как делать метамодели - нужно заняться и заменой SQL на нормальный типизированный код. УРА!

public class DealDAO {

    public static List<Deal> getByDate(Long date, ComparisonOperator operator){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Deal> criteriaQuery = builder.createQuery(Deal.class);
            Root<Deal> deal = criteriaQuery.from(Deal.class);
            Predicate datePredicate;

            Path<Long> datePath = deal.get("dealDate");

            switch (operator){
                case NOT_EQUAL:
                    datePredicate = builder.notEqual(datePath, date);
                    break;
                case GREATHER_THAN:
                    datePredicate = builder.greaterThan(datePath, date);
                    break;
                case GREATHER_THAN_OR_EQUAL:
                    datePredicate = builder.greaterThanOrEqualTo(datePath, date);
                    break;
                case LESS_THAN:
                    datePredicate = builder.lessThan(datePath, date);
                    break;
                case LESS_THAN_OR_EQUAL:
                    datePredicate = builder.lessThanOrEqualTo(datePath, date);
                    break;
                default:
                    datePredicate = builder.equal(datePath, date);
            }
            criteriaQuery.select(deal).where(datePredicate);

            TypedQuery<Deal> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }

    public static List<Deal> getByDiscount(BigDecimal discount, ComparisonOperator operator){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Deal> criteriaQuery = builder.createQuery(Deal.class);
            Root<Deal> deal = criteriaQuery.from(Deal.class);

            Predicate predicate;
            switch (operator){
                case NOT_EQUAL:
                    predicate = builder.notEqual(deal.get("discount"), discount);
                    break;
                case GREATHER_THAN:
                    predicate = builder.greaterThan(deal.get("discount"), discount);
                    break;
                case GREATHER_THAN_OR_EQUAL:
                    predicate = builder.greaterThanOrEqualTo(deal.get("discount"), discount);
                    break;
                case LESS_THAN:
                    predicate = builder.lessThan(deal.get("discount"), discount);
                    break;
                case LESS_THAN_OR_EQUAL:
                    predicate = builder.lessThanOrEqualTo(deal.get("discount"), discount);
                    break;
                default:
                    predicate = builder.equal(deal.get("discount"), discount);
            }

            criteriaQuery.select(deal).where(predicate);
            TypedQuery<Deal> query = session.createQuery(criteriaQuery);

            return query.getResultList();
        }
    }

    public static List<Deal> getByPrice(BigDecimal price, ComparisonOperator operator){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Deal> criteriaQuery = builder.createQuery(Deal.class);
            Root<Deal> deal = criteriaQuery.from(Deal.class);

            Predicate predicate;
            switch (operator){
                case NOT_EQUAL:
                    predicate = builder.notEqual(deal.get("price"), price);
                    break;
                case GREATHER_THAN:
                    predicate = builder.greaterThan(deal.get("price"), price);
                    break;
                case GREATHER_THAN_OR_EQUAL:
                    predicate = builder.greaterThanOrEqualTo(deal.get("price"), price);
                    break;
                case LESS_THAN:
                    predicate = builder.lessThan(deal.get("price"), price);
                    break;
                case LESS_THAN_OR_EQUAL:
                    predicate = builder.lessThanOrEqualTo(deal.get("price"), price);
                    break;
                default:
                    predicate = builder.equal(deal.get("price"), price);
            }

            criteriaQuery.select(deal).where(predicate);
            TypedQuery<Deal> query = session.createQuery(criteriaQuery);

            return query.getResultList();
        }
    }

    public static List<Deal> getByCustomerID(Integer customerID){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Deal> criteriaQuery = builder.createQuery(Deal.class);
            Root<Deal> deal = criteriaQuery.from(Deal.class);
            Join<Deal, Customer> customerJoin = deal.join(Deal_.CUSTOMER);

            Predicate predicate = builder.equal(customerJoin.get(Customer_.CUSTOMER_ID), customerID);

            criteriaQuery.select(deal).where(predicate);
            TypedQuery<Deal> query = session.createQuery(criteriaQuery);

            return query.getResultList();
        }
    }

    public static List<Deal> getByProductID(Integer productID){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Deal> criteriaQuery = builder.createQuery(Deal.class);
            Root<Deal> deal = criteriaQuery.from(Deal.class);
            Join<Deal, Product> productJoin = deal.join(Deal_.PRODUCT);

            Predicate predicate = builder.equal(productJoin.get(Product_.PRODUCT_ID), productID);
            criteriaQuery.select(deal).where(predicate);
            TypedQuery<Deal> query = session.createQuery(criteriaQuery);

            return query.getResultList();
        }
    }

    public static List<Deal> getByPeriod(Long startRange, Long endRange){

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Deal> criteriaQuery = builder.createQuery(Deal.class);
            Root<Deal> deal = criteriaQuery.from(Deal.class);

            Predicate predicate = builder.between(deal.get(Deal_.DEAL_DATE), startRange, endRange);

            criteriaQuery.select(deal).where(predicate);
            TypedQuery<Deal> query = session.createQuery(criteriaQuery);

            return query.getResultList();
        }
    }

}
