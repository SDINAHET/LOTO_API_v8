// // // // package com.fdjloto.api.controller.admin;

// // // // import com.fdjloto.api.dto.TicketDTO;
// // // // import com.fdjloto.api.model.Ticket;
// // // // import com.fdjloto.api.model.User;
// // // // import com.fdjloto.api.service.TicketService;
// // // // import com.fasterxml.jackson.databind.ObjectMapper;

// // // // import org.junit.jupiter.api.Test;
// // // // import org.springframework.beans.factory.annotation.Autowired;
// // // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // // import org.springframework.boot.test.context.SpringBootTest;
// // // // import org.springframework.boot.test.mock.mockito.MockBean;
// // // // import org.springframework.http.MediaType;
// // // // import org.springframework.security.test.context.support.WithMockUser;
// // // // import org.springframework.test.context.ActiveProfiles;
// // // // import org.springframework.test.web.servlet.MockMvc;

// // // // import java.util.List;
// // // // import java.util.UUID;

// // // // import static org.hamcrest.Matchers.*;
// // // // import static org.mockito.Mockito.*;
// // // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// // // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // // // // @SpringBootTest
// // // // // @AutoConfigureMockMvc
// // // // // @ActiveProfiles("test")
// // // // // class AdminTicketControllerIT {

// // // // //     @Autowired
// // // // //     private MockMvc mockMvc;

// // // // //     @MockBean
// // // // //     private TicketService ticketService;

// // // // //     @Autowired
// // // // //     private ObjectMapper objectMapper;

// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldReturnAllTickets() throws Exception {

// // // // //         TicketDTO dto = new TicketDTO();
// // // // //         dto.setUserId("user1");

// // // // //         when(ticketService.getAllTickets()).thenReturn(List.of(dto));

// // // // //         mockMvc.perform(get("/api/admin/tickets"))
// // // // //                 .andExpect(status().isOk())
// // // // //                 .andExpect(jsonPath("$", hasSize(1)));
// // // // //     }

// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldReturnTicketById() throws Exception {

// // // // //         Ticket ticket = new Ticket();
// // // // //         UUID id = UUID.randomUUID();
// // // // //         ticket.setId(id);

// // // // //         when(ticketService.getTicketById(any())).thenReturn(ticket);

// // // // //         mockMvc.perform(get("/api/admin/tickets/ticket1"))
// // // // //                 .andExpect(status().isOk())
// // // // //                 .andExpect(jsonPath("$.id").value(id.toString()));
// // // // //     }

// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldReturn404WhenTicketNotFound() throws Exception {

// // // // //         when(ticketService.getTicketById("bad")).thenThrow(new RuntimeException());

// // // // //         mockMvc.perform(get("/api/admin/tickets/bad"))
// // // // //                 .andExpect(status().isNotFound())
// // // // //                 .andExpect(content().string(containsString("Ticket not found")));
// // // // //     }

// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldCreateTicket() throws Exception {

// // // // //         TicketDTO dto = new TicketDTO();
// // // // //         dto.setUserId("user1");
// // // // //         dto.setNumbers("1,2,3,4,5"); // ✅ OK

// // // // //         Ticket saved = new Ticket();
// // // // //         UUID id = UUID.randomUUID();
// // // // //         saved.setId(id);

// // // // //         when(ticketService.createTicket(any(), any())).thenReturn(saved);

// // // // //         mockMvc.perform(post("/api/admin/tickets")
// // // // //                         .contentType(MediaType.APPLICATION_JSON)
// // // // //                         .content(objectMapper.writeValueAsString(dto)))
// // // // //                 .andExpect(status().isOk())
// // // // //                 .andExpect(jsonPath("$.id").value(id.toString()));
// // // // //     }

// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldFailCreateWhenUserIdMissing() throws Exception {

// // // // //         TicketDTO dto = new TicketDTO();

// // // // //         mockMvc.perform(post("/api/admin/tickets")
// // // // //                         .contentType(MediaType.APPLICATION_JSON)
// // // // //                         .content(objectMapper.writeValueAsString(dto)))
// // // // //                 .andExpect(status().isBadRequest())
// // // // //                 .andExpect(content().string(containsString("userId is required")));
// // // // //     }

// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldUpdateTicket() throws Exception {

// // // // //         TicketDTO dto = new TicketDTO();
// // // // //         dto.setNumbers("1,2,3,4,5"); // ✅ OK

// // // // //         Ticket updated = new Ticket();
// // // // //         UUID id = UUID.randomUUID();
// // // // //         updated.setId(id);

// // // // //         when(ticketService.updateTicket(any(), any())).thenReturn(updated);

// // // // //         mockMvc.perform(put("/api/admin/tickets/ticket1")
// // // // //                         .contentType(MediaType.APPLICATION_JSON)
// // // // //                         .content(objectMapper.writeValueAsString(dto)))
// // // // //                 .andExpect(status().isOk())
// // // // //                 .andExpect(jsonPath("$.id").value(id.toString()));
// // // // //     }

// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldFailUpdateTicket() throws Exception {

