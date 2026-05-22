// package com.fdjloto.api.controller.admin;

// import org.junit.jupiter.api.Test;

// import com.fdjloto.api.repository.TirageRepository;

// import static org.junit.jupiter.api.Assertions.*;

// class AdminUserStatsControllerLoadTest {
// @Test void classLoads_01() {
//             assertDoesNotThrow(() -> Class.forName("com.fdjloto.api.controller.admin.AdminUserStatsController"));
//         }
// @Test void classLoads_02() {
//             assertDoesNotThrow(() -> Class.forName("com.fdjloto.api.controller.admin.AdminUserStatsController"));
//         }
// @Test void classLoads_03() {
//             assertDoesNotThrow(() -> Class.forName("com.fdjloto.api.controller.admin.AdminUserStatsController"));
//         }
// @Test void classLoads_04() {
//             assertDoesNotThrow(() -> Class.forName("com.fdjloto.api.controller.admin.AdminUserStatsController"));
//         }
// @Test void classLoads_05() {
//             assertDoesNotThrow(() -> Class.forName("com.fdjloto.api.controller.admin.AdminUserStatsController"));
//         }
// @Test void classLoads_06() {
//             assertDoesNotThrow(() -> Class.forName("com.fdjloto.api.controller.admin.AdminUserStatsController"));
//         }
// @Test void classLoads_07() {
//             assertDoesNotThrow(() -> Class.forName("com.fdjloto.api.controller.admin.AdminUserStatsController"));
//         }
// @Test void classLoads_08() {
//             assertDoesNotThrow(() -> Class.forName("com.fdjloto.api.controller.admin.AdminUserStatsController"));
//         }
// @Test void classLoads_09() {
//             assertDoesNotThrow(() -> Class.forName("com.fdjloto.api.controller.admin.AdminUserStatsController"));
//         }
// @Test void classLoads_10() {
//             assertDoesNotThrow(() -> Class.forName("com.fdjloto.api.controller.admin.AdminUserStatsController"));
//         }
// }

package com.fdjloto.api.controller.admin;

import com.fdjloto.api.repository.TirageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AdminUserStatsControllerContextTest {

    @MockBean
    private TirageRepository tirageRepository;

    @Test
    void contextLoads() {
        // ✔ Spring démarre sans erreur
    }
}
