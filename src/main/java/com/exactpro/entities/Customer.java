package com.exactpro.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer implements Serializable {

//     Columns
    @Id
    private int customerID;

    private String name;
    private String surname;
    private short age;
    private Integer favouriteProduct;
    private Set<Deal> deals = new HashSet<>();

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

    public void setFavouriteProduct(Integer favouriteProduct) {
        this.favouriteProduct = favouriteProduct;
    }

    // deals
    @OneToMany(mappedBy = "customer", targetEntity = Deal.class, cascade = CascadeType.ALL)
    public Set<Deal> getDeals() {
        return deals;
    }

    public void setDeals(Set<Deal> deals) {
        this.deals = deals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerID == customer.customerID &&
                age == customer.age &&
                Objects.equals(name, customer.name) &&
                Objects.equals(surname, customer.surname) &&
                Objects.equals(favouriteProduct, customer.favouriteProduct) &&
                Objects.equals(deals, customer.deals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerID, name, surname, age, favouriteProduct, deals);
    }
}
