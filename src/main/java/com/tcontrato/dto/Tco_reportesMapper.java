package com.tcontrato.dto;

import com.tcontrato.model.Tco_reportes;

public class Tco_reportesMapper {

	public static Tco_reportesDto toTco_reportesDto(Tco_reportes reporte) {
		return new Tco_reportesDto()
				.setCuerpo_reporte(reporte.getCuerpo_reporte())
				.setFecha_creacion(reporte.getFecha_creacion())
				.setId_reporte_oferta(reporte.getId_reporte_oferta().getIdOferta());
	}
	
}