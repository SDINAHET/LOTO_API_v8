package com.fdjloto.api.controller.admin;

import com.fdjloto.api.repository.UserRepository;
import com.fdjloto.api.repository.TicketRepository;
import com.fdjloto.api.repository.Historique6Repository;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardSummaryController {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final Historique6Repository historique6Repository;

    public AdminDashboardSummaryController(
            UserRepository userRepository,
            TicketRepository ticketRepository,
            Historique6Repository historique6Repository
    ) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.historique6Repository = historique6Repository;
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        return Map.of(
                "users", userRepository.count(),
                "tickets", ticketRepository.count(),
                "tirages", historique6Repository.count(),
                "errors24h", 0
        );
    }

    @GetMapping("/services")
    public Map<String, Object> services() {
        return Map.of(
                "api", "En ligne",
                "postgres", "En ligne",
                "mongo", "En ligne",
                "swagger", "Disponible",
                "security", "Active"
        );
    }

	@GetMapping("/resources")
	public Map<String, Object> resources() {
		Runtime runtime = Runtime.getRuntime();

		long maxMemory = runtime.maxMemory();
		long usedMemory = runtime.totalMemory() - runtime.freeMemory();

		int memoryPercent = maxMemory > 0
				? (int) ((usedMemory * 100) / maxMemory)
				: 0;

		File root = new File("/");
		long totalDisk = root.getTotalSpace();
		long freeDisk = root.getFreeSpace();

		int diskPercent = totalDisk > 0
				? (int) (((totalDisk - freeDisk) * 100) / totalDisk)
				: 0;

		return Map.of(
				"cpu", -1,
				"memory", memoryPercent,
				"disk", diskPercent
		);
	}

    // @GetMapping("/activity")
    // public Map<String, Object> activity() {
    //     LocalDate today = LocalDate.now();
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

    //     List<String> labels = new ArrayList<>();
    //     List<Integer> values = new ArrayList<>();

    //     for (int i = 6; i >= 0; i--) {
    //         LocalDate day = today.minusDays(i);
    //         labels.add(day.format(formatter));

    //         // V1 provisoire
    //         values.add(0);
    //     }

    //     return Map.of(
    //             "label", "Tickets enregistrés",
    //             "labels", labels,
    //             "values", values
    //     );
    // }

    @GetMapping("/activity")
    public Map<String, Object> activity() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        List<String> labels = new ArrayList<>();
        List<Long> values = new ArrayList<>();

        for (int i = 30; i >= 0; i--) { /** boucle 30 dernier jours */
            LocalDate day = today.minusDays(i);

            LocalDateTime start = day.atStartOfDay();
            LocalDateTime end = day.plusDays(1).atStartOfDay();

            labels.add(day.format(formatter));

            long count = ticketRepository.countByCreatedAtBetween(start, end);
            values.add(count);
        }

        return Map.of(
                "label", "Tickets créés",
                "labels", labels,
                "values", values
        );
    }

    // @GetMapping("/users-distribution")
    // public Map<String, Object> usersDistribution() {
    //     long total = userRepository.count();

    //     return Map.of(
    //             "active", total,
    //             "inactive", 0,
    //             "newUsers", 0
    //     );
    // }
    // @GetMapping("/users-distribution")
    // public Map<String, Object> usersDistribution() {
    //     long total = userRepository.count();

    //     long deletedByEmail =
    //             userRepository.countByEmailStartingWithIgnoreCase("deleted_");

    //     long deletedByFirstName =
    //             userRepository.countByFirstNameIgnoreCase("DELETED");

    //     long inactive = Math.max(deletedByEmail, deletedByFirstName);
    //     long active = Math.max(0, total - inactive);

    //     return Map.of(
    //             "active", active,
    //             "inactive", inactive,
    //             "newUsers", 0
    //     );
    // }
    @GetMapping("/users-distribution")
    public Map<String, Object> usersDistribution() {
        long total = userRepository.count();

        long inactive = Math.max(
                userRepository.countByEmailStartingWithIgnoreCase("deleted_"),
                userRepository.countByFirstNameIgnoreCase("DELETED")
        );

        long newUsers = userRepository.countByCreatedAtAfter(
                LocalDate.now().minusDays(30).atStartOfDay()
        );

        long active = Math.max(0, total - inactive);

        return Map.of(
                "active", active,
                "inactive", inactive,
                "newUsers", newUsers
        );
    }

    // @GetMapping("/logs-preview")
    // public Map<String, Object> logsPreview() {
    //     return Map.of(
    //             "logs", new String[]{
    //                     "INFO  AdminService - Dashboard chargé",
    //                     "INFO  TicketService - Tickets disponibles",
    //                     "INFO  LotoService - Résultats MongoDB accessibles",
    //                     "INFO  SecurityService - Accès administrateur validé"
    //             }
    //     );
    // }
    @GetMapping("/logs-preview")
    public Map<String, Object> logsPreview() {
        Path logPath = Path.of("logs/loto-tracker.log");

        try {
            if (!Files.exists(logPath)) {
                return Map.of(
                        "logs",
                        List.of("INFO AdminDashboard - Aucun fichier de log trouvé")
                );
            }

            List<String> allLines = Files.readAllLines(logPath);
            Collections.reverse(allLines);

            List<String> lastLines = allLines.stream()
                    .limit(8)
                    .toList();

            return Map.of("logs", lastLines);

        } catch (IOException e) {
            return Map.of(
                    "logs",
                    List.of("ERROR AdminDashboard - Impossible de lire les logs")
            );
        }
    }
}
