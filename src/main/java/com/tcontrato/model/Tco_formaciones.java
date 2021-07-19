package com.tcontrato.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Data

@Entity
@Table(name = "TCO_FORMACIONES"
		, indexes = @Index(name = "IDX_FORMAC_01"
							, columnList = "NIVEL_FORMACION, TITULO_FORMACION"))
public class Tco_formaciones {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDFORMACION", updatable = false)
	private Integer idFormacion;
	
	@Column(name = "NIVEL_FORMACION")
	private String nivel_formacion;
	
	@Column(name = "TITULO_FORMACION")
	private String titulo_formacion;
	
	@ManyToMany(mappedBy = "formaciones", fetch = FetchType.LAZY) // Se mapea al nombre de la lista tipo Tco_formaciones definida en Tco_candidatos
	private List<Tco_candidatos> candidatos;
	
}