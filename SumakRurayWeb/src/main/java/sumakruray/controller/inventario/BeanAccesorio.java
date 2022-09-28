package sumakruray.controller.inventario;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import sumakruray.controller.JSFUtil;
import sumakruray.controller.seguridades.BeanSegDependencia;
import sumakruray.controller.seguridades.BeanSegLogin;
import sumakruray.model.core.entities.Accesorio;
import sumakruray.model.core.entities.AccesorioAtributo;
import sumakruray.model.core.entities.AccesorioMantenimiento;
import sumakruray.model.core.entities.Atributo;
import sumakruray.model.core.entities.BodegaAccesorio;
import sumakruray.model.core.entities.BodegaEquipo;
import sumakruray.model.core.entities.Equipo;
import sumakruray.model.core.entities.EquipoAccesorio;
import sumakruray.model.core.entities.ListaIp;
import sumakruray.model.core.entities.Marca;
import sumakruray.model.core.entities.Proveedor;
import sumakruray.model.core.entities.Responsable;
import sumakruray.model.core.entities.SegDependencia;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.core.entities.TipoAccesorio;
import sumakruray.model.inventario.managers.ManagerAccesorio;
import sumakruray.model.inventario.managers.ManagerBitacora;
import sumakruray.model.inventario.managers.ManagerBodega;
import sumakruray.model.inventario.managers.ManagerDependencia;
import sumakruray.model.inventario.managers.ManagerEquipo;
import sumakruray.model.inventario.managers.ManagerMantenimiento;
import sumakruray.model.inventario.managers.ManagerMarca;
import sumakruray.model.inventario.managers.ManagerProveedor;
import sumakruray.model.inventario.managers.ManagerResponsable;
import sumakruray.model.inventario.managers.ManagerTipoAE;
import sumakruray.model.seguridades.managers.ManagerSeguridades;

@Named
@SessionScoped
public class BeanAccesorio implements Serializable {

	private static final long serialVersionUID = 1L;
	@EJB
	private ManagerResponsable managerResponsable;
	@EJB
	private ManagerTipoAE managerTipoAE;
	@EJB
	private ManagerMarca managerMarca;
	@EJB
	private ManagerProveedor managerProveedor;
	@EJB
	private ManagerDependencia managerDependencia;
	@EJB
	private ManagerAccesorio managerAccesorio;
	@EJB
	private ManagerSeguridades managerSeguridades;
	@EJB
	private ManagerEquipo managerEquipo;
	@EJB
	private ManagerBodega managerBodega;
	@EJB
	private ManagerMantenimiento managerMantenimiento;

	@Inject
	private BeanSegLogin beanSegLogin;
	@Inject
	private BeanTipoAccesorio beanTipoAccesorio;
	@Inject
	private BeanEquipo beanEquipo;
	@Inject
	private BeanMantenimiento beanMantenimiento;
	@Inject
	private BeanBodega beanBodega;
	@Inject
	private BeanDependencia beanDependencia;
	@Inject
	private BeanProveedor beanProveedor;
	@Inject
	private BeanResponsable beanResponsable;
	@Inject
	private BeanMarca beanMarca;
	@Inject
	private BeanBitacora beanBitacora;
	@Inject
	private BeanAtributo beanAtributo;
	// Accesorios
	private List<Accesorio> listaAccesoriosActivos;
	private List<Accesorio> listaAccesoriosInactivo;
	private List<Accesorio> listaAccesoriosMantenimiento;
	private List<Accesorio> listaAccesoriosBaja;
	private List<Accesorio> listaAccesoriosInactivoEnEquipo;
	private Accesorio AccesorioCreado;
	private Accesorio nuevoAccesorio;
	private Accesorio respaldoAccesorio;
	private Accesorio edicionAccesorio;
	private Accesorio vistaAccesorio;
	private Accesorio accesorioBodega;
	private List<Accesorio> listaAccesoriosAll;
	private Equipo equipoDevuelto;

	// Cabecera
	private Accesorio cabecera;

	// Atributos de un Accesorio
	private List<AccesorioAtributo> listaAccesorioAtributo;

	// Lista de Equipo asignado a un accesorio(utilizado para el retorno de una
	// consulta)
	private List<EquipoAccesorio> equiposDevuelto;
	// Equipo asignado a un accesorio (asignado de una lista de Equipos)
	private EquipoAccesorio equipoAccesorioDevuelto;

