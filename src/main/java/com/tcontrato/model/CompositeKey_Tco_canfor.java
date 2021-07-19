package com.tcontrato.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CompositeKey_Tco_canfor implements Serializable {
	
	private int id_candidato_formacion;
	private int id_formacion_candidato;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_candidato_formacion;
		result = prime * result + id_formacion_candidato;
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
		CompositeKey_Tco_canfor other = (CompositeKey_Tco_canfor) obj;
		if (id_candidato_formacion != other.id_candidato_formacion)
			return false;
		if (id_formacion_candidato != other.id_formacion_candidato)
			return false;
		return true;
	}	
	
}