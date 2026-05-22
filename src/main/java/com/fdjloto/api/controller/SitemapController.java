package com.fdjloto.api.controller;

import com.fdjloto.api.model.Historique20Detail;
import com.fdjloto.api.service.Historique20DetailService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Controller
public class SitemapController {

    private static final String BASE_URL = "https://loto-tracker.fr";
    private static final ZoneId PARIS = ZoneId.of("Europe/Paris");

    // ✅ Crawl budget : derniers tirages passés (DB)
    private static final int MAX_PAST_DRAWS_IN_SITEMAP = 120;

    // ✅ Tirages futurs à annoncer (calendrier) -> indexation en avance
    private static final int FUTURE_DRAWS_IN_SITEMAP = 60; // 30 ou 60 conseillé

    // ✅ Heure de fin "théorique" du tirage (après : on bascule au prochain tirage)
    private static final LocalTime DRAW_CUTOFF = LocalTime.of(20, 35);

    private final Historique20DetailService detailService;

    public SitemapController(Historique20DetailService detailService) {
        this.detailService = detailService;
    }

    /**
     * ✅ Sitemap index (à soumettre dans Google Search Console)
     */
    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemapIndex() {

        String lastmod = getLastDrawDateIsoOrToday();
        String today = LocalDate.now(PARIS).toString();

        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <sitemapindex xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
              <sitemap>
                <loc>%s/sitemap-pages.xml</loc>
                <lastmod>%s</lastmod>
              </sitemap>
              <sitemap>
                <loc>%s/sitemap-tirages.xml</loc>
                <lastmod>%s</lastmod>
              </sitemap>
              <sitemap>
                <loc>%s/sitemap-static.xml</loc>
                <lastmod>%s</lastmod>
              </sitemap>
            </sitemapindex>
            """.formatted(BASE_URL, lastmod, BASE_URL, lastmod, BASE_URL, today);
    }

    /**
     * ✅ Pages principales (SEO/navigation)
     */
    @GetMapping(value = "/sitemap-pages.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemapPages() {

        String lastmod = getLastDrawDateIsoOrToday();

        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
              <url>
                <loc>%s/</loc>
                <lastmod>%s</lastmod>
                <changefreq>daily</changefreq>
                <priority>1.0</priority>
              </url>

              <url>
                <loc>%s/dernier-tirage</loc>
                <lastmod>%s</lastmod>
                <changefreq>daily</changefreq>
                <priority>0.9</priority>
              </url>

              <!-- optionnel: page stable "aujourd'hui" -->
              <url>
                <loc>%s/resultat-loto-aujourdhui</loc>
                <lastmod>%s</lastmod>
                <changefreq>daily</changefreq>
                <priority>0.9</priority>
              </url>
            </urlset>
            """.formatted(BASE_URL, lastmod, BASE_URL, lastmod, BASE_URL, lastmod);
    }

