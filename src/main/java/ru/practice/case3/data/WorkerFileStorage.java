package ru.practice.case3.data;

import ru.practice.case3.model.Worker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Локальный файл списка сотрудников (TSV, UTF-8). Путь по умолчанию — {@code data/workers.tsv}
 * относительно рабочей директории процесса.
 */
public final class WorkerFileStorage {

    private WorkerFileStorage() {
    }

    public static Path defaultPath() {
        return Path.of(System.getProperty("user.dir"), "data", "workers.tsv");
    }

    /**
     * Если файла нет, он пустой или битый — подставляются {@link InitialWorkers#seed()} и сразу пишутся на диск.
     * Иначе читается содержимое файла.
     */
    public static List<Worker> loadOrSeed(Path path) {
        try {
            if (!Files.exists(path)) {
                return writeSeedAndReturn(path);
            }
            List<Worker> loaded = readAll(path);
            if (loaded.isEmpty()) {
                return writeSeedAndReturn(path);
            }
            return new ArrayList<>(loaded);
        } catch (IOException | DateTimeParseException | IllegalArgumentException e) {
            System.err.println("Файл данных повреждён или недоступен, восстановлены демо-данные: " + e.getMessage());
            try {
                return writeSeedAndReturn(path);
            } catch (IOException ex) {
                System.err.println("Не удалось записать файл данных: " + ex.getMessage());
                return new ArrayList<>(InitialWorkers.seed());
            }
        }
    }

    private static List<Worker> writeSeedAndReturn(Path path) throws IOException {
        List<Worker> seed = InitialWorkers.seed();
        writeAll(path, seed);
        return new ArrayList<>(seed);
    }

    public static List<Worker> readAll(Path path) throws IOException {
        List<Worker> out = new ArrayList<>();
        for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            String t = line.trim();
            if (t.isEmpty() || t.startsWith("#")) {
                continue;
            }
            String[] p = t.split("\t", -1);
            if (p.length != 5) {
                throw new IllegalArgumentException("ожидается 5 полей, строка: " + t);
            }
            LocalDate birth = p[3].isBlank() ? null : LocalDate.parse(p[3]);
            LocalDate actual = LocalDate.parse(p[4]);
            out.add(new Worker(
                    p[0],
                    p[1],
                    new BigDecimal(p[2].replace(',', '.')),
                    birth,
                    actual
            ));
        }
        return out;
    }

    public static void writeAll(Path path, List<Worker> workers) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter w = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            w.write("# worker-store v1: ФИО, должность, зарплата, дата рождения, дата выхода (ISO)\n");
            for (Worker worker : workers) {
                w.write(encodeLine(worker));
                w.write('\n');
            }
        }
    }

    private static String encodeLine(Worker w) {
        String birth = w.getBirthDate() != null ? w.getBirthDate().toString() : "";
        return String.join("\t",
                w.getLastNameAndInitials(),
                w.getPosition(),
                w.getSalary().toPlainString(),
                birth,
                w.getActualStartDate().toString());
    }
}
