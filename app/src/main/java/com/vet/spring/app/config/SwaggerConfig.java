package com.vet.spring.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VetSaaS API - Sistema Multi-Tenant para Veterinarias")
                        .version("2.0.0")
                        .description("""
                                ## 🏥 Sistema SaaS de Gestión Veterinaria
                                
                                API RESTful para gestión multi-tenant de veterinarias con las siguientes características:
                                
                                ### 🔐 Autenticación
                                - **Super Admin**: Login en `/api/auth/super-admin/login` (gestiona todo el sistema)
                                - **Tenant (Veterinarias)**: Login en `/api/auth/tenant/login` (acceso a su espacio)
                                
                                ### 📋 Módulos Principales
                                - **Planes**: Gestión de planes de suscripción (Básico, Profesional, Empresarial, Enterprise)
                                - **Tenants**: Registro y administración de veterinarias
                                - **Usuarios**: Gestión de usuarios por tenant
                                - **Mascotas**: Registro y seguimiento de mascotas
                                - **Citas**: Programación y gestión de citas veterinarias
                                - **Historias Clínicas**: Expedientes médicos completos
                                - **Inventario**: Control de productos y stock
                                
                                ### 🚀 Para comenzar:
                                1. **Registrar una veterinaria**: `POST /api/public/tenants/register`
                                2. **Login**: Use el usuario creado en `/api/auth/tenant/login`
                                3. **Copiar el token JWT** del response
                                4. **Autorizar**: Click en el botón "Authorize" 🔓 arriba y pegar: `Bearer {tu-token}`
                                5. **Probar endpoints**: Ahora puede acceder a todos los endpoints protegidos
                                
                                ### 📌 Credenciales de Demo
                                - **Super Admin**: username=`superadmin`, password=`admin123`
                                - **Tenant Demo**: username=`admin`, password=`admin123`
                                """)
                        .contact(new Contact()
                                .name("ItamiDeveloper")
                                .email("support@appvet.com")
                                .url("https://github.com/ItamiDeveloper"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de Desarrollo Local"),
                        new Server()
                                .url("https://api.appvet.com")
                                .description("Servidor de Producción (Próximamente)")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingrese el token JWT obtenido del login. Formato: Bearer {token}")
                        ));
    }
}
