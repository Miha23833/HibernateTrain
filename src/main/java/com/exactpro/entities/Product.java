package com.exactpro.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    private int productID;

    private String productName;
    private String description;
    private BigDecimal price;
    private Set<Deal> deals = new HashSet<>();

    public Product() {}

    // productID
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    // productName
    @Column(name = "product_name")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // description
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // price
    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    // deals
    @OneToMany(mappedBy = "customer", targetEntity = Deal.class, cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Deal> getDeals() {
        return deals;
    }

    public void setDeals(Set<Deal> deals) {
        this.deals = deals;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Product))
            return false;

        Product guest = (Product) obj;

        return guest.getDeals().equals(this.deals) &&
                guest.getPrice().compareTo(this.price) == 0 &&
                guest.getDescription().equals(this.description) &&
                guest.getProductID() == (this.productID) &&
                guest.getProductName().equals(this.productName);
    }

}
