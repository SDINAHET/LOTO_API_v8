package com.fdjloto.api.controller;

import com.fdjloto.api.model.Tirage;
import com.fdjloto.api.service.OgService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/og")
public class OgImageController {

    private final OgService ogService;

    public OgImageController(OgService ogService) {
        this.ogService = ogService;
    }

    @GetMapping(value = "/tirage-{date}.png", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateOg(@PathVariable String date) {

        LocalDate localDate = LocalDate.parse(date);

        Optional<Tirage> tirageOpt = ogService.getTirage(localDate);

        BufferedImage image = new BufferedImage(1200, 630, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 🎨 Background
        g.setColor(new Color(15, 23, 42));
        g.fillRect(0, 0, 1200, 630);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 🧠 Title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Résultat Loto", 60, 100);

        g.setFont(new Font("Arial", Font.PLAIN, 28));
        g.drawString(localDate.toString(), 60, 150);

        if (tirageOpt.isPresent()) {

            Tirage t = tirageOpt.get();

            int[] nums = {
                t.getBoule1(),
                t.getBoule2(),
                t.getBoule3(),
                t.getBoule4(),
                t.getBoule5()
            };

            int x = 80;

            for (int n : nums) {
                drawBall(g, x, 250, String.valueOf(n), new Color(59, 130, 246));
                x += 120;
            }

            // ⭐ numéro chance
            drawBall(g, x + 40, 250, String.valueOf(t.getNumeroChance()), new Color(250, 204, 21));

        } else {
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            g.drawString("Tirage en attente...", 60, 300);
        }

        g.dispose();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                    .body(baos.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private void drawBall(Graphics2D g, int x, int y, String text, Color color) {

        g.setColor(color);
        g.fillOval(x, y, 80, 80);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));

        FontMetrics fm = g.getFontMetrics();
        int tx = x + (80 - fm.stringWidth(text)) / 2;
        int ty = y + ((80 - fm.getHeight()) / 2) + fm.getAscent();

        g.drawString(text, tx, ty);
    }
}
