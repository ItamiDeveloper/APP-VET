package com.vet.spring.app.dto.compraDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompraDTO {
    private Integer idCompra;
    @NotNull(message = "El campo 'idTenant' no puede ser nulo")
    private Integer idTenant;
    @NotNull(message = "El campo 'idProveedor' no puede ser nulo")
    private Integer idProveedor;
    @NotNull(message = "El campo 'fecha' no puede ser nulo")
    private LocalDateTime fecha;
    @NotNull(message = "El campo 'total' no puede ser nulo")
    private BigDecimal total;
    @NotNull(message = "El campo 'estado' no puede ser nulo")
    @Size(min = 1, max = 50, message = "El campo 'estado' debe tener entre 1 y 50 caracteres")
    private String estado;
}
