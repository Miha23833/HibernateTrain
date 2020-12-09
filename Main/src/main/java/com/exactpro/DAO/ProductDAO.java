package com.exactpro.DAO;

import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import com.exactpro.entities.metamodel.Customer_;
import com.exactpro.entities.metamodel.Deal_;
import com.exactpro.entities.metamodel.Product_;
import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.List;

public class ProductDAO {

    private static final Logger logger = StaticLogger.infoLogger;

    public static Product getByID(Session session, int id) {
        return GenericDAO.selectByID(session, Product.class, id);
    }

    public static List<Product> getAllProducts(Session session) {
        return GenericDAO.gelAllEntities(session, Product.class);
    }

    public static List<Product> getByName(Session session, String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Product> criteriaQuery = builder.createQuery(Product.class);
        Root<Product> product = criteriaQuery.from(Product.class);

        Predicate predicate = builder.equal(product.get("productName"), name);
        criteriaQuery.select(product).where(predicate);

        TypedQuery<Product> query = session.createQuery(criteriaQuery);

        List<Product> products = query.getResultList();
        logger.info(String.format("Returned list of products found by name(%s) ", name));
        return products;
    }

    public static List<Product> getByPrice(Session session, BigDecimal price, ComparisonOperator operator) {
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Product> criteriaQuery = builder.createQuery(Product.class);
        Root<Product> product = criteriaQuery.from(Product.class);

        Predicate predicate;

        switch (operator) {
            case NOT_EQUAL:
                predicate = builder.notEqual(product.get("price"), price);
                break;
            case GREATER_THAN:
                predicate = builder.greaterThan(product.get("price"), price);
                break;
            case GREATER_THAN_OR_EQUAL:
                predicate = builder.greaterThanOrEqualTo(product.get("price"), price);
                break;
            case LESS_THAN:
                predicate = builder.lessThan(product.get("price"), price);
                break;
            case LESS_THAN_OR_EQUAL:
                predicate = builder.lessThanOrEqualTo(product.get("price"), price);
                break;
            default:
                predicate = builder.equal(product.get("price"), price);
        }

        criteriaQuery.select(product).where(predicate);

        TypedQuery<Product> query = session.createQuery(criteriaQuery);

        List<Product> products = query.getResultList();
        logger.info(String.format("Returned list of products found by price which %s %s", operator, price));
        return products;
    }


    public static List<Product> getBoughtByCustomer(Session session, Integer customerID) {
        List<Product> result = session.createNativeQuery(
                "SELECT\n" +
                        "products.*\n" +
                        "FROM deals\n" +
                        "JOIN products\n" +
                        "USING(product_id)\n" +
                        "WHERE deals.customer_id = ?\n" +
                        "GROUP BY 1,2,3,4",
                Product.class).setParameter(1, customerID)
                .list();
        logger.info(String.format("Returned list of products that customer(%s) has bought", customerID));
        return result;
    }
}
