package sumakruray.controller.actividades;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.shaded.commons.io.FilenameUtils;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import sumakruray.controller.JSFUtil;
import sumakruray.controller.seguridades.BeanSegLogin;
import sumakruray.model.actividades.managers.ManagerActividades;
import sumakruray.model.actividades.managers.ManagerDetalleSolicitudes;
import sumakruray.model.actividades.managers.ManagerInsumos;
import sumakruray.model.actividades.managers.ManagerSolicitudes;
import sumakruray.model.core.entities.Detalle;
import sumakruray.model.core.entities.RegActividad;
import sumakruray.model.core.entities.RegInsAct;
import sumakruray.model.core.entities.RegInsumo;
import sumakruray.model.core.entities.RegSolicitud;
import sumakruray.model.core.entities.RegTipo;
import sumakruray.model.core.entities.SegDependencia;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.core.utils.ModelUtil;
import sumakruray.model.seguridades.managers.ManagerSeguridades;

@Named
@SessionScoped
public class BeanDetalleSolicitud implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private ManagerSolicitudes managerSolicitudes;

	@EJB
	private ManagerDetalleSolicitudes managerDetalleSolicitudes;

	@EJB
	private ManagerInsumos managerInsumos;

	@EJB
	private ManagerActividades managerActividades;

	@EJB
	private ManagerSeguridades managerSeguridades;

	private List<Detalle> listaDetalles;
	private List<RegSolicitud> listaSolicitudesTecnico;
	private List<Detalle> listaDetallesSolicitud;
	private List<SegUsuario> listaTecnicos;
	private int idTecnicoSeleccionado;
	private String moticoTraspaso;
	private Detalle nuevoDetalle;
	private List<RegTipo> listaTipo;
	private Detalle edicionDetalle;
	private RegSolicitud solicitudSeleccionada;

	private int tipoSelected;
	private int tipoSelectednuevo;
	private int actividadSeleccionada;
	private int actividadSeleccionadanueva;
	private int idInsumoSeleccionado;
	private int idInsumoSeleccionadonuevo;
	private int idDepandenciaSeleccionada;
	private int idDepandenciaSeleccionadanuevo;
	private List<RegInsAct> listaActividadesByInsumo;
	private List<RegActividad> listaActividades;
	private List<RegInsumo> listaInsumos;
	private List<SegDependencia> listaDependencias;

	private Timestamp tiempoActual;
	private Date fechaIni;
	private Date fechaFin;
	private Date fechaIninuevo;
	private Date fechaFinnuevo;
	
	//Reporte: usuario(Técnico Reporte), fecha inicio y fecha fin
	private int idTecnicoReporte;
	private Date inicio;
	private Date fin;

	@Inject
	private BeanSegLogin beanSegLogin;

	public BeanDetalleSolicitud() {

	}
	
	/*
	 * Detalle son a las actividades realizadas, pueden estar ligadas a una solicitud o no.
	 * Detalle tiene Actividad, Observacion, Insumo, Dependencia, Usuario
	 * MotivoTraspaso se llena cuando es una actividad de una solicitud que nos pasaron por algún motivo
	 */

	@PostConstruct
	public void inicializar() {
		nuevoDetalle = new Detalle();
		edicionDetalle = new Detalle();
		inicio = ModelUtil.addDays(new Date(), -1);
		fin = new Date();
		listaActividades = managerActividades.findAllActividades();
		listaTecnicos = managerSolicitudes.findUsuariosTecnicos();
		listaTipo = managerDetalleSolicitudes.findAllTipos();
		listaDetalles = managerDetalleSolicitudes.findDetallesByTecnico(beanSegLogin.getLoginDTO().getIdSegUsuario());
		listaSolicitudesTecnico = managerDetalleSolicitudes
				.findSolicitudesByTecnico(beanSegLogin.getLoginDTO().getIdSegUsuario());
	}

	public String actionMenuSolicitudesRecibidas() {
		listaSolicitudesTecnico = managerDetalleSolicitudes
				.findSolicitudesByTecnico(beanSegLogin.getLoginDTO().getIdSegUsuario());
		return "solicitudesrecibidas";
	}

	public String actionMenuActividadesTecnico() {
		listaTecnicos = managerSolicitudes.findUsuariosTecnicos();
		idTecnicoReporte = beanSegLogin.getLoginDTO().getIdSegUsuario();
		listaDetalles = managerDetalleSolicitudes.findDetallesByTecnico(beanSegLogin.getLoginDTO().getIdSegUsuario());
		listaDependencias = managerSeguridades.findAllDependencias();
		listaInsumos = managerInsumos.findAllInsumos();
		return "actividadestecnico";
	}

	public String actionCargarSolicitud(RegSolicitud solicitud) {
		listaDetallesSolicitud = managerDetalleSolicitudes.findDetallesBySolicitud(solicitud.getRegId());
		solicitudSeleccionada = solicitud;
		listaActividadesByInsumo = managerInsumos
				.findActividadesByInsumo(solicitudSeleccionada.getRegInsumo().getInsId());
		return "detallesolicitud";
	}
	
	public boolean verificarTraspasar() {
		if(listaDetallesSolicitud.size() < 1) {
			return false;
		}
		if(listaDetallesSolicitud.get(listaDetallesSolicitud.size()-1).getDetFechaFin() == null 
			|| solicitudSeleccionada.getRegTipo().getTipCodigo().equals("FN")) {
			return true;
		}
		return false;
	}
	
	public boolean verificarFinalizarSolicitud() {
		if(listaDetallesSolicitud.size() < 1) {
			return true;
		}
		if(listaDetallesSolicitud.get(listaDetallesSolicitud.size()-1).getDetFechaFin() == null 
			|| solicitudSeleccionada.getRegTipo().getTipCodigo().equals("FN")) {
			return true;
		}
		return false;
	}
	
	public boolean verificarNuevaActividad() {
		if(listaDetallesSolicitud.size() < 1) {
			return false;
		}
		if(listaDetallesSolicitud.get(listaDetallesSolicitud.size()-1).getDetFechaFin() == null 
			|| solicitudSeleccionada.getRegTipo().getTipCodigo().equals("FN")) {
			return true;
		}
		return false;
	}

	//Traspasar una solicitud a otro técnico
	public void actionListenerTraspasar() {
		try {
			int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
			SegUsuario persona;
			Timestamp tiempo = new Timestamp(System.currentTimeMillis());

			persona = managerSeguridades.findByIdSegUsuario(id_user);

			nuevoDetalle.setDetMotTraspaso(moticoTraspaso);
			nuevoDetalle.setSegUsuario(managerSeguridades.findByIdSegUsuario(idTecnicoSeleccionado));
			nuevoDetalle.setDetUsuAnterior(persona.getNombres() + " " + persona.getApellidos());
			nuevoDetalle.setFechaModi(tiempo);
			nuevoDetalle.setRegSolicitud(solicitudSeleccionada);
			nuevoDetalle.setUsuarioCrea(persona.getNombres() + " " + persona.getApellidos());
			nuevoDetalle.setEstadoMostrar(true);
			solicitudSeleccionada.setIdTecnico(idTecnicoSeleccionado);
			solicitudSeleccionada
					.setNombreTecnico(managerSeguridades.findByIdSegUsuario(idTecnicoSeleccionado).getApellidos() + " "
							+ managerSeguridades.findByIdSegUsuario(idTecnicoSeleccionado).getNombres());
			nuevoDetalle.setRegInsumo(solicitudSeleccionada.getRegInsumo());
			nuevoDetalle.setSegDependencia(managerDetalleSolicitudes.findByIdDependencia(solicitudSeleccionada.getIdDependencia()));
			if (listaDetallesSolicitud.size() == 0) {
				nuevoDetalle.setDetFechaIni(solicitudSeleccionada.getRegFechaCreacion());
			} else {
				nuevoDetalle
						.setDetFechaIni(listaDetallesSolicitud.get(listaDetallesSolicitud.size() - 1).getDetFechaFin());
			}
			managerDetalleSolicitudes.insertarDetalle(nuevoDetalle);
			managerSolicitudes.actualizarTecnicoSolicitud(solicitudSeleccionada);
			listaDetallesSolicitud = managerDetalleSolicitudes
					.findDetallesBySolicitud(solicitudSeleccionada.getRegId());
			nuevoDetalle = new Detalle();
			idTecnicoSeleccionado = 0;
			moticoTraspaso = "";
			verificarTraspasar();
			verificarFinalizarSolicitud();
			verificarNuevaActividad();
			JSFUtil.crearMensajeINFO("Solicitud Traspasada correctamente");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	//Metodo de generar la hora actual y asignaria a una variable global
	public void generarTiempoActual() {
		System.out.println("..................." + tiempoActual);
		tiempoActual = new Timestamp(System.currentTimeMillis());
	}

	//Método para registrar una actividad que pertenece a una solicitud.
	public void actionListenerRegistrarActividadSolicitud() {
		try {
			int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
			Timestamp tiempo = new Timestamp(System.currentTimeMillis());
			SegUsuario persona;
			persona = managerSeguridades.findByIdSegUsuario(id_user);

			nuevoDetalle.setSegUsuario(managerSeguridades.findByIdSegUsuario(id_user));
			nuevoDetalle.setRegSolicitud(solicitudSeleccionada);
			nuevoDetalle.setRegInsumo(solicitudSeleccionada.getRegInsumo());			
			nuevoDetalle.setSegDependencia(managerDetalleSolicitudes.findByIdDependencia(solicitudSeleccionada.getIdDependencia()));
			nuevoDetalle.setUsuarioCrea(persona.getNombres() + " " + persona.getApellidos());
			if (listaDetallesSolicitud.size() == 0) {
				nuevoDetalle.setDetFechaIni(solicitudSeleccionada.getRegFechaCreacion());
			} else {
				nuevoDetalle
						.setDetFechaIni(listaDetallesSolicitud.get(listaDetallesSolicitud.size() - 1).getDetFechaFin());
			}
			if (managerDetalleSolicitudes.findByIdTipo(tipoSelected).getTipCodigo().equals("FN")) {
				nuevoDetalle.setDetFechaFin(tiempo);
				nuevoDetalle.setDetTiempo(findDifference(nuevoDetalle.getDetFechaIni().toString(), tiempo.toString()));
			}
			cambioEstadoSolicitud();
			nuevoDetalle.setRegTipo(managerDetalleSolicitudes.findByIdTipo(tipoSelected));
			nuevoDetalle.setRegActividad(managerActividades.findActividadById(actividadSeleccionada));
			nuevoDetalle.setEstadoMostrar(true);
			managerDetalleSolicitudes.insertarDetalle(nuevoDetalle);
			listaDetallesSolicitud = managerDetalleSolicitudes
					.findDetallesBySolicitud(solicitudSeleccionada.getRegId());
			nuevoDetalle = new Detalle();
			verificarTraspasar();
			verificarFinalizarSolicitud();
			verificarNuevaActividad();
			JSFUtil.crearMensajeINFO("Actividad registrada correctamente");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	//Método para registrar una actividad que no esté ligada a una solicitud
	public void actionListenerRegistrarActividad() {
		try {
			int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
			Timestamp tiempo = new Timestamp(System.currentTimeMillis());
			SegUsuario persona;
			persona = managerSeguridades.findByIdSegUsuario(id_user);

			nuevoDetalle.setSegUsuario(managerSeguridades.findByIdSegUsuario(id_user));
			nuevoDetalle.setRegInsumo(managerInsumos.findByIdInsumo(idInsumoSeleccionadonuevo));
			nuevoDetalle.setSegDependencia(managerSeguridades.findByIdSegDependencia(idDepandenciaSeleccionadanuevo));
			nuevoDetalle.setUsuarioCrea(persona.getNombres() + " " + persona.getApellidos());
			nuevoDetalle.setDetFechaIni(DateToTimeStamp(fechaIninuevo));
			if (managerDetalleSolicitudes.findByIdTipo(tipoSelectednuevo).getTipCodigo().equals("FN")
					&& fechaFin == null) {
				nuevoDetalle.setDetFechaFin(tiempo);
				nuevoDetalle.setDetTiempo(findDifference(nuevoDetalle.getDetFechaIni().toString(), tiempo.toString()));
			} else if (fechaFinnuevo != null) {
				nuevoDetalle.setDetFechaFin(DateToTimeStamp(fechaFinnuevo));
				nuevoDetalle.setDetTiempo(findDifference(nuevoDetalle.getDetFechaIni().toString(),
						nuevoDetalle.getDetFechaFin().toString()));
			}
			nuevoDetalle.setRegTipo(managerDetalleSolicitudes.findByIdTipo(tipoSelectednuevo));
			nuevoDetalle.setRegActividad(managerActividades.findActividadById(actividadSeleccionadanueva));
			nuevoDetalle.setEstadoMostrar(true);
			managerDetalleSolicitudes.insertarDetalle(nuevoDetalle);
			;
			nuevoDetalle = new Detalle();
			tipoSelectednuevo = 0;
			actividadSeleccionadanueva = 0;
			idInsumoSeleccionadonuevo = 0;
			idDepandenciaSeleccionadanuevo = 0;
			fechaFinnuevo = null;
			fechaIninuevo = null;
			listaDetalles = managerDetalleSolicitudes
					.findDetallesByTecnico(beanSegLogin.getLoginDTO().getIdSegUsuario());
			JSFUtil.crearMensajeINFO("Actividad registrada correctamente");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	// Metodo para convertir de Tipo Date a Timestamp
	public Timestamp DateToTimeStamp(Date fecha) {
		Timestamp ts = null;
		try {
			String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(fecha.getTime());
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = df.parse(timeStamp);
			long time = date.getTime();
			ts = new Timestamp(time);
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return ts;
	}

	//Cambiar de estado a una solicitud como FINALIZADO
	public void actionListenerFinalizarSolicitud() {
		try {
			int cambios;
			Timestamp tiempo = new Timestamp(System.currentTimeMillis());
			solicitudSeleccionada.setRegTipo(managerSolicitudes.findTipoEstadoFinalizada().get(0));
			solicitudSeleccionada.setFechaModi(tiempo);
			solicitudSeleccionada
					.setRegFechaFin(listaDetallesSolicitud.get(listaDetallesSolicitud.size() - 1).getDetFechaFin());
			solicitudSeleccionada
					.setRegTiempofin(findDifference(listaDetallesSolicitud.get(0).getDetFechaIni().toString(),
							listaDetallesSolicitud.get(listaDetallesSolicitud.size() - 1).getDetFechaFin().toString()));
			cambios = managerDetalleSolicitudes.actualizarDetallesMostrar(solicitudSeleccionada.getRegId());
			managerSolicitudes.cambiarEstadoSolicitud(solicitudSeleccionada);
			verificarTraspasar();
			verificarFinalizarSolicitud();
			verificarNuevaActividad();
			JSFUtil.crearMensajeINFO("FINALIZADA CORRECTAMENTE");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	//Cambio de estado de una solicitud dependiendo de las actividades(excepto a finalizado)
	public void cambioEstadoSolicitud() {
		try {
			if (managerDetalleSolicitudes.findByIdTipo(tipoSelected).getTipCodigo().equals("FN") == false) {
				Timestamp tiempo = new Timestamp(System.currentTimeMillis());
				solicitudSeleccionada.setRegTipo(managerDetalleSolicitudes.findByIdTipo(tipoSelected));
				solicitudSeleccionada.setFechaModi(tiempo);
				managerSolicitudes.cambiarEstadoSolicitud(solicitudSeleccionada);
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	//Editar una actividad realizada desde pantalla de Actividades
	public void actionListenerActualizarEdicionDetalle() {
		try {
			Timestamp tiempo = new Timestamp(System.currentTimeMillis());
			edicionDetalle.setDetFechaIni(DateToTimeStamp(fechaIni));
			if (managerDetalleSolicitudes.findByIdTipo(tipoSelected).getTipCodigo().equals("FN")
					&& edicionDetalle.getRegSolicitud() != null) {
				edicionDetalle.setDetFechaFin(tiempo);
				edicionDetalle.setDetTiempo(findDifference(edicionDetalle.getDetFechaIni().toString(), tiempo.toString()));
			} else if (fechaFin != null) {
				edicionDetalle.setDetFechaFin(DateToTimeStamp(fechaFin));
				edicionDetalle.setDetTiempo(findDifference(edicionDetalle.getDetFechaIni().toString(),
						edicionDetalle.getDetFechaFin().toString()));
			}
			edicionDetalle.setRegActividad(managerActividades.findActividadById(actividadSeleccionada));
			edicionDetalle.setSegDependencia(managerDetalleSolicitudes.findByIdDependencia(idDepandenciaSeleccionada));
			edicionDetalle.setRegInsumo(managerInsumos.findByIdInsumo(idInsumoSeleccionado));
			edicionDetalle.setRegTipo(managerDetalleSolicitudes.findByIdTipo(tipoSelected));
			edicionDetalle.setFechaModi(tiempo);
			if (edicionDetalle.getRegSolicitud() != null) {
				cambioEstadoSolicitud();
			}
			managerDetalleSolicitudes.actualizarDetalleActividad(edicionDetalle);
			listaDetalles = managerDetalleSolicitudes
					.findDetallesByTecnico(beanSegLogin.getLoginDTO().getIdSegUsuario());
			edicionDetalle = new Detalle();
			fechaIni = null;
			fechaFin = null;
			solicitudSeleccionada = new RegSolicitud();
			JSFUtil.crearMensajeINFO("Actividad actualizado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}
	
	//Editar una actividad realizada desde pantalla de detalle solicitud
	public void actionListenerActualizarEdicionDetalleSolicitud() {
		try {
			Timestamp tiempo = new Timestamp(System.currentTimeMillis());
			edicionDetalle.setDetFechaIni(DateToTimeStamp(fechaIni));
			if (managerDetalleSolicitudes.findByIdTipo(tipoSelected).getTipCodigo().equals("FN")
					&& edicionDetalle.getRegSolicitud() != null) {
				edicionDetalle.setDetFechaFin(tiempo);
				edicionDetalle.setDetTiempo(findDifference(edicionDetalle.getDetFechaIni().toString(), tiempo.toString()));
			} else if (fechaFin != null) {
				edicionDetalle.setDetFechaFin(DateToTimeStamp(fechaFin));
				edicionDetalle.setDetTiempo(findDifference(edicionDetalle.getDetFechaIni().toString(),
						edicionDetalle.getDetFechaFin().toString()));
			}
			edicionDetalle.setRegActividad(managerActividades.findActividadById(actividadSeleccionada));
			edicionDetalle.setSegDependencia(managerDetalleSolicitudes.findByIdDependencia(idDepandenciaSeleccionada));
			edicionDetalle.setRegInsumo(managerInsumos.findByIdInsumo(idInsumoSeleccionado));
			edicionDetalle.setRegTipo(managerDetalleSolicitudes.findByIdTipo(tipoSelected));
			edicionDetalle.setFechaModi(tiempo);
			if (edicionDetalle.getRegSolicitud() != null) {
				cambioEstadoSolicitud();
			}
			managerDetalleSolicitudes.actualizarDetalleActividad(edicionDetalle);
			listaDetalles = managerDetalleSolicitudes
					.findDetallesByTecnico(beanSegLogin.getLoginDTO().getIdSegUsuario());
			edicionDetalle = new Detalle();
			fechaIni = null;
			fechaFin = null;
			verificarTraspasar();
			verificarFinalizarSolicitud();
			verificarNuevaActividad();
			JSFUtil.crearMensajeINFO("Actividad actualizado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}
	
	

	public void actionListenerCargarDetalle(Detalle detalle) {
		try {
			edicionDetalle = detalle;
			fechaIni = edicionDetalle.getDetFechaIni();
			fechaFin = edicionDetalle.getDetFechaFin();
			if (edicionDetalle.getRegActividad() != null) {
				actividadSeleccionada = edicionDetalle.getRegActividad().getActId();
			} else {
				actividadSeleccionada = 0;
			}
			if (edicionDetalle.getRegTipo() != null) {
				tipoSelected = edicionDetalle.getRegTipo().getTipId();
			} else {
				tipoSelected = 0;
			}
			if (edicionDetalle.getSegDependencia() != null) {
				idDepandenciaSeleccionada = edicionDetalle.getSegDependencia().getIdSegDependencia();
			} else {
				idDepandenciaSeleccionada = 0;
			}
			if (edicionDetalle.getRegInsumo() != null) {
				idInsumoSeleccionado = edicionDetalle.getRegInsumo().getInsId();
			} else {
				idInsumoSeleccionado = 0;
			}
			if (detalle.getRegSolicitud() != null) {
				solicitudSeleccionada = managerSolicitudes
						.findSolicitudByCodigo(detalle.getRegSolicitud().getRegCodigo()).get(0);
				listaActividades = AbstraerSolicitudes(
						managerInsumos.findActividadesByInsumo(solicitudSeleccionada.getRegInsumo().getInsId()));
			} else {
				listaActividades = managerActividades.findAllActividades();
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}

	}

	public List<RegActividad> AbstraerSolicitudes(List<RegInsAct> insAct) {
		List<RegActividad> listado = new ArrayList<RegActividad>();
		for (int i = 0; i < insAct.size(); i++) {
			listado.add(insAct.get(i).getRegActividad());
		}
		return listado;
	}

	static String findDifference(String start_date, String end_date) {
		// SimpleDateFormat converts the
		// string format to date object
		String tiempo = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// Try Class
		try {
			// parse method is used to parse
			// the text from a string to
			// produce the date
			Date d1 = sdf.parse(start_date);
			Date d2 = sdf.parse(end_date);

			// Calucalte time difference
			// in milliseconds
			long difference_In_Time = d2.getTime() - d1.getTime();

			long difference_In_Seconds = TimeUnit.MILLISECONDS.toSeconds(difference_In_Time) % 60;
			long difference_In_Minutes = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time) % 60;
			long difference_In_Hours = TimeUnit.MILLISECONDS.toHours(difference_In_Time) % 24;
			long difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time);
			
			// % 365
			//long difference_In_Years = TimeUnit.MILLISECONDS.toDays(difference_In_Time) / 365l;

			tiempo = difference_In_Days + "d: " + difference_In_Hours + "h: " + difference_In_Minutes + "m: "
					+ difference_In_Seconds + "s";

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tiempo;
	}

	/**
	 * Método para descargar archivos dependiendo el tipo de archivo
	 * @return El tipo de archivo para la descarga
	 */
	public StreamedContent Descargar() {
		try {
			File file = new File(solicitudSeleccionada.getRegAdjunto());
			FileInputStream inputStream = new FileInputStream(file);
			String nombre = FilenameUtils.getName(file.getName());
			String extension = FilenameUtils.getExtension(nombre);
			if (extension.equals("jpeg") || extension.equals("jpg")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.jpg").contentType("image/jpeg")
						.stream(() -> inputStream).build();
			}
			if (extension.equals("png")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.png").contentType("image/png")
						.stream(() -> inputStream).build();
			}
			if (extension.equals("pdf")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.pdf").contentType("application/pdf")
						.stream(() -> inputStream).build();
			}
			if (extension.equals("doc")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.doc").contentType("application/msword")
						.stream(() -> inputStream).build();
			}
			if (extension.equals("docx")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.docx")
						.contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
						.stream(() -> inputStream).build();
			}
			if (extension.equals("rar")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.rar").contentType("application/vnd.rar")
						.stream(() -> inputStream).build();
			}
			if (extension.equals("7z")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.7z")
						.contentType("application/x-7z-compressed").stream(() -> inputStream).build();
			}
			if (extension.equals("xls")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.xls")
						.contentType("application/vnd.ms-excel").stream(() -> inputStream).build();
			}
			if (extension.equals("xlsx")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.xlsx")
						.contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
						.stream(() -> inputStream).build();
			}

			if (extension.equals("ppt")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.ppt")
						.contentType("application/vnd.ms-powerpoint").stream(() -> inputStream).build();
			}
			if (extension.equals("pptx")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.pptx")
						.contentType("application/vnd.openxmlformats-officedocument.presentationml.presentation")
						.stream(() -> inputStream).build();
			}
			if (extension.equals("mp4")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.mp4").contentType("audio/mpeg")
						.stream(() -> inputStream).build();
			}
			if (extension.equals("mp3")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.mp3").contentType("video/mpeg")
						.stream(() -> inputStream).build();
			}
			if (extension.equals("txt")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.txt").contentType("text/plain")
						.stream(() -> inputStream).build();
			}
			if (extension.equals("csv")) {
				return DefaultStreamedContent.builder().name("archivoAdjunto.csv").contentType("text/csv")
						.stream(() -> inputStream).build();
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public String actionReporteResumido(int usuario, Date fechaini, Date fechafin) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("usuario",usuario);
		parametros.put("fechaini",fechaini);
		parametros.put("fechafin",fechafin);
		FacesContext context = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String ruta = servletContext.getRealPath("resources/jasper/reporte_resumido.jasper");
		System.out.println(ruta);
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		response.addHeader("Content-disposition", "attachment;filename=reporte_resumido.pdf");
		response.setContentType("application/pdf");
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection = null;
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sumakruray", "postgres",
					"2001");
			JasperPrint impresion = JasperFillManager.fillReport(ruta, parametros, connection);
			JasperExportManager.exportReportToPdfStream(impresion, response.getOutputStream());
			context.getApplication().getStateManager().saveView(context);
			System.out.println("reporte generado.");
			context.responseComplete();
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}
	
	public String actionReporteDetallado(int usuario, Date fechaini, Date fechafin) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("usuario",usuario);
		parametros.put("fechaini",fechaini);
		parametros.put("fechafin",fechafin);
		FacesContext context = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String ruta = servletContext.getRealPath("resources/jasper/reporte_detallado.jasper");
		System.out.println(ruta);
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		response.addHeader("Content-disposition", "attachment;filename=reporte_detallado.pdf");
		response.setContentType("application/pdf");
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection = null;
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sumakruray", "postgres",
					"2001");
			JasperPrint impresion = JasperFillManager.fillReport(ruta, parametros, connection);
			JasperExportManager.exportReportToPdfStream(impresion, response.getOutputStream());
			context.getApplication().getStateManager().saveView(context);
			System.out.println("reporte generado.");
			context.responseComplete();
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public List<RegTipo> getListaTipo() {
		return listaTipo;
	}

	public void setListaTipo(List<RegTipo> listaTipo) {
		this.listaTipo = listaTipo;
	}

	public Timestamp getTiempoActual() {
		return tiempoActual;
	}

	public void setTiempoActual(Timestamp tiempoActual) {
		this.tiempoActual = tiempoActual;
	}

	public RegSolicitud getSolicitudSeleccionada() {
		return solicitudSeleccionada;
	}

	public void setSolicitudSeleccionada(RegSolicitud solicitudSeleccionada) {
		this.solicitudSeleccionada = solicitudSeleccionada;
	}

	public List<Detalle> getListaDetalles() {
		return listaDetalles;
	}

	public void setListaDetalles(List<Detalle> listaDetalles) {
		this.listaDetalles = listaDetalles;
	}

	public List<RegSolicitud> getListaSolicitudesTecnico() {
		return listaSolicitudesTecnico;
	}

	public void setListaSolicitudesTecnico(List<RegSolicitud> listaSolicitudesTecnico) {
		this.listaSolicitudesTecnico = listaSolicitudesTecnico;
	}

	public List<Detalle> getListaDetallesSolicitud() {
		return listaDetallesSolicitud;
	}

	public void setListaDetallesSolicitud(List<Detalle> listaDetallesSolicitud) {
		this.listaDetallesSolicitud = listaDetallesSolicitud;
	}

	public List<SegUsuario> getListaTecnicos() {
		return listaTecnicos;
	}

	public void setListaTecnicos(List<SegUsuario> listaTecnicos) {
		this.listaTecnicos = listaTecnicos;
	}

	public int getIdTecnicoSeleccionado() {
		return idTecnicoSeleccionado;
	}

	public void setIdTecnicoSeleccionado(int idTecnicoSeleccionado) {
		this.idTecnicoSeleccionado = idTecnicoSeleccionado;
	}

	public String getMoticoTraspaso() {
		return moticoTraspaso;
	}

	public void setMoticoTraspaso(String moticoTraspaso) {
		this.moticoTraspaso = moticoTraspaso;
	}

	public Detalle getNuevoDetalle() {
		return nuevoDetalle;
	}

	public void setNuevoDetalle(Detalle nuevoDetalle) {
		this.nuevoDetalle = nuevoDetalle;
	}

	public List<RegInsAct> getListaActividadesByInsumo() {
		return listaActividadesByInsumo;
	}

	public void setListaActividadesByInsumo(List<RegInsAct> listaActividadesByInsumo) {
		this.listaActividadesByInsumo = listaActividadesByInsumo;
	}

	public int getTipoSelected() {
		return tipoSelected;
	}

	public void setTipoSelected(int tipoSelected) {
		this.tipoSelected = tipoSelected;
	}

	public int getActividadSeleccionada() {
		return actividadSeleccionada;
	}

	public void setActividadSeleccionada(int actividadSeleccionada) {
		this.actividadSeleccionada = actividadSeleccionada;
	}

	public Date getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Detalle getEdicionDetalle() {
		return edicionDetalle;
	}

	public void setEdicionDetalle(Detalle edicionDetalle) {
		this.edicionDetalle = edicionDetalle;
	}

	public List<RegActividad> getListaActividades() {
		return listaActividades;
	}

	public void setListaActividades(List<RegActividad> listaActividades) {
		this.listaActividades = listaActividades;
	}

	public int getIdInsumoSeleccionado() {
		return idInsumoSeleccionado;
	}

	public void setIdInsumoSeleccionado(int idInsumoSeleccionado) {
		this.idInsumoSeleccionado = idInsumoSeleccionado;
	}

	public int getIdDepandenciaSeleccionada() {
		return idDepandenciaSeleccionada;
	}

	public void setIdDepandenciaSeleccionada(int idDepandenciaSeleccionada) {
		this.idDepandenciaSeleccionada = idDepandenciaSeleccionada;
	}

	public List<RegInsumo> getListaInsumos() {
		return listaInsumos;
	}

	public void setListaInsumos(List<RegInsumo> listaInsumos) {
		this.listaInsumos = listaInsumos;
	}

	public List<SegDependencia> getListaDependencias() {
		return listaDependencias;
	}

	public void setListaDependencias(List<SegDependencia> listaDependencias) {
		this.listaDependencias = listaDependencias;
	}

	public int getTipoSelectednuevo() {
		return tipoSelectednuevo;
	}

	public void setTipoSelectednuevo(int tipoSelectednuevo) {
		this.tipoSelectednuevo = tipoSelectednuevo;
	}

	public int getActividadSeleccionadanueva() {
		return actividadSeleccionadanueva;
	}

	public void setActividadSeleccionadanueva(int actividadSeleccionadanueva) {
		this.actividadSeleccionadanueva = actividadSeleccionadanueva;
	}

	public int getIdInsumoSeleccionadonuevo() {
		return idInsumoSeleccionadonuevo;
	}

	public void setIdInsumoSeleccionadonuevo(int idInsumoSeleccionadonuevo) {
		this.idInsumoSeleccionadonuevo = idInsumoSeleccionadonuevo;
	}

	public int getIdDepandenciaSeleccionadanuevo() {
		return idDepandenciaSeleccionadanuevo;
	}

	public void setIdDepandenciaSeleccionadanuevo(int idDepandenciaSeleccionadanuevo) {
		this.idDepandenciaSeleccionadanuevo = idDepandenciaSeleccionadanuevo;
	}

	public Date getFechaIninuevo() {
		return fechaIninuevo;
	}

	public void setFechaIninuevo(Date fechaIninuevo) {
		this.fechaIninuevo = fechaIninuevo;
	}

	public Date getFechaFinnuevo() {
		return fechaFinnuevo;
	}

	public void setFechaFinnuevo(Date fechaFinnuevo) {
		this.fechaFinnuevo = fechaFinnuevo;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}

	public int getIdTecnicoReporte() {
		return idTecnicoReporte;
	}

	public void setIdTecnicoReporte(int idTecnicoReporte) {
		this.idTecnicoReporte = idTecnicoReporte;
	}
	

}