	// Dependencias
	private int idSegDependenciaSeleccionado;
	// Responsable
	private int respIdSeleccionado;
	// Proveedor;
	private int proIdSeleccionado;
	// Atributos
	private int atriIdSeleccionado;
	// Marcas
	private int marIdSeleccionado;
	// Tipo Accesorio
	private int tipAccIdSeleccionado;
	// Bodega
	private BodegaAccesorio bodegaAccesorio;
	private List<BodegaAccesorio> bodegaAccesorioBuscado;

	// Valor o descripcion del atributo
	private String valorAtributo;

	// MantenimientoAccesorio
	private List<AccesorioMantenimiento> listaAcceMan;

	// --------Extras

	private boolean verificador_mantenimiento;
	private String vidaUtil;
	private String valorDepreciado;
	private boolean verificadorNuevoAccesorio;
	private String enlace;

	// Tiempo
	private Timestamp tiempo;

	//
	public BeanAccesorio() {
		// TODO Auto-generated constructor stub
	}

	// Inicializa las siguientes variables
	@PostConstruct
	public void inicializar() throws Exception {
		actionConsultarListaAccesoriosActivos();
		tiempo = new Timestamp(System.currentTimeMillis());

	}
	// **********************--___ACCESORIO__--******************************************

	// Inicializar Variables
	public void actionRecargarListaAccesoriosAll() throws Exception {
		listaAccesoriosAll = managerAccesorio.findAllAccesorios();
	}

	/**
	 * Consultas de Accesorios dependiendo su estado
	 * 
	 * @throws Exception
	 */
	public void actionConsultarListaAccesoriosActivos() throws Exception {
		listaAccesoriosActivos = managerAccesorio.findWhereByAcceEstado("Activo");
	}

	public void actionConsultarListaAccesoriosInactivos() throws Exception {
		listaAccesoriosInactivo = managerAccesorio.findWhereByAcceEstado("Inactivo");
	}

	public void actionConsultarListaAccesoriosMantenimientos() throws Exception {
		listaAccesoriosMantenimiento = managerAccesorio.findWhereByAcceEstado("Mantenimiento");
	}

	public void actionConsultarListaAccesoriosEnEquipos() throws Exception {
		listaAccesoriosInactivoEnEquipo = managerAccesorio.findWhereByAcceEstado("Inactivo_Equipo");
	}

	public void actionConsultarListaAccesoriosBajas() throws Exception {
		listaAccesoriosBaja = managerAccesorio.findWhereByAcceEstado("De Baja");
	}

	/**
	 * Inicializar Variables para un nuevo Accesorio
	 * 
	 * @throws Exception
	 */
	public void inicializarVaribalesAccesorio() throws Exception {
		nuevoAccesorio = new Accesorio();
		respaldoAccesorio = new Accesorio();
		respaldoAccesorio.setAcceEstado("Activo");
		nuevoAccesorio.setAcceEstado("Activo");
		respIdSeleccionado = 0;
		proIdSeleccionado = 0;
		marIdSeleccionado = 0;
		atriIdSeleccionado = 0;
		idSegDependenciaSeleccionado = 0;
		cabecera = null;
		tipAccIdSeleccionado = 0;
		beanAtributo.setNuevoAtributo(null);
		valorAtributo=null;
		actionConsultarListaAccesoriosActivos();// Accesorios Activos
	}

	/**
	 * Consultar las diferentes tablas para crear un nuevo Accesorio
	 * 
	 * @throws Exception
	 */
	public void cargarVaribalesAccesorio() throws Exception {
		beanDependencia.actionConsultarAllDependencias();
		beanResponsable.actionConsultarAllResponsable();
		beanProveedor.actionConsultarAllProveedor();
		beanMarca.actionConsultarAllMarca();
		beanAtributo.actionConsultarAllAtributo();
		beanTipoAccesorio.actionConsultarAllTipoAccesorio();
	}

	/**
	 * 
	 * @param tipAccIdSeleccionados tipo accesorio seleccionado
	 * @return la pagina de npara un nuevo aacesorio
	 * @throws Exception
	 */
	public String actionMenuAccesorios() throws Exception {
		cargarVaribalesAccesorio();
		inicializarVaribalesAccesorio();
		return "accesorios_nuevo";
	}

