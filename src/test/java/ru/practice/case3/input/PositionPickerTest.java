package ru.practice.case3.input;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.practice.case3.model.StaffRole;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionPickerTest {

    private static Scanner scannerForLines(String lines) {
        return new Scanner(new ByteArrayInputStream(lines.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    @ParameterizedTest
    @EnumSource(
            value = StaffRole.class,
            names = {"FRONTEND", "BACKEND", "DESIGNER", "DEVOPS", "ANALYST", "QA", "MANAGER"}
    )
    void read_position_returns_catalog_title(StaffRole role) {
        int menu = role.ordinal() + 1;
        String pos = PositionPicker.readPosition(scannerForLines(menu + "\n"));
        assertEquals(role.getPositionTitle(), pos);
    }

    @Test
    void read_position_other_then_custom_title() {
        int otherMenu = StaffRole.values().length;
        String custom = "Ведущий архитектор";
        String pos = PositionPicker.readPosition(
                scannerForLines(otherMenu + "\n" + custom + "\n"));
        assertEquals(custom, pos);
    }

    @Test
    void read_position_skips_blank_then_invalid_then_ok() {
        int menu = StaffRole.ANALYST.ordinal() + 1;
        String input = "\n0\nмусор\n" + menu + "\n";
        assertEquals(StaffRole.ANALYST.getPositionTitle(), PositionPicker.readPosition(scannerForLines(input)));
    }

    @Test
    void read_non_blank_skips_empty_line() {
        Scanner in = scannerForLines("\n\nФинал\n");
        assertEquals("Финал", PositionPicker.readNonBlank(in, "P: "));
    }
}
