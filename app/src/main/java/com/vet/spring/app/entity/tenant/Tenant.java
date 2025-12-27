package com.vet.spring.app.entity.tenant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenant")
@Getter @Setter
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tenant")
    private Integer idTenant;

    @Column(name = "codigo_tenant", nullable = false, unique = true, length = 50)
    private String codigoTenant;

    @Column(name = "nombre_comercial", nullable = false, length = 150)
    private String nombreComercial;

    @Column(name = "razon_social", length = 200)
    private String razonSocial;

    @Column(length = 20)
    private String ruc;

    @Column(length = 30)
    private String telefono;

    @Column(name = "email_contacto", nullable = false, length = 100)
    private String emailContacto;

    @Column(length = 255)
    private String direccion;

    @Column(length = 50)
    private String pais = "Perú";

    @Column(length = 100)
    private String ciudad;

    // Configuración del tenant
    @ManyToOne
    @JoinColumn(name = "id_plan_actual", nullable = false)
    private Plan planActual;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_suscripcion", nullable = false)
    private EstadoSuscripcion estadoSuscripcion = EstadoSuscripcion.TRIAL;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "fecha_activacion")
    private LocalDateTime fechaActivacion;

    @Column(name = "fecha_suspension")
    private LocalDateTime fechaSuspension;

    @Column(name = "dias_trial", nullable = false)
    private Integer diasTrial = 15;

    // Propietario
    @Column(name = "nombre_propietario", nullable = false, length = 150)
    private String nombrePropietario;

    @Column(name = "email_propietario", nullable = false, length = 100)
    private String emailPropietario;

    @Column(name = "telefono_propietario", length = 30)
    private String telefonoPropietario;

    // Uso y límites
    @Column(name = "usuarios_activos", nullable = false)
    private Integer usuariosActivos = 0;

    @Column(name = "doctores_activos", nullable = false)
    private Integer doctoresActivos = 0;

    @Column(name = "mascotas_registradas", nullable = false)
    private Integer mascotasRegistradas = 0;

    @Column(name = "almacenamiento_usado_mb", nullable = false)
    private Integer almacenamientoUsadoMb = 0;

    // Metadata
    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @Column(name = "color_primario", length = 7)
    private String colorPrimario = "#3B82F6";

    @Column(name = "color_secundario", length = 7)
    private String colorSecundario = "#10B981";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTenant estado = EstadoTenant.ACTIVO;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public enum EstadoSuscripcion {
        TRIAL, ACTIVO, SUSPENDIDO, CANCELADO
    }

    public enum EstadoTenant {
        ACTIVO, INACTIVO
    }

    // Métodos helper para validar límites
    public boolean puedeAgregarUsuario() {
        return usuariosActivos < planActual.getMaxUsuarios();
    }

    public boolean puedeAgregarDoctor() {
        return doctoresActivos < planActual.getMaxDoctores();
    }

    public boolean puedeAgregarMascota() {
        return mascotasRegistradas < planActual.getMaxMascotas();
    }

    public boolean tieneEspacioAlmacenamiento(int mbRequeridos) {
        return (almacenamientoUsadoMb + mbRequeridos) <= planActual.getMaxAlmacenamientoMb();
    }
}
