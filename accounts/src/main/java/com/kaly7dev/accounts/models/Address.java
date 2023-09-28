package com.kaly7dev.accounts.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(value = "addresses")
public class Address {
    @Id
    private Long addressID;
    private String street;
    private String city;
    private String zipCode;
    private String country;
    private  User user;
}
