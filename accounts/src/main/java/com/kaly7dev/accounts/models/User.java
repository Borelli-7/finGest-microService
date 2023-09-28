package com.kaly7dev.accounts.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document(value = "users")
public class User {
    @Id
    private String userID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Address address;
}
