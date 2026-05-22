// // // // // // package com.fdjloto.api.controller.admin;

// // // // // // import org.junit.jupiter.api.Test;
// // // // // // import org.springframework.beans.factory.annotation.Autowired;
// // // // // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // // // // import org.springframework.boot.test.context.SpringBootTest;
// // // // // // import org.springframework.jdbc.core.JdbcTemplate;
// // // // // // import org.springframework.security.test.context.support.WithMockUser;
// // // // // // import org.springframework.test.context.ActiveProfiles;
// // // // // // import org.springframework.test.web.servlet.MockMvc;

// // // // // // import static org.hamcrest.Matchers.*;
// // // // // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // // // // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // // // // // @SpringBootTest
// // // // // // @ActiveProfiles("test")
// // // // // // @AutoConfigureMockMvc
// // // // // // class AdminUserStatsControllerDBTest {

// // // // // //     @Autowired
// // // // // //     private MockMvc mockMvc;

// // // // // //     @Autowired
// // // // // //     private JdbcTemplate jdbc;

// // // // // //     @Test
// // // // // //     @WithMockUser(roles = "ADMIN")
// // // // // //     void shouldReturnRealUserStatsFromDatabase() throws Exception {

// // // // // //         // nettoyage
// // // // // //         jdbc.execute("DELETE FROM ticket_gains");
// // // // // //         jdbc.execute("DELETE FROM tickets");
// // // // // //         jdbc.execute("DELETE FROM users");

// // // // // //         // user
// // // // // //         jdbc.update("""
// // // // // //             INSERT INTO users (id, email, first_name, last_name, is_admin, password)
// // // // // //             VALUES ('user1', 'user@test.com', 'John', 'Doe', false, 'testpassword')
// // // // // //         """);

// // // // // //         // ticket
// // // // // //         jdbc.update("""
// // // // // //             INSERT INTO tickets (
// // // // // //                 id,
// // // // // //                 user_id,
// // // // // //                 created_at,
// // // // // //                 updated_at,
// // // // // //                 numbers,
// // // // // //                 lucky_number,
// // // // // //                 draw_date
// // // // // //             )
// // // // // //             VALUES (
// // // // // //                 't1',
// // // // // //                 'user1',
// // // // // //                 NOW(),
// // // // // //                 NOW(),
// // // // // //                 '1-2-3-4-5',
// // // // // //                 7,
// // // // // //                 '2026-03-23'
// // // // // //             )
// // // // // //         """);

// // // // // //         // gain ✅ CORRIGÉ
// // // // // //         jdbc.update("""
// // // // // //             INSERT INTO ticket_gains (
// // // // // //                 id,
// // // // // //                 ticket_id,
// // // // // //                 matching_numbers,
// // // // // //                 lucky_number_match,
// // // // // //                 gain_amount
// // // // // //             )
// // // // // //             VALUES (
// // // // // //                 'g1',
// // // // // //                 't1',
// // // // // //                 3,
// // // // // //                 true,
// // // // // //                 100.50
// // // // // //             )
// // // // // //         """);

// // // // // //         mockMvc.perform(get("/api/admin/users-stats"))
// // // // // //                 .andExpect(status().isOk())
// // // // // //                 .andExpect(jsonPath("$", hasSize(1)))
// // // // // //                 .andExpect(jsonPath("$[0].id").value("user1"))
// // // // // //                 .andExpect(jsonPath("$[0].ticketsCount").value(1))
// // // // // //                 .andExpect(jsonPath("$[0].totalGain").value(100.50));
// // // // // //     }
// // // // // // }

// // // // // package com.fdjloto.api.controller.admin;

// // // // // import org.junit.jupiter.api.Test;
// // // // // import org.springframework.beans.factory.annotation.Autowired;
// // // // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // // // import org.springframework.boot.test.context.SpringBootTest;
// // // // // import org.springframework.jdbc.core.JdbcTemplate;
// // // // // import org.springframework.security.test.context.support.WithMockUser;
// // // // // import org.springframework.test.context.ActiveProfiles;
// // // // // import org.springframework.test.context.DynamicPropertyRegistry;
// // // // // import org.springframework.test.context.DynamicPropertySource;
// // // // // import org.springframework.test.web.servlet.MockMvc;

