package com.example.kafkastreamdemo;

import java.util.List;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;


import com.example.debeziumdemo.domain.OrderUserAggregate;
import com.example.debeziumdemo.domain.order.Order;
import com.example.debeziumdemo.domain.user.User;

public class OrderUserStream {

    private final static String ORDER_TOPIC = "evolve.public.orders";
    private final static String USER_TOPIC = "evolve.public.users";
    private final static String AGG_TOPIC = "evolve.finalagg.orderuser";

    public static void build(StreamsBuilder builder) {
        final Serde<String> idSerde = Serdes.serdeFrom(new IdSerializer(), new IdDeserializer());
        final Serde<Order> orderSerde = SerdeFactory.createSerdeFor(Order.class, true);
        final Serde<User> userSerde = SerdeFactory.createSerdeFor(User.class, true);
        final Serde<OrderUserAggregate> orderUserAggregateSerde = SerdeFactory.createSerdeFor(
            OrderUserAggregate.class, true);

        KTable<String, Order> orderTable = builder.table(ORDER_TOPIC,
            Consumed.with(idSerde, orderSerde));

        KTable<String, User> userTable = builder.table(USER_TOPIC,
                Consumed.with(idSerde, userSerde)).toStream()
            .map((userId, user) -> new KeyValue<>(user.getOrderId(), user))
            .groupByKey(Grouped.with(idSerde, userSerde))
            .aggregate(
                () -> new User(),
                (orderId, user, users) -> {
                    return users;
                },
                Materialized.<String, User, KeyValueStore<Bytes, byte[]>>
                        as(USER_TOPIC + "_table_agg")
                    .withKeySerde(idSerde)
                    .withValueSerde(userSerde)
            );

        KTable<String, OrderUserAggregate> orderUserAggregate =
            orderTable.join(userTable, (order, user) ->
                new OrderUserAggregate(order, List.of(user))
            );

        orderUserAggregate.toStream().to(AGG_TOPIC, Produced.with(idSerde, orderUserAggregateSerde));
    }
}
