package com.fdjloto.api.unit.model;

import com.fdjloto.api.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    @Test
    void shouldCreateUserWithDefaultRoleUser() {
        User user = new User();
        user.setFirstName("CI");
        user.setLastName("Runner");
        user.setEmail("ci@loto.local");
        user.setPassword("StrongPass123!");

        assertFalse(user.isAdmin());
        assertEquals("ROLE_USER", user.getRole());
    }

    @Test
    void shouldReturnAdminRoleWhenUserIsAdmin() {
        User user = new User();
        user.setAdmin(true);

        assertTrue(user.isAdmin());
        assertEquals("ROLE_ADMIN", user.getRole());
    }

    @Test
    void shouldGenerateUuidAndTimestampsOnPrePersist() {
        User user = new User();

        user.prePersist();

        assertNotNull(user.getId());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void shouldUpdateTimestampOnPreUpdate() {
        User user = new User();
        user.prePersist();

        var oldUpdatedAt = user.getUpdatedAt();

        user.preUpdate();

        assertNotNull(user.getUpdatedAt());
        assertFalse(user.getUpdatedAt().isBefore(oldUpdatedAt));
    }
}
