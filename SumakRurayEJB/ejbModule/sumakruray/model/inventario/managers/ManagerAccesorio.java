package sumakruray.model.inventario.managers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;


import sumakruray.model.core.entities.Accesorio;
import sumakruray.model.core.entities.AccesorioAtributo;
import sumakruray.model.core.entities.Atributo;
import sumakruray.model.core.entities.Equipo;
import sumakruray.model.core.entities.ListaIp;
import sumakruray.model.core.entities.TipoAccesorio;
import sumakruray.model.core.managers.ManagerDAO;
import sumakruray.model.seguridades.dtos.LoginDTO;

/**
 * Session Bean implementation class ManagerAccesorio
 */
@Stateless
@LocalBean
public class ManagerAccesorio {
	// mDAO cotiene Sentencias SQL generales para cualquier tabla
	@EJB
	private ManagerDAO mDAO;
	@EJB
	private ManagerBitacora managerBitacora;
	// Lista de Accesorio
	List<Accesorio> listaAccesorio;
	Accesorio accesorio;

	/**
	 * Default constructor.
	 */
	public ManagerAccesorio() {
		// TODO Auto-generated constructor stub
	}

	// *********************************ACCESORIOS******************************************************************************************

	/**
	 * Consulta de todos los registros de la tabla Accesorio
	 */
	public List<Accesorio> findAllAccesorios() {
		return mDAO.findAll(Accesorio.class, null);
	}

	/**
	 * Consultas de registros con estado "Activo e Nuevo_Activo" de la tabla
	 * Accesorios
	 */
	public List<Accesorio> findWhereByAcceEstados(String Activo, String Nuevo_Activo) throws Exception {
		return mDAO.findWhere(Accesorio.class, "acce_estado='" + Activo + "' or acce_estado= '" + Nuevo_Activo + "'",
				null);
	}

	/**
	 * Consultas de registros de Accesorios por un estado
	 */
	public List<Accesorio> findWhereByAcceEstado(String estado) throws Exception {
		return mDAO.findWhere(Accesorio.class, "acce_estado='" + estado + "'", null);
	}

	/**
	 * Insertar un Nuevo Accesorio
	 */
	public void insertarAccesorio(Accesorio nuevoAccesorio) throws Exception {
		mDAO.insertar(nuevoAccesorio);
		// Auditoria del los eventos

	}

	/**
	 * Consultar de registro de un Accesorio por su acceId(primary key)
	 */
	public Accesorio findByIdAccesorio(int acceId) throws Exception {
		return (Accesorio) mDAO.findById(Accesorio.class, acceId);
	}

	/**
	 * Consultar de registro de un Accesorio por su Nro Serie
	 */
	public Accesorio findByNroSerieAccesorio(String NroSerie) throws Exception {

		listaAccesorio = mDAO.findWhere(Accesorio.class, "acce_nro_serie='" + NroSerie + "'", null);
		if (listaAccesorio != null) {
			accesorio = (Accesorio) listaAccesorio.get(0);
		}
		return accesorio;
	}

	/**
	 * Eliminado F�sico de un registro de la tabla Accesorio(No utilizado)
	 */
	public void eliminarAtributoAccesorio(int atriId) throws Exception {
		mDAO.eliminar(AccesorioAtributo.class, atriId);
	}

	/**
	 * Quitar un Atributo de lista temporal de Atributos de un Accesorio
	 */
	public Accesorio QuitarAtributo(Accesorio cabecera, int index) throws Exception {

		cabecera.getAccesorioAtributos().remove(index);
		return cabecera;
	}

