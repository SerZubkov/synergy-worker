package ru.practice.case3;

import org.junit.jupiter.api.Test;
import ru.practice.case3.model.StaffRole;
import ru.practice.case3.model.Worker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleAppTest {

    private static Scanner scanner(String lines) {
        return new Scanner(
                new ByteArrayInputStream(lines.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);
    }

    @Test
    void read_worker_retries_after_validation_error_then_adds_one() {
        List<Worker> workers = new ArrayList<>();
        String in = """
                Петров П.П.
                1
                0
                01.01.1990
                01.06.2010
                Петров П.П.
                1
                50000
                01.01.1990
                01.06.2010
                """;
        ConsoleApp.readWorkerFromKeyboard(scanner(in), workers);
        assertEquals(1, workers.size());
        assertEquals("Петров П.П.", workers.get(0).getLastNameAndInitials());
        assertEquals(new BigDecimal("50000"), workers.get(0).getSalary());
        assertEquals(StaffRole.FRONTEND.getPositionTitle(), workers.get(0).getPosition());
    }

    @Test
    void read_worker_second_attempt_after_age_error() {
        List<Worker> workers = new ArrayList<>();
        String in = """
                Иванов И.И.
                2
                1000
                01.01.2010
                01.06.2020
                Иванов И.И.
                2
                1000
                01.01.1990
                01.06.2010
                """;
        ConsoleApp.readWorkerFromKeyboard(scanner(in), workers);
        assertEquals(1, workers.size());
        assertEquals(LocalDate.of(1990, 1, 1), workers.get(0).getBirthDate());
    }

    @Test
    void filter_by_experience_retries_after_invalid_threshold() {
        List<Worker> workers = new ArrayList<>();
        workers.add(new Worker(
                "Сидоров С.С.",
                "Раб",
                BigDecimal.ONE,
                LocalDate.of(1980, 1, 1),
                LocalDate.of(2010, 6, 1)));

        String userInput = """
                81
                0
                """;
        PrintStream oldOut = System.out;
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            System.setOut(new PrintStream(buf, true, StandardCharsets.UTF_8));
            ConsoleApp.filterByExperience(scanner(userInput), workers);
            String out = buf.toString(StandardCharsets.UTF_8);
            assertTrue(out.contains("80"), out);
            assertTrue(out.contains("Сидоров"), out);
        } finally {
            System.setOut(oldOut);
        }
    }
}
