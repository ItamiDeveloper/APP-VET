package com.vet.spring.app.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String username;
    private String tenantId;
    
    public JwtResponse(String token, String refreshToken, String username) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.username = username;
        this.tenantId = null;
    }
    
    public JwtResponse(String token, String refreshToken, String username, String tenantId) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.username = username;
        this.tenantId = tenantId;
    }
}
