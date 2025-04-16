package com.sem.ecommerce.core.r2dbc.converter;


import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class ZoneToLocalDateTimeConverter implements Converter<ZonedDateTime, LocalDateTime> {
    @Override
    public LocalDateTime convert(ZonedDateTime source) {
        return source != null ? source.toLocalDateTime() : null;
    }
}