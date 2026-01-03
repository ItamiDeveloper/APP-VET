package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.inventarioDto.CategoriaProductoDTO;
import com.vet.spring.app.service.tenantService.CategoriaProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Gestión de categorías de productos")
public class CategoriaProductoController {

    private final CategoriaProductoService categoriaService;

    @GetMapping
    @Operation(summary = "Listar todas las categorías")
    public ResponseEntity<List<CategoriaProductoDTO>> getAllCategorias() {
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una categoría por ID")
    public ResponseEntity<CategoriaProductoDTO> getCategoriaById(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.getCategoriaById(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva categoría")
    public ResponseEntity<CategoriaProductoDTO> createCategoria(@RequestBody CategoriaProductoDTO dto) {
        CategoriaProductoDTO created = categoriaService.createCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoría existente")
    public ResponseEntity<CategoriaProductoDTO> updateCategoria(@PathVariable Integer id, @RequestBody CategoriaProductoDTO dto) {
        return ResponseEntity.ok(categoriaService.updateCategoria(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
