package com.meditrak.service;

import com.meditrak.dto.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public boolean authenticate(LoginRequest loginRequest) {
        return "admin".equalsIgnoreCase(loginRequest.getUsername())
            && "meditrak123".equals(loginRequest.getPassword());
    }
}
