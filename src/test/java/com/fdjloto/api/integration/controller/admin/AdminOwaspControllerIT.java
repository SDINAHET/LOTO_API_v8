// // // // // // package com.fdjloto.api.controller.admin;

// // // // // // import org.junit.jupiter.api.BeforeEach;
// // // // // // import org.junit.jupiter.api.Test;
// // // // // // import org.springframework.beans.factory.annotation.Autowired;
// // // // // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // // // // import org.springframework.boot.test.context.SpringBootTest;
// // // // // // import org.springframework.security.test.context.support.WithMockUser;
// // // // // // import org.springframework.test.web.servlet.MockMvc;

// // // // // // import com.fdjloto.api.controller.admin.AdminOwaspController;

// // // // // // import java.nio.charset.StandardCharsets;
// // // // // // import java.nio.file.Files;
// // // // // // import java.nio.file.Path;
// // // // // // import java.nio.file.Paths;
// // // // // // import java.time.Instant;
// // // // // // import java.util.List;
// // // // // // import java.util.Map;

// // // // // // import static org.hamcrest.Matchers.containsString;
// // // // // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // // // // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// // // // // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // // // // // import org.springframework.test.context.ActiveProfiles;
// // // // // // import static org.junit.jupiter.api.Assertions.*;

// // // // // // @SpringBootTest
// // // // // // @AutoConfigureMockMvc
// // // // // // @ActiveProfiles("test")
// // // // // // class AdminOwaspControllerIT {

// // // // // //     @Autowired
// // // // // //     private MockMvc mockMvc;

// // // // // // 	// ✅ AJOUT ICI
// // // // // // 	@Autowired
// // // // // // 	private AdminOwaspController controller;

// // // // // //     private Path reportsDir;
// // // // // //     private Path latest;



// // // // // //     @BeforeEach
// // // // // //     void setup() throws Exception {

// // // // // //         reportsDir = Paths.get("reports/owasp").toAbsolutePath().normalize();
// // // // // //         Files.createDirectories(reportsDir);

// // // // // //         latest = reportsDir.resolve("latest.json");

// // // // // //         // nettoyage avant chaque test
// // // // // //         Files.deleteIfExists(latest);
// // // // // //     }

// // // // // //     /**
// // // // // //      * GET /api/admin/owasp-score
// // // // // //      * -> aucun rapport
// // // // // //      */
// // // // // //     @Test
// // // // // //     @WithMockUser(roles = "ADMIN")
// // // // // //     void shouldReturn404WhenNoReportExists() throws Exception {

// // // // // //         mockMvc.perform(get("/api/admin/owasp-score"))
// // // // // //                 .andExpect(status().isNotFound())
// // // // // //                 .andExpect(jsonPath("$.error",
// // // // // //                         containsString("No OWASP report")));
// // // // // //     }

// // // // // //     /**
// // // // // //      * GET dernier rapport
// // // // // //      */
// // // // // //     @Test
// // // // // //     @WithMockUser(roles = "ADMIN")
// // // // // //     void shouldReturnLastReport() throws Exception {

// // // // // //         String json = """
// // // // // //                 {
// // // // // //                   "timestamp":"2026-03-16T22:00:00Z",
// // // // // //                   "total":82,
// // // // // //                   "grade":"B",
// // // // // //                   "scores":{"A02":8,"A07":7,"A10":9},
// // // // // //                   "raw":"debug-output"
// // // // // //                 }
// // // // // //                 """;

// // // // // //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// // // // // //         mockMvc.perform(get("/api/admin/owasp-score"))
// // // // // //                 .andExpect(status().isOk())
// // // // // //                 .andExpect(jsonPath("$.total").value(82))
// // // // // //                 .andExpect(jsonPath("$.grade").value("B"))
// // // // // //                 .andExpect(jsonPath("$.scores.A02").value(8))
// // // // // //                 .andExpect(jsonPath("$.raw").doesNotExist());
// // // // // //     }

// // // // // //     /**
// // // // // //      * GET avec detail=true
// // // // // //      */
// // // // // //     @Test
// // // // // //     @WithMockUser(roles = "ADMIN")
// // // // // //     void shouldReturnRawWhenDetailTrue() throws Exception {

// // // // // //         String json = """
// // // // // //                 {
// // // // // //                   "timestamp":"2026-03-16T22:00:00Z",
// // // // // //                   "total":90,
// // // // // //                   "grade":"A",
// // // // // //                   "scores":{"A02":9,"A07":9,"A10":9},
// // // // // //                   "raw":"OWASP RAW OUTPUT"
// // // // // //                 }
// // // // // //                 """;

// // // // // //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// // // // // //         mockMvc.perform(get("/api/admin/owasp-score")
// // // // // //                         .param("detail","true"))
// // // // // //                 .andExpect(status().isOk())
// // // // // //                 .andExpect(jsonPath("$.raw").value("OWASP RAW OUTPUT"));
// // // // // //     }

// // // // // //     /**
// // // // // //      * POST /run -> cooldown renvoie dernier rapport
// // // // // //      */
// // // // // //     // @Test
// // // // // //     // @WithMockUser(roles = "ADMIN")
// // // // // //     // void runShouldReturnExistingReportDuringCooldown() throws Exception {

// // // // // //     //     String json = """
// // // // // //     //             {
// // // // // //     //               "timestamp":"2026-03-16T22:00:00Z",
// // // // // //     //               "total":75,
// // // // // //     //               "grade":"B",
// // // // // //     //               "scores":{"A02":7,"A07":7,"A10":8}
// // // // // //     //             }
// // // // // //     //             """;

// // // // // //     //     Files.writeString(latest, json, StandardCharsets.UTF_8);

// // // // // //     //     mockMvc.perform(post("/api/admin/owasp-score/run"))
// // // // // //     //             .andExpect(status().isAccepted()) // ⚠️ 202
// // // // // //     //             .andExpect(jsonPath("$.grade").value("B"))
// // // // // //     //             .andExpect(jsonPath("$.scores.A02").value(7));
// // // // // //     // }
// // // // // // 	@Test
// // // // // // 	@WithMockUser(roles = "ADMIN")
// // // // // // 	void runShouldReturnExistingReportDuringCooldown() throws Exception {