// // // // // import org.testcontainers.containers.PostgreSQLContainer;
// // // // // import org.testcontainers.junit.jupiter.Container;
// // // // // import org.testcontainers.junit.jupiter.Testcontainers;

// // // // // import static org.hamcrest.Matchers.*;
// // // // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // // // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // // // // @Testcontainers
// // // // // @SpringBootTest
// // // // // @ActiveProfiles("test")
// // // // // @AutoConfigureMockMvc
// // // // // class AdminUserStatsControllerDBTest {

// // // // //     // 🔥 PostgreSQL embarqué (auto Docker)
// // // // //     @Container
// // // // //     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
// // // // //             .withDatabaseName("testdb")
// // // // //             .withUsername("test")
// // // // //             .withPassword("test");

// // // // //     // 🔥 injection dynamique dans Spring
// // // // //     @DynamicPropertySource
// // // // //     static void configureProperties(DynamicPropertyRegistry registry) {
// // // // //         registry.add("spring.datasource.url", postgres::getJdbcUrl);
// // // // //         registry.add("spring.datasource.username", postgres::getUsername);
// // // // //         registry.add("spring.datasource.password", postgres::getPassword);
// // // // //         registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
// // // // //     }

// // // // //     @Autowired
// // // // //     private MockMvc mockMvc;

// // // // //     @Autowired
// // // // //     private JdbcTemplate jdbc;

// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldReturnRealUserStatsFromDatabase() throws Exception {

// // // // //         // nettoyage
// // // // //         jdbc.execute("DELETE FROM ticket_gains");
// // // // //         jdbc.execute("DELETE FROM tickets");
// // // // //         jdbc.execute("DELETE FROM users");

// // // // //         // user
// // // // //         jdbc.update("""
// // // // //             INSERT INTO users (id, email, first_name, last_name, is_admin, password)
// // // // //             VALUES ('user1', 'user@test.com', 'John', 'Doe', false, 'testpassword')
// // // // //         """);

// // // // //         // ticket
// // // // //         jdbc.update("""
// // // // //             INSERT INTO tickets (
// // // // //                 id,
// // // // //                 user_id,
// // // // //                 created_at,
// // // // //                 updated_at,
// // // // //                 numbers,
// // // // //                 lucky_number,
// // // // //                 draw_date
// // // // //             )
// // // // //             VALUES (
// // // // //                 't1',
// // // // //                 'user1',
// // // // //                 NOW(),
// // // // //                 NOW(),
// // // // //                 '1-2-3-4-5',
// // // // //                 7,
// // // // //                 '2026-03-23'
// // // // //             )
// // // // //         """);

// // // // //         // gain
// // // // //         jdbc.update("""
// // // // //             INSERT INTO ticket_gains (
// // // // //                 id,
// // // // //                 ticket_id,
// // // // //                 matching_numbers,
// // // // //                 lucky_number_match,
// // // // //                 gain_amount
// // // // //             )
// // // // //             VALUES (
// // // // //                 'g1',
// // // // //                 't1',
// // // // //                 3,
// // // // //                 true,
// // // // //                 100.50
// // // // //             )
// // // // //         """);

// // // // //         mockMvc.perform(get("/api/admin/users-stats"))
// // // // //                 .andExpect(status().isOk())
// // // // //                 .andExpect(jsonPath("$", hasSize(1)))
// // // // //                 .andExpect(jsonPath("$[0].id").value("user1"))
// // // // //                 .andExpect(jsonPath("$[0].ticketsCount").value(1))
// // // // //                 .andExpect(jsonPath("$[0].totalGain").value(100.50));
// // // // //     }
// // // // // }



// // // // package com.fdjloto.api.controller.admin;

// // // // import org.junit.jupiter.api.Test;
// // // // import org.springframework.beans.factory.annotation.Autowired;
// // // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // // import org.springframework.boot.test.context.SpringBootTest;
// // // // import org.springframework.jdbc.core.JdbcTemplate;
// // // // import org.springframework.security.test.context.support.WithMockUser;
// // // // import org.springframework.test.context.ActiveProfiles;
// // // // import org.springframework.test.context.DynamicPropertyRegistry;
// // // // import org.springframework.test.context.DynamicPropertySource;
// // // // import org.springframework.test.context.jdbc.Sql;
// // // // import org.springframework.test.web.servlet.MockMvc;

