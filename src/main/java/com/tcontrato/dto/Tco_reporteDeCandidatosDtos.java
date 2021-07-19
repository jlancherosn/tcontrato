package com.tcontrato.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tco_reporteDeCandidatosDtos {
	
	private Integer id;
	private Integer ponderacion;
	private String nombre;
	private String apellido;
	private String sexo;
	private String edad;
	private String ciudad_residencia;
	private String tiempo_experiencia;
	private List<String> nivel_formacion;
	private List<String> titulo_formacion;
	
}