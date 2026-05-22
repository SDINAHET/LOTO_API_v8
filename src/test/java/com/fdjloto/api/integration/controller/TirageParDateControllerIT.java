// // package com.fdjloto.api.integration.controller;

// // import com.fdjloto.api.model.Historique20Detail;
// // import com.fdjloto.api.service.Historique20DetailService;
// // import org.junit.jupiter.api.DisplayName;
// // import org.junit.jupiter.api.Test;
// // import org.mockito.Mockito;
// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// // import org.springframework.boot.test.context.SpringBootTest;
// // import org.springframework.boot.test.mock.mockito.MockBean;
// // import org.springframework.test.context.ActiveProfiles;
// // import org.springframework.test.web.servlet.MockMvc;

// // import java.util.Optional;

// // import static org.mockito.ArgumentMatchers.any;
// // import static org.mockito.ArgumentMatchers.eq;
// // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// // /**
// //  * Test d'intégration du controller TirageParDateController
// //  *
// //  * On utilise :
// //  * - MockMvc pour simuler les requêtes HTTP
// //  * - @MockBean pour simuler le service
// //  * - profile "test"
// //  */
// // @SpringBootTest
// // @AutoConfigureMockMvc(addFilters = false)
// // @ActiveProfiles("test")
// // class TirageParDateControllerIT {

// //     @Autowired
// //     private MockMvc mockMvc;

// //     /**
// //      * On mock le service pour contrôler
// //      * les réponses de la base de données
// //      */
// //     @MockBean
// //     private Historique20DetailService detailService;


// //     /**
// //      * ✅ TEST 1
// //      *
// //      * Cas normal :
// //      * tirage existant en base
// //      *
// //      * Vérifie :
// //      * - status 200
// //      * - présence des variables Thymeleaf
// //      */
// //     @Test
// //     @DisplayName("Should return draw page when draw exists")
// //     void shouldReturnDrawWhenExists() throws Exception {

// //         Historique20Detail detail = new Historique20Detail();

// //         Mockito.when(detailService.getTirageByDate("2026-03-04"))
// //                 .thenReturn(Optional.of(detail));

// //         mockMvc.perform(get("/tirage/2026-03-04"))
// //                 .andExpect(status().isOk())
// //                 .andExpect(view().name("tirage-date"))
// //                 .andExpect(model().attributeExists("details"))
// //                 .andExpect(model().attribute("isPending", false));
// //     }


// //     /**
// //      * ✅ TEST 2
// //      *
// //      * Cas tirage futur :
// //      * pas encore présent en base
// //      *
// //      * Le controller doit :
// //      * - retourner 200
// //      * - afficher une page "pending"
// //      */
// //     @Test
// //     @DisplayName("Should return pending page when draw not yet available")
// //     void shouldReturnPendingWhenNotInDatabase() throws Exception {

// //         Mockito.when(detailService.getTirageByDate("2026-03-07"))
// //                 .thenReturn(Optional.empty());

// //         mockMvc.perform(get("/tirage/2026-03-07"))
// //                 .andExpect(status().isOk())
// //                 .andExpect(view().name("tirage-date"))
// //                 .andExpect(model().attribute("isPending", true));
// //     }


// //     /**
// //      * ❌ TEST 3
// //      *
// //      * Format de date invalide
// //      *
// //      * Exemple :
// //      * /tirage/abc
// //      *
// //      * Doit retourner :
// //      * 400 BAD REQUEST
// //      */
// //     @Test
// //     @DisplayName("Should return 400 when date format invalid")
// //     void shouldReturnBadRequestWhenDateInvalid() throws Exception {

// //         mockMvc.perform(get("/tirage/invalid-date"))
// //                 .andExpect(status().isBadRequest());
// //     }


// //     /**
// //      * ❌ TEST 4
// //      *
// //      * Jour invalide
// //      *
// //      * Les tirages ne sont que :
// //      * lundi / mercredi / samedi
// //      *
// //      * Exemple :
// //      * 2026-03-05 (jeudi)
// //      */
// //     @Test
// //     @DisplayName("Should return 404 when date is not a draw day")
// //     void shouldReturn404WhenNotDrawDay() throws Exception {

