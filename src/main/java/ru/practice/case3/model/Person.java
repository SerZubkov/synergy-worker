package ru.practice.case3.model;

import java.util.Objects;

// База для Worker — только ФИО в одной строке
public class Person {

    private String lastNameAndInitials;

    public Person() {
        this.lastNameAndInitials = "";
    }

    public Person(String lastNameAndInitials) {
        this.lastNameAndInitials = Objects.requireNonNullElse(lastNameAndInitials, "");
    }

    public String getLastNameAndInitials() {
        return lastNameAndInitials;
    }

    public void setLastNameAndInitials(String lastNameAndInitials) {
        this.lastNameAndInitials = lastNameAndInitials != null ? lastNameAndInitials : "";
    }

    // до первого пробела — считаем фамилией (для пункта 3)
    public String getSurname() {
        String s = lastNameAndInitials == null ? "" : lastNameAndInitials.trim();
        int sp = s.indexOf(' ');
        return sp > 0 ? s.substring(0, sp) : s;
    }
}
