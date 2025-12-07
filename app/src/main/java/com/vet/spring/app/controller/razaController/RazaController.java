package com.vet.spring.app.controller.razaController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vet.spring.app.dto.razaDto.RazaDTO;
import com.vet.spring.app.service.razaService.RazaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/razas")
@RequiredArgsConstructor
public class RazaController {

    private final RazaService razaService;

    @GetMapping
    public ResponseEntity<List<RazaDTO>> getAllRazas(
        @RequestParam(required = false) Integer idEspecie
    ) {
        if (idEspecie != null) {
            return ResponseEntity.ok(razaService.getRazasByEspecie(idEspecie));
        }
        return ResponseEntity.ok(razaService.getAllRazas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RazaDTO> getRazaById(@PathVariable Integer id) {
        return ResponseEntity.ok(razaService.getRazaById(id));
    }
}
