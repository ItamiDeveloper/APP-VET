package com.vet.spring.app;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Standalone test to generate VERIFIED BCrypt hashes
 * Run this to get the EXACT hash for admin123 and vet123
 */
public class BCryptHashGenerator {

    @Test
    void generatePasswordHashes() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Generate hash for admin123
        String adminPassword = "admin123";
        String adminHash = encoder.encode(adminPassword);
        System.out.println("\n========================================");
        System.out.println("Password: " + adminPassword);
        System.out.println("BCrypt Hash: " + adminHash);
        System.out.println("Hash Length: " + adminHash.length());
        System.out.println("Verification: " + encoder.matches(adminPassword, adminHash));
        System.out.println("========================================\n");
        
        // Generate hash for vet123
        String vetPassword = "vet123";
        String vetHash = encoder.encode(vetPassword);
        System.out.println("========================================");
        System.out.println("Password: " + vetPassword);
        System.out.println("BCrypt Hash: " + vetHash);
        System.out.println("Hash Length: " + vetHash.length());
        System.out.println("Verification: " + encoder.matches(vetPassword, vetHash));
        System.out.println("========================================\n");
        
        // Test with the OLD hash from database
        String oldHash = "$2a$10$dXJ3SW6G7P370PKenZP82u3.LwdFP7D.CnK0MkLfJLBB3KPLfITLe";
        System.out.println("========================================");
        System.out.println("Testing OLD hash from database:");
        System.out.println("Old Hash: " + oldHash);
        System.out.println("Matches 'admin123': " + encoder.matches(adminPassword, oldHash));
        System.out.println("Matches 'vet123': " + encoder.matches(vetPassword, oldHash));
        System.out.println("Matches 'admin': " + encoder.matches("admin", oldHash));
        System.out.println("========================================\n");
        
        // Print SQL INSERT statements with new hashes
        System.out.println("========================================");
        System.out.println("SQL STATEMENTS TO USE:");
        System.out.println("========================================");
        System.out.println("DELETE FROM REFRESH_TOKEN WHERE id_usuario IN (1, 2);");
        System.out.println("DELETE FROM DOCTOR WHERE id_usuario = 2;");
        System.out.println("DELETE FROM USUARIO WHERE id_usuario IN (1, 2);");
        System.out.println("");
        System.out.println("INSERT INTO USUARIO (id_usuario, id_veterinaria, id_rol, username, password_hash, email, estado) VALUES");
        System.out.println("(1, 1, 1, 'admin', '" + adminHash + "', 'admin@vetdemo.com', 'ACTIVO');");
        System.out.println("");
        System.out.println("INSERT INTO USUARIO (id_usuario, id_veterinaria, id_rol, username, password_hash, email, estado) VALUES");
        System.out.println("(2, 1, 2, 'veterinario', '" + vetHash + "', 'veterinario@vetdemo.com', 'ACTIVO');");
        System.out.println("");
        System.out.println("INSERT INTO DOCTOR (id_doctor, id_veterinaria, id_usuario, nombres, apellidos, colegiatura, especialidad, estado) VALUES");
        System.out.println("(1, 1, 2, 'Juan Carlos', 'Pérez López', 'CMP12345', 'Medicina General', 'ACTIVO');");
        System.out.println("========================================");
    }
}