    /**
     * ✅ Pages statiques (légales / info)
     */
    @GetMapping(value = "/sitemap-static.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemapStatic() {

        String lastmod = LocalDate.now(PARIS).toString();

        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
              <url>
                <loc>%s/politique_confidentialite.html</loc>
                <lastmod>%s</lastmod>
                <changefreq>yearly</changefreq>
                <priority>0.3</priority>
              </url>
            </urlset>
            """.formatted(BASE_URL, lastmod);
    }

    /**
     * ✅ Pages tirages : /tirage/yyyy-MM-dd
     * Contient :
     * - les derniers tirages en base (limite crawl budget)
     * - + les prochains tirages futurs (calendrier) pour indexation en avance
     *
     * ✅ Logique 20h35 :
     * - avant 20h35 : on part de "today"
     * - après 20h35 : on part de "today + 1" pour les futurs
     */
    @GetMapping(value = "/sitemap-tirages.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemapTirages() {

        LocalDate today = LocalDate.now(PARIS);
        LocalTime now = LocalTime.now(PARIS);

        // ✅ Après 20h35 : le prochain tirage à annoncer commence demain
        LocalDate futureStart = now.isAfter(DRAW_CUTOFF) ? today.plusDays(1) : today;

        StringBuilder sb = new StringBuilder(48_000);
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

        // =========================
        // 1) ✅ Tirages PASSÉS (DB)
        // =========================
        List<Historique20Detail> tirages = detailService.getAllTirages();

        // Trier récent -> ancien
        tirages.sort(Comparator.comparing(Historique20Detail::getDateDeTirage,
                Comparator.nullsLast(Comparator.naturalOrder())).reversed());

        int pastAdded = 0;

        for (Historique20Detail t : tirages) {

            if (t.getDateDeTirage() == null) continue;

            LocalDate ld = t.getDateDeTirage().toInstant().atZone(PARIS).toLocalDate();
            if (!isDrawDay(ld)) continue;

            String iso = ld.toString();
            long daysOld = ChronoUnit.DAYS.between(ld, today);

            String priority;
            String changefreq;

            if (daysOld <= 7) {
                priority = "0.9";
                changefreq = "daily";
            } else if (daysOld <= 30) {
                priority = "0.8";
                changefreq = "weekly";
            } else if (daysOld <= 365) {
                priority = "0.7";
                changefreq = "monthly";
            } else {
                priority = "0.6";
                changefreq = "yearly";
            }

            appendUrl(sb,
                    BASE_URL + "/tirage/" + iso,
                    iso,
                    changefreq,
                    priority
            );
            // ✅ AJOUT : pages SEO alias
            appendSeoAliasesForDate(sb, iso, iso, changefreq, priority);

            pastAdded++;
            if (pastAdded >= MAX_PAST_DRAWS_IN_SITEMAP) break;
        }

        // =========================
        // 2) ✅ Tirages FUTURS (calendrier)
        // =========================
        int futureAdded = 0;
        LocalDate d = futureStart;

        while (futureAdded < FUTURE_DRAWS_IN_SITEMAP) {
            d = d.plusDays(1);
            if (!isDrawDay(d)) continue;

            String iso = d.toString();

            // ✅ "pending" : Google peut repasser souvent
            // lastmod = today (le contenu peut changer jusqu'au tirage)
            appendUrl(sb,
                    BASE_URL + "/tirage/" + iso,
                    today.toString(),
                    "daily",
                    "0.8"
            );
            // ✅ AJOUT : pages SEO alias (future)
            appendSeoAliasesForDate(sb, iso, today.toString(), "daily", "0.8");

            futureAdded++;
        }

        sb.append("</urlset>");
        return sb.toString();
    }

    // -------------------------
    // Helpers
    // -------------------------

    private boolean isDrawDay(LocalDate ld) {
        DayOfWeek day = ld.getDayOfWeek();
        return day == DayOfWeek.MONDAY || day == DayOfWeek.WEDNESDAY || day == DayOfWeek.SATURDAY;
    }


    private String daySlugFr(LocalDate d) {
        // slugs simples qui rankent bien
        return switch (d.getDayOfWeek()) {
            case MONDAY -> "lundi";
            case WEDNESDAY -> "mercredi";
            case SATURDAY -> "samedi";
            default -> "jour";
        };
    }

    private void appendSeoAliasesForDate(StringBuilder sb, String isoDate, String lastmod, String changefreq, String priority) {

        // ✅ Alias 1 : "resultat loto yyyy-mm-dd"
        appendUrl(sb,
                BASE_URL + "/resultat-loto-" + isoDate,
                lastmod,
                changefreq,
                priority
        );

        // ✅ Alias 2 (optionnel) : "tirage loto samedi yyyy-mm-dd"
        String slug = daySlugFr(LocalDate.parse(isoDate));
        appendUrl(sb,
                BASE_URL + "/tirage-loto-" + slug + "-" + isoDate,
                lastmod,
                changefreq,
                priority
        );
    }


    private void appendUrl(StringBuilder sb, String loc, String lastmod, String changefreq, String priority) {
        sb.append("<url>");
        sb.append("<loc>").append(loc).append("</loc>");
        sb.append("<lastmod>").append(lastmod).append("</lastmod>");
        sb.append("<changefreq>").append(changefreq).append("</changefreq>");
        sb.append("<priority>").append(priority).append("</priority>");
        sb.append("</url>");
    }

    /**
     * ✅ Dernier tirage dispo (ISO yyyy-MM-dd) sinon today
     */
    private String getLastDrawDateIsoOrToday() {

        try {
            List<Historique20Detail> tirages = detailService.getAllTirages();
            if (tirages == null || tirages.isEmpty()) return LocalDate.now(PARIS).toString();

            tirages.sort(Comparator.comparing(Historique20Detail::getDateDeTirage,
                    Comparator.nullsLast(Comparator.naturalOrder())).reversed());

            for (Historique20Detail t : tirages) {
                if (t.getDateDeTirage() == null) continue;
                LocalDate ld = t.getDateDeTirage().toInstant().atZone(PARIS).toLocalDate();
                if (isDrawDay(ld)) return ld.toString();
            }

            return LocalDate.now(PARIS).toString();
        } catch (Exception e) {
            return LocalDate.now(PARIS).toString();
        }
    }
}
