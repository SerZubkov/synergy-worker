package ru.practice.case3.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.practice.case3.model.StaffRole;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkerInputValidatorTest {

    @Test
    void actual_start_date_early_fails_future() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateActualStartDate(LocalDate.now().plusDays(1)));
    }

    @Test
    void actual_start_date_early_message_future() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateActualStartDate(LocalDate.of(2026, 12, 12)));
        assertTrue(ex.getMessage().contains("будущем"));
    }

    @Test
    void actual_start_date_early_fails_before_1970() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateActualStartDate(LocalDate.of(1969, 12, 31)));
    }

    @Test
    void actual_start_date_early_null() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateActualStartDate(null));
    }

    @Test
    void actual_start_date_early_ok_today() {
        assertDoesNotThrow(() -> WorkerInputValidator.validateActualStartDate(LocalDate.now()));
    }

    @Test
    void actual_start_date_early_ok_1970() {
        assertDoesNotThrow(() -> WorkerInputValidator.validateActualStartDate(LocalDate.of(1970, 1, 1)));
    }

    @Test
    void age_at_start_ok_on_sixteenth_birthday() {
        assertDoesNotThrow(() -> WorkerInputValidator.validateAgeAtStart(
                LocalDate.of(2000, 6, 10),
                LocalDate.of(2016, 6, 10)));
    }

    @Test
    void age_at_start_fails_day_before_sixteenth_birthday() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateAgeAtStart(
                        LocalDate.of(2000, 6, 11),
                        LocalDate.of(2016, 6, 10)));
    }

    @Test
    void age_at_start_fails_fifteen_full_calendar_years() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateAgeAtStart(
                        LocalDate.of(2005, 1, 1),
                        LocalDate.of(2020, 1, 1)));
    }

    @Test
    void age_at_start_fails_actual_before_birth() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateAgeAtStart(
                        LocalDate.of(2000, 1, 1),
                        LocalDate.of(1999, 1, 1)));
        assertTrue(ex.getMessage().contains("не раньше дня рождения"));
    }

    @Test
    void age_at_start_null_birth() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateAgeAtStart(null, LocalDate.of(2020, 1, 1)));
    }

    @Test
    void age_at_start_null_actual() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateAgeAtStart(LocalDate.of(1990, 1, 1), null));
    }

    @Test
    void too_young_message_via_validate_age_at_start() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateAgeAtStart(
                        LocalDate.of(2005, 1, 1),
                        LocalDate.of(2020, 1, 1)));
        assertTrue(ex.getMessage().contains("16"));
    }

    @Test
    void ok_worker() {
        int now = Year.now().getValue();
        assertDoesNotThrow(() -> WorkerInputValidator.validateNewWorker(
                "Иванов И.И.",
                StaffRole.BACKEND.getPositionTitle(),
                new BigDecimal("1000"),
                LocalDate.of(1980, 5, 5),
                LocalDate.of(now - 1, 7, 1)));
    }

    @Test
    void empty_fio() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "  ", "Раб", BigDecimal.ONE,
                        LocalDate.of(1970, 1, 1), LocalDate.of(2020, 1, 1)));
    }

    @Test
    void null_fio() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        null, "Раб", BigDecimal.ONE,
                        LocalDate.of(1970, 1, 1), LocalDate.of(2020, 1, 1)));
    }

    @Test
    void empty_position() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "Иванов И.", "  ", BigDecimal.ONE,
                        LocalDate.of(1970, 1, 1), LocalDate.of(2020, 1, 1)));
    }

    @Test
    void null_position() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "Иванов И.", null, BigDecimal.ONE,
                        LocalDate.of(1970, 1, 1), LocalDate.of(2020, 1, 1)));
    }

    @Test
    void salary_not_positive() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "Иванов И.", "Раб", BigDecimal.ZERO,
                        LocalDate.of(1970, 1, 1), LocalDate.of(2020, 1, 1)));
    }

    @Test
    void null_salary() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "Иванов И.", "Раб", null,
                        LocalDate.of(1970, 1, 1), LocalDate.of(2020, 1, 1)));
    }

    @Test
    void negative_salary() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "Иванов И.", "Раб", new BigDecimal("-1"),
                        LocalDate.of(1970, 1, 1), LocalDate.of(2020, 1, 1)));
        assertTrue(ex.getMessage().contains("Зарплата"));
    }

    @Test
    void actual_start_boundaries_ok() {
        int now = Year.now().getValue();
        assertDoesNotThrow(() -> WorkerInputValidator.validateNewWorker(
                "А Б.", "Раб", new BigDecimal("0.01"),
                LocalDate.of(1950, 1, 1), LocalDate.of(1970, 1, 1)));
        assertDoesNotThrow(() -> WorkerInputValidator.validateNewWorker(
                "А Б.", "Раб", BigDecimal.ONE,
                LocalDate.of(1955, 1, 1), LocalDate.of(now, 1, 1)));
    }

    @Test
    void birth_in_future() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "А Б.", "Раб", BigDecimal.ONE,
                        LocalDate.now().plusDays(1),
                        LocalDate.of(2020, 1, 1)));
    }

    @Test
    void actual_start_before_birth() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "А Б.", "Раб", BigDecimal.ONE,
                        LocalDate.of(2000, 1, 1),
                        LocalDate.of(1995, 1, 1)));
    }

    @Test
    void too_young_at_start() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "А Б.", "Раб", BigDecimal.ONE,
                        LocalDate.of(2005, 1, 1),
                        LocalDate.of(2020, 1, 1)));
    }

    @Test
    void null_birth() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "А Б.", "Раб", BigDecimal.ONE,
                        null,
                        LocalDate.of(2020, 1, 1)));
    }

    @Test
    void null_actual_start() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "А Б.", "Раб", BigDecimal.ONE,
                        LocalDate.of(1970, 1, 1),
                        null));
    }

    @Test
    void birth_year_before_minimum() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "А Б.", "Раб", BigDecimal.ONE,
                        LocalDate.of(1939, 12, 31),
                        LocalDate.of(2020, 1, 1)));
    }

    @Test
    void actual_start_in_future() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "А Б.", "Раб", BigDecimal.ONE,
                        LocalDate.of(1970, 1, 1),
                        LocalDate.now().plusDays(1)));
    }

    @Test
    void actual_start_before_1970() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateNewWorker(
                        "А Б.", "Раб", BigDecimal.ONE,
                        LocalDate.of(1940, 1, 1),
                        LocalDate.of(1969, 12, 31)));
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

    @Test
    void threshold_negative_message() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateExperienceThreshold(-5));
        assertTrue(ex.getMessage().contains("отрицательн"));
    }

    @Test
    void threshold_over_max_message() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> WorkerInputValidator.validateExperienceThreshold(81));
        assertTrue(ex.getMessage().contains("80"));
    }

    @Test
    void validate_age_at_start_ok_sixteen_years_calendar() {
        assertDoesNotThrow(() -> WorkerInputValidator.validateAgeAtStart(
                LocalDate.of(2004, 1, 1),
                LocalDate.of(2020, 1, 1)));
    }

    @Test
    void validate_new_worker_exactly_sixteen_at_start_ok() {
        assertDoesNotThrow(() -> WorkerInputValidator.validateNewWorker(
                "А Б.",
                "Раб",
                BigDecimal.ONE,
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2016, 1, 1)));
    }
}
