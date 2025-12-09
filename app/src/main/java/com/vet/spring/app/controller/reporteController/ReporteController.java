package com.vet.spring.app.controller.reporteController;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vet.spring.app.dto.reporteDto.ReporteCitasDTO;
import com.vet.spring.app.dto.reporteDto.ReporteVentasDTO;
import com.vet.spring.app.dto.reporteDto.ResumenCitasDTO;
import com.vet.spring.app.dto.reporteDto.ResumenVentasDTO;
import com.vet.spring.app.service.reporteService.ReporteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/ventas")
    public ResponseEntity<List<ReporteVentasDTO>> getReporteVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(reporteService.getReporteVentasPorFecha(fechaInicio, fechaFin));
    }

    @GetMapping("/citas")
    public ResponseEntity<List<ReporteCitasDTO>> getReporteCitas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(reporteService.getReporteCitasPorFecha(fechaInicio, fechaFin));
    }

    @GetMapping("/ventas/resumen")
    public ResponseEntity<ResumenVentasDTO> getResumenVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(reporteService.getResumenVentas(fechaInicio, fechaFin));
    }

    @GetMapping("/citas/resumen")
    public ResponseEntity<ResumenCitasDTO> getResumenCitas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(reporteService.getResumenCitas(fechaInicio, fechaFin));
    }

    @GetMapping("/citas/hoy")
    public ResponseEntity<List<ReporteCitasDTO>> getCitasDelDia() {
        return ResponseEntity.ok(reporteService.getCitasDelDia());
    }

    @GetMapping("/ventas/mes")
    public ResponseEntity<List<ReporteVentasDTO>> getVentasDelMes() {
        return ResponseEntity.ok(reporteService.getVentasDelMes());
    }
}
