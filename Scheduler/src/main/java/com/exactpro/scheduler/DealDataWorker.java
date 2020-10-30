package com.exactpro.scheduler;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import org.hibernate.Session;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Inserts data from CSV to Database.
 */
public class DealDataWorker extends DataWorker {

    public DealDataWorker() {
        this.columns = new String[] {"deal_date", "customer_id", "discount", "product_id", "price", "deal_id"};
    }

    @Override
    void insertData(Session session, String path, String filename, char delimiter) throws SQLException, ClassNotFoundException {
        ResultSet data = this.getDataFromCSV(path, filename, delimiter);

        session.beginTransaction();

        while (data.next()){
            Deal dealToInsert = new Deal();

            Product product = GenericDAO.selectByID(session, Product.class, data.getInt("product_id"));
            Customer customer = GenericDAO.selectByID(session, Customer.class, data.getInt("customer_id"));

            if (product == null && customer == null){
                throw new SQLException("Product and customer does not exist.");
            }
            else if (product == null){
                throw new SQLException("Product does not exist.");
            }
            else if (customer == null){
                throw new SQLException("Customer does not exist.");
            }

            dealToInsert.setDealDate(data.getLong("deal_date"));
            dealToInsert.setPrice(data.getBigDecimal("price"));
            dealToInsert.setDiscount(data.getBigDecimal("discount"));
            dealToInsert.setProduct(product);
            dealToInsert.setCustomer(customer);

            GenericDAO.insertEntity(session, dealToInsert);
        }
    }


}
