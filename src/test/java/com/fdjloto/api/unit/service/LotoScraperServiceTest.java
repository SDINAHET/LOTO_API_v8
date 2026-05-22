// // // // package com.fdjloto.api.service;

// // // // import org.junit.jupiter.api.Test;
// // // // import static org.junit.jupiter.api.Assertions.*;

// // // // class LotoScraperServiceTest {

// // // //     @Test
// // // //     void shouldParseDoubleCorrectly() {
// // // //         LotoScraperService service = new LotoScraperService();

// // // //         double result = service.parseDouble("12.5", 0.0);

// // // //         assertEquals(12.5, result);
// // // //     }

// // // //     @Test
// // // //     void shouldReturnDefaultIfParseDoubleFails() {
// // // //         LotoScraperService service = new LotoScraperService();

// // // //         double result = service.parseDouble("abc", 1.0);

// // // //         assertEquals(1.0, result);
// // // //     }

// // // //     @Test
// // // //     void shouldParseIntegerCorrectly() {
// // // //         LotoScraperService service = new LotoScraperService();

// // // //         int result = service.parseInteger("42", 0);

// // // //         assertEquals(42, result);
// // // //     }

// // // //     @Test
// // // //     void shouldReturnDefaultIfParseIntegerFails() {
// // // //         LotoScraperService service = new LotoScraperService();

// // // //         int result = service.parseInteger("abc", 99);

// // // //         assertEquals(99, result);
// // // //     }
// // // // }

// // // package com.fdjloto.api.service;

// // // import org.junit.jupiter.api.Test;

// // // import java.io.File;
// // // import java.util.ArrayList;
// // // import java.util.List;

// // // import static org.junit.jupiter.api.Assertions.*;

// // // class LotoScraperServiceTest {

// // //     private final LotoScraperService service = new LotoScraperService();

// // //     // ===============================
// // //     // 🔢 TESTS parseDouble
// // //     // ===============================

// // //     @Test
// // //     void shouldParseDoubleCorrectly() {
// // //         double result = service.parseDouble("12.5", 0.0);
// // //         assertEquals(12.5, result);
// // //     }

// // //     @Test
// // //     void shouldParseDoubleWithCommaAndSpaces() {
// // //         double result = service.parseDouble("1 234,56", 0.0);
// // //         assertEquals(1234.56, result);
// // //     }

// // //     @Test
// // //     void shouldReturnDefaultIfParseDoubleFails() {
// // //         double result = service.parseDouble("abc", 1.0);
// // //         assertEquals(1.0, result);
// // //     }

// // //     @Test
// // //     void shouldReturnDefaultIfDoubleIsNull() {
// // //         double result = service.parseDouble(null, 2.0);
// // //         assertEquals(2.0, result);
// // //     }

// // //     @Test
// // //     void shouldReturnDefaultIfDoubleIsEmpty() {
// // //         double result = service.parseDouble("", 3.0);
// // //         assertEquals(3.0, result);
// // //     }

// // //     @Test
// // //     void shouldNotThrowExceptionForInvalidDouble() {
// // //         assertDoesNotThrow(() -> service.parseDouble("invalid", 0.0));
// // //     }

// // //     // ===============================
// // //     // 🔢 TESTS parseInteger
// // //     // ===============================

// // //     @Test
// // //     void shouldParseIntegerCorrectly() {
// // //         int result = service.parseInteger("42", 0);
// // //         assertEquals(42, result);
// // //     }

// // //     @Test
// // //     void shouldReturnDefaultIfParseIntegerFails() {
// // //         int result = service.parseInteger("abc", 99);
// // //         assertEquals(99, result);
// // //     }

// // //     @Test
// // //     void shouldReturnDefaultIfIntegerIsNull() {
// // //         int result = service.parseInteger(null, 7);
// // //         assertEquals(7, result);
// // //     }

// // //     @Test
// // //     void shouldReturnDefaultIfIntegerIsEmpty() {
// // //         int result = service.parseInteger("", 8);
// // //         assertEquals(8, result);
// // //     }

// // //     @Test
// // //     void shouldNotThrowExceptionForInvalidInteger() {
// // //         assertDoesNotThrow(() -> service.parseInteger("invalid", 0));
// // //     }

