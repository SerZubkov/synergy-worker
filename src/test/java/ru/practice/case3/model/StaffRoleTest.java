package ru.practice.case3.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StaffRoleTest {

    @Test
    void from_menu_first_and_last() {
        assertEquals(StaffRole.FRONTEND, StaffRole.fromMenuIndex(1));
        assertEquals(StaffRole.OTHER, StaffRole.fromMenuIndex(StaffRole.values().length));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 100})
    void from_menu_out_of_range(int index) {
        assertNull(StaffRole.fromMenuIndex(index));
    }

    @Test
    void other_flag() {
        assertTrue(StaffRole.OTHER.isOther());
        assertFalse(StaffRole.BACKEND.isOther());
    }

    @Test
    void titles_non_blank_except_other_uses_title_for_display_in_list() {
        for (StaffRole r : StaffRole.values()) {
            assertNotNull(r.getPositionTitle());
            assertFalse(r.getPositionTitle().isBlank());
        }
    }

    @Test
    void menu_round_trip() {
        StaffRole[] all = StaffRole.values();
        for (int i = 0; i < all.length; i++) {
            assertEquals(all[i], StaffRole.fromMenuIndex(i + 1));
        }
    }

    @Test
    void from_menu_index_after_last_is_null() {
        assertNull(StaffRole.fromMenuIndex(StaffRole.values().length + 1));
    }

    @Test
    void position_titles_distinct_for_fixed_roles() {
        long distinct = java.util.Arrays.stream(StaffRole.values())
                .map(StaffRole::getPositionTitle)
                .distinct()
                .count();
        assertEquals(StaffRole.values().length, distinct);
    }
}
