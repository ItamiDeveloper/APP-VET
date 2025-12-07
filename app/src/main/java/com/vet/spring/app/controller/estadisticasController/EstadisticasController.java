package com.vet.spring.app.controller.estadisticasController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vet.spring.app.dto.estadisticasDto.ActividadRecienteDTO;
import com.vet.spring.app.dto.estadisticasDto.CitaPorEstadoDTO;
import com.vet.spring.app.dto.estadisticasDto.EstadisticasGeneralesDTO;
import com.vet.spring.app.dto.estadisticasDto.IngresoMensualDTO;
import com.vet.spring.app.dto.estadisticasDto.MascotaDistribucionDTO;
import com.vet.spring.app.service.estadisticasService.EstadisticasService;

@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    @GetMapping
    public ResponseEntity<EstadisticasGeneralesDTO> getEstadisticasGenerales() {
        return ResponseEntity.ok(estadisticasService.getEstadisticasGenerales());
    }

    @GetMapping("/ingresos-mensuales")
    public ResponseEntity<List<IngresoMensualDTO>> getIngresosMensuales() {
        return ResponseEntity.ok(estadisticasService.getIngresosMensuales());
    }

    @GetMapping("/citas-por-estado")
    public ResponseEntity<List<CitaPorEstadoDTO>> getCitasPorEstado() {
        return ResponseEntity.ok(estadisticasService.getCitasPorEstado());
    }

    @GetMapping("/mascotas-distribucion")
    public ResponseEntity<List<MascotaDistribucionDTO>> getMascotasDistribucion() {
        return ResponseEntity.ok(estadisticasService.getMascotasDistribucion());
    }

    @GetMapping("/actividad-reciente")
    public ResponseEntity<List<ActividadRecienteDTO>> getActividadReciente() {
        return ResponseEntity.ok(estadisticasService.getActividadReciente());
    }
}
