package com.exactpro.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "deals")
public class Deal implements Serializable {
    @Id
    @Column(name = "deal_id", unique = true, nullable = false)
    private int dealID;

    private List<Customer> customerID = new ArrayList<>();
    private List<Product> productID;
    private Date dealDate;
    private BigDecimal discount;
    private BigDecimal price;

    public Deal() {}

    // dealID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getDealID() {
        return dealID;
    }

    public void setDealID(int dealID) {
        this.dealID = dealID;
    }

    // customerID
    @OneToMany(mappedBy = "customerID", targetEntity = Customer.class)
    @Column(name = "customer_id")
    public List<Customer> getCustomerID() {
        return customerID;
    }

    public void setCustomerID(List<Customer> customerID) {
        this.customerID = customerID;
    }

    // productID
    @OneToMany(mappedBy = "productID", targetEntity = Product.class)
    @Column(name = "product_id")
    public List<Product> getProductID() {
        return productID;
    }

    public void setProductID(List<Product> productID) {
        this.productID = productID;
    }

    // dealDate
    @Column(name = "deal_date")
    public Date getDealDate() {
        return dealDate;
    }

    public void setDealDate(Date dealDate) {
        this.dealDate = dealDate;
    }

    // discount
    @Column(name = "discount")
    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    // price
    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
