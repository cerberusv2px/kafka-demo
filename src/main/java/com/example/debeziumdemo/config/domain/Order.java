package com.example.debeziumdemo.config.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "orders")
public class Order {
    @Id
    private Long id;
    private String itemname;
    private String status;
}
