package com.nivtech.petitecaisse.controller;

import com.nivtech.petitecaisse.controller.payload.*;
import com.nivtech.petitecaisse.domain.entity.AuthProvider;
import com.nivtech.petitecaisse.domain.entity.Role;
import com.nivtech.petitecaisse.domain.entity.User;
import com.nivtech.petitecaisse.exception.BadRequestException;
import com.nivtech.petitecaisse.repository.UserRepository;
import com.nivtech.petitecaisse.security.CurrentUser;
import com.nivtech.petitecaisse.security.TokenProvider;
import com.nivtech.petitecaisse.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/auth")
public class AuthController
{

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
            PasswordEncoder passwordEncoder, TokenProvider tokenProvider)
    {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
            @CurrentUser UserPrincipal userPrincipal)
    {
        if (userPrincipal != null)
        {
            return new ResponseEntity<>("Déconnectez-vous avant de vous authentifier", HttpStatus.FORBIDDEN);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createJwt(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest,
            @CurrentUser UserPrincipal userPrincipal)
    {
        if (userPrincipal != null)
        {
            return new ResponseEntity<>("Déconnectez-vous avant de vous authentifier", HttpStatus.FORBIDDEN);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail()))
        {
            throw new BadRequestException("Adresse email déjà utilisée");
        }

        // Creating user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (!userRepository.existsByRolesContains(Role.ROLE_ADMIN))
        {
            user.setRoles(List.of(Role.ROLE_ADMIN, Role.ROLE_USER));
        } else
        {
            user.setRoles(List.of(Role.ROLE_USER));
        }

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Utilisateur correctement ajouté"));
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePasswordUser(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
            @CurrentUser UserPrincipal userPrincipal)
    {
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), userPrincipal.getPassword()))
        {
            throw new BadRequestException("Le mot de passe n'est pas valide");
        }

        var user = userRepository.getOne(userPrincipal.getId());
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPasswordUser(@Valid @RequestBody String email)
    {
        return ResponseEntity.ok(new ApiResponse(true, "Email envoyé"));
    }

}
