// package com.fdjloto.api.service;

// import org.junit.jupiter.api.*;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.*;
// import com.fdjloto.api.repository.TirageRepository;
// import com.fdjloto.api.repository.TicketGainRepository;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// @ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
// class GainCalculationServiceTest {
//     @Mock TirageRepository tirageRepository;
//     @Mock TicketGainRepository ticketGainRepository;

//     @InjectMocks GainCalculationService service;

// @Test void smoke_01() {
//             assertNotNull(service);
//         }
// @Test void smoke_02() {
//             assertNotNull(service);
//         }
// @Test void smoke_03() {
//             assertNotNull(service);
//         }
// @Test void smoke_04() {
//             assertNotNull(service);
//         }
// @Test void smoke_05() {
//             assertNotNull(service);
//         }
// @Test void smoke_06() {
//             assertNotNull(service);
//         }
// @Test void smoke_07() {
//             assertNotNull(service);
//         }
// @Test void smoke_08() {
//             assertNotNull(service);
//         }
// @Test void smoke_09() {
//             assertNotNull(service);
//         }
// @Test void smoke_10() {
//             assertNotNull(service);
//         }
// @Test void smoke_11() {
//             assertNotNull(service);
//         }
// @Test void smoke_12() {
//             assertNotNull(service);
//         }
// }

package com.fdjloto.api.service;

import com.fdjloto.api.model.*;
import com.fdjloto.api.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class GainCalculationServiceTest {

    @Mock TicketRepository ticketRepository;
    @Mock LotoRepository lotoRepository;
    @Mock TicketGainRepository ticketGainRepository;
    @Mock TicketGainService ticketGainService;

    @InjectMocks GainCalculationService service;

    // 🔧 helper pour appeler méthode privée
    private double callGetGain(int match, boolean chance, LotoResult loto) throws Exception {
        Method m = GainCalculationService.class
                .getDeclaredMethod("getGainAmount", int.class, boolean.class, LotoResult.class);
        m.setAccessible(true);
        return (double) m.invoke(service, match, chance, loto);
    }

    // ========================
    // 🎯 TEST getGainAmount
    // ========================

    @Test
    void gain_rank1() throws Exception {
        LotoResult loto = mock(LotoResult.class);
        when(loto.getRapportDuRang1()).thenReturn(100.0);

        assertEquals(100.0, callGetGain(5, true, loto));
    }

    @Test
    void gain_rank2() throws Exception {
        LotoResult loto = mock(LotoResult.class);
        when(loto.getRapportDuRang2()).thenReturn(90.0);

        assertEquals(90.0, callGetGain(5, false, loto));
    }

    @Test
    void gain_rank3() throws Exception {
        LotoResult loto = mock(LotoResult.class);
        when(loto.getRapportDuRang3()).thenReturn(80.0);

        assertEquals(80.0, callGetGain(4, true, loto));
    }

    @Test
    void gain_rank4() throws Exception {
        LotoResult loto = mock(LotoResult.class);
        when(loto.getRapportDuRang4()).thenReturn(70.0);

        assertEquals(70.0, callGetGain(4, false, loto));
    }

    @Test
    void gain_rank5() throws Exception {
        LotoResult loto = mock(LotoResult.class);
        when(loto.getRapportDuRang5()).thenReturn(60.0);

        assertEquals(60.0, callGetGain(3, true, loto));
    }

    @Test
    void gain_rank6() throws Exception {
        LotoResult loto = mock(LotoResult.class);
        when(loto.getRapportDuRang6()).thenReturn(50.0);

        assertEquals(50.0, callGetGain(3, false, loto));
    }

    @Test
    void gain_rank7() throws Exception {
        LotoResult loto = mock(LotoResult.class);
        when(loto.getRapportDuRang7()).thenReturn(40.0);

        assertEquals(40.0, callGetGain(2, true, loto));
    }

    @Test
    void gain_rank8() throws Exception {
        LotoResult loto = mock(LotoResult.class);
        when(loto.getRapportDuRang8()).thenReturn(30.0);

        assertEquals(30.0, callGetGain(2, false, loto));
    }

    @Test
    void gain_rank9() throws Exception {
        LotoResult loto = mock(LotoResult.class);
        when(loto.getRapportDuRang9()).thenReturn(20.0);

        assertEquals(20.0, callGetGain(0, true, loto));
    }

    @Test
    void gain_rank10() throws Exception {
        LotoResult loto = mock(LotoResult.class);

        assertEquals(2.20, callGetGain(1, true, loto));
    }

    @Test
    void gain_zero_case() throws Exception {
        LotoResult loto = mock(LotoResult.class);

        assertEquals(0.0, callGetGain(1, false, loto));
    }

    // ========================
    // 🎯 TEST saveOrUpdateGain
    // ========================

    @Test
    void updateExistingGain() throws Exception {
        Ticket ticket = mock(Ticket.class);
        when(ticket.getId()).thenReturn("t1");

        TicketGain existing = new TicketGain();
        when(ticketGainRepository.findByTicketId("t1"))
                .thenReturn(Optional.of(existing));

        Method m = GainCalculationService.class
                .getDeclaredMethod("saveOrUpdateGain", Ticket.class, int.class, boolean.class, double.class);
        m.setAccessible(true);

        m.invoke(service, ticket, 3, true, 50.0);

        verify(ticketGainRepository).save(existing);
        assertEquals(3, existing.getMatchingNumbers());
        assertTrue(existing.isLuckyNumberMatch());
        assertEquals(50.0, existing.getGainAmount());
    }

    @Test
    void createNewGain() throws Exception {
        Ticket ticket = mock(Ticket.class);
        when(ticket.getId()).thenReturn("t2");

        when(ticketGainRepository.findByTicketId("t2"))
                .thenReturn(Optional.empty());

        Method m = GainCalculationService.class
                .getDeclaredMethod("saveOrUpdateGain", Ticket.class, int.class, boolean.class, double.class);
        m.setAccessible(true);

        m.invoke(service, ticket, 2, false, 30.0);

        verify(ticketGainRepository).save(any(TicketGain.class));
    }
}