// //         mockMvc.perform(get("/tirage/2026-03-05"))
// //                 .andExpect(status().isNotFound());
// //     }


// //     /**
// //      * ✅ TEST 5
// //      *
// //      * Route SEO
// //      *
// //      * /resultat-loto-2026-03-04
// //      */
// //     @Test
// //     @DisplayName("SEO route should work")
// //     void seoRouteShouldWork() throws Exception {

// //         Mockito.when(detailService.getTirageByDate("2026-03-04"))
// //                 .thenReturn(Optional.empty());

// //         mockMvc.perform(get("/resultat-loto-2026-03-04"))
// //                 .andExpect(status().isOk());
// //     }


// //     /**
// //      * ✅ TEST 6
// //      *
// //      * Route SEO avec jour
// //      *
// //      * /tirage-loto-samedi-2026-03-07
// //      */
// //     @Test
// //     @DisplayName("SEO route with day should work")
// //     void seoDayRouteShouldWork() throws Exception {

// //         Mockito.when(detailService.getTirageByDate("2026-03-07"))
// //                 .thenReturn(Optional.empty());

// //         mockMvc.perform(get("/tirage-loto-samedi-2026-03-07"))
// //                 .andExpect(status().isOk());
// //     }


// //     /**
// //      * ✅ TEST 7
// //      *
// //      * Page :
// //      * /resultat-loto-aujourdhui
// //      *
// //      * Vérifie juste que la route fonctionne
// //      */
// //     @Test
// //     @DisplayName("Should return today draw page")
// //     void todayPageShouldWork() throws Exception {

// //         Mockito.when(detailService.getTirageByDate(any()))
// //                 .thenReturn(Optional.empty());

// //         mockMvc.perform(get("/resultat-loto-aujourdhui"))
// //                 .andExpect(status().isOk());
// //     }


// //     /**
// //      * ✅ TEST 8
// //      *
// //      * Page :
// //      * /resultat-loto-hier
// //      */
// //     @Test
// //     @DisplayName("Should return yesterday draw page")
// //     void yesterdayPageShouldWork() throws Exception {

// //         Mockito.when(detailService.getTirageByDate(any()))
// //                 .thenReturn(Optional.empty());

// //         mockMvc.perform(get("/resultat-loto-hier"))
// //                 .andExpect(status().isOk());
// //     }


// //     /**
// //      * ✅ TEST 9
// //      *
// //      * Page :
// //      * /prochain-tirage-loto
// //      */
// //     @Test
// //     @DisplayName("Should return next draw page")
// //     void nextDrawPageShouldWork() throws Exception {

// //         Mockito.when(detailService.getTirageByDate(any()))
// //                 .thenReturn(Optional.empty());

// //         mockMvc.perform(get("/prochain-tirage-loto"))
// //                 .andExpect(status().isOk());
// //     }

// // }

// package com.fdjloto.api.integration.controller;

// import com.fdjloto.api.model.Historique20Detail;
// import com.fdjloto.api.security.JwtAuthenticationFilter;
// import com.fdjloto.api.service.Historique20DetailService;
// import com.fdjloto.api.service.PredictionTirageService;
// import com.fdjloto.api.service.UserServiceImpl;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.Optional;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import com.fdjloto.api.controller.TirageParDateController;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.context.annotation.Import;


// @SpringBootTest
// // @WebMvcTest(TirageParDateController.class)
// @AutoConfigureMockMvc(addFilters = false)
// // @AutoConfigureMockMvc
// @ActiveProfiles("test")
// // @Import(com.fdjloto.api.config.TestSecurityConfig.class)
// // @WebMvcTest(controllers = TirageParDateController.class)
// // @AutoConfigureMockMvc(addFilters = false)
// // @ActiveProfiles("test")
// // @Import(com.fdjloto.api.config.TestSecurityConfig.class)