	/**
	 * Cambiar el Estado de un Registro de Accesorio
	 */
	public void cambiarEstadoAccesorioEnEquipo(LoginDTO loginDTO, Accesorio edicionAccesorio, Equipo equipo,
			String estado) throws Exception {
		Accesorio accesorio = (Accesorio) mDAO.findById(Accesorio.class, edicionAccesorio.getAcceId());
		String enlace = "";

		if (estado.equals("Inactivo_Equipo")) {
			accesorio.setAcceEstado(edicionAccesorio.getAcceEstado());
			accesorio.setSegDependencia(edicionAccesorio.getSegDependencia());
			accesorio.setResponsable(edicionAccesorio.getResponsable());
			mDAO.actualizar(accesorio);
			// Auditoria del los eventos
			managerBitacora.mostrarLogAccesorio(loginDTO, edicionAccesorio, "A�adirAccesorioAEquipo",
					"Accesorio a�adido al Equipo " + equipo.getEquiNombre() + " de Bodega ");
		} else if (estado.equals("Activo")) {

			enlace += " <>  Asignado Responsable a : " + edicionAccesorio.getResponsable().getRespApellidos();
			enlace += " <>  Asignado Dependencia a : " + edicionAccesorio.getSegDependencia().getDepDescripcion();
			accesorio.setAcceEstado(edicionAccesorio.getAcceEstado());
			accesorio.setSegDependencia(edicionAccesorio.getSegDependencia());
			accesorio.setResponsable(edicionAccesorio.getResponsable());
			mDAO.actualizar(accesorio);
			// Auditoria del los eventos
			managerBitacora.mostrarLogAccesorio(loginDTO, edicionAccesorio, "AccesorioBodega",
					"Accesorio Activado y " + enlace + " del equipo " + equipo.getEquiNombre() + " en funcionamiento");
		}

	}

	/**
	 * Cambiar el Estado de un Registro de Accesorio cuando se envia a bodega con
	 * todo su equipo
	 */
	public void cambiarEstadoAccesorioEnEquipoParaBodega(LoginDTO loginDTO, Accesorio edicionAccesorio, Equipo equipo,
			String estado) throws Exception {

		if (estado.equals("Inactivo_Equipo")) {
			mDAO.actualizar(edicionAccesorio);
			// Auditoria del los eventos
			managerBitacora.mostrarLogAccesorio(loginDTO, edicionAccesorio, "EnviarBodega",
					"Accesorio enviado con el equipo : " + equipo.getEquiNombre() + " a Bodega ");
		}
	}

	/**
	 * Cambiar el Estado de un Registro de Accesorio directamente
	 */
	public void cambiarEstadoAccesorioDirecto(Accesorio accesorio) throws Exception {
		mDAO.actualizar(accesorio);
	}

	/**
	 * Adicionar un Atributo a un lista temporal de Atributos de un Accesorio
	 */
	public Accesorio adicionarAtributo(Accesorio cabecera, Accesorio nuevoAccesorio, int atriIdSeleccionado,
			String valorAtributo)

			throws Exception {
		// Verificaci�n de la creaci�n del Mestro Accesorio
		if (cabecera == null) {

			cabecera = new Accesorio();
			cabecera.setAccesorioAtributos(new ArrayList<AccesorioAtributo>());
			cabecera.setProveedor(nuevoAccesorio.getProveedor());
			cabecera.setTipoAccesorio(nuevoAccesorio.getTipoAccesorio());
			cabecera.setResponsable(nuevoAccesorio.getResponsable());
			cabecera.setMarca(nuevoAccesorio.getMarca());
			cabecera.setAcceNroSerie(nuevoAccesorio.getAcceNroSerie());
			cabecera.setAcceNombre(nuevoAccesorio.getAcceNombre());
			cabecera.setAccPrecio(nuevoAccesorio.getAccPrecio());
			cabecera.setAcceCodBodega(nuevoAccesorio.getAcceCodBodega());
			cabecera.setAcceFechaCreacion(nuevoAccesorio.getAcceFechaCreacion());
			cabecera.setAcceGarantia(nuevoAccesorio.getAcceGarantia());
			cabecera.setAcceAnioVidaUtil(nuevoAccesorio.getAcceAnioVidaUtil());
			cabecera.setAcceUsuarioCrea(nuevoAccesorio.getAcceUsuarioCrea());
			cabecera.setAcceEstado(nuevoAccesorio.getAcceEstado());
			cabecera.setSegDependencia(nuevoAccesorio.getSegDependencia());

		}
		// Cosulta del resgitro Atributo que se desea adicionar al Accesorio
		if (atriIdSeleccionado != 0) {
			Atributo atributo = (Atributo) mDAO.findById(Atributo.class, atriIdSeleccionado);

			AccesorioAtributo detalle = new AccesorioAtributo();

			detalle.setAtributo(atributo);
			detalle.setAccesorio(cabecera);
			detalle.setAtriDescripcion(valorAtributo);

			cabecera.getAccesorioAtributos().add(detalle);
		}
		return cabecera;
	}

