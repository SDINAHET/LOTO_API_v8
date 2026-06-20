
// package com.fdjloto.api.dto;

// import com.fdjloto.api.TestDataFactory;
// import com.fdjloto.api.model.*;
// import org.junit.jupiter.api.*;

// import java.time.*;

// import static org.junit.jupiter.api.Assertions.*;

// class TicketDTOTest {
// @Test void ctor_fromTicket_populatesFields() {
//                 User u = TestDataFactory.user("u1", false);
//                 Ticket t = TestDataFactory.ticket("t1", u);
//                 TicketDTO dto = new TicketDTO(t);
//                 assertEquals("t1", dto.getId());
//                 assertEquals("u1", dto.getUserId());
//                 assertEquals("1-2-3-4-5", dto.getNumbers());
//                 assertEquals("7", dto.getChanceNumber());
//                 assertNotNull(dto.getDrawDate());
//                 assertNotNull(dto.getCreatedAt());
//                 assertNotNull(dto.getUpdatedAt());
//             }

// @Test void ctor_fromTicket_handlesNullDates() {
//                 User u = TestDataFactory.user("u1", false);
//                 Ticket t = new Ticket();
//                 t.setId("t1");
//                 t.setUser(u);
//                 t.setNumbers("1-2-3-4-5");
//                 t.setChanceNumber(1);
//                 t.setDrawDate(null);
//                 t.setCreatedAt(null);
//                 t.setUpdatedAt(null);
//                 TicketDTO dto = new TicketDTO(t);
//                 assertNull(dto.getDrawDate());
//                 assertEquals("N/A", dto.getCreatedAt());
//                 assertNull(dto.getUpdatedAt());
//             }

// @Test void settersAndGetters_work() {
//                 TicketDTO dto = new TicketDTO();
//                 dto.setId("id");
//                 dto.setUserId("u1");
//                 dto.setNumbers("a");
//                 dto.setChanceNumber("2");
//                 dto.setDrawDate("2025-01-01");
//                 dto.setCreatedAt("c");
//                 dto.setUpdatedAt("u");
//                 assertEquals("id", dto.getId());
//                 assertEquals("u1", dto.getUserId());
//                 assertEquals("a", dto.getNumbers());
//                 assertEquals("2", dto.getChanceNumber());
//                 assertEquals("2025-01-01", dto.getDrawDate());
//                 assertEquals("c", dto.getCreatedAt());
//                 assertEquals("u", dto.getUpdatedAt());
//             }

// @Test void smoke_04() {
//             TicketDTO obj = new TicketDTO();
//             assertNotNull(obj);
//         }

// @Test void smoke_05() {
//             TicketDTO obj = new TicketDTO();
//             assertNotNull(obj);
//         }

// @Test void smoke_06() {
//             TicketDTO obj = new TicketDTO();
//             assertNotNull(obj);
//         }

// @Test void smoke_07() {
//             TicketDTO obj = new TicketDTO();
//             assertNotNull(obj);
//         }

// @Test void smoke_08() {
//             TicketDTO obj = new TicketDTO();
//             assertNotNull(obj);
//         }

// @Test void smoke_09() {
//             TicketDTO obj = new TicketDTO();
//             assertNotNull(obj);
//         }

// @Test void smoke_10() {
//             TicketDTO obj = new TicketDTO();
//             assertNotNull(obj);
//         }

// @Test void smoke_11() {
//             TicketDTO obj = new TicketDTO();
//             assertNotNull(obj);
//         }

// @Test void smoke_12() {
//             TicketDTO obj = new TicketDTO();
//             assertNotNull(obj);
//         }

// @Test void smoke_13() {
//             TicketDTO obj = new TicketDTO();
//             assertNotNull(obj);
//         }

// @Test void smoke_14() {
//             TicketDTO obj = new TicketDTO();
//             assertNotNull(obj);
//         }

// @Test void smoke_15() {
//             TicketDTO obj = new TicketDTO();
//             assertNotNull(obj);
//         }
// }



package com.fdjloto.api.unit.dto;

import com.fdjloto.api.dto.TicketDTO;
import com.fdjloto.api.model.Ticket;
import com.fdjloto.api.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TicketDTOTest {

    @Test
    void shouldCreateDtoFromTicket() {
        User user = new User();
        user.setId("user-1");

        Ticket ticket = new Ticket();
        ticket.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        ticket.setUser(user);
        ticket.setNumbers("1-2-3-4-5");
        ticket.setChanceNumber(6);
        ticket.setDrawDate(LocalDate.of(2025, 3, 12));
        ticket.setCreatedAt(LocalDateTime.of(2025, 3, 1, 10, 30));
        ticket.setUpdatedAt(LocalDateTime.of(2025, 3, 2, 11, 45));

        TicketDTO dto = new TicketDTO(ticket);

        assertEquals("11111111-1111-1111-1111-111111111111", dto.getId());
        assertEquals("user-1", dto.getUserId());
        assertEquals("1-2-3-4-5", dto.getNumbers());
        assertEquals("6", dto.getChanceNumber());
        assertEquals("2025-03-12", dto.getDrawDate());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
    }

    @Test
    void shouldKeepUpdatedAtNullWhenTicketUpdatedAtIsNull() {
        User user = new User();
        user.setId("user-1");

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setNumbers("1-2-3-4-5");
        ticket.setChanceNumber(6);
        ticket.setDrawDate(LocalDate.of(2025, 3, 12));
        ticket.setCreatedAt(LocalDateTime.of(2025, 3, 1, 10, 30));
        ticket.setUpdatedAt((LocalDateTime) null);

        TicketDTO dto = new TicketDTO(ticket);

        assertNull(dto.getUpdatedAt());
    }
}
