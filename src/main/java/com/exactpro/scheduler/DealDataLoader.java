package com.exactpro.scheduler;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.DAO.SingleSessionFactory;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DealDataLoader extends AbstractDataLoader{

    public DealDataLoader() {
        this.selectStatement = "SELECT deal_date, customer_id, discount, product_id, price, deal_id\n, customer_id, product_id";
    }

    @Override
    void insertData(String path, String fileName) throws SQLException, ClassNotFoundException {
        ResultSet data = this.getDataFromCSV(path, fileName);

        Session session = SingleSessionFactory.getInstance().openSession();
        session.beginTransaction();

        while (data.next()){
            Deal dealToInsert = new Deal();

            Product product = GenericDAO.selectByID(session, Product.class, data.getInt("product_id"));
            Customer customer = GenericDAO.selectByID(session, Customer.class, data.getInt("customer_id"));

            dealToInsert.setDealID(data.getInt("deal_id"));
            dealToInsert.setDealDate(data.getLong("deal_date"));
            dealToInsert.setPrice(data.getBigDecimal("price"));
            dealToInsert.setDiscount(data.getBigDecimal("discount"));
            dealToInsert.setProduct(product);
            dealToInsert.setCustomer(customer);

            GenericDAO.insertEntity(session, dealToInsert);
        }
        session.close();
        session.getTransaction().commit();
    }


}