// // // // // // 		String json = """
// // // // // // 				{
// // // // // // 				"timestamp":"2026-03-16T22:00:00Z",
// // // // // // 				"total":75,
// // // // // // 				"grade":"B",
// // // // // // 				"scores":{"A02":7,"A07":7,"A10":8}
// // // // // // 				}
// // // // // // 				""";

// // // // // // 		Files.writeString(latest, json, StandardCharsets.UTF_8);

// // // // // // 		// ✅ FORCE LE COOLDOWN
// // // // // // 		controller.setLastRunEpochSec(Instant.now().getEpochSecond());

// // // // // // 		mockMvc.perform(post("/api/admin/owasp-score/run"))
// // // // // // 				.andExpect(status().isAccepted())
// // // // // // 				.andExpect(jsonPath("$.grade").value("B"))
// // // // // // 				.andExpect(jsonPath("$.scores.A02").value(7));
// // // // // // 	}

// // // // // //     /**
// // // // // //      * sécurité : USER non admin
// // // // // //      */
// // // // // //     @Test
// // // // // //     @WithMockUser(roles = "USER")
// // // // // //     void shouldReturnForbiddenForNonAdmin() throws Exception {

// // // // // //         mockMvc.perform(get("/api/admin/owasp-score"))
// // // // // //                 .andExpect(status().isForbidden());
// // // // // //     }


// // // // // // 	@Test
// // // // // // 	void shouldParseOutputCorrectly() throws Exception {

// // // // // // 		String fake = """
// // // // // // 			TOTAL........: 75 / 100
// // // // // // 			A02 Broken Auth: 7 / 10
// // // // // // 			A07 XSS: 8 / 10
// // // // // // 			A10 Logging: 9 / 10
// // // // // // 			__EXIT_CODE__=0
// // // // // // 		""";

// // // // // // 		Map<String, Object> result = controller.parseOutput(fake, false);

// // // // // // 		assertEquals(75, result.get("total"));
// // // // // // 		assertEquals("B", result.get("grade"));

// // // // // // 		Map<String, Integer> scores = (Map<String, Integer>) result.get("scores");

// // // // // // 		assertEquals(7, scores.get("A02"));
// // // // // // 		assertEquals(8, scores.get("A07"));
// // // // // // 	}

// // // // // // 	@Test
// // // // // // 	void shouldGenerateTipsWhenScoresLow() {

// // // // // // 		Map<String, Integer> scores = Map.of(
// // // // // // 				"A02", 5,
// // // // // // 				"A07", 5,
// // // // // // 				"A10", 5
// // // // // // 		);

// // // // // // 		List<String> tips = controller.buildFrontTips(scores);

// // // // // // 		assertFalse(tips.isEmpty());
// // // // // // 	}

// // // // // // 	@Test
// // // // // // 	void shouldIncludeRawWhenDetailTrue() {

// // // // // // 		String fake = "TOTAL........: 90 / 100";

// // // // // // 		Map<String, Object> result = controller.parseOutput(fake, true);

// // // // // // 		assertTrue(result.containsKey("raw"));
// // // // // // 	}

// // // // // // 	@Test
// // // // // // 	void shouldReturnGradeA() {
// // // // // // 		String fake = "TOTAL........: 90 / 100";
// // // // // // 		Map<String, Object> result = controller.parseOutput(fake, false);
// // // // // // 		assertEquals("A", result.get("grade"));
// // // // // // 	}

// // // // // // 	@Test
// // // // // // 	void shouldReturnGradeC() {
// // // // // // 		String fake = "TOTAL........: 60 / 100";
// // // // // // 		Map<String, Object> result = controller.parseOutput(fake, false);
// // // // // // 		assertEquals("C", result.get("grade"));
// // // // // // 	}

// // // // // // }

// // // // // package com.fdjloto.api.controller.admin;

// // // // // import org.junit.jupiter.api.BeforeEach;
// // // // // import org.junit.jupiter.api.Test;
// // // // // import org.springframework.beans.factory.annotation.Autowired;
// // // // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // // // import org.springframework.boot.test.context.TestConfiguration;
// // // // // import org.springframework.context.annotation.Bean;
// // // // // import org.springframework.boot.test.context.SpringBootTest;
// // // // // import org.springframework.security.test.context.support.WithMockUser;
// // // // // import org.springframework.test.context.ActiveProfiles;
// // // // // import org.springframework.test.web.servlet.MockMvc;

// // // // // import java.nio.charset.StandardCharsets;
// // // // // import java.nio.file.*;
// // // // // import java.time.Instant;
// // // // // import java.util.List;
// // // // // import java.util.Map;

// // // // // import static org.hamcrest.Matchers.containsString;
// // // // // import static org.junit.jupiter.api.Assertions.*;
// // // // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// // // // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// // // // // import org.springframework.http.ResponseEntity;

// // // // // @SpringBootTest
// // // // // @AutoConfigureMockMvc
// // // // // @ActiveProfiles("test")
// // // // // class AdminOwaspControllerIT {

// // // // //     @Autowired
// // // // //     private MockMvc mockMvc;

// // // // //     @Autowired
// // // // //     private AdminOwaspController controller;

// // // // //     private Path reportsDir;
// // // // //     private Path latest;

// // // // //     // 🔥 MOCK DU SCRIPT → COVERAGE MAX
// // // // //     @TestConfiguration
// // // // //     static class TestConfig {
// // // // //         @Bean
// // // // //         public AdminOwaspController controller() {
// // // // //             return new AdminOwaspController() {
// // // // //                 @Override
// // // // //                 protected String runScript(boolean detail) {
// // // // //                     return """
// // // // //                         TOTAL........: 88 / 100
// // // // //                         A02 Broken Auth: 9 / 10
// // // // //                         A07 XSS: 8 / 10
// // // // //                         A10 Logging: 9 / 10
// // // // //                         __EXIT_CODE__=0
// // // // //                     """;
// // // // //                 }
// // // // //             };
// // // // //         }
// // // // //     }

