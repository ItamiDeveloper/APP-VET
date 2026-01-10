package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.estadisticasDto.*;
import com.vet.spring.app.entity.cita.Cita;
import com.vet.spring.app.entity.compra.Compra;
import com.vet.spring.app.entity.venta.Venta;
import com.vet.spring.app.repository.citaRepository.CitaRepository;
import com.vet.spring.app.repository.clienteRepository.ClienteRepository;
import com.vet.spring.app.repository.compraRepository.CompraRepository;
import com.vet.spring.app.repository.mascotaRepository.MascotaRepository;
import com.vet.spring.app.repository.ventaRepository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstadisticasService {

    private final ClienteRepository clienteRepository;
    private final MascotaRepository mascotaRepository;
    private final CitaRepository citaRepository;
    private final VentaRepository ventaRepository;
    private final CompraRepository compraRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats(Integer tenantId) {
        Long totalClientes = clienteRepository.findAll().stream()
                .filter(c -> c.getTenant().getIdTenant().equals(tenantId))
                .count();
        
        Long totalMascotas = mascotaRepository.findAll().stream()
                .filter(m -> m.getTenant().getIdTenant().equals(tenantId))
                .count();
        
        Long totalCitas = citaRepository.findAll().stream()
                .filter(c -> c.getTenant().getIdTenant().equals(tenantId))
                .count();
        
        // Calcular ingresos totales de las ventas
        Double totalVentas = ventaRepository.findAll().stream()
                .filter(v -> v.getTenant().getIdTenant().equals(tenantId))
                .map(Venta::getTotal)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
        
        // Calcular total de compras
        Double totalCompras = compraRepository.findAll().stream()
                .filter(c -> c.getTenant().getIdTenant().equals(tenantId))
                .map(Compra::getTotal)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
        
        // Ingresos netos = ventas - compras
        Double totalIngresos = totalVentas - totalCompras;
        
        return new DashboardStatsDTO(
                totalClientes,
                totalMascotas,
                totalCitas,
                totalIngresos
        );
    }

    @Transactional(readOnly = true)
    public List<IngresosMensualDTO> getIngresosMensuales(Integer tenantId) {
        LocalDateTime hace6Meses = LocalDateTime.now().minusMonths(6);
        List<Venta> ventas = ventaRepository.findAll().stream()
                .filter(v -> v.getTenant().getIdTenant().equals(tenantId))
                .filter(v -> v.getFecha() != null && v.getFecha().isAfter(hace6Meses))
                .collect(Collectors.toList());

        Map<String, Double> ingresosPorMes = new LinkedHashMap<>();
        
        // Inicializar últimos 6 meses con 0
        for (int i = 5; i >= 0; i--) {
            LocalDateTime mes = LocalDateTime.now().minusMonths(i);
            String nombreMes = mes.getMonth().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("es-ES"));
            ingresosPorMes.put(nombreMes, 0.0);
        }

        // Agregar ingresos reales
        for (Venta venta : ventas) {
            String mes = venta.getFecha().getMonth()
                    .getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("es-ES"));
            double ingresoActual = venta.getTotal() != null ? venta.getTotal().doubleValue() : 0.0;
            ingresosPorMes.merge(mes, ingresoActual, Double::sum);
        }

        return ingresosPorMes.entrySet().stream()
                .map(e -> new IngresosMensualDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CitasPorEstadoDTO> getCitasPorEstado(Integer tenantId) {
        List<Cita> citas = citaRepository.findAll().stream()
                .filter(c -> c.getTenant().getIdTenant().equals(tenantId))
                .collect(Collectors.toList());
        
        Map<String, Long> citasPorEstado = citas.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getEstado().toString(),
                        Collectors.counting()
                ));

        return citasPorEstado.entrySet().stream()
                .map(e -> new CitasPorEstadoDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MascotasDistribucionDTO> getMascotasDistribucion(Integer tenantId) {
        // Distribución de mascotas por cliente
        Map<String, Long> mascotasPorCliente = mascotaRepository.findAll().stream()
                .filter(m -> m.getTenant().getIdTenant().equals(tenantId))
                .collect(Collectors.groupingBy(
                        m -> m.getCliente().getNombres() + " " + m.getCliente().getApellidos(),
                        Collectors.counting()
                ));
        
        return mascotasPorCliente.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10) // Top 10 clientes con más mascotas
                .map(e -> new MascotasDistribucionDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ActividadRecienteDTO> getActividadReciente(Integer tenantId) {
        // Obtener últimas citas creadas como actividad reciente
        return citaRepository.findAll().stream()
                .filter(c -> c.getTenant().getIdTenant().equals(tenantId))
                .sorted(Comparator.comparing(Cita::getFechaCreacion, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(10)
                .map(cita -> {
                    String descripcion = String.format(
                            "Cita con %s para %s - %s",
                            cita.getDoctor().getNombres() + " " + cita.getDoctor().getApellidos(),
                            cita.getMascota().getNombre(),
                            cita.getEstado()
                    );
                    return new ActividadRecienteDTO(
                            "CITA",
                            descripcion,
                            cita.getFechaCreacion()
                    );
                })
                .collect(Collectors.toList());
    }
}
