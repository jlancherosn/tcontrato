package com.tcontrato.dto;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tco_candidatosDto implements Comparable<Tco_candidatosDto> {
	
	private Integer idCandidato;	
	private String nombre_candidato;	
	private String apellido_candidato;
	private String ciudad_residencia;
	private String sexo;
	private String edad;
	private String tiempo_experiencia;
	private Set<Tco_formacionesDto> formaciones;
	private List<Tco_ofertasDto> ofertas;
	
	@Override
	public int compareTo(Tco_candidatosDto o) {
		// TODO Auto-generated method stub
		return this.getIdCandidato().compareTo(((Tco_candidatosDto) o).getIdCandidato());
	}
	
}