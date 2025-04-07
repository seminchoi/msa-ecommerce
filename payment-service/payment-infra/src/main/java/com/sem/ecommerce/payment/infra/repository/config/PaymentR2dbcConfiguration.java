package com.sem.ecommerce.payment.infra.repository.config;


import com.sem.ecommerce.payment.domain.PaymentMethodType;
import com.sem.ecommerce.payment.domain.PaymentStatus;
import com.sem.ecommerce.payment.infra.repository.converter.PaymentMethodTypeConverter;
import com.sem.ecommerce.payment.infra.repository.converter.PaymentStatusConverter;
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
public class PaymentR2dbcConfiguration {
    @Bean
    public ConnectionFactoryOptionsBuilderCustomizer paymentConnectionFactoryOptionsBuilderCustomizer() {
        return builder -> {
            builder.option(Option.valueOf("extensions"),
                    List.of(EnumCodec.builder()
                            .withEnum("payment_status", PaymentStatus.class)
                            .withEnum("payment_method_type", PaymentMethodType.class)
                            .build()));
        };
    }

    @Bean
    public R2dbcCustomConversions paymentR2dbcCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new PaymentStatusConverter());
        converters.add(new PaymentMethodTypeConverter());
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }
}