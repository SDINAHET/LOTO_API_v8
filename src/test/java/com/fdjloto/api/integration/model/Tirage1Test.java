package com.fdjloto.api.model;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Tirage1Test {

    @Test
    void shouldTestGettersAndSetters() {
        Tirage t = new Tirage();

        t.setId("1");
        t.setJourDeTirage("Monday");
        t.setDateDeTirage("2025-03-15");
        t.setBoule1(1);
        t.setBoule2(2);
        t.setBoule3(3);
        t.setBoule4(4);
        t.setBoule5(5);
        t.setNumeroChance(7);

        assertEquals("1", t.getId());
        assertEquals("Monday", t.getJourDeTirage());
        assertEquals("2025-03-15", t.getDateDeTirage());
        assertEquals(1, t.getBoule1());
        assertEquals(2, t.getBoule2());
        assertEquals(3, t.getBoule3());
        assertEquals(4, t.getBoule4());
        assertEquals(5, t.getBoule5());
        assertEquals(7, t.getNumeroChance());
    }

    @Test
    void shouldReturnCorrectBoulesList() {
        Tirage t = new Tirage();

        t.setBoule1(10);
        t.setBoule2(20);
        t.setBoule3(30);
        t.setBoule4(40);
        t.setBoule5(50);

        List<Integer> boules = t.getBoules();

        assertEquals(5, boules.size());
        assertEquals(List.of(10, 20, 30, 40, 50), boules);
    }

    @Test
    void shouldTestEqualsFullCoverage() {
        Tirage t1 = new Tirage();
        Tirage t2 = new Tirage();

        // même instance
        assertEquals(t1, t1);

        // null
        assertNotEquals(t1, null);

        // autre type
        assertNotEquals(t1, "string");

        // objets différents
        t1.setId("1");
        t2.setId("2");
        assertNotEquals(t1, t2);

        // objets égaux
        t2.setId("1");
        assertEquals(t1, t2);
    }

    @Test
    void shouldTestHashCodeFullCoverage() {
        Tirage t1 = new Tirage();
        Tirage t2 = new Tirage();

        t1.setId("1");
        t2.setId("1");

        assertEquals(t1.hashCode(), t2.hashCode());

        t2.setId("2");
        assertNotEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void shouldTestCanEqual() {
        Tirage t = new Tirage();
        assertTrue(t.canEqual(new Tirage()));
    }

    @Test
    void shouldTestToString() {
        Tirage t = new Tirage();
        t.setId("1");

        String str = t.toString();

        assertNotNull(str);
        assertTrue(str.contains("1"));
    }

    @Test
    void shouldHandleNullValues() {
        Tirage t = new Tirage();

        assertNull(t.getId());
        assertNull(t.getJourDeTirage());
        assertNull(t.getDateDeTirage());
    }
}
