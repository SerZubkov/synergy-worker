package ru.practice.case3;

import ru.practice.case3.data.WorkerFileStorage;
import ru.practice.case3.input.PositionPicker;
import ru.practice.case3.model.Worker;
import ru.practice.case3.validation.WorkerInputValidator;

import ru.practice.case3.util.RuInputFormats;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class ConsoleApp {

    private static final String PRACTICE_ORG = "ООО «РСХБ-Автоматизация»";

    private ConsoleApp() {
    }

    public static void main(String[] args) {
        Path storePath = WorkerFileStorage.defaultPath();
        List<Worker> workers = WorkerFileStorage.loadOrSeed(storePath);

        System.out.println("=== Учёт работников: " + PRACTICE_ORG + " ===");
        System.out.println("Сотрудников в списке: " + workers.size() + ". Файл данных: " + storePath.toAbsolutePath());

        try (Scanner in = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = in.nextLine().trim();
                switch (choice) {
                    case "1" -> readWorkerFromKeyboard(in, workers);
                    case "2" -> printAll(workers);
                    case "3" -> filterByExperience(in, workers);
                    case "0" -> running = false;
                    default -> System.out.println("Неизвестная команда, попробуйте снова.");
                }
            }
            System.out.println("Выход.");
        } finally {
            try {
                WorkerFileStorage.writeAll(storePath, workers);
            } catch (IOException e) {
                System.err.println("Не удалось сохранить список в файл: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1 — добавить работника (ввод с клавиатуры)");
        System.out.println("2 — показать всех (все поля на дисплей)");
        System.out.println("3 — фамилии: стаж строго больше введённого числа лет");
        System.out.println("0 — выход");
        System.out.print("> ");
    }

    /** Повторяет запрос, пока строка не будет непустой (без одних пробелов). */
    private static String readNonBlank(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();
            if (!line.isBlank()) {
                return line;
            }
            System.out.println("Пустой ввод недопустим — введите значение.");
        }
    }

    private static BigDecimal readBigDecimal(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();
            if (line.isBlank()) {
                System.out.println("Зарплата не может быть пустой — введите число.");
                continue;
            }
            try {
                return RuInputFormats.parseMoney(line);
            } catch (NumberFormatException e) {
                System.out.println("Нужно число, например 75000 или 75000,5.");
            }
        }
    }

    private static int readInt(Scanner in, String prompt, String emptyMessage) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();
            if (line.isBlank()) {
                System.out.println(emptyMessage);
                continue;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Нужно целое число.");
            }
        }
    }

    private static LocalDate readLocalDate(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();
            if (line.isBlank()) {
                System.out.println("Дата не может быть пустой — введите дд.мм.гггг.");
                continue;
            }
            try {
                return RuInputFormats.parseDateDmy(line);
            } catch (DateTimeParseException e) {
                System.out.println("Формат: дд.мм.гггг (например 15.03.1990).");
            }
        }
    }

    static void readWorkerFromKeyboard(Scanner in, List<Worker> workers) {
        while (true) {
            try {
                String fio = readNonBlank(in, "Фамилия и инициалы: ");
                String pos = PositionPicker.readPosition(in);
                BigDecimal sal = readBigDecimal(in, "Зарплата (число): ");

                LocalDate birth = readLocalDate(in, "Дата рождения (дд.мм.гггг): ");
                LocalDate actualStart = readLocalDate(
                        in, "Дата фактического выхода на работу (дд.мм.гггг): ");

                WorkerInputValidator.validateActualStartDate(actualStart);
                WorkerInputValidator.validateAgeAtStart(birth, actualStart);

                WorkerInputValidator.validateNewWorker(fio, pos, sal, birth, actualStart);
                Worker w = new Worker(fio, pos, sal, birth, actualStart);
                workers.add(w);
                System.out.println("Добавлено: " + w.getLastNameAndInitials());
                return;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Введите данные работника заново (с фамилии и инициалов).");
            } catch (Exception e) {
                System.out.println("Ошибка ввода: " + e.getMessage());
                System.out.println("Введите данные работника заново (с фамилии и инициалов).");
            }
        }
    }

    private static void printAll(List<Worker> workers) {
        if (workers.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }
        for (Worker w : workers) {
            w.display(System.out);
        }
    }

    static void filterByExperience(Scanner in, List<Worker> workers) {
        if (workers.isEmpty()) {
            System.out.println("Нет ни одного работника в списке.");
            return;
        }
        while (true) {
            try {
                int threshold = readInt(
                        in,
                        "Порог стажа в годах (кого показать — только с стажем больше): ",
                        "Порог не может быть пустым — введите число лет.");
                WorkerInputValidator.validateExperienceThreshold(threshold);
                int yearNow = java.time.Year.now().getValue();
                List<Worker> found = new ArrayList<>();
                for (Worker w : workers) {
                    if (w.isExperienceExceeding(threshold, yearNow)) {
                        found.add(w);
                    }
                }
                if (found.isEmpty()) {
                    System.out.println("Работников, чей стаж превышает введённое значение, нет.");
                } else {
                    System.out.println("Фамилии (стаж больше " + threshold + " лет):");
                    for (Worker w : found) {
                        System.out.println("  • " + w.getSurname());
                    }
                }
                return;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Введите порог ещё раз.");
            }
        }
    }
}
