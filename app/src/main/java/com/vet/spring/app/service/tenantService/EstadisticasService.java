package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.estadisticasDto.*;
import com.vet.spring.app.entity.cita.Cita;
import com.vet.spring.app.entity.venta.Venta;
import com.vet.spring.app.repository.CitaRepository;
import com.vet.spring.app.repository.ClienteRepository;
import com.vet.spring.app.repository.MascotaRepository;
import com.vet.spring.app.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Double totalIngresos = ventaRepository.findAll().stream()
                .filter(v -> v.getTenant().getIdTenant().equals(tenantId))
                .mapToDouble(Venta::getTotal)
                .sum();
        
        return new DashboardStatsDTO(
                totalClientes,
                totalMascotas,
                totalCitas,
                totalIngresos,
                1L, // Total veterinarias (para tenant es 1)
                0L  // Total planes (no aplicable para tenant)
        );
    }

    @Transactional(readOnly = true)
    public List<IngresosMensualDTO> getIngresosMensuales(Integer tenantId) {
        LocalDateTime hace6Meses = LocalDateTime.now().minusMonths(6);
        List<Venta> ventas = ventaRepository.findAll().stream()
                .filter(v -> v.getTenant().getIdTenant().equals(tenantId))
                .filter(v -> v.getFechaVenta().isAfter(hace6Meses))
                .collect(Collectors.toList());

        Map<String, Double> ingresosPorMes = new LinkedHashMap<>();
        
        // Inicializar últimos 6 meses con 0
        for (int i = 5; i >= 0; i--) {
            LocalDateTime mes = LocalDateTime.now().minusMonths(i);
            String nombreMes = mes.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            ingresosPorMes.put(nombreMes, 0.0);
        }

        // Agregar ingresos reales
        for (Venta venta : ventas) {
            String mes = venta.getFechaVenta().getMonth()
                    .getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            ingresosPorMes.merge(mes, venta.getTotal(), Double::sum);
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
        return mascotaRepository.findAll().stream()
                .filter(m -> m.getTenant().getIdTenant().equals(tenantId))
                .collect(Collectors.groupingBy(
                        m -> m.getCliente().getNombre() + " " + m.getCliente().getApellido(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .limit(10) // Top 10 clientes con más mascotas
                .map(e -> new MascotasDistribucionDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(MascotasDistribucionDTO::getValue).reversed())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ActividadRecienteDTO> getActividadReciente(Integer tenantId) {
        // Obtener últimas citas creadas como actividad reciente
        return citaRepository.findAll().stream()
                .filter(c -> c.getTenant().getIdTenant().equals(tenantId))
                .sorted(Comparator.comparing(Cita::getCreatedAt).reversed())
                .limit(10)
                .map(cita -> {
                    String descripcion = String.format(
                            "Cita con %s para %s - %s",
                            cita.getDoctor().getNombre(),
                            cita.getMascota().getNombre(),
                            cita.getEstado()
                    );
                    return new ActividadRecienteDTO(
                            cita.getId().longValue(),
                            descripcion,
                            cita.getCreatedAt(),
                            "CREATE"
                    );
                })
                .collect(Collectors.toList());
    }
}