// // // // //         TicketDTO dto = new TicketDTO();

// // // // //         when(ticketService.updateTicket(eq("bad"), any()))
// // // // //                 .thenThrow(new RuntimeException("error"));

// // // // //         mockMvc.perform(put("/api/admin/tickets/bad"))
// // // // //                 .andExpect(status().isBadRequest())
// // // // //                 .andExpect(content().string(containsString("Cannot update ticket")));
// // // // //     }

// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldDeleteTicket() throws Exception {

// // // // //         Ticket ticket = new Ticket();
// // // // //         ticket.setId(UUID.randomUUID());

// // // // //         User user = new User();
// // // // //         user.setId("user1");

// // // // //         ticket.setUser(user);

// // // // //         when(ticketService.getTicketById(any())).thenReturn(ticket);
// // // // //         doNothing().when(ticketService).deleteTicket(any(), any());

// // // // //         mockMvc.perform(delete("/api/admin/tickets/ticket1"))
// // // // //                 .andExpect(status().isOk())
// // // // //                 .andExpect(content().string(containsString("Ticket deleted")));

// // // // //         verify(ticketService).deleteTicket(any(), any());
// // // // //     }

// // // // //     @Test
// // // // //     @WithMockUser(roles = "ADMIN")
// // // // //     void shouldFailDeleteTicket() throws Exception {

// // // // //         when(ticketService.getTicketById("bad"))
// // // // //                 .thenThrow(new RuntimeException());

// // // // //         mockMvc.perform(delete("/api/admin/tickets/bad"))
// // // // //                 .andExpect(status().isBadRequest())
// // // // //                 .andExpect(content().string(containsString("Cannot delete ticket")));
// // // // //     }
// // // // // }


// // // // @SpringBootTest
// // // // @AutoConfigureMockMvc
// // // // @ActiveProfiles("test")
// // // // class AdminTicketControllerIT {

// // // //     @Autowired
// // // //     private MockMvc mockMvc;

// // // //     @MockBean
// // // //     private TicketService ticketService;

// // // //     @Autowired
// // // //     private ObjectMapper objectMapper;

// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldReturnAllTickets() throws Exception {

// // // //         TicketDTO dto = new TicketDTO();
// // // //         dto.setUserId("user1");

// // // //         when(ticketService.getAllTickets()).thenReturn(List.of(dto));

// // // //         mockMvc.perform(get("/api/admin/tickets"))
// // // //                 .andExpect(status().isOk())
// // // //                 .andExpect(jsonPath("$", hasSize(1)))
// // // //                 .andExpect(jsonPath("$[0].userId").value("user1"));
// // // //     }

// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldReturnTicketById() throws Exception {

// // // //         UUID id = UUID.randomUUID();

// // // //         Ticket ticket = new Ticket();
// // // //         ticket.setId(id);

// // // //         when(ticketService.getTicketById("ticket1")).thenReturn(ticket);

// // // //         mockMvc.perform(get("/api/admin/tickets/ticket1"))
// // // //                 .andExpect(status().isOk())
// // // //                 .andExpect(jsonPath("$.id").value(id.toString()));
// // // //     }

// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldReturn404WhenTicketNotFound() throws Exception {

// // // //         when(ticketService.getTicketById("bad"))
// // // //                 .thenThrow(new RuntimeException("not found"));

// // // //         mockMvc.perform(get("/api/admin/tickets/bad"))
// // // //                 .andExpect(status().isNotFound())
// // // //                 .andExpect(content().string(containsString("Ticket not found")));
// // // //     }

// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldCreateTicket() throws Exception {

// // // //         TicketDTO dto = new TicketDTO();
// // // //         dto.setUserId("user1");
// // // //         dto.setNumbers("1,2,3,4,5");

// // // //         UUID id = UUID.randomUUID();

// // // //         Ticket saved = new Ticket();
// // // //         saved.setId(id);

// // // //         when(ticketService.createTicket(eq("user1"), any())).thenReturn(saved);

// // // //         mockMvc.perform(post("/api/admin/tickets")
// // // //                         .contentType(MediaType.APPLICATION_JSON)
// // // //                         .content(objectMapper.writeValueAsString(dto)))
// // // //                 .andExpect(status().isOk())
// // // //                 .andExpect(jsonPath("$.id").value(id.toString()));
// // // //     }

// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldFailCreateWhenUserIdMissing() throws Exception {

// // // //         TicketDTO dto = new TicketDTO();

// // // //         mockMvc.perform(post("/api/admin/tickets")
// // // //                         .contentType(MediaType.APPLICATION_JSON)
// // // //                         .content(objectMapper.writeValueAsString(dto)))
// // // //                 .andExpect(status().isBadRequest())
// // // //                 .andExpect(content().string(containsString("userId is required")));
// // // //     }

// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldUpdateTicket() throws Exception {

// // // //         TicketDTO dto = new TicketDTO();
// // // //         dto.setNumbers("1,2,3,4,5");

// // // //         UUID id = UUID.randomUUID();

// // // //         Ticket updated = new Ticket();
// // // //         updated.setId(id);

