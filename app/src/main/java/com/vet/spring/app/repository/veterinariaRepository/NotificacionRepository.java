package com.vet.spring.app.repository.veterinariaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.veterinaria.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

}
