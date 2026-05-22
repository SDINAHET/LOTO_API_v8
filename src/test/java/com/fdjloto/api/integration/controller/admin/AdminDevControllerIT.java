// // // // // // // package com.fdjloto.api.controller.admin;

// // // // // // // import org.junit.jupiter.api.BeforeEach;
// // // // // // // import org.junit.jupiter.api.Test;
// // // // // // // import org.springframework.beans.factory.annotation.Autowired;
// // // // // // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // // // // // import org.springframework.boot.test.context.SpringBootTest;
// // // // // // // import org.springframework.security.test.context.support.WithMockUser;
// // // // // // // import org.springframework.test.context.ActiveProfiles;
// // // // // // // import org.springframework.test.web.servlet.MockMvc;

// // // // // // // import java.nio.file.*;

// // // // // // // import static org.hamcrest.Matchers.containsString;
// // // // // // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // // // // // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // // // // // // @SpringBootTest
// // // // // // // @AutoConfigureMockMvc
// // // // // // // @ActiveProfiles("test")
// // // // // // // class AdminDevControllerIT {

// // // // // // //     @Autowired
// // // // // // //     private MockMvc mockMvc;

// // // // // // //     private Path coverageFile;

// // // // // // //     @BeforeEach
// // // // // // //     void setup() throws Exception {
// // // // // // //         coverageFile = Paths.get("target/site/jacoco/index.html");

// // // // // // //         // 🔥 création fichier mock
// // // // // // //         Files.createDirectories(coverageFile.getParent());
// // // // // // //         Files.writeString(coverageFile, "<html><body>TEST COVERAGE</body></html>");
// // // // // // //     }

// // // // // // //     // =========================
// // // // // // //     // ✅ SUCCESS (ADMIN)
// // // // // // //     // =========================
// // // // // // //     @Test
// // // // // // //     @WithMockUser(roles = "ADMIN")
// // // // // // //     void shouldReturnCoverageHtmlForAdmin() throws Exception {

// // // // // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // // //                 .andExpect(status().isOk())
// // // // // // //                 // ✅ FIX PRO (Spring ajoute charset)
// // // // // // //                 .andExpect(content().contentTypeCompatibleWith("text/html"))
// // // // // // //                 .andExpect(content().string(containsString("TEST COVERAGE")));
// // // // // // //     }

// // // // // // //     // =========================
// // // // // // //     // ❌ FILE NOT FOUND
// // // // // // //     // =========================
// // // // // // //     @Test
// // // // // // //     @WithMockUser(roles = "ADMIN")
// // // // // // //     void shouldReturn404WhenFileDoesNotExist() throws Exception {

// // // // // // //         Files.deleteIfExists(coverageFile);

// // // // // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // // //                 .andExpect(status().isNotFound());
// // // // // // //     }

// // // // // // //     // =========================
// // // // // // //     // 🔐 SECURITY
// // // // // // //     // =========================
// // // // // // //     @Test
// // // // // // //     @WithMockUser(roles = "USER")
// // // // // // //     void shouldReturnForbiddenForNonAdmin() throws Exception {

// // // // // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // // //                 .andExpect(status().isForbidden());
// // // // // // //     }
// // // // // // // }

// // // // // // package com.fdjloto.api.controller.admin;

// // // // // // import org.junit.jupiter.api.BeforeEach;
// // // // // // import org.junit.jupiter.api.Test;
// // // // // // import org.springframework.beans.factory.annotation.Autowired;
// // // // // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // // // // import org.springframework.boot.test.context.SpringBootTest;
// // // // // // import org.springframework.security.test.context.support.WithMockUser;
// // // // // // import org.springframework.test.context.ActiveProfiles;
// // // // // // import org.springframework.test.web.servlet.MockMvc;

// // // // // // import java.nio.file.*;

// // // // // // import static org.hamcrest.Matchers.containsString;
// // // // // // import static org.junit.jupiter.api.Assertions.assertTrue;
// // // // // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // // // // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// // // // // // import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

// // // // // // @SpringBootTest
// // // // // // @AutoConfigureMockMvc
// // // // // // @ActiveProfiles("test")
// // // // // // class AdminDevControllerIT {

// // // // // //     @Autowired
// // // // // //     private MockMvc mockMvc;

// // // // // //     private Path coverageFile;

// // // // // //     @BeforeEach
// // // // // //     void setup() throws Exception {

// // // // // //         // 🔥 même logique que le controller (important)
// // // // // //         coverageFile = Paths.get(System.getProperty("user.dir"))
// // // // // //                 .resolve("target/site/jacoco/index.html")
// // // // // //                 .normalize();

// // // // // //         // création du dossier
// // // // // //         Files.createDirectories(coverageFile.getParent());

// // // // // //         // 🔥 nettoyage propre
// // // // // //         Files.deleteIfExists(coverageFile);

// // // // // //         // 🔥 création fichier mock
// // // // // //         Files.writeString(coverageFile, "<html><body>TEST COVERAGE</body></html>");

// // // // // //         // sanity check (très pro)
// // // // // //         assertTrue(Files.exists(coverageFile));
// // // // // //     }

// // // // // //     // =========================
// // // // // //     // ✅ SUCCESS (ADMIN)
// // // // // //     // =========================
// // // // // //     @Test
// // // // // //     @WithMockUser(roles = "ADMIN")
// // // // // //     void shouldReturnCoverageHtmlForAdmin() throws Exception {

// // // // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // //                 .andExpect(status().isOk())
// // // // // //                 // ✅ compatible charset UTF-8
// // // // // //                 .andExpect(content().contentTypeCompatibleWith("text/html"))
// // // // // //                 .andExpect(content().string(containsString("TEST COVERAGE")));
// // // // // //     }

// // // // // //     // =========================
// // // // // //     // ❌ FILE NOT FOUND
// // // // // //     // =========================
// // // // // //     @Test
// // // // // //     @WithMockUser(roles = "ADMIN")
// // // // // //     void shouldReturn404WhenFileDoesNotExist() throws Exception {

// // // // // //         Files.deleteIfExists(coverageFile);

// // // // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // //                 .andExpect(status().isNotFound());
// // // // // //     }

