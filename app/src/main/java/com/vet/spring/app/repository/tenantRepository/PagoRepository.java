package com.vet.spring.app.repository.tenantRepository;

import com.vet.spring.app.entity.tenant.Pago;
import com.vet.spring.app.entity.tenant.Pago.EstadoPago;
import com.vet.spring.app.entity.tenant.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    
    List<Pago> findByTenantOrderByFechaPagoDesc(Tenant tenant);
    
    List<Pago> findByEstado(EstadoPago estado);
    
    @Query("SELECT p FROM Pago p WHERE p.tenant = :tenant AND p.fechaPago BETWEEN :fechaInicio AND :fechaFin")
    List<Pago> findPagosByTenantAndFechaRange(Tenant tenant, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.estado = 'COMPLETADO' AND YEAR(p.fechaPago) = :year AND MONTH(p.fechaPago) = :month")
    Double calcularIngresosMensuales(int year, int month);
}
