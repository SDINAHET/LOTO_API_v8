// package com.fdjloto.api.service;

// import org.junit.jupiter.api.*;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.*;
// import com.fdjloto.api.repository.Historique20DetailRepository;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// @ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
// class Historique20DetailServiceTest {
//     @Mock Historique20DetailRepository historique20DetailRepository;

//     @InjectMocks Historique20DetailService service;

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

import com.fdjloto.api.model.Historique20Detail;
import com.fdjloto.api.repository.Historique20DetailRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class Historique20DetailServiceTest {

    @Mock
    Historique20DetailRepository repository;

    @InjectMocks
    Historique20DetailService service;

    @Test
    void getTirageByDate_valid() {
        when(repository.findByDateDeTirage(any()))
                .thenReturn(Optional.of(new Historique20Detail()));

        Optional<Historique20Detail> result =
                service.getTirageByDate("2024-01-01");

        assertThat(result).isPresent();
    }

    @Test
    void getTirageByDate_invalid() {
        Optional<Historique20Detail> result =
                service.getTirageByDate("bad-date");

        assertThat(result).isEmpty();
    }

    @Test
    void getTiragesParPlage_valid() {
        when(repository.findByDateDeTirageBetween(any(), any()))
                .thenReturn(List.of(new Historique20Detail()));

        List<Historique20Detail> result =
                service.getTiragesParPlageDeDates("2024-01-01", "2024-01-10");

        assertThat(result).isNotEmpty();
    }

    @Test
    void getTiragesParPlage_invalid() {
        List<Historique20Detail> result =
                service.getTiragesParPlageDeDates("bad", "bad");

        assertThat(result).isEmpty();
    }

    @Test
    void getAllTirages() {
        when(repository.findAll()).thenReturn(List.of(new Historique20Detail()));

        List<Historique20Detail> result = service.getAllTirages();

        assertThat(result).isNotEmpty();
    }

    @Test
    void getTiragePrecedent() {
        when(repository.findTopByDateDeTirageBeforeOrderByDateDeTirageDesc(any()))
                .thenReturn(Optional.of(new Historique20Detail()));

        Optional<Historique20Detail> result =
                service.getTiragePrecedent(LocalDate.now());

        assertThat(result).isPresent();
    }

    @Test
    void getTirageSuivant() {
        when(repository.findTopByDateDeTirageAfterOrderByDateDeTirageAsc(any()))
                .thenReturn(Optional.of(new Historique20Detail()));

        Optional<Historique20Detail> result =
                service.getTirageSuivant(LocalDate.now());

        assertThat(result).isPresent();
    }
}
