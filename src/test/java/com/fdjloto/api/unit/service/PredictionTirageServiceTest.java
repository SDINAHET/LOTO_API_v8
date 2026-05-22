// // package com.fdjloto.api.service;

// // import com.fdjloto.api.model.PredictionTirageModel;
// // import com.fdjloto.api.model.Tirage;
// // import com.fdjloto.api.repository.PredictionRepository;
// // import com.fdjloto.api.repository.TirageRepository;
// // import org.junit.jupiter.api.AfterEach;
// // import org.junit.jupiter.api.BeforeEach;
// // import org.junit.jupiter.api.Test;
// // import org.mockito.ArgumentCaptor;
// // import org.mockito.Captor;
// // import org.mockito.InjectMocks;
// // import org.mockito.Mock;
// // import org.mockito.MockitoAnnotations;

// // import java.util.List;

// // import static org.assertj.core.api.Assertions.assertThat;
// // import static org.mockito.Mockito.*;

// // class PredictionTirageServiceTest {

// //     private AutoCloseable mocks;

// //     @Mock
// //     TirageRepository tirageRepository;

// //     @Mock
// //     PredictionRepository predictionRepository;

// //     @InjectMocks
// //     PredictionTirageService service;

// //     @Captor
// //     ArgumentCaptor<PredictionTirageModel> predictionCaptor;

// //     @BeforeEach
// //     void setUp() {
// //         mocks = MockitoAnnotations.openMocks(this);
// //     }

// //     @AfterEach
// //     void tearDown() throws Exception {
// //         if (mocks != null) mocks.close();
// //     }

// //     @Test
// //     void generatePrediction_whenNoTirage_returnsNullAndDoesNotSave() {
// //         when(tirageRepository.findAll()).thenReturn(List.of());

// //         PredictionTirageModel result = service.generatePrediction();

// //         assertThat(result).isNull();
// //         verify(predictionRepository, never()).save(any());
// //     }

// //     @Test
// //     void generatePrediction_whenTiragesExist_savesPrediction() {
// //         Tirage t = new Tirage();
// //         t.setBoules(new int[]{1, 2, 3, 4, 5});
// //         t.setNumeroChance(9);
// //         when(tirageRepository.findAll()).thenReturn(List.of(t));

// //         PredictionTirageModel result = service.generatePrediction();

// //         assertThat(result).isNotNull();
// //         verify(predictionRepository).save(any(PredictionTirageModel.class));
// //         assertThat(result.getProbableNumbers()).isNotNull();
// //         assertThat(result.getProbableNumbers().size()).isBetween(1, 5);
// //         assertThat(result.getProbableChance()).isEqualTo(9);
// //     }

// //     @Test
// //     void generatePrediction_savesModelWithSortieRates() {
// //         Tirage t1 = new Tirage();
// //         t1.setBoules(new int[]{10, 11, 12, 13, 14});
// //         t1.setNumeroChance(1);
// //         Tirage t2 = new Tirage();
// //         t2.setBoules(new int[]{10, 20, 30, 40, 49});
// //         t2.setNumeroChance(1);

// //         when(tirageRepository.findAll()).thenReturn(List.of(t1, t2));
// //         when(predictionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

// //         PredictionTirageModel result = service.generatePrediction();

// //         assertThat(result).isNotNull();
// //         assertThat(result.getSortieRates()).isNotNull();
// //         // 10 apparaît 2 fois sur 10 boules => 20%
// //         assertThat(result.getSortieRates().get(10)).isNotNull();
// //     }

// //     @Test
// //     void generatePrediction_picksAtMost5Numbers() {
// //         Tirage t = new Tirage();
// //         t.setBoules(new int[]{1, 2, 3, 4, 5});
// //         t.setNumeroChance(7);
// //         // répéter pour gonfler les stats
// //         when(tirageRepository.findAll()).thenReturn(List.of(t, t, t, t, t));

// //         PredictionTirageModel result = service.generatePrediction();

// //         assertThat(result).isNotNull();
// //         assertThat(result.getProbableNumbers().size()).isLessThanOrEqualTo(5);
// //     }
// // }

// package com.fdjloto.api.service;

// import com.fdjloto.api.model.PredictionTirageModel;
// import com.fdjloto.api.model.Tirage;
// import com.fdjloto.api.repository.PredictionRepository;
// import com.fdjloto.api.repository.TirageRepository;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.*;

// import java.util.List;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.Mockito.*;

// @ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
// class PredictionTirageServiceTest {

//     @Mock
//     TirageRepository tirageRepository;

//     @Mock
//     PredictionRepository predictionRepository;

//     @InjectMocks
//     PredictionTirageService service;

//     // 🔴 CAS 1 : aucun tirage → couvre if(tirages.isEmpty)
//     @Test
//     void generatePrediction_whenEmpty_returnsNull() {
//         when(tirageRepository.findAll()).thenReturn(List.of());

//         PredictionTirageModel result = service.generatePrediction();

