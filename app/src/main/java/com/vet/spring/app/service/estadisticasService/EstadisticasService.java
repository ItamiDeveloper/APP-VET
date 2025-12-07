package com.vet.spring.app.service.estadisticasService;

import java.util.List;
import com.vet.spring.app.dto.estadisticasDto.EstadisticasGeneralesDTO;
import com.vet.spring.app.dto.estadisticasDto.IngresoMensualDTO;
import com.vet.spring.app.dto.estadisticasDto.CitaPorEstadoDTO;
import com.vet.spring.app.dto.estadisticasDto.MascotaDistribucionDTO;
import com.vet.spring.app.dto.estadisticasDto.ActividadRecienteDTO;

public interface EstadisticasService {
    EstadisticasGeneralesDTO getEstadisticasGenerales();
    List<IngresoMensualDTO> getIngresosMensuales();
    List<CitaPorEstadoDTO> getCitasPorEstado();
    List<MascotaDistribucionDTO> getMascotasDistribucion();
    List<ActividadRecienteDTO> getActividadReciente();
}
