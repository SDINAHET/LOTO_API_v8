package com.fdjloto.api.controller.admin;

import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<User>> list() {
        return ResponseEntity.ok(userRepository.findAllByOrderByCreatedAtDesc());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable String id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("User not found with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User payload) {

        payload.setId(null);

        if (payload.getPassword() != null && !payload.getPassword().isBlank()) {
            payload.setPassword(passwordEncoder.encode(payload.getPassword()));
        }

        User saved = userRepository.save(payload);
        logger.info("User created with id: {}", saved.getId());

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable String id,
                                       @RequestBody Map<String, Object> patch) {

        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) {
            logger.warn("Update failed - user not found: {}", id);
            return ResponseEntity.notFound().build();
        }

        User existing = opt.get();

        if (patch.containsKey("firstName"))
            existing.setFirstName((String) patch.get("firstName"));

        if (patch.containsKey("lastName"))
            existing.setLastName((String) patch.get("lastName"));

        if (patch.containsKey("email"))
            existing.setEmail((String) patch.get("email"));

        if (patch.containsKey("admin") && patch.get("admin") instanceof Boolean b)
            existing.setAdmin(b);

        if (patch.containsKey("password")) {
            String raw = (String) patch.get("password");
            if (raw != null && !raw.isBlank()) {
                existing.setPassword(passwordEncoder.encode(raw));
            }
        }

        User saved = userRepository.save(existing);
        logger.info("User updated: {}", id);

        return ResponseEntity.ok(saved);
    }

    /**
     * Soft delete sécurisé :
     * - anonymisation
     * - suppression des privilèges
     * - mot de passe invalide
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable String id) {

        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) {
            logger.warn("Delete failed - user not found: {}", id);
            return ResponseEntity.notFound().build();
        }

        User u = opt.get();

        // 🔒 anonymisation
        u.setFirstName("DELETED");
        u.setLastName("USER");
        u.setEmail("deleted_" + u.getId() + "@example.com");

        // 🔒 invalider le mot de passe (sécurisé pour Sonar)
        String randomPassword = generateTempPassword(32);
        u.setPassword(passwordEncoder.encode(randomPassword));

        // 🔒 retirer les droits admin
        u.setAdmin(false);

        userRepository.save(u);

        logger.info("User soft deleted: {}", id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable String id,
                                           @RequestBody(required = false) Map<String, Object> body) {

        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) {
            logger.warn("Reset password failed - user not found: {}", id);
            return ResponseEntity.notFound().build();
        }

        User u = opt.get();

        String newPassword = null;

        if (body != null && body.get("newPassword") != null) {
            newPassword = String.valueOf(body.get("newPassword")).trim();
            if (newPassword.isBlank()) newPassword = null;
        }

        boolean generated = false;

        if (newPassword == null) {
            newPassword = generateTempPassword(14);
            generated = true;
        }

        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);

        logger.info("Password reset for user: {}", id);

        if (generated) {
            return ResponseEntity.ok(Map.of(
                    "message", "Mot de passe réinitialisé",
                    "temporaryPassword", newPassword
            ));
        }

        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé"));
    }

    private static String generateTempPassword(int len) {
        final String alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789@$!%*?";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
        }
        return sb.toString();
    }
}
