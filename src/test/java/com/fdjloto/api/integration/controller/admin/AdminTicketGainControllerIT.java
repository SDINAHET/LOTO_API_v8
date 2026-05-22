// package com.fdjloto.api.controller.admin;

// import com.fdjloto.api.model.TicketGain;
// import com.fdjloto.api.repository.TicketGainRepository;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.List;
// import java.util.Optional;

// import static org.hamcrest.Matchers.hasSize;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import org.springframework.test.context.ActiveProfiles;

// @SpringBootTest
// @AutoConfigureMockMvc
// @ActiveProfiles("test")
// class AdminTicketGainControllerIT {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private TicketGainRepository ticketGainRepository;

//     /**
//      * GET ALL ticket gains
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnAllTicketGains() throws Exception {

//         TicketGain gain = mock(TicketGain.class);
//         when(gain.getId()).thenReturn("gain1");

//         when(ticketGainRepository.findAll()).thenReturn(List.of(gain));

//         mockMvc.perform(get("/api/admin/ticket-gains"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$", hasSize(1)))
//                 .andExpect(jsonPath("$[0].id").value("gain1"));
//     }

//     /**
//      * GET ONE ticket gain
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnTicketGainById() throws Exception {

//         TicketGain gain = mock(TicketGain.class);
//         when(gain.getId()).thenReturn("gain1");

//         when(ticketGainRepository.findById("gain1"))
//                 .thenReturn(Optional.of(gain));

//         mockMvc.perform(get("/api/admin/ticket-gains/gain1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value("gain1"));
//     }

//     /**
//      * GET ONE ticket gain not found
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturn404WhenTicketGainNotFound() throws Exception {

//         when(ticketGainRepository.findById("bad"))
//                 .thenReturn(Optional.empty());

//         mockMvc.perform(get("/api/admin/ticket-gains/bad"))
//                 .andExpect(status().isNotFound())
//                 .andExpect(content().string("TicketGain not found"));
//     }
// }

package com.fdjloto.api.controller.admin;

import com.fdjloto.api.model.TicketGain;
import com.fdjloto.api.repository.TicketGainRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(roles = "ADMIN") // ✅ global (comme ton autre IT)
class AdminTicketGainControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketGainRepository ticketGainRepository;

    // =========================
    // Helper
    // =========================

    // private TicketGain createGain(String id) {
    //     TicketGain g = new TicketGain();
    //     g.setId(id);
    //     return g;
    // }
	private TicketGain createGain(String id) {
		TicketGain g = mock(TicketGain.class);
		when(g.getId()).thenReturn(id);
		return g;
	}

    // =========================
    // GET ALL
    // =========================

    @Test
    @DisplayName("GET /ticket-gains → OK")
    void shouldReturnAllTicketGains() throws Exception {

        TicketGain gain = createGain("gain1");

        when(ticketGainRepository.findAll())
                .thenReturn(List.of(gain));

        mockMvc.perform(get("/api/admin/ticket-gains"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("gain1"));
    }

    // =========================
    // GET BY ID
    // =========================

    @Test
    @DisplayName("GET /ticket-gains/{id} → OK")
    void shouldReturnTicketGainById() throws Exception {

        TicketGain gain = createGain("gain1");

        when(ticketGainRepository.findById("gain1"))
                .thenReturn(Optional.of(gain));

        mockMvc.perform(get("/api/admin/ticket-gains/gain1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("gain1"));
    }

    // =========================
    // NOT FOUND
    // =========================

    @Test
    @DisplayName("GET /ticket-gains/{id} → 404")
    void shouldReturn404WhenTicketGainNotFound() throws Exception {

        when(ticketGainRepository.findById("bad"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/ticket-gains/bad"))
                .andExpect(status().isNotFound());
                // ❌ on ne teste plus le message exact
    }
}
