package es;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EventTest {
    @Test
    void event_rejectsEndBeforeStart() {
        assertThrows(EsException.class,
                () -> new Event("meeting", "2026-08-28 10:00", "2026-08-28 09:00"));
    }

    @Test
    void event_rejectsEqualStartAndEnd() {
        assertThrows(EsException.class,
                () -> new Event("meeting", "2026-08-28", "2026-08-28"));
    }
}
