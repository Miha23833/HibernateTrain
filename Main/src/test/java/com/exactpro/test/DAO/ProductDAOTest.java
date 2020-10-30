package com.exactpro.test.DAO;

import com.exactpro.DAO.ComparisonOperator;
import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.ProductDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.cache.DealService;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import com.exactpro.loggers.StaticLogger;
import com.exactpro.tests.CommonUnitTests;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

class ProductDAOTest {

    private final Logger infoLogger = StaticLogger.infoLogger;

    Product product;
    private final SessionFactory sf = SingleSessionFactory.getInstance();

    @Before
    public void launchAllTests() throws SQLException, ClassNotFoundException {
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");

        DealService service = new DealService();
        service.clean();

        infoLogger.info("Cache was cleaned during clearing database");
    }


    @BeforeEach
    void launchTest(){
        product = new Product();

        product.setPrice(new BigDecimal(50000));
        product.setDescription("Some desc");
        product.setProductName("Name");
    }

    @AfterEach
    void cleanTest() throws SQLException, ClassNotFoundException {
        product = null;
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");

        DealService service = new DealService();
        service.clean();

        infoLogger.info("Cache was cleaned during clearing database");
    }

    @Test
    void getByID() {

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        int id = GenericDAO.insertEntity(insertSession, product);
        insertSession.getTransaction().commit();

        Session selectSession = sf.openSession();
        Product newProduct = ProductDAO.getByID(selectSession, id);
        selectSession.close();

        Assert.assertEquals(product.getProductID(), newProduct.getProductID());
        Assert.assertEquals(product.getProductName(), newProduct.getProductName());
        Assert.assertEquals(product.getDescription(), newProduct.getDescription());
        Assert.assertEquals(0, product.getPrice().compareTo(newProduct.getPrice()));
    }

    @Test
    void getAllProducts() {

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        for (int i = 0; i < 10; i++) {
            Product products = new Product();

            products.setPrice(new BigDecimal(50000));
            products.setDescription("Some desc");
            products.setProductName("Name");
            GenericDAO.insertEntity(insertSession, products);
        }
        insertSession.getTransaction().commit();
        List<Product> productsList = ProductDAO.getAllProducts(sf.openSession());

        Assert.assertEquals(productsList.size(), 10);

        for (Product compProduct : productsList) {
            Assert.assertEquals(0, compProduct.getPrice().compareTo(product.getPrice()));
            Assert.assertEquals(product.getDescription(), compProduct.getDescription());
            Assert.assertEquals(product.getProductName(), compProduct.getProductName());
        }
    }

    @Test
    void getByName() {

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        GenericDAO.insertEntity(insertSession, product);
        insertSession.getTransaction().commit();

        Session selectSession = sf.openSession();
        List<Product> products = ProductDAO.getByName(selectSession, "Name");
        selectSession.close();

        for (Product compProduct : products) {
            Assert.assertEquals(product.getProductName(), compProduct.getProductName());
        }
    }

    @Test
    void getByPrice() {
        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        for (int i = 1; i < 6; i++) {
            Product productToInsert = new Product();
            productToInsert.setProductName("Product to getByPrice");
            productToInsert.setDescription("Just a product");
            productToInsert.setPrice(new BigDecimal(i));
            GenericDAO.insertEntity(insertSession, productToInsert);
        }
        insertSession.getTransaction().commit();

        // Сравнение по количеству возвращённых записей для учитывания всех енумов
        Assert.assertEquals(1, ProductDAO.getByPrice(sf.openSession(), new BigDecimal(5), ComparisonOperator.EQUAL).size());

        Assert.assertEquals(4, ProductDAO.getByPrice(sf.openSession(), new BigDecimal(5), ComparisonOperator.NOT_EQUAL).size());

        Assert.assertEquals(2, ProductDAO.getByPrice(sf.openSession(), new BigDecimal(3), ComparisonOperator.GREATER_THAN).size());

        Assert.assertEquals(3, ProductDAO.getByPrice(sf.openSession(), new BigDecimal(3), ComparisonOperator.GREATER_THAN_OR_EQUAL).size());

        Assert.assertEquals(2, ProductDAO.getByPrice(sf.openSession(), new BigDecimal(3), ComparisonOperator.LESS_THAN).size());

        Assert.assertEquals(3, ProductDAO.getByPrice(sf.openSession(), new BigDecimal(3), ComparisonOperator.LESS_THAN_OR_EQUAL).size());

    }

    @Test
    void getBoughtByCustomer() {
        Customer customer = new Customer("Unit", "Test", (short) 15, null);

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();
        GenericDAO.insertEntity(insertSession, customer);
        GenericDAO.insertEntity(insertSession, product);

        for (int i = 0; i < 5; i++) {
            Deal deal = new Deal();
            deal.setCustomer(customer);
            deal.setProduct(product);
            deal.setPrice(product.getPrice());
            deal.setDiscount(new BigDecimal(0));
            deal.setDealDate(System.currentTimeMillis());
            GenericDAO.insertEntity(insertSession, deal);
        }
        insertSession.getTransaction().commit();

        List<Product> products = ProductDAO.getBoughtByCustomer(sf.openSession(), customer.getCustomerID());

        Assert.assertEquals(1, products.size());

        for (Product compProduct: products) {
            Assert.assertEquals(0, compProduct.getPrice().compareTo(product.getPrice()));
            Assert.assertEquals(product.getDescription(), compProduct.getDescription());
            Assert.assertEquals(product.getProductName(), compProduct.getProductName());
        }
    }
}