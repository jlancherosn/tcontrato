package com.tcontrato.service;

import javax.sql.DataSource;

import javax.transaction.Transactional;

import org.apache.commons.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import com.tcontrato.dto.Tco_ofertasDto;
import com.tcontrato.model.Tco_candidatos;
import com.tcontrato.repository.Invocador;
import com.tcontrato.repository.Tco_candidatosRepo;
import com.tcontrato.repository.Tco_formacionesRepo;
import com.tcontrato.repository.Tco_ofertasRepo;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class Tco_segmentacionServiceImplementacion implements Tco_segmentacionService{
	
	//Instanciamos las clases que se usar�n en la implementaci�n del servicio de segmentaci�n de candidatos
	@Autowired
	private Tco_candidatosRepo tco_candidatosRepo;
	
	@Autowired
	private Tco_ofertasRepo tco_ofertasRepo;
	
	@Autowired
	private Tco_formacionesRepo tco_formacionesRepo;
    
	//El invocador lo usamos para ejecutar instrucciones JPQL que traingan informaci�n a la BD
    @Autowired
	private Invocador invocador;
    
    //El ModelMapper nos sirve para mapear datos de las entities (tablas) en sus correspondientes DTO's
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Constructor de la clase Tco_segmentacionServiceImplementaci�n. Usamos como par�metros los repositorios necesarios para llevar
     * a cabo el proceso de segmentaci�n, as� como el de generar los reportes.
     * @param tco_candidatosRepo
     * @param tco_ofertasRepo
     * @param tco_formacionesRepo
     * @param invocador
     * @param modelMapper
     */
	public Tco_segmentacionServiceImplementacion(Tco_candidatosRepo tco_candidatosRepo,
			Tco_ofertasRepo tco_ofertasRepo,
			Tco_formacionesRepo tco_formacionesRepo,
			Invocador invocador,
			ModelMapper modelMapper) {
		this.tco_candidatosRepo = tco_candidatosRepo;
		this.tco_ofertasRepo = tco_ofertasRepo;
		this.tco_formacionesRepo = tco_formacionesRepo;
		this.invocador = invocador;
		this.modelMapper = modelMapper;
	}
	
	/**
	 * El m�todo mostrarOfertas consulta todas las ofertas laborales existentes en la BD, para esto usa el m�todo findAll de
	 *  la interfaz de JpaRepository.
	 * @return Retorna la lista de las ofertas, la lista es de tipo Tco_ofertasDto (objetos con atributos idOferta, titulo y fecha).
	 */
	@Override
	public List<Tco_ofertasDto> mostrarOfertas(){
		return tco_ofertasRepo.findAll().stream()
              .map(candidato -> modelMapper.map(candidato, Tco_ofertasDto.class))
              .collect(Collectors.toList());
	}
	
	/**
	 * El m�todo listarCriterios consulta los posibles campos de las tablas de la base de datos para hacer el proceso de segmentaci�n,
	 * adem�s los transforma para mostrarlos de manera adecuada en la vista del aplicativo donde sean llamados.
	 * @return Retorna la lista de todos los posibles criterios para hacer la segmentaci�n de candidatos, la lista es formateada
	 * para mostrar strings en lenguaje natural.
	 */
	@Override
	public ArrayList<ArrayList<String>> listarCriterios() {
		String string = invocador.listarCriterios();		
		string = string.replace("_", " ");
		String[] lista = string.split(",");
		ArrayList<ArrayList<String>> listaCapital = new ArrayList<ArrayList<String>>();
		for(String criterio : lista) {
			ArrayList<String> sublista = new ArrayList<String>();
			if(criterio.equals("NIVEL FORMACION")) {
				sublista.add("Nivel Formaci�n");
				sublista.add("cuadroTexto");
				listaCapital.add(sublista);
			}
			else if(criterio.equals("TITULO FORMACION")) {
				sublista.add("T�tulo Formaci�n");
				sublista.add("cuadroTexto");
				listaCapital.add(sublista);
			}
			else if(criterio.equals("CIUDAD RESIDENCIA") || criterio.equals("SEXO")) {				
				sublista.add(WordUtils.capitalizeFully(criterio));
				sublista.add("listaSeleccion");
				listaCapital.add(sublista);
			}
			else if(criterio.equals("EDAD") || criterio.equals("TIEMPO EXPERIENCIA")) {
				sublista.add(WordUtils.capitalizeFully(criterio));
				sublista.add("slider");
				listaCapital.add(sublista);
			}
		}
		return listaCapital;
	}
	
	/**
	 * El m�todo nombreTabla trae el nombre de la tabla de la BD a la que pertenece el criterio que entra como par�metro, para esto
	 * usa el m�todo obtenerNombreTabla definifo en la clase com.tcontrato.repository/Invocador.
	 * @param criterio El criterio del que se quiere conocer el nombre de la tabla a la que pertenece.
	 * @return Retorna el nombre de la tabla a la que pertenece el criterio.
	 */
	@Override
	public String nombreTabla(String criterio) {
		String string = invocador.obtenerNombreTabla(criterio);
		return string;
	}
	
	/**
	 * El m�todo aliasTabla trae el nombre del alias de la tabla de la BD a la que pertenece el criterio que entra como par�metro, para
	 *  esto usa el m�todo obtenerAlias de la clase com.tcontrato.repository/Invocador.
	 *  @param criterio El criterio del que se quiere conocer el nombre del alias de la tabla a la que pertenece.
	 *  @return Retorna el nombre del alias de la tabla a la que pertenece el criterio.
	 */
	@Override
	public String aliasTabla(String criterio) {
		String string = invocador.obtenerAlias(criterio);
		return string;
	}
	
	/*
	 * Debido a que el modelo de seh�gmentaci�n es din�mico (se pueden escoger distintos criterios, ponderaciones y prioridades cada vez)
	 * consider� necesario crear los dos siguientes m�todos que se encargan de construir los strings que contienen las declaraciones SQL
	 * necesarias para llevar a cabo el proceso de segmentaci�n sobre una oferta laboral determinada, estos strings son usados en instrucciones
	 *	OPEN-FOR y EXECUTE IMMEDIATE dentro de un procedimiento de la BD
	 */
	/**
	 * El m�todo crearQueryPonderaci�n construye el string apropiado para hacer el SELECT FROM + INNER JOIN + WHERE seg�n los criterios,
	 * las ponderaciones y las prioridades establecidas por el usuario, este query es el que se abre con el cursor referenciado en el
	 * procedimiento CALCULARPONDERACION definido en la BD.
	 * @param criteriosSeleccionados Los criterios que elige el usuario para hacer el proceso e segmentaci�n.
	 * @param nombresTablas Los nombres de las tablas a la que pertenece cada criterio.
	 * @param ponderaciones Los valores de las ponderaciones que elige el usuario para hacer el proceso e segmentaci�n.
	 * @param prioridades Las prioridades que elige el usuario para hacer el proceso e segmentaci�n.
	 * @param id_ofeta_candidato El Id de la oferta laboral sobre la que se va a realizar la segmentaci�n.
	 * @return Retorna el string que contiene la instrucci�n conformada por palabras claves como SELECT FROM + INNER JOIN.
	 */
	@Override
	public String crearQueryPonderacion(List<String> criteriosSeleccionados, 
			List<String> nombresTablas,
			List<Integer> ponderaciones,
			List<List<List<Object>>> prioridades,
			Integer id_oferta_candidato) {
		
		//Comienzo del cuerpo del m�todo y de la creaci�n de la instrucci�n SQL
		String query = "SELECT idCandidato, id_oferta_candidato ";
		
		//se concatena cada criterio elegido a la variable query
		for(String criterio : criteriosSeleccionados) {
			query = query + ", " + criterio;
		}
		
		/*Finalmente se completa la variable query con las instrucciones que permiten hacer el enlace con las tablas necesarias: las
		 *que contienen la informaci�n b�sica, acad�mica o econ�mica de los candidatos
		 */
		query = query + " ";
		query = query + "\nFROM tco_candidatos \n"
				+ "INNER JOIN tco_candidatos_ofertas ON idCandidato = id_candidato_oferta \n"
				+ "INNER JOIN tco_candidatos_formaciones ON idCandidato = id_candidato_formacion \n"
				+ "INNER JOIN tco_formaciones ON id_formacion_candidato = idformacion \n"
				+ "WHERE id_oferta_candidato = " + id_oferta_candidato;
		return query;
		
	}

	/**
	 * El m�todo crearSentenciaPonderacion construye el string apropiado para hacer el c�lculo de los valores de ponderaci�n correspondientes
	 * a cada candidato de la oferta laboral seleccionada, este string es el que es ejecutado dentro de la instrucci�n EXECUTE IMMEDIATE 
	 * del procedimiento CALCULARPONDERACION definido en la BD.
	 * @param criteriosSeleccionados Los criterios que elige el usuario para hacer el proceso e segmentaci�n.
	 * @param ponderaciones Los valores de las ponderaciones que elige el usuario para hacer el proceso e segmentaci�n.
	 * @param prioridades Las prioridades que elige el usuario para hacer el proceso e segmentaci�n.
	 * @param id_ofeta_candidato El Id de la oferta laboral sobre la que se va a realizar la segmentaci�n.
	 * @return Retorna el string que contiene la instrucci�n donde se declaran las variables que se usan dentro del conjunto de instrucciones
	 * del tipo "IF (variable) IN ('valor 1', 'valor 2', ..., 'valor n') THEN ponderacion = ...".
	 */
	@Override
	public String crearSentenciaPonderacion(List<String> criteriosSeleccionados,
			List<Integer> ponderaciones,
			List<List<List<Object>>> prioridades,
			Integer id_oferta_candidato) {
		
		//Comienzo del m�todo y de la creaci�n de la instrucci�n PLSQL		
		String sentencia = "DECLARE"
							+ "\n	id_candidato INTEGER;"
							+ "\n	id_oferta_laboral INTEGER;"
							+ "\n	ponderacion_total INTEGER;";
		
		//Lista auxiliar para la construcci�n de la sentencia
		List<String> numeros = new ArrayList<>(Arrays.asList("Uno", "Dos", "Tres", "Cuatro", "Cinco"));
		for (String numero: numeros) {
			sentencia = sentencia + "\n	parametro" + numero + " VARCHAR2(100);";
		}
		
		//Instrucciones para la construcci�n de las variables que se usar�n dentro de las instrucciones "IF (variable) THEN..."
		sentencia = sentencia + "\nBEGIN\n	id_candidato := :id_candidato;"
				+ "\n	id_oferta_laboral := :id_oferta_laboral;"
				+ "\n	ponderacion_total := :ponderacion_total;";		
		for (String numero: numeros) {
			sentencia = sentencia + "\n	parametro" + numero + " := " + ":parametro" + numero + ";";
		}
		
		//Instrucciones para la continuaci�n de la construcci�n de las instrucciones IF(variable) IN ('valor 1', ..., 'valor n') THEN ponderacion = ..."
		for(int i = 0; i < 5; i++) {
			List<List<Object>> prioridadesCriterios = prioridades.get(i);
			
			//If que valida si el criterio es de tipo String o no, si es de tipo String significa que la instrucci�n condicional es de
			//la forma "IF (variable) IN ('valor 1', 'valor 2', ..., 'valor n')"
			if(prioridadesCriterios.get(0).get(0) instanceof String) {
				sentencia = sentencia + "\nIF parametro" + numeros.get(i) + " IN (";
				for(List<Object> prioridad : prioridadesCriterios) {
					for(Object prioridadInstancia : prioridad) {
						Object last_value = (prioridadesCriterios.get(prioridadesCriterios.size()-1))
											.get(prioridadesCriterios.get(prioridadesCriterios.size()-1).size()-1);
						boolean condicion = prioridadInstancia == last_value;
						if(condicion) {
							sentencia = sentencia + "'" + prioridadInstancia + "'" + ") THEN\n";
						}
						else {
							sentencia = sentencia + "'" + prioridadInstancia + "'" + ", ";
						}
					}
				}
			}
			/*
			 * Si el criterio no es de tipo String hay dos opciones m�s, que su tipo sea un array de dos n�meros (por ejemplo un rango de edades, la edad debe estar entre los valores x e y).
			 * O que sea de tipo un s�lo n�mero (por ejemplo el caso de tiempo de experiencia, el candidato debe tener m�nimo x a�os de experiencia)
			 */
			else {
				//If que valida si el criterio es de tipo un s�lo n�mero
				if(prioridadesCriterios.get(0).size() == 1) {
					sentencia = sentencia + "\nIF ";
					for(List<Object> prioridad : prioridadesCriterios) {
						if(prioridad == prioridadesCriterios.get(prioridadesCriterios.size()-1)){
							sentencia = sentencia + " parametro" + numeros.get(i) + " >= " + prioridad.get(0) + " THEN\n";
						}
						else {
							sentencia = sentencia + " parametro" + numeros.get(i) + " >= " + prioridad.get(0) + " OR";
						}
					}
				}
				//Cuando el criterio es de tipo array de dos n�meros
				else {
					sentencia = sentencia + "\nIF parametro" + numeros.get(i) + " BETWEEN ";
					for(List<Object> prioridad : prioridadesCriterios) {
						if(prioridad == prioridadesCriterios.get(prioridadesCriterios.size()-1)){
							sentencia = sentencia + prioridad.get(0) + " AND " + prioridad.get(1) + " THEN\n";
						}
						else {
							sentencia = sentencia + prioridad.get(0) + " AND " + prioridad.get(1) + " OR parametro"
										+ numeros.get(i) + " BETWEEN ";
						}
					}
				}				
			}
			//Se concatenan las instrucciones "ponderacion = ponderacio + ponderacion_asignada_criterio"
			sentencia = sentencia + "	ponderacion_total := ponderacion_total + " + ponderaciones.get(i)
						+ ";\nEND IF;";
		}
		
		//Finalmente se concatena la instrucci�n UPDATE para registrar las ponderaciones obtenidas en la tabla TCO_CANDIDATOS_OFERTAS
		sentencia = sentencia + "\nUPDATE tco_candidatos_ofertas SET ponderacion = ponderacion_total \r\n"
				+ "WHERE id_candidato_oferta = id_candidato AND id_oferta_candidato = id_oferta_laboral;\r\n"
				+ "ponderacion_total := 0;"
				+ "\nEND;";
		return sentencia;
	}
	
	
	/**
	 * El m�todo calcularPonderaci�n es el que establece la conexi�n con JDBC a la BD y llama al procedimiento calcularPonderacion.
	 * @param queryStringEntrada El string que es generado con el m�todo crearQueryPonderacion
	 * @param listaCriteriosElegidos Los criterios que elige el usuario para hacer el proceso e segmentaci�n.
	 * @param querySentenciaponderacion El string que es generado con el m�todo crearSentenciaPonderacion
	 */
	@Override
	public void calcularPonderacion(String queryStringEntrada,
			List<String> listaCriteriosElegidos,
			String querySentenciaponderacion) throws ClassNotFoundException, SQLException {
		
		//Variables de conexi�n a la BD
		String driver = "oracle.jdbc.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
        String usuario	= "posiciones_admin";
        String pass= "admin1234";
        
        //Conexi�n a la BD
        Class.forName(driver);
        //Connection conn = DriverManager.getConnection(url, usuario, pass);
	    DataSource dataSource = new DriverManagerDataSource(url, usuario, pass);
	    JdbcTemplate template = new JdbcTemplate(dataSource);
	    
	    //Llamado al procedimiento almacenado
	    try {
            //Crear el JDCB Call para el procedimiento almacenado y registrar los parametros de entrada y salida
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(template)
                    .withProcedureName("calcularPonderacion")
                    .declareParameters(
                            new SqlParameter("queryStringEntrada", Types.VARCHAR),
                            new SqlParameter("primerCriterioElegido", Types.VARCHAR),
                            new SqlParameter("segundoCriterioElegido", Types.VARCHAR),
                            new SqlParameter("tercerCriterioElegido", Types.VARCHAR),
                            new SqlParameter("cuartoCriterioElegido", Types.VARCHAR),
                            new SqlParameter("quintoCriterioElegido", Types.VARCHAR),
                            new SqlParameter("querySentenciaponderacion", Types.VARCHAR)
                    );

            // Mapear los parametros de entrada del SP
            MapSqlParameterSource paramMap = new MapSqlParameterSource()
                    .addValue("queryStringEntrada", queryStringEntrada)
                    .addValue("primerCriterioElegido", listaCriteriosElegidos.get(0))
            		.addValue("segundoCriterioElegido", listaCriteriosElegidos.get(1))
            		.addValue("tercerCriterioElegido", listaCriteriosElegidos.get(2))
            		.addValue("cuartoCriterioElegido", listaCriteriosElegidos.get(3))
            		.addValue("quintoCriterioElegido", listaCriteriosElegidos.get(4))
            		.addValue("querySentenciaponderacion", querySentenciaponderacion);
            
            @SuppressWarnings("unused")
            Map<String, Object> resultMap = jdbcCall.execute(paramMap);
        }
        catch (Exception ex) {
            System.out.println(ex);
        }

    }
	
	/*
	 * Similar a como hicimos para llevar a cabo el proceso de segmentaci�n, creando do strings para ejecutar queries din�micos, se opta por
	 *crear otro string conformado por otra instrucci�n SELECT que se encarga de la generaci�n del reporte y su almacenamiento en la BD
	 */
	/**
	 * El m�todo crearSentenciaReporte se encarga de construir la sentencia que ser� ejecutada dentro de la instrucci�n OPEN-FOR del
	 * procedimiento almacenado GENERARRPORTE de la BD, la sentencia tiene una estructura de la forma
	 * "SELECT FROM tco_candidatos_ofertas
	 * INNER JOIN tco_candidatos ON ...
	 * INNER JOIN tco_candidatos_formaciones ON ...
	 * INNER JOIN tco_formaciones ON ...
	 * WHEN ...
	 * ORDER BY
		 * 	ponderacion DESC,
		 * (CASE A THEN ...) ASC,
		 * (CASE B THEN ...) ASC,
		 * ...
		 * (CASE E THEN ...) ASC".
	 * @param idOferta El Id de la oferta a la que se crear� la sentencia del reporte.
	 * @param Los criterios que elige el usuario para hacer el proceso e segmentaci�n.
	 * @param prioridades Las prioridades que elige el usuario para hacer el proceso e segmentaci�n.
	 * @return Retorna el string adecuado para llevar a cabo la construcci�n del reporte asociado a la oferta laboral seleccionada y
	 * determinado por los par�metros de segmentaci�n escogidos por el usuario.
	 */
	@Override
	public String crearSentenciaReporte(Integer idOferta,
			List<String> criteriosSeleccionados,
			List<List<List<Object>>> prioridades){
		
		//Comienzo del m�todo y de la construcci�n de la sentencia SQL
		String sentencia = "SELECT canofe.id_candidato_oferta\n"
				+ "FROM\n"
				+ "	tco_candidatos_ofertas canofe\n"
				+ "INNER JOIN\n"
				+ "	tco_candidatos candid ON canofe.id_candidato_oferta = candid.idCandidato\n"
				+ "INNER JOIN\n"
				+ "	tco_candidatos_formaciones canfor ON canofe.id_candidato_oferta = canfor.id_candidato_formacion\n"
				+ "INNER JOIN\n"
				+ "	tco_formaciones formac ON canfor.id_formacion_candidato = formac.idFormacion\n"
				+ "WHERE\n"
				+ "	canofe.id_oferta_candidato = " + idOferta + "\n"
				+ "ORDER BY\n"
				+ "	canofe.ponderacion DESC,";
		
		//Concatenaci�n de las instruccciones (CASE ... THEN ...) ASC
		for(int i = 0; i <= criteriosSeleccionados.size() - 1; i++) {
			List<List<Object>> prioridadesCriterio = prioridades.get(i);
			boolean isString = prioridadesCriterio.get(0).get(0) instanceof String;
			
			/*
			 * If que valida si el criterio es de tipo String o no. Si es String entonces la estructura de la sentencia queda de la forma 
			 * "CASE variable IN ('valor 1', ..., 'valor n') THEN valor_num�rico"
			 */
			if (isString) {
				sentencia = sentencia + "\n	(CASE";
				for(List<Object> prioridadArray : prioridadesCriterio) {
					sentencia = sentencia + "\n		WHEN "+ aliasTabla(nombreTabla(criteriosSeleccionados.get(i))) + "."
							+ criteriosSeleccionados.get(i) + " IN (";
					for (Object prioridadInstancia : prioridadArray) {
						if (prioridadInstancia == prioridadArray.get(prioridadArray.size()-1)) {
							sentencia = sentencia + "'" + prioridadInstancia + "'";
						}
						else {
							sentencia = sentencia + "'" + prioridadInstancia + "'" + ", ";
						}						
					}
					sentencia = sentencia + ") THEN " + (prioridadesCriterio.indexOf(prioridadArray) + 1);
				}
				sentencia = sentencia + "\n		ELSE " + (prioridadesCriterio.size() + 1) + "\n	END) ASC,";
			}
			/*Si el criterio no es de tipo String hay dos opciones m�s, que su tipo sea un array de dos n�meros (por ejemplo un rango de edades, la edad debe estar entre los valores x e y).
			 *O que sea de tipo un s�lo n�mero (por ejemplo el caso de tiempo de experiencia, el candidato debe tener m�nimo x a�os de experiencia)
			 */
			else {
				/*
				 * If que valida si el criterio es de tipo array de n�meros, en este caso la estructura de la sentencia queda de la forma
				 *"CASE variable BETWEEN valor_1 AND valor_2 THEN valor_num�rico"
				 */
				if(prioridadesCriterio.get(0).size() > 1) {
					sentencia = sentencia + "\n	(CASE";
					for(List<Object> prioridadArray : prioridadesCriterio) {
						sentencia = sentencia + "\n		WHEN "+ aliasTabla(nombreTabla(criteriosSeleccionados.get(i))) + "."
								+ criteriosSeleccionados.get(i) + " BETWEEN ";
						for (Object prioridadInstancia : prioridadArray) {
							if (prioridadInstancia == prioridadArray.get(prioridadArray.size()-1)) {
								sentencia = sentencia + prioridadInstancia;
							}
							else {
								sentencia = sentencia + prioridadInstancia + " AND ";
							}						
						}
						sentencia = sentencia + " THEN " + (prioridadesCriterio.indexOf(prioridadArray) + 1);
					}
					sentencia = sentencia + "\n		ELSE " + (prioridadesCriterio.size() + 1) + "\n	END) ASC,";
				}
				//Si el criterio es de tipo un s�lo n�mero la estructura de la sentencia queda de la forma
				//"CASE variable >= valor_1 THEN valor_num�rico"
				else {
					sentencia = sentencia + "\n	(CASE";
					for(List<Object> prioridadArray : prioridadesCriterio) {
						sentencia = sentencia + "\n		WHEN "+ aliasTabla(nombreTabla(criteriosSeleccionados.get(i))) + "."
								+ criteriosSeleccionados.get(i) + " >= " + prioridadArray.get(0);
						sentencia = sentencia + " THEN " + (prioridadesCriterio.indexOf(prioridadArray) + 1);
					}
					sentencia = sentencia + "\n		ELSE " + (prioridadesCriterio.size() + 1) + "\n	END) ASC,";
				}
			}
		}
		
		//Finalmente se quita la coma del final para terminar la construcci�n del string adecuado
		String sentenciaFinal = sentencia.substring(0, sentencia.length()-1);
		return sentenciaFinal;
		
	}
	
	/**
	 * El m�todo generarSegmentaci�n es el que establece la conexi�n con JDBC a la BD y llama al procedimiento generarReporte.
	 * @return Retorna el string (la variable segmentaci�n) que recoge el resultado de la segmentaci�n generada.
	 */
	@Override
	public String generarSegmentacion(String stringReporte) throws ClassNotFoundException, SQLException {
		String driver = "oracle.jdbc.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
        String usuario	= "posiciones_admin";
        String pass= "admin1234";
        
        Class.forName(driver);
        //Connection conn = DriverManager.getConnection(url, usuario, pass);
	    DataSource dataSource = new DriverManagerDataSource(url, usuario, pass);
	    JdbcTemplate template = new JdbcTemplate(dataSource);
	    
	    try {
            //Crear el JDCB Call para el procedimiento almacenado y registrar los parametros de entrada y salida
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(template)
                    .withProcedureName("generarReporte")
                    .declareParameters(
                            new SqlParameter("sentencia", Types.VARCHAR),
                            new SqlOutParameter("segmentacion", Types.VARCHAR)
                    );

            // Mapear los parametros de entrada del SP
            MapSqlParameterSource paramMap = new MapSqlParameterSource()
                    .addValue("sentencia", stringReporte);
            
            Map<String, Object> resultMap = jdbcCall.execute(paramMap);
            String resultadoSegmentacion = (String) resultMap.get("segmentacion");
            String segmentacion = resultadoSegmentacion.substring(0, resultadoSegmentacion.length() - 1);
            return segmentacion;
        }
        catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
	    
	}
	
	/**
	 * El m�todo guardarReporte almacena el string generado con el m�todo anterior en el campo cuerpo_reporte de la tabla tco_reportes
	 */
	@Override
	public void guardarReporte(String cuerpoReporte, Integer idReporteOferta, Date fecha) {
		invocador.guardarReporte(cuerpoReporte, idReporteOferta, fecha);
	}
	
	/**
	 * El m�todo obtenerCandidatoPoriD se basa en la interfaz JpaRepository para consultar la informaci�n de un candidato por medio de 
	 * su Id, esto lo hace por medio del m�todo findTco_candidatosByIdCandidato de la interfaz com.tcontrato.repository/tco_candidatosRepo
	 *@param id El par�metro id representa el Id del candidato a consultar
	 */
	@Override
	public Tco_candidatos obtenerCandidatoPorId(Integer id) {		
		return tco_candidatosRepo.findTco_candidatosByIdCandidato(id);
	}
	
	/**
	 * El m�todo obtenerPonderaci�n se basa en el m�todo obtenerPonderacion de la interfaz com.tcontrato.repository/tco_ofertasRepo
	 * y es para hacer la consulta de la ponderaci�n obtenida por un candidato en una oferta determinada.
	 * @param id Representa el Id del candidato del que se quiere consultar la ponderaci�n
	 * @return Retorna el valor de la ponderaci�n obtenida por el candidato
	 */
	public Integer obtenerPonderacion(Integer id) {
		return tco_ofertasRepo.obtenerPonderacion(id);
	}
	
	/**
	 * El m�todo obtenerNivelesFormacionesCandidato consulta en la base de datos los niveles de formaci�n acad�mica registrada por
	 * un candidato, se basa en el m�todo obtenerNivelesFormacionesCandidato de la interfaz com.tcontrato.repository/tco_formacionesRepo
	 * @param id El Id del candidato del que se quiere consultar los niveles de formaci�n acad�mica
	 * @return Retorna el conjunto de las formaciones acad�micas registradas por el usuario
	 */
	public Set<?> obtenerNivelesFormacionesCandidato(Integer id) {
		return tco_formacionesRepo.obtenerNivelesFormacionesCandidato(id);
	}
	
	/**
	 * El m�todo obtenerNivelesFormacionesCandidato consulta en la base de datos los t�tulos de formaci�n acad�mica registrada por
	 * un candidato, se basa en el m�todo obtenerTitulosFormacionesCandidato de la interfaz com.tcontrato.repository/tco_formacionesRepo
	 * @param id El Id del candidato del que se quiere consultar los t�tulos de formaci�n acad�mica
	 * @return Retorna el conjunto de t�tulos acad�micpos registrados por el usuario
	 */
	public Set<?> obtenerTitulosFormacionesCandidato(Integer id) {
		return tco_formacionesRepo.obtenerTitulosFormacionesCandidato(id);
	}
	
	/**
	 * El m�todo generarReporte llama la informaci�n correspondiente a los datos b�sicos y de formaci�n acad�mica de cada uno de los
	 * candidatos para ser presentada en el reporte bajo la petici�n GET de la API
	 * @param id El Id de la oferta para la cu�l se quiere consultar el reporte
	 * @return Retorna el reporte generado, el retorno es de tipo JSONArray
	 */
	@Override
	public JSONArray generarReporte(Integer idOferta) {
		String stringCuerpoReporte = invocador.obtenerCuerpoReporte(idOferta);
		String[] listaCuerpoReporte = stringCuerpoReporte.split(",");
		JSONArray lista = new JSONArray();
		for(int i = 5; i < listaCuerpoReporte.length; i++) {
			Integer idCandidato = Integer.parseInt(listaCuerpoReporte[i]);
			JSONObject valoresCriteriosCandidato = new JSONObject();
			valoresCriteriosCandidato.put("id", obtenerCandidatoPorId(idCandidato).getIdCandidato());
			valoresCriteriosCandidato.put("ponderacion", obtenerPonderacion(idCandidato));
			valoresCriteriosCandidato.put("nombre", obtenerCandidatoPorId(idCandidato).getNombre_candidato());
			valoresCriteriosCandidato.put("apellido", obtenerCandidatoPorId(idCandidato).getApellido_candidato());
			valoresCriteriosCandidato.put("edad", obtenerCandidatoPorId(idCandidato).getEdad());
			valoresCriteriosCandidato.put("sexo", obtenerCandidatoPorId(idCandidato).getSexo());
			valoresCriteriosCandidato.put("ciudad_residencia", obtenerCandidatoPorId(idCandidato).getCiudad_residencia());
			valoresCriteriosCandidato.put("tiempo_experiencia", obtenerCandidatoPorId(idCandidato).getTiempo_experiencia());
			valoresCriteriosCandidato.put("nivel_formacion", obtenerNivelesFormacionesCandidato(idCandidato));
			valoresCriteriosCandidato.put("titulo_formacion", obtenerTitulosFormacionesCandidato(idCandidato));
			lista.put(valoresCriteriosCandidato);
		}
		return lista;
	}
	
	/*
	 * @Override
		public Set<Tco_candidatosDto> getAllCandidatos() {
		return tco_candidatosRepo.findAll()
				.stream()
                .map(candidato -> modelMapper.map(candidato, Tco_candidatosDto.class))
                .collect(Collectors.toCollection(TreeSet::new));
		}
	 */	

}