package com.fdjloto.api.controller;

import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/protected")
@Tag(name = "Protected Resources", description = "Endpoints that require authentication via JWT.")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ProtectedController {

    private final UserRepository userRepository;

    public ProtectedController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(
            summary = "Get authenticated user info",
            description = "Returns user details extracted from the JWT, including username and roles."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user information."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid or missing JWT token."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("/userinfo")
    public ResponseEntity<Map<String, Object>> getUserInfo(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or missing JWT token"));
        }

        String email = authentication.getName();

        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        User user = optUser.get();

        List<String> roles = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .toList();

        // ✅ "username" = prénom + nom (ou fallback)
        String displayName = (user.getFirstName() != null ? user.getFirstName() : "")
                + (user.getLastName() != null ? " " + user.getLastName() : "");
        displayName = displayName.trim();
        if (displayName.isBlank()) displayName = user.getEmail();

        return ResponseEntity.ok(Map.of(
                "token_valid", "✅",
                "message", "Bienvenue " + displayName,
                "username", displayName,          // 👈 affichage header
                "first_name", user.getFirstName(),
                "last_name", user.getLastName(),
                "email", user.getEmail(),
                "id", user.getId(),
                "roles", roles
        ));
    }
}
