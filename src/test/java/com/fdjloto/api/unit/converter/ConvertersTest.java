package com.fdjloto.api.unit.converter;

import com.fdjloto.api.converter.DateConverter;
import com.fdjloto.api.converter.LocalDateTimeAttributeConverter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ConvertersTest {

    @Test
    void shouldConvertLocalDateToDatabaseColumn() {
        DateConverter converter = new DateConverter();

        String result = converter.convertToDatabaseColumn(LocalDate.of(2025, 3, 12));

        assertEquals("2025-03-12", result);
    }

    @Test
    void shouldConvertDatabaseColumnToLocalDate() {
        DateConverter converter = new DateConverter();

        LocalDate result = converter.convertToEntityAttribute("2025-03-12");

        assertEquals(LocalDate.of(2025, 3, 12), result);
    }

    @Test
    void shouldConvertLocalDateTimeToDatabaseColumn() {
        LocalDateTimeAttributeConverter converter = new LocalDateTimeAttributeConverter();

        String result = converter.convertToDatabaseColumn(LocalDateTime.of(2025, 3, 12, 20, 0, 0));

        assertEquals("2025-03-12 20:00:00", result);
    }

    @Test
    void shouldConvertDatabaseColumnToLocalDateTime() {
        LocalDateTimeAttributeConverter converter = new LocalDateTimeAttributeConverter();

        LocalDateTime result = converter.convertToEntityAttribute("2025-03-12 20:00:00");

        assertEquals(LocalDateTime.of(2025, 3, 12, 20, 0, 0), result);
    }

    @Test
    void shouldReturnNullWhenDateIsNull() {
        DateConverter converter = new DateConverter();

        assertNull(converter.convertToDatabaseColumn(null));
        assertNull(converter.convertToEntityAttribute(null));
    }
}
