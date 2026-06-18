package com.fdjloto.api.controller;

import com.fdjloto.api.dto.LotoResultDTO;
import com.fdjloto.api.model.Historique6Result;
import com.fdjloto.api.service.Historique6Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * **Controller for retrieving historical lottery results.**
 */
@CrossOrigin(origins = "http://127.0.0.1:5500") // 🔥 Allows CORS for Live Server
// @CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "https://stephanedinahet.fr", "https://loto-tracker.fr"})
@RestController
@RequestMapping("/api/historique")
@Tag(name = "Lottery History", description = "Endpoints for retrieving historical lottery results.")
// @CrossOrigin(origins = "*") // Enables requests from the frontend
public class Historique6Controller {

    private final Historique6Service historique6Service;

    /**
     * **Constructor for injecting the Historique20Service dependency.**
     *
     * @param historique6Service Service handling historical lottery results.
     */
    public Historique6Controller(Historique6Service historique6Service) {
        this.historique6Service = historique6Service;
    }

    /**
     * **Retrieve the last 6 lottery results.**
     *
     * This endpoint fetches the most recent 20 lottery results from the database, formatted properly.
     *
     * @return **200 OK** - A list of the last 20 lottery results.
     */
    @Operation(summary = "Get last 6 lottery results", description = "Fetches the latest 20 lottery results with formatted dates.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the last 20 results."),
        @ApiResponse(responseCode = "500", description = "Internal server error while fetching results.")
    })
    @GetMapping("/last6")
    public List<LotoResultDTO> getLast6Results() {
        List<Historique6Result> results = historique6Service.getLast6Results();
        return results.stream()
                .map(result -> new LotoResultDTO(
                        result.getId(),
                        result.getDateDeTirage(), // ✅ Keeping Date, as @JsonFormat handles formatting
                        // result.getCombinaisonGagnante(),
                        result.getBoule1(),
                        result.getBoule2(),
                        result.getBoule3(),
                        result.getBoule4(),
                        result.getBoule5(),
                        result.getNumeroChance()))
                .toList(); // ✅ SonarLint recommends using `.toList()` instead of `Collectors.toList()`
    }
}
