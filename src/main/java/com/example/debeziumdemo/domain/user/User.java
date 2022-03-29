package com.example.debeziumdemo.domain.user;

import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @Id
    private String id;
    private String name;
    private BigInteger quantity;
    private String orderId;
}
