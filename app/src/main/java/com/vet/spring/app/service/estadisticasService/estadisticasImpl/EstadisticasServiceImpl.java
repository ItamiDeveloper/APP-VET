package com.vet.spring.app.service.estadisticasService.estadisticasImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.estadisticasDto.ActividadRecienteDTO;
import com.vet.spring.app.dto.estadisticasDto.CitaPorEstadoDTO;
import com.vet.spring.app.dto.estadisticasDto.EstadisticasGeneralesDTO;
import com.vet.spring.app.dto.estadisticasDto.IngresoMensualDTO;
import com.vet.spring.app.dto.estadisticasDto.MascotaDistribucionDTO;
import com.vet.spring.app.entity.cita.Cita;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.entity.mascota.Mascota;
import com.vet.spring.app.entity.venta.Venta;
import com.vet.spring.app.repository.citaRepository.CitaRepository;
import com.vet.spring.app.repository.clienteRepository.ClienteRepository;
import com.vet.spring.app.repository.mascotaRepository.MascotaRepository;
import com.vet.spring.app.repository.ventaRepository.VentaRepository;
import com.vet.spring.app.repository.veterinariaRepository.VeterinariaRepository;
import com.vet.spring.app.repository.planRepository.PlanRepository;
import com.vet.spring.app.service.estadisticasService.EstadisticasService;

@Service
@Transactional(readOnly = true)
public class EstadisticasServiceImpl implements EstadisticasService {

    private final ClienteRepository clienteRepository;
    private final MascotaRepository mascotaRepository;
    private final CitaRepository citaRepository;
    private final VentaRepository ventaRepository;
    private final VeterinariaRepository veterinariaRepository;
    private final PlanRepository planRepository;

    public EstadisticasServiceImpl(
            ClienteRepository clienteRepository,
            MascotaRepository mascotaRepository,
            CitaRepository citaRepository,
            VentaRepository ventaRepository,
            VeterinariaRepository veterinariaRepository,
            PlanRepository planRepository) {
        this.clienteRepository = clienteRepository;
        this.mascotaRepository = mascotaRepository;
        this.citaRepository = citaRepository;
        this.ventaRepository = ventaRepository;
        this.veterinariaRepository = veterinariaRepository;
        this.planRepository = planRepository;
    }

    @Override
    public EstadisticasGeneralesDTO getEstadisticasGenerales() {
        Long totalClientes = clienteRepository.count();
        Long totalMascotas = mascotaRepository.count();
        Long totalCitas = citaRepository.count();
        Long totalVeterinarias = veterinariaRepository.count();
        Long totalPlanes = planRepository.count();
        
        // Calcular ingresos totales de ventas pagadas
        List<Venta> ventas = ventaRepository.findAll();
        BigDecimal totalIngresos = ventas.stream()
            .filter(v -> "PAGADA".equals(v.getEstado()))
            .map(Venta::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new EstadisticasGeneralesDTO(totalClientes, totalMascotas, totalCitas, totalIngresos, totalVeterinarias, totalPlanes);
    }

    @Override
    public List<IngresoMensualDTO> getIngresosMensuales() {
        List<Venta> ventas = ventaRepository.findAll();
        
        // Agrupar ventas por mes
        Map<String, BigDecimal> ingresosPorMes = new HashMap<>();
        
        for (Venta venta : ventas) {
            if ("PAGADA".equals(venta.getEstado()) && venta.getFecha() != null) {
                String mes = venta.getFecha().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
                ingresosPorMes.merge(mes, venta.getTotal(), BigDecimal::add);
            }
        }
        
        return ingresosPorMes.entrySet().stream()
            .map(entry -> new IngresoMensualDTO(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    @Override
    public List<CitaPorEstadoDTO> getCitasPorEstado() {
        List<Cita> citas = citaRepository.findAll();
        
        // Agrupar citas por estado
        Map<String, Long> citasPorEstado = citas.stream()
            .collect(Collectors.groupingBy(
                cita -> cita.getEstado() != null ? cita.getEstado().toString() : "SIN_ESTADO",
                Collectors.counting()
            ));
        
        return citasPorEstado.entrySet().stream()
            .map(entry -> new CitaPorEstadoDTO(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    @Override
    public List<MascotaDistribucionDTO> getMascotasDistribucion() {
        List<Mascota> mascotas = mascotaRepository.findAll();
        
        // Agrupar mascotas por especie
        Map<String, Long> mascotasPorEspecie = mascotas.stream()
            .collect(Collectors.groupingBy(
                mascota -> mascota.getRaza() != null && mascota.getRaza().getEspecie() != null 
                    ? mascota.getRaza().getEspecie().getNombre() 
                    : "Sin Especie",
                Collectors.counting()
            ));
        
        return mascotasPorEspecie.entrySet().stream()
            .map(entry -> new MascotaDistribucionDTO(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    @Override
    public List<ActividadRecienteDTO> getActividadReciente() {
        List<ActividadRecienteDTO> actividades = new ArrayList<>();
        
        // Obtener las últimas 5 citas
        List<Cita> citas = citaRepository.findAll().stream()
            .filter(c -> c.getFechaHora() != null)
            .sorted((c1, c2) -> c2.getFechaHora().compareTo(c1.getFechaHora()))
            .limit(5)
            .toList();
        
        for (Cita cita : citas) {
            String descripcion = String.format("Cita con %s - %s", 
                cita.getMascota() != null ? cita.getMascota().getNombre() : "Mascota",
                cita.getMotivo() != null ? cita.getMotivo() : "");
            actividades.add(new ActividadRecienteDTO("CITA", descripcion, cita.getFechaHora()));
        }
        
        // Obtener las últimas 5 ventas
        List<Venta> ventas = ventaRepository.findAll().stream()
            .filter(v -> v.getFecha() != null)
            .sorted((v1, v2) -> v2.getFecha().compareTo(v1.getFecha()))
            .limit(5)
            .toList();
        
        for (Venta venta : ventas) {
            String descripcion = String.format("Venta a %s - $%.2f", 
                venta.getCliente() != null && venta.getCliente().getNombres() != null 
                    ? venta.getCliente().getNombres() : "Cliente",
                venta.getTotal());
            actividades.add(new ActividadRecienteDTO("VENTA", descripcion, venta.getFecha()));
        }
        
        // Ordenar todas las actividades por fecha
        return actividades.stream()
            .sorted((a1, a2) -> a2.getFecha().compareTo(a1.getFecha()))
            .limit(10)
            .collect(Collectors.toList());
    }
}
