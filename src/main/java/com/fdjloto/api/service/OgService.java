package com.fdjloto.api.service;

import com.fdjloto.api.model.Tirage;
import com.fdjloto.api.repository.TirageRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.fdjloto.api.exception.TirageNotFoundException;

import java.time.*;
import java.util.Date;
/**
 * Service dédié à la génération des données pour les images OG (Open Graph).
 * Permet de récupérer les tirages depuis MongoDB.
 */
@Service
public class OgService {

    private final TirageRepository tirageRepository;

    public OgService(TirageRepository tirageRepository) {
        this.tirageRepository = tirageRepository;
    }


    public Optional<Tirage> getTirage(LocalDate date) {

        if (date == null) return Optional.empty();

        ZoneId zone = ZoneId.of("Europe/Paris");

        ZonedDateTime start = date.atStartOfDay(zone);
        ZonedDateTime end = start.plusDays(1);

        return tirageRepository.findByDateBetween(
                Date.from(start.toInstant()),
                Date.from(end.toInstant())
        );
    }

    public Tirage getTirageOrThrow(LocalDate date) {

        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas être null");
        }

        ZoneId zone = ZoneId.of("Europe/Paris");

        ZonedDateTime start = date.atStartOfDay(zone);
        ZonedDateTime end = start.plusDays(1);

        return tirageRepository.findByDateBetween(
                Date.from(start.toInstant()),
                Date.from(end.toInstant())
        ).orElseThrow(() ->
            new TirageNotFoundException("Tirage introuvable pour la date : " + date)
        );
    }

    public Optional<Tirage> getLatestTirage() {
        return tirageRepository.findFirstByOrderByDateDeTirageDesc();
    }
}
