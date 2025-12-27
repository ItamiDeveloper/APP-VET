package com.vet.spring.app.dto.clienteDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteDTO {
    private Integer idCliente;
    private Integer idTenant;
    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String numeroDocumento;
    private String telefono;
    private String email;
    private String direccion;
    private String estado;
}
