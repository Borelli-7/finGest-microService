package com.kaly7dev.managemoneyqueryservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Transaction {
    @Id
    private String id;
    private String desc;
    private Double amount;
    private String month;
    private String weekNum;
}
