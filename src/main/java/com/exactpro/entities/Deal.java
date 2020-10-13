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
    private int dealID;

    private Customer customer;
    private Product product;
    private Date dealDate;
    private BigDecimal discount;
    private BigDecimal price;

    public Deal() {}

    // dealID
    @Id
    @Column(name = "deal_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getDealID() {
        return dealID;
    }

    public void setDealID(int dealID) {
        this.dealID = dealID;
    }

    // customer
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinColumn(name = "customer_id", nullable = false)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // product
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Product.class)
    @JoinColumn(name = "product_id", nullable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Deal))
            return false;

        Deal guest = (Deal) obj;

        return guest.getCustomer().equals(this.customer) &&
                guest.getProduct().equals(this.product) &&
                guest.getDealDate() == this.dealDate &&
                guest.getDealID() == (this.dealID) &&
                guest.getDiscount().compareTo(this.discount) == 0 &&
                guest.getPrice().compareTo(this.price) == 0;
    }

}
