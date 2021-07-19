package com.tcontrato.dto;

import java.util.Date;

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
public class Tco_reportesDto {

	private Integer idReporte;
	private String cuerpo_reporte;
	private Integer id_reporte_oferta;
	private Date fecha_creacion;
	
}