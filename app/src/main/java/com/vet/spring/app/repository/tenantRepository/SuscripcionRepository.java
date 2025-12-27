package com.vet.spring.app.repository.tenantRepository;

import com.vet.spring.app.entity.tenant.Suscripcion;
import com.vet.spring.app.entity.tenant.Suscripcion.EstadoSuscripcion;
import com.vet.spring.app.entity.tenant.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Integer> {
    
    List<Suscripcion> findByTenantOrderByFechaCreacionDesc(Tenant tenant);
    
    @Query("SELECT s FROM Suscripcion s WHERE s.tenant = :tenant AND s.estado = 'ACTIVO' ORDER BY s.fechaFin DESC")
    Optional<Suscripcion> findActiveSuscripcionByTenant(Tenant tenant);
    
    @Query("SELECT s FROM Suscripcion s WHERE s.estado = :estado AND s.fechaFin BETWEEN :fechaInicio AND :fechaFin")
    List<Suscripcion> findSuscripcionesProximasAVencer(EstadoSuscripcion estado, LocalDate fechaInicio, LocalDate fechaFin);
}
