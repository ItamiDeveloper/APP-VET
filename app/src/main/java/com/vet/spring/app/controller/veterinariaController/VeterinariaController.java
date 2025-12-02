package com.vet.spring.app.controller.veterinariaController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vet.spring.app.dto.veterinariaDto.VeterinariaDTO;
import com.vet.spring.app.service.veterinariaService.VeterinariaService;

@RestController
@RequestMapping("/api/veterinarias")
public class VeterinariaController {

    private final VeterinariaService veterinariaService;

    public VeterinariaController(VeterinariaService veterinariaService) {
        this.veterinariaService = veterinariaService;
    }

    @GetMapping
    public ResponseEntity<List<VeterinariaDTO>> list() {
        return ResponseEntity.ok(veterinariaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeterinariaDTO> get(@PathVariable Integer id) {
        VeterinariaDTO d = veterinariaService.findById(id);
        return d == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(d);
    }

    @PostMapping
    public ResponseEntity<VeterinariaDTO> create(@RequestBody VeterinariaDTO dto) {
        return ResponseEntity.ok(veterinariaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinariaDTO> update(@PathVariable Integer id, @RequestBody VeterinariaDTO dto) {
        VeterinariaDTO updated = veterinariaService.update(id, dto);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        veterinariaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
