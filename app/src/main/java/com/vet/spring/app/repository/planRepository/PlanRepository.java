package com.vet.spring.app.repository.planRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.plan.Plan;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

}