// // // // // //     // =========================
// // // // // //     // 🔐 SECURITY
// // // // // //     // =========================
// // // // // //     @Test
// // // // // //     @WithMockUser(roles = "USER")
// // // // // //     void shouldReturnForbiddenForNonAdmin() throws Exception {

// // // // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // //                 .andExpect(status().isForbidden());
// // // // // //     }


// // // // // //     @Test
// // // // // // 	@WithMockUser(roles = "ADMIN")
// // // // // // 	void shouldHandleCorruptedFileGracefully() throws Exception {

// // // // // // 		// fichier "cassé"
// // // // // // 		Files.writeString(coverageFile, "\u0000\u0000\u0000");

// // // // // // 		mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // // 				.andExpect(status().isOk()); // le controller doit survivre
// // // // // // 	}

// // // // // // 	@Test
// // // // // // 	@WithMockUser(roles = "ADMIN")
// // // // // // 	void shouldRespondUnder200ms() throws Exception {

// // // // // // 		long start = System.currentTimeMillis();

// // // // // // 		mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // // 				.andExpect(status().isOk());

// // // // // // 		long duration = System.currentTimeMillis() - start;

// // // // // // 		System.out.println("⏱ Temps réponse: " + duration + "ms");

// // // // // // 		assertTrue(duration < 200, "API trop lente !");
// // // // // // 	}

// // // // // // 	@Test
// // // // // // 	void shouldHandleDifferentUserRoles() throws Exception {

// // // // // // 		// ADMIN → OK
// // // // // // 		mockMvc.perform(get("/api/admin/dev/coverage")
// // // // // // 				.with(user("admin").roles("ADMIN")))
// // // // // // 				.andExpect(status().isOk());

// // // // // // 		// USER → interdit
// // // // // // 		mockMvc.perform(get("/api/admin/dev/coverage")
// // // // // // 				.with(user("user").roles("USER")))
// // // // // // 				.andExpect(status().isForbidden());

// // // // // // 		// ANONYME → interdit
// // // // // // 		mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // // 				.andExpect(status().isUnauthorized());
// // // // // // 	}

// // // // // // }


// // // // // // package com.fdjloto.api.controller.admin;

// // // // // // import org.junit.jupiter.api.BeforeEach;
// // // // // // import org.junit.jupiter.api.Test;
// // // // // // import org.springframework.beans.factory.annotation.Autowired;
// // // // // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // // // // import org.springframework.boot.test.context.SpringBootTest;
// // // // // // import org.springframework.security.test.context.support.WithMockUser;
// // // // // // import org.springframework.test.context.ActiveProfiles;
// // // // // // import org.springframework.test.web.servlet.MockMvc;

// // // // // // import java.nio.file.*;

// // // // // // import static org.hamcrest.Matchers.containsString;
// // // // // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // // // // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // // // // // @SpringBootTest
// // // // // // @AutoConfigureMockMvc
// // // // // // @ActiveProfiles("test")
// // // // // // class AdminDevControllerIT {

// // // // // //     @Autowired
// // // // // //     private MockMvc mockMvc;

// // // // // //     private Path coverageFile;

// // // // // //     @BeforeEach
// // // // // //     void setup() throws Exception {
// // // // // //         coverageFile = Paths.get("target/site/jacoco/index.html");

// // // // // //         // 🔥 création fichier mock
// // // // // //         Files.createDirectories(coverageFile.getParent());
// // // // // //         Files.writeString(coverageFile, "<html><body>TEST COVERAGE</body></html>");
// // // // // //     }

// // // // // //     // =========================
// // // // // //     // ✅ SUCCESS (ADMIN)
// // // // // //     // =========================
// // // // // //     @Test
// // // // // //     @WithMockUser(roles = "ADMIN")
// // // // // //     void shouldReturnCoverageHtmlForAdmin() throws Exception {

// // // // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // //                 .andExpect(status().isOk())
// // // // // //                 // ✅ FIX PRO (Spring ajoute charset)
// // // // // //                 .andExpect(content().contentTypeCompatibleWith("text/html"))
// // // // // //                 .andExpect(content().string(containsString("TEST COVERAGE")));
// // // // // //     }

// // // // // //     // =========================
// // // // // //     // ❌ FILE NOT FOUND
// // // // // //     // =========================
// // // // // //     @Test
// // // // // //     @WithMockUser(roles = "ADMIN")
// // // // // //     void shouldReturn404WhenFileDoesNotExist() throws Exception {

// // // // // //         Files.deleteIfExists(coverageFile);

// // // // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // //                 .andExpect(status().isNotFound());
// // // // // //     }

// // // // // //     // =========================
// // // // // //     // 🔐 SECURITY
// // // // // //     // =========================
// // // // // //     @Test
// // // // // //     @WithMockUser(roles = "USER")
// // // // // //     void shouldReturnForbiddenForNonAdmin() throws Exception {

// // // // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // //                 .andExpect(status().isForbidden());
// // // // // //     }
// // // // // // }

// // // // // package com.fdjloto.api.controller.admin;

// // // // // import org.junit.jupiter.api.BeforeEach;
// // // // // import org.junit.jupiter.api.Test;
// // // // // import org.springframework.beans.factory.annotation.Autowired;
// // // // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // // // import org.springframework.boot.test.context.SpringBootTest;
// // // // // import org.springframework.security.test.context.support.WithMockUser;
// // // // // import org.springframework.test.context.ActiveProfiles;
// // // // // import org.springframework.test.web.servlet.MockMvc;

// // // // // import java.nio.file.*;

// // // // // import static org.hamcrest.Matchers.containsString;
// // // // // import static org.junit.jupiter.api.Assertions.assertTrue;
// // // // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // // // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// // // // // import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

// // // // // @SpringBootTest
// // // // // @AutoConfigureMockMvc
// // // // // @ActiveProfiles("test")
// // // // // class AdminDevControllerIT {

// // // // //     @Autowired
// // // // //     private MockMvc mockMvc;

// // // // //     private Path coverageFile;

// // // // //     @BeforeEach
// // // // //     void setup() throws Exception {

// // // // //         // 🔥 même logique que le controller (important)
// // // // //         coverageFile = Paths.get(System.getProperty("user.dir"))
// // // // //                 .resolve("target/site/jacoco/index.html")
// // // // //                 .normalize();

// // // // //         // création du dossier
// // // // //         Files.createDirectories(coverageFile.getParent());

