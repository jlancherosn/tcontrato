package com.tcontrato.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TCO_CANDIDATOS_FORMACIONES"
		, indexes = @Index(name = "IDX_CANFOR_01"
							, columnList = "ID_FORMACION_CANDIDATO"))
@IdClass(CompositeKey_Tco_canfor.class)
public class Tco_candidatos_formaciones {
	
	@Id
	@Column(name = "ID_CANDIDATO_FORMACION")
	private Integer id_candidato_formacion;
	
	@Id
	@Column(name = "ID_FORMACION_CANDIDATO")
	private Integer id_formacion_candidato;
	
}