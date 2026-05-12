package ru.practice.case3.input;

import ru.practice.case3.model.StaffRole;

import java.util.Scanner;

/**
 * Выбор должности из нумерованного списка {@link StaffRole}; для «ДруГое» — ввод своей строки.
 */
public final class PositionPicker {

    private PositionPicker() {
    }

    public static void printStaffRoles() {
        StaffRole[] roles = StaffRole.values();
        for (int i = 0; i < roles.length; i++) {
            System.out.println("  " + (i + 1) + " — " + roles[i].getPositionTitle());
        }
    }

    /**
     * Читает номер из {@code in}, при необходимости повторяя запрос; для {@link StaffRole#OTHER} — следующую непустую строку.
     */
    public static String readPosition(Scanner in) {
        while (true) {
            System.out.println("Должность (выберите номер из списка):");
            printStaffRoles();
            System.out.print("Номер: ");
            String line = in.nextLine().trim();
            if (line.isBlank()) {
                System.out.println("Укажите номер — поле не может быть пустым.");
                continue;
            }
            try {
                int num = Integer.parseInt(line);
                StaffRole role = StaffRole.fromMenuIndex(num);
                if (role == null) {
                    System.out.println("Номер от 1 до " + StaffRole.values().length + ".");
                    continue;
                }
                if (role.isOther()) {
                    return readNonBlank(in, "Своя должность (введите текст): ");
                }
                return role.getPositionTitle();
            } catch (NumberFormatException e) {
                System.out.println("Нужен номер из списка (целое число).");
            }
        }
    }

    static String readNonBlank(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            if (!s.isBlank()) {
                return s;
            }
            System.out.println("Пустой ввод недопустим — введите значение.");
        }
    }
}
