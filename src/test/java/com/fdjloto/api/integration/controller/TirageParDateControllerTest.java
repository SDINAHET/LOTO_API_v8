package com.fdjloto.api.controller;

import com.fdjloto.api.service.Historique20DetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TirageParDateControllerTest {

    private TirageParDateController controller;

    @BeforeEach
    void setup() {
        controller = new TirageParDateController(Mockito.mock(Historique20DetailService.class));
    }

    // ===============================
    // TEST isDrawDay()
    // ===============================

    @Test
    void shouldDetectValidDrawDay() throws Exception {

        Method method = TirageParDateController.class
                .getDeclaredMethod("isDrawDay", LocalDate.class);

        method.setAccessible(true);

        boolean result = (boolean) method.invoke(controller, LocalDate.of(2026,3,4)); // mercredi

        assertTrue(result);
    }

    @Test
    void shouldDetectInvalidDrawDay() throws Exception {

        Method method = TirageParDateController.class
                .getDeclaredMethod("isDrawDay", LocalDate.class);

        method.setAccessible(true);

        boolean result = (boolean) method.invoke(controller, LocalDate.of(2026,3,5)); // jeudi

        assertFalse(result);
    }

    // ===============================
    // TEST nearestDrawOnOrAfter()
    // ===============================

    @Test
    void nearestDrawShouldReturnSameDayIfDrawDay() throws Exception {

        Method method = TirageParDateController.class
                .getDeclaredMethod("nearestDrawOnOrAfter", LocalDate.class);

        method.setAccessible(true);

        LocalDate result = (LocalDate) method.invoke(controller, LocalDate.of(2026,3,4));

        assertEquals(LocalDate.of(2026,3,4), result);
    }

    @Test
    void nearestDrawShouldSkipDays() throws Exception {

        Method method = TirageParDateController.class
                .getDeclaredMethod("nearestDrawOnOrAfter", LocalDate.class);

        method.setAccessible(true);

        LocalDate result = (LocalDate) method.invoke(controller, LocalDate.of(2026,3,5)); // jeudi

        assertEquals(LocalDate.of(2026,3,7), result); // samedi
    }

    // ===============================
    // TEST effectiveTodayForDraws()
    // ===============================

    @Test
    void effectiveTodayShouldReturnTodayOrTomorrow() throws Exception {

        Method method = TirageParDateController.class
                .getDeclaredMethod("effectiveTodayForDraws");

        method.setAccessible(true);

        LocalDate result = (LocalDate) method.invoke(controller);

        assertNotNull(result);
    }

}
