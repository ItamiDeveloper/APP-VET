package com.vet.spring.app.service.reporteService.reporteImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.reporteDto.ReporteCitasDTO;
import com.vet.spring.app.dto.reporteDto.ReporteVentasDTO;
import com.vet.spring.app.dto.reporteDto.ResumenCitasDTO;
import com.vet.spring.app.dto.reporteDto.ResumenVentasDTO;
import com.vet.spring.app.entity.cita.Cita;
import com.vet.spring.app.entity.venta.Venta;
import com.vet.spring.app.repository.citaRepository.CitaRepository;
import com.vet.spring.app.repository.ventaRepository.VentaRepository;
import com.vet.spring.app.service.reporteService.ReporteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    private final VentaRepository ventaRepository;
    private final CitaRepository citaRepository;

    @Override
    public List<ReporteVentasDTO> getReporteVentasPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
        
        List<Venta> ventas = ventaRepository.findAll().stream()
            .filter(v -> v.getFecha() != null && 
                        !v.getFecha().isBefore(inicio) && 
                        !v.getFecha().isAfter(fin))
            .collect(Collectors.toList());
        
        return ventas.stream()
            .map(this::convertirVentaAReporte)
            .collect(Collectors.toList());
    }

    @Override
    public List<ReporteCitasDTO> getReporteCitasPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
        
        List<Cita> citas = citaRepository.findAll().stream()
            .filter(c -> c.getFechaHora() != null && 
                        !c.getFechaHora().isBefore(inicio) && 
                        !c.getFechaHora().isAfter(fin))
            .collect(Collectors.toList());
        
        return citas.stream()
            .map(this::convertirCitaAReporte)
            .collect(Collectors.toList());
    }

    @Override
    public ResumenVentasDTO getResumenVentas(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
        
        List<Venta> ventas = ventaRepository.findAll().stream()
            .filter(v -> v.getFecha() != null && 
                        !v.getFecha().isBefore(inicio) && 
                        !v.getFecha().isAfter(fin))
            .collect(Collectors.toList());
        
        Long totalVentas = (long) ventas.size();
        BigDecimal totalIngresos = ventas.stream()
            .filter(v -> "PAGADA".equals(v.getEstado()))
            .map(Venta::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal promedioVenta = totalVentas > 0 
            ? totalIngresos.divide(BigDecimal.valueOf(totalVentas), 2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
        
        Long ventasPagadas = ventas.stream()
            .filter(v -> "PAGADA".equals(v.getEstado()))
            .count();
        
        Long ventasPendientes = ventas.stream()
            .filter(v -> "PENDIENTE".equals(v.getEstado()))
            .count();
        
        Long ventasCanceladas = ventas.stream()
            .filter(v -> "CANCELADA".equals(v.getEstado()))
            .count();
        
        return new ResumenVentasDTO(totalVentas, totalIngresos, promedioVenta, 
                                    ventasPagadas, ventasPendientes, ventasCanceladas);
    }

    @Override
    public ResumenCitasDTO getResumenCitas(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
        
        List<Cita> citas = citaRepository.findAll().stream()
            .filter(c -> c.getFechaHora() != null && 
                        !c.getFechaHora().isBefore(inicio) && 
                        !c.getFechaHora().isAfter(fin))
            .collect(Collectors.toList());
        
        Long totalCitas = (long) citas.size();
        
        Long citasProgramadas = citas.stream()
            .filter(c -> "PROGRAMADA".equals(c.getEstado().toString()))
            .count();
        
        Long citasCompletadas = citas.stream()
            .filter(c -> "COMPLETADA".equals(c.getEstado().toString()))
            .count();
        
        Long citasCanceladas = citas.stream()
            .filter(c -> "CANCELADA".equals(c.getEstado().toString()))
            .count();
        
        LocalDateTime hoy = LocalDate.now().atStartOfDay();
        LocalDateTime finHoy = LocalDate.now().atTime(LocalTime.MAX);
        
        Long citasHoy = citas.stream()
            .filter(c -> c.getFechaHora() != null &&
                        !c.getFechaHora().isBefore(hoy) &&
                        !c.getFechaHora().isAfter(finHoy))
            .count();
        
        return new ResumenCitasDTO(totalCitas, citasProgramadas, citasCompletadas, 
                                   citasCanceladas, citasHoy);
    }

    @Override
    public List<ReporteCitasDTO> getCitasDelDia() {
        LocalDate hoy = LocalDate.now();
        return getReporteCitasPorFecha(hoy, hoy);
    }

    @Override
    public List<ReporteVentasDTO> getVentasDelMes() {
        YearMonth mesActual = YearMonth.now();
        LocalDate inicio = mesActual.atDay(1);
        LocalDate fin = mesActual.atEndOfMonth();
        return getReporteVentasPorFecha(inicio, fin);
    }

    private ReporteVentasDTO convertirVentaAReporte(Venta venta) {
        String nombreCliente = venta.getCliente() != null 
            ? venta.getCliente().getNombres() + " " + venta.getCliente().getApellidos()
            : "Sin cliente";
        
        // La cantidad de productos no está disponible en la entidad actual
        Integer cantidadProductos = 0;
        
        return new ReporteVentasDTO(
            venta.getIdVenta(),
            nombreCliente,
            venta.getFecha(),
            venta.getTotal(),
            venta.getEstado(),
            cantidadProductos
        );
    }

    private ReporteCitasDTO convertirCitaAReporte(Cita cita) {
        String nombreMascota = cita.getMascota() != null 
            ? cita.getMascota().getNombre()
            : "Sin mascota";
        
        String nombreCliente = cita.getMascota() != null && cita.getMascota().getCliente() != null
            ? cita.getMascota().getCliente().getNombres() + " " + cita.getMascota().getCliente().getApellidos()
            : "Sin cliente";
        
        String nombreDoctor = cita.getDoctor() != null && cita.getDoctor().getUsuario() != null
            ? cita.getDoctor().getUsuario().getUsername()
            : "Sin asignar";
        
        return new ReporteCitasDTO(
            cita.getIdCita(),
            nombreMascota,
            nombreCliente,
            nombreDoctor,
            cita.getFechaHora(),
            cita.getMotivo(),
            cita.getEstado() != null ? cita.getEstado().toString() : "SIN_ESTADO"
        );
    }
}
