package com.tcontrato.dto;

import com.tcontrato.model.Tco_ofertas;

public class Tco_ofertasMapper {

	public static Tco_ofertasDto toTco_ofertasDto(Tco_ofertas oferta) {
		
		return new Tco_ofertasDto()
				.setIdOferta(oferta.getIdOferta())
				.setTitulo(oferta.getTitulo_oferta());
		
	}
	
}