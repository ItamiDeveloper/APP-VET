package com.vet.spring.app.mapper.doctorMapper;

import org.springframework.stereotype.Component;
import com.vet.spring.app.dto.doctorDto.DoctorDTO;
import com.vet.spring.app.entity.doctor.Doctor;

@Component
public class DoctorMapper {

    public DoctorDTO toDTO(Doctor doctor) {
        if (doctor == null) return null;
        
        DoctorDTO dto = new DoctorDTO();
        dto.setIdDoctor(doctor.getIdDoctor());
        dto.setIdTenant(doctor.getTenant() != null ? doctor.getTenant().getIdTenant() : null);
        dto.setIdUsuario(doctor.getUsuario() != null ? doctor.getUsuario().getIdUsuario() : null);
        dto.setNombres(doctor.getNombres());
        dto.setApellidos(doctor.getApellidos());
        dto.setColegiatura(doctor.getColegiatura());
        dto.setEspecialidad(doctor.getEspecialidad());
        dto.setTelefono(doctor.getTelefono());
        dto.setEmail(doctor.getEmail());
        dto.setEstado(doctor.getEstado() != null ? doctor.getEstado().name() : null);
        
        return dto;
    }

    public Doctor toEntity(DoctorDTO dto) {
        if (dto == null) return null;
        
        Doctor doctor = new Doctor();
        doctor.setIdDoctor(dto.getIdDoctor());
        doctor.setNombres(dto.getNombres());
        doctor.setApellidos(dto.getApellidos());
        doctor.setColegiatura(dto.getColegiatura());
        doctor.setEspecialidad(dto.getEspecialidad());
        doctor.setTelefono(dto.getTelefono());
        doctor.setEmail(dto.getEmail());
        
        if (dto.getEstado() != null) {
            doctor.setEstado(Doctor.EstadoDoctor.valueOf(dto.getEstado()));
        }
        
        return doctor;
    }
}
