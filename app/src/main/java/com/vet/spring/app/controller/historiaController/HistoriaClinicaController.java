package com.vet.spring.app.controller.historiaController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vet.spring.app.dto.historiaDto.HistoriaClinicaDTO;
import com.vet.spring.app.service.historiaService.HistoriaService;

@RestController
@RequestMapping("/api/historias")
public class HistoriaClinicaController {

    private final HistoriaService historiaService;

    public HistoriaClinicaController(HistoriaService historiaService) {
        this.historiaService = historiaService;
    }

    @GetMapping
    public ResponseEntity<List<HistoriaClinicaDTO>> all() {
        return ResponseEntity.ok(historiaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriaClinicaDTO> get(@PathVariable Integer id) {
        HistoriaClinicaDTO dto = historiaService.findById(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<HistoriaClinicaDTO> create(@Valid @RequestBody HistoriaClinicaDTO dto) {
        HistoriaClinicaDTO created = historiaService.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriaClinicaDTO> update(@PathVariable Integer id, @Valid @RequestBody HistoriaClinicaDTO dto) {
        HistoriaClinicaDTO updated = historiaService.update(id, dto);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        historiaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
