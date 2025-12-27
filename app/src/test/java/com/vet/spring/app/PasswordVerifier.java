package com.vet.spring.app;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordVerifier {

    @Test
    void verifyPasswords() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String password = "admin123";
        String hashFromDB = "$2a$10$dXJ3SW6G7P370PKenZP82u3.LwdFP7D.CnK0MkLfJLBB3KPLfITLe";
        
        System.out.println("\n=== PASSWORD VERIFICATION ===");
        System.out.println("Password: " + password);
        System.out.println("Hash from DB: " + hashFromDB);
        System.out.println("Hash length: " + hashFromDB.length());
        System.out.println("Matches: " + encoder.matches(password, hashFromDB));
        
        // Generate a NEW hash
        String newHash = encoder.encode(password);
        System.out.println("\n=== NEW HASH GENERATED ===");
        System.out.println("New Hash: " + newHash);
        System.out.println("New Hash Matches: " + encoder.matches(password, newHash));
        
        System.out.println("\n=== SQL UPDATE ===");
        System.out.println("UPDATE super_admin SET password_hash = '" + newHash + "' WHERE username = 'superadmin';");
        System.out.println("UPDATE usuario SET password_hash = '" + newHash + "' WHERE username = 'admin';");
        System.out.println("=============================\n");
    }
}