// // // // //     @BeforeEach
// // // // //     void setup() throws Exception {
// // // // //         reportsDir = Paths.get("reports/owasp").toAbsolutePath().normalize();
// // // // //         Files.createDirectories(reportsDir);

// // // // //         latest = reportsDir.resolve("latest.json");

// // // // //         Files.deleteIfExists(latest);
// // // // //     }

// // // // //     // =========================
// // // // //     // GET - NO REPORT
// // // // //     // =========================
// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldReturn404WhenNoReportExists() throws Exception {

// // // // //         mockMvc.perform(get("/api/admin/owasp-score"))
// // // // //                 .andExpect(status().isNotFound())
// // // // //                 .andExpect(jsonPath("$.error",
// // // // //                         containsString("No OWASP report")));
// // // // //     }

// // // // //     // =========================
// // // // //     // GET LAST REPORT
// // // // //     // =========================
// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldReturnLastReport() throws Exception {

// // // // //         String json = """
// // // // //                 {
// // // // //                   "timestamp":"2026-03-16T22:00:00Z",
// // // // //                   "total":82,
// // // // //                   "grade":"B",
// // // // //                   "scores":{"A02":8},
// // // // //                   "raw":"debug"
// // // // //                 }
// // // // //                 """;

// // // // //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// // // // //         mockMvc.perform(get("/api/admin/owasp-score"))
// // // // //                 .andExpect(status().isOk())
// // // // //                 .andExpect(jsonPath("$.total").value(82))
// // // // //                 .andExpect(jsonPath("$.grade").value("B"))
// // // // //                 .andExpect(jsonPath("$.raw").doesNotExist());
// // // // //     }

// // // // //     // =========================
// // // // //     // GET DETAIL TRUE
// // // // //     // =========================
// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldReturnRawWhenDetailTrue() throws Exception {

// // // // //         String json = """
// // // // //                 {
// // // // //                   "timestamp":"2026-03-16T22:00:00Z",
// // // // //                   "total":90,
// // // // //                   "grade":"A",
// // // // //                   "scores":{"A02":9},
// // // // //                   "raw":"RAW DATA"
// // // // //                 }
// // // // //                 """;

// // // // //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// // // // //         mockMvc.perform(get("/api/admin/owasp-score")
// // // // //                         .param("detail", "true"))
// // // // //                 .andExpect(status().isOk())
// // // // //                 .andExpect(jsonPath("$.raw").value("RAW DATA"));
// // // // //     }

// // // // //     // =========================
// // // // //     // RUN NORMAL (🔥 IMPORTANT)
// // // // //     // =========================
// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldRunAuditAndPersistFile() throws Exception {

// // // // //         mockMvc.perform(post("/api/admin/owasp-score/run"))
// // // // //                 .andExpect(status().isAccepted())
// // // // //                 .andExpect(jsonPath("$.total").value(88))
// // // // //                 .andExpect(jsonPath("$.grade").value("A"))
// // // // //                 .andExpect(jsonPath("$.scores.A02").value(9));

// // // // //         assertTrue(Files.exists(latest));

// // // // //         String content = Files.readString(latest);
// // // // //         assertTrue(content.contains("\"total\" : 88"));
// // // // //     }

// // // // //     // =========================
// // // // //     // COOLDOWN
// // // // //     // =========================
// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldReturnExistingReportDuringCooldown() throws Exception {

// // // // //         String json = """
// // // // //                 {
// // // // //                   "timestamp":"2026-03-16T22:00:00Z",
// // // // //                   "total":75,
// // // // //                   "grade":"B",
// // // // //                   "scores":{"A02":7}
// // // // //                 }
// // // // //                 """;

// // // // //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// // // // //         controller.setLastRunEpochSec(Instant.now().getEpochSecond());

// // // // //         mockMvc.perform(post("/api/admin/owasp-score/run"))
// // // // //                 .andExpect(status().isAccepted())
// // // // //                 .andExpect(jsonPath("$.grade").value("B"));
// // // // //     }

// // // // //     // =========================
// // // // //     // SECURITY
// // // // //     // =========================
// // // // //     @Test
// // // // //     @WithMockUser(roles = "USER")
// // // // //     void shouldReturnForbiddenForNonAdmin() throws Exception {

// // // // //         mockMvc.perform(get("/api/admin/owasp-score"))
// // // // //                 .andExpect(status().isForbidden());
// // // // //     }

// // // // //     // =========================
// // // // //     // PARSE OUTPUT
// // // // //     // =========================
// // // // //     @Test
// // // // //     void shouldParseOutputCorrectly() {

// // // // //         String fake = """
// // // // //             TOTAL........: 75 / 100
// // // // //             A02 Broken Auth: 7 / 10
// // // // //             A07 XSS: 8 / 10
// // // // //             __EXIT_CODE__=0
// // // // //         """;

// // // // //         Map<String, Object> result = controller.parseOutput(fake, false);

// // // // //         assertEquals(75, result.get("total"));
// // // // //         assertEquals("B", result.get("grade"));
// // // // //     }

// // // // //     // =========================
// // // // //     // TIPS
// // // // //     // =========================
// // // // //     @Test
// // // // //     void shouldGenerateTipsWhenScoresLow() {

// // // // //         Map<String, Integer> scores = Map.of(
// // // // //                 "A02", 5,
// // // // //                 "A07", 5,
// // // // //                 "A10", 5
// // // // //         );

// // // // //         List<String> tips = controller.buildFrontTips(scores);

// // // // //         assertFalse(tips.isEmpty());
// // // // //     }

// // // // //     // =========================
// // // // //     // ERROR PATH (🔥 BONUS)
// // // // //     // =========================
// // // // //     @Test
// // // // //     void shouldReturn500WhenScriptFails() {

// // // // //         AdminOwaspController bad = new AdminOwaspController() {
// // // // //             @Override
// // // // //             protected String runScript(boolean detail) {
// // // // //                 throw new RuntimeException("boom");
// // // // //             }
// // // // //         };

