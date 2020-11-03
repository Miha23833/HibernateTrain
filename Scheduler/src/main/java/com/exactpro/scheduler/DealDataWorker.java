package com.exactpro.scheduler;

import com.exactpro.DAO.GenericDAO;
import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import com.opencsv.exceptions.CsvException;
import org.hibernate.Session;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Inserts data from CSV to Database.
 */
public class DealDataWorker extends DataWorker {

    public DealDataWorker() {
        this.columns = new String[]{"deal_date", "customer_id", "discount", "product_id", "price", "deal_id"};
    }

    @Override
    void insertData(Session session, String path, String filename) throws IOException, CsvException, SQLException {
        Map<String, List<String>> data = this.getDataFromCSVL(path, filename);

        String randomKey = null;
        for (String key: data.keySet()) {
            randomKey = key;
            break;
        }

        for (int i = 0; i < data.get(randomKey).size(); i++) {
            for (String key: data.keySet()) {
                System.out.print(key + ": " + data.get(key).get(i) + ", ");

                Deal dealToInsert = new Deal();

                Product product = GenericDAO.selectByID(session, Product.class, Integer.parseInt(data.get("product_id").get(i)));
                Customer customer = GenericDAO.selectByID(session, Customer.class, Integer.parseInt(data.get("customer_id").get(i)));

                if (product == null && customer == null) {
                    throw new SQLException("Product and customer does not exist.");
                }
                else if (product == null) {
                    throw new SQLException("Product does not exist.");
                }
                else if (customer == null) {
                    throw new SQLException("Customer does not exist.");
                }

                dealToInsert.setDealDate(Long.parseLong(data.get("deal_date").get(i)));
                dealToInsert.setPrice(new BigDecimal(data.get("price").get(i)));
                dealToInsert.setDiscount(new BigDecimal(data.get("discount").get(i)));
                dealToInsert.setProduct(product);
                dealToInsert.setCustomer(customer);

                GenericDAO.insertEntity(session, dealToInsert);

            }
        }
    }

}
