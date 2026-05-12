package ru.practice.case3.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class RuInputFormatsTest {

    @ParameterizedTest
    @CsvSource({
            "1.1.2020, 2020-01-01",
            "01.01.2020, 2020-01-01",
            "31.12.1999, 1999-12-31",
            "15.03.1990, 1990-03-15",
            " 5.6.1985 , 1985-06-05"
    })
    void parse_date_dmy_ok(String input, String isoExpected) {
        assertEquals(LocalDate.parse(isoExpected), RuInputFormats.parseDateDmy(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "32.01.2020", "99.99.2020", "2020-01-15", "абв"})
    void parse_date_invalid(String bad) {
        assertThrows(DateTimeParseException.class, () -> RuInputFormats.parseDateDmy(bad));
    }

    static Stream<Arguments> moneyOk() {
        return Stream.of(
                arguments("75000", "75000"),
                arguments(" 75000,50 ", "75000.50"),
                arguments("0.01", "0.01"),
                arguments("1000000", "1000000"),
                arguments("1", "1")
        );
    }

    @ParameterizedTest
    @MethodSource("moneyOk")
    void parse_money_ok(String input, String expectedPlain) {
        assertEquals(0, new BigDecimal(expectedPlain).compareTo(RuInputFormats.parseMoney(input)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "abc", "12..5"})
    void parse_money_invalid(String bad) {
        assertThrows(NumberFormatException.class, () -> RuInputFormats.parseMoney(bad));
    }

    @Test
    void parse_money_comma_decimal_separator() {
        assertEquals(0, new BigDecimal("1234.56").compareTo(RuInputFormats.parseMoney("1234,56")));
    }
}