// // // // //         ResponseEntity<?> response = bad.run(false);

// // // // //         assertEquals(500, response.getStatusCodeValue());
// // // // //     }
// // // // // }

// // // // package com.fdjloto.api.controller.admin;

// // // // import org.junit.jupiter.api.BeforeEach;
// // // // import org.junit.jupiter.api.Test;
// // // // import org.springframework.beans.factory.annotation.Autowired;
// // // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // // import org.springframework.boot.test.context.TestConfiguration;
// // // // import org.springframework.context.annotation.Primary;
// // // // import org.springframework.context.annotation.Bean;
// // // // import org.springframework.boot.test.context.SpringBootTest;
// // // // import org.springframework.security.test.context.support.WithMockUser;
// // // // import org.springframework.test.context.ActiveProfiles;
// // // // import org.springframework.test.web.servlet.MockMvc;
// // // // import org.springframework.http.ResponseEntity;

// // // // import java.nio.charset.StandardCharsets;
// // // // import java.nio.file.*;
// // // // import java.time.Instant;
// // // // import java.util.List;
// // // // import java.util.Map;

// // // // import static org.hamcrest.Matchers.containsString;
// // // // import static org.junit.jupiter.api.Assertions.*;
// // // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// // // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // // // @SpringBootTest
// // // // @AutoConfigureMockMvc
// // // // @ActiveProfiles("test")
// // // // class AdminOwaspControllerIT {

// // // //     @Autowired
// // // //     private MockMvc mockMvc;

// // // //     @Autowired
// // // //     private AdminOwaspController controller;

// // // //     private Path reportsDir;
// // // //     private Path latest;

// // // //     // ✅ FIX IMPORTANT → @Primary
// // // //     @TestConfiguration
// // // //     static class TestConfig {

// // // //         @Bean
// // // //         @Primary
// // // //         public AdminOwaspController mockController() {
// // // //             return new AdminOwaspController() {
// // // //                 @Override
// // // //                 protected String runScript(boolean detail) {
// // // //                     return """
// // // //                         TOTAL........: 88 / 100
// // // //                         A02 Broken Auth: 9 / 10
// // // //                         A07 XSS: 8 / 10
// // // //                         A10 Logging: 9 / 10
// // // //                         __EXIT_CODE__=0
// // // //                     """;
// // // //                 }
// // // //             };
// // // //         }
// // // //     }

// // // //     @BeforeEach
// // // //     void setup() throws Exception {
// // // //         reportsDir = Paths.get("reports/owasp").toAbsolutePath().normalize();
// // // //         Files.createDirectories(reportsDir);

// // // //         latest = reportsDir.resolve("latest.json");
// // // //         Files.deleteIfExists(latest);
// // // //     }

// // // //     // =========================
// // // //     // GET - NO REPORT
// // // //     // =========================
// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldReturn404WhenNoReportExists() throws Exception {

// // // //         mockMvc.perform(get("/api/admin/owasp-score"))
// // // //                 .andExpect(status().isNotFound())
// // // //                 .andExpect(jsonPath("$.error",
// // // //                         containsString("No OWASP report")));
// // // //     }

// // // //     // =========================
// // // //     // GET LAST REPORT
// // // //     // =========================
// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldReturnLastReport() throws Exception {

// // // //         String json = """
// // // //                 {
// // // //                   "timestamp":"2026-03-16T22:00:00Z",
// // // //                   "total":82,
// // // //                   "grade":"B",
// // // //                   "scores":{"A02":8},
// // // //                   "raw":"debug"
// // // //                 }
// // // //                 """;

// // // //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// // // //         mockMvc.perform(get("/api/admin/owasp-score"))
// // // //                 .andExpect(status().isOk())
// // // //                 .andExpect(jsonPath("$.total").value(82))
// // // //                 .andExpect(jsonPath("$.grade").value("B"))
// // // //                 .andExpect(jsonPath("$.raw").doesNotExist());
// // // //     }

// // // //     // =========================
// // // //     // GET DETAIL TRUE
// // // //     // =========================
// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldReturnRawWhenDetailTrue() throws Exception {

// // // //         String json = """
// // // //                 {
// // // //                   "timestamp":"2026-03-16T22:00:00Z",
// // // //                   "total":90,
// // // //                   "grade":"A",
// // // //                   "scores":{"A02":9},
// // // //                   "raw":"RAW DATA"
// // // //                 }
// // // //                 """;

// // // //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// // // //         mockMvc.perform(get("/api/admin/owasp-score")
// // // //                         .param("detail", "true"))
// // // //                 .andExpect(status().isOk())
// // // //                 .andExpect(jsonPath("$.raw").value("RAW DATA"));
// // // //     }

// // // //     // =========================
// // // //     // RUN NORMAL
// // // //     // =========================
// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldRunAuditAndPersistFile() throws Exception {

// // // //         mockMvc.perform(post("/api/admin/owasp-score/run"))
// // // //                 .andExpect(status().isAccepted())
// // // //                 .andExpect(jsonPath("$.total").value(88))
// // // //                 .andExpect(jsonPath("$.grade").value("A"))
// // // //                 .andExpect(jsonPath("$.scores.A02").value(9));

// // // //         assertTrue(Files.exists(latest));
// // // //     }

// // // //     // =========================
// // // //     // COOLDOWN
// // // //     // =========================
// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldReturnExistingReportDuringCooldown() throws Exception {

// // // //         String json = """
// // // //                 {
// // // //                   "timestamp":"2026-03-16T22:00:00Z",
// // // //                   "total":75,
// // // //                   "grade":"B",
// // // //                   "scores":{"A02":7}
// // // //                 }
// // // //                 """;

// // // //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// // // //         controller.setLastRunEpochSec(Instant.now().getEpochSecond());

// // // //         mockMvc.perform(post("/api/admin/owasp-score/run"))
// // // //                 .andExpect(status().isAccepted())
// // // //                 .andExpect(jsonPath("$.grade").value("B"));
// // // //     }

