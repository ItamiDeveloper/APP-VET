package com.vet.spring.app.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    
    public JwtResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }
}
