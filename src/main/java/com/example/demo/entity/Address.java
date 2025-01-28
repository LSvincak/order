package com.example.demo.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Embeddable
public class Address {
    private String street;
    private String city;
    private String state;
    private String zipCode;

    public Address(){
    }

    public Address(String street, String city, String state, String zipCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    //BL - (Maybe some checking if state is CZ/SK)
}