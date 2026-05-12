package ru.practice.case3;

import ru.practice.case3.model.Worker;
import ru.practice.case3.validation.WorkerInputValidator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class ConsoleApp {

    private static final String PRACTICE_ORG = "ООО «РСХБ-Автоматизация»";

    private ConsoleApp() {
    }

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            List<Worker> workers = new ArrayList<>();

            System.out.println("=== Учёт работников: " + PRACTICE_ORG + " ===");
            System.out.println("Кейс 3: ввод WORKER, фильтр по стажу.");

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

    private static void readWorkerFromKeyboard(Scanner in, List<Worker> workers) {
        try {
            System.out.print("Фамилия и инициалы: ");
            String fio = in.nextLine().trim();
            System.out.print("Должность: ");
            String pos = in.nextLine().trim();
            System.out.print("Зарплата (число): ");
            BigDecimal sal = new BigDecimal(in.nextLine().trim().replace(',', '.'));
            System.out.print("Год поступления на работу: ");
            int year = Integer.parseInt(in.nextLine().trim());

            WorkerInputValidator.validateNewWorker(fio, pos, sal, year);
            Worker w = new Worker(fio, pos, sal, year);
            workers.add(w);
            System.out.println("Добавлено: " + w.getLastNameAndInitials());
        } catch (NumberFormatException e) {
            System.out.println("Год и зарплату вводите числами.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
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

    private static void filterByExperience(Scanner in, List<Worker> workers) {
        if (workers.isEmpty()) {
            System.out.println("Нет ни одного работника в списке.");
            return;
        }
        try {
            System.out.print("Порог стажа в годах (кого показать — только с стажем больше): ");
            int threshold = Integer.parseInt(in.nextLine().trim());
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
        } catch (NumberFormatException e) {
            System.out.println("Нужно целое число лет.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
