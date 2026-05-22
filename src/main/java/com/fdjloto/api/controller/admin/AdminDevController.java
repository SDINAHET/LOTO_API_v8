package com.fdjloto.api.controller.admin;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/admin/dev")
public class AdminDevController {

    private final Path BASE_PATH = Paths.get(System.getProperty("user.dir"))
            .resolve("target/site/jacoco")
            .normalize();

    // =========================
    // 🔥 MAIN COVERAGE (FIXED)
    // =========================
    @GetMapping("/coverage")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getCoverage() throws IOException {

        Path file = BASE_PATH.resolve("index.html");

        if (!Files.exists(file)) {
            return ResponseEntity.notFound().build();
        }

        String html = Files.readString(file);

        // ✅ FIX CRITIQUE → base href
        html = html.replace("<head>",
                "<head><base href=\"/api/admin/dev/coverage/files/\">"
        );

        html = injectCss(html);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    // =========================
    // 🔥 FILE SERVER (inchangé)
    // =========================
    @GetMapping("/coverage/files/**")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCoverageFiles(HttpServletRequest request) throws IOException {

        String uri = request.getRequestURI();
        String path = uri.replace("/api/admin/dev/coverage/files/", "");

        Path file = BASE_PATH.resolve(path).normalize();

        // 🔐 sécurité anti traversal
        if (!file.startsWith(BASE_PATH)) {
            return ResponseEntity.badRequest().build();
        }

        // 🔥 résolution fichier
        if (!Files.exists(file)) {

            // fallback jacoco-resources
            Path fallback = BASE_PATH.resolve("jacoco-resources")
                    .resolve(path)
                    .normalize();

            if (Files.exists(fallback)) {
                file = fallback;
            } else {
                // fallback global
                try (Stream<Path> paths = Files.walk(BASE_PATH)) {
                    Optional<Path> found = paths
                            .filter(p -> p.getFileName().toString().equals(path))
                            .findFirst();

                    if (found.isPresent()) {
                        file = found.get();
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                }
            }
        }

        // =========================
        // 🔥 HTML (dark uniquement)
        // =========================
        if (file.toString().endsWith(".html")) {

            String html = Files.readString(file);

            html = injectCss(html); // ✅ plus de rewriteLinks

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }

        // =========================
        // 📁 STATIC FILES
        // =========================
        Resource resource = new UrlResource(file.toUri());

        String contentType = Files.probeContentType(file);

        return ResponseEntity.ok()
                .contentType(contentType != null
                        ? MediaType.parseMediaType(contentType)
                        : MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    // =========================
    // 📊 SUMMARY (inchangé)
    // =========================
    @GetMapping("/coverage/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getCoverageSummary() {

        try {
            Path file = BASE_PATH.resolve("index.html");

            if (!Files.exists(file)) {
                return Map.of("coverage", "N/A", "error", "JaCoCo report not found");
            }

            String content = Files.readString(file);

            int index = content.indexOf("Total");

            if (index == -1) {
                return Map.of("coverage", "N/A", "error", "Coverage not found");
            }

            String snippet = content.substring(index, Math.min(index + 200, content.length()));

            return Map.of("coverage", snippet);

        } catch (Exception e) {
            return Map.of("coverage", "N/A", "error", e.getMessage());
        }
    }

    // =========================
    // ⚡ PERFORMANCE
    // =========================
    @GetMapping("/coverage/performance")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getPerformance() {

        long start = System.currentTimeMillis();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long duration = System.currentTimeMillis() - start;

        return Map.of("responseTime", duration);
    }

    // =========================
    // 🎨 CSS GLOBAL
    // =========================
    private String injectCss(String html) {

        String css = """
            <style>

                body {
                    background:#0b1220 !important;
                    color:#e5e7eb !important;
                    font-family:Arial;
                }

                #header, .header, .report, .breadcrumb {
                    background:#0f172a !important;
                    color:#f9fafb !important;
                    border-bottom:1px solid #1f2937 !important;
                }

                h1, h2, h3, h4 {
                    color:#f9fafb !important;
                }

                table {
                    background:#111827 !important;
                    border-radius:10px;
                    overflow:hidden;
                }

                th {
                    background:#1f2937 !important;
                    color:#d1d5db !important;
                }

                td {
                    color:#e5e7eb !important;
                }

                tr:hover {
                    background:rgba(59,130,246,0.1);
                }

                a {
                    color:#60a5fa !important;
                }

                * {
                    background-color: inherit !important;
                }

            </style>
        """;

        return html.replace("</head>", css + "</head>");
    }
}
