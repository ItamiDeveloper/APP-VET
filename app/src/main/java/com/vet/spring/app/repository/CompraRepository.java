package com.vet.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.compra.Compra;

public interface CompraRepository extends JpaRepository<Compra, Integer> {

}