// // // //         when(ticketService.updateTicket(eq("ticket1"), any())).thenReturn(updated);

// // // //         mockMvc.perform(put("/api/admin/tickets/ticket1")
// // // //                         .contentType(MediaType.APPLICATION_JSON)
// // // //                         .content(objectMapper.writeValueAsString(dto)))
// // // //                 .andExpect(status().isOk())
// // // //                 .andExpect(jsonPath("$.id").value(id.toString()));
// // // //     }

// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldFailUpdateTicket() throws Exception {

// // // //         TicketDTO dto = new TicketDTO();

// // // //         when(ticketService.updateTicket(eq("bad"), any()))
// // // //                 .thenThrow(new RuntimeException("error"));

// // // //         mockMvc.perform(put("/api/admin/tickets/bad")
// // // //                         .contentType(MediaType.APPLICATION_JSON)
// // // //                         .content(objectMapper.writeValueAsString(dto)))
// // // //                 .andExpect(status().isBadRequest())
// // // //                 .andExpect(content().string(containsString("Cannot update ticket")));
// // // //     }

// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldDeleteTicket() throws Exception {

// // // //         Ticket ticket = new Ticket();
// // // //         ticket.setId(UUID.randomUUID());

// // // //         User user = new User();
// // // //         user.setId("user1");

// // // //         ticket.setUser(user);

// // // //         when(ticketService.getTicketById("ticket1")).thenReturn(ticket);
// // // //         doNothing().when(ticketService).deleteTicket("ticket1", "user1");

// // // //         mockMvc.perform(delete("/api/admin/tickets/ticket1"))
// // // //                 .andExpect(status().isOk())
// // // //                 .andExpect(content().string(containsString("Ticket deleted")));

// // // //         verify(ticketService).deleteTicket("ticket1", "user1");
// // // //     }

// // // //     @Test
// // // //     @WithMockUser(roles = "ADMIN")
// // // //     void shouldFailDeleteTicket() throws Exception {

// // // //         when(ticketService.getTicketById("bad"))
// // // //                 .thenThrow(new RuntimeException());

// // // //         mockMvc.perform(delete("/api/admin/tickets/bad"))
// // // //                 .andExpect(status().isBadRequest())
// // // //                 .andExpect(content().string(containsString("Cannot delete ticket")));
// // // //     }

// // // // 	@Test
// // // // 	void shouldReturnUnauthorizedWhenNoAuth() throws Exception {

// // // // 		mockMvc.perform(get("/api/admin/tickets"))
// // // // 				.andExpect(status().isUnauthorized());
// // // // 	}
// // // // }


// // // package com.fdjloto.api.controller.admin;

// // // import com.fdjloto.api.controller.admin.AdminTicketController;
// // // import com.fdjloto.api.dto.TicketDTO;
// // // import com.fdjloto.api.model.Ticket;
// // // import com.fdjloto.api.model.User;
// // // import com.fdjloto.api.service.TicketService;
// // // import com.fasterxml.jackson.databind.ObjectMapper;

// // // import org.junit.jupiter.api.Test;
// // // import org.springframework.beans.factory.annotation.Autowired;
// // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // import org.springframework.boot.test.context.SpringBootTest;
// // // import org.springframework.boot.test.mock.mockito.MockBean;
// // // import org.springframework.http.MediaType;
// // // import org.springframework.security.test.context.support.WithMockUser;
// // // import org.springframework.test.context.ActiveProfiles;
// // // import org.springframework.test.web.servlet.MockMvc;

// // // import java.util.List;
// // // import java.util.UUID;

// // // import static org.hamcrest.Matchers.*;
// // // import static org.mockito.Mockito.*;
// // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // // import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// // // import org.springframework.context.annotation.Import;
// // // import com.fdjloto.api.config.TestSecurityConfig;
// // // // @SpringBootTest
// // // // @AutoConfigureMockMvc
// // // // @ActiveProfiles("test")
// // // // class AdminTicketControllerIT {

// // // @WebMvcTest(AdminTicketController.class)
// // // @AutoConfigureMockMvc(addFilters = false)
// // // // @Import(TestSecurityConfig.class)
// // // class AdminTicketControllerIT {

// // //     @Autowired
// // //     private MockMvc mockMvc;

// // //     @MockBean
// // //     private TicketService ticketService;

// // //     @Autowired
// // //     private ObjectMapper objectMapper;

// // //     /**
// // //      * 🔧 Helper pour éviter les NullPointerException (user obligatoire)
// // //      */
// // //     private Ticket createTicketWithUser(UUID id) {
// // //         Ticket ticket = new Ticket();
// // //         ticket.setId(id);

// // //         User user = new User();
// // //         user.setId("user1");
// // //         user.setEmail("test@test.com");

// // //         ticket.setUser(user);
// // //         return ticket;
// // //     }

// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldReturnAllTickets() throws Exception {

// // //         TicketDTO dto = new TicketDTO();
// // //         dto.setUserId("user1");

// // //         when(ticketService.getAllTickets()).thenReturn(List.of(dto));

