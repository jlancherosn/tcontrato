package com.tcontrato.dto;

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
public class Tco_formacionesDto {
	
	private String nivel_formacion;
	private String titulo_formacion;
	
}