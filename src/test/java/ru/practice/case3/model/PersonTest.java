package ru.practice.case3.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {

    @Test
    void constructor_null_becomes_empty() {
        Person p = new Person(null);
        assertEquals("", p.getLastNameAndInitials());
        assertEquals("", p.getSurname());
    }

    @Test
    void setLastNameAndInitials_null_becomes_empty() {
        Person p = new Person("Иванов И.И.");
        p.setLastNameAndInitials(null);
        assertEquals("", p.getLastNameAndInitials());
        assertEquals("", p.getSurname());
    }

    @Test
    void surname_trim_and_first_token() {
        Person p = new Person("  Петров П.П.  ");
        assertEquals("Петров", p.getSurname());
    }

    @Test
    void surname_single_word() {
        assertEquals("Сидоров", new Person("Сидоров").getSurname());
    }
}
