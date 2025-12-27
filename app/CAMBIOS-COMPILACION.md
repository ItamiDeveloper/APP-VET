# Cambios necesarios para compilar el backend

## 1. Controllers - Cambiar getCurrentTenantId() por getTenantId()
   - UsuarioController, ClienteController, MascotaController, CitaController, DoctorController

## 2. Services - Cambiar mapper::toDTO por mapper.toDTO() (métodos static)
   - UsuarioService, ClienteService, MascotaService, CitaService, DoctorService

## 3. Services - Corregir enums de entities
   - CitaService: `EstadoCita` → `Cita.EstadoCita`
   - ClienteService: `TipoDocumento` → `Cliente.TipoDocumento`
   
## 4. DoctorService - Quitar `setFechaRegistro()` que no existe en Doctor entity

## 5. Entities a revisar:
   - Cita.java - enum EstadoCita
   - Cliente.java - enum TipoDocumento
   - Doctor.java - verificar campos