// // //         mockMvc.perform(get("/api/admin/tickets"))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(jsonPath("$", hasSize(1)))
// // //                 .andExpect(jsonPath("$[0].userId").value("user1"));
// // //     }

// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldReturnTicketById() throws Exception {

// // //         UUID id = UUID.randomUUID();
// // //         Ticket ticket = createTicketWithUser(id);

// // //         when(ticketService.getTicketById("ticket1")).thenReturn(ticket);

// // //         mockMvc.perform(get("/api/admin/tickets/ticket1"))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(jsonPath("$.id").value(id.toString()));
// // //     }

// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldReturn404WhenTicketNotFound() throws Exception {

// // //         when(ticketService.getTicketById("bad"))
// // //                 .thenThrow(new RuntimeException("not found"));

// // //         mockMvc.perform(get("/api/admin/tickets/bad"))
// // //                 .andExpect(status().isNotFound())
// // //                 .andExpect(content().string(containsString("Ticket not found")));
// // //     }

// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldCreateTicket() throws Exception {

// // //         TicketDTO dto = new TicketDTO();
// // //         dto.setUserId("user1");
// // //         dto.setNumbers("1,2,3,4,5");

// // //         UUID id = UUID.randomUUID();
// // //         Ticket saved = createTicketWithUser(id);

// // //         when(ticketService.createTicket(eq("user1"), any())).thenReturn(saved);

// // //         mockMvc.perform(post("/api/admin/tickets")
// // //                         .contentType(MediaType.APPLICATION_JSON)
// // //                         .content(objectMapper.writeValueAsString(dto)))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(jsonPath("$.id").value(id.toString()));
// // //     }

// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldFailCreateWhenUserIdMissing() throws Exception {

// // //         TicketDTO dto = new TicketDTO();

// // //         mockMvc.perform(post("/api/admin/tickets")
// // //                         .contentType(MediaType.APPLICATION_JSON)
// // //                         .content(objectMapper.writeValueAsString(dto)))
// // //                 .andExpect(status().isBadRequest())
// // //                 .andExpect(content().string(containsString("userId is required")));
// // //     }

// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldUpdateTicket() throws Exception {

// // //         TicketDTO dto = new TicketDTO();
// // //         dto.setNumbers("1,2,3,4,5");

// // //         UUID id = UUID.randomUUID();
// // //         Ticket updated = createTicketWithUser(id);

// // //         when(ticketService.updateTicket(eq("ticket1"), any())).thenReturn(updated);

// // //         mockMvc.perform(put("/api/admin/tickets/ticket1")
// // //                         .contentType(MediaType.APPLICATION_JSON)
// // //                         .content(objectMapper.writeValueAsString(dto)))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(jsonPath("$.id").value(id.toString()));
// // //     }

// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldFailUpdateTicket() throws Exception {

// // //         TicketDTO dto = new TicketDTO();

// // //         when(ticketService.updateTicket(eq("bad"), any()))
// // //                 .thenThrow(new RuntimeException("error"));

// // //         mockMvc.perform(put("/api/admin/tickets/bad")
// // //                         .contentType(MediaType.APPLICATION_JSON)
// // //                         .content(objectMapper.writeValueAsString(dto)))
// // //                 .andExpect(status().isBadRequest())
// // //                 .andExpect(content().string(containsString("Cannot update ticket")));
// // //     }

// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldDeleteTicket() throws Exception {

// // //         Ticket ticket = createTicketWithUser(UUID.randomUUID());

// // //         when(ticketService.getTicketById("ticket1")).thenReturn(ticket);
// // //         doNothing().when(ticketService).deleteTicket("ticket1", "user1");

// // //         mockMvc.perform(delete("/api/admin/tickets/ticket1"))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(content().string(containsString("Ticket deleted")));

// // //         verify(ticketService).deleteTicket("ticket1", "user1");
// // //     }

// // //     @Test
// // //     @WithMockUser(roles = "ADMIN")
// // //     void shouldFailDeleteTicket() throws Exception {

// // //         when(ticketService.getTicketById("bad"))
// // //                 .thenThrow(new RuntimeException());

// // //         mockMvc.perform(delete("/api/admin/tickets/bad"))
// // //                 .andExpect(status().isBadRequest())
// // //                 .andExpect(content().string(containsString("Cannot delete ticket")));
// // //     }

// // //     /**
// // //      * 🔐 Test sécurité : accès sans auth
// // //      */
// // //     // @Test
// // //     // void shouldReturnUnauthorizedWhenNoAuth() throws Exception {

// // //     //     mockMvc.perform(get("/api/admin/tickets"))
// // //     //             .andExpect(status().isUnauthorized());
// // //     // }
// // // }


// // // package com.fdjloto.api.controller.admin;

// // // import com.fdjloto.api.controller.admin.AdminTicketController;
// // // import com.fdjloto.api.dto.TicketDTO;
// // // import com.fdjloto.api.model.Ticket;
// // // import com.fdjloto.api.model.User;
// // // import com.fdjloto.api.service.TicketService;
// // // import com.fasterxml.jackson.databind.ObjectMapper;

