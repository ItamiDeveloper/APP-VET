package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.clienteDto.ClienteDTO;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.exception.ResourceNotFoundException;
import com.vet.spring.app.mapper.clienteMapper.ClienteMapper;
import com.vet.spring.app.repository.clienteRepository.ClienteRepository;
import com.vet.spring.app.repository.tenantRepository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final TenantRepository tenantRepository;
    private final ClienteMapper clienteMapper;

    /**
     * Obtener todos los clientes de un tenant
     */
    public List<ClienteDTO> getAllClientesByTenant(Integer tenantId) {
        List<Cliente> clientes = clienteRepository.findAll()
            .stream()
            .filter(c -> c.getTenant().getIdTenant().equals(tenantId))
            .collect(Collectors.toList());
        return clientes.stream()
            .map(ClienteMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener un cliente por ID
     */
    public ClienteDTO getClienteById(Integer id, Integer tenantId) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        
        if (!cliente.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Cliente no encontrado en este tenant");
        }
        
        return ClienteMapper.toDTO(cliente);
    }

    /**
     * Crear un nuevo cliente
     */
    @Transactional
    public ClienteDTO createCliente(ClienteDTO dto, Integer tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new ResourceNotFoundException("Tenant no encontrado con ID: " + tenantId));

        // Verificar que el número de documento es único en el tenant
        if (dto.getNumeroDocumento() != null) {
            boolean existeDocumento = clienteRepository.findAll()
                .stream()
                .anyMatch(c -> c.getTenant().getIdTenant().equals(tenantId) 
                    && c.getNumeroDocumento() != null
                    && c.getNumeroDocumento().equals(dto.getNumeroDocumento()));
            
            if (existeDocumento) {
                throw new IllegalArgumentException("Ya existe un cliente con ese número de documento");
            }
        }

        Cliente cliente = new Cliente();
        cliente.setTenant(tenant);
        cliente.setNombres(dto.getNombres());
        cliente.setApellidos(dto.getApellidos());
        cliente.setTipoDocumento(dto.getTipoDocumento());
        cliente.setNumeroDocumento(dto.getNumeroDocumento());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());
        cliente.setDireccion(dto.getDireccion());
        cliente.setEstado(Cliente.EstadoCliente.ACTIVO);
        cliente.setFechaRegistro(LocalDateTime.now());

        Cliente savedCliente = clienteRepository.save(cliente);
        return ClienteMapper.toDTO(savedCliente);
    }

    /**
     * Actualizar un cliente existente
     */
    @Transactional
    public ClienteDTO updateCliente(Integer id, ClienteDTO dto, Integer tenantId) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        if (!cliente.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Cliente no encontrado en este tenant");
        }

        // Verificar número de documento si cambió
        if (dto.getNumeroDocumento() != null && 
            !dto.getNumeroDocumento().equals(cliente.getNumeroDocumento())) {
            boolean existeDocumento = clienteRepository.findAll()
                .stream()
                .anyMatch(c -> c.getTenant().getIdTenant().equals(tenantId) 
                    && c.getNumeroDocumento() != null
                    && c.getNumeroDocumento().equals(dto.getNumeroDocumento())
                    && !c.getIdCliente().equals(id));
            
            if (existeDocumento) {
                throw new IllegalArgumentException("Ya existe otro cliente con ese número de documento");
            }
            cliente.setNumeroDocumento(dto.getNumeroDocumento());
        }

        if (dto.getNombres() != null) cliente.setNombres(dto.getNombres());
        if (dto.getApellidos() != null) cliente.setApellidos(dto.getApellidos());
        if (dto.getTipoDocumento() != null) {
            cliente.setTipoDocumento(dto.getTipoDocumento());
        }
        if (dto.getTelefono() != null) cliente.setTelefono(dto.getTelefono());
        if (dto.getEmail() != null) cliente.setEmail(dto.getEmail());
        if (dto.getDireccion() != null) cliente.setDireccion(dto.getDireccion());

        Cliente updatedCliente = clienteRepository.save(cliente);
        return ClienteMapper.toDTO(updatedCliente);
    }

    /**
     * Eliminar un cliente (soft delete)
     */
    @Transactional
    public void deleteCliente(Integer id, Integer tenantId) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        if (!cliente.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Cliente no encontrado en este tenant");
        }

        cliente.setEstado(Cliente.EstadoCliente.INACTIVO);
        clienteRepository.save(cliente);
    }

    /**
     * Obtener clientes activos
     */
    public List<ClienteDTO> getClientesActivos(Integer tenantId) {
        List<Cliente> clientes = clienteRepository.findAll()
            .stream()
            .filter(c -> c.getTenant().getIdTenant().equals(tenantId) 
                && c.getEstado() == Cliente.EstadoCliente.ACTIVO)
            .collect(Collectors.toList());
        
        return clientes.stream()
            .map(ClienteMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Buscar clientes por nombre o documento
     */
    public List<ClienteDTO> buscarClientes(String termino, Integer tenantId) {
        String terminoLower = termino.toLowerCase();
        List<Cliente> clientes = clienteRepository.findAll()
            .stream()
            .filter(c -> c.getTenant().getIdTenant().equals(tenantId) 
                && (c.getNombres().toLowerCase().contains(terminoLower)
                    || c.getApellidos().toLowerCase().contains(terminoLower)
                    || (c.getNumeroDocumento() != null && c.getNumeroDocumento().contains(termino))))
            .collect(Collectors.toList());
        
        return clientes.stream()
            .map(ClienteMapper::toDTO)
            .collect(Collectors.toList());
    }
}