// // // // import org.testcontainers.containers.PostgreSQLContainer;
// // // // import org.testcontainers.junit.jupiter.Container;
// // // // import org.testcontainers.junit.jupiter.Testcontainers;

// // // // import static org.hamcrest.Matchers.*;
// // // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // // // // @Testcontainers
// // // // // @SpringBootTest
// // // // // @ActiveProfiles("test")
// // // // // @AutoConfigureMockMvc
// // // // // @Sql("/schema.sql") // 🔥 FORCE le chargement du schéma
// // // // // class AdminUserStatsControllerDBTest {


// // // // @Testcontainers
// // // // @SpringBootTest(
// // // //     properties = {
// // // //         "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration",
// // // //         "spring.data.mongodb.repositories.enabled=false"
// // // //     }
// // // // )
// // // // @ActiveProfiles("test")
// // // // @AutoConfigureMockMvc
// // // // @Sql("/schema.sql")
// // // // class AdminUserStatsControllerDBTest {

// // // //     @Container
// // // //     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
// // // //             .withDatabaseName("testdb")
// // // //             .withUsername("test")
// // // //             .withPassword("test");

// // // //     @DynamicPropertySource
// // // //     static void configureProperties(DynamicPropertyRegistry registry) {
// // // //         registry.add("spring.datasource.url", postgres::getJdbcUrl);
// // // //         registry.add("spring.datasource.username", postgres::getUsername);
// // // //         registry.add("spring.datasource.password", postgres::getPassword);
// // // //         registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
// // // //     }

// // // //     @Autowired
// // // //     private MockMvc mockMvc;

// // // //     @Autowired
// // // //     private JdbcTemplate jdbc;

// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldReturnRealUserStatsFromDatabase() throws Exception {

// // // //         // nettoyage (FK safe)
// // // //         jdbc.execute("DELETE FROM ticket_gains");
// // // //         jdbc.execute("DELETE FROM tickets");
// // // //         jdbc.execute("DELETE FROM users");

// // // //         // user
// // // //         jdbc.update("""
// // // //             INSERT INTO users (id, email, first_name, last_name, is_admin, password)
// // // //             VALUES ('user1', 'user@test.com', 'John', 'Doe', false, 'testpassword')
// // // //         """);

// // // //         // ticket
// // // //         jdbc.update("""
// // // //             INSERT INTO tickets (
// // // //                 id,
// // // //                 user_id,
// // // //                 created_at,
// // // //                 updated_at,
// // // //                 numbers,
// // // //                 lucky_number,
// // // //                 draw_date
// // // //             )
// // // //             VALUES (
// // // //                 't1',
// // // //                 'user1',
// // // //                 NOW(),
// // // //                 NOW(),
// // // //                 '1-2-3-4-5',
// // // //                 7,
// // // //                 '2026-03-23'
// // // //             )
// // // //         """);

// // // //         // gain
// // // //         jdbc.update("""
// // // //             INSERT INTO ticket_gains (
// // // //                 id,
// // // //                 ticket_id,
// // // //                 matching_numbers,
// // // //                 lucky_number_match,
// // // //                 gain_amount
// // // //             )
// // // //             VALUES (
// // // //                 'g1',
// // // //                 't1',
// // // //                 3,
// // // //                 true,
// // // //                 100.50
// // // //             )
// // // //         """);

// // // //         mockMvc.perform(get("/api/admin/users-stats"))
// // // //                 .andExpect(status().isOk())
// // // //                 .andExpect(jsonPath("$", hasSize(1)))
// // // //                 .andExpect(jsonPath("$[0].id").value("user1"))
// // // //                 .andExpect(jsonPath("$[0].ticketsCount").value(1))
// // // //                 .andExpect(jsonPath("$[0].totalGain").value(100.50));
// // // //     }
// // // // }


// // // package com.fdjloto.api.controller.admin;