// class TirageParDateControllerIT {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private Historique20DetailService detailService;

//     @MockBean
//     private PredictionTirageService predictionService;

//     @MockBean
//     private JwtAuthenticationFilter jwtAuthenticationFilter;

//     @MockBean
//     private UserServiceImpl userService;


// //     @MockBean
// //     private com.fdjloto.api.service.PredictionTirageService predictionService;

// //     @MockBean
// //     private Historique20DetailService detailService;

// //     @MockBean
// //     private PredictionTirageService predictionService;

// //     @MockBean
// //     private com.fdjloto.api.security.JwtAuthenticationFilter jwtAuthenticationFilter;

// //     @MockBean
// //     private com.fdjloto.api.service.UserServiceImpl userService;


//     // ===============================
//     // CAS OK : tirage existant
//     // ===============================

//     @Test
//     @DisplayName("Should return draw page when draw exists")
//     void shouldReturnDrawWhenExists() throws Exception {

//         Historique20Detail detail = new Historique20Detail();

//         Mockito.when(detailService.getTirageByDate("2026-03-04"))
//                 .thenReturn(Optional.of(detail));

//         mockMvc.perform(get("/tirage/2026-03-04"))
//                 .andExpect(status().isOk())
//                 .andExpect(view().name("tirage-date"))
//                 .andExpect(model().attribute("details", detail))
//                 .andExpect(model().attribute("isPending", false))
//                 .andExpect(model().attributeExists("dateFr"))
//                 .andExpect(model().attributeExists("dateIso"))
//                 .andExpect(model().attributeExists("prevIso"))
//                 .andExpect(model().attributeExists("nextIso"));
//     }


//     // ===============================
//     // CAS FUTUR (pending)
//     // ===============================

//     @Test
//     @DisplayName("Should return pending page when draw not yet available")
//     void shouldReturnPendingWhenNotInDatabase() throws Exception {

//         Mockito.when(detailService.getTirageByDate("2026-03-07"))
//                 .thenReturn(Optional.empty());

//         mockMvc.perform(get("/tirage/2026-03-07"))
//                 .andExpect(status().isOk())
//                 .andExpect(view().name("tirage-date"))
//                 .andExpect(model().attribute("details", (Object) null))
//                 .andExpect(model().attribute("isPending", true))
//                 .andExpect(model().attributeExists("seoTitle"))
//                 .andExpect(model().attributeExists("seoDescription"));
//     }


//     // ===============================
//     // FORMAT DATE INVALIDE
//     // ===============================

//     @Test
//     @DisplayName("Should return 400 when date format invalid")
//     void shouldReturnBadRequestWhenDateInvalid() throws Exception {

//         mockMvc.perform(get("/tirage/invalid-date"))
//                 .andExpect(status().isBadRequest());
//     }


//     // ===============================
//     // JOUR NON AUTORISÉ
//     // ===============================

//     @Test
//     @DisplayName("Should return 404 when date is not a draw day")
//     void shouldReturn404WhenNotDrawDay() throws Exception {

//         mockMvc.perform(get("/tirage/2026-03-05")) // jeudi
//                 .andExpect(status().isNotFound());
//     }


//     // ===============================
//     // ROUTE SEO RESULTAT
//     // ===============================

//     @Test
//     @DisplayName("SEO route should work")
//     void seoRouteShouldWork() throws Exception {

//         Mockito.when(detailService.getTirageByDate("2026-03-04"))
//                 .thenReturn(Optional.empty());

//         mockMvc.perform(get("/resultat-loto-2026-03-04"))
//                 .andExpect(status().isOk())
//                 .andExpect(view().name("tirage-date"));
//     }


//     // ===============================
//     // ROUTE SEO AVEC JOUR
//     // ===============================

//     @Test
//     @DisplayName("SEO route with day should work")
//     void seoDayRouteShouldWork() throws Exception {

//         Mockito.when(detailService.getTirageByDate("2026-03-07"))
//                 .thenReturn(Optional.empty());