	/**
	 * Insertar un nuevo Accesorio
	 */
	public void registrarAccesorio(LoginDTO loginDTO, Accesorio cabecera) throws Exception {
		// verificaci�n del Maestro Accesorio
		if (cabecera == null)
			throw new Exception("Debe Ingresar los datos Correspondientes");
		// Suma del tipo de accesorio que se este ingresando
		TipoAccesorio tipoAccesorio = new TipoAccesorio();
		tipoAccesorio = (TipoAccesorio) mDAO.findById(TipoAccesorio.class, cabecera.getTipoAccesorio().getTipAccId());
		tipoAccesorio.setTipAccCantidad(tipoAccesorio.getTipAccCantidad() + 1);
		// Actualizar el tipo Accesorio
		mDAO.actualizar(tipoAccesorio);
		// Insertar un nuevo Accesorio
		mDAO.insertar(cabecera);

		managerBitacora.mostrarLogAccesorio(loginDTO, cabecera, "insertarAccesorio", " Creaci�n del Accesorio");
	}

	/**
	 * Actualizar un registro de Accesorio
	 */

	public void actualizarAccesorio(LoginDTO loginDTO, Accesorio edicionAccesorio) throws Exception {
		String enlace = "";
		Accesorio accesorio = (Accesorio) mDAO.findById(Accesorio.class, edicionAccesorio.getAcceId());
		
		accesorio.setAccesorioAtributos(edicionAccesorio.getAccesorioAtributos());
		if (!accesorio.getResponsable().getRespApellidos()
				.equals(edicionAccesorio.getResponsable().getRespApellidos())) {
			enlace += "Modificado Responsable a : " + edicionAccesorio.getResponsable().getRespApellidos();
		}
		accesorio.setResponsable(edicionAccesorio.getResponsable());
		accesorio.setAcceNombre(edicionAccesorio.getAcceNombre());
		accesorio.setAcceNroSerie(edicionAccesorio.getAcceNroSerie());
		accesorio.setAccPrecio(edicionAccesorio.getAccPrecio());
		accesorio.setAcceFechaModificacion(edicionAccesorio.getAcceFechaModificacion());
		accesorio.setAcceUsuarioModifica(edicionAccesorio.getAcceUsuarioModifica());
		accesorio.setAcceEstado(edicionAccesorio.getAcceEstado());
		if (!accesorio.getSegDependencia().getDepDescripcion()
				.equals(edicionAccesorio.getSegDependencia().getDepDescripcion())) {
			enlace += " <>  Modificado Dependencia a : " + edicionAccesorio.getSegDependencia().getDepDescripcion();
		}
		accesorio.setSegDependencia(edicionAccesorio.getSegDependencia());
		for (int i = 0; i < accesorio.getAccesorioAtributos().size(); i++) {
			AccesorioAtributo accesorioAtributo = accesorio.getAccesorioAtributos().get(i);
			AccesorioAtributo accesorioAtributo2 = edicionAccesorio.getAccesorioAtributos().get(i);
			if (!accesorioAtributo.getAtriDescripcion().equals(accesorioAtributo2.getAtriDescripcion())) {
				enlace += " <>   Modificado " + accesorioAtributo.getAtributo().getAtriNombre() + " a : "
						+ accesorioAtributo2.getAtriDescripcion();
			}
		}
		accesorio.setAccesorioAtributos(edicionAccesorio.getAccesorioAtributos());
		if (enlace.equals("")) {
			enlace = "Ninguna";
		}
		mDAO.actualizar(accesorio);
		// Auditoria del los eventos
		managerBitacora.mostrarLogAccesorio(loginDTO, accesorio, "ActualizarAccesorio", " Cambios : " + enlace);
	}

	/**
	 * Actualizar un registro de Accesorio desde Mantenimiento
	 */