// // // //     // =========================
// // // //     // SECURITY
// // // //     // =========================
// // // //     @Test
// // // //     @WithMockUser(roles = "USER")
// // // //     void shouldReturnForbiddenForNonAdmin() throws Exception {

// // // //         mockMvc.perform(get("/api/admin/owasp-score"))
// // // //                 .andExpect(status().isForbidden());
// // // //     }

// // // //     // =========================
// // // //     // PARSE
// // // //     // =========================
// // // //     @Test
// // // //     void shouldParseOutputCorrectly() {

// // // //         String fake = """
// // // //             TOTAL........: 75 / 100
// // // //             A02 Broken Auth: 7 / 10
// // // //             A07 XSS: 8 / 10
// // // //             __EXIT_CODE__=0
// // // //         """;

// // // //         Map<String, Object> result = controller.parseOutput(fake, false);

// // // //         assertEquals(75, result.get("total"));
// // // //         assertEquals("B", result.get("grade"));
// // // //     }

// // // //     // =========================
// // // //     // ERROR
// // // //     // =========================
// // // //     @Test
// // // //     void shouldReturn500WhenScriptFails() {

// // // //         AdminOwaspController bad = new AdminOwaspController() {
// // // //             @Override
// // // //             protected String runScript(boolean detail) {
// // // //                 throw new RuntimeException("boom");
// // // //             }
// // // //         };

// // // //         ResponseEntity<?> response = bad.run(false);

// // // //         assertEquals(500, response.getStatusCodeValue());
// // // //     }
// // // // }

// // // package com.fdjloto.api.controller.admin;

// // // import org.junit.jupiter.api.BeforeEach;
// // // import org.junit.jupiter.api.Test;
// // // import org.springframework.beans.factory.annotation.Autowired;
// // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // import org.springframework.boot.test.context.TestConfiguration;
// // // import org.springframework.context.annotation.Primary;
// // // import org.springframework.context.annotation.Bean;
// // // import org.springframework.boot.test.context.SpringBootTest;
// // // import org.springframework.security.test.context.support.WithMockUser;
// // // import org.springframework.test.context.ActiveProfiles;
// // // import org.springframework.test.web.servlet.MockMvc;
// // // import org.springframework.http.ResponseEntity;

// // // import java.nio.charset.StandardCharsets;
// // // import java.nio.file.*;
// // // import java.time.Instant;
// // // import java.util.List;
// // // import java.util.Map;

// // // import static org.hamcrest.Matchers.containsString;
// // // import static org.junit.jupiter.api.Assertions.*;
// // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // // @SpringBootTest
// // // @AutoConfigureMockMvc
// // // @ActiveProfiles("test")
// // // class AdminOwaspControllerIT {

// // //     @Autowired
// // //     private MockMvc mockMvc;

// // //     @Autowired
// // //     private AdminOwaspController controller;

// // //     private Path reportsDir;
// // //     private Path latest;

// // //     // ✅ FIX IMPORTANT → @Primary
// // //     // @TestConfiguration
// // //     // static class TestConfig {

// // //     //     @Bean
// // //     //     @Primary
// // //     //     public AdminOwaspController mockController() {
// // //     //         return new AdminOwaspController() {
// // //     //             @Override
// // //     //             protected String runScript(boolean detail) {
// // //     //                 return """
// // //     //                     TOTAL........: 88 / 100
// // //     //                     A02 Broken Auth: 9 / 10
// // //     //                     A07 XSS: 8 / 10
// // //     //                     A10 Logging: 9 / 10
// // //     //                     __EXIT_CODE__=0
// // //     //                 """;
// // //     //             }
// // //     //         };
// // //     //     }
// // //     // }

// // //     @BeforeEach
// // //     void setup() throws Exception {
// // //         reportsDir = Paths.get("reports/owasp").toAbsolutePath().normalize();
// // //         Files.createDirectories(reportsDir);

// // //         latest = reportsDir.resolve("latest.json");
// // //         Files.deleteIfExists(latest);
// // //     }