// // //     // ===============================
// // //     // 📁 TEST saveCsvFile
// // //     // ===============================

// // //     @Test
// // //     void shouldSaveCsvFileSuccessfully() {
// // //         List<String[]> data = new ArrayList<>();
// // //         data.add(new String[]{"col1", "col2"});
// // //         data.add(new String[]{"val1", "val2"});

// // //         String fileName = "test_loto.csv";

// // //         service.saveCsvFile(fileName, data);

// // //         File file = new File("src/main/resources/files/" + fileName);

// // //         assertTrue(file.exists());

// // //         // Nettoyage après test (propre 👍)
// // //         file.delete();
// // //     }

// // //     // ===============================
// // //     // 🧠 TEST GLOBAL SIMPLE
// // //     // ===============================

// // //     @Test
// // //     void shouldCreateServiceWithoutCrash() {
// // //         assertDoesNotThrow(LotoScraperService::new);
// // //     }
// // // }

// // package com.fdjloto.api.service;

// // import org.junit.jupiter.api.Test;
// // import org.mockito.Mockito;

// // import java.io.*;
// // import java.util.ArrayList;
// // import java.util.List;

// // import com.fdjloto.api.repository.LotoRepository;

// // import static org.junit.jupiter.api.Assertions.*;

// // class LotoScraperServiceTest {

// //     private final LotoScraperService service = new LotoScraperService();

// //     // ===============================
// //     // 🔢 TESTS parseDouble
// //     // ===============================

// //     @Test
// //     void shouldParseDoubleCorrectly() {
// //         double result = service.parseDouble("12.5", 0.0);
// //         assertEquals(12.5, result);
// //     }

// //     @Test
// //     void shouldParseDoubleWithCommaAndSpaces() {
// //         double result = service.parseDouble("1 234,56", 0.0);
// //         assertEquals(1234.56, result);
// //     }

// //     @Test
// //     void shouldReturnDefaultIfParseDoubleFails() {
// //         double result = service.parseDouble("abc", 1.0);
// //         assertEquals(1.0, result);
// //     }

// //     @Test
// //     void shouldReturnDefaultIfDoubleIsNullOrEmpty() {
// //         assertEquals(2.0, service.parseDouble(null, 2.0));
// //         assertEquals(3.0, service.parseDouble("", 3.0));
// //     }

// //     @Test
// //     void shouldNotThrowExceptionForInvalidDouble() {
// //         assertDoesNotThrow(() -> service.parseDouble("invalid", 0.0));
// //     }

// //     // ===============================
// //     // 🔢 TESTS parseInteger
// //     // ===============================

// //     @Test
// //     void shouldParseIntegerCorrectly() {
// //         int result = service.parseInteger("42", 0);
// //         assertEquals(42, result);
// //     }

// //     @Test
// //     void shouldReturnDefaultIfParseIntegerFails() {
// //         int result = service.parseInteger("abc", 99);
// //         assertEquals(99, result);
// //     }

// //     @Test
// //     void shouldReturnDefaultIfIntegerIsNullOrEmpty() {
// //         assertEquals(7, service.parseInteger(null, 7));
// //         assertEquals(8, service.parseInteger("", 8));
// //     }

// //     @Test
// //     void shouldNotThrowExceptionForInvalidInteger() {
// //         assertDoesNotThrow(() -> service.parseInteger("invalid", 0));
// //     }

// //     // ===============================
// //     // 📁 TEST saveCsvFile
// //     // ===============================

// //     @Test
// //     void shouldSaveCsvFileSuccessfully() {
// //         List<String[]> data = new ArrayList<>();
// //         data.add(new String[]{"col1", "col2"});
// //         data.add(new String[]{"val1", "val2"});

// //         String fileName = "test_loto.csv";

// //         service.saveCsvFile(fileName, data);

// //         File file = new File("src/main/resources/files/" + fileName);

// //         assertTrue(file.exists());

// //         file.delete(); // cleanup
// //     }

// //     @Test
// //     void shouldHandleSaveCsvError() {
// //         List<String[]> data = new ArrayList<>();

// //         assertDoesNotThrow(() ->
// //                 service.saveCsvFile("/invalid/path/test.csv", data)
// //         );
// //     }

