package com.exactpro.test.DAO;

import com.exactpro.DAO.ComparisonOperator;
import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.ProductDAO;
import com.exactpro.connection.DBConnection;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import com.exactpro.test.common.CommonUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

class ProductDAOTest {

    Product product;

    @Before
    public void launchAllTests() throws SQLException, ClassNotFoundException {
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
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
    }

    @Test
    void getByID() {
        int id = GenericDAO.insertEntity(product);
        Product newProduct = ProductDAO.getByID(id);

        Assert.assertEquals(product.getProductID(), newProduct.getProductID());
        Assert.assertEquals(product.getProductName(), newProduct.getProductName());
        Assert.assertEquals(product.getDescription(), newProduct.getDescription());
        Assert.assertEquals(0, product.getPrice().compareTo(newProduct.getPrice()));
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
            Assert.assertEquals(0, compProduct.getPrice().compareTo(product.getPrice()));
            Assert.assertEquals(product.getDescription(), compProduct.getDescription());
            Assert.assertEquals(product.getProductName(), compProduct.getProductName());
        }
    }

    @Test
    void getByName() {
        GenericDAO.insertEntity(product);
        List<Product> products = ProductDAO.getByName("Name");
        for (Product compProduct : products) {
            Assert.assertEquals(product.getProductName(), compProduct.getProductName());
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
        Assert.assertEquals(1, ProductDAO.getByPrice(new BigDecimal(5), ComparisonOperator.EQUAL).size());

        Assert.assertEquals(4, ProductDAO.getByPrice(new BigDecimal(5), ComparisonOperator.NOT_EQUAL).size());

        Assert.assertEquals(2, ProductDAO.getByPrice(new BigDecimal(3), ComparisonOperator.GREATHER_THAN).size());

        Assert.assertEquals(3, ProductDAO.getByPrice(new BigDecimal(3), ComparisonOperator.GREATHER_THAN_OR_EQUAL).size());

        Assert.assertEquals(2, ProductDAO.getByPrice(new BigDecimal(3), ComparisonOperator.LESS_THAN).size());

        Assert.assertEquals(3, ProductDAO.getByPrice(new BigDecimal(3), ComparisonOperator.LESS_THAN_OR_EQUAL).size());

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
            deal.setDealDate(System.currentTimeMillis());
            GenericDAO.insertEntity(deal);
        }

        List<Product> products = ProductDAO.getBoughtByCustomer(customer.getCustomerID());

        Assert.assertEquals(1, products.size());

        for (Product compProduct: products) {
            Assert.assertEquals(0, compProduct.getPrice().compareTo(product.getPrice()));
            Assert.assertEquals(product.getDescription(), compProduct.getDescription());
            Assert.assertEquals(product.getProductName(), compProduct.getProductName());
        }
    }
}