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
import com.vet.spring.app.repository.mascotaRepository.MascotaRepository;
import com.vet.spring.app.repository.doctorRepository.DoctorRepository;
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
    private final MascotaRepository mascotaRepository;
    private final DoctorRepository doctorRepository;
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

        // Crear el tenant con estado PENDIENTE (esperando aprobación del SuperAdmin)
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
        
        // ESTADO PENDIENTE - Requiere aprobación del SuperAdmin
        tenant.setEstado(Tenant.EstadoTenant.PENDIENTE);
        tenant.setFechaRegistro(LocalDateTime.now());

        Tenant savedTenant = tenantRepository.save(tenant);

        // Guardar datos del usuario administrador propuesto (se creará después de aprobación)
        // Por ahora solo guardamos la información en el tenant
        savedTenant.setUsuariosActivos(0); // No hay usuarios hasta que se apruebe
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
     * APROBAR SOLICITUD DE REGISTRO (Solo SuperAdmin)
     * Crea la suscripción y el usuario administrador
     */
    @Transactional
    public TenantDTO aprobarSolicitud(Integer tenantId, TenantRegistroDTO datosUsuario) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado"));

        if (tenant.getEstado() != Tenant.EstadoTenant.PENDIENTE) {
            throw new RuntimeException("El tenant ya fue procesado");
        }

        // Activar el tenant
        tenant.setEstado(Tenant.EstadoTenant.ACTIVO);
        tenant.setFechaActivacion(LocalDateTime.now());
        tenantRepository.save(tenant);

        // Crear suscripción de trial
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setTenant(tenant);
        suscripcion.setPlan(tenant.getPlanActual());
        suscripcion.setFechaInicio(LocalDate.now());
        suscripcion.setFechaFin(LocalDate.now().plusDays(tenant.getDiasTrial()));
        suscripcion.setEstado(Suscripcion.EstadoSuscripcion.ACTIVA);
        suscripcionRepository.save(suscripcion);

        // Crear usuario administrador del tenant
        Rol rolAdmin = rolRepository.findByNombre("ADMIN")
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

        Usuario adminUser = new Usuario();
        adminUser.setTenant(tenant);
        adminUser.setRol(rolAdmin);
        adminUser.setUsername(datosUsuario.getUsernamePropietario());
        adminUser.setPasswordHash(passwordEncoder.encode(datosUsuario.getPasswordPropietario()));
        adminUser.setEmail(datosUsuario.getEmailPropietario());
        adminUser.setNombres(datosUsuario.getNombrePropietario());
        adminUser.setApellidos(datosUsuario.getApellidoPropietario());
        adminUser.setTelefono(datosUsuario.getTelefonoPropietario());
        adminUser.setEstado(Usuario.EstadoUsuario.ACTIVO);
        usuarioRepository.save(adminUser);

        // Incrementar contador de usuarios activos
        tenant.setUsuariosActivos(1);
        tenantRepository.save(tenant);

        return toDTO(tenant);
    }

    /**
     * RECHAZAR SOLICITUD DE REGISTRO (Solo SuperAdmin)
     */
    @Transactional
    public void rechazarSolicitud(Integer tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado"));

        if (tenant.getEstado() != Tenant.EstadoTenant.PENDIENTE) {
            throw new RuntimeException("El tenant ya fue procesado");
        }

        tenant.setEstado(Tenant.EstadoTenant.RECHAZADO);
        tenantRepository.save(tenant);
    }

    /**
     * Obtener solicitudes pendientes (Solo SuperAdmin)
     */
    public List<TenantDTO> getSolicitudesPendientes() {
        return tenantRepository.findAll().stream()
                .filter(t -> t.getEstado() == Tenant.EstadoTenant.PENDIENTE)
                .map(this::toDTO)
                .collect(Collectors.toList());
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
        
        // Uso actual - Contar desde las tablas directamente
        long usuariosActivos = usuarioRepository.countByIdTenant(tenant.getIdTenant());
        long doctoresActivos = doctorRepository.countByIdTenant(tenant.getIdTenant());
        long mascotasRegistradas = mascotaRepository.countByIdTenant(tenant.getIdTenant());
        
        dto.setUsuariosActivos((int) usuariosActivos);
        dto.setDoctoresActivos((int) doctoresActivos);
        dto.setMascotasRegistradas((int) mascotasRegistradas);
        dto.setAlmacenamientoUsadoMb(tenant.getAlmacenamientoUsadoMb() != null ? tenant.getAlmacenamientoUsadoMb() : 0);
        
        // Calcular porcentajes de uso
        dto.setPorcentajeUsuarios((usuariosActivos * 100.0) / plan.getMaxUsuarios());
        dto.setPorcentajeDoctores((doctoresActivos * 100.0) / plan.getMaxDoctores());
        dto.setPorcentajeMascotas((mascotasRegistradas * 100.0) / plan.getMaxMascotas());
        dto.setPorcentajeAlmacenamiento((dto.getAlmacenamientoUsadoMb() * 100.0) / plan.getMaxAlmacenamientoMb());
        
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
        
        // Validar que el plan actual no sea null
        Plan plan = tenant.getPlanActual();
        if (plan != null) {
            dto.setIdPlanActual(plan.getIdPlan());
            dto.setNombrePlan(plan.getNombre());
            dto.setMaxUsuarios(plan.getMaxUsuarios());
            dto.setMaxDoctores(plan.getMaxDoctores());
            dto.setMaxMascotas(plan.getMaxMascotas());
            dto.setMaxAlmacenamientoMb(plan.getMaxAlmacenamientoMb());
        } else {
            // Valores por defecto si no hay plan asignado
            dto.setIdPlanActual(null);
            dto.setNombrePlan("Sin Plan");
            dto.setMaxUsuarios(0);
            dto.setMaxDoctores(0);
            dto.setMaxMascotas(0);
            dto.setMaxAlmacenamientoMb(0);
        }
        
        dto.setEstadoSuscripcion(tenant.getEstadoSuscripcion() != null ? tenant.getEstadoSuscripcion().name() : "INACTIVO");
        
        dto.setNombrePropietario(tenant.getNombrePropietario());
        dto.setEmailPropietario(tenant.getEmailPropietario());
        dto.setTelefonoPropietario(tenant.getTelefonoPropietario());
        
        // Contar desde las tablas directamente
        dto.setUsuariosActivos((int) usuarioRepository.countByIdTenant(tenant.getIdTenant()));
        dto.setDoctoresActivos((int) doctorRepository.countByIdTenant(tenant.getIdTenant()));
        dto.setMascotasRegistradas((int) mascotaRepository.countByIdTenant(tenant.getIdTenant()));
        dto.setAlmacenamientoUsadoMb(tenant.getAlmacenamientoUsadoMb() != null ? tenant.getAlmacenamientoUsadoMb() : 0);
        
        dto.setLogoUrl(tenant.getLogoUrl());
        dto.setColorPrimario(tenant.getColorPrimario());
        dto.setColorSecundario(tenant.getColorSecundario());
        dto.setEstado(tenant.getEstado() != null ? tenant.getEstado().name() : "INACTIVO");
        
        return dto;
    }
}
