package com.vet.spring.app.dto.clienteDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteDTO {
    private Integer idCliente;
    private Integer idVeterinaria;
    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String documento;
    private String telefono;
    private String email;
    private String direccion;
    private String estado;
}
