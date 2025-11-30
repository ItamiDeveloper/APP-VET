package com.vet.spring.app.repository.clienteRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.cliente.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
