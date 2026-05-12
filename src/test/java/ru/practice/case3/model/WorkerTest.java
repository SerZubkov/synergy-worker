package ru.practice.case3.model;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkerTest {

    @Test
    void surname() {
        assertEquals("Иванов", new Worker("Иванов И.И.", "Инженер", 2020).getSurname());
        assertEquals("Петров", new Worker("Петров", "Кладовщик", 2021).getSurname());
    }

    @Test
    void stazh() {
        Worker w = new Worker("Иванов И.И.", "Инженер", new BigDecimal("100"), 2020);
        assertEquals(6, w.getExperienceYears(2026));
        assertEquals(0, w.getExperienceYears(2020));
        assertEquals(0, w.getExperienceYears(2019));
    }

    @Test
    void stazh_bolshe_poroga() {
        Worker w = new Worker("Петров П.П.", "Бухгалтер", 2019);
        int now = 2026;
        assertTrue(w.isExperienceExceeding(6, now));
        assertFalse(w.isExperienceExceeding(7, now));
    }

    @Test
    void changeAllFields() {
        Worker w = new Worker();
        w.changeAllFields(
                "Сидоров С.С.",
                StaffRole.DESIGNER.getPositionTitle(),
                new BigDecimal("55000"),
                LocalDate.of(1995, 2, 2),
                LocalDate.of(2021, 3, 1)
        );
        assertEquals("Сидоров С.С.", w.getLastNameAndInitials());
        assertEquals(StaffRole.DESIGNER.getPositionTitle(), w.getPosition());
        assertEquals(new BigDecimal("55000"), w.getSalary());
        assertEquals(2021, w.getHireYear());
        assertEquals(LocalDate.of(1995, 2, 2), w.getBirthDate());
        assertEquals(LocalDate.of(2021, 3, 1), w.getActualStartDate());
    }

    @Test
    void display() {
        Worker w = new Worker(
                "А А.А.",
                StaffRole.FRONTEND.getPositionTitle(),
                new BigDecimal("1"),
                LocalDate.of(2003, 1, 1),
                LocalDate.of(2025, 1, 1)
        );
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        w.display(new PrintStream(buf, true, StandardCharsets.UTF_8));
        String line = buf.toString(StandardCharsets.UTF_8).trim();
        assertTrue(line.contains("А А.А."));
        assertTrue(line.contains(StaffRole.FRONTEND.getPositionTitle()));
        assertTrue(line.contains("руб."));
        assertTrue(line.contains("2025"));
    }

    @Test
    void toString_contains_main_parts() {
        Worker w = new Worker("Иванов И.И.", "Инженер", new BigDecimal("50000"), 2020);
        String s = w.toString();
        assertTrue(s.contains("Иванов И.И."));
        assertTrue(s.contains("Инженер"));
        assertTrue(s.contains("50000"));
        assertTrue(s.contains("2020"));
        assertTrue(s.contains("стаж"));
    }

    @Test
    void full_constructor_hire_year_from_actual_start() {
        Worker w = new Worker(
                "П П.П.",
                "Тест",
                BigDecimal.TEN,
                LocalDate.of(1990, 6, 15),
                LocalDate.of(2024, 3, 20)
        );
        assertEquals(2024, w.getHireYear());
        assertEquals(LocalDate.of(2024, 3, 20), w.getActualStartDate());
    }

    @Test
    void default_constructor_sets_sane_defaults() {
        Worker w = new Worker();
        assertEquals("", w.getLastNameAndInitials());
        assertEquals("", w.getPosition());
        assertEquals(BigDecimal.ZERO, w.getSalary());
        assertEquals(Year.now().getValue(), w.getHireYear());
    }

    @Test
    void three_arg_constructor_zero_salary() {
        Worker w = new Worker("А Б.", "Вахтёр", 2018);
        assertEquals(BigDecimal.ZERO, w.getSalary());
    }

    @Test
    void constructor_null_position_and_salary() {
        Worker w = new Worker("Козлов К.К.", null, null, 2017);
        assertEquals("", w.getPosition());
        assertEquals(BigDecimal.ZERO, w.getSalary());
    }

    @Test
    void is_experience_not_exceeding_when_equals_threshold() {
        Worker w = new Worker("Л Л.Л.", "Охранник", 2020);
        int now = 2026;
        assertEquals(6, w.getExperienceYears(now));
        assertFalse(w.isExperienceExceeding(6, now));
        assertTrue(w.isExperienceExceeding(5, now));
    }

    @Test
    void get_experience_years_matches_now() {
        int y = Year.now().getValue();
        Worker w = new Worker("М М.М.", "Уборщик", y - 3);
        assertEquals(3, w.getExperienceYears());
        assertEquals(w.getExperienceYears(y), w.getExperienceYears());
    }
}
