package com.vet.spring.app.dto.ventaDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VentaDTO {
    private Integer idVenta;
    private Integer idTenant;
    private Integer idCliente;
    private String clienteNombre; // Full name of cliente
    private LocalDateTime fecha;
    private BigDecimal total;
    private String metodoPago;
    private String estado;
    private List<DetalleVentaDTO> detalles;
}
