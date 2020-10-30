package com.exactpro.test.DAO;

import com.exactpro.DAO.ComparisonOperator;
import com.exactpro.DAO.CustomerDAO;
import com.exactpro.DAO.GenericDAO;
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

class CustomerDAOTest {

    private Customer customer;
    private final Logger infoLogger = StaticLogger.infoLogger;

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
        customer = new Customer("TEST", "UNIT", (short) 20, null);
    }

    @AfterEach
    void cleanTest() throws SQLException, ClassNotFoundException {
        customer = null;
        CommonUnitTests.cleanDB("jdbc:mysql://localhost:3306/hibernate_unittests", "root", "password");

        DealService service = new DealService();
        service.clean();

        infoLogger.info("Cache was cleaned during clearing database");
    }

    @Test
    void getByName() {

        Session insertSession = sf.openSession();
        insertSession.beginTransaction();

        for (int i = 0; i < 5; i++) {
            customer = new Customer("TEST"+i, "UNIT", (short) 20, null);
            GenericDAO.insertEntity(insertSession, customer);
        }
        insertSession.getTransaction().commit();

        List<Customer> customers = CustomerDAO.getByName(sf.openSession(), "TEST3");

        Assert.assertEquals(1, customers.size());
        for (Customer compCustomer: customers) {
            Assert.assertEquals(compCustomer.getName(), "TEST3");
        }
    }

    @Test
    void getBySurname() {

        Session session = sf.openSession();
        session.beginTransaction();
        for (int i = 0; i < 5; i++) {
            customer = new Customer("TEST", "UNIT"+i, (short) 20, null);
            GenericDAO.insertEntity(session, customer);
        }
        session.getTransaction().commit();

        List<Customer> customers = CustomerDAO.getBySurname(sf.openSession(), "UNIT3");

        Assert.assertEquals(1, customers.size());
        for (Customer compCustomer: customers) {
            Assert.assertEquals("UNIT3", compCustomer.getSurname());
        }
    }

    @Test
    void getByID() {
        Session session = sf.openSession();
        session.beginTransaction();
        GenericDAO.insertEntity(session, customer);
        session.getTransaction().commit();

        Session selectSession = sf.openSession();
        Customer compCustomer = CustomerDAO.getByID(selectSession, customer.getCustomerID());
        selectSession.close();

        Assert.assertEquals(customer.getName(), compCustomer.getName());
        Assert.assertEquals(customer.getSurname(), compCustomer.getSurname());
        Assert.assertEquals(customer.getAge(), compCustomer.getAge());
        Assert.assertEquals(customer.getCustomerID(), compCustomer.getCustomerID());

    }

    @Test
    void getAllCustomers() {
        Session session = sf.openSession();
        session.beginTransaction();
        for (int i = 0; i < 50; i++) {
            Customer newCustomer = new Customer("TEST"+i, "UNIT"+i, (short) 20, null);
            GenericDAO.insertEntity(session, newCustomer);
        }
        session.getTransaction().commit();

        List<Customer> customers = CustomerDAO.getAllCustomers(sf.openSession());

        Assert.assertEquals(customers.size(), 50);

        for (int i = 0; i < customers.size(); i++) {
            Assert.assertEquals("TEST"+i, customers.get(i).getName());
            Assert.assertEquals("UNIT"+i, customers.get(i).getSurname());
        }

    }

    @Test
    void getByAge() {
        Session session = sf.openSession();
        session.beginTransaction();
        for (int i = 0; i < 10; i++) {
            Customer newCustomer = new Customer("TEST", "UNIT", (short) i, null);
            GenericDAO.insertEntity(session, newCustomer);
        }
        session.getTransaction().commit();

        List<Customer> customers = CustomerDAO.getByAge(sf.openSession(), (short)8, ComparisonOperator.EQUAL);
        Assert.assertEquals(customers.size(), 1);
        Assert.assertEquals(customers.get(0).getAge(), (short)8);

        // Сравнение по количеству возвращённых записей для учитывания всех енумов

        Assert.assertEquals(9, CustomerDAO.getByAge(sf.openSession(), (short) 3, ComparisonOperator.NOT_EQUAL).size());

        Assert.assertEquals(6, CustomerDAO.getByAge(sf.openSession(), (short) 3, ComparisonOperator.GREATER_THAN).size());

        Assert.assertEquals(7, CustomerDAO.getByAge(sf.openSession(), (short) 3, ComparisonOperator.GREATER_THAN_OR_EQUAL).size());

        Assert.assertEquals(7, CustomerDAO.getByAge(sf.openSession(), (short) 7, ComparisonOperator.LESS_THAN).size());

        Assert.assertEquals(8, CustomerDAO.getByAge(sf.openSession(), (short) 7, ComparisonOperator.LESS_THAN_OR_EQUAL).size());
    }

    @Test
    void getWhoBoughtProduct() {
        Product product = new Product();

        product.setPrice(new BigDecimal(50000));
        product.setDescription("Some desc");
        product.setProductName("Name");

        Session session = sf.openSession();
        session.beginTransaction();
        GenericDAO.insertEntity(session, customer);
        GenericDAO.insertEntity(session, product);

        for (int i = 0; i < 5; i++) {
            Customer newCustomer = new Customer("TEST", "UNIT", (short) i, null);
            Deal deal = new Deal();

            deal.setCustomer(customer);
            deal.setProduct(product);
            deal.setPrice(product.getPrice());
            deal.setDiscount(new BigDecimal(0));
            deal.setDealDate(System.currentTimeMillis());

            GenericDAO.insertEntity(session, newCustomer);
            GenericDAO.insertEntity(session, deal);
        }
        session.getTransaction().commit();

        List<Customer> customers = CustomerDAO.getWhoBoughtProduct(sf.openSession(), product.getProductID());

        Assert.assertEquals(1, customers.size());

        for (Customer compCustomer: customers) {
            Assert.assertEquals(customer.getAge(), compCustomer.getAge());
            Assert.assertEquals(customer.getName(), compCustomer.getName());
            Assert.assertEquals(customer.getSurname(), compCustomer.getSurname());
            Assert.assertEquals(customer.getCustomerID(), compCustomer.getCustomerID());
        }
    }
}