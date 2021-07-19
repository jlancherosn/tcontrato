package com.tcontrato.dto;

import com.tcontrato.model.Tco_formaciones;

public class Tco_formacionesMapper {

	public static Tco_formacionesDto toTco_formacionesDto(Tco_formaciones formacion) {
	
	return new Tco_formacionesDto()
			.setNivel_formacion(formacion.getNivel_formacion())
			.setTitulo_formacion(formacion.getTitulo_formacion());
	
	}
	
}