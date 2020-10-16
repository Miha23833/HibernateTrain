package com.exactpro.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "deals")
public class Deal implements Serializable {
    @Id
    private Integer dealID;

    private Customer customer;
    private Product product;
    private Long dealDate;
    private BigDecimal discount;
    private BigDecimal price;

    public Deal() {}

    public Deal(Customer customer, Product product, Long dealDate, BigDecimal price, BigDecimal discount) {
        this.customer = customer;
        this.product = product;
        this.dealDate = dealDate;
        this.discount = discount;
        this.price = price;
    }

    // dealID
    @Id
    @Column(name = "deal_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getDealID() {
        return dealID;
    }

    public void setDealID(Integer dealID) {
        this.dealID = dealID;
    }

    // customer
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
    @JoinColumn(name = "customer_id", nullable = false)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // product
    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Product.class)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // dealDate
    @Column(name = "deal_date")
    public Long getDealDate() {
        return dealDate;
    }

    public void setDealDate(Long dealDate) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deal deal = (Deal) o;
        return dealID == deal.dealID &&
                Objects.equals(customer, deal.customer) &&
                Objects.equals(product, deal.product) &&
                Objects.equals(dealDate, deal.dealDate) &&
                Objects.equals(discount, deal.discount) &&
                Objects.equals(price, deal.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dealID, customer, product, dealDate, discount, price);
    }
}