//         assertThat(result).isNull();
//         verify(predictionRepository, never()).save(any());
//     }

//     // 🟢 CAS 2 : tirages présents → couvre logique principale
//     @Test
//     void generatePrediction_whenData_exists() {
//         Tirage t = new Tirage();
//         t.setBoules(new int[]{1,2,3,4,5});
//         t.setNumeroChance(7);

//         when(tirageRepository.findAll()).thenReturn(List.of(t));
//         when(predictionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

//         PredictionTirageModel result = service.generatePrediction();

//         assertThat(result).isNotNull();
//         assertThat(result.getProbableNumbers()).isNotEmpty();
//         verify(predictionRepository).save(any());
//     }

//     // 🔥 CAS 3 : couvre generatePredictionScheduled() IF
//     @Test
//     void generatePredictionScheduled_withResult() {
//         Tirage t = new Tirage();
//         t.setBoules(new int[]{1,2,3,4,5});
//         t.setNumeroChance(7);

//         when(tirageRepository.findAll()).thenReturn(List.of(t));
//         when(predictionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

//         service.generatePredictionScheduled();
//     }

//     // 🔥 CAS 4 : couvre generatePredictionScheduled() ELSE
//     @Test
//     void generatePredictionScheduled_withoutResult() {
//         when(tirageRepository.findAll()).thenReturn(List.of());

//         service.generatePredictionScheduled();
//     }

//     // 🔥 BONUS : couvre scheduler init
//     @Test
//     void scheduleInitialPrediction_doesNotCrash() {
//         service.scheduleInitialPrediction();
//     }
// }


package com.fdjloto.api.service;

import com.fdjloto.api.model.PredictionTirageModel;
import com.fdjloto.api.model.Tirage;
import com.fdjloto.api.repository.PredictionRepository;
import com.fdjloto.api.repository.TirageRepository;
import com.fdjloto.api.service.PredictionTirageService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PredictionTirageServiceTest {

    @Mock
    TirageRepository tirageRepository;

    @Mock
    PredictionRepository predictionRepository;

    @InjectMocks
    PredictionTirageService service;

    // 🔴 CAS 1 : aucun tirage → couvre if(tirages.isEmpty)
    @Test
    void generatePrediction_whenEmpty_returnsNull() {
        when(tirageRepository.findAll()).thenReturn(List.of());

        PredictionTirageModel result = service.generatePrediction();

        assertThat(result).isNull();
        verify(predictionRepository, never()).save(any());
    }

    // 🟢 CAS 2 : tirages présents → couvre logique principale
    @Test
    void generatePrediction_whenData_exists() {
        Tirage t = new Tirage();
        t.setBoule1(1);
        t.setBoule2(2);
        t.setBoule3(3);
        t.setBoule4(4);
        t.setBoule5(5);
        t.setNumeroChance(7);

        when(tirageRepository.findAll()).thenReturn(List.of(t));
        when(predictionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PredictionTirageModel result = service.generatePrediction();

        assertThat(result).isNotNull();
        assertThat(result.getProbableNumbers()).isNotEmpty();

        verify(predictionRepository).save(any());
    }

    // 🔥 CAS 3 : couvre generatePredictionScheduled() IF
    @Test
    void generatePredictionScheduled_withResult() {
        Tirage t = new Tirage();
        t.setBoule1(1);
        t.setBoule2(2);
        t.setBoule3(3);
        t.setBoule4(4);
        t.setBoule5(5);
        t.setNumeroChance(7);

        when(tirageRepository.findAll()).thenReturn(List.of(t));
        when(predictionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.generatePredictionScheduled();

        verify(predictionRepository).save(any());
    }

    // 🔥 CAS 4 : couvre generatePredictionScheduled() ELSE
    @Test
    void generatePredictionScheduled_withoutResult() {
        when(tirageRepository.findAll()).thenReturn(List.of());

        service.generatePredictionScheduled();

        verify(predictionRepository, never()).save(any());
    }

    // 🔥 BONUS : couvre scheduler init
    // @Test
    // void scheduleInitialPrediction_doesNotCrash() {
    //     service.scheduleInitialPrediction();
    // }
    // @Test
    // void generateFirstPredictionOnce_doesNotCrash() {
    //     service.generateFirstPredictionOnce();
    // }
    @Test
    void generateFirstPredictionOnce_doesNotCrash() {

        when(tirageRepository.findAll()).thenReturn(List.of());

        service.generateFirstPredictionOnce();

        verify(predictionRepository, never()).save(any());
    }

	@Test
	void generatePrediction_whenTotalNumbersZero_doesNotCrash() {
		Tirage t = new Tirage();
		// ❌ volontairement aucun numéro valide
		// => getBoules() = [0,0,0,0,0] → totalNumbers reste 0 selon logique

		when(tirageRepository.findAll()).thenReturn(List.of(t));
		when(predictionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		PredictionTirageModel result = service.generatePrediction();

		assertThat(result).isNotNull();
	}
}
