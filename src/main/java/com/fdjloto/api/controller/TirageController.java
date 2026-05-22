package com.fdjloto.api.controller;

import com.fdjloto.api.model.LotoResult;
import com.fdjloto.api.repository.LotoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Locale;
import java.util.Optional;

/**
 * **Controller for managing lottery draws (Tirages).**
 */
@RestController
@RequestMapping("/api/tirages")
@Tag(name = "Loto Draw Management", description = "Endpoints for retrieving available draw dates and searching draws by date range.")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class TirageController {

    private final LotoRepository lotoRepository;

    /**
     * ✅ Thread-safe formatter (Java moderne)
     */
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy", Locale.FRANCE);

    public TirageController(LotoRepository lotoRepository) {
        this.lotoRepository = lotoRepository;
    }

    /**
     * 🔹 Retrieves all available draw dates in descending order.
     */
    @Operation(summary = "Retrieve available draw dates", description = "Returns a sorted list of unique draw dates in descending order.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved available draw dates.")
    })
    @GetMapping("/dates")
    public List<String> getAvailableDates() {

        return lotoRepository.findAll().stream()
                .map(LotoResult::getDateDeTirage)
                .filter(date -> date != null)
                .distinct()
                .sorted((d1, d2) -> d2.compareTo(d1))
                .map(date -> date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .format(DATE_FORMAT))
                .collect(Collectors.toList());
    }

    /**
     * ✅ Retrieves lottery draws within a specific date range.
     */
    @Operation(summary = "Retrieve draws by date range", description = "Searches for lottery draws between the given start and end dates.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved draws for the given period."),
        @ApiResponse(responseCode = "400", description = "Invalid date format. Use 'dd/MM/yyyy'."),
        @ApiResponse(responseCode = "404", description = "No draws found for the given date range.")
    })
    @GetMapping
    public List<LotoResult> getTiragesParPeriode(
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate) {

        Optional<Date> lastTirageDate = lotoRepository.findAll().stream()
                .map(LotoResult::getDateDeTirage)
                .max(Date::compareTo);

        if (startDate == null && lastTirageDate.isPresent()) {
            startDate = lastTirageDate.get();
        }

        if (endDate != null && startDate != null && endDate.before(startDate)) {
            endDate = startDate;
        }

        return lotoRepository.findByDateDeTirageBetween(startDate, endDate);
    }
}
