package com.vet.spring.app.dto.tenantDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TenantDTO {
    private Integer idTenant;
    private String codigoTenant;
    private String nombreComercial;
    private String razonSocial;
    private String ruc;
    private String telefono;
    private String emailContacto;
    private String direccion;
    private String pais;
    private String ciudad;
    
    // Plan y suscripción
    private Integer idPlanActual;
    private String nombrePlan;
    private String estadoSuscripcion;
    
    // Propietario
    private String nombrePropietario;
    private String emailPropietario;
    private String telefonoPropietario;
    
    // Uso y límites
    private Integer usuariosActivos;
    private Integer doctoresActivos;
    private Integer mascotasRegistradas;
    private Integer almacenamientoUsadoMb;
    
    // Límites del plan
    private Integer maxUsuarios;
    private Integer maxDoctores;
    private Integer maxMascotas;
    private Integer maxAlmacenamientoMb;
    
    // Metadata
    private String logoUrl;
    private String colorPrimario;
    private String colorSecundario;
    private String estado;
}
