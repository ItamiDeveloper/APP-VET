package com.vet.spring.app.repository.inventarioRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.vet.spring.app.entity.inventario.Inventario;

import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
    
    @Query("SELECT i FROM Inventario i WHERE i.tenant.idTenant = :tenantId AND i.producto.idProducto = :productoId")
    Optional<Inventario> findByTenantIdAndProductoId(@Param("tenantId") Integer tenantId, @Param("productoId") Integer productoId);

}
