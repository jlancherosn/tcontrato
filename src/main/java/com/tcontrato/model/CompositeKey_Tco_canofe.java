package com.tcontrato.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CompositeKey_Tco_canofe implements Serializable{
	
	private int id_candidato_oferta;
	private Integer id_oferta_candidato;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_candidato_oferta;
		result = prime * result + ((id_oferta_candidato == null) ? 0 : id_oferta_candidato.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompositeKey_Tco_canofe other = (CompositeKey_Tco_canofe) obj;
		if (id_candidato_oferta != other.id_candidato_oferta)
			return false;
		if (id_oferta_candidato == null) {
			if (other.id_oferta_candidato != null)
				return false;
		} else if (!id_oferta_candidato.equals(other.id_oferta_candidato))
			return false;
		return true;
	}	
	
}