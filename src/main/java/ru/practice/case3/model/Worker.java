package ru.practice.case3.model;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Worker extends Person {

    private String position;
    private BigDecimal salary;
    /** Календарный год даты фактического выхода (для вывода и задания по кейсу). */
    private int hireYear;
    /** День рождения; может быть null у «пустого» работника */
    private LocalDate birthDate;
    /** Фактическая дата выхода на работу (первый рабочий день) */
    private LocalDate actualStartDate;

    public Worker() {
        super();
        this.position = "";
        this.salary = BigDecimal.ZERO;
        int y = java.time.Year.now().getValue();
        this.hireYear = y;
        this.birthDate = null;
        this.actualStartDate = LocalDate.of(y, 1, 1);
    }

    /**
     * Короткий конструктор для обратной совместимости: выход 01.01 указанного года,
     * учётный год = этот же год.
     */
    public Worker(String lastNameAndInitials, String position, BigDecimal salary, int hireYear) {
        this(
                lastNameAndInitials,
                position,
                salary,
                defaultBirthForLegacyHire(hireYear),
                LocalDate.of(hireYear, 1, 1)
        );
    }

    public Worker(String lastNameAndInitials, String position, int hireYear) {
        this(lastNameAndInitials, position, BigDecimal.ZERO, hireYear);
    }

    public Worker(
            String lastNameAndInitials,
            String position,
            BigDecimal salary,
            LocalDate birthDate,
            LocalDate actualStartDate
    ) {
        super(lastNameAndInitials);
        this.position = Objects.requireNonNullElse(position, "");
        this.salary = salary != null ? salary : BigDecimal.ZERO;
        this.birthDate = birthDate;
        this.actualStartDate = actualStartDate != null ? actualStartDate : LocalDate.of(1970, 1, 1);
        this.hireYear = this.actualStartDate.getYear();
    }

    private static LocalDate defaultBirthForLegacyHire(int hireYear) {
        int y = Math.max(1950, hireYear - 25);
        return LocalDate.of(y, 1, 1);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position != null ? position : "";
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary != null ? salary : BigDecimal.ZERO;
    }

    public int getHireYear() {
        return hireYear;
    }

    public void setHireYear(int hireYear) {
        this.hireYear = hireYear;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(LocalDate actualStartDate) {
        this.actualStartDate = actualStartDate != null ? actualStartDate : LocalDate.of(hireYear, 1, 1);
        this.hireYear = this.actualStartDate.getYear();
    }

    /**
     * Стаж в полных годах от фактической даты выхода до конца календарного года {@code currentYear}.
     */
    public int getExperienceYears(int currentYear) {
        LocalDate end = LocalDate.of(currentYear, 12, 31);
        long years = ChronoUnit.YEARS.between(actualStartDate, end);
        return (int) Math.max(0, years);
    }

    public int getExperienceYears() {
        return (int) Math.max(0, ChronoUnit.YEARS.between(actualStartDate, LocalDate.now()));
    }

    /** Стаж строго больше порога (лет) */
    public boolean isExperienceExceeding(int thresholdYears, int currentYear) {
        return getExperienceYears(currentYear) > thresholdYears;
    }

    public void changeAllFields(
            String lastNameAndInitials,
            String position,
            BigDecimal salary,
            LocalDate birthDate,
            LocalDate actualStartDate
    ) {
        setLastNameAndInitials(lastNameAndInitials);
        setPosition(position);
        setSalary(salary);
        setBirthDate(birthDate);
        setActualStartDate(actualStartDate);
    }

    public void display(PrintStream out) {
        String birth = birthDate != null ? birthDate.toString() : "—";
        out.printf(
                "%s | %s | %s руб. | год выхода: %d | факт. выход: %s | ДР: %s | стаж: %d лет%n",
                getLastNameAndInitials(),
                position,
                salary.toPlainString(),
                hireYear,
                actualStartDate,
                birth,
                getExperienceYears()
        );
    }

    @Override
    public String toString() {
        String birth = birthDate != null ? birthDate.toString() : "—";
        return getLastNameAndInitials()
                + " — "
                + position
                + ", "
                + salary.toPlainString()
                + " руб., год выхода: "
                + hireYear
                + ", дата выхода: "
                + actualStartDate
                + ", ДР: "
                + birth
                + " (стаж "
                + getExperienceYears()
                + " лет)";
    }
}
