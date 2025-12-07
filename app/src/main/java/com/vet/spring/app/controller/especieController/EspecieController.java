package com.vet.spring.app.controller.especieController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vet.spring.app.dto.especieDto.EspecieDTO;
import com.vet.spring.app.service.especieService.EspecieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/especies")
@RequiredArgsConstructor
public class EspecieController {

    private final EspecieService especieService;

    @GetMapping
    public ResponseEntity<List<EspecieDTO>> getAllEspecies() {
        return ResponseEntity.ok(especieService.getAllEspecies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecieDTO> getEspecieById(@PathVariable Integer id) {
        return ResponseEntity.ok(especieService.getEspecieById(id));
    }
}