	/*
	 * METODOS PARA CRUD DE ACCESORIO
	 */
	/**
	 * Seleccionar un nuevo Accesorio
	 * 
	 * @throws Exception
	 */
	public void actionNuevoAccesorio() throws Exception {
		inicializarVaribalesAccesorio();
		// Condici�n para verificar si se esta creando un accesorio nuevo apartir de
		// un
		// equipo nuevo
		if (beanEquipo.getCabecera() != null) {
			if (beanEquipo.getCabecera().getResponsable() != null) {
				nuevoAccesorio.setResponsable(beanEquipo.getCabecera().getResponsable());
				respIdSeleccionado = beanEquipo.getCabecera().getResponsable().getRespId();
			}
			if (beanEquipo.getCabecera().getSegDependencia() != null) {
				nuevoAccesorio.setSegDependencia(beanEquipo.getCabecera().getSegDependencia());
				idSegDependenciaSeleccionado = beanEquipo.getCabecera().getSegDependencia().getIdSegDependencia();
			}
			nuevoAccesorio.setMarca(beanEquipo.getCabecera().getMarca());
			nuevoAccesorio.setProveedor(beanEquipo.getCabecera().getProveedor());
			marIdSeleccionado = beanEquipo.getCabecera().getMarca().getMarId();
			proIdSeleccionado = beanEquipo.getCabecera().getProveedor().getProId();
			beanEquipo.Mostrardialogos();
		}

	}

	/**
	 * Crear Accesorio(Maestro) y Atributo(detalle) para poder Insertar nuevo
	 * Accesorio
	 * 
	 * @throws Exception
	 */

	public void actionListenerAdicionarAtributo(String verificador) throws Exception {
		if (!valorAtributo.equals("")) {
			String add = "ADD";
			if (cabecera == null) {
				AsignarValoresNuevoAcesorios();
			} else if (cabecera != null && !verificador.equals("SinAtributo")) {

				for (int i = 0; i < cabecera.getAccesorioAtributos().size(); i++) {
					int id = cabecera.getAccesorioAtributos().get(i).getAtributo().getAtriId();
					if (id == atriIdSeleccionado) {
						JSFUtil.crearMensajeWARN("Caracteristica ya seleccionado");
						add = "no";
						break;
					}
				}

				if (add.equals("ADD")) {
					cabecera = managerAccesorio.adicionarAtributo(cabecera, nuevoAccesorio, atriIdSeleccionado,
							valorAtributo);
				}
				beanMantenimiento.setAccesorioDevuelto(cabecera);

			}
		} else {
			JSFUtil.crearMensajeWARN("Digite una Descripcion de la Caracteristica");
		}
	}

	/**
	 * Eliminar una Caracteristica de un Accesorio
	 * 
	 * @param acceId
	 * @param atriId
	 * @throws Exception
	 */
	public void actionListenerQuitarAtributo(int acceId, int atriId) throws Exception {

		if (cabecera == null) {
			JSFUtil.crearMensajeWARN("No hay Atributos que eliminar");
		} else if (cabecera != null) {
			for (int i = 0; i < cabecera.getAccesorioAtributos().size(); i++) {
				if ((int) cabecera.getAccesorioAtributos().get(i).getAtributo().getAtriId() == atriId) {
					List<AccesorioAtributo> accesorioAtributo = managerAccesorio.findWhereByAcceAtriId(acceId, atriId);
					if (accesorioAtributo.size() > 0) {
						managerAccesorio
								.eliminarAtributoAccesorio(cabecera.getAccesorioAtributos().get(i).getAcceAtriId());
					}
					cabecera = managerAccesorio.QuitarAtributo(cabecera, i);
					JSFUtil.crearMensajeWARN("Atributo quitado de la lista");
					break;
				}
			}
		}
	}

	/**
	 * Insertrar un nuevo Registro de Accesorio con o sin Atributos
	 */