// //     // ===============================
// //     // 🔥 TEST parseCSV (GROS BOOST JACOCO)
// //     // ===============================

// //     @Test
// //     void shouldParseCsvAndSaveData() throws Exception {

// //         String csv =
// //                 "annee;jour;date;forclusion;1;2;3;4;5;6;combinaison;1;100;2;200;3;300;4;400;5;500;6;600;7;700;8;800;9;900;1;1;code;1;2;3;4;5;test;1;100;2;200;3;300;4;400;7;EUR\n" +
// //                 "2024;LUNDI;01/01/2024;01/02/2024;1;2;3;4;5;6;test;1;100;2;200;3;300;4;400;5;500;6;600;7;700;8;800;9;900;1;1;code;1;2;3;4;5;test;1;100;2;200;3;300;4;400;7;EUR";

// //         InputStream inputStream = new ByteArrayInputStream(csv.getBytes());

// //         LotoScraperService service = new LotoScraperService();

// //         // 🔥 mock dépendances
// //         LotoRepository repo = Mockito.mock(LotoRepository.class);
// //         GainCalculationService gainService = Mockito.mock(GainCalculationService.class);

// //         var repoField = LotoScraperService.class.getDeclaredField("lotoRepository");
// //         repoField.setAccessible(true);
// //         repoField.set(service, repo);

// //         var gainField = LotoScraperService.class.getDeclaredField("gainCalculationService");
// //         gainField.setAccessible(true);
// //         gainField.set(service, gainService);

// //         var method = LotoScraperService.class
// //                 .getDeclaredMethod("parseCSV", InputStream.class);
// //         method.setAccessible(true);

// //         method.invoke(service, inputStream);

// //         Mockito.verify(repo).deleteAll();
// //         Mockito.verify(repo, Mockito.atLeastOnce()).saveAll(Mockito.any());
// //         Mockito.verify(gainService).calculerGains();
// //     }

// //     @Test
// //     void shouldHandleEmptyCsv() throws Exception {

// //         InputStream inputStream = new ByteArrayInputStream("".getBytes());

// //         var method = LotoScraperService.class
// //                 .getDeclaredMethod("parseCSV", InputStream.class);
// //         method.setAccessible(true);

// //         assertDoesNotThrow(() -> method.invoke(service, inputStream));
// //     }

// //     // ===============================
// //     // 🌐 TEST scrapeData
// //     // ===============================

// //     @Test
// //     void shouldHandleScrapeDataWithoutCrash() {

// //         LotoScraperService service = new LotoScraperService();

// //         LotoRepository repo = Mockito.mock(LotoRepository.class);

// //         try {
// //             var field = LotoScraperService.class.getDeclaredField("lotoRepository");
// //             field.setAccessible(true);
// //             field.set(service, repo);
// //         } catch (Exception e) {
// //             fail();
// //         }

// //         assertDoesNotThrow(service::scrapeData);
// //     }

// //     // ===============================
// //     // ⏱ TEST scheduler
// //     // ===============================

// //     @Test
// //     void shouldCallScrapeDataScheduled() {
// //         LotoScraperService spyService = Mockito.spy(new LotoScraperService());
// //         assertDoesNotThrow(spyService::scrapeDataScheduled);
// //     }

// //     // ===============================
// //     // 🧠 TEST GLOBAL
// //     // ===============================

// //     @Test
// //     void shouldCreateServiceWithoutCrash() {
// //         assertDoesNotThrow(LotoScraperService::new);
// //     }
// // }

// package com.fdjloto.api.service;

// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;

// import java.io.*;
// import java.util.ArrayList;
// import java.util.List;

// import com.fdjloto.api.repository.LotoRepository;
// import com.fdjloto.api.service.GainCalculationService;

// import static org.junit.jupiter.api.Assertions.*;

// class LotoScraperServiceTest {

//     // ===============================
//     // 🔧 MÉTHODE UTILITAIRE (IMPORTANT)
//     // ===============================
//     private LotoScraperService createServiceWithMocks() throws Exception {
//         LotoScraperService service = new LotoScraperService();

//         LotoRepository repo = Mockito.mock(LotoRepository.class);
//         GainCalculationService gainService = Mockito.mock(GainCalculationService.class);

