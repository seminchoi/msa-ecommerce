package com.sem.ecommerce.infra.repository.config;

import com.sem.ecommerce.domain.order.OrderItemState;
import com.sem.ecommerce.domain.order.OrderState;
import com.sem.ecommerce.domain.order.ShippingState;
import com.sem.ecommerce.domain.refund.RefundState;
import com.sem.ecommerce.infra.repository.converter.OrderItemStateConverter;
import com.sem.ecommerce.infra.repository.converter.OrderStateConverter;
import com.sem.ecommerce.infra.repository.converter.RefundStateConverter;
import com.sem.ecommerce.infra.repository.converter.ShippingStateConverter;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.Option;
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryOptionsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class R2dbcConfiguration {
    @Bean
    public ConnectionFactoryOptionsBuilderCustomizer connectionFactoryOptionsBuilderCustomizer() {
        return builder -> {
            builder.option(Option.valueOf("extensions"),
                    List.of(EnumCodec.builder()
                            .withEnum("order_item_state", OrderItemState.class)
                            .withEnum("order_state", OrderState.class)
                            .withEnum("shipping_state", ShippingState.class)
                            .withEnum("refund_state", RefundState.class)
                            .build()));
        };
    }

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new OrderStateConverter());
        converters.add(new OrderItemStateConverter());
        converters.add(new ShippingStateConverter());
        converters.add(new RefundStateConverter());
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }
}
