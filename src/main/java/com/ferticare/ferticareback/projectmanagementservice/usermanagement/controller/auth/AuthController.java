package com.ferticare.ferticareback.projectmanagementservice.usermanagement.controller.auth;

import com.ferticare.ferticareback.common.response.ErrorResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.GoogleLoginRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.LoginRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.response.LoginResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.GoogleAuthService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final GoogleAuthService googleAuthService;

    public AuthController(UserService userService, GoogleAuthService googleAuthService) {
        this.userService = userService;
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse loginResponse = userService.authenticateUser(request);
            return ResponseEntity.ok(loginResponse);
        } catch (IllegalArgumentException ex) {
            ErrorResponse errorResponse = new ErrorResponse(
                    401,
                    "Unauthorized",
                    ex.getMessage(),
                    "/api/auth/login"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(
                            403,
                            "Forbidden",
                            ex.getMessage(),
                            "/api/auth/login"
                    ));
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(
                    500,
                    "Internal Server Error",
                    "An unexpected error occurred",
                    "/api/auth/login"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequest request) {
        try {
            LoginResponse loginResponse = googleAuthService.authenticateGoogleUser(request);
            return ResponseEntity.ok(loginResponse);
        } catch (IllegalArgumentException ex) {
            ErrorResponse errorResponse = new ErrorResponse(
                    401,
                    "Unauthorized",
                    ex.getMessage(),
                    "/api/auth/google-login"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(
                    500,
                    "Internal Server Error",
                    "Google authentication failed: " + ex.getMessage(),
                    "/api/auth/google-login"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}