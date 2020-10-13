package com.exactpro.test.DAO;

import com.exactpro.DAO.ComparisonOperator;
import com.exactpro.DAO.CustomerDAO;
import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.ProductDAO;
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
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDAOTest {

    Customer customer;

    @Before
    public void launchAllTests() throws SQLException, ClassNotFoundException {
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
    }


    @BeforeEach
    void launchTest(){
        customer = new Customer("TEST", "UNIT", (short) 20, null);
    }

    @AfterEach
    void cleanTest() throws SQLException, ClassNotFoundException {
        customer = null;
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");
    }

    @Test
    void getByName() {
        for (int i = 0; i < 5; i++) {
            customer = new Customer("TEST"+i, "UNIT", (short) 20, null);
            GenericDAO.insertEntity(customer);
        }

        List<Customer> customers = CustomerDAO.getByName("TEST3");

        Assert.assertEquals(customers.size(), 1);
        for (Customer compCustomer: customers) {
            Assert.assertEquals(compCustomer.getName(), "TEST3");
        }
    }

    @Test
    void getBySurname() {
        for (int i = 0; i < 5; i++) {
            customer = new Customer("TEST", "UNIT"+i, (short) 20, null);
            GenericDAO.insertEntity(customer);
        }

        List<Customer> customers = CustomerDAO.getBySurname("UNIT3");

        Assert.assertEquals(customers.size(), 1);
        for (Customer compCustomer: customers) {
            Assert.assertEquals(compCustomer.getSurname(), "UNIT3");
        }
    }

    @Test
    void getByID() {
        GenericDAO.insertEntity(customer);
        Customer compCustomer = CustomerDAO.getByID(customer.getCustomerID());

        Assert.assertEquals(compCustomer.getName(), customer.getName());
        Assert.assertEquals(compCustomer.getSurname(), customer.getSurname());
        Assert.assertEquals(compCustomer.getAge(), customer.getAge());
        Assert.assertEquals(compCustomer.getCustomerID(), customer.getCustomerID());

    }

    @Test
    void getAllCustomers() {
        for (int i = 0; i < 50; i++) {
            Customer newCustomer = new Customer("TEST"+i, "UNIT"+i, (short) 20, null);
            GenericDAO.insertEntity(newCustomer);
        }

        List<Customer> customers = CustomerDAO.getAllCustomers();

        Assert.assertEquals(customers.size(), 50);

        for (int i = 0; i < customers.size(); i++) {
            Assert.assertEquals(customers.get(i).getName(), "TEST"+i);
            Assert.assertEquals(customers.get(i).getSurname(), "UNIT"+i);
        }

    }

    @Test
    void getByAge() {
        for (int i = 0; i < 10; i++) {
            Customer newCustomer = new Customer("TEST", "UNIT", (short) i, null);
            GenericDAO.insertEntity(newCustomer);
        }

        List<Customer> customers = CustomerDAO.getByAge((short)8, ComparisonOperator.EQUAL);
        Assert.assertEquals(customers.size(), 1);
        Assert.assertEquals(customers.get(0).getAge(), (short)8);

        // Сравнение по количеству возвращённых записей для учитывания всех енумов

        Assert.assertEquals(CustomerDAO.getByAge((short) 3, ComparisonOperator.NOT_EQUAL).size(), 9);

        Assert.assertEquals(CustomerDAO.getByAge((short) 3, ComparisonOperator.GREATHER_THAN).size(), 6);

        Assert.assertEquals(CustomerDAO.getByAge((short) 3, ComparisonOperator.GREATHER_THAN_OR_EQUAL).size(), 7);

        Assert.assertEquals(CustomerDAO.getByAge((short) 7, ComparisonOperator.LESS_THAN).size(), 7);

        Assert.assertEquals(CustomerDAO.getByAge((short) 7, ComparisonOperator.LESS_THAN_OR_EQUAL).size(), 8);
    }

    @Test
    void getWhoBoughtProduct() {
        Product product = new Product();

        product.setPrice(new BigDecimal(50000));
        product.setDescription("Some desc");
        product.setProductName("Name");

        GenericDAO.insertEntity(customer);

        GenericDAO.insertEntity(product);

        for (int i = 0; i < 5; i++) {
            Customer newCustomer = new Customer("TEST", "UNIT", (short) i, null);
            Deal deal = new Deal();

            deal.setCustomer(customer);
            deal.setProduct(product);
            deal.setPrice(product.getPrice());
            deal.setDiscount(new BigDecimal(0));
            deal.setDealDate(new Timestamp(System.currentTimeMillis()));

            GenericDAO.insertEntity(newCustomer);
            GenericDAO.insertEntity(deal);
        }

        List<Customer> customers = CustomerDAO.getWhoBoughtProduct(product.getProductID());

        Assert.assertEquals(1, customers.size());

        for (Customer compCustomer: customers) {
            Assert.assertEquals(compCustomer.getAge(), customer.getAge());
            Assert.assertEquals(compCustomer.getName(), customer.getName());
            Assert.assertEquals(compCustomer.getSurname(), customer.getSurname());
            Assert.assertEquals(compCustomer.getCustomerID(), customer.getCustomerID());
        }
    }
}