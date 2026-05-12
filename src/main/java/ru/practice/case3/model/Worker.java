package ru.practice.case3.model;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.Year;
import java.util.Objects;

public class Worker extends Person {

    private String position;
    private BigDecimal salary;
    private int hireYear;

    public Worker() {
        super();
        this.position = "";
        this.salary = BigDecimal.ZERO;
        this.hireYear = Year.now().getValue();
    }

    public Worker(String lastNameAndInitials, String position, BigDecimal salary, int hireYear) {
        super(lastNameAndInitials);
        this.position = Objects.requireNonNullElse(position, "");
        this.salary = salary != null ? salary : BigDecimal.ZERO;
        this.hireYear = hireYear;
    }

    public Worker(String lastNameAndInitials, String position, int hireYear) {
        this(lastNameAndInitials, position, BigDecimal.ZERO, hireYear);
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

    public int getExperienceYears(int currentYear) {
        return Math.max(0, currentYear - hireYear);
    }

    public int getExperienceYears() {
        return getExperienceYears(Year.now().getValue());
    }

    // строго больше N лет, как в формулировке «превышает»
    public boolean isExperienceExceeding(int thresholdYears, int currentYear) {
        return getExperienceYears(currentYear) > thresholdYears;
    }

    public void changeAllFields(String lastNameAndInitials, String position, BigDecimal salary, int hireYear) {
        setLastNameAndInitials(lastNameAndInitials);
        setPosition(position);
        setSalary(salary);
        setHireYear(hireYear);
    }

    public void display(PrintStream out) {
        out.printf(
                "%s | %s | %s руб. | год поступления: %d | стаж: %d лет%n",
                getLastNameAndInitials(),
                position,
                salary.toPlainString(),
                hireYear,
                getExperienceYears()
        );
    }

    @Override
    public String toString() {
        return getLastNameAndInitials() + " — " + position + ", " + salary.toPlainString()
                + " руб., с " + hireYear + " г. (стаж " + getExperienceYears() + " лет)";
    }
}
