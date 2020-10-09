package com.exactpro.entities;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customers")
public class Customer implements Serializable {

//     Columns
    @Id
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Deal.class)
    @JoinColumn(name = "customerID", nullable = false)
    private int customerID;

    private String name;
    private String surname;
    private short age;
    private Integer favouriteProduct;

    public Customer(){}

    public Customer(String name, String surname, short age, Integer favouriteProduct){
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.favouriteProduct = favouriteProduct;
    }


    // customerID
    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    // name
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // surname
    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    // age
    @Column(name = "age")
    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }

    // favouriteProduct
    @Column(name = "favourite_product")
    public Integer getFavouriteProduct() {
        return favouriteProduct;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Customer))
            return false;

        return
                ((Customer) obj).getAge() == this.age &&
                ((Customer) obj).getCustomerID() == this.customerID &&
                ((Customer) obj).getName().equals(this.name) &&
                ((Customer) obj).getSurname().equals(this.surname) &&
                ((Customer) obj).getFavouriteProduct().equals(this.favouriteProduct);


    }

    public void setFavouriteProduct(Integer favouriteProduct) {
        this.favouriteProduct = favouriteProduct;
    }
}