// // // import org.junit.jupiter.api.Test;
// // // import org.springframework.beans.factory.annotation.Autowired;
// // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // import org.springframework.boot.test.context.SpringBootTest;
// // // import org.springframework.boot.test.mock.mockito.MockBean;
// // // import org.springframework.jdbc.core.JdbcTemplate;
// // // import org.springframework.security.test.context.support.WithMockUser;
// // // import org.springframework.test.context.ActiveProfiles;
// // // import org.springframework.test.context.DynamicPropertyRegistry;
// // // import org.springframework.test.context.DynamicPropertySource;
// // // import org.springframework.test.context.jdbc.Sql;
// // // import org.springframework.test.web.servlet.MockMvc;

// // // import org.testcontainers.containers.PostgreSQLContainer;
// // // import org.testcontainers.junit.jupiter.Container;
// // // import org.testcontainers.junit.jupiter.Testcontainers;

// // // import static org.hamcrest.Matchers.*;
// // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // // @Testcontainers
// // // @SpringBootTest(
// // //     properties = {
// // //         "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration",
// // //         "spring.data.mongodb.repositories.enabled=false"
// // //     },
// // //     classes = {
// // //         com.fdjloto.api.LotoApiApplication.class,
// // //         AdminUserStatsControllerDBTest.TestConfig.class
// // //     }
// // // )
// // // @ActiveProfiles("test")
// // // @AutoConfigureMockMvc

// // // // 🔥 charge ton schema.sql depuis src/test/resources
// // // @Sql(scripts = "/schema.sql")
// // // class AdminUserStatsControllerDBTest {

// // //     // 🔥 PostgreSQL Testcontainers
// // //     @Container
// // //     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
// // //             .withDatabaseName("testdb")
// // //             .withUsername("test")
// // //             .withPassword("test");

// // //     // 🔥 injection Spring dynamique
// // //     @DynamicPropertySource
// // //     static void configureProperties(DynamicPropertyRegistry registry) {
// // //         registry.add("spring.datasource.url", postgres::getJdbcUrl);
// // //         registry.add("spring.datasource.username", postgres::getUsername);
// // //         registry.add("spring.datasource.password", postgres::getPassword);
// // //         registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);

// // //         // optionnel mais safe
// // //         registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
// // //     }

// // //     @Autowired
// // //     private MockMvc mockMvc;

// // //     @Autowired
// // //     private JdbcTemplate jdbc;

// // //     // 🔥 MOCK obligatoire pour éviter crash Mongo
// // //     @MockBean
// // //     private com.fdjloto.api.repository.Historique20Repository historique20Repository;

// // //     @MockBean
// // //     private com.fdjloto.api.service.Historique20Service historique20Service;

// // //     @MockBean
// // //     private com.fdjloto.api.repository.Historique20DetailRepository historique20DetailRepository;

// // //     @MockBean
// // //     private com.fdjloto.api.service.Historique20DetailService historique20DetailService;

// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldReturnRealUserStatsFromDatabase() throws Exception {

// // //         // nettoyage FK safe
// // //         jdbc.execute("DELETE FROM ticket_gains");
// // //         jdbc.execute("DELETE FROM tickets");
// // //         jdbc.execute("DELETE FROM users");

// // //         // insert user
// // //         jdbc.update("""
// // //             INSERT INTO users (id, email, first_name, last_name, is_admin, password)
// // //             VALUES ('user1', 'user@test.com', 'John', 'Doe', false, 'testpassword')
// // //         """);

// // //         // insert ticket
// // //         jdbc.update("""
// // //             INSERT INTO tickets (
// // //                 id,
// // //                 user_id,
// // //                 created_at,
// // //                 updated_at,
// // //                 numbers,
// // //                 lucky_number,
// // //                 draw_date
// // //             )
// // //             VALUES (
// // //                 't1',
// // //                 'user1',
// // //                 NOW(),
// // //                 NOW(),
// // //                 '1-2-3-4-5',
// // //                 7,
// // //                 '2026-03-23'
// // //             )
// // //         """);

