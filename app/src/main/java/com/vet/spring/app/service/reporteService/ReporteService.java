package com.vet.spring.app.service.reporteService;

import java.time.LocalDate;
import java.util.List;
import com.vet.spring.app.dto.reporteDto.ReporteVentasDTO;
import com.vet.spring.app.dto.reporteDto.ReporteCitasDTO;
import com.vet.spring.app.dto.reporteDto.ResumenVentasDTO;
import com.vet.spring.app.dto.reporteDto.ResumenCitasDTO;

public interface ReporteService {
    List<ReporteVentasDTO> getReporteVentasPorFecha(LocalDate fechaInicio, LocalDate fechaFin);
    List<ReporteCitasDTO> getReporteCitasPorFecha(LocalDate fechaInicio, LocalDate fechaFin);
    ResumenVentasDTO getResumenVentas(LocalDate fechaInicio, LocalDate fechaFin);
    ResumenCitasDTO getResumenCitas(LocalDate fechaInicio, LocalDate fechaFin);
    List<ReporteCitasDTO> getCitasDelDia();
    List<ReporteVentasDTO> getVentasDelMes();
}