//         mockMvc.perform(get("/tirage-loto-samedi-2026-03-07"))
//                 .andExpect(status().isOk())
//                 .andExpect(view().name("tirage-date"));
//     }


//     // ===============================
//     // PAGE AUJOURD'HUI
//     // ===============================

//     @Test
//     @DisplayName("Should return today draw page")
//     void todayPageShouldWork() throws Exception {

//         Mockito.when(detailService.getTirageByDate(any()))
//                 .thenReturn(Optional.empty());

//         mockMvc.perform(get("/resultat-loto-aujourdhui"))
//                 .andExpect(status().isOk())
//                 .andExpect(view().name("tirage-date"));
//     }


//     // ===============================
//     // PAGE HIER
//     // ===============================

//     @Test
//     @DisplayName("Should return yesterday draw page")
//     void yesterdayPageShouldWork() throws Exception {

//         Mockito.when(detailService.getTirageByDate(any()))
//                 .thenReturn(Optional.empty());

//         mockMvc.perform(get("/resultat-loto-hier"))
//                 .andExpect(status().isOk());
//     }


//     // ===============================
//     // PAGE PROCHAIN TIRAGE
//     // ===============================

//     @Test
//     @DisplayName("Should return next draw page")
//     void nextDrawPageShouldWork() throws Exception {

//         Mockito.when(detailService.getTirageByDate(any()))
//                 .thenReturn(Optional.empty());

//         mockMvc.perform(get("/prochain-tirage-loto"))
//                 .andExpect(status().isOk());
//     }


// 	// ===============================
// 	// TEST : route SEO jour invalide
// 	// ===============================
// 	@Test
// 	@DisplayName("SEO route should return 404 when day slug invalid")
// 	void seoRouteInvalidDayShouldReturn404() throws Exception {

// 		mockMvc.perform(get("/tirage-loto-dimanche-2026-03-08"))
// 				.andExpect(status().isNotFound());
// 	}


// 	// ===============================
// 	// TEST : tirage valide mercredi
// 	// ===============================
// 	@Test
// 	@DisplayName("Should accept valid Wednesday draw")
// 	void shouldAcceptWednesdayDraw() throws Exception {

// 		Mockito.when(detailService.getTirageByDate("2026-03-04"))
// 				.thenReturn(Optional.empty());

// 		mockMvc.perform(get("/tirage/2026-03-04"))
// 				.andExpect(status().isOk());
// 	}


// 	// ===============================
// 	// TEST : tirage valide lundi
// 	// ===============================
// 	@Test
// 	@DisplayName("Should accept Monday draw")
// 	void shouldAcceptMondayDraw() throws Exception {

// 		Mockito.when(detailService.getTirageByDate("2026-03-02"))
// 				.thenReturn(Optional.empty());

// 		mockMvc.perform(get("/tirage/2026-03-02"))
// 				.andExpect(status().isOk());
// 	}


// 	// ===============================
// 	// TEST : route SEO avec mercredi
// 	// ===============================
// 	@Test
// 	@DisplayName("SEO route Wednesday should work")
// 	void seoRouteWednesdayShouldWork() throws Exception {

// 		Mockito.when(detailService.getTirageByDate("2026-03-04"))
// 				.thenReturn(Optional.empty());

// 		mockMvc.perform(get("/tirage-loto-mercredi-2026-03-04"))
// 				.andExpect(status().isOk());
// 	}

// }

package com.fdjloto.api.integration.controller;

import com.fdjloto.api.model.Historique20Detail;
import com.fdjloto.api.security.JwtAuthenticationFilter;
import com.fdjloto.api.service.Historique20DetailService;
import com.fdjloto.api.service.PredictionTirageService;
import com.fdjloto.api.service.UserServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
// @WebMvcTest(TirageParDateController.class) ✅
@AutoConfigureMockMvc(addFilters = false) // 🔥 IMPORTANT : désactive JWT
@ActiveProfiles("test")
// @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) // ✅ ICI
class TirageParDateControllerIT {

