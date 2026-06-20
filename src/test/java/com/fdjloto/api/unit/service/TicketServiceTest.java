
// package com.fdjloto.api.service;

// import com.fdjloto.api.TestDataFactory;
// import com.fdjloto.api.dto.TicketDTO;
// import com.fdjloto.api.exception.TicketNotFoundException;
// import com.fdjloto.api.model.Ticket;
// import com.fdjloto.api.model.User;
// import com.fdjloto.api.repository.TicketRepository;
// import com.fdjloto.api.repository.UserRepository;
// import org.junit.jupiter.api.*;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.*;

// import java.time.LocalDate;
// import java.util.*;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// @ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
// class TicketServiceTest {

//     @Mock TicketRepository ticketRepository;
//     @Mock UserRepository userRepository;

//     @InjectMocks TicketService ticketService;

//     @Test void createTicket_userNotFound_throws() {
//         when(userRepository.findById("u1")).thenReturn(Optional.empty());
//         TicketDTO dto = TestDataFactory.ticketDto("1-2-3-4-5", "7", "2025-01-01");
//         assertThrows(RuntimeException.class, () -> ticketService.createTicket("u1", dto));
//         verify(ticketRepository, never()).save(any());
//     }

//     @Test void createTicket_savesTicket_withDrawDateAndDrawDay() {
//         User u = TestDataFactory.user("u1", false);
//         when(userRepository.findById("u1")).thenReturn(Optional.of(u));
//         when(ticketRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

//         TicketDTO dto = TestDataFactory.ticketDto("1-2-3-4-5", "7", "2025-01-01");
//         Ticket saved = ticketService.createTicket("u1", dto);

//         assertEquals(u, saved.getUser());
//         assertEquals("1-2-3-4-5", saved.getNumbers());
//         assertEquals(7, saved.getChanceNumber());
//         assertEquals(LocalDate.of(2025,1,1), saved.getDrawDate());
//         assertNotNull(saved.getDrawDay());
//         assertNotNull(saved.getCreatedAt());
//         assertNotNull(saved.getUpdatedAt());
//         verify(ticketRepository).save(any(Ticket.class));
//     }

//     @Test void updateTicket_dtoInvalidChance_throwsIllegalArgumentException() {
//         Ticket existing = TestDataFactory.ticket("t1", TestDataFactory.user("u1", false));
//         when(ticketRepository.findById("t1")).thenReturn(Optional.of(existing));
//         TicketDTO dto = TestDataFactory.ticketDto("1-2-3-4-5", "abc", "2025-01-01");
//         assertThrows(IllegalArgumentException.class, () -> ticketService.updateTicket("t1", dto));
//         verify(ticketRepository, never()).save(any());
//     }

//     @Test void updateTicket_changesDrawDate_updatesDrawDay() {
//         Ticket existing = TestDataFactory.ticket("t1", TestDataFactory.user("u1", false));
//         existing.setDrawDate(LocalDate.of(2025,1,1));
//         existing.setDrawDay("mercredi");
//         when(ticketRepository.findById("t1")).thenReturn(Optional.of(existing));
//         when(ticketRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

//         TicketDTO dto = TestDataFactory.ticketDto("9-8-7-6-5", "1", "2025-01-04");
//         Ticket updated = ticketService.updateTicket("t1", dto);

//         assertEquals("9-8-7-6-5", updated.getNumbers());
//         assertEquals(1, updated.getChanceNumber());
//         assertEquals(LocalDate.of(2025,1,4), updated.getDrawDate());
//         assertNotNull(updated.getDrawDay());
//         verify(ticketRepository).save(existing);
//     }

//     @Test void updateTicket_sameDrawDate_keepsDrawDay() {
//         Ticket existing = TestDataFactory.ticket("t1", TestDataFactory.user("u1", false));
//         existing.setDrawDate(LocalDate.of(2025,1,1));
//         existing.setDrawDay("mercredi");
//         when(ticketRepository.findById("t1")).thenReturn(Optional.of(existing));
//         when(ticketRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

//         TicketDTO dto = TestDataFactory.ticketDto("1-1-1-1-1", "2", "2025-01-01");
//         Ticket updated = ticketService.updateTicket("t1", dto);

//         assertEquals("mercredi", updated.getDrawDay());
//         verify(ticketRepository).save(existing);
//     }

//     @Test void getTicketById_notFound_throwsTicketNotFoundException() {
//         when(ticketRepository.findById("t1")).thenReturn(Optional.empty());
//         assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById("t1"));
//     }

//     @Test void getAllTickets_mapsToDTO() {
//         User u = TestDataFactory.user("u1", false);
//         when(ticketRepository.findAll()).thenReturn(List.of(
//             TestDataFactory.ticket("t1", u),
//             TestDataFactory.ticket("t2", u)
//         ));
//         assertEquals(2, ticketService.getAllTickets().size());
//     }

//     @Test void deleteTicket_owner_canDelete() {
//         User u = TestDataFactory.user("u1", false);
//         Ticket t = TestDataFactory.ticket("t1", u);
//         when(ticketRepository.findById("t1")).thenReturn(Optional.of(t));
//         ticketService.deleteTicket("t1", "u1");
//         verify(ticketRepository).deleteById("t1");
//     }

//     @Test void deleteTicket_admin_canDelete() {
//         User owner = TestDataFactory.user("u1", false);
//         Ticket t = TestDataFactory.ticket("t1", owner);
//         when(ticketRepository.findById("t1")).thenReturn(Optional.of(t));
//         when(userRepository.findById("admin")).thenReturn(Optional.of(TestDataFactory.user("admin", true)));
//         ticketService.deleteTicket("t1", "admin");
//         verify(ticketRepository).deleteById("t1");
//     }