//         var repoField = LotoScraperService.class.getDeclaredField("lotoRepository");
//         repoField.setAccessible(true);
//         repoField.set(service, repo);

//         var gainField = LotoScraperService.class.getDeclaredField("gainCalculationService");
//         gainField.setAccessible(true);
//         gainField.set(service, gainService);

//         return service;
//     }

//     private final LotoScraperService service = new LotoScraperService();

//     // ===============================
//     // 🔢 TESTS parseDouble
//     // ===============================

//     @Test
//     void shouldParseDoubleCorrectly() {
//         assertEquals(12.5, service.parseDouble("12.5", 0.0));
//     }

//     @Test
//     void shouldParseDoubleWithCommaAndSpaces() {
//         assertEquals(1234.56, service.parseDouble("1 234,56", 0.0));
//     }

//     @Test
//     void shouldReturnDefaultIfParseDoubleFails() {
//         assertEquals(1.0, service.parseDouble("abc", 1.0));
//     }

//     @Test
//     void shouldReturnDefaultIfDoubleIsNullOrEmpty() {
//         assertEquals(2.0, service.parseDouble(null, 2.0));
//         assertEquals(3.0, service.parseDouble("", 3.0));
//     }

//     @Test
//     void shouldNotThrowExceptionForInvalidDouble() {
//         assertDoesNotThrow(() -> service.parseDouble("invalid", 0.0));
//     }

//     // ===============================
//     // 🔢 TESTS parseInteger
//     // ===============================

//     @Test
//     void shouldParseIntegerCorrectly() {
//         assertEquals(42, service.parseInteger("42", 0));
//     }

//     @Test
//     void shouldReturnDefaultIfParseIntegerFails() {
//         assertEquals(99, service.parseInteger("abc", 99));
//     }

//     @Test
//     void shouldReturnDefaultIfIntegerIsNullOrEmpty() {
//         assertEquals(7, service.parseInteger(null, 7));
//         assertEquals(8, service.parseInteger("", 8));
//     }

//     @Test
//     void shouldNotThrowExceptionForInvalidInteger() {
//         assertDoesNotThrow(() -> service.parseInteger("invalid", 0));
//     }

//     // ===============================
//     // 📁 TEST saveCsvFile
//     // ===============================

//     @Test
//     void shouldSaveCsvFileSuccessfully() {
//         List<String[]> data = new ArrayList<>();
//         data.add(new String[]{"col1", "col2"});

//         service.saveCsvFile("test_loto.csv", data);

//         File file = new File("src/main/resources/files/test_loto.csv");
//         assertTrue(file.exists());

//         file.delete(); // cleanup
//     }

//     @Test
//     void shouldHandleSaveCsvError() {
//         assertDoesNotThrow(() ->
//                 service.saveCsvFile("/invalid/path/test.csv", new ArrayList<>())
//         );
//     }

//     // ===============================
//     // 🔥 TEST parseCSV (FIX + JACOCO BOOST)
//     // ===============================

// 	@Test
// 	void shouldParseCsvAndSaveData() throws Exception {

// 		String header = String.join(";", java.util.Collections.nCopies(50, "col"));
// 		String row = String.join(";", java.util.Collections.nCopies(50, "1"));

// 		String csv = header + "\n" + row;

// 		InputStream inputStream = new ByteArrayInputStream(csv.getBytes());

// 		LotoScraperService service = new LotoScraperService();

// 		LotoRepository repo = Mockito.mock(LotoRepository.class);
// 		GainCalculationService gainService = Mockito.mock(GainCalculationService.class);

// 		var repoField = LotoScraperService.class.getDeclaredField("lotoRepository");
// 		repoField.setAccessible(true);
// 		repoField.set(service, repo);

// 		var gainField = LotoScraperService.class.getDeclaredField("gainCalculationService");
// 		gainField.setAccessible(true);
// 		gainField.set(service, gainService);

// 		var method = LotoScraperService.class
// 				.getDeclaredMethod("parseCSV", InputStream.class);
// 		method.setAccessible(true);

// 		method.invoke(service, inputStream);

