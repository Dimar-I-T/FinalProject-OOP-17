package com.dimar.floorislavabackend.service;

import com.dimar.floorislavabackend.dtos.LoginUserDto;
import com.dimar.floorislavabackend.dtos.RegisterUserDto;
import com.dimar.floorislavabackend.model.Player;
import com.dimar.floorislavabackend.repository.PlayerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final PlayerRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            PlayerRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Player signup(RegisterUserDto input) {
        Player user = new Player()
                .setUsername(input.getUsername())
                .setPasswordHash(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    public Player authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }
}
