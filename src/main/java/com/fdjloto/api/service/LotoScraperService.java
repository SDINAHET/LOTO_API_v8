package com.fdjloto.api.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



import java.io.*;
import java.nio.file.*;


import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fdjloto.api.model.LotoResult;
import com.fdjloto.api.repository.LotoRepository;
import com.fdjloto.api.service.GainCalculationService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.scheduling.annotation.EnableScheduling;


import org.springframework.context.annotation.Profile;

@Service
@Profile("!test") // 🔥 IMPORTANT pour les tests
public class LotoScraperService {

// @Profile("!test")
// @Service
// @SpringBootApplication
// @EnableScheduling // ✅ Active la planification
// public class LotoScraperService {

    // private static final String ZIP_URL = "https://www.sto.api.fdj.fr/anonymous/service-draw-info/v3/documentations/1a2b3c4d-9876-4562-b3fc-2c963f66afp6";
	private static final String ZIP_URL = "https://www.sto.api.fdj.fr/anonymous/service-draw-info/v3/documentations/1a2b3c4d-9876-4562-b3fc-2c963f66afp6";

    @Autowired
    private LotoRepository lotoRepository;

	@Autowired
	private GainCalculationService gainCalculationService;
	@Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE)
	public void initialScrape() {
		System.out.println("🚀 Lancement initial du scraping...");
		scrapeData();
	}

    // @Scheduled(fixedRate = 3600000)  // Exécution toutes les heures
	// @Scheduled(fixedDelay = 300000)  // Attendre 5 minutes après la fin de l'exécution précédente
	// @Scheduled(fixedRate = 300000)  // Exécution toutes les 5 minutes
	// ✅ Planification 4 fois par jour : 00h00, 06h00, 12h00, 18h00
	// @Scheduled(cron = "0 0 0,6,12,18 * * *", zone = "Europe/Paris")
	// ✅ Planification 2 fois par heure : 00 et 30 minutes de chaque heure
	@Scheduled(cron = "0 0,30 * * * *", zone = "Europe/Paris")
	public void scrapeDataScheduled() {
        System.out.println("⏰ Exécution planifiée du scraping des données...");
        scrapeData();
        long count = lotoRepository.count();
        if (count > 0) {
            System.out.println("✅ Scraping terminé avec succès. Nombre total d'entrées en base : " + count);
        } else {
            System.out.println("❌ Aucune donnée insérée. Vérifiez le fichier source.");
        }
    }

    public void scrapeData() {
        System.out.println("🟢 Démarrage du scraping...");

        System.out.println("🟡 Test de connexion à MongoDB...");
        try {
            lotoRepository.count();
            System.out.println("✅ Connexion MongoDB réussie !");
        } catch (Exception e) {
            System.err.println("🚨 Erreur de connexion MongoDB : " + e.getMessage());
            return;
        }

        try {
            System.out.println("🟡 Téléchargement du fichier ZIP...");
            URL url = new URL(ZIP_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.err.println("🚨 Erreur HTTP : " + connection.getResponseCode());
                return;
            }

            try (InputStream inputStream = connection.getInputStream();
                 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream)) {

                ZipArchiveEntry entry;
                while ((entry = zipInputStream.getNextZipEntry()) != null) {
                    if (entry.getName().endsWith(".csv")) {
                        System.out.println("✅ Fichier trouvé et extrait : " + entry.getName());
                        parseCSV(zipInputStream);
						System.out.println("✅ Nettoyages des données pour l'affichage depuis le fichier : " + entry.getName());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("🚨 Erreur lors du téléchargement : " + e.getMessage());
        }
    }

	public void saveCsvFile(String fileName, List<String[]> data) {
		String resourceDirectory = "src/main/resources/files/";
		Path filePath = Paths.get(resourceDirectory, fileName);

		// Vérifier et créer le dossier si nécessaire
		File directory = new File(resourceDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
			for (String[] row : data) {
				writer.write(String.join(";", row));
				writer.newLine();
			}
			System.out.println("✅ Fichier CSV sauvegardé avec succès dans : " + filePath.toAbsolutePath());
		} catch (IOException e) {
			System.err.println("❌ Erreur lors de l'écriture du fichier CSV : " + e.getMessage());
		}
	}


	// private int parseInteger(String value, int defaultValue) {

	public int parseInteger(String value, int defaultValue) {
		try {
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException | NullPointerException e) {
			// System.err.println("🚨 Erreur de conversion en entier : " + value);
			return defaultValue;
		}
	}



	// private double parseDouble(String value, double defaultValue) {
	public double parseDouble(String value, double defaultValue) {
		try {
			if (value == null || value.trim().isEmpty()) {
				return defaultValue;
			}
			return Double.parseDouble(value.replace(" ", "").replace(",", ".")); // Remplacement "," -> "." pour décimaux
		} catch (NumberFormatException e) {
			System.err.println("🚨 Erreur de conversion en double : " + value);
			return defaultValue;
		}
	}

    private void parseCSV(InputStream inputStream) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

			CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
			CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(csvParser).build();

			List<String[]> records = csvReader.readAll();
			for (String[] row : records) {
				if (row.length > 49) { // Ignore la dernière colonne si elle est vide
					row = Arrays.copyOf(row, 49);
				}
			}
			// Définir un nom de fichier unique basé sur la date
			String fileName = "loto_201911"+".csv";
			// String fileName = "loto_results_" + System.currentTimeMillis() + ".csv";
			saveCsvFile(fileName, records);  // Sauvegarde du fichier CSV dans resources/files


			System.out.println("🟡 Nombre total de lignes : " + records.size());

			if (records.isEmpty()) {
				System.out.println("🚨 Le fichier CSV est vide.");
				return;
			}

			System.out.println("🟡 Suppression des anciennes données MongoDB...");
			lotoRepository.deleteAll();

			if (records.get(0).length < 50) {
				System.err.println("🚨 Erreur : Le CSV contient " + records.get(0).length + " colonnes au lieu de 50.");
				return;
			}

			List<LotoResult> lotoResults = new ArrayList<>();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE); // ✅ Format correct
			for (int i = 1; i < records.size(); i++) {
				String[] row = records.get(i);

				if (row.length < 49) {
					System.err.println("🚨 Ligne " + i + " ignorée (trop courte, seulement " + row.length + " colonnes).");
					continue;
				}

				if (i <= 5) {
					System.out.println("🔹 Ligne " + i + " : " + Arrays.toString(row));
				}

				try {
                    LotoResult lotoResult = new LotoResult();

					lotoResult.setAnneeNumeroDeTirage(parseInteger(row[0], 0));
					lotoResult.setJourDeTirage(row[1]);

					lotoResult.setDateDeTirage(dateFormat.parse(row[2])); // Correction ici    string --> date

					lotoResult.setDateDeForclusion(row[3]);
					lotoResult.setBoule1(parseInteger(row[4], 0));
					lotoResult.setBoule2(parseInteger(row[5], 0));
					lotoResult.setBoule3(parseInteger(row[6], 0));
					lotoResult.setBoule4(parseInteger(row[7], 0));
					lotoResult.setBoule5(parseInteger(row[8], 0));
					lotoResult.setNumeroChance(parseInteger(row[9], 0));
					lotoResult.setCombinaisonGagnante(row[10]);
					lotoResult.setNombreDeGagnantAuRang1(parseInteger(row[11], 0));
					lotoResult.setRapportDuRang1(parseDouble(row[12], 0.0));

					// ✅ Extraction des rangs 2 à 9
					for (int j = 13, rank = 2; j <= 28 && j + 1 < row.length; j += 2, rank++) {
						try {
							int nombreGagnants = parseInteger(row[j], -1);
							double rapport = parseDouble(row[j + 1], -1.0);

							if (nombreGagnants == -1 || rapport == -1.0) {
								System.err.println("🚨 Erreur d'extraction du Rang " + rank + " (ligne CSV mal formatée).");
							} else {
								// Utilisation de la réflexion pour éviter les répétitions
								lotoResult.getClass().getMethod("setNombreDeGagnantAuRang" + rank, int.class)
										.invoke(lotoResult, nombreGagnants);
								lotoResult.getClass().getMethod("setRapportDuRang" + rank, double.class)
										.invoke(lotoResult, rapport);

								// 🔹 Debugging : Afficher les valeurs extraites pour vérifier
								// System.out.println("✅ Rang " + rank + " : " + nombreGagnants + " gagnants, rapport = " + rapport);
							}
						} catch (NoSuchMethodException e) {
							System.err.println("⚠️ Méthode setNombreDeGagnantAuRang" + rank + " non trouvée.");
						} catch (Exception e) {
							System.err.println("🚨 Exception lors de l'extraction du rang " + rank + " : " + e.getMessage());
						}
					}


					lotoResult.setNombreDeCodesGagnants(parseInteger(row[29], 0));
					lotoResult.setRapportCodesGagnants(parseInteger(row[30], 0));
					lotoResult.setCodesGagnants(row[31]);
					lotoResult.setBoule1SecondTirage(parseInteger(row[32], 0));
					lotoResult.setBoule2SecondTirage(parseInteger(row[33], 0));
					lotoResult.setBoule3SecondTirage(parseInteger(row[34], 0));
					lotoResult.setBoule4SecondTirage(parseInteger(row[35], 0));
					lotoResult.setBoule5SecondTirage(parseInteger(row[36], 0));

					lotoResult.setCombinaisonGagnanteSecondTirage(row[38]);
					lotoResult.setNombreDeGagnantAuRang1SecondTirage(parseInteger(row[39], 0));
					lotoResult.setRapportDuRang1SecondTirage(parseDouble(row[40], 0.0));

					// ✅ Second tirage : Rang 2 à Rang 4 (colonnes 41 à 46)
					lotoResult.setNombreDeGagnantAuRang2SecondTirage(parseInteger(row[41], 0));
					lotoResult.setRapportDuRang2SecondTirage(parseDouble(row[42], 0.0));

					lotoResult.setNombreDeGagnantAuRang3SecondTirage(parseInteger(row[43], 0));
					lotoResult.setRapportDuRang3SecondTirage(parseDouble(row[44], 0.0));

					lotoResult.setNombreDeGagnantAuRang4SecondTirage(parseInteger(row[45], 0));
					lotoResult.setRapportDuRang4SecondTirage(parseDouble(row[46], 0.0));

					lotoResult.setNumeroJokerplus(parseInteger(row[47], 0));

					// ✅ Vérification avant d'accéder à la colonne "Devise"
					if (row.length >= 49 && !row[48].isEmpty()) {
						lotoResult.setDevise(row[48]);  // Devise correcte si elle existe
					} else {
						lotoResult.setDevise("EUR");  // Valeur par défaut si absente
					}

					lotoResults.add(lotoResult);
                } catch (Exception e) {
                    System.err.println("🚨 Erreur parsing ligne " + i + " : " + e.getMessage());
                }
            }

            if (!lotoResults.isEmpty()) {
                lotoRepository.saveAll(lotoResults);
                System.out.println("✅ " + lotoResults.size() + " documents insérés dans MongoDB !");
            } else {
                System.out.println("🚨 Aucun document inséré !");
            }

			lotoRepository.saveAll(lotoResults);
			System.out.println("✅ " + lotoResults.size() + " documents insérés dans MongoDB !");

			// 🔥 recalcul immédiat des gains après MAJ des tirages
			gainCalculationService.calculerGains();
			System.out.println("✅ Gains recalculés après mise à jour des tirages.");


        } catch (IOException | CsvException e) {
            System.err.println("🚨 Erreur CSV : " + e.getMessage());
        }
    }
}
