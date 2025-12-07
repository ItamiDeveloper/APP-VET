package com.vet.spring.app.service.doctorService;

import java.util.List;

import com.vet.spring.app.dto.doctorDto.DoctorDTO;

public interface DoctorService {
    List<DoctorDTO> getAllDoctores();
    DoctorDTO getDoctorById(Integer id);
}
