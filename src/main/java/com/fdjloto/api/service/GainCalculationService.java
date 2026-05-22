package com.fdjloto.api.service;

import com.fdjloto.api.model.Ticket;
import com.fdjloto.api.model.LotoResult;
import com.fdjloto.api.dto.GainResultDTO;
import com.fdjloto.api.repository.TicketRepository;
import com.fdjloto.api.repository.LotoRepository;
import com.fdjloto.api.repository.TicketGainRepository;
import com.fdjloto.api.model.TicketGain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fdjloto.api.repository.TicketGainRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.time.*;

@Service
public class GainCalculationService {

    private final TicketRepository ticketRepository;
    private final LotoRepository lotoRepository;
    // private final TicketGainRepository ticketGainRepository;

    private final TicketGainRepository ticketGainRepository;
    private final TicketGainService ticketGainService; // 🔥 Injection du service pour éviter `this.` dans @Transactional

    public GainCalculationService(TicketRepository ticketRepository, LotoRepository lotoRepository, TicketGainRepository ticketGainRepository, TicketGainService ticketGainService) {
        this.ticketRepository = ticketRepository;
        this.lotoRepository = lotoRepository;
        this.ticketGainRepository = ticketGainRepository;
        this.ticketGainService = ticketGainService;
    }

    private void clearGainForTicket(String ticketId) {
        ticketGainRepository.findByTicketId(ticketId)
                .ifPresent(ticketGainRepository::delete);
    }


// ✅ Planification : Exécution toutes les 30 minutes
// @Scheduled(cron = "0 */30 * * * *", zone = "Europe/Paris")
@Scheduled(cron = "*/30 * * * * *", zone = "Europe/Paris")

    /**
     * 🔥 Calcule les gains pour tous les tickets et enregistre les résultats.
     */
    @Transactional
    public List<GainResultDTO> calculerGains() {
        List<Ticket> tickets = ticketRepository.findAll();
        List<LotoResult> resultatsLoto = lotoRepository.findAll();

        return tickets.stream()
            .map(ticket -> {
                Optional<LotoResult> tirageOptionnel = lotoRepository.findByDateDeTirage(ticket.getDrawDate());

                if (tirageOptionnel.isPresent()) {
                    LotoResult tirageCorrespondant = tirageOptionnel.get();
                    int nbCorrespondances = compareNumbers(ticket, tirageCorrespondant);
                    boolean chanceMatch = ticket.getChanceNumber() == tirageCorrespondant.getNumeroChance();
                    double gain = getGainAmount(nbCorrespondances, chanceMatch, tirageCorrespondant);

                    saveOrUpdateGain(ticket, nbCorrespondances, chanceMatch, gain);

                    return new GainResultDTO(ticket.getId(), nbCorrespondances, chanceMatch, gain);
                }

                // ✅ IMPORTANT : si pas de tirage pour cette date => on supprime le gain existant
                clearGainForTicket(ticket.getId());

                return new GainResultDTO(ticket.getId(), 0, false, 0.0);
            })
            .toList();

    }

    /**
     * 🔍 Compare les numéros d'un ticket avec ceux du tirage officiel.
     */
    private int compareNumbers(Ticket ticket, LotoResult lotoResult) {
        List<Integer> ticketNumbers = convertStringToList(ticket.getNumbers());
        List<Integer> winningNumbers = extractWinningNumbers(lotoResult);
        return (int) ticketNumbers.stream().filter(winningNumbers::contains).count();
    }

    /**
     * 💰 Détermine le montant du gain en fonction du nombre de correspondances.
     */
    private double getGainAmount(int correspondances, boolean chanceMatch, LotoResult lotoResult) {
        if (correspondances == 5 && chanceMatch) return lotoResult.getRapportDuRang1();
        if (correspondances == 5) return lotoResult.getRapportDuRang2();
        if (correspondances == 4 && chanceMatch) return lotoResult.getRapportDuRang3();
        if (correspondances == 4) return lotoResult.getRapportDuRang4();
        if (correspondances == 3 && chanceMatch) return lotoResult.getRapportDuRang5();
        if (correspondances == 3) return lotoResult.getRapportDuRang6();
        if (correspondances == 2 && chanceMatch) return lotoResult.getRapportDuRang7();
        if (correspondances == 2) return lotoResult.getRapportDuRang8();
        if (correspondances == 0 && chanceMatch) return lotoResult.getRapportDuRang9();
        // ✅ Rang 10: 1 numéro + chance = 2.20 €
        if (correspondances == 1 && chanceMatch) return 2.20;
        return 0.0;
    }

    /**
     * 📌 Enregistre ou met à jour les gains dans la base SQLite.
     */
    // @Transactional
    private void saveOrUpdateGain(Ticket ticket, int matchingNumbers, boolean luckyNumberMatch, double gainAmount) {
        // Optional<TicketGain> existingGain = ticketGainRepository.findById(ticket.getId());
        Optional<TicketGain> existingGain = ticketGainRepository.findByTicketId(ticket.getId());


        if (existingGain.isPresent()) {
            TicketGain ticketGain = existingGain.get();
            ticketGain.setMatchingNumbers(matchingNumbers);
            ticketGain.setLuckyNumberMatch(luckyNumberMatch);
            ticketGain.setGainAmount(gainAmount);
            ticketGainRepository.save(ticketGain);
        } else {
            TicketGain newTicketGain = new TicketGain(ticket.getId(), ticket, matchingNumbers, luckyNumberMatch, gainAmount);
            ticketGainRepository.save(newTicketGain);
        }
    }

    /**
     * ✅ Convertit une chaîne de numéros en liste d'entiers.
     */
    private List<Integer> convertStringToList(String numbers) {
        return Arrays.stream(numbers.split("-"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Extrait les numéros gagnants d'un tirage officiel.
     */
    private List<Integer> extractWinningNumbers(LotoResult lotoResult) {
        return List.of(
                lotoResult.getBoule1(),
                lotoResult.getBoule2(),
                lotoResult.getBoule3(),
                lotoResult.getBoule4(),
                lotoResult.getBoule5()
        );
    }
}
