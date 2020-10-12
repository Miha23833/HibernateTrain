package com.exactpro.DAO;

import com.exactpro.entities.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;

public class ProductDAO {
    public static Product getByID(int id){
        return GenericDAO.selectByID(Product.class, id);
    }

    public static List<Product> getAllProducts(){
        return GenericDAO.gelAllEntities(Product.class);
    }

    public static List<Product> getByName(String name){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Product> criteriaQuery = builder.createQuery(Product.class);
            Root<Product> product = criteriaQuery.from(Product.class);

            Predicate predicate = builder.equal(product.get("name"), name);
            criteriaQuery.select(product).where(predicate);

            TypedQuery<Product> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }

    public static List<Product> getByPrice(BigDecimal price, ComparisonOperator operator){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Product> criteriaQuery = builder.createQuery(Product.class);
            Root<Product> product = criteriaQuery.from(Product.class);

            Predicate predicate;

            switch (operator){
                case NOT_EQUAL:
                    predicate = builder.notEqual(product.get("price"), price);
                    break;
                case GREATHER_THAN:
                    predicate = builder.gt(product.get("price"), price);
                    break;
                case GREATHER_THAN_OR_EQUAL:
                    predicate = builder.greaterThanOrEqualTo(product.get("price"), price);
                    break;
                case LESS_THAN:
                    predicate = builder.le(product.get("price"), price);
                    break;
                case LESS_THAN_OR_EQUAL:
                    predicate = builder.lessThanOrEqualTo(product.get("price"), price);
                    break;
                default:
                    predicate = builder.equal(product.get("price"), price);
            }

            criteriaQuery.select(product).where(predicate);

            TypedQuery<Product> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }


    public static List<Product> getBoughtByCustomer(Integer customerID){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery(
                    "SELECT\n" +
                            "products.*\n" +
                            "FROM deals\n" +
                            "JOIN products\n" +
                            "USING(product_id)\n" +
                            "WHERE deals.customer_id = 26\n" +
                            "GROUP BY 1,2,3,4",
                    Product.class
            ).setParameter(1, customerID)
             .list();
        }
    }
}
