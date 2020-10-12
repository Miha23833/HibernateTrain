package com.exactpro.DAO;

import com.exactpro.entities.Deal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class DealDAO {

    public static List<Deal> getByDate(Date date, ComparisonOperator operator){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Deal> criteriaQuery = builder.createQuery(Deal.class);
            Root<Deal> deal = criteriaQuery.from(Deal.class);
            Predicate datePredicate;

            switch (operator){
                case NOT_EQUAL:
                    datePredicate = builder.notEqual(deal.get("dealDate"), date);
                    break;
                case GREATHER_THAN:
                case GREATHER_THAN_OR_EQUAL:
                    datePredicate = builder.greaterThanOrEqualTo(deal.get("dealDate"), date);
                    break;
                case LESS_THAN:
                case LESS_THAN_OR_EQUAL:
                    datePredicate = builder.lessThanOrEqualTo(deal.get("dealDate"), date);
                    break;
                default:
                    datePredicate = builder.equal(deal.get("dealDate"), date);
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
                    predicate = builder.gt(deal.get("discount"), discount);
                    break;
                case GREATHER_THAN_OR_EQUAL:
                    predicate = builder.greaterThanOrEqualTo(deal.get("discount"), discount);
                    break;
                case LESS_THAN:
                    predicate = builder.le(deal.get("discount"), discount);
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
                    predicate = builder.gt(deal.get("price"), price);
                    break;
                case GREATHER_THAN_OR_EQUAL:
                    predicate = builder.greaterThanOrEqualTo(deal.get("price"), price);
                    break;
                case LESS_THAN:
                    predicate = builder.le(deal.get("price"), price);
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

            Predicate predicate = builder.equal(deal.get("customer_id"), customerID);

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

            Predicate predicate = builder.equal(deal.get("product_id"), productID);

            criteriaQuery.select(deal).where(predicate);
            TypedQuery<Deal> query = session.createQuery(criteriaQuery);

            return query.getResultList();
        }
    }

    public static List<Deal> getByPeriod(Date startRange, Date endRange){

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Deal> criteriaQuery = builder.createQuery(Deal.class);
            Root<Deal> deal = criteriaQuery.from(Deal.class);

            Predicate predicate = builder.between(deal.get("product_id"), startRange, endRange);

            criteriaQuery.select(deal).where(predicate);
            TypedQuery<Deal> query = session.createQuery(criteriaQuery);

            return query.getResultList();
        }
    }

}
