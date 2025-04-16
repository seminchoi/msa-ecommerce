package com.sem.ecommerce.core.r2dbc.converter;


import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class LocalToZonedDateTimeConverter implements Converter<LocalDateTime, ZonedDateTime> {
    @Override
    public ZonedDateTime convert(LocalDateTime source) {
        return source != null ? source.atZone(ZoneId.systemDefault()) : null;
    }
}