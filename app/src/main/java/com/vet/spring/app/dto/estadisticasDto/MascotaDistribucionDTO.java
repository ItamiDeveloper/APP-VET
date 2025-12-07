package com.vet.spring.app.dto.estadisticasDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MascotaDistribucionDTO {
    private String tipo;
    private Long cantidad;
}
