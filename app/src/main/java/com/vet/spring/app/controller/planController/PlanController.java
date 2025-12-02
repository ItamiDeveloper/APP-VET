package com.vet.spring.app.controller;

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

import com.vet.spring.app.dto.plan.PlanDTO;
import com.vet.spring.app.service.plan.PlanService;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping
    public ResponseEntity<List<PlanDTO>> list() {
        return ResponseEntity.ok(planService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanDTO> get(@PathVariable Integer id) {
        PlanDTO d = planService.findById(id);
        return d == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(d);
    }

    @PostMapping
    public ResponseEntity<PlanDTO> create(@RequestBody PlanDTO dto) {
        return ResponseEntity.ok(planService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanDTO> update(@PathVariable Integer id, @RequestBody PlanDTO dto) {
        PlanDTO updated = planService.update(id, dto);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
