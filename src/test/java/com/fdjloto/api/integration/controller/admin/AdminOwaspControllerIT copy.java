// package com.fdjloto.api.controller.admin;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;

// import java.nio.charset.StandardCharsets;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;

// import static org.hamcrest.Matchers.containsString;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import org.springframework.test.context.ActiveProfiles;

// @SpringBootTest
// @AutoConfigureMockMvc
// @ActiveProfiles("test")
// class AdminOwaspControllerIT {

//     @Autowired
//     private MockMvc mockMvc;

//     private Path reportsDir;
//     private Path latest;

//     @BeforeEach
//     void setup() throws Exception {

//         reportsDir = Paths.get("reports/owasp").toAbsolutePath().normalize();
//         Files.createDirectories(reportsDir);

//         latest = reportsDir.resolve("latest.json");

//         // nettoyage avant chaque test
//         Files.deleteIfExists(latest);
//     }

//     /**
//      * GET /api/admin/owasp-score
//      * -> aucun rapport
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturn404WhenNoReportExists() throws Exception {

//         mockMvc.perform(get("/api/admin/owasp-score"))
//                 .andExpect(status().isNotFound())
//                 .andExpect(jsonPath("$.error",
//                         containsString("No OWASP report")));
//     }

//     /**
//      * GET dernier rapport
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnLastReport() throws Exception {

//         String json = """
//                 {
//                   "timestamp":"2026-03-16T22:00:00Z",
//                   "total":82,
//                   "grade":"B",
//                   "scores":{"A02":8,"A07":7,"A10":9},
//                   "raw":"debug-output"
//                 }
//                 """;

//         Files.writeString(latest, json, StandardCharsets.UTF_8);

//         mockMvc.perform(get("/api/admin/owasp-score"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.total").value(82))
//                 .andExpect(jsonPath("$.grade").value("B"))
//                 .andExpect(jsonPath("$.scores.A02").value(8))
//                 .andExpect(jsonPath("$.raw").doesNotExist());
//     }

//     /**
//      * GET avec detail=true
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnRawWhenDetailTrue() throws Exception {

//         String json = """
//                 {
//                   "timestamp":"2026-03-16T22:00:00Z",
//                   "total":90,
//                   "grade":"A",
//                   "scores":{"A02":9,"A07":9,"A10":9},
//                   "raw":"OWASP RAW OUTPUT"
//                 }
//                 """;

//         Files.writeString(latest, json, StandardCharsets.UTF_8);

//         mockMvc.perform(get("/api/admin/owasp-score")
//                         .param("detail","true"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.raw").value("OWASP RAW OUTPUT"));
//     }

//     /**
//      * POST /run -> cooldown renvoie dernier rapport
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void runShouldReturnExistingReportDuringCooldown() throws Exception {

//         String json = """
//                 {
//                   "timestamp":"2026-03-16T22:00:00Z",
//                   "total":75,
//                   "grade":"B",
//                   "scores":{"A02":7,"A07":7,"A10":8}
//                 }
//                 """;

//         Files.writeString(latest, json, StandardCharsets.UTF_8);

//         mockMvc.perform(post("/api/admin/owasp-score/run"))
//                 .andExpect(status().isAccepted()) // ⚠️ 202
//                 .andExpect(jsonPath("$.grade").value("B"))
//                 .andExpect(jsonPath("$.scores.A02").value(7));
//     }

//     /**
//      * sécurité : USER non admin
//      */
//     @Test
//     @WithMockUser(roles = "USER")
//     void shouldReturnForbiddenForNonAdmin() throws Exception {

//         mockMvc.perform(get("/api/admin/owasp-score"))
//                 .andExpect(status().isForbidden());
//     }
// }
