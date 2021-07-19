package com.tcontrato.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.tcontrato.model.Tco_candidatos;

public class Tco_candidatosMapper{

	public static Tco_candidatosDto toTco_candidatosDto(Tco_candidatos candidato) {
		
		return new Tco_candidatosDto()
				.setIdCandidato(candidato.getIdCandidato())
				.setNombre_candidato(candidato.getNombre_candidato())
				.setApellido_candidato(candidato.getApellido_candidato())
				.setCiudad_residencia(candidato.getCiudad_residencia())
				.setSexo(candidato.getSexo())
				.setEdad(candidato.getEdad())
				.setTiempo_experiencia(candidato.getTiempo_experiencia())
				.setFormaciones(new HashSet<Tco_formacionesDto>((candidato
						.getFormaciones()
						.stream()
						.map(formacion -> new ModelMapper().map(formacion, Tco_formacionesDto.class))
						.collect(Collectors.toSet()))))
				.setOfertas(new ArrayList<Tco_ofertasDto>((candidato
						.getOfertas()
						.stream()
						.map(oferta -> new ModelMapper().map(oferta, Tco_ofertasDto.class))
						.collect(Collectors.toList()))));
	}

}