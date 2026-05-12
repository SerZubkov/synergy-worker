package ru.practice.case3.validation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

// проверки ввода для консоли
public final class WorkerInputValidator {

    private static final int MIN_HIRE_YEAR = 1970;
    private static final int MAX_THRESHOLD_YEARS = 80;
    private static final int MIN_BIRTH_YEAR = 1940;
    private static final int MIN_AGE_AT_WORK_YEARS = 16;

    private WorkerInputValidator() {
    }

    /** Дата выхода: не null, не раньше 01.01.1970, не позже сегодня. Сразу после ввода даты выхода. */
    public static void validateActualStartDate(LocalDate actualStartDate) {
        if (actualStartDate == null) {
            throw new IllegalArgumentException("Укажите дату фактического выхода на работу.");
        }
        if (actualStartDate.isBefore(LocalDate.of(MIN_HIRE_YEAR, 1, 1))) {
            throw new IllegalArgumentException("Дата выхода не раньше 01.01." + MIN_HIRE_YEAR + ".");
        }
        if (actualStartDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата фактического выхода не может быть в будущем.");
        }
    }

    /** Возраст на дату выхода ≥ 16 лет; дата выхода не раньше дня рождения. Вызывать сразу после ввода дат. */
    public static void validateAgeAtStart(LocalDate birthDate, LocalDate actualStartDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Укажите дату рождения.");
        }
        if (actualStartDate == null) {
            throw new IllegalArgumentException("Укажите дату фактического выхода на работу.");
        }
        if (actualStartDate.isBefore(birthDate)) {
            throw new IllegalArgumentException("Дата выхода на работу не раньше дня рождения.");
        }
        int fullYears = Period.between(birthDate, actualStartDate).getYears();
        if (fullYears < MIN_AGE_AT_WORK_YEARS) {
            throw new IllegalArgumentException(
                    "На дату выхода возраст должен быть не меньше " + MIN_AGE_AT_WORK_YEARS + " лет.");
        }
    }

    public static void validateNewWorker(
            String lastNameAndInitials,
            String position,
            BigDecimal salary,
            LocalDate birthDate,
            LocalDate actualStartDate
    ) {
        if (lastNameAndInitials == null || lastNameAndInitials.isBlank()) {
            throw new IllegalArgumentException("Фамилия и инициалы не могут быть пустыми.");
        }
        if (position == null || position.isBlank()) {
            throw new IllegalArgumentException("Должность не может быть пустой.");
        }
        if (salary == null || salary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Зарплата должна быть больше нуля.");
        }
        LocalDate today = LocalDate.now();
        if (birthDate == null) {
            throw new IllegalArgumentException("Укажите дату рождения.");
        }
        if (birthDate.getYear() < MIN_BIRTH_YEAR) {
            throw new IllegalArgumentException("Год рождения не раньше " + MIN_BIRTH_YEAR + ".");
        }
        if (birthDate.isAfter(today)) {
            throw new IllegalArgumentException("Дата рождения не может быть в будущем.");
        }
        validateActualStartDate(actualStartDate);
        validateAgeAtStart(birthDate, actualStartDate);
    }

    public static void validateExperienceThreshold(int years) {
        if (years < 0) {
            throw new IllegalArgumentException("Порог стажа не может быть отрицательным.");
        }
        if (years > MAX_THRESHOLD_YEARS) {
            throw new IllegalArgumentException("Слишком большое число (макс. " + MAX_THRESHOLD_YEARS + ").");
        }
    }
}
