package com.tcontrato.config;

import java.sql.Types;
import org.hibernate.dialect.Oracle12cDialect;
import org.hibernate.type.StandardBasicTypes;

public class CustomDialectOracle extends Oracle12cDialect {

	public CustomDialectOracle() {
		super();
		registerHibernateType(Types.NCHAR, StandardBasicTypes.CHARACTER.getName());
		registerHibernateType(Types.NCHAR, 1, StandardBasicTypes.CHARACTER.getName());
		registerHibernateType(Types.NCHAR, 255, StandardBasicTypes.STRING.getName());
		registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
		registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.TEXT.getName());
		registerHibernateType(Types.NCLOB, StandardBasicTypes.CLOB.getName());
	}

}