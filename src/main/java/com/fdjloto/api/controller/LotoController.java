package com.fdjloto.api.controller;

import com.fdjloto.api.service.LotoScraperService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling lottery scraping operations.
 */
@RestController
@RequestMapping("/api/loto")
@Tag(name = "Lottery Scraper", description = "Endpoints for triggering the lottery web scraping process.")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class LotoController {

    @Autowired(required = false) // ✅ IMPORTANT
    private LotoScraperService lotoScraperService;

    /**
     * Endpoint to trigger the web scraping process for lottery results.
     */
    @Operation(summary = "Start lottery web scraping",
               description = "Triggers the scraping process to fetch lottery results from an external source.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Scraping successfully started.")
    })
    @GetMapping("/scrape")
    public String triggerScraping() {

        // ✅ Sécurité pour éviter crash en test
        if (lotoScraperService == null) {
            return "Scraper disabled in test environment.";
        }

        lotoScraperService.scrapeData();
        return "Scraping successfully started!";
    }
}
