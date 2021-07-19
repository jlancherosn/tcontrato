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
@Table(name = "TCO_CANDIDATOS_OFERTAS"
		, indexes = @Index(name = "IDX_CANOFE_01"
							, columnList = "ID_OFERTA_CANDIDATO"))
@IdClass(CompositeKey_Tco_canofe.class)
public class Tco_candidatos_ofertas {
	
	@Id
	@Column(name = "ID_CANDIDATO_OFERTA")
	private Integer id_candidato_oferta;
	
	@Id
	@Column(name = "ID_OFERTA_CANDIDATO")
	private Integer id_oferta_candidato;
	
	@Column(name = "PONDERACION")
	private float ponderacion;
	
}