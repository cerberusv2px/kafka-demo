package com.example.debeziumdemo.domain;

import java.util.List;

import com.example.debeziumdemo.domain.order.Order;
import com.example.debeziumdemo.domain.user.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class OrderUserAggregate {

    private final Order order;
    private final List<User> users;

    @JsonCreator
    public OrderUserAggregate(
        @JsonProperty("order") Order order,
        @JsonProperty("users") List<User> users
    ) {
        this.order = order;
        this.users = users;
    }
}
