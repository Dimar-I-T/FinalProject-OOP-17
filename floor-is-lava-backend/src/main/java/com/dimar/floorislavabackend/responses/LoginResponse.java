package com.dimar.floorislavabackend.responses;

import com.dimar.floorislavabackend.model.Player;
import com.dimar.floorislavabackend.repository.PlayerRepository;

import java.util.Optional;
import java.util.UUID;

public class LoginResponse {
    private String token;
    private long expiresIn;

    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
}
