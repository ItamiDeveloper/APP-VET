package com.vet.spring.app.service.doctorService.doctorImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.vet.spring.app.dto.doctorDto.DoctorDTO;
import com.vet.spring.app.entity.doctor.Doctor;
import com.vet.spring.app.repository.doctorRepository.DoctorRepository;
import com.vet.spring.app.service.doctorService.DoctorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public List<DoctorDTO> getAllDoctores() {
        return doctorRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public DoctorDTO getDoctorById(Integer id) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
        return convertToDTO(doctor);
    }

    private DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setIdDoctor(doctor.getIdDoctor());
        dto.setIdVeterinaria(doctor.getVeterinaria().getIdVeterinaria());
        if (doctor.getUsuario() != null) {
            dto.setIdUsuario(doctor.getUsuario().getIdUsuario());
        }
        dto.setNombres(doctor.getNombres());
        dto.setApellidos(doctor.getApellidos());
        dto.setColegiatura(doctor.getColegiatura());
        dto.setEspecialidad(doctor.getEspecialidad());
        dto.setEstado(doctor.getEstado().name());
        return dto;
    }
}
