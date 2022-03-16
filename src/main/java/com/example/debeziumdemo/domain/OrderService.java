package com.example.debeziumdemo.domain;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.data.Envelope.Operation;

@Service
public class OrderService {

    private final OrderRepository orderRepository;


    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void replicateData(Map<String, Object> orders, Operation operation) {
        final ObjectMapper mapper = new ObjectMapper();
        final Order order = mapper.convertValue(orders, Order.class);

        if(Operation.DELETE == operation) {
            orderRepository.deleteById(order.getId());
        } else {
            orderRepository.save(order);
        }
    }
}
