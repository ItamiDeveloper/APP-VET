package com.vet.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.doctor.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

}