	public void actionListenerInsertarNuevoAccesorio() {

		try {

			// Insertar directamente un nuevo Accesorio sin Atributos
			if (cabecera == null) {
				AsignarValoresNuevoAcesorios();
			}
			cabecera.setAcceNroSerie(nuevoAccesorio.getAcceNroSerie());
			cabecera.setAcceNombre(nuevoAccesorio.getAcceNombre());
			cabecera.setAcceCodBodega(nuevoAccesorio.getAcceCodBodega());
			if (idSegDependenciaSeleccionado != 0 && respIdSeleccionado != 0) {

				if (validarCreacionAccesorio(cabecera) == false) {
					cabecera.setSegDependencia(managerDependencia.findByIdSegDependencia(idSegDependenciaSeleccionado));
					cabecera.setResponsable(managerResponsable.findByIdResponsable(respIdSeleccionado));
					if (verificadorNuevoAccesorio && equipoDevuelto != null) {
						cabecera.setSegDependencia(
								managerDependencia.findByIdSegDependencia(idSegDependenciaSeleccionado));
						cabecera.setResponsable(managerResponsable.findByIdResponsable(respIdSeleccionado));
					}
					// Insertar Accesorio con o sin Atributos
					managerAccesorio.registrarAccesorio(beanSegLogin.getLoginDTO(), cabecera);
					// M�todo para identficar el nuevo accesorio creado desde un nuevo equipo
					if (verificadorNuevoAccesorio && equipoDevuelto != null) {
						beanEquipo.ActionAccesorioColocarAEquipo(cabecera);
					}
					// M�todo para verificar si se crea el accesorio desde un nuevo equipo
					if (beanEquipo.getCabecera() == null) {
						inicializarVaribalesAccesorio();
					} else {
						AccesorioCreado = managerAccesorio.findByNroSerieAccesorio(cabecera.getAcceNroSerie());
						nuevoAccesorio = new Accesorio();
						nuevoAccesorio.setResponsable(beanEquipo.getCabecera().getResponsable());
						nuevoAccesorio.setSegDependencia(beanEquipo.getCabecera().getSegDependencia());
						cabecera = null;
					}
					actionConsultarListaAccesoriosActivos();

					JSFUtil.crearMensajeINFO("Accesorio insertado Exitamente");
				} else {
					JSFUtil.crearMensajeWARN(enlace + "Ya Existen");
					enlace = "";
				}
			} else {
				JSFUtil.crearMensajeWARN("Ingrese un Responsable o Dependencia ");
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Asignar Valores a un Nuevo Accesorio
	 * 
	 * @throws Exception
	 */
	public void AsignarValoresNuevoAcesorios() throws Exception {
		nuevoAccesorio.setResponsable(managerResponsable.findByIdResponsable(respIdSeleccionado));
		nuevoAccesorio.setProveedor(managerProveedor.findByIdProveedor(proIdSeleccionado));
		nuevoAccesorio.setMarca(managerMarca.findByIdMarca(marIdSeleccionado));
		nuevoAccesorio.setTipoAccesorio(managerTipoAE.findByIdTipoAccesorio(tipAccIdSeleccionado));
		nuevoAccesorio.setSegDependencia(managerDependencia.findByIdSegDependencia(idSegDependenciaSeleccionado));
		nuevoAccesorio.setAcceFechaCreacion(tiempo);
		nuevoAccesorio.setAcceEstado("Activo");

		int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
		SegUsuario persona = managerSeguridades.findByIdSegUsuario(id_user);

		nuevoAccesorio.setAcceUsuarioCrea(persona.getNombres() + " " + persona.getApellidos());
		cabecera = managerAccesorio.adicionarAtributo(cabecera, nuevoAccesorio, atriIdSeleccionado, valorAtributo);
	}

	/**
	 * Buscar un Accesorio con sus atributos y al equipo que pertenece y
	 * redirecciona a la pagina de vista
	 */

	public String actionSeleccionarAccesorio(Accesorio accesorio) throws Exception {
		vistaAccesorio = ConsultarAccesorioAtributoEquipo(accesorio);
		ConsultarVidaUtilofAccessorio(accesorio);
		

		return "accesorios_vista";
	}

	/**
	 * Buscar un Accesorio con sus atributos y al equipo que pertenece y
	 * redirecciona a la pagina de vista
	 */

	public void actionSeleccionarAccesorioBodega(Accesorio accesorio) throws Exception {
		accesorioBodega = ConsultarAccesorioAtributoEquipo(accesorio);

	}

	/**
	 * Buscar un Accesorio con sus atributos y al equipo que pertenece
	 * 
	 * @param accesorio
	 * @throws Exception
	 */
	public Accesorio ConsultarAccesorioAtributoEquipo(Accesorio accesorio) throws Exception {

		Accesorio accesorioConsulta;
		accesorioConsulta = accesorio;
		listaAccesorioAtributo = managerAccesorio.findByAcceIdSeleccionadoAtributo(accesorio.getAcceId());
		accesorioConsulta.setAccesorioAtributos(listaAccesorioAtributo);
		equiposDevuelto = managerEquipo.findByAcceIdSeleccionadforEquipo(accesorio.getAcceId());
		if (equiposDevuelto.size() > 0) {
			equipoAccesorioDevuelto = equiposDevuelto.get(0);
		}

		return accesorioConsulta;

	}

	/**
	 * <<<<<<< HEAD Selecciona el Accesorio para la edici�n ======= Consultar la
	 * vida util y el precio depreciado del un accesorio
	 * 
	 * @param accesorio
	 * @throws Exception
	 */
	public void ConsultarVidaUtilofAccessorio(Accesorio accesorio) throws Exception {
		String[] a = new String[2];

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		String Date = format.format(accesorio.getAcceFechaCreacion());
		Date datos = format.parse(Date);
		Timestamp dato = new Timestamp(datos.getTime());

		a = beanBitacora.actionCargarFechaTranscurridos(dato, accesorio.getAccPrecio());
		vidaUtil = a[0];
		valorDepreciado = a[1];

	}

	/**
	 * Selecciona el Accesorio para la edici�n >>>>>>>
	 * 1205b7302cab9962d839b45f3d81a60ccc1fc863
	 * 
	 * @param accesorio
	 * @return
	 * @throws Exception
	 */
	public String actionSeleccionarEdicionAccesorio(Accesorio accesorio) throws Exception {
		edicionAccesorio = ConsultarAccesorioAtributoEquipo(accesorio);

		if (accesorio.getResponsable() != null) {
			respIdSeleccionado = accesorio.getResponsable().getRespId();
		}
		if (accesorio.getProveedor() != null) {
			proIdSeleccionado = accesorio.getProveedor().getProId();
		}
		if (accesorio.getMarca() != null) {
			marIdSeleccionado = accesorio.getMarca().getMarId();
		}
		if (accesorio.getSegDependencia() != null) {
			idSegDependenciaSeleccionado = accesorio.getSegDependencia().getIdSegDependencia();
		}
		cabecera = accesorio;
		cabecera.setAccesorioAtributos(listaAccesorioAtributo);
		cargarVaribalesAccesorio();
		return "accesorios_edicion";

	}

	/**
	 * Accion actualizar Accesorio
	 */
	public void actionListenerActualizarEdicionAccesorio() {
		try {
			edicionAccesorio = cabecera;
			edicionAccesorio.setResponsable(managerResponsable.findByIdResponsable(respIdSeleccionado));
			edicionAccesorio.setProveedor(managerProveedor.findByIdProveedor(proIdSeleccionado));
			edicionAccesorio.setMarca(managerMarca.findByIdMarca(marIdSeleccionado));
			edicionAccesorio.setTipoAccesorio(managerTipoAE.findByIdTipoAccesorio(tipAccIdSeleccionado));
			edicionAccesorio.setAcceFechaCreacion(tiempo);
			edicionAccesorio.setSegDependencia(managerDependencia.findByIdSegDependencia(idSegDependenciaSeleccionado));

			int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
			SegUsuario persona = managerSeguridades.findByIdSegUsuario(id_user);
			edicionAccesorio.setAcceFechaModificacion(tiempo);
			edicionAccesorio.setAcceUsuarioModifica(persona.getNombres() + " " + persona.getApellidos());
			managerAccesorio.actualizarAccesorio(beanSegLogin.getLoginDTO(), edicionAccesorio);
			inicializarVaribalesAccesorio();
			JSFUtil.crearMensajeINFO("Accesorio actualizado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Actualizar un registro de un Equipo
	 */
	public void actionListenerActualizarEdicionAccesoriofromMantenimiento(Accesorio accesorio) {
		try {

			int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
			SegUsuario persona = managerSeguridades.findByIdSegUsuario(id_user);

			accesorio.setAcceFechaModificacion(tiempo);
			accesorio.setAcceUsuarioModifica(persona.getNombres() + " " + persona.getApellidos());
			managerAccesorio.actualizarAccesoriofromMantenimiento(beanSegLogin.getLoginDTO(), accesorio);

			JSFUtil.crearMensajeINFO("Accesorio actualizado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

//------------------------------PENDIENTE--------------------
	/**
	 * Asignar un Accesorio a un equipo con su Dependencia y Responsable
	 * 
	 * @param equipo
	 * @param accesorio
	 * @throws Exception
	 */
	public void actionListenerActualizarAccesorioPorEquipo(Equipo equipo, Accesorio accesorio) throws Exception {
		edicionAccesorio = new Accesorio();
		edicionAccesorio = accesorio;
		edicionAccesorio.setAcceEstado("Activo");
		edicionAccesorio.setResponsable(equipo.getResponsable());
		edicionAccesorio.setSegDependencia(equipo.getSegDependencia());
		managerAccesorio.cambiarEstadoAccesorioDirecto(edicionAccesorio);
	}
	// ------------------------------PENDIENTE--------------------

	/**
	 * Insertar un Nuevo Registro de Accesorio a la bodega
	 * 
	 * @param accesorio
	 */
	public void actionListenerInsertarNuevoAccesorioABodega(Accesorio accesorio, Equipo equipo) {
		try {
			String enlace = "";

			if (accesorio == null) {
				JSFUtil.crearMensajeINFO("Guardar el Accesorio");
			} else if (accesorio != null) {
				// Cambio de estado de mantenimiento cuando se envie a bodega

				if (verificador_mantenimiento) {
					System.out.println("111");

					listaAcceMan = managerMantenimiento.findAccesorioMantenimientoByAcceId(accesorio.getAcceId());

					for (int i = 0; i < listaAcceMan.size(); i++) {
						System.out.println(listaAcceMan.get(i).getMantenimiento().getManEstado() + "22");
						if (listaAcceMan.get(i).getMantenimiento().getManEstado().equals("En_Mantenimiento")) {

							System.out.println(listaAcceMan.get(i).getMantenimiento().getManEstado() + "33");

							listaAcceMan.get(i).getMantenimiento().setManEstado("Finalizado");

							System.out.println(listaAcceMan.get(i).getMantenimiento().getManEstado() + "33");

							managerMantenimiento.cambiarEstadoAccesorioDirecto(listaAcceMan.get(i).getMantenimiento());

							enlace += "de Mantenimiento finalizado ";
						}
					}
					beanMantenimiento.actionRecargarListaAccesoriosMantenimiento("En_Mantenimiento");
				}

				if (beanBodega.getNuevoBodegaAccesorio() != null) {
					enlace += beanBodega.getNuevoBodegaAccesorio().getBodObservacion() + " del equipo :";
				} else if (!verificador_mantenimiento) {
					enlace += " <> Quitado de la lista del equipo : ";
				}

				if (equipo != null) {
					enlace += equipo.getEquiNombre();

				} else if (!verificador_mantenimiento) {
					enlace += " <> y No estaba en un equipo";
				}

				accesorio.setAcceEstado("Inactivo");

				managerAccesorio.actualizarEstadoAccesorio(beanSegLogin.getLoginDTO(), accesorio, "Inactivo", enlace);
				// Buscar y eliminar la relaci�n del accesorio en un equipo
				equiposDevuelto = managerEquipo.findEquiAcceByOneAcce(accesorio.getAcceId());

				if (equiposDevuelto.size() > 0) {
					managerEquipo.deleteEquiAccByAccId(equiposDevuelto.get(0));
				}
				// Buscar si existe un resgitro del accesorio en bodega
				// si hay solo reemplaza el estado del accesorio
				// no hay crea un nuevo registro del Accesorio
				bodegaAccesorioBuscado = managerBodega.findWhereByAcceIdBodegaOne(accesorio.getAcceId());
				if (bodegaAccesorioBuscado.size() == 0) {
					beanBodega.actionListenerInsertarNuevoAccesorioABodega(accesorio);
				}

				JSFUtil.crearMensajeINFO("Accesorio enviado a bodega.");
				actionConsultarListaAccesoriosActivos();

			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para abrir una ventana emergente para asignar un Accesorio a un equipo
	 */
	public void Mostrardialogos() {
		PrimeFaces current = PrimeFaces.current();
		if (equiposDevuelto.size() == 0) {
			current.executeScript("PF('dialogoActivarAccesorio').show()");

		} else {
			JSFUtil.crearMensajeINFO("El Accesorio ya se encuentra en un Equipo");
			equiposDevuelto.equals(null);
			vistaAccesorio = new Accesorio();
		}
	}

	/*
	 *
	 * 
	 * ----------CODIGO PARA MANTENIMIENTO--------
	 */

	/**
	 * Verificar si el Accesorio va en Mnatenimiento
	 * 
	 * @throws Exception
	 */
	public void ValidarVerificadorMantenimiento() {
		verificador_mantenimiento = true;
	}

	// Metodo de Bodega
	/**
	 * Vista Accesorios para colocar en un Equipo -->
	 * 
	 * @param accesorio
	 * @throws Exception
	 */
	public void actionVistaSeleccionarAccesorio(Accesorio accesorio) throws Exception {
		equipoAccesorioDevuelto=null;
		vistaAccesorio = ConsultarAccesorioAtributoEquipo(accesorio);
		idSegDependenciaSeleccionado = vistaAccesorio.getSegDependencia().getIdSegDependencia();
		respIdSeleccionado = vistaAccesorio.getResponsable().getRespId();
		beanDependencia.actionConsultarAllDependencias();
		beanResponsable.actionConsultarAllResponsable();

	}

	// Seleccionar un nuevo Ip a un Equipo
	public void actionAsignarAEquipo() throws Exception {
		PrimeFaces current = PrimeFaces.current();

		if (cabecera != null) {
			current.executeScript("PF('dialogoAsignarAEquipo').show()");
			verificadorNuevoAccesorio = true;
		}

	}

	/**
	 * Accesorio Retorna a la funcionalidad
	 * 
	 * @param accesorio
	 * @throws Exception
	 */

	public void actionActivarAccesorio(Accesorio accesorio) throws Exception {
		String enlace = "";
		accesorio = ConsultarAccesorioAtributoEquipo(accesorio);
		accesorio.setAcceEstado("Activo");
		managerAccesorio.actualizarEstadoAccesorio(beanSegLogin.getLoginDTO(), accesorio, "Activo", enlace);
		beanBodega.actionSelectionEquiposInactivos();
		beanBodega.actionSelectionAccesoriosInactivos();
	

		// beanBodega.actionSelectionAccesoriosInactivosCE();
		// beanAccesorio.actionConsultarListaAccesoriosActivos();
		// actionRecargarListaEquiposActivos();
		JSFUtil.crearMensajeWARN("Equipo y Accesorios Activados");
	}

	/**
	 * Busca si existe un Accesorio a crear
	 * 
	 * @param nroSerie campo a verificar
	 * @return
	 * @throws Exception
	 */

	public boolean validarCreacionAccesorio(Accesorio accesorio) throws Exception {
		enlace = "";
		boolean nro = false;
		boolean nom = false;
		boolean cod = false;
		actionRecargarListaAccesoriosAll();
		List<Accesorio> listaAllAccesorio = listaAccesoriosAll;
		for (int i = 0; i < listaAllAccesorio.size(); i++) {
			if (listaAllAccesorio.get(i).getAcceNroSerie().equals(accesorio.getAcceNroSerie()) && nro == false) {
				enlace += "Nro de Serie , ";
				nro = true;
			}
			if (listaAllAccesorio.get(i).getAcceNombre().equals(accesorio.getAcceNombre()) && nom == false) {
				enlace += " Nombre , ";
				nom = true;
			}
			if (listaAllAccesorio.get(i).getAcceCodBodega().equals(accesorio.getAcceCodBodega()) && cod == false) {
				enlace += " Codigo de Bodega , ";
				cod = true;
			}

		}

		if (!enlace.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * Botones de regresar y menu
	 * 
	 * @param pagina
	 * @return
	 * @throws Exception
	 */
	public String actionRegresar(String pagina) throws Exception {
		nuevoAccesorio = null;
		cabecera = null;
		idSegDependenciaSeleccionado = 0;
		respaldoAccesorio = null;
		respIdSeleccionado = 0;
		proIdSeleccionado = 0;
		marIdSeleccionado = 0;
		idSegDependenciaSeleccionado = 0;
		tipAccIdSeleccionado = 0;
		beanDependencia.setNuevoSegDependencia(null);
		beanAtributo.setNuevoAtributo(null);
		beanMarca.setNuevoMarca(null);
		beanProveedor.setNuevoProveedor(null);
		beanResponsable.setNuevoResponsable(null);
		beanTipoAccesorio.setNuevoTipoAccesorio(null);
		enlace = null;
		equipoAccesorioDevuelto = null;
		beanEquipo.setEquipoDevuelto(null);
		beanEquipo.setListaAccesorioofEquipoTemporal(null);
		vistaAccesorio=null;
		edicionAccesorio=null;
		valorAtributo=null;

		return pagina;
	}

	public List<Accesorio> getListaAccesoriosActivos() {
		return listaAccesoriosActivos;
	}

	public void setListaAccesoriosActivos(List<Accesorio> listaAccesoriosActivos) {
		this.listaAccesoriosActivos = listaAccesoriosActivos;
	}

	public Accesorio getNuevoAccesorio() {
		return nuevoAccesorio;
	}

	public void setNuevoAccesorio(Accesorio nuevoAccesorio) {
		this.nuevoAccesorio = nuevoAccesorio;
	}

	public Accesorio getEdicionAccesorio() {
		return edicionAccesorio;
	}

	public void setEdicionAccesorio(Accesorio edicionAccesorio) {
		this.edicionAccesorio = edicionAccesorio;
	}

	public int getRespIdSeleccionado() {
		return respIdSeleccionado;
	}

	public void setRespIdSeleccionado(int respIdSeleccionado) {
		this.respIdSeleccionado = respIdSeleccionado;
	}

	public int getProIdSeleccionado() {
		return proIdSeleccionado;
	}

	public void setProIdSeleccionado(int proIdSeleccionado) {
		this.proIdSeleccionado = proIdSeleccionado;
	}

	public int getIdSegDependenciaSeleccionado() {
		return idSegDependenciaSeleccionado;
	}

	public void setIdSegDependenciaSeleccionado(int idSegDependenciaSeleccionado) {
		this.idSegDependenciaSeleccionado = idSegDependenciaSeleccionado;
	}

	public int getAtriIdSeleccionado() {
		return atriIdSeleccionado;
	}

	public void setAtriIdSeleccionado(int atriIdSeleccionado) {
		this.atriIdSeleccionado = atriIdSeleccionado;
	}

	public int getMarIdSeleccionado() {
		return marIdSeleccionado;
	}

	public void setMarIdSeleccionado(int marIdSeleccionado) {
		this.marIdSeleccionado = marIdSeleccionado;
	}

	public int getTipAccIdSeleccionado() {
		return tipAccIdSeleccionado;
	}

	public void setTipAccIdSeleccionado(int tipAccIdSeleccionado) {
		this.tipAccIdSeleccionado = tipAccIdSeleccionado;
	}

	public String getValorAtributo() {
		return valorAtributo;
	}

	public void setValorAtributo(String valorAtributo) {
		this.valorAtributo = valorAtributo;
	}

	public Accesorio getCabecera() {
		return cabecera;
	}

	public void setCabecera(Accesorio cabecera) {
		this.cabecera = cabecera;
	}

	public Accesorio getVistaAccesorio() {
		return vistaAccesorio;
	}

	public void setVistaAccesorio(Accesorio vistaAccesorio) {
		this.vistaAccesorio = vistaAccesorio;
	}

	public List<AccesorioAtributo> getListaAccesorioAtributo() {
		return listaAccesorioAtributo;
	}

	public void setListaAccesorioAtributo(List<AccesorioAtributo> listaAccesorioAtributo) {
		this.listaAccesorioAtributo = listaAccesorioAtributo;
	}

	public EquipoAccesorio getEquipoAccesorioDevuelto() {
		return equipoAccesorioDevuelto;
	}

	public void setEquipoAccesorioDevuelto(EquipoAccesorio equipoAccesorioDevuelto) {
		this.equipoAccesorioDevuelto = equipoAccesorioDevuelto;
	}

	public Accesorio getAccesorioCreado() {
		return AccesorioCreado;
	}

	public void setAccesorioCreado(Accesorio accesorioCreado) {
		AccesorioCreado = accesorioCreado;
	}

	public Accesorio getAccesorioBodega() {
		return accesorioBodega;
	}

	public void setAccesorioBodega(Accesorio accesorioBodega) {
		this.accesorioBodega = accesorioBodega;
	}

	public List<Accesorio> getListaAccesoriosAll() {
		return listaAccesoriosAll;
	}

	public void setListaAccesoriosAll(List<Accesorio> listaAccesoriosAll) {
		this.listaAccesoriosAll = listaAccesoriosAll;
	}

	public String getVidaUtil() {
		return vidaUtil;
	}

	public void setVidaUtil(String vidaUtil) {
		this.vidaUtil = vidaUtil;
	}

	public String getValorDepreciado() {
		return valorDepreciado;
	}

	public void setValorDepreciado(String valorDepreciado) {
		this.valorDepreciado = valorDepreciado;
	}

	public boolean getVerificadorNuevoAccesorio() {
		return verificadorNuevoAccesorio;
	}

	public void setVerificadorNuevoAccesorio(boolean verificadorNuevoAccesorio) {
		this.verificadorNuevoAccesorio = verificadorNuevoAccesorio;
	}

	public Equipo getEquipoDevuelto() {
		return equipoDevuelto;
	}

	public void setEquipoDevuelto(Equipo equipoDevuelto) {
		this.equipoDevuelto = equipoDevuelto;
	}

}
