package com.exactpro.entities;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customers")
public class Customer implements Serializable {

    // Columns
    @Id
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Deal.class)
    @JoinColumn(name = "customerID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerID;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "age")
    private short age;

    @Column(name = "favourite_product")
    private long favouriteProduct;

    public Customer(){}

    public Customer(String name, String surname, short age, long favouriteProduct){
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.favouriteProduct = favouriteProduct;
    }


    // customerID
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    // name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // surname
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    // age
    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }

    // favouriteProduct
    public long getFavouriteProduct() {
        return favouriteProduct;
    }

    public void setFavouriteProduct(long favouriteProduct) {
        this.favouriteProduct = favouriteProduct;
    }
}