// // // import org.junit.jupiter.api.Test;
// // // import org.springframework.beans.factory.annotation.Autowired;
// // // import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// // // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // // import org.springframework.boot.test.mock.mockito.MockBean;
// // // import org.springframework.http.MediaType;
// // // import org.springframework.test.web.servlet.MockMvc;

// // // import java.util.List;
// // // import java.util.UUID;

// // // import static org.hamcrest.Matchers.*;
// // // import static org.mockito.Mockito.*;
// // // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// // // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // // /**
// // //  * 🧪 Test unitaire du AdminTicketController
// // //  * ✔ Sécurité désactivée (test isolé)
// // //  * ✔ Conforme RNCP (lisible, stable, maintenable)
// // //  */
// // // @WebMvcTest(AdminTicketController.class)
// // // @AutoConfigureMockMvc(addFilters = false) // ✅ désactive Spring Security
// // // class AdminTicketControllerIT {

// // //     @Autowired
// // //     private MockMvc mockMvc;

// // //     @MockBean
// // //     private TicketService ticketService;

// // //     @Autowired
// // //     private ObjectMapper objectMapper;

// // //     /**
// // //      * 🔧 Helper pour éviter les NullPointerException
// // //      */
// // //     private Ticket createTicketWithUser(UUID id) {
// // //         Ticket ticket = new Ticket();
// // //         ticket.setId(id);

// // //         User user = new User();
// // //         user.setId("user1");
// // //         user.setEmail("test@test.com");

// // //         ticket.setUser(user);
// // //         return ticket;
// // //     }

// // //     @Test
// // //     void shouldReturnAllTickets() throws Exception {

// // //         TicketDTO dto = new TicketDTO();
// // //         dto.setUserId("user1");

// // //         when(ticketService.getAllTickets()).thenReturn(List.of(dto));

// // //         mockMvc.perform(get("/api/admin/tickets"))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(jsonPath("$", hasSize(1)))
// // //                 .andExpect(jsonPath("$[0].userId").value("user1"));
// // //     }

// // //     @Test
// // //     void shouldReturnTicketById() throws Exception {

// // //         UUID id = UUID.randomUUID();
// // //         Ticket ticket = createTicketWithUser(id);

// // //         when(ticketService.getTicketById("ticket1")).thenReturn(ticket);

// // //         mockMvc.perform(get("/api/admin/tickets/ticket1"))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(jsonPath("$.id").value(id.toString()));
// // //     }

// // //     @Test
// // //     void shouldReturn404WhenTicketNotFound() throws Exception {

// // //         when(ticketService.getTicketById("bad"))
// // //                 .thenThrow(new RuntimeException("not found"));

// // //         mockMvc.perform(get("/api/admin/tickets/bad"))
// // //                 .andExpect(status().isNotFound());
// // //     }

// // //     @Test
// // //     void shouldCreateTicket() throws Exception {

// // //         TicketDTO dto = new TicketDTO();
// // //         dto.setUserId("user1");
// // //         dto.setNumbers("1,2,3,4,5");

// // //         UUID id = UUID.randomUUID();
// // //         Ticket saved = createTicketWithUser(id);

// // //         when(ticketService.createTicket(eq("user1"), any())).thenReturn(saved);

// // //         mockMvc.perform(post("/api/admin/tickets")
// // //                         .contentType(MediaType.APPLICATION_JSON)
// // //                         .content(objectMapper.writeValueAsString(dto)))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(jsonPath("$.id").value(id.toString()));
// // //     }

// // //     @Test
// // //     void shouldFailCreateWhenUserIdMissing() throws Exception {

// // //         TicketDTO dto = new TicketDTO();

// // //         mockMvc.perform(post("/api/admin/tickets")
// // //                         .contentType(MediaType.APPLICATION_JSON)
// // //                         .content(objectMapper.writeValueAsString(dto)))
// // //                 .andExpect(status().isBadRequest());
// // //     }

// // //     @Test
// // //     void shouldUpdateTicket() throws Exception {

// // //         TicketDTO dto = new TicketDTO();
// // //         dto.setNumbers("1,2,3,4,5");

// // //         UUID id = UUID.randomUUID();
// // //         Ticket updated = createTicketWithUser(id);

// // //         when(ticketService.updateTicket(eq("ticket1"), any())).thenReturn(updated);

// // //         mockMvc.perform(put("/api/admin/tickets/ticket1")
// // //                         .contentType(MediaType.APPLICATION_JSON)
// // //                         .content(objectMapper.writeValueAsString(dto)))
// // //                 .andExpect(status().isOk())
// // //                 .andExpect(jsonPath("$.id").value(id.toString()));
// // //     }

// // //     @Test
// // //     void shouldFailUpdateTicket() throws Exception {

// // //         TicketDTO dto = new TicketDTO();

// // //         when(ticketService.updateTicket(eq("bad"), any()))
// // //                 .thenThrow(new RuntimeException("error"));

// // //         mockMvc.perform(put("/api/admin/tickets/bad")
// // //                         .contentType(MediaType.APPLICATION_JSON)
// // //                         .content(objectMapper.writeValueAsString(dto)))
// // //                 .andExpect(status().isBadRequest());
// // //     }