    @Autowired
    private MockMvc mockMvc;

    // 🔥 Mock des dépendances du controller
    @MockBean
    private Historique20DetailService detailService;

    @MockBean
    private PredictionTirageService predictionService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private UserServiceImpl userService;

    // ===============================
    // CAS OK : tirage existant
    // ===============================
    @Test
    @DisplayName("Should return draw page when draw exists")
    void shouldReturnDrawWhenExists() throws Exception {

        Historique20Detail detail = new Historique20Detail();

        Mockito.when(detailService.getTirageByDate("2026-03-04"))
                .thenReturn(Optional.of(detail));

        mockMvc.perform(get("/tirage/2026-03-04"))
                .andExpect(status().isOk())
                .andExpect(view().name("tirage-date"))
                .andExpect(model().attribute("details", detail))
                .andExpect(model().attribute("isPending", false));
    }

    // ===============================
    // CAS FUTUR (pending)
    // ===============================
    @Test
    @DisplayName("Should return pending page when draw not yet available")
    void shouldReturnPendingWhenNotInDatabase() throws Exception {

        Mockito.when(detailService.getTirageByDate("2026-03-07"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/tirage/2026-03-07"))
                .andExpect(status().isOk())
                .andExpect(view().name("tirage-date"))
                .andExpect(model().attribute("isPending", true));
    }

    // ===============================
    // FORMAT DATE INVALIDE
    // ===============================
    @Test
    @DisplayName("Should return 400 when date format invalid")
    void shouldReturnBadRequestWhenDateInvalid() throws Exception {

        mockMvc.perform(get("/tirage/invalid-date"))
                .andExpect(status().isBadRequest());
    }

    // ===============================
    // JOUR NON AUTORISÉ
    // ===============================
    @Test
    @DisplayName("Should return 404 when date is not a draw day")
    void shouldReturn404WhenNotDrawDay() throws Exception {

        mockMvc.perform(get("/tirage/2026-03-05")) // jeudi
                .andExpect(status().isNotFound());
    }

    // ===============================
    // ROUTE SEO RESULTAT
    // ===============================
    @Test
    @DisplayName("SEO route should work")
    void seoRouteShouldWork() throws Exception {

        Mockito.when(detailService.getTirageByDate("2026-03-04"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/resultat-loto-2026-03-04"))
                .andExpect(status().isOk());
    }

    // ===============================
    // ROUTE SEO AVEC JOUR
    // ===============================
    @Test
    @DisplayName("SEO route with day should work")
    void seoDayRouteShouldWork() throws Exception {

        Mockito.when(detailService.getTirageByDate("2026-03-07"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/tirage-loto-samedi-2026-03-07"))
                .andExpect(status().isOk());
    }

    // ===============================
    // PAGE AUJOURD'HUI
    // ===============================
    @Test
    @DisplayName("Should return today draw page")
    void todayPageShouldWork() throws Exception {

        Mockito.when(detailService.getTirageByDate(any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/resultat-loto-aujourdhui"))
                .andExpect(status().isOk());
    }

    // ===============================
    // PAGE HIER
    // ===============================
    @Test
    @DisplayName("Should return yesterday draw page")
    void yesterdayPageShouldWork() throws Exception {

        Mockito.when(detailService.getTirageByDate(any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/resultat-loto-hier"))
                .andExpect(status().isOk());
    }

    // ===============================
    // PAGE PROCHAIN TIRAGE
    // ===============================
    @Test
    @DisplayName("Should return next draw page")
    void nextDrawPageShouldWork() throws Exception {

        Mockito.when(detailService.getTirageByDate(any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/prochain-tirage-loto"))
                .andExpect(status().isOk());
    }

    // ===============================
    // SEO jour invalide
    // ===============================
    @Test
    @DisplayName("SEO route should return 404 when day slug invalid")
    void seoRouteInvalidDayShouldReturn404() throws Exception {

        mockMvc.perform(get("/tirage-loto-dimanche-2026-03-08"))
                .andExpect(status().isNotFound());
    }
}
