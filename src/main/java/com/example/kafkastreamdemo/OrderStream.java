package com.example.kafkastreamdemo;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import com.example.debeziumdemo.domain.Order;

public class OrderStream {
    private final static String TOPIC = "evolve.public.orders";

    public static void build(StreamsBuilder builder) {
        final Serde<Order> orderSerde = SerdeFactory.createSerdeFor(Order.class, true);
        final Serde<String> idSerde = Serdes.serdeFrom(new IdSerializer(), new IdDeserializer());

        KStream<String, Order> orderKStream =
            builder.stream(TOPIC, Consumed.with(idSerde, orderSerde));

        orderKStream.peek((key, value) -> {
            System.out.println(key.toString());
            System.out.println(value.toString());
        });
        orderKStream.map((id, order) -> {
            Order newOrder = new Order();
            newOrder.setItemname(order.getItemname() + ":))");
            newOrder.setStatus(order.getStatus() + ":))");
            return new KeyValue<>(id, newOrder);
        }).to("evolve.orderdb.new-orders", Produced.with(idSerde, orderSerde));
    }
}