// // // // //         // 🔥 nettoyage propre
// // // // //         Files.deleteIfExists(coverageFile);

// // // // //         // 🔥 création fichier mock
// // // // //         Files.writeString(coverageFile, "<html><body>TEST COVERAGE</body></html>");

// // // // //         // sanity check (très pro)
// // // // //         assertTrue(Files.exists(coverageFile));
// // // // //     }

// // // // //     // =========================
// // // // //     // ✅ SUCCESS (ADMIN)
// // // // //     // =========================
// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldReturnCoverageHtmlForAdmin() throws Exception {

// // // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // //                 .andExpect(status().isOk())
// // // // //                 // ✅ compatible charset UTF-8
// // // // //                 .andExpect(content().contentTypeCompatibleWith("text/html"))
// // // // //                 .andExpect(content().string(containsString("TEST COVERAGE")));
// // // // //     }

// // // // //     // =========================
// // // // //     // ❌ FILE NOT FOUND
// // // // //     // =========================
// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldReturn404WhenFileDoesNotExist() throws Exception {

// // // // //         Files.deleteIfExists(coverageFile);

// // // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // //                 .andExpect(status().isNotFound());
// // // // //     }

// // // // //     // =========================
// // // // //     // 🔐 SECURITY
// // // // //     // =========================
// // // // //     @Test
// // // // //     @WithMockUser(roles = "USER")
// // // // //     void shouldReturnForbiddenForNonAdmin() throws Exception {

// // // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // //                 .andExpect(status().isForbidden());
// // // // //     }


// // // // //     @Test
// // // // // 	@WithMockUser(roles = "ADMIN")
// // // // // 	void shouldHandleCorruptedFileGracefully() throws Exception {

// // // // // 		// fichier "cassé"
// // // // // 		Files.writeString(coverageFile, "\u0000\u0000\u0000");

// // // // // 		mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // 				.andExpect(status().isOk()); // le controller doit survivre
// // // // // 	}

// // // // // 	@Test
// // // // // 	@WithMockUser(roles = "ADMIN")
// // // // // 	void shouldRespondUnder200ms() throws Exception {

// // // // // 		long start = System.currentTimeMillis();

// // // // // 		mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // 				.andExpect(status().isOk());

// // // // // 		long duration = System.currentTimeMillis() - start;

// // // // // 		System.out.println("⏱ Temps réponse: " + duration + "ms");

// // // // // 		assertTrue(duration < 200, "API trop lente !");
// // // // // 	}

// // // // // 	@Test
// // // // // 	void shouldHandleDifferentUserRoles() throws Exception {

// // // // // 		// ADMIN → OK
// // // // // 		mockMvc.perform(get("/api/admin/dev/coverage")
// // // // // 				.with(user("admin").roles("ADMIN")))
// // // // // 				.andExpect(status().isOk());

// // // // // 		// USER → interdit
// // // // // 		mockMvc.perform(get("/api/admin/dev/coverage")
// // // // // 				.with(user("user").roles("USER")))
// // // // // 				.andExpect(status().isForbidden());

// // // // // 		// ANONYME → interdit
// // // // // 		mockMvc.perform(get("/api/admin/dev/coverage"))
// // // // // 				.andExpect(status().isUnauthorized());
// // // // // 	}

// // // // // }


// // // // package com.fdjloto.api.controller.admin;

// // // // import org.junit.jupiter.api.BeforeEach;
// // // // import org.junit.jupiter.api.Test;
// // // // import org.springframework.beans.factory.annotation.Autowired;
// // // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // // import org.springframework.boot.test.context.SpringBootTest;
// // // // import org.springframework.security.test.context.support.WithMockUser;
// // // // import org.springframework.test.context.ActiveProfiles;
// // // // import org.springframework.test.web.servlet.MockMvc;

// // // // import java.nio.file.*;

// // // // import static org.hamcrest.Matchers.containsString;
// // // // import static org.junit.jupiter.api.Assertions.assertTrue;
// // // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// // // // import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

// // // // @SpringBootTest
// // // // @AutoConfigureMockMvc
// // // // @ActiveProfiles("test")
// // // // class AdminDevControllerIT {

// // // //     @Autowired
// // // //     private MockMvc mockMvc;

// // // //     private Path coverageFile;
// // // //     private Path coverageDir;

// // // //     @BeforeEach
// // // //     void setup() throws Exception {

// // // //         coverageDir = Paths.get(System.getProperty("user.dir"))
// // // //                 .resolve("target/site/jacoco")
// // // //                 .normalize();

// // // //         coverageFile = coverageDir.resolve("index.html");

// // // //         // 🔥 create dir
// // // //         Files.createDirectories(coverageDir);

// // // //         // 🔥 clean
// // // //         Files.deleteIfExists(coverageFile);

// // // //         // 🔥 fake JaCoCo content (REALISTIC)
// // // //         Files.writeString(coverageFile, """
// // // //             <html>
// // // //                 <head></head>
// // // //                 <body>
// // // //                     <table>
// // // //                         <tr><td>Total</td><td>84%</td></tr>
// // // //                     </table>
// // // //                     TEST COVERAGE
// // // //                 </body>
// // // //             </html>
// // // //         """);

// // // //         assertTrue(Files.exists(coverageFile));
// // // //     }

// // // //     // =========================
// // // //     // ✅ COVERAGE HTML
// // // //     // =========================
// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldReturnCoverageHtmlForAdmin() throws Exception {

// // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // //                 .andExpect(status().isOk())
// // // //                 .andExpect(content().contentTypeCompatibleWith("text/html"))
// // // //                 .andExpect(content().string(containsString("TEST COVERAGE")));
// // // //     }

// // // //     // =========================
// // // //     // ❌ FILE NOT FOUND
// // // //     // =========================
// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldReturn404WhenFileDoesNotExist() throws Exception {

// // // //         Files.deleteIfExists(coverageFile);

// // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // //                 .andExpect(status().isNotFound());
// // // //     }

// // // //     // =========================
// // // //     // 🔐 SECURITY
// // // //     // =========================
// // // //     @Test
// // // //     @WithMockUser(roles = "USER")
// // // //     void shouldReturnForbiddenForNonAdmin() throws Exception {

// // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // //                 .andExpect(status().isForbidden());
// // // //     }

// // // //     // =========================
// // // //     // 🔥 SUMMARY
// // // //     // =========================
// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldReturnCoverageSummary() throws Exception {

// // // //         mockMvc.perform(get("/api/admin/dev/coverage/summary"))
// // // //                 .andExpect(status().isOk())
// // // //                 .andExpect(jsonPath("$.coverage").exists());
// // // //     }

// // // //     // =========================
// // // //     // 🔥 FILES (CSS / JS / HTML)
// // // //     // =========================
// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldServeCoverageFiles() throws Exception {

// // // //         Path cssFile = coverageDir.resolve("style.css");
// // // //         Files.writeString(cssFile, "body { background: red; }");

// // // //         mockMvc.perform(get("/api/admin/dev/coverage/files/style.css"))
// // // //                 .andExpect(status().isOk());
// // // //     }

// // // //     // =========================
// // // //     // ⚠️ CORRUPTED FILE
// // // //     // =========================
// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldHandleCorruptedFileGracefully() throws Exception {

// // // //         Files.writeString(coverageFile, "\u0000\u0000\u0000");

// // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // //                 .andExpect(status().isOk());
// // // //     }

// // // //     // =========================
// // // //     // ⚡ PERFORMANCE
// // // //     // =========================
// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldRespondUnder200ms() throws Exception {

// // // //         long start = System.currentTimeMillis();

// // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // //                 .andExpect(status().isOk());

// // // //         long duration = System.currentTimeMillis() - start;

// // // //         System.out.println("⏱ Temps réponse: " + duration + "ms");

// // // //         assertTrue(duration < 200, "API trop lente !");
// // // //     }

// // // //     // =========================
// // // //     // 🔐 ROLES
// // // //     // =========================
// // // //     @Test
// // // //     void shouldHandleDifferentUserRoles() throws Exception {

// // // //         // ADMIN
// // // //         mockMvc.perform(get("/api/admin/dev/coverage")
// // // //                 .with(user("admin").roles("ADMIN")))
// // // //                 .andExpect(status().isOk());

// // // //         // USER
// // // //         mockMvc.perform(get("/api/admin/dev/coverage")
// // // //                 .with(user("user").roles("USER")))
// // // //                 .andExpect(status().isForbidden());

// // // //         // ANONYME
// // // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // // //                 .andExpect(status().isUnauthorized());
// // // //     }
// // // // }

// // // package com.fdjloto.api.controller.admin;

// // // import org.junit.jupiter.api.BeforeEach;
// // // import org.junit.jupiter.api.Test;
// // // import org.springframework.beans.factory.annotation.Autowired;
// // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // import org.springframework.boot.test.context.SpringBootTest;
// // // import org.springframework.security.test.context.support.WithMockUser;
// // // import org.springframework.test.context.ActiveProfiles;
// // // import org.springframework.test.web.servlet.MockMvc;

// // // import java.nio.file.*;

// // // import static org.hamcrest.Matchers.containsString;
// // // import static org.junit.jupiter.api.Assertions.assertTrue;
// // // import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
// // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // // @SpringBootTest
// // // @AutoConfigureMockMvc
// // // @ActiveProfiles("test")
// // // class AdminDevControllerIT {

// // //     @Autowired
// // //     private MockMvc mockMvc;

// // //     private Path coverageFile;
// // //     private Path coverageDir;

// // //     @BeforeEach
// // //     void setup() throws Exception {

// // //         coverageDir = Paths.get(System.getProperty("user.dir"))
// // //                 .resolve("target/site/jacoco")
// // //                 .normalize();

// // //         coverageFile = coverageDir.resolve("index.html");

// // //         Files.createDirectories(coverageDir);
// // //         Files.deleteIfExists(coverageFile);

// // //         // 🔥 contenu réaliste JaCoCo
// // //         Files.writeString(coverageFile, """
// // //             <html>
// // //                 <head></head>
// // //                 <body>
// // //                     <table>
// // //                         <tr><td>Total</td><td>84%</td></tr>
// // //                     </table>
// // //                     TEST COVERAGE
// // //                 </body>
// // //             </html>
// // //         """);

// // //         assertTrue(Files.exists(coverageFile));
// // //     }

