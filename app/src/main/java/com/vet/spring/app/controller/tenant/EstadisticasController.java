package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.estadisticasDto.*;
import com.vet.spring.app.service.tenantService.EstadisticasService;
import com.vet.spring.app.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
@Tag(name = "Estadísticas", description = "Estadísticas del dashboard")
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    @GetMapping
    @Operation(summary = "Obtener estadísticas principales del dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(estadisticasService.getDashboardStats(tenantId));
    }

    @GetMapping("/ingresos-mensuales")
    @Operation(summary = "Obtener ingresos mensuales para gráfico")
    public ResponseEntity<List<IngresosMensualDTO>> getIngresosMensuales() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(estadisticasService.getIngresosMensuales(tenantId));
    }

    @GetMapping("/citas-por-estado")
    @Operation(summary = "Obtener citas agrupadas por estado")
    public ResponseEntity<List<CitasPorEstadoDTO>> getCitasPorEstado() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(estadisticasService.getCitasPorEstado(tenantId));
    }

    @GetMapping("/mascotas-distribucion")
    @Operation(summary = "Obtener distribución de mascotas por cliente")
    public ResponseEntity<List<MascotasDistribucionDTO>> getMascotasDistribucion() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(estadisticasService.getMascotasDistribucion(tenantId));
    }

    @GetMapping("/actividad-reciente")
    @Operation(summary = "Obtener actividad reciente")
    public ResponseEntity<List<ActividadRecienteDTO>> getActividadReciente() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(estadisticasService.getActividadReciente(tenantId));
    }
}
