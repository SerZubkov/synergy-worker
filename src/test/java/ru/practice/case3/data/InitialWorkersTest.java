package ru.practice.case3.data;

import org.junit.jupiter.api.Test;
import ru.practice.case3.model.StaffRole;
import ru.practice.case3.model.Worker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InitialWorkersTest {

    @Test
    void seed_has_fixed_demo_roster() {
        List<Worker> s = InitialWorkers.seed();
        assertEquals(4, s.size());
        assertEquals("Смирнов А.В.", s.get(0).getLastNameAndInitials());
        assertEquals(StaffRole.BACKEND.getPositionTitle(), s.get(0).getPosition());
        assertEquals(new BigDecimal("125000"), s.get(0).getSalary());
        assertEquals(LocalDate.of(1988, 3, 15), s.get(0).getBirthDate());
        assertEquals(LocalDate.of(2015, 4, 1), s.get(0).getActualStartDate());
    }

    @Test
    void seed_hire_year_matches_actual_start_year() {
        for (Worker w : InitialWorkers.seed()) {
            assertEquals(w.getActualStartDate().getYear(), w.getHireYear());
        }
    }

    @Test
    void seed_contains_expected_positions() {
        List<Worker> s = InitialWorkers.seed();
        assertTrue(s.stream().anyMatch(w -> StaffRole.ANALYST.getPositionTitle().equals(w.getPosition())));
        assertTrue(s.stream().anyMatch(w -> StaffRole.FRONTEND.getPositionTitle().equals(w.getPosition())));
        assertTrue(s.stream().anyMatch(w -> StaffRole.QA.getPositionTitle().equals(w.getPosition())));
    }
}
