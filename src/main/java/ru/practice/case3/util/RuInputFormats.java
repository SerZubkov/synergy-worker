package ru.practice.case3.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/** Парсинг строк ввода консоли: дата дд.мм.гггг и денежная сумма с запятой или точкой */
public final class RuInputFormats {

    private static final DateTimeFormatter DATE_D_M_YYYY = DateTimeFormatter.ofPattern("d.M.uuuu");

    private RuInputFormats() {
    }

    public static LocalDate parseDateDmy(String line) throws DateTimeParseException {
        return LocalDate.parse(line.trim(), DATE_D_M_YYYY);
    }

    public static BigDecimal parseMoney(String line) throws NumberFormatException {
        String normalized = line.trim().replace(',', '.');
        return new BigDecimal(normalized);
    }
}