// // //         // insert gain
// // //         jdbc.update("""
// // //             INSERT INTO ticket_gains (
// // //                 id,
// // //                 ticket_id,
// // //                 matching_numbers,
// // //                 lucky_number_match,
// // //                 gain_amount
// // //             )
// // //             VALUES (
// // //                 'g1',
// // //                 't1',
// // //                 3,
// // //                 true,
// // //                 100.50
// // //             )
// // //         """);

// // //         // test API
// // //         mockMvc.perform(get("/api/admin/users-stats"))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(jsonPath("$", hasSize(1)))
// // //                 .andExpect(jsonPath("$[0].id").value("user1"))
// // //                 .andExpect(jsonPath("$[0].ticketsCount").value(1))
// // //                 .andExpect(jsonPath("$[0].totalGain").value(100.50));
// // //     }
// // // }


// // package com.fdjloto.api.controller.admin;

// // import org.junit.jupiter.api.Test;
// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // import org.springframework.boot.test.context.SpringBootTest;
// // import org.springframework.boot.test.mock.mockito.MockBean;
// // import org.springframework.jdbc.core.JdbcTemplate;
// // import org.springframework.security.test.context.support.WithMockUser;
// // import org.springframework.test.context.ActiveProfiles;
// // import org.springframework.test.context.DynamicPropertyRegistry;
// // import org.springframework.test.context.DynamicPropertySource;
// // import org.springframework.test.context.jdbc.Sql;
// // import org.springframework.test.web.servlet.MockMvc;

// // import org.testcontainers.containers.PostgreSQLContainer;
// // import org.testcontainers.junit.jupiter.Container;
// // import org.testcontainers.junit.jupiter.Testcontainers;

// // import org.springframework.data.mongodb.core.MongoTemplate;

// // import static org.hamcrest.Matchers.*;
// // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // @Testcontainers
// // @SpringBootTest(
// //     properties = {
// //         "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration",
// //         "spring.data.mongodb.repositories.enabled=false"
// //     }
// // )
// // @ActiveProfiles("test")
// // @AutoConfigureMockMvc
// // @Sql("/schema.sql")
// // class AdminUserStatsControllerDBTest {

// //     @Container
// //     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
// //             .withDatabaseName("testdb")
// //             .withUsername("test")
// //             .withPassword("test");

// //     @DynamicPropertySource
// //     static void configureProperties(DynamicPropertyRegistry registry) {
// //         registry.add("spring.datasource.url", postgres::getJdbcUrl);
// //         registry.add("spring.datasource.username", postgres::getUsername);
// //         registry.add("spring.datasource.password", postgres::getPassword);
// //         registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
// //         registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
// //     }

// //     @Autowired
// //     private MockMvc mockMvc;

// //     @Autowired
// //     private JdbcTemplate jdbc;

// //     // 🔥 FIX CRITIQUE → empêche crash Mongo
// //     @MockBean
// //     private MongoTemplate mongoTemplate;

// //     // (optionnel mais safe si dépendances indirectes)
// //     @MockBean
// //     private com.fdjloto.api.repository.Historique20Repository historique20Repository;

// //     @MockBean
// //     private com.fdjloto.api.repository.Historique20DetailRepository historique20DetailRepository;

// //     @Test
// //     @WithMockUser(roles = "ADMIN")
// //     void shouldReturnRealUserStatsFromDatabase() throws Exception {

// //         jdbc.execute("DELETE FROM ticket_gains");
// //         jdbc.execute("DELETE FROM tickets");
// //         jdbc.execute("DELETE FROM users");

// //         jdbc.update("""
// //             INSERT INTO users (id, email, first_name, last_name, is_admin, password)
// //             VALUES ('user1', 'user@test.com', 'John', 'Doe', false, 'testpassword')
// //         """);

// //         jdbc.update("""
// //             INSERT INTO tickets (
// //                 id,
// //                 user_id,
// //                 created_at,
// //                 updated_at,
// //                 numbers,
// //                 lucky_number,
// //                 draw_date
// //             )
// //             VALUES (
// //                 't1',
// //                 'user1',
// //                 NOW(),
// //                 NOW(),
// //                 '1-2-3-4-5',
// //                 7,
// //                 '2026-03-23'
// //             )
// //         """);

// //         jdbc.update("""
// //             INSERT INTO ticket_gains (
// //                 id,
// //                 ticket_id,
// //                 matching_numbers,
// //                 lucky_number_match,
// //                 gain_amount
// //             )
// //             VALUES (
// //                 'g1',
// //                 't1',
// //                 3,
// //                 true,
// //                 100.50
// //             )
// //         """);

// //         mockMvc.perform(get("/api/admin/users-stats"))
// //                 .andExpect(status().isOk())
// //                 .andExpect(jsonPath("$", hasSize(1)))
// //                 .andExpect(jsonPath("$[0].id").value("user1"))
// //                 .andExpect(jsonPath("$[0].ticketsCount").value(1))
// //                 .andExpect(jsonPath("$[0].totalGain").value(100.50));
// //     }
// // }


// package com.fdjloto.api.controller.admin;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.DynamicPropertyRegistry;
// import org.springframework.test.context.DynamicPropertySource;
// import org.springframework.test.context.jdbc.Sql;
// import org.springframework.test.web.servlet.MockMvc;

// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;

// import com.fdjloto.api.repository.LotoRepository;
// import com.fdjloto.api.repository.TirageRepository;

// import static org.hamcrest.Matchers.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import org.springframework.boot.test.mock.mockito.MockBean;

// @Testcontainers
// @SpringBootTest(
//     properties = {
//         "spring.task.scheduling.enabled=false",
//         // 🔥 désactive complètement Mongo + repositories
//         "spring.autoconfigure.exclude=" +
//         "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration," +
//         "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration," +
//         "org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration",

//         "spring.data.mongodb.repositories.enabled=false"
//     }
// )
// @ActiveProfiles("test")
// @AutoConfigureMockMvc
// // @Sql("/schema.sql")
// // @Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
// @Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
// class AdminUserStatsControllerDBTest {

//     // 🔥 PostgreSQL Testcontainers
//     @Container
//     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
//             .withDatabaseName("testdb")
//             .withUsername("test")
//             .withPassword("test");

//     // 🔥 injection dynamique dans Spring
//     @DynamicPropertySource
//     static void configureProperties(DynamicPropertyRegistry registry) {
//         registry.add("spring.datasource.url", postgres::getJdbcUrl);
//         registry.add("spring.datasource.username", postgres::getUsername);
//         registry.add("spring.datasource.password", postgres::getPassword);
//         registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
//         registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
//     }


//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private JdbcTemplate jdbc;


//     @MockBean
//     private TirageRepository tirageRepository;
//     @MockBean
//     private com.fdjloto.api.service.OgService ogService;

//     // 🔥 FIX Mongo manquant (OBLIGATOIRE)
//     @MockBean
//     private com.fdjloto.api.repository.Historique20Repository historique20Repository;

//     @MockBean
//     private com.fdjloto.api.repository.Historique20DetailRepository historique20DetailRepository;

//     @MockBean
//     private LotoRepository lotoRepository;

//     @MockBean
//     private com.fdjloto.api.repository.PredictionRepository predictionRepository;

//     @MockBean
//     private com.fdjloto.api.service.PredictionService predictionService;

//     @MockBean
//     private com.fdjloto.api.service.PredictionTirageService predictionTirageService;

//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnRealUserStatsFromDatabase() throws Exception {

//         // nettoyage FK-safe
//         jdbc.execute("DELETE FROM ticket_gains");
//         jdbc.execute("DELETE FROM tickets");
//         jdbc.execute("DELETE FROM users");

//         // insert user
//         jdbc.update("""
//             INSERT INTO users (id, email, first_name, last_name, is_admin, password)
//             VALUES ('user1', 'user@test.com', 'John', 'Doe', false, 'testpassword')
//         """);

//         // insert ticket
//         jdbc.update("""
//             INSERT INTO tickets (
//                 id,
//                 user_id,
//                 created_at,
//                 updated_at,
//                 numbers,
//                 lucky_number,
//                 draw_date
//             )
//             VALUES (
//                 't1',
//                 'user1',
//                 NOW(),
//                 NOW(),
//                 '1-2-3-4-5',
//                 7,
//                 '2026-03-23'
//             )
//         """);

//         // insert gain
//         jdbc.update("""
//             INSERT INTO ticket_gains (
//                 id,
//                 ticket_id,
//                 matching_numbers,
//                 lucky_number_match,
//                 gain_amount
//             )
//             VALUES (
//                 'g1',
//                 't1',
//                 3,
//                 true,
//                 100.50
//             )
//         """);

//         // test API
//         mockMvc.perform(get("/api/admin/users-stats"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$", hasSize(1)))
//                 .andExpect(jsonPath("$[0].id").value("user1"))
//                 .andExpect(jsonPath("$[0].ticketsCount").value(1))
//                 .andExpect(jsonPath("$[0].totalGain").value(100.50));
//     }
// }

package com.fdjloto.api.controller.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(
    properties = {
        "spring.task.scheduling.enabled=false",

        // 🔥 désactivation Mongo COMPLET
        "spring.autoconfigure.exclude=" +
        "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration," +
        "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration," +
        "org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration",

        "spring.data.mongodb.repositories.enabled=false",

        // fallback docker-compose
        "spring.datasource.url=jdbc:postgresql://postgres_test:5432/testdb",
        "spring.datasource.username=test",
        "spring.datasource.password=test",

        "spring.jpa.hibernate.ddl-auto=none"
    }
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AdminUserStatsControllerDBTest {

    // 🔥 Testcontainers pour LOCAL uniquement
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        try {
            postgres.start();

            registry.add("spring.datasource.url", postgres::getJdbcUrl);
            registry.add("spring.datasource.username", postgres::getUsername);
            registry.add("spring.datasource.password", postgres::getPassword);
            registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);

            System.out.println("✅ Testcontainers actif");

        } catch (Exception e) {

            // 🐳 fallback Docker Compose
            registry.add("spring.datasource.url",
                    () -> "jdbc:postgresql://postgres_test:5432/testdb");
            registry.add("spring.datasource.username", () -> "test");
            registry.add("spring.datasource.password", () -> "test");

            System.out.println("🐳 Docker Compose utilisé");
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbc;

    // 🔥 mocks obligatoires pour éviter crash Spring context
    @MockBean private com.fdjloto.api.repository.Historique20Repository historique20Repository;
    @MockBean private com.fdjloto.api.repository.Historique20DetailRepository historique20DetailRepository;
    @MockBean private com.fdjloto.api.repository.PredictionRepository predictionRepository;
    @MockBean private com.fdjloto.api.service.PredictionService predictionService;
    @MockBean private com.fdjloto.api.service.PredictionTirageService predictionTirageService;
    @MockBean private com.fdjloto.api.repository.LotoRepository lotoRepository;
    @MockBean private com.fdjloto.api.repository.TirageRepository tirageRepository;
    @MockBean private com.fdjloto.api.service.OgService ogService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnRealUserStatsFromDatabase() throws Exception {

        // 🔥 clean FK-safe
        jdbc.execute("DELETE FROM ticket_gains");
        jdbc.execute("DELETE FROM tickets");
        jdbc.execute("DELETE FROM users");

        // 👤 user
        jdbc.update("""
            INSERT INTO users (id, email, first_name, last_name, is_admin, password)
            VALUES ('user1', 'user@test.com', 'John', 'Doe', false, 'testpassword')
        """);

        // 🎟️ ticket
        jdbc.update("""
            INSERT INTO tickets (
                id, user_id, created_at, updated_at,
                numbers, lucky_number, draw_date
            )
            VALUES (
                't1', 'user1', NOW(), NOW(),
                '1-2-3-4-5', 7, '2026-03-23'
            )
        """);

        // 💰 gain
        jdbc.update("""
            INSERT INTO ticket_gains (
                id, ticket_id, matching_numbers,
                lucky_number_match, gain_amount
            )
            VALUES (
                'g1', 't1', 3, true, 100.50
            )
        """);

        // ✅ TEST API
        mockMvc.perform(get("/api/admin/users-stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("user1"))
                .andExpect(jsonPath("$[0].ticketsCount").value(1))
                .andExpect(jsonPath("$[0].totalGain").value(100.50));
    }
}
