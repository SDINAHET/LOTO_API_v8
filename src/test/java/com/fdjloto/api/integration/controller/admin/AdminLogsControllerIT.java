package com.fdjloto.api.controller.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AdminLogsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private Path logsDir;
    private Path logFile;

    @BeforeEach
    void setup() throws Exception {

        logsDir = Paths.get("logs").toAbsolutePath().normalize();
        Files.createDirectories(logsDir);

        logFile = logsDir.resolve("application.log");

        Files.writeString(
                logFile,
                "line1\nline2\nline3\nline4\nline5\n",
                StandardCharsets.UTF_8
        );
    }

    /**
     * Lecture normale des logs
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnLogs() throws Exception {

        mockMvc.perform(get("/api/admin/logs")
                        .param("lines", "3")
                        .param("file", "application.log"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("line3")))
                .andExpect(content().string(containsString("line5")));
    }

    /**
     * Clamp lines (min = 50)
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldClampLinesParameter() throws Exception {

        mockMvc.perform(get("/api/admin/logs")
                        .param("lines", "1")
                        .param("file", "application.log"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("line1")));
    }

    /**
     * Fichier inexistant
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenFileNotFound() throws Exception {

        mockMvc.perform(get("/api/admin/logs")
                        .param("file", "unknown.log"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Log file not found")));
    }

    /**
     * Protection path traversal
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRejectPathTraversal() throws Exception {

        mockMvc.perform(get("/api/admin/logs")
                        .param("file", "../secret.txt"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid file name"));
    }

    /**
     * Logs vides
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnEmptyMessageWhenLogsEmpty() throws Exception {

        Files.writeString(logFile, "", StandardCharsets.UTF_8);

        mockMvc.perform(get("/api/admin/logs")
                        .param("file", "application.log"))
                .andExpect(status().isOk())
                .andExpect(content().string("(Aucun log pour le moment)"));
    }

    /**
     * Sécurité : accès interdit si non ADMIN
     */
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenForNonAdmin() throws Exception {

        mockMvc.perform(get("/api/admin/logs"))
                .andExpect(status().isForbidden());
    }
}