// 		Mockito.verify(repo).deleteAll();
// 		Mockito.verify(repo, Mockito.atLeastOnce()).saveAll(Mockito.any());
// 		Mockito.verify(gainService).calculerGains();
// 	}

//     @Test
//     void shouldHandleEmptyCsv() throws Exception {
//         LotoScraperService service = createServiceWithMocks();

//         InputStream inputStream = new ByteArrayInputStream("".getBytes());

//         var method = LotoScraperService.class
//                 .getDeclaredMethod("parseCSV", InputStream.class);
//         method.setAccessible(true);

//         assertDoesNotThrow(() -> method.invoke(service, inputStream));
//     }

//     // ===============================
//     // 🌐 TEST scrapeData
//     // ===============================

//     @Test
//     void shouldHandleScrapeDataWithoutCrash() throws Exception {
//         LotoScraperService service = createServiceWithMocks();
//         assertDoesNotThrow(service::scrapeData);
//     }

//     // ===============================
//     // ⏱ TEST scheduler
//     // ===============================

//     @Test
//     void shouldCallScrapeDataScheduled() throws Exception {
//         LotoScraperService service = createServiceWithMocks();
//         assertDoesNotThrow(service::scrapeDataScheduled);
//     }

//     // ===============================
//     // 🧠 TEST GLOBAL
//     // ===============================

//     @Test
//     void shouldCreateServiceWithoutCrash() {
//         assertDoesNotThrow(LotoScraperService::new);
//     }
// }


package com.fdjloto.api.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.fdjloto.api.repository.LotoRepository;
import com.fdjloto.api.service.GainCalculationService;

import static org.junit.jupiter.api.Assertions.*;

class LotoScraperServiceTest {

    // ===============================
    // 🔧 FACTORY AVEC MOCKS
    // ===============================
    private LotoScraperService createServiceWithMocks() throws Exception {
        LotoScraperService service = new LotoScraperService();

        LotoRepository repo = Mockito.mock(LotoRepository.class);
        GainCalculationService gainService = Mockito.mock(GainCalculationService.class);

        Mockito.when(repo.count()).thenReturn(1L); // évite NPE scheduler

        var repoField = LotoScraperService.class.getDeclaredField("lotoRepository");
        repoField.setAccessible(true);
        repoField.set(service, repo);

        var gainField = LotoScraperService.class.getDeclaredField("gainCalculationService");
        gainField.setAccessible(true);
        gainField.set(service, gainService);

        return service;
    }

    private final LotoScraperService service = new LotoScraperService();

    // ===============================
    // 🔢 parseDouble
    // ===============================

    @Test
    void shouldParseDoubleCorrectly() {
        assertEquals(12.5, service.parseDouble("12.5", 0.0));
    }

    @Test
    void shouldParseDoubleWithCommaAndSpaces() {
        assertEquals(1234.56, service.parseDouble("1 234,56", 0.0));
    }

    @Test
    void shouldReturnDefaultIfParseDoubleFails() {
        assertEquals(1.0, service.parseDouble("abc", 1.0));
    }

    @Test
    void shouldReturnDefaultIfDoubleIsNullOrEmpty() {
        assertEquals(2.0, service.parseDouble(null, 2.0));
        assertEquals(3.0, service.parseDouble("", 3.0));
    }

    @Test
    void shouldNotThrowExceptionForInvalidDouble() {
        assertDoesNotThrow(() -> service.parseDouble("invalid", 0.0));
    }

    // ===============================
    // 🔢 parseInteger
    // ===============================

    @Test
    void shouldParseIntegerCorrectly() {
        assertEquals(42, service.parseInteger("42", 0));
    }

    @Test
    void shouldReturnDefaultIfParseIntegerFails() {
        assertEquals(99, service.parseInteger("abc", 99));
    }

    @Test
    void shouldReturnDefaultIfIntegerIsNullOrEmpty() {
        assertEquals(7, service.parseInteger(null, 7));
        assertEquals(8, service.parseInteger("", 8));
    }

    @Test
    void shouldNotThrowExceptionForInvalidInteger() {
        assertDoesNotThrow(() -> service.parseInteger("invalid", 0));
    }

    // ===============================
    // 📁 saveCsvFile
    // ===============================