// // //     // =========================
// // //     // GET - NO REPORT
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldReturn404WhenNoReportExists() throws Exception {

// // //         mockMvc.perform(get("/api/admin/owasp-score"))
// // //                 .andExpect(status().isNotFound())
// // //                 .andExpect(jsonPath("$.error",
// // //                         containsString("No OWASP report")));
// // //     }

// // //     // =========================
// // //     // GET LAST REPORT
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldReturnLastReport() throws Exception {

// // //         String json = """
// // //                 {
// // //                   "timestamp":"2026-03-16T22:00:00Z",
// // //                   "total":82,
// // //                   "grade":"B",
// // //                   "scores":{"A02":8},
// // //                   "raw":"debug"
// // //                 }
// // //                 """;

// // //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// // //         mockMvc.perform(get("/api/admin/owasp-score"))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(jsonPath("$.total").value(82))
// // //                 .andExpect(jsonPath("$.grade").value("B"))
// // //                 .andExpect(jsonPath("$.raw").doesNotExist());
// // //     }

// // //     // =========================
// // //     // GET DETAIL TRUE
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldReturnRawWhenDetailTrue() throws Exception {

// // //         String json = """
// // //                 {
// // //                   "timestamp":"2026-03-16T22:00:00Z",
// // //                   "total":90,
// // //                   "grade":"A",
// // //                   "scores":{"A02":9},
// // //                   "raw":"RAW DATA"
// // //                 }
// // //                 """;

// // //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// // //         mockMvc.perform(get("/api/admin/owasp-score")
// // //                         .param("detail", "true"))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(jsonPath("$.raw").value("RAW DATA"));
// // //     }

// // //     // =========================
// // //     // RUN NORMAL
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldRunAuditAndPersistFile() throws Exception {

// // //         mockMvc.perform(post("/api/admin/owasp-score/run"))
// // //                 .andExpect(status().isAccepted())
// // //                 .andExpect(jsonPath("$.total").value(88))
// // //                 .andExpect(jsonPath("$.grade").value("A"))
// // //                 .andExpect(jsonPath("$.scores.A02").value(9));

// // //         assertTrue(Files.exists(latest));
// // //     }

// // //     // =========================
// // //     // COOLDOWN
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldReturnExistingReportDuringCooldown() throws Exception {

// // //         String json = """
// // //                 {
// // //                   "timestamp":"2026-03-16T22:00:00Z",
// // //                   "total":75,
// // //                   "grade":"B",
// // //                   "scores":{"A02":7}
// // //                 }
// // //                 """;

// // //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// // //         controller.setLastRunEpochSec(Instant.now().getEpochSecond());

// // //         mockMvc.perform(post("/api/admin/owasp-score/run"))
// // //                 .andExpect(status().isAccepted())
// // //                 .andExpect(jsonPath("$.grade").value("B"));
// // //     }

// // //     // =========================
// // //     // SECURITY
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "USER")
// // //     void shouldReturnForbiddenForNonAdmin() throws Exception {

// // //         mockMvc.perform(get("/api/admin/owasp-score"))
// // //                 .andExpect(status().isForbidden());
// // //     }

// // //     // =========================
// // //     // PARSE
// // //     // =========================
// // //     @Test
// // //     void shouldParseOutputCorrectly() {

// // //         String fake = """
// // //             TOTAL........: 75 / 100
// // //             A02 Broken Auth: 7 / 10
// // //             A07 XSS: 8 / 10
// // //             __EXIT_CODE__=0
// // //         """;

// // //         Map<String, Object> result = controller.parseOutput(fake, false);

// // //         assertEquals(75, result.get("total"));
// // //         assertEquals("B", result.get("grade"));
// // //     }

// // //     // =========================
// // //     // ERROR
// // //     // =========================
// // //     @Test
// // //     void shouldReturn500WhenScriptFails() {

// // //         AdminOwaspController bad = new AdminOwaspController() {
// // //             @Override
// // //             protected String runScript(boolean detail) {
// // //                 throw new RuntimeException("boom");
// // //             }
// // //         };

// // //         ResponseEntity<?> response = bad.run(false);

// // //         assertEquals(500, response.getStatusCodeValue());
// // //     }
// // // }

// // package com.fdjloto.api.controller.admin;

// // import org.junit.jupiter.api.BeforeEach;
// // import org.junit.jupiter.api.Test;
// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // import org.springframework.boot.test.context.SpringBootTest;
// // import org.springframework.boot.test.mock.mockito.SpyBean;
// // import org.springframework.security.test.context.support.WithMockUser;
// // import org.springframework.test.context.ActiveProfiles;
// // import org.springframework.test.web.servlet.MockMvc;
// // import org.springframework.http.ResponseEntity;

// // import java.nio.charset.StandardCharsets;
// // import java.nio.file.*;
// // import java.time.Instant;
// // import java.util.Map;

// // import static org.hamcrest.Matchers.containsString;
// // import static org.junit.jupiter.api.Assertions.*;
// // import static org.mockito.Mockito.*;
// // import static org.mockito.ArgumentMatchers.*;
// // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // @SpringBootTest
// // @AutoConfigureMockMvc
// // @ActiveProfiles("test")
// // class AdminOwaspControllerIT {

// //     @Autowired
// //     private MockMvc mockMvc;

// //     // ✅ FIX PRO → on garde le vrai controller
// //     @SpyBean
// //     private AdminOwaspController controller;

// //     private Path reportsDir;
// //     private Path latest;

// //     @BeforeEach
// //     void setup() throws Exception {
// //         reportsDir = Paths.get("reports/owasp").toAbsolutePath().normalize();
// //         Files.createDirectories(reportsDir);

// //         latest = reportsDir.resolve("latest.json");
// //         Files.deleteIfExists(latest);
// //     }

// //     // =========================
// //     // GET - NO REPORT
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldReturn404WhenNoReportExists() throws Exception {

// //         mockMvc.perform(get("/api/admin/owasp-score"))
// //                 .andExpect(status().isNotFound())
// //                 .andExpect(jsonPath("$.error",
// //                         containsString("No OWASP report")));
// //     }

// //     // =========================
// //     // GET LAST REPORT
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldReturnLastReport() throws Exception {

// //         String json = """
// //                 {
// //                   "timestamp":"2026-03-16T22:00:00Z",
// //                   "total":82,
// //                   "grade":"B",
// //                   "scores":{"A02":8},
// //                   "raw":"debug"
// //                 }
// //                 """;

// //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// //         mockMvc.perform(get("/api/admin/owasp-score"))
// //                 .andExpect(status().isOk())
// //                 .andExpect(jsonPath("$.total").value(82))
// //                 .andExpect(jsonPath("$.grade").value("B"))
// //                 .andExpect(jsonPath("$.raw").doesNotExist());
// //     }

// //     // =========================
// //     // GET DETAIL TRUE
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldReturnRawWhenDetailTrue() throws Exception {

// //         String json = """
// //                 {
// //                   "timestamp":"2026-03-16T22:00:00Z",
// //                   "total":90,
// //                   "grade":"A",
// //                   "scores":{"A02":9},
// //                   "raw":"RAW DATA"
// //                 }
// //                 """;

// //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// //         mockMvc.perform(get("/api/admin/owasp-score")
// //                         .param("detail", "true"))
// //                 .andExpect(status().isOk())
// //                 .andExpect(jsonPath("$.raw").value("RAW DATA"));
// //     }

// //     // =========================
// //     // RUN NORMAL (mock script)
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldRunAuditAndPersistFile() throws Exception {

// //         doReturn("""
// //             TOTAL........: 88 / 100
// //             A02 Broken Auth: 9 / 10
// //             A07 XSS: 8 / 10
// //             A10 Logging: 9 / 10
// //             __EXIT_CODE__=0
// //         """).when(controller).runScript(anyBoolean());

// //         mockMvc.perform(post("/api/admin/owasp-score/run"))
// //                 .andExpect(status().isAccepted())
// //                 .andExpect(jsonPath("$.total").value(88))
// //                 .andExpect(jsonPath("$.grade").value("A"))
// //                 .andExpect(jsonPath("$.scores.A02").value(9));

// //         assertTrue(Files.exists(latest));
// //     }

// //     // =========================
// //     // COOLDOWN
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldReturnExistingReportDuringCooldown() throws Exception {

// //         String json = """
// //                 {
// //                   "timestamp":"2026-03-16T22:00:00Z",
// //                   "total":75,
// //                   "grade":"B",
// //                   "scores":{"A02":7}
// //                 }
// //                 """;

// //         Files.writeString(latest, json, StandardCharsets.UTF_8);

// //         controller.setLastRunEpochSec(Instant.now().getEpochSecond());

// //         mockMvc.perform(post("/api/admin/owasp-score/run"))
// //                 .andExpect(status().isAccepted())
// //                 .andExpect(jsonPath("$.grade").value("B"));
// //     }

// //     // =========================
// //     // SECURITY
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "USER")
// //     void shouldReturnForbiddenForNonAdmin() throws Exception {

// //         mockMvc.perform(get("/api/admin/owasp-score"))
// //                 .andExpect(status().isForbidden());
// //     }

// //     // =========================
// //     // PARSE
// //     // =========================
// //     @Test
// //     void shouldParseOutputCorrectly() {

// //         String fake = """
// //             TOTAL........: 75 / 100
// //             A02 Broken Auth: 7 / 10
// //             A07 XSS: 8 / 10
// //             __EXIT_CODE__=0
// //         """;

// //         Map<String, Object> result = controller.parseOutput(fake, false);

// //         assertEquals(75, result.get("total"));
// //         assertEquals("B", result.get("grade"));
// //     }

// //     // =========================
// //     // ERROR
// //     // =========================
// //     @Test
// //     void shouldReturn500WhenScriptFails() {

// //         AdminOwaspController bad = new AdminOwaspController() {
// //             @Override
// //             protected String runScript(boolean detail) {
// //                 throw new RuntimeException("boom");
// //             }
// //         };

// //         ResponseEntity<?> response = bad.run(false);

// //         assertEquals(500, response.getStatusCodeValue());
// //     }
// // }


// package com.fdjloto.api.controller.admin;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.SpyBean;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.http.ResponseEntity;

// import java.nio.charset.StandardCharsets;
// import java.nio.file.*;
// import java.time.Instant;
// import java.util.Map;

// import static org.hamcrest.Matchers.containsString;
// import static org.hamcrest.Matchers.greaterThan;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// @ActiveProfiles("test")
// class AdminOwaspControllerIT {

//     @Autowired
//     private MockMvc mockMvc;

//     // ✅ On garde le vrai controller mais on peut mock certaines méthodes
//     @SpyBean
//     private AdminOwaspController controller;

//     private Path reportsDir;
//     private Path latest;

//     @BeforeEach
//     void setup() throws Exception {
//         reportsDir = Paths.get("reports/owasp").toAbsolutePath().normalize();
//         Files.createDirectories(reportsDir);

//         latest = reportsDir.resolve("latest.json");
//         Files.deleteIfExists(latest);
//     }

//     // =========================
//     // GET - NO REPORT
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturn404WhenNoReportExists() throws Exception {

//         mockMvc.perform(get("/api/admin/owasp-score"))
//                 .andExpect(status().isNotFound())
//                 .andExpect(jsonPath("$.error",
//                         containsString("No OWASP report")));
//     }

//     // =========================
//     // GET LAST REPORT
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnLastReport() throws Exception {

//         String json = """
//                 {
//                   "timestamp":"2026-03-16T22:00:00Z",
//                   "total":82,
//                   "grade":"B",
//                   "scores":{"A02":8},
//                   "raw":"debug"
//                 }
//                 """;

//         Files.writeString(latest, json, StandardCharsets.UTF_8);

//         mockMvc.perform(get("/api/admin/owasp-score"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.total").value(82))
//                 .andExpect(jsonPath("$.grade").value("B"))
//                 .andExpect(jsonPath("$.raw").doesNotExist());
//     }

//     // =========================
//     // GET DETAIL TRUE
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnRawWhenDetailTrue() throws Exception {

//         String json = """
//                 {
//                   "timestamp":"2026-03-16T22:00:00Z",
//                   "total":90,
//                   "grade":"A",
//                   "scores":{"A02":9},
//                   "raw":"RAW DATA"
//                 }
//                 """;

//         Files.writeString(latest, json, StandardCharsets.UTF_8);

//         mockMvc.perform(get("/api/admin/owasp-score")
//                         .param("detail", "true"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.raw").value("RAW DATA"));
//     }

//     // =========================
//     // RUN NORMAL (mock script)
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldRunAuditAndPersistFile_mocked() throws Exception {

//         doReturn("""
//             TOTAL........: 88 / 100
//             A02 Broken Auth: 9 / 10
//             A07 XSS: 8 / 10
//             A10 Logging: 9 / 10
//             __EXIT_CODE__=0
//         """).when(controller).runScript(anyBoolean());

//         mockMvc.perform(post("/api/admin/owasp-score/run"))
//                 .andExpect(status().isAccepted())
//                 .andExpect(jsonPath("$.total").value(88))
//                 .andExpect(jsonPath("$.grade").value("A"))
//                 .andExpect(jsonPath("$.scores.A02").value(9));

//         assertTrue(Files.exists(latest));
//     }

//     // =========================
//     // RUN REAL (optionnel mais pro)
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldRunAuditReal() throws Exception {

//         mockMvc.perform(post("/api/admin/owasp-score/run"))
//                 .andExpect(status().isAccepted())
//                 .andExpect(jsonPath("$.total").isNumber())
//                 .andExpect(jsonPath("$.total").value(greaterThan(0)));
//     }

//     // =========================
//     // COOLDOWN
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnExistingReportDuringCooldown() throws Exception {

//         String json = """
//                 {
//                   "timestamp":"2026-03-16T22:00:00Z",
//                   "total":75,
//                   "grade":"B",
//                   "scores":{"A02":7}
//                 }
//                 """;

//         Files.writeString(latest, json, StandardCharsets.UTF_8);

//         controller.setLastRunEpochSec(Instant.now().getEpochSecond());

//         mockMvc.perform(post("/api/admin/owasp-score/run"))
//                 .andExpect(status().isAccepted())
//                 .andExpect(jsonPath("$.grade").value("B"));
//     }

//     // =========================
//     // SECURITY
//     // =========================
//     @Test
//     @WithMockUser(roles = "USER")
//     void shouldReturnForbiddenForNonAdmin() throws Exception {

//         mockMvc.perform(get("/api/admin/owasp-score"))
//                 .andExpect(status().isForbidden());
//     }

//     // =========================
//     // PARSE
//     // =========================
//     @Test
//     void shouldParseOutputCorrectly() {

//         String fake = """
//             TOTAL........: 75 / 100
//             A02 Broken Auth: 7 / 10
//             A07 XSS: 8 / 10
//             __EXIT_CODE__=0
//         """;

//         Map<String, Object> result = controller.parseOutput(fake, false);

//         assertEquals(75, result.get("total"));
//         assertEquals("B", result.get("grade"));
//     }

//     // =========================
//     // ERROR
//     // =========================
//     @Test
//     void shouldReturn500WhenScriptFails() {

//         AdminOwaspController bad = new AdminOwaspController() {
//             @Override
//             protected String runScript(boolean detail) {
//                 throw new RuntimeException("boom");
//             }
//         };

//         ResponseEntity<?> response = bad.run(false);

//         assertEquals(500, response.getStatusCodeValue());
//     }
// }


package com.fdjloto.api.controller.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Instant;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminOwaspControllerIT {

    @Autowired
    private MockMvc mockMvc;

    // ✅ On garde le vrai controller mais on peut mock certaines méthodes
    @SpyBean
    private AdminOwaspController controller;

    private Path reportsDir;
    private Path latest;

    @BeforeEach
    void setup() throws Exception {
        reportsDir = Paths.get("reports/owasp").toAbsolutePath().normalize();
        Files.createDirectories(reportsDir);

        latest = reportsDir.resolve("latest.json");
        Files.deleteIfExists(latest);
    }

    // =========================
    // GET - NO REPORT
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenNoReportExists() throws Exception {

        mockMvc.perform(get("/api/admin/owasp-score"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error",
                        containsString("No OWASP report")));
    }

    // =========================
    // GET LAST REPORT
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnLastReport() throws Exception {

        String json = """
                {
                  "timestamp":"2026-03-16T22:00:00Z",
                  "total":82,
                  "grade":"B",
                  "scores":{"A02":8},
                  "raw":"debug"
                }
                """;

        Files.writeString(latest, json, StandardCharsets.UTF_8);

        mockMvc.perform(get("/api/admin/owasp-score"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(82))
                .andExpect(jsonPath("$.grade").value("B"))
                .andExpect(jsonPath("$.raw").doesNotExist());
    }

    // =========================
    // GET DETAIL TRUE
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnRawWhenDetailTrue() throws Exception {

        String json = """
                {
                  "timestamp":"2026-03-16T22:00:00Z",
                  "total":90,
                  "grade":"A",
                  "scores":{"A02":9},
                  "raw":"RAW DATA"
                }
                """;

        Files.writeString(latest, json, StandardCharsets.UTF_8);

        mockMvc.perform(get("/api/admin/owasp-score")
                        .param("detail", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.raw").value("RAW DATA"));
    }

    // =========================
    // RUN NORMAL (mock script)
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRunAuditAndPersistFile_mocked() throws Exception {

        doReturn("""
            TOTAL........: 88 / 100
            A02 Broken Auth: 9 / 10
            A07 XSS: 8 / 10
            A10 Logging: 9 / 10
            __EXIT_CODE__=0
        """).when(controller).runScript(anyBoolean());

        mockMvc.perform(post("/api/admin/owasp-score/run"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.total").value(88))
                .andExpect(jsonPath("$.grade").value("A"))
                .andExpect(jsonPath("$.scores.A02").value(9));

        assertTrue(Files.exists(latest));
    }

    // =========================
    // RUN REAL (optionnel mais pro)
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRunAuditReal() throws Exception {

        mockMvc.perform(post("/api/admin/owasp-score/run"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.total").isNumber())
                .andExpect(jsonPath("$.total").value(greaterThan(0)));
    }

    // =========================
    // COOLDOWN
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnExistingReportDuringCooldown() throws Exception {

        String json = """
                {
                  "timestamp":"2026-03-16T22:00:00Z",
                  "total":75,
                  "grade":"B",
                  "scores":{"A02":7}
                }
                """;

        Files.writeString(latest, json, StandardCharsets.UTF_8);

        controller.setLastRunEpochSec(Instant.now().getEpochSecond());

        mockMvc.perform(post("/api/admin/owasp-score/run"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.grade").value("B"));
    }

    // =========================
    // SECURITY
    // =========================
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenForNonAdmin() throws Exception {

        mockMvc.perform(get("/api/admin/owasp-score"))
                .andExpect(status().isForbidden());
    }

    // =========================
    // PARSE
    // =========================
    @Test
    void shouldParseOutputCorrectly() {

        String fake = """
            TOTAL........: 75 / 100
            A02 Broken Auth: 7 / 10
            A07 XSS: 8 / 10
            __EXIT_CODE__=0
        """;

        Map<String, Object> result = controller.parseOutput(fake, false);

        assertEquals(75, result.get("total"));
        assertEquals("B", result.get("grade"));
    }

    // =========================
    // ERROR
    // =========================
    @Test
    void shouldReturn500WhenScriptFails() {

        AdminOwaspController bad = new AdminOwaspController() {
            @Override
            protected String runScript(boolean detail) {
                throw new RuntimeException("boom");
            }
        };

        ResponseEntity<?> response = bad.run(false);

        assertEquals(500, response.getStatusCodeValue());
    }
}
