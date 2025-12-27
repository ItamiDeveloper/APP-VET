package com.vet.spring.app.dto.tenantDto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para el registro de un nuevo tenant desde el landing page
 */
@Getter @Setter
public class TenantRegistroDTO {
    // Datos de la veterinaria
    private String codigoTenant;
    private String nombreComercial;
    private String razonSocial;
    private String ruc;
    private String telefono;
    private String emailContacto;
    private String direccion;
    private String ciudad;
    private String pais;
    
    // Plan seleccionado
    private Integer idPlan;
    
    // Datos del propietario/administrador
    private String nombrePropietario;
    private String apellidoPropietario;
    private String emailPropietario;
    private String telefonoPropietario;
    private String usernamePropietario;
    private String passwordPropietario;
}
