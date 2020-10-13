package com.exactpro.test.DAO;

import com.exactpro.DAO.ComparisonOperator;
import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.ProductDAO;
import com.exactpro.connection.DBConnection;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

class ProductDAOTest {

    Product product;

    @Before
    public void launchAllTests() throws SQLException, ClassNotFoundException {
        DBConnection.setConnectionData("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");

        DBConnection.executeNonResult("DELETE FROM CUSTOMERS");
        DBConnection.executeNonResult("ALTER TABLE CUSTOMERS AUTO_INCREMENT = 0");
        DBConnection.executeNonResult("DELETE FROM PRODUCTS");
        DBConnection.executeNonResult("ALTER TABLE PRODUCTS AUTO_INCREMENT = 0");
        DBConnection.executeNonResult("DELETE FROM DEALS");
        DBConnection.executeNonResult("ALTER TABLE DEALS AUTO_INCREMENT = 0");
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
        DBConnection.setConnectionData("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
        DBConnection.executeNonResult("DELETE FROM CUSTOMERS");
        DBConnection.executeNonResult("ALTER TABLE CUSTOMERS AUTO_INCREMENT = 0");
        DBConnection.executeNonResult("DELETE FROM PRODUCTS");
        DBConnection.executeNonResult("ALTER TABLE PRODUCTS AUTO_INCREMENT = 0");
        DBConnection.executeNonResult("DELETE FROM DEALS");
        DBConnection.executeNonResult("ALTER TABLE DEALS AUTO_INCREMENT = 0");
    }

    @Test
    void getByID() {
        int id = GenericDAO.insertEntity(product);
        Product newProduct = ProductDAO.getByID(id);
        Assert.assertEquals(newProduct, product);
    }

    @Test
    void getAllProducts() {
        for (int i = 0; i < 10; i++) {
            Product products = new Product();

            products.setPrice(new BigDecimal(50000));
            products.setDescription("Some desc");
            products.setProductName("Name");
            GenericDAO.insertEntity(products);
        }
        List<Product> productsList = ProductDAO.getAllProducts();

        Assert.assertEquals(productsList.size(), 10);

        for (Product compProduct : productsList) {
            Assert.assertEquals(compProduct.getDeals(), (product.getDeals()));
            Assert.assertEquals(0, compProduct.getPrice().compareTo(product.getPrice()));
            Assert.assertEquals(compProduct.getDescription(), product.getDescription());
            Assert.assertEquals(compProduct.getProductName(), product.getProductName());
        }
    }

    @Test
    void getByName() {
        GenericDAO.insertEntity(product);
        List<Product> products = ProductDAO.getByName("Name");
        for (Product compProduct : products) {
            Assert.assertEquals(compProduct.getProductName(), product.getProductName());
        }
    }

    @Test
    void getByPrice() {
        for (int i = 1; i < 6; i++) {
            Product productToInsert = new Product();
            productToInsert.setProductName("Product to getByPrice");
            productToInsert.setDescription("Just a product");
            productToInsert.setPrice(new BigDecimal(i));
            GenericDAO.insertEntity(productToInsert);
        }

        // Сравнение по количеству возвращённых записей для учитывания всех енумов
        Assert.assertEquals(ProductDAO.getByPrice(new BigDecimal(5), ComparisonOperator.EQUAL).size(), 1);

        Assert.assertEquals(ProductDAO.getByPrice(new BigDecimal(5), ComparisonOperator.NOT_EQUAL).size(), 4);

        Assert.assertEquals(ProductDAO.getByPrice(new BigDecimal(3), ComparisonOperator.GREATHER_THAN).size(), 2);

        Assert.assertEquals(ProductDAO.getByPrice(new BigDecimal(3), ComparisonOperator.GREATHER_THAN_OR_EQUAL).size(), 3);

        Assert.assertEquals(ProductDAO.getByPrice(new BigDecimal(3), ComparisonOperator.LESS_THAN).size(), 2);

        Assert.assertEquals(ProductDAO.getByPrice(new BigDecimal(3), ComparisonOperator.LESS_THAN_OR_EQUAL).size(), 3);

    }

    @Test
    void getBoughtByCustomer() {
        Customer customer = new Customer("Unit", "Test", (short) 15, null);

        GenericDAO.insertEntity(customer); GenericDAO.insertEntity(product);

        for (int i = 0; i < 5; i++) {
            Deal deal = new Deal();
            deal.setCustomer(customer);
            deal.setProduct(product);
            deal.setPrice(product.getPrice());
            deal.setDiscount(new BigDecimal(0));
            deal.setDealDate(Date.valueOf(LocalDate.now()));
            GenericDAO.insertEntity(deal);
        }

        List<Product> products = ProductDAO.getBoughtByCustomer(customer.getCustomerID());

        Assert.assertEquals(1, products.size());

        for (Product compProduct: products) {
            Assert.assertEquals(0, compProduct.getPrice().compareTo(product.getPrice()));
            Assert.assertEquals(compProduct.getDescription(), product.getDescription());
            Assert.assertEquals(compProduct.getProductName(), product.getProductName());
        }
    }
}