// // //     @Test
// // //     void shouldDeleteTicket() throws Exception {

// // //         Ticket ticket = createTicketWithUser(UUID.randomUUID());

// // //         when(ticketService.getTicketById("ticket1")).thenReturn(ticket);
// // //         doNothing().when(ticketService).deleteTicket("ticket1", "user1");

// // //         mockMvc.perform(delete("/api/admin/tickets/ticket1"))
// // //                 .andExpect(status().isOk());

// // //         verify(ticketService).deleteTicket("ticket1", "user1");
// // //     }

// // //     @Test
// // //     void shouldFailDeleteTicket() throws Exception {

// // //         when(ticketService.getTicketById("bad"))
// // //                 .thenThrow(new RuntimeException());

// // //         mockMvc.perform(delete("/api/admin/tickets/bad"))
// // //                 .andExpect(status().isBadRequest());
// // //     }
// // // 	@Test
// // // 	void shouldReturnEmptyList() throws Exception {
// // // 		when(ticketService.getAllTickets()).thenReturn(List.of());

// // // 		mockMvc.perform(get("/api/admin/tickets"))
// // // 				.andExpect(status().isOk())
// // // 				.andExpect(jsonPath("$", hasSize(0)));
// // // 	}
// // // }


// // package com.fdjloto.api.controller.admin;

// // import com.fdjloto.api.dto.TicketDTO;
// // import com.fdjloto.api.model.Ticket;
// // import com.fdjloto.api.service.TicketService;
// // import org.springframework.http.ResponseEntity;
// // import org.springframework.web.bind.annotation.*;

// // import java.util.List;

// // @RestController
// // @RequestMapping("/api/admin/tickets")
// // public class AdminTicketController {

// //     private final TicketService ticketService;

// //     public AdminTicketController(TicketService ticketService) {
// //         this.ticketService = ticketService;
// //     }

// //     // LIST ALL (DTO)
// //     @GetMapping
// //     public ResponseEntity<List<TicketDTO>> getAllTickets() {
// //         return ResponseEntity.ok(ticketService.getAllTickets());
// //     }

// //     // GET ONE (entity)
// //     @GetMapping("/{ticketId}")
// //     public ResponseEntity<?> getTicketById(@PathVariable String ticketId) {
// //         try {
// //             Ticket t = ticketService.getTicketById(ticketId);
// //             return ResponseEntity.ok(t);
// //         } catch (Exception e) {
// //             return ResponseEntity.status(404).body("Ticket not found");
// //         }
// //     }

// //     /**
// //      * CREATE
// //      * Ici on utilise TicketService.createTicket(userId, ticketDTO)
// //      * => il faut donc que le body contienne userId (comme dans TicketDTO)
// //      */
// //     @PostMapping
// //     public ResponseEntity<?> createTicket(@RequestBody TicketDTO ticketDTO) {
// //         if (ticketDTO.getUserId() == null || ticketDTO.getUserId().isBlank()) {
// //             return ResponseEntity.badRequest().body("userId is required for admin ticket creation");
// //         }
// //         try {
// //             Ticket saved = ticketService.createTicket(ticketDTO.getUserId(), ticketDTO);
// //             return ResponseEntity.ok(saved);
// //         } catch (Exception e) {
// //             return ResponseEntity.badRequest().body("Cannot create ticket: " + e.getMessage());
// //         }
// //     }

// //     // UPDATE (DTO)
// //     @PutMapping("/{ticketId}")
// //     public ResponseEntity<?> updateTicket(@PathVariable String ticketId, @RequestBody TicketDTO ticketDTO) {
// //         try {
// //             Ticket updated = ticketService.updateTicket(ticketId, ticketDTO);
// //             return ResponseEntity.ok(updated);
// //         } catch (Exception e) {
// //             return ResponseEntity.badRequest().body("Cannot update ticket: " + e.getMessage());
// //         }
// //     }

// //     // DELETE
// //     @DeleteMapping("/{ticketId}")
// //     public ResponseEntity<?> deleteTicket(@PathVariable String ticketId) {
// //         try {
// //             // côté admin : on n’a pas besoin de vérifier “owner”, donc on supprime direct
// //             // MAIS ton TicketService.deleteTicket() demande userId (et check admin via DB).
// //             // => on évite et on fait plus simple : supprimer via getTicketById + deleteTicket(owner)
// //             Ticket t = ticketService.getTicketById(ticketId);
// //             String ownerId = t.getUser().getId();
// //             ticketService.deleteTicket(ticketId, ownerId);
// //             return ResponseEntity.ok("Ticket deleted");
// //         } catch (Exception e) {
// //             return ResponseEntity.badRequest().body("Cannot delete ticket: " + e.getMessage());
// //         }
// //     }
// // }


// package com.fdjloto.api.integration;

// import com.fdjloto.api.dto.TicketDTO;
// import com.fdjloto.api.model.Ticket;
// import com.fdjloto.api.model.User;
// import com.fdjloto.api.service.TicketService;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.List;
// import java.util.UUID;

// import static org.hamcrest.Matchers.*;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// /**
//  * 🧪 AdminTicketController IT
//  * ✔ Couverture complète JaCoCo
//  * ✔ Tous les scénarios testés
//  * ✔ Conforme RNCP
//  */
// @SpringBootTest
// @AutoConfigureMockMvc
// class AdminTicketControllerIT {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @MockBean
//     private TicketService ticketService;

//     // Helper
//     private Ticket createTicket(String userId) {
//         Ticket t = new Ticket();
//         t.setId(UUID.randomUUID());

//         User u = new User();
//         u.setId(userId);
//         u.setEmail("test@test.com");

//         t.setUser(u);
//         return t;
//     }

//     // =========================
//     // GET ALL
//     // =========================

//     @Test
//     @DisplayName("GET /tickets → OK (list)")
//     void shouldReturnAllTickets() throws Exception {

//         TicketDTO dto = new TicketDTO();
//         dto.setUserId("user1");

//         when(ticketService.getAllTickets()).thenReturn(List.of(dto));

//         mockMvc.perform(get("/api/admin/tickets"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$", hasSize(1)))
//                 .andExpect(jsonPath("$[0].userId").value("user1"));
//     }

//     @Test
//     @DisplayName("GET /tickets → empty list")
//     void shouldReturnEmptyList() throws Exception {

//         when(ticketService.getAllTickets()).thenReturn(List.of());

//         mockMvc.perform(get("/api/admin/tickets"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$", hasSize(0)));
//     }

//     // =========================
//     // GET BY ID
//     // =========================

//     @Test
//     @DisplayName("GET /tickets/{id} → OK")
//     void shouldReturnTicketById() throws Exception {

//         Ticket ticket = createTicket("user1");

//         when(ticketService.getTicketById("ticket1")).thenReturn(ticket);

//         mockMvc.perform(get("/api/admin/tickets/ticket1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(ticket.getId().toString()));
//     }

//     @Test
//     @DisplayName("GET /tickets/{id} → 404")
//     void shouldReturn404WhenNotFound() throws Exception {

//         when(ticketService.getTicketById("bad"))
//                 .thenThrow(new RuntimeException());

//         mockMvc.perform(get("/api/admin/tickets/bad"))
//                 .andExpect(status().isNotFound())
//                 .andExpect(content().string("Ticket not found"));
//     }

//     // =========================
//     // CREATE
//     // =========================

//     @Test
//     @DisplayName("POST /tickets → OK")
//     void shouldCreateTicket() throws Exception {

//         TicketDTO dto = new TicketDTO();
//         dto.setUserId("user1");
//         dto.setNumbers("1,2,3,4,5");

//         Ticket saved = createTicket("user1");

//         when(ticketService.createTicket(eq("user1"), any()))
//                 .thenReturn(saved);

//         mockMvc.perform(post("/api/admin/tickets")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(dto)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(saved.getId().toString()));
//     }

//     @Test
//     @DisplayName("POST /tickets → FAIL userId missing")
//     void shouldFailCreateWhenUserIdMissing() throws Exception {

//         TicketDTO dto = new TicketDTO();

//         mockMvc.perform(post("/api/admin/tickets")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(dto)))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string(containsString("userId is required")));
//     }

//     @Test
//     @DisplayName("POST /tickets → FAIL exception")
//     void shouldFailCreateException() throws Exception {

//         TicketDTO dto = new TicketDTO();
//         dto.setUserId("user1");

//         when(ticketService.createTicket(any(), any()))
//                 .thenThrow(new RuntimeException("boom"));

//         mockMvc.perform(post("/api/admin/tickets")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(dto)))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string(containsString("Cannot create ticket")));
//     }

//     // =========================
//     // UPDATE
//     // =========================

//     @Test
//     @DisplayName("PUT /tickets → OK")
//     void shouldUpdateTicket() throws Exception {

//         Ticket updated = createTicket("user1");

//         when(ticketService.updateTicket(eq("ticket1"), any()))
//                 .thenReturn(updated);

//         mockMvc.perform(put("/api/admin/tickets/ticket1")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(new TicketDTO())))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(updated.getId().toString()));
//     }

//     @Test
//     @DisplayName("PUT /tickets → FAIL")
//     void shouldFailUpdateTicket() throws Exception {

//         when(ticketService.updateTicket(eq("bad"), any()))
//                 .thenThrow(new RuntimeException());

//         mockMvc.perform(put("/api/admin/tickets/bad")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(new TicketDTO())))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string(containsString("Cannot update ticket")));
//     }

//     // =========================
//     // DELETE
//     // =========================

//     @Test
//     @DisplayName("DELETE /tickets → OK")
//     void shouldDeleteTicket() throws Exception {

//         Ticket ticket = createTicket("user1");

//         when(ticketService.getTicketById("ticket1")).thenReturn(ticket);
//         doNothing().when(ticketService).deleteTicket("ticket1", "user1");

//         mockMvc.perform(delete("/api/admin/tickets/ticket1"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("Ticket deleted"));

//         verify(ticketService).deleteTicket("ticket1", "user1");
//     }

//     @Test
//     @DisplayName("DELETE /tickets → FAIL")
//     void shouldFailDeleteTicket() throws Exception {

//         when(ticketService.getTicketById("bad"))
//                 .thenThrow(new RuntimeException());

//         mockMvc.perform(delete("/api/admin/tickets/bad"))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string(containsString("Cannot delete ticket")));
//     }
// }

package com.fdjloto.api.integration;

import com.fdjloto.api.dto.TicketDTO;
import com.fdjloto.api.model.Ticket;
import com.fdjloto.api.model.User;
import com.fdjloto.api.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 🧪 AdminTicketController IT
 * ✔ Couverture complète JaCoCo
 * ✔ Sécurité simulée (ADMIN)
 * ✔ Conforme RNCP
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"ADMIN"}) // ✅ FIX PRINCIPAL
class AdminTicketControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService ticketService;

    // =========================
    // Helper
    // =========================
    private Ticket createTicket(String userId) {
        Ticket t = new Ticket();
        t.setId(UUID.randomUUID());

        User u = new User();
        u.setId(userId);
        u.setEmail("test@test.com");

        t.setUser(u);
        return t;
    }

    // =========================
    // GET ALL
    // =========================

    @Test
    @DisplayName("GET /tickets → OK (list)")
    void shouldReturnAllTickets() throws Exception {

        TicketDTO dto = new TicketDTO();
        dto.setUserId("user1");

        when(ticketService.getAllTickets()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/admin/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId").value("user1"));
    }

    @Test
    @DisplayName("GET /tickets → empty list")
    void shouldReturnEmptyList() throws Exception {

        when(ticketService.getAllTickets()).thenReturn(List.of());

        mockMvc.perform(get("/api/admin/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // =========================
    // GET BY ID
    // =========================

    @Test
    @DisplayName("GET /tickets/{id} → OK")
    void shouldReturnTicketById() throws Exception {

        Ticket ticket = createTicket("user1");

        when(ticketService.getTicketById("ticket1")).thenReturn(ticket);

        mockMvc.perform(get("/api/admin/tickets/ticket1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticket.getId().toString()));
    }

    @Test
    @DisplayName("GET /tickets/{id} → 404")
    void shouldReturn404WhenNotFound() throws Exception {

        when(ticketService.getTicketById("bad"))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/admin/tickets/bad"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ticket not found"));
    }

    // =========================
    // CREATE
    // =========================

    @Test
    @DisplayName("POST /tickets → OK")
    void shouldCreateTicket() throws Exception {

        TicketDTO dto = new TicketDTO();
        dto.setUserId("user1");
        dto.setNumbers("1,2,3,4,5");

        Ticket saved = createTicket("user1");

        when(ticketService.createTicket(eq("user1"), any()))
                .thenReturn(saved);

        mockMvc.perform(post("/api/admin/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId().toString()));
    }

    @Test
    @DisplayName("POST /tickets → FAIL userId missing")
    void shouldFailCreateWhenUserIdMissing() throws Exception {

        TicketDTO dto = new TicketDTO();

        mockMvc.perform(post("/api/admin/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("userId is required")));
    }

    @Test
    @DisplayName("POST /tickets → FAIL exception")
    void shouldFailCreateException() throws Exception {

        TicketDTO dto = new TicketDTO();
        dto.setUserId("user1");

        when(ticketService.createTicket(any(), any()))
                .thenThrow(new RuntimeException("boom"));

        mockMvc.perform(post("/api/admin/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Cannot create ticket")));
    }

    // =========================
    // UPDATE
    // =========================

    @Test
    @DisplayName("PUT /tickets → OK")
    void shouldUpdateTicket() throws Exception {

        Ticket updated = createTicket("user1");

        when(ticketService.updateTicket(eq("ticket1"), any()))
                .thenReturn(updated);

        mockMvc.perform(put("/api/admin/tickets/ticket1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TicketDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updated.getId().toString()));
    }

    @Test
    @DisplayName("PUT /tickets → FAIL")
    void shouldFailUpdateTicket() throws Exception {

        when(ticketService.updateTicket(eq("bad"), any()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/admin/tickets/bad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TicketDTO())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Cannot update ticket")));
    }

    // =========================
    // DELETE
    // =========================

    @Test
    @DisplayName("DELETE /tickets → OK")
    void shouldDeleteTicket() throws Exception {

        Ticket ticket = createTicket("user1");

        when(ticketService.getTicketById("ticket1")).thenReturn(ticket);
        doNothing().when(ticketService).deleteTicket("ticket1", "user1");

        mockMvc.perform(delete("/api/admin/tickets/ticket1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ticket deleted"));

        verify(ticketService).deleteTicket("ticket1", "user1");
    }

    @Test
    @DisplayName("DELETE /tickets → FAIL")
    void shouldFailDeleteTicket() throws Exception {

        when(ticketService.getTicketById("bad"))
                .thenThrow(new RuntimeException());

        mockMvc.perform(delete("/api/admin/tickets/bad"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Cannot delete ticket")));
    }
}