//     @Test void deleteTicket_nonOwnerNonAdmin_throws() {
//         User owner = TestDataFactory.user("u1", false);
//         Ticket t = TestDataFactory.ticket("t1", owner);
//         when(ticketRepository.findById("t1")).thenReturn(Optional.of(t));
//         when(userRepository.findById("u2")).thenReturn(Optional.of(TestDataFactory.user("u2", false)));
//         assertThrows(RuntimeException.class, () -> ticketService.deleteTicket("t1", "u2"));
//         verify(ticketRepository, never()).deleteById(anyString());
//     }

//     @Test void getDrawDay_formatsInFrenchUpper() {
//         String day = ticketService.getDrawDay("2025-01-01");
//         assertNotNull(day);
//         assertEquals(day, day.toUpperCase());
//     }

//     // Padding tests for count & basic coverage
//     @Test void getTicketsByEmail_delegatesToRepo() {
//         when(ticketRepository.findByUserEmail("a@b.com")).thenReturn(Collections.emptyList());
//         assertNotNull(ticketService.getTicketsByEmail("a@b.com"));
//     }

//     @Test void getTicketsByUserId_mapsToDTO() {
//         when(ticketRepository.findByUserId("u1")).thenReturn(Collections.emptyList());
//         assertEquals(0, ticketService.getTicketsByUserId("u1").size());
//     }
// }




package com.fdjloto.api.unit.service;

import com.fdjloto.api.dto.TicketDTO;
import com.fdjloto.api.exception.TicketNotFoundException;
import com.fdjloto.api.model.Ticket;
import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.TicketRepository;
import com.fdjloto.api.repository.UserRepository;
import com.fdjloto.api.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void shouldCreateTicketForExistingUser() {
        User user = new User();
        user.setId("user-1");
        user.setEmail("test@loto.local");

        TicketDTO dto = new TicketDTO();
        dto.setNumbers("1-2-3-4-5");
        dto.setChanceNumber("6");
        dto.setDrawDate("2025-03-12");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ticket result = ticketService.createTicket("user-1", dto);

        assertEquals(user, result.getUser());
        assertEquals("1-2-3-4-5", result.getNumbers());
        assertEquals(6, result.getChanceNumber());
        assertEquals(LocalDate.of(2025, 3, 12), result.getDrawDate());
        assertEquals("mercredi", result.getDrawDay());

        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void shouldThrowWhenUserDoesNotExistOnCreate() {
        TicketDTO dto = new TicketDTO();
        dto.setNumbers("1-2-3-4-5");
        dto.setChanceNumber("6");
        dto.setDrawDate("2025-03-12");

        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.createTicket("missing", dto));

        verify(ticketRepository, never()).save(any());
    }

    @Test
    void shouldUpdateTicketDtoFields() {
        Ticket existing = new Ticket();
        existing.setNumbers("1-2-3-4-5");
        existing.setChanceNumber(6);
        existing.setDrawDate(LocalDate.of(2025, 3, 12));

        TicketDTO dto = new TicketDTO();
        dto.setNumbers("10-11-12-13-14");
        dto.setChanceNumber("8");
        dto.setDrawDate("2025-03-15");

        when(ticketRepository.findById("ticket-1")).thenReturn(Optional.of(existing));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ticket result = ticketService.updateTicket("ticket-1", dto);

        assertEquals("10-11-12-13-14", result.getNumbers());
        assertEquals(8, result.getChanceNumber());
        assertEquals(LocalDate.of(2025, 3, 15), result.getDrawDate());
        assertEquals("samedi", result.getDrawDay());

        verify(ticketRepository).save(existing);
    }

    @Test
    void shouldThrowWhenChanceNumberIsInvalid() {
        Ticket existing = new Ticket();

        TicketDTO dto = new TicketDTO();
        dto.setNumbers("1-2-3-4-5");
        dto.setChanceNumber("bad");
        dto.setDrawDate("2025-03-12");

        when(ticketRepository.findById("ticket-1")).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> ticketService.updateTicket("ticket-1", dto));

        verify(ticketRepository, never()).save(any());
    }

    @Test
    void shouldDeleteTicketWhenOwner() {
        User owner = new User();
        owner.setId("user-1");

        Ticket ticket = new Ticket();
        ticket.setUser(owner);

        when(ticketRepository.findById("ticket-1")).thenReturn(Optional.of(ticket));

        ticketService.deleteTicket("ticket-1", "user-1");

        verify(ticketRepository).deleteById("ticket-1");
    }

    @Test
    void shouldRejectDeleteWhenNotOwnerAndNotAdmin() {
        User owner = new User();
        owner.setId("owner-1");

        Ticket ticket = new Ticket();
        ticket.setUser(owner);

        User otherUser = new User();
        otherUser.setId("other-1");
        otherUser.setAdmin(false);

        when(ticketRepository.findById("ticket-1")).thenReturn(Optional.of(ticket));
        when(userRepository.findById("other-1")).thenReturn(Optional.of(otherUser));

        assertThrows(RuntimeException.class, () -> ticketService.deleteTicket("ticket-1", "other-1"));

        verify(ticketRepository, never()).deleteById(anyString());
    }

    @Test
    void shouldReturnDrawDayInFrenchUppercase() {
        String result = ticketService.getDrawDay("2025-03-12");

        assertEquals("MERCREDI", result);
    }

    @Test
    void shouldThrowWhenTicketNotFound() {
        when(ticketRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById("missing"));
    }
}