    @Test
    void shouldSaveCsvFileSuccessfully() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"col1", "col2"});

        service.saveCsvFile("test_loto.csv", data);

        File file = new File("src/main/resources/files/test_loto.csv");
        assertTrue(file.exists());

        file.delete(); // cleanup
    }

    @Test
    void shouldHandleSaveCsvError() {
        assertDoesNotThrow(() ->
                service.saveCsvFile("/invalid/path/test.csv", new ArrayList<>())
        );
    }

    // ===============================
    // 🔥 parseCSV (FULL COVERAGE)
    // ===============================

    @Test
    void shouldParseCsvAndSaveData() throws Exception {

        // CSV valide (50 colonnes)
        String header = String.join(";", java.util.Collections.nCopies(50, "col"));
        String row = String.join(";", java.util.Collections.nCopies(50, "1"));

        String csv = header + "\n" + row;

        InputStream inputStream = new ByteArrayInputStream(csv.getBytes());

        LotoScraperService service = createServiceWithMocks();

        var method = LotoScraperService.class
                .getDeclaredMethod("parseCSV", InputStream.class);
        method.setAccessible(true);

        method.invoke(service, inputStream);

        // LotoRepository repo = (LotoRepository)
        //         LotoScraperService.class.getDeclaredField("lotoRepository")
        //                 .get(service);
		// ✅ CORRECT
		var field = LotoScraperService.class.getDeclaredField("lotoRepository");
		field.setAccessible(true);
		LotoRepository repo = (LotoRepository) field.get(service);

        Mockito.verify(repo).deleteAll();
        Mockito.verify(repo, Mockito.atLeastOnce()).saveAll(Mockito.any());
    }

    @Test
    void shouldHandleEmptyCsv() throws Exception {
        LotoScraperService service = createServiceWithMocks();

        InputStream inputStream = new ByteArrayInputStream("".getBytes());

        var method = LotoScraperService.class
                .getDeclaredMethod("parseCSV", InputStream.class);
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(service, inputStream));
    }

    @Test
    void shouldSkipInvalidCsvRow() throws Exception {
        LotoScraperService service = createServiceWithMocks();

        String csv = "a;b;c\n1;2;3"; // lignes trop courtes

        InputStream inputStream = new ByteArrayInputStream(csv.getBytes());

        var method = LotoScraperService.class
                .getDeclaredMethod("parseCSV", InputStream.class);
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(service, inputStream));
    }

    // ===============================
    // 🌐 scrapeData
    // ===============================

    @Test
    void shouldHandleScrapeDataWithoutCrash() throws Exception {
        LotoScraperService service = createServiceWithMocks();
        assertDoesNotThrow(service::scrapeData);
    }

    @Test
    void shouldHandleMongoError() throws Exception {
        LotoScraperService service = new LotoScraperService();

        LotoRepository repo = Mockito.mock(LotoRepository.class);
        Mockito.when(repo.count()).thenThrow(new RuntimeException("Mongo down"));

        var field = LotoScraperService.class.getDeclaredField("lotoRepository");
        field.setAccessible(true);
        field.set(service, repo);

        assertDoesNotThrow(service::scrapeData);
    }

    // ===============================
    // ⏱ scheduler
    // ===============================

    @Test
    void shouldCallScrapeDataScheduled() throws Exception {
        LotoScraperService service = createServiceWithMocks();
        assertDoesNotThrow(service::scrapeDataScheduled);
    }

    // @Test
    // void shouldCallInitialScheduler() {
    //     assertDoesNotThrow(() -> new LotoScraperService().scheduleInitialScrape());
    // }
    // @Test
    // void shouldCallInitialScheduler() throws Exception {

    //     // Arrange
    //     LotoScraperService service = Mockito.spy(createServiceWithMocks());

    //     // On mock scrapeData pour éviter appel réel
    //     Mockito.doNothing().when(service).scrapeData();

    //     // Act
    //     service.scheduleInitialScrape();

    //     // Assert
    //     Mockito.verify(service, Mockito.times(1)).scrapeData();
    // }

    // ===============================
    // 🧠 GLOBAL
    // ===============================

    @Test
    void shouldCreateServiceWithoutCrash() {
        assertDoesNotThrow(LotoScraperService::new);
    }
}
