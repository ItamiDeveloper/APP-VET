package com.vet.spring.app.repository.tenantRepository;

import com.vet.spring.app.entity.tenant.Plan;
import com.vet.spring.app.entity.tenant.Plan.EstadoPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    
    List<Plan> findByEstadoOrderByOrdenVisualizacionAsc(EstadoPlan estado);
    
    List<Plan> findAllByOrderByOrdenVisualizacionAsc();
}
