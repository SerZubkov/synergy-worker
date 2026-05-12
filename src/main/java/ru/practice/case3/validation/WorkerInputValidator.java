package ru.practice.case3.validation;

import java.math.BigDecimal;
import java.time.Year;

// проверки ввода для консоли
public final class WorkerInputValidator {

    private static final int MIN_HIRE_YEAR = 1970;
    private static final int MAX_THRESHOLD_YEARS = 80;

    private WorkerInputValidator() {
    }

    public static void validateNewWorker(String lastNameAndInitials, String position,
                                         BigDecimal salary, int hireYear) {
        if (lastNameAndInitials == null || lastNameAndInitials.isBlank()) {
            throw new IllegalArgumentException("Фамилия и инициалы не могут быть пустыми.");
        }
        if (position == null || position.isBlank()) {
            throw new IllegalArgumentException("Должность не может быть пустой.");
        }
        if (salary == null || salary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Зарплата должна быть больше нуля.");
        }
        int now = Year.now().getValue();
        if (hireYear < MIN_HIRE_YEAR || hireYear > now) {
            throw new IllegalArgumentException(
                    "Год поступления: от " + MIN_HIRE_YEAR + " до " + now + " включительно.");
        }
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
