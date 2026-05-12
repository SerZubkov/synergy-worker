package ru.practice.case3.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkerInputValidatorTest {

    @Test
    void ok_worker() {
        int now = Year.now().getValue();
        assertDoesNotThrow(() -> WorkerInputValidator.validateNewWorker(
                "Иванов И.И.", "Инженер", new BigDecimal("1000"), now - 1));
    }

    @Test
    void empty_fio() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker("  ", "Раб", BigDecimal.ONE, 2020));
    }

    @Test
    void null_fio() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(null, "Раб", BigDecimal.ONE, 2020));
    }

    @Test
    void empty_position() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker("Иванов И.", "  ", BigDecimal.ONE, 2020));
    }

    @Test
    void null_position() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker("Иванов И.", null, BigDecimal.ONE, 2020));
    }

    @Test
    void salary_not_positive() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker("Иванов И.", "Раб", BigDecimal.ZERO, 2020));
    }

    @Test
    void null_salary() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker("Иванов И.", "Раб", null, 2020));
    }

    @Test
    void negative_salary() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker("Иванов И.", "Раб", new BigDecimal("-1"), 2020));
        assertTrue(ex.getMessage().contains("Зарплата"));
    }

    @Test
    void year_out_of_range() {
        int now = Year.now().getValue();
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker("А Б.", "Раб", BigDecimal.ONE, 1969));
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker("А Б.", "Раб", BigDecimal.ONE, now + 1));
    }

    @Test
    void year_boundaries_ok() {
        int now = Year.now().getValue();
        assertDoesNotThrow(() -> WorkerInputValidator.validateNewWorker(
                "А Б.", "Раб", new BigDecimal("0.01"), 1970));
        assertDoesNotThrow(() -> WorkerInputValidator.validateNewWorker(
                "А Б.", "Раб", BigDecimal.ONE, now));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10, 80})
    void threshold_ok(int y) {
        assertDoesNotThrow(() -> WorkerInputValidator.validateExperienceThreshold(y));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 81})
    void threshold_bad(int y) {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateExperienceThreshold(y));
    }
}