// // //     // =========================
// // //     // ✅ COVERAGE HTML
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldReturnCoverageHtmlForAdmin() throws Exception {

// // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(content().contentTypeCompatibleWith("text/html"))
// // //                 .andExpect(content().string(containsString("TEST COVERAGE")));
// // //     }

// // //     // =========================
// // //     // ❌ FILE NOT FOUND
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldReturn404WhenFileDoesNotExist() throws Exception {

// // //         Files.deleteIfExists(coverageFile);

// // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // //                 .andExpect(status().isNotFound());
// // //     }

// // //     // =========================
// // //     // 🔐 SECURITY ROLE
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "USER")
// // //     void shouldReturnForbiddenForNonAdmin() throws Exception {

// // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // //                 .andExpect(status().isForbidden());
// // //     }

// // //     // =========================
// // //     // 🔥 SUMMARY
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldReturnCoverageSummary() throws Exception {

// // //         mockMvc.perform(get("/api/admin/dev/coverage/summary"))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(jsonPath("$.coverage").exists());
// // //     }

// // //     // =========================
// // //     // 📁 FILES SERVING
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldServeCoverageFiles() throws Exception {

// // //         Path cssFile = coverageDir.resolve("style.css");
// // //         Files.writeString(cssFile, "body { background: red; }");

// // //         mockMvc.perform(get("/api/admin/dev/coverage/files/style.css"))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(content().string(containsString("background")));
// // //     }

// // //     // =========================
// // //     // 🔥 PATH TRAVERSAL SECURITY
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldPreventPathTraversal() throws Exception {

// // //         mockMvc.perform(get("/api/admin/dev/coverage/files/../../../../etc/passwd"))
// // //                 .andExpect(status().isBadRequest());
// // //     }

// // //     // =========================
// // //     // ⚠️ CORRUPTED FILE
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldHandleCorruptedFileGracefully() throws Exception {

// // //         Files.writeString(coverageFile, "\u0000\u0000\u0000");

// // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // //                 .andExpect(status().isOk());
// // //     }

// // //     // =========================
// // //     // ⚡ PERFORMANCE ENDPOINT
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldReturnPerformanceMetric() throws Exception {

// // //         mockMvc.perform(get("/api/admin/dev/coverage/performance"))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(jsonPath("$.responseTime").exists());
// // //     }

// // //     // =========================
// // //     // ⚡ PERFORMANCE SPEED
// // //     // =========================
// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldRespondUnder200ms() throws Exception {

// // //         long start = System.currentTimeMillis();

// // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // //                 .andExpect(status().isOk());

// // //         long duration = System.currentTimeMillis() - start;

// // //         System.out.println("⏱ Temps réponse: " + duration + "ms");

// // //         assertTrue(duration < 200, "API trop lente !");
// // //     }

// // //     // =========================
// // //     // 🔐 ROLES GLOBAL TEST
// // //     // =========================
// // //     @Test
// // //     void shouldHandleDifferentUserRoles() throws Exception {

// // //         // ADMIN
// // //         mockMvc.perform(get("/api/admin/dev/coverage")
// // //                 .with(user("admin").roles("ADMIN")))
// // //                 .andExpect(status().isOk());

// // //         // USER
// // //         mockMvc.perform(get("/api/admin/dev/coverage")
// // //                 .with(user("user").roles("USER")))
// // //                 .andExpect(status().isForbidden());

// // //         // ANONYME
// // //         mockMvc.perform(get("/api/admin/dev/coverage"))
// // //                 .andExpect(status().isUnauthorized());
// // //     }
// // // }

// // package com.fdjloto.api.controller.admin;

// // import org.junit.jupiter.api.BeforeEach;
// // import org.junit.jupiter.api.Test;
// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // import org.springframework.boot.test.context.SpringBootTest;
// // import org.springframework.security.test.context.support.WithMockUser;
// // import org.springframework.test.context.ActiveProfiles;
// // import org.springframework.test.web.servlet.MockMvc;

// // import java.nio.file.*;

// // import static org.hamcrest.Matchers.containsString;
// // import static org.junit.jupiter.api.Assertions.assertTrue;
// // import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
// // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // @SpringBootTest
// // @AutoConfigureMockMvc
// // @ActiveProfiles("test")
// // class AdminDevControllerIT {

// //     @Autowired
// //     private MockMvc mockMvc;

// //     private Path coverageFile;
// //     private Path coverageDir;

// //     @BeforeEach
// //     void setup() throws Exception {

// //         coverageDir = Paths.get(System.getProperty("user.dir"))
// //                 .resolve("target/site/jacoco")
// //                 .normalize();

// //         coverageFile = coverageDir.resolve("index.html");

// //         Files.createDirectories(coverageDir);
// //         Files.deleteIfExists(coverageFile);

// //         Files.writeString(coverageFile, """
// //             <html>
// //                 <head></head>
// //                 <body>
// //                     <table>
// //                         <tr><td>Total</td><td>84%</td></tr>
// //                     </table>
// //                     <a href="com.fdjloto.api.service/index.html">Service</a>
// //                     TEST COVERAGE
// //                 </body>
// //             </html>
// //         """);

// //         assertTrue(Files.exists(coverageFile));
// //     }

// //     // =========================
// //     // ✅ COVERAGE HTML + REWRITE
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldReturnCoverageHtmlForAdmin() throws Exception {

// //         mockMvc.perform(get("/api/admin/dev/coverage"))
// //                 .andExpect(status().isOk())
// //                 .andExpect(content().contentTypeCompatibleWith("text/html"))
// //                 .andExpect(content().string(containsString("TEST COVERAGE")))
// //                 // 🔥 check rewrite lien
// //                 .andExpect(content().string(containsString("/api/admin/dev/coverage/files/com.fdjloto.api.service/index.html")));
// //     }

// //     // =========================
// //     // ❌ FILE NOT FOUND
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldReturn404WhenFileDoesNotExist() throws Exception {

// //         Files.deleteIfExists(coverageFile);

// //         mockMvc.perform(get("/api/admin/dev/coverage"))
// //                 .andExpect(status().isNotFound());
// //     }

// //     // =========================
// //     // 🔐 SECURITY ROLE
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "USER")
// //     void shouldReturnForbiddenForNonAdmin() throws Exception {

// //         mockMvc.perform(get("/api/admin/dev/coverage"))
// //                 .andExpect(status().isForbidden());
// //     }

// //     // =========================
// //     // 🔥 SUMMARY
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldReturnCoverageSummary() throws Exception {

// //         mockMvc.perform(get("/api/admin/dev/coverage/summary"))
// //                 .andExpect(status().isOk())
// //                 .andExpect(jsonPath("$.coverage").exists());
// //     }

// //     // =========================
// //     // 📁 FILES SERVING
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldServeCoverageFiles() throws Exception {

// //         Path cssFile = coverageDir.resolve("style.css");
// //         Files.writeString(cssFile, "body { background: red; }");

// //         mockMvc.perform(get("/api/admin/dev/coverage/files/style.css"))
// //                 .andExpect(status().isOk())
// //                 .andExpect(content().string(containsString("background")));
// //     }

// //     // =========================
// //     // 🔥 JACOCO RESOURCES (CRITIQUE)
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldServeJacocoResourcesFallback() throws Exception {

// //         Path resourcesDir = coverageDir.resolve("jacoco-resources");
// //         Files.createDirectories(resourcesDir);

// //         Path gif = resourcesDir.resolve("greenbar.gif");
// //         Files.write(gif, new byte[]{1,2,3});

// //         mockMvc.perform(get("/api/admin/dev/coverage/files/jacoco-resources/greenbar.gif"))
// //                 .andExpect(status().isOk());
// //     }

// //     // =========================
// //     // 🔥 PATH TRAVERSAL SECURITY
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldPreventPathTraversal() throws Exception {

// //         mockMvc.perform(get("/api/admin/dev/coverage/files/../../../../etc/passwd"))
// //                 .andExpect(status().isBadRequest());
// //     }

// //     // =========================
// //     // ⚠️ CORRUPTED FILE
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldHandleCorruptedFileGracefully() throws Exception {

// //         Files.writeString(coverageFile, "\u0000\u0000\u0000");

// //         mockMvc.perform(get("/api/admin/dev/coverage"))
// //                 .andExpect(status().isOk());
// //     }

// //     // =========================
// //     // ⚡ PERFORMANCE ENDPOINT
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldReturnPerformanceMetric() throws Exception {

// //         mockMvc.perform(get("/api/admin/dev/coverage/performance"))
// //                 .andExpect(status().isOk())
// //                 .andExpect(jsonPath("$.responseTime").exists());
// //     }

// //     // =========================
// //     // ⚡ PERFORMANCE SPEED
// //     // =========================
// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldRespondUnder200ms() throws Exception {

// //         long start = System.currentTimeMillis();

// //         mockMvc.perform(get("/api/admin/dev/coverage"))
// //                 .andExpect(status().isOk());

// //         long duration = System.currentTimeMillis() - start;

// //         System.out.println("⏱ Temps réponse: " + duration + "ms");

// //         assertTrue(duration < 200, "API trop lente !");
// //     }

// //     // =========================
// //     // 🔐 ROLES GLOBAL TEST
// //     // =========================
// //     @Test
// //     void shouldHandleDifferentUserRoles() throws Exception {

// //         // ADMIN
// //         mockMvc.perform(get("/api/admin/dev/coverage")
// //                 .with(user("admin").roles("ADMIN")))
// //                 .andExpect(status().isOk());

// //         // USER
// //         mockMvc.perform(get("/api/admin/dev/coverage")
// //                 .with(user("user").roles("USER")))
// //                 .andExpect(status().isForbidden());

// //         // ANONYME
// //         mockMvc.perform(get("/api/admin/dev/coverage"))
// //                 .andExpect(status().isUnauthorized());
// //     }
// // }

// package com.fdjloto.api.controller.admin;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;

// import java.nio.file.*;

// import static org.hamcrest.Matchers.containsString;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// @ActiveProfiles("test")
// class AdminDevControllerIT {

//     @Autowired
//     private MockMvc mockMvc;

//     private Path coverageDir;
//     private Path coverageFile;

//     @BeforeEach
//     void setup() throws Exception {

//         coverageDir = Paths.get(System.getProperty("user.dir"))
//                 .resolve("target/site/jacoco")
//                 .normalize();

//         coverageFile = coverageDir.resolve("index.html");

//         // 🔥 clean setup
//         Files.createDirectories(coverageDir);
//         Files.deleteIfExists(coverageFile);

//         // 🔥 HTML réaliste avec lien relatif (important pour rewrite)
//         Files.writeString(coverageFile, """
//             <html>
//                 <head></head>
//                 <body>
//                     <table>
//                         <tr><td>Total</td><td>84%</td></tr>
//                     </table>

//                     <a href="com.fdjloto.api.service/index.html">Service</a>

//                     TEST COVERAGE
//                 </body>
//             </html>
//         """);

//         assertTrue(Files.exists(coverageFile));
//     }

//     // =========================
//     // ✅ HTML + CSS + REWRITE
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnCoverageHtmlAndRewriteLinks() throws Exception {

//         mockMvc.perform(get("/api/admin/dev/coverage"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentTypeCompatibleWith("text/html"))
//                 .andExpect(content().string(containsString("TEST COVERAGE")))

//                 // 🔥 CSS injecté
//                 .andExpect(content().string(containsString("background:#0b1220")))

//                 // 🔥 lien réécrit
//                 .andExpect(content().string(containsString(
//                         "/api/admin/dev/coverage/files/com.fdjloto.api.service/index.html"
//                 )));
//     }

//     // =========================
//     // ❌ 404
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturn404WhenCoverageFileMissing() throws Exception {

//         Files.deleteIfExists(coverageFile);

//         mockMvc.perform(get("/api/admin/dev/coverage"))
//                 .andExpect(status().isNotFound());
//     }

//     // =========================
//     // 🔐 SECURITY
//     // =========================
//     @Test
//     @WithMockUser(roles = "USER")
//     void shouldRejectNonAdminUser() throws Exception {

//         mockMvc.perform(get("/api/admin/dev/coverage"))
//                 .andExpect(status().isForbidden());
//     }

//     // =========================
//     // 📊 SUMMARY
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnCoverageSummary() throws Exception {

//         mockMvc.perform(get("/api/admin/dev/coverage/summary"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.coverage").exists());
//     }

//     // =========================
//     // 📁 STATIC FILE
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldServeStaticFile() throws Exception {

//         Path cssFile = coverageDir.resolve("style.css");
//         Files.writeString(cssFile, "body { background: red; }");

//         mockMvc.perform(get("/api/admin/dev/coverage/files/style.css"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string(containsString("background")));
//     }

//     // =========================
//     // 🔥 HTML FILE (rewrite inside sub page)
//     // =========================
//     // @Test
//     // @WithMockUser(roles = "ADMIN")
//     // void shouldRewriteLinksInSubHtml() throws Exception {

//     //     Path subDir = coverageDir.resolve("com.fdjloto.api.service");
//     //     Files.createDirectories(subDir);

//     //     Path subHtml = subDir.resolve("index.html");

//     //     Files.writeString(subHtml, """
//     //         <html>
//     //             <body>
//     //                 <a href="Test.html">Next</a>
//     //             </body>
//     //         </html>
//     //     """);

//     //     mockMvc.perform(get("/api/admin/dev/coverage/files/com.fdjloto.api.service/index.html"))
//     //             .andExpect(status().isOk())
//     //             .andExpect(content().string(containsString(
//     //                     "/api/admin/dev/coverage/files/Test.html"
//     //             )));
//     // }

//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldRewriteLinksAndInjectCssInSubHtml() throws Exception {

//         Path subDir = coverageDir.resolve("com.fdjloto.api.service");
//         Files.createDirectories(subDir);

//         Path subHtml = subDir.resolve("index.html");

//         Files.writeString(subHtml, """
//             <html>
//                 <head></head>
//                 <body>
//                     <a href="Test.html">Next</a>
//                 </body>
//             </html>
//         """);

//         mockMvc.perform(get("/api/admin/dev/coverage/files/com.fdjloto.api.service/index.html"))
//                 .andExpect(status().isOk())

//                 // 🔥 rewrite OK
//                 .andExpect(content().string(containsString(
//                         "/api/admin/dev/coverage/files/Test.html"
//                 )))

//                 // 🔥 CSS injecté aussi
//                 .andExpect(content().string(containsString("background:#0b1220")));
//     }

//     // =========================
//     // 🔥 JACOCO RESOURCES (gif)
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldServeJacocoResourceGif() throws Exception {

//         Path resourcesDir = coverageDir.resolve("jacoco-resources");
//         Files.createDirectories(resourcesDir);

//         Path gif = resourcesDir.resolve("greenbar.gif");
//         Files.write(gif, new byte[]{1, 2, 3});

//         mockMvc.perform(get("/api/admin/dev/coverage/files/greenbar.gif"))
//                 .andExpect(status().isOk());
//     }

//     // =========================
//     // 🔥 GLOBAL SEARCH FALLBACK
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldFindFileAnywhereInJacocoTree() throws Exception {

//         Path deepDir = coverageDir.resolve("com.fdjloto.api.service");
//         Files.createDirectories(deepDir);

//         Path html = deepDir.resolve("Historique20DetailService.html");
//         Files.writeString(html, "<html>DETAIL</html>");

//         mockMvc.perform(get("/api/admin/dev/coverage/files/Historique20DetailService.html"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string(containsString("DETAIL")));
//     }

//     // =========================
//     // 🔐 PATH TRAVERSAL
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldBlockPathTraversal() throws Exception {

//         mockMvc.perform(get("/api/admin/dev/coverage/files/../../../../etc/passwd"))
//                 .andExpect(status().isBadRequest());
//     }

//     // =========================
//     // ⚠️ CORRUPTED FILE
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldHandleCorruptedFile() throws Exception {

//         Files.writeString(coverageFile, "\u0000\u0000\u0000");

//         mockMvc.perform(get("/api/admin/dev/coverage"))
//                 .andExpect(status().isOk());
//     }

//     // =========================
//     // ⚡ PERFORMANCE ENDPOINT
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnPerformanceMetric() throws Exception {

//         mockMvc.perform(get("/api/admin/dev/coverage/performance"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.responseTime").exists());
//     }

//     // =========================
//     // ⚡ PERFORMANCE SPEED
//     // =========================
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldRespondFast() throws Exception {

//         long start = System.currentTimeMillis();

//         mockMvc.perform(get("/api/admin/dev/coverage"))
//                 .andExpect(status().isOk());

//         long duration = System.currentTimeMillis() - start;

//         assertTrue(duration < 200);
//     }

//     // =========================
//     // 🔐 ROLES GLOBAL
//     // =========================
//     @Test
//     void shouldHandleRolesCorrectly() throws Exception {

//         // ADMIN
//         mockMvc.perform(get("/api/admin/dev/coverage")
//                 .with(user("admin").roles("ADMIN")))
//                 .andExpect(status().isOk());

//         // USER
//         mockMvc.perform(get("/api/admin/dev/coverage")
//                 .with(user("user").roles("USER")))
//                 .andExpect(status().isForbidden());

//         // ANONYME
//         mockMvc.perform(get("/api/admin/dev/coverage"))
//                 .andExpect(status().isUnauthorized());
//     }

//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldFixJacocoHeaderStyle() throws Exception {

//         mockMvc.perform(get("/api/admin/dev/coverage"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string(containsString("#header")))
//                 .andExpect(content().string(containsString("#0f172a")));
//     }
// }


package com.fdjloto.api.controller.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.*;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.ResponseEntity;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminDevControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private Path coverageDir;
    private Path coverageFile;

    @BeforeEach
    void setup() throws Exception {

        coverageDir = Paths.get(System.getProperty("user.dir"))
                .resolve("target/site/jacoco")
                .normalize();

        coverageFile = coverageDir.resolve("index.html");

        // 🔥 clean setup
        Files.createDirectories(coverageDir);
        Files.deleteIfExists(coverageFile);

        // 🔥 HTML réaliste avec lien relatif (important pour rewrite)
        Files.writeString(coverageFile, """
            <html>
                <head></head>
                <body>
                    <table>
                        <tr><td>Total</td><td>84%</td></tr>
                    </table>

                    <a href="com.fdjloto.api.service/index.html">Service</a>

                    TEST COVERAGE
                </body>
            </html>
        """);

        assertTrue(Files.exists(coverageFile));
    }

    // =========================
    // ✅ HTML + CSS + REWRITE
    // =========================
    // @Test
    // @WithMockUser(roles = "ADMIN")
    // void shouldReturnCoverageHtmlAndRewriteLinks() throws Exception {

    //     mockMvc.perform(get("/api/admin/dev/coverage"))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentTypeCompatibleWith("text/html"))
    //             .andExpect(content().string(containsString("TEST COVERAGE")))

    //             // 🔥 CSS injecté
    //             .andExpect(content().string(containsString("background:#0b1220")))

    //             // 🔥 lien réécrit
    //             .andExpect(content().string(containsString(
    //                     "/api/admin/dev/coverage/files/com.fdjloto.api.service/index.html"
    //             )));
    // }
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnCoverageHtmlAndRewriteLinks() throws Exception {

        mockMvc.perform(get("/api/admin/dev/coverage"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/html"))

                // contenu OK
                .andExpect(content().string(containsString("TEST COVERAGE")))

                // CSS injecté
                .andExpect(content().string(containsString("background:#0b1220")))

                // ✅ FIX : vérifier base href au lieu du rewrite
                .andExpect(content().string(containsString(
                        "<base href=\"/api/admin/dev/coverage/files/\">"
                )))

                // ✅ lien original conservé (logique navigateur)
                .andExpect(content().string(containsString(
                        "href=\"com.fdjloto.api.service/index.html\""
                )));
    }

    // =========================
    // ❌ 404
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenCoverageFileMissing() throws Exception {

        Files.deleteIfExists(coverageFile);

        mockMvc.perform(get("/api/admin/dev/coverage"))
                .andExpect(status().isNotFound());
    }

    // =========================
    // 🔐 SECURITY
    // =========================
    @Test
    @WithMockUser(roles = "USER")
    void shouldRejectNonAdminUser() throws Exception {

        mockMvc.perform(get("/api/admin/dev/coverage"))
                .andExpect(status().isForbidden());
    }

    // =========================
    // 📊 SUMMARY
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnCoverageSummary() throws Exception {

        mockMvc.perform(get("/api/admin/dev/coverage/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coverage").exists());
    }

    // =========================
    // 📁 STATIC FILE
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldServeStaticFile() throws Exception {

        Path cssFile = coverageDir.resolve("style.css");
        Files.writeString(cssFile, "body { background: red; }");

        mockMvc.perform(get("/api/admin/dev/coverage/files/style.css"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("background")));
    }

    // =========================
    // 🔥 HTML FILE (rewrite inside sub page)
    // =========================
    // @Test
    // @WithMockUser(roles = "ADMIN")
    // void shouldRewriteLinksInSubHtml() throws Exception {

    //     Path subDir = coverageDir.resolve("com.fdjloto.api.service");
    //     Files.createDirectories(subDir);

    //     Path subHtml = subDir.resolve("index.html");

    //     Files.writeString(subHtml, """
    //         <html>
    //             <body>
    //                 <a href="Test.html">Next</a>
    //             </body>
    //         </html>
    //     """);

    //     mockMvc.perform(get("/api/admin/dev/coverage/files/com.fdjloto.api.service/index.html"))
    //             .andExpect(status().isOk())
    //             .andExpect(content().string(containsString(
    //                     "/api/admin/dev/coverage/files/Test.html"
    //             )));
    // }

    // @Test
    // @WithMockUser(roles = "ADMIN")
    // void shouldRewriteLinksAndInjectCssInSubHtml() throws Exception {

    //     Path subDir = coverageDir.resolve("com.fdjloto.api.service");
    //     Files.createDirectories(subDir);

    //     Path subHtml = subDir.resolve("index.html");

    //     Files.writeString(subHtml, """
    //         <html>
    //             <head></head>
    //             <body>
    //                 <a href="Test.html">Next</a>
    //             </body>
    //         </html>
    //     """);

    //     mockMvc.perform(get("/api/admin/dev/coverage/files/com.fdjloto.api.service/index.html"))
    //             .andExpect(status().isOk())

    //             // 🔥 rewrite OK
    //             .andExpect(content().string(containsString(
    //                     "/api/admin/dev/coverage/files/Test.html"
    //             )))

    //             // 🔥 CSS injecté aussi
    //             .andExpect(content().string(containsString("background:#0b1220")));
    // }
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRewriteLinksAndInjectCssInSubHtml() throws Exception {

        Path subDir = coverageDir.resolve("com.fdjloto.api.service");
        Files.createDirectories(subDir);

        Path subHtml = subDir.resolve("index.html");

        Files.writeString(subHtml, """
            <html>
                <head></head>
                <body>
                    <a href="Test.html">Next</a>
                </body>
            </html>
        """);

        mockMvc.perform(get("/api/admin/dev/coverage/files/com.fdjloto.api.service/index.html"))
                .andExpect(status().isOk())

                // ✅ FIX : on ne rewrite plus → on vérifie le lien brut
                .andExpect(content().string(containsString("href=\"Test.html\"")))

                // CSS injecté
                .andExpect(content().string(containsString("background:#0b1220")));
    }

    // =========================
    // 🔥 JACOCO RESOURCES (gif)
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldServeJacocoResourceGif() throws Exception {

        Path resourcesDir = coverageDir.resolve("jacoco-resources");
        Files.createDirectories(resourcesDir);

        Path gif = resourcesDir.resolve("greenbar.gif");
        Files.write(gif, new byte[]{1, 2, 3});

        mockMvc.perform(get("/api/admin/dev/coverage/files/greenbar.gif"))
                .andExpect(status().isOk());
    }

    // =========================
    // 🔥 GLOBAL SEARCH FALLBACK
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindFileAnywhereInJacocoTree() throws Exception {

        Path deepDir = coverageDir.resolve("com.fdjloto.api.service");
        Files.createDirectories(deepDir);

        Path html = deepDir.resolve("Historique20DetailService.html");
        Files.writeString(html, "<html>DETAIL</html>");

        mockMvc.perform(get("/api/admin/dev/coverage/files/Historique20DetailService.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("DETAIL")));
    }

    // =========================
    // 🔐 PATH TRAVERSAL
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldBlockPathTraversal() throws Exception {

        mockMvc.perform(get("/api/admin/dev/coverage/files/../../../../etc/passwd"))
                .andExpect(status().isBadRequest());
    }

    // =========================
    // ⚠️ CORRUPTED FILE
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldHandleCorruptedFile() throws Exception {

        Files.writeString(coverageFile, "\u0000\u0000\u0000");

        mockMvc.perform(get("/api/admin/dev/coverage"))
                .andExpect(status().isOk());
    }

    // =========================
    // ⚡ PERFORMANCE ENDPOINT
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnPerformanceMetric() throws Exception {

        mockMvc.perform(get("/api/admin/dev/coverage/performance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseTime").exists());
    }

    // =========================
    // ⚡ PERFORMANCE SPEED
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRespondFast() throws Exception {

        long start = System.currentTimeMillis();

        mockMvc.perform(get("/api/admin/dev/coverage"))
                .andExpect(status().isOk());

        long duration = System.currentTimeMillis() - start;

        assertTrue(duration < 200);
    }

    // =========================
    // 🔐 ROLES GLOBAL
    // =========================
    @Test
    void shouldHandleRolesCorrectly() throws Exception {

        // ADMIN
        mockMvc.perform(get("/api/admin/dev/coverage")
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        // USER
        mockMvc.perform(get("/api/admin/dev/coverage")
                .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());

        // ANONYME
        mockMvc.perform(get("/api/admin/dev/coverage"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFixJacocoHeaderStyle() throws Exception {

        mockMvc.perform(get("/api/admin/dev/coverage"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("#header")))
                .andExpect(content().string(containsString("#0f172a")));
    }


    // @Test
    // void getCoverage_fileNotFound_returns404() throws Exception {
    //     AdminDevController controller = new AdminDevController();

    //     ResponseEntity<String> response = controller.getCoverage();

    //     assertEquals(404, response.getStatusCode().value());
    // }
    @Test
    void getPerformance_returnsResponseTime() {
        AdminDevController controller = new AdminDevController();

        Map<String, Object> result = controller.getPerformance();

        assertTrue(result.containsKey("responseTime"));
    }
}
