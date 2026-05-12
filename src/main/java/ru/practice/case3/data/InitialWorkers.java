package ru.practice.case3.data;

import ru.practice.case3.model.StaffRole;
import ru.practice.case3.model.Worker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Демонстрационный состав сотрудников при первом запуске (до появления локального файла). */
public final class InitialWorkers {

    private InitialWorkers() {
    }

    public static List<Worker> seed() {
        return List.of(
                new Worker(
                        "Смирнов А.В.",
                        StaffRole.BACKEND.getPositionTitle(),
                        new BigDecimal("125000"),
                        LocalDate.of(1988, 3, 15),
                        LocalDate.of(2015, 4, 1)),
                new Worker(
                        "Кузнецова Е.И.",
                        StaffRole.ANALYST.getPositionTitle(),
                        new BigDecimal("98000.50"),
                        LocalDate.of(1992, 7, 22),
                        LocalDate.of(2018, 9, 10)),
                new Worker(
                        "Волков Д.С.",
                        StaffRole.FRONTEND.getPositionTitle(),
                        new BigDecimal("110000"),
                        LocalDate.of(1995, 11, 5),
                        LocalDate.of(2021, 2, 15)),
                new Worker(
                        "Новикова М.П.",
                        StaffRole.QA.getPositionTitle(),
                        new BigDecimal("87000"),
                        LocalDate.of(1999, 1, 30),
                        LocalDate.of(2022, 6, 1))
        );
    }
}
