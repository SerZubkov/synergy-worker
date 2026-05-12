package ru.practice.case3.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.practice.case3.model.Worker;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorkerFileStorageTest {

    @Test
    void roundtrip_preserves_fields(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("w.tsv");
        List<Worker> original = new ArrayList<>();
        original.add(new Worker(
                "Тест Т.Т.",
                "Инженер",
                new BigDecimal("50000.25"),
                LocalDate.of(1990, 5, 5),
                LocalDate.of(2014, 3, 1)));

        WorkerFileStorage.writeAll(f, original);
        List<Worker> back = WorkerFileStorage.readAll(f);

        assertEquals(1, back.size());
        Worker w = back.get(0);
        assertEquals("Тест Т.Т.", w.getLastNameAndInitials());
        assertEquals("Инженер", w.getPosition());
        assertEquals(new BigDecimal("50000.25"), w.getSalary());
        assertEquals(LocalDate.of(1990, 5, 5), w.getBirthDate());
        assertEquals(LocalDate.of(2014, 3, 1), w.getActualStartDate());
        assertEquals(2014, w.getHireYear());
    }

    @Test
    void load_or_seed_writes_file_when_missing(@TempDir Path dir) {
        Path f = dir.resolve("missing.tsv");
        List<Worker> workers = WorkerFileStorage.loadOrSeed(f);
        assertEquals(InitialWorkers.seed().size(), workers.size());
        List<Worker> again = WorkerFileStorage.loadOrSeed(f);
        assertEquals(workers.size(), again.size());
        assertEquals(workers.get(0).getLastNameAndInitials(), again.get(0).getLastNameAndInitials());
    }

    @Test
    void read_all_rejects_wrong_field_count(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("bad.tsv");
        Files.writeString(f, "a\tb\tc\n", StandardCharsets.UTF_8);
        assertThrows(IllegalArgumentException.class, () -> WorkerFileStorage.readAll(f));
    }

    @Test
    void read_all_accepts_comma_in_salary(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("comma.tsv");
        Files.writeString(
                f,
                "Иванов И.\tРаб\t1000,5\t1990-01-01\t2015-06-01\n",
                StandardCharsets.UTF_8);
        List<Worker> w = WorkerFileStorage.readAll(f);
        assertEquals(1, w.size());
        assertEquals(new BigDecimal("1000.5"), w.get(0).getSalary());
    }

    @Test
    void load_or_seed_recovers_from_corrupt_file(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("broken.tsv");
        Files.writeString(f, "несовместимая\tстрока\n", StandardCharsets.UTF_8);
        List<Worker> recovered = WorkerFileStorage.loadOrSeed(f);
        assertEquals(InitialWorkers.seed().size(), recovered.size());
        assertDoesNotThrow(() -> WorkerFileStorage.readAll(f));
    }

    @Test
    void load_or_seed_reseeds_when_file_has_only_comments(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("only-hash.tsv");
        Files.writeString(f, "# worker-store v1\n# пусто\n", StandardCharsets.UTF_8);
        List<Worker> workers = WorkerFileStorage.loadOrSeed(f);
        assertEquals(InitialWorkers.seed().size(), workers.size());
    }

    @Test
    void roundtrip_null_birth_date(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("null-birth.tsv");
        List<Worker> original = new ArrayList<>();
        original.add(new Worker(
                "Петров П.",
                "Сторож",
                BigDecimal.TEN,
                null,
                LocalDate.of(2018, 1, 10)));

        WorkerFileStorage.writeAll(f, original);
        List<Worker> back = WorkerFileStorage.readAll(f);

        assertEquals(1, back.size());
        assertNull(back.get(0).getBirthDate());
        assertEquals(LocalDate.of(2018, 1, 10), back.get(0).getActualStartDate());
    }
}