	public void actualizarAccesoriofromMantenimiento(LoginDTO loginDTO, Accesorio edicionAccesorio) throws Exception {
		String enlace = "";
		Accesorio accesorio = (Accesorio) mDAO.findById(Accesorio.class, edicionAccesorio.getAcceId());

		enlace += "Caravteristicas Anteriores: ";
		for (int i = 0; i < accesorio.getAccesorioAtributos().size(); i++) {
			AccesorioAtributo accesorioAtributo = accesorio.getAccesorioAtributos().get(i);
			enlace += "  <> " + accesorioAtributo.getAtributo().getAtriNombre() + " = "
					+ accesorioAtributo.getAtriDescripcion();
		}

		enlace += "Caravteristicas Atuales: ";
		for (int i = 0; i < edicionAccesorio.getAccesorioAtributos().size(); i++) {
			AccesorioAtributo edicionAccesorioAtributo = edicionAccesorio.getAccesorioAtributos().get(i);
			enlace += "  <> " + edicionAccesorioAtributo.getAtributo().getAtriNombre() + " = "
					+ edicionAccesorioAtributo.getAtriDescripcion();
		}

		accesorio.setAccesorioAtributos(edicionAccesorio.getAccesorioAtributos());

		mDAO.actualizar(accesorio);
		// Auditoria del los eventos
		managerBitacora.mostrarLogAccesorio(loginDTO, accesorio, "ActualizarAccesorio",
				" Cambios : " + enlace + " en Mantenimiento");
	}

	/**
	 * Actualizar un registro de Accesorio para Bodega o Mantenimiento
	 */

	public void actualizarEstadoAccesorio(LoginDTO loginDTO, Accesorio edicionAccesorio, String estado,
			String Observacion) throws Exception {
		if (estado.equals("Inactivo")) {
			mDAO.actualizar(edicionAccesorio);
			// Auditoria del los eventos
			managerBitacora.mostrarLogAccesorio(loginDTO, edicionAccesorio, "AccesorioBodega",
					"Accesorio Enviado a Bodega, Observacion : " + Observacion);
		} else if (estado.equals("Activo")) {
			mDAO.actualizar(edicionAccesorio);
			// Auditoria del los eventos
			managerBitacora.mostrarLogAccesorio(loginDTO, edicionAccesorio, "ActualizarAccesorio",
					" Accesorio Retorna a la Funcionalidad");

		} else if (estado.equals("Mantenimiento")) {
			mDAO.actualizar(edicionAccesorio);
			// Auditoria del los eventos
			managerBitacora.mostrarLogAccesorio(loginDTO, edicionAccesorio, "MantenimientoAccesorio",
					" Accesorio en Mantenimiento por: " + Observacion);

		}

	}

	/**
	 * Consultarel ultimo registro de un accesorio
	 */
	public int ConsultarUltimoAcceidofAccesorio() throws Exception {
		return (int) mDAO.obtenerValorMax(Accesorio.class, "acceId");

	}

	// *********************************ACCESORIOS-ATRIBUTOS********************************************************************************************
	/**
	 * Consultar el registro de un Accesorio y sus Atributos por su acceId
	 * 
	 */
	public List<AccesorioAtributo> findByAcceIdSeleccionadoAtributo(int AcceIdSeleccionado) throws Exception {
		return mDAO.findWhere(AccesorioAtributo.class, "acce_id=" + AcceIdSeleccionado + "", null);
	}

	/**
	 * Consulta de registros de un Accesorio y sus Atributos por su acceid y atriId
	 */
	public List<AccesorioAtributo> findWhereByAcceAtriId(int acceId, int atriId) throws Exception {
		return mDAO.findWhere(AccesorioAtributo.class, "acce_id=" + acceId + " and atri_id=" + atriId, null);
	}

	// CAMBIOS REALIZADOS POR HACHE
	// --------------------------------------------------------

	/**
	 * Consulta de registros de un Accesorio y sus Atributos por su atributo y valor
	 */
	public List<AccesorioAtributo> findAccesorioByAtriIdandValor(int atriId, String valor) throws Exception {
		return mDAO.findWhere(AccesorioAtributo.class, "atri_id=" + atriId + " and atri_descripcion='" + valor + "'",
				null);
	}

	public List<AccesorioAtributo> findAtributosValorAccesorio(String atriNombre) {

		String consulta1 = "select aa from AccesorioAtributo aa where aa.acceAtriId = (select max(aaa.acceAtriId) from AccesorioAtributo aaa "
				+ "inner join Atributo a on aaa.atributo.atriId = a.atriId where a.atriNombre = '" + atriNombre + "')";

		Query q = mDAO.getEntityManager().createQuery(consulta1, AccesorioAtributo.class);
		return (List<AccesorioAtributo>) q.getResultList();

	}

	// ------------------------------------------------------ HACHE
	// -----------------------------------

}
