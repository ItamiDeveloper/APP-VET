package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.tenantDto.MiSuscripcionDTO;
import com.vet.spring.app.dto.tenantDto.TenantDTO;
import com.vet.spring.app.dto.tenantDto.TenantRegistroDTO;
import com.vet.spring.app.entity.tenant.Plan;
import com.vet.spring.app.entity.tenant.Suscripcion;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.entity.usuario.Rol;
import com.vet.spring.app.entity.usuario.Usuario;
import com.vet.spring.app.repository.tenantRepository.PlanRepository;
import com.vet.spring.app.repository.tenantRepository.SuscripcionRepository;
import com.vet.spring.app.repository.tenantRepository.TenantRepository;
import com.vet.spring.app.repository.usuarioRepository.RolRepository;
import com.vet.spring.app.repository.usuarioRepository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final PlanRepository planRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo tenant desde el landing page
     */
    @Transactional
    public TenantDTO registrarTenant(TenantRegistroDTO dto) {
        // Validar que el código del tenant no exista
        if (tenantRepository.existsByCodigoTenant(dto.getCodigoTenant())) {
            throw new RuntimeException("El código de tenant ya existe: " + dto.getCodigoTenant());
        }

        // Obtener el plan seleccionado
        Plan plan = planRepository.findById(dto.getIdPlan())
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        // Crear el tenant
        Tenant tenant = new Tenant();
        tenant.setCodigoTenant(dto.getCodigoTenant());
        tenant.setNombreComercial(dto.getNombreComercial());
        tenant.setRazonSocial(dto.getRazonSocial());
        tenant.setRuc(dto.getRuc());
        tenant.setTelefono(dto.getTelefono());
        tenant.setEmailContacto(dto.getEmailContacto());
        tenant.setDireccion(dto.getDireccion());
        tenant.setCiudad(dto.getCiudad());
        tenant.setPais(dto.getPais());
        tenant.setPlanActual(plan);
        tenant.setEstadoSuscripcion(Tenant.EstadoSuscripcion.TRIAL);
        tenant.setNombrePropietario(dto.getNombrePropietario() + " " + dto.getApellidoPropietario());
        tenant.setEmailPropietario(dto.getEmailPropietario());
        tenant.setTelefonoPropietario(dto.getTelefonoPropietario());
        tenant.setEstado(Tenant.EstadoTenant.ACTIVO);
        tenant.setFechaActivacion(LocalDateTime.now());

        Tenant savedTenant = tenantRepository.save(tenant);

        // Crear suscripción de trial
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setTenant(savedTenant);
        suscripcion.setPlan(plan);
        suscripcion.setFechaInicio(LocalDate.now());
        suscripcion.setFechaFin(LocalDate.now().plusDays(savedTenant.getDiasTrial()));
        suscripcion.setEstado(Suscripcion.EstadoSuscripcion.ACTIVO);
        suscripcion.setNotas("Suscripción de prueba gratuita");
        suscripcionRepository.save(suscripcion);

        // Crear usuario administrador del tenant
        Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

        Usuario adminUser = new Usuario();
        adminUser.setTenant(savedTenant);
        adminUser.setRol(rolAdmin);
        adminUser.setUsername(dto.getUsernamePropietario());
        adminUser.setPasswordHash(passwordEncoder.encode(dto.getPasswordPropietario()));
        adminUser.setEmail(dto.getEmailPropietario());
        adminUser.setNombres(dto.getNombrePropietario());
        adminUser.setApellidos(dto.getApellidoPropietario());
        adminUser.setTelefono(dto.getTelefonoPropietario());
        adminUser.setEstado(Usuario.EstadoUsuario.ACTIVO);
        usuarioRepository.save(adminUser);

        // Incrementar contador de usuarios activos
        savedTenant.setUsuariosActivos(1);
        tenantRepository.save(savedTenant);

        return toDTO(savedTenant);
    }

    /**
     * Obtiene todos los tenants
     */
    public List<TenantDTO> getAllTenants() {
        return tenantRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un tenant por ID
     */
    public TenantDTO getTenantById(Integer id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado con ID: " + id));
        return toDTO(tenant);
    }

    /**
     * Obtiene un tenant por código
     */
    public TenantDTO getTenantByCodigo(String codigo) {
        Tenant tenant = tenantRepository.findByCodigoTenant(codigo)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado con código: " + codigo));
        return toDTO(tenant);
    }

    /**
     * Actualiza la información de un tenant
     */
    @Transactional
    public TenantDTO actualizarTenant(Integer id, TenantDTO dto) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado con ID: " + id));

        tenant.setNombreComercial(dto.getNombreComercial());
        tenant.setRazonSocial(dto.getRazonSocial());
        tenant.setRuc(dto.getRuc());
        tenant.setTelefono(dto.getTelefono());
        tenant.setEmailContacto(dto.getEmailContacto());
        tenant.setDireccion(dto.getDireccion());
        tenant.setCiudad(dto.getCiudad());
        tenant.setPais(dto.getPais());
        tenant.setLogoUrl(dto.getLogoUrl());
        tenant.setColorPrimario(dto.getColorPrimario());
        tenant.setColorSecundario(dto.getColorSecundario());

        Tenant updated = tenantRepository.save(tenant);
        return toDTO(updated);
    }

    /**
     * Cambia el plan de un tenant
     */
    @Transactional
    public void cambiarPlan(Integer tenantId, Integer nuevoPlanId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado"));
        
        Plan nuevoPlan = planRepository.findById(nuevoPlanId)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        tenant.setPlanActual(nuevoPlan);
        tenantRepository.save(tenant);
    }

    /**
     * Suspende un tenant
     */
    @Transactional
    public void suspenderTenant(Integer tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado"));
        
        tenant.setEstadoSuscripcion(Tenant.EstadoSuscripcion.SUSPENDIDO);
        tenant.setFechaSuspension(LocalDateTime.now());
        tenantRepository.save(tenant);
    }

    /**
     * Reactiva un tenant suspendido
     */
    @Transactional
    public void reactivarTenant(Integer tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado"));
        
        tenant.setEstadoSuscripcion(Tenant.EstadoSuscripcion.ACTIVO);
        tenant.setFechaSuspension(null);
        tenantRepository.save(tenant);
    }

    /**
     * Valida si un tenant puede agregar un nuevo usuario
     */
    public boolean puedeAgregarUsuario(Integer tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado"));
        return tenant.puedeAgregarUsuario();
    }

    /**
     * Incrementa el contador de usuarios activos
     */
    @Transactional
    public void incrementarUsuariosActivos(Integer tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado"));
        tenant.setUsuariosActivos(tenant.getUsuariosActivos() + 1);
        tenantRepository.save(tenant);
    }

    /**
     * Obtiene los datos del tenant autenticado (para vista de perfil)
     */
    public TenantDTO getMiVeterinaria(Integer tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado"));
        return toDTO(tenant);
    }

    /**
     * Actualiza los datos del tenant autenticado (perfil)
     */
    @Transactional
    public TenantDTO actualizarMiVeterinaria(Integer tenantId, TenantDTO dto) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado"));
        
        // Solo actualizar campos que el tenant puede modificar
        tenant.setNombreComercial(dto.getNombreComercial());
        tenant.setRazonSocial(dto.getRazonSocial());
        tenant.setTelefono(dto.getTelefono());
        tenant.setEmailContacto(dto.getEmailContacto());
        tenant.setDireccion(dto.getDireccion());
        tenant.setCiudad(dto.getCiudad());
        tenant.setLogoUrl(dto.getLogoUrl());
        tenant.setColorPrimario(dto.getColorPrimario());
        tenant.setColorSecundario(dto.getColorSecundario());
        
        tenant = tenantRepository.save(tenant);
        return toDTO(tenant);
    }

    /**
     * Obtiene información de la suscripción del tenant autenticado
     */
    public MiSuscripcionDTO getMiSuscripcion(Integer tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado"));
        
        Plan plan = tenant.getPlanActual();
        if (plan == null) {
            throw new RuntimeException("El tenant no tiene un plan asignado");
        }
        
        // Obtener la suscripción activa (puede ser null si es nuevo)
        Optional<Suscripcion> suscripcionOpt = suscripcionRepository.findActiveSuscripcionByTenant(tenant);
        Suscripcion suscripcion = suscripcionOpt.orElse(null);
        
        MiSuscripcionDTO dto = new MiSuscripcionDTO();
        
        // Información del plan
        dto.setIdPlan(plan.getIdPlan());
        dto.setNombrePlan(plan.getNombre());
        dto.setDescripcionPlan(plan.getDescripcion());
        dto.setPrecioMensual(plan.getPrecioMensual());
        dto.setPrecioAnual(plan.getPrecioAnual());
        
        // Estado de suscripción
        dto.setEstadoSuscripcion(tenant.getEstadoSuscripcion().name());
        if (suscripcion != null) {
            dto.setFechaInicio(suscripcion.getFechaInicio());
            dto.setFechaFin(suscripcion.getFechaFin());
            dto.setProximoPago(suscripcion.getFechaFin()); // La próxima fecha de pago es la fecha de fin
        } else {
            dto.setFechaInicio(tenant.getFechaActivacion() != null ? tenant.getFechaActivacion().toLocalDate() : null);
            dto.setFechaFin(null);
            dto.setProximoPago(null);
        }
        
        // Límites del plan
        dto.setMaxUsuarios(plan.getMaxUsuarios());
        dto.setMaxDoctores(plan.getMaxDoctores());
        dto.setMaxMascotas(plan.getMaxMascotas());
        dto.setMaxAlmacenamientoMb(plan.getMaxAlmacenamientoMb());
        
        // Uso actual
        dto.setUsuariosActivos(tenant.getUsuariosActivos());
        dto.setDoctoresActivos(tenant.getDoctoresActivos());
        dto.setMascotasRegistradas(tenant.getMascotasRegistradas());
        dto.setAlmacenamientoUsadoMb(tenant.getAlmacenamientoUsadoMb());
        
        // Calcular porcentajes de uso
        dto.setPorcentajeUsuarios((tenant.getUsuariosActivos() * 100.0) / plan.getMaxUsuarios());
        dto.setPorcentajeDoctores((tenant.getDoctoresActivos() * 100.0) / plan.getMaxDoctores());
        dto.setPorcentajeMascotas((tenant.getMascotasRegistradas() * 100.0) / plan.getMaxMascotas());
        dto.setPorcentajeAlmacenamiento((tenant.getAlmacenamientoUsadoMb() * 100.0) / plan.getMaxAlmacenamientoMb());
        
        // Features del plan
        dto.setTieneReportesAvanzados(plan.getTieneReportesAvanzados());
        dto.setTieneApiAcceso(plan.getTieneApiAcceso());
        dto.setTieneSoportePrioritario(plan.getTieneSoportePrioritario());
        
        // Calcular días restantes de trial si aplica
        dto.setEnPeriodoTrial(tenant.getEstadoSuscripcion() == Tenant.EstadoSuscripcion.TRIAL);
        if (dto.getEnPeriodoTrial() && tenant.getFechaRegistro() != null) {
            // Calcular fecha fin de trial basado en días configurados
            LocalDate fechaFinTrial = tenant.getFechaRegistro().toLocalDate().plusDays(tenant.getDiasTrial());
            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), fechaFinTrial);
            dto.setDiasRestantesTrial((int) Math.max(0, diasRestantes));
        }
        
        return dto;
    }

    // Conversión DTO
    private TenantDTO toDTO(Tenant tenant) {
        TenantDTO dto = new TenantDTO();
        dto.setIdTenant(tenant.getIdTenant());
        dto.setCodigoTenant(tenant.getCodigoTenant());
        dto.setNombreComercial(tenant.getNombreComercial());
        dto.setRazonSocial(tenant.getRazonSocial());
        dto.setRuc(tenant.getRuc());
        dto.setTelefono(tenant.getTelefono());
        dto.setEmailContacto(tenant.getEmailContacto());
        dto.setDireccion(tenant.getDireccion());
        dto.setPais(tenant.getPais());
        dto.setCiudad(tenant.getCiudad());
        
        dto.setIdPlanActual(tenant.getPlanActual().getIdPlan());
        dto.setNombrePlan(tenant.getPlanActual().getNombre());
        dto.setEstadoSuscripcion(tenant.getEstadoSuscripcion().name());
        
        dto.setNombrePropietario(tenant.getNombrePropietario());
        dto.setEmailPropietario(tenant.getEmailPropietario());
        dto.setTelefonoPropietario(tenant.getTelefonoPropietario());
        
        dto.setUsuariosActivos(tenant.getUsuariosActivos());
        dto.setDoctoresActivos(tenant.getDoctoresActivos());
        dto.setMascotasRegistradas(tenant.getMascotasRegistradas());
        dto.setAlmacenamientoUsadoMb(tenant.getAlmacenamientoUsadoMb());
        
        dto.setMaxUsuarios(tenant.getPlanActual().getMaxUsuarios());
        dto.setMaxDoctores(tenant.getPlanActual().getMaxDoctores());
        dto.setMaxMascotas(tenant.getPlanActual().getMaxMascotas());
        dto.setMaxAlmacenamientoMb(tenant.getPlanActual().getMaxAlmacenamientoMb());
        
        dto.setLogoUrl(tenant.getLogoUrl());
        dto.setColorPrimario(tenant.getColorPrimario());
        dto.setColorSecundario(tenant.getColorSecundario());
        dto.setEstado(tenant.getEstado().name());
        
        return dto;
    }
}
