package sumakruray.controller.actividades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.util.LangUtils;

import sumakruray.controller.JSFUtil;
import sumakruray.controller.seguridades.BeanSegLogin;
import sumakruray.model.actividades.managers.ManagerDetalleSolicitudes;
import sumakruray.model.actividades.managers.ManagerInsumos;
import sumakruray.model.actividades.managers.ManagerReportes;
import sumakruray.model.actividades.managers.ManagerSolicitudes;
import sumakruray.model.core.entities.Detalle;
import sumakruray.model.core.entities.RegInsumo;
import sumakruray.model.core.entities.RegSolicitud;
import sumakruray.model.core.entities.RegTipo;
import sumakruray.model.core.entities.SegDependencia;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.core.utils.ModelUtil;
import sumakruray.model.seguridades.managers.ManagerSeguridades;

/**
 * @author rfern
 *
 */

@Named
@SessionScoped
public class BeanReportes implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private ManagerReportes managerReportes;

	@EJB
	private ManagerSolicitudes managerSolicitudes;

	@EJB
	private ManagerInsumos managerInsumos;

	@EJB
	private ManagerSeguridades managerSeguridades;

	private List<RegSolicitud> listaSolicitudesConsulta;
	private List<RegSolicitud> solicitudesFiltradas;
	private List<RegTipo> listaEstados;
	private List<SegDependencia> listaDependencias;
	private List<SegUsuario> listaTecnicos;
	private List<RegInsumo> listaInsumos;
	private List<Detalle> listaDetallesConsulta;
	private List<SegUsuario> listaUsuariosNormales;

	private int idTecnicoSeleccionado;
	private int idInsumoSeleccionado;
	private int idDependenciaSeleccionada;
	private int idEstadoSeleccionado;
	private int idUsuarioSeleccionado;
	private String nombreDependenciaSeleccionada;
	private Date fechaini;
	private Date fechafin;
	private String consulta;

	@Inject
	private BeanSegLogin beanSegLogin;

	public BeanReportes() {

	}

	@PostConstruct
	public void inicializar() {
		listaDetallesConsulta = managerReportes.findAllDetalles();
		listaSolicitudesConsulta = managerReportes.findAllSolicitudes();
		listaEstados = managerSolicitudes.findAllTipoEstados();
		fechaini = ModelUtil.addDays(new Date(), -1);
		fechafin = new Date();
		consulta = "";
	}

	public String actionMenuReporteActividades() {
		listaDetallesConsulta = managerReportes.findAllDetalles();
		listaTecnicos = managerSolicitudes.findUsuariosTecnicos();
		listaUsuariosNormales = managerSolicitudes.findUsuariosNormales();
		listaInsumos = managerInsumos.findAllInsumos();
		listaEstados = managerSolicitudes.findAllTipoEstados();
		listaDependencias = managerSeguridades.findAllDependencias();
		idDependenciaSeleccionada = 0;
		idEstadoSeleccionado = 0;
		idInsumoSeleccionado = 0;
		idTecnicoSeleccionado = 0;
		fechaini = ModelUtil.addDays(new Date(), -1);
		fechafin = new Date();
		consulta = "";
		return "actividades?faces-redirect=true";
	}

	public String actionMenuImprimirReporte() {
		listaTecnicos = managerSolicitudes.findUsuariosTecnicos();
		listaDetallesConsulta = managerReportes.findAllDetalles();
		idTecnicoSeleccionado = 0;
		fechaini = ModelUtil.addDays(new Date(), -1);
		fechafin = new Date();
		consulta = "";
		return "imprimir_reporte?faces-redirect=true";
	}

	public String actionMenuReporteSolicitudes() {
		listaSolicitudesConsulta = managerReportes.findAllSolicitudes();
		listaTecnicos = managerSolicitudes.findUsuariosTecnicos();
		listaUsuariosNormales = managerSolicitudes.findUsuariosNormales();
		listaInsumos = managerInsumos.findAllInsumos();
		listaEstados = managerSolicitudes.findAllTipoEstados();
		listaDependencias = managerSeguridades.findAllDependencias();
		idDependenciaSeleccionada = 0;
		idEstadoSeleccionado = 0;
		idInsumoSeleccionado = 0;
		idTecnicoSeleccionado = 0;
		idUsuarioSeleccionado = 0;
		fechaini = ModelUtil.addDays(new Date(), -1);
		fechafin = new Date();
		consulta = "";
		return "solicitudes?faces-redirect=true";
	}

	public void generarConsultaActividades() {
		if (idDependenciaSeleccionada != 0 && consulta.equals("")) {
			consulta = "o.segDependencia.idSegDependencia =" + idDependenciaSeleccionada;
		} else if (idDependenciaSeleccionada != 0) {
			consulta = consulta + " AND o.segDependencia.idSegDependencia =" + idDependenciaSeleccionada;
		}
		if (idEstadoSeleccionado != 0 && consulta.equals("")) {
			consulta = "o.regTipo.tipId =" + idEstadoSeleccionado;
		} else if (idEstadoSeleccionado != 0) {
			consulta = consulta + " AND o.regTipo.tipId  =" + idEstadoSeleccionado;
		}
		if (idInsumoSeleccionado != 0 && consulta.equals("")) {
			consulta = "o.regInsumo.insId =" + idInsumoSeleccionado;
		} else if (idInsumoSeleccionado != 0) {
			consulta = consulta + " AND o.regInsumo.insId =" + idInsumoSeleccionado;
		}
		if (idTecnicoSeleccionado != 0 && consulta.equals("")) {
			consulta = "o.segUsuario.idSegUsuario =" + idTecnicoSeleccionado;
		} else if (idTecnicoSeleccionado != 0) {
			consulta = consulta + " AND o.segUsuario.idSegUsuario =" + idTecnicoSeleccionado;
		}
	}

	public void generarConsultaSolicitudes() {
		if (nombreDependenciaSeleccionada != null && consulta.equals("")) {
			consulta = "o.regDependencia ='" + nombreDependenciaSeleccionada + "'";
		} else if (nombreDependenciaSeleccionada != null) {
			consulta = consulta + " AND o.regDependencia ='" + nombreDependenciaSeleccionada + "'";
		}
		if (idEstadoSeleccionado != 0 && consulta.equals("")) {
			consulta = "o.regTipo.tipId =" + idEstadoSeleccionado;
		} else if (idEstadoSeleccionado != 0) {
			consulta = consulta + " AND o.regTipo.tipId  =" + idEstadoSeleccionado;
		}
		if (idInsumoSeleccionado != 0 && consulta.equals("")) {
			consulta = "o.regInsumo.insId =" + idInsumoSeleccionado;
		} else if (idInsumoSeleccionado != 0) {
			consulta = consulta + " AND o.regInsumo.insId =" + idInsumoSeleccionado;
		}
		if (idTecnicoSeleccionado != 0 && consulta.equals("")) {
			consulta = "o.idTecnico =" + idTecnicoSeleccionado;
		} else if (idTecnicoSeleccionado != 0) {
			consulta = consulta + " AND o.idTecnico =" + idTecnicoSeleccionado;
		}
		if (idUsuarioSeleccionado != 0 && consulta.equals("")) {
			consulta = "o.segUsuario.idSegUsuario =" + idUsuarioSeleccionado;
		} else if (idUsuarioSeleccionado != 0) {
			consulta = consulta + " AND o.segUsuario.idSegUsuario =" + idUsuarioSeleccionado;
		}
	}

	public void actionListenerConsultarActividades() {
		generarConsultaActividades();
		listaDetallesConsulta = managerReportes.findWithConsultaActividades(consulta, fechaini, fechafin);
		JSFUtil.crearMensajeINFO("Registros encontrados: " + listaDetallesConsulta.size());
		idDependenciaSeleccionada = 0;
		idEstadoSeleccionado = 0;
		idInsumoSeleccionado = 0;
		idTecnicoSeleccionado = 0;
		consulta = "";
	}

	public void actionListenerConsultarSolicitudes() {
		generarConsultaSolicitudes();
		listaSolicitudesConsulta = managerReportes.findWithConsultaSolicitudes(consulta, fechaini, fechafin);
		JSFUtil.crearMensajeINFO("Registros encontrados: " + listaSolicitudesConsulta.size());
		nombreDependenciaSeleccionada = null;
		idEstadoSeleccionado = 0;
		idInsumoSeleccionado = 0;
		idTecnicoSeleccionado = 0;
		idUsuarioSeleccionado = 0;
		consulta = "";
	}

	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (LangUtils.isValueBlank(filterText)) {
			return true;
		}
		int filterInt = getInteger(filterText);

		RegSolicitud solicitud = (RegSolicitud) value;
		return solicitud.getRegCodigo().toLowerCase().contains(filterText)
				|| solicitud.getRegDependencia().toLowerCase().contains(filterText)
				|| solicitud.getRegTipo().getTipNombre().toLowerCase().contains(filterText)
				|| solicitud.getRegFechaCreacion().toString().toLowerCase().contains(filterText);
	}

	private int getInteger(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	public List<RegSolicitud> getSolicitudesFiltradas() {
		return solicitudesFiltradas;
	}

	public void setSolicitudesFiltradas(List<RegSolicitud> solicitudesFiltradas) {
		this.solicitudesFiltradas = solicitudesFiltradas;
	}

	public List<RegTipo> getListaEstados() {
		return listaEstados;
	}

	public void setListaEstados(List<RegTipo> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public int getIdTecnicoSeleccionado() {
		return idTecnicoSeleccionado;
	}

	public void setIdTecnicoSeleccionado(int idTecnicoSeleccionado) {
		this.idTecnicoSeleccionado = idTecnicoSeleccionado;
	}

	public int getIdInsumoSeleccionado() {
		return idInsumoSeleccionado;
	}

	public void setIdInsumoSeleccionado(int idInsumoSeleccionado) {
		this.idInsumoSeleccionado = idInsumoSeleccionado;
	}

	public int getIdDependenciaSeleccionada() {
		return idDependenciaSeleccionada;
	}

	public void setIdDependenciaSeleccionada(int idDependenciaSeleccionada) {
		this.idDependenciaSeleccionada = idDependenciaSeleccionada;
	}

	public Date getFechaini() {
		return fechaini;
	}

	public void setFechaini(Date fechaini) {
		this.fechaini = fechaini;
	}

	public Date getFechafin() {
		return fechafin;
	}

	public void setFechafin(Date fechafin) {
		this.fechafin = fechafin;
	}

	public List<SegDependencia> getListaDependencias() {
		return listaDependencias;
	}

	public void setListaDependencias(List<SegDependencia> listaDependencias) {
		this.listaDependencias = listaDependencias;
	}

	public List<SegUsuario> getListaTecnicos() {
		return listaTecnicos;
	}

	public void setListaTecnicos(List<SegUsuario> listaTecnicos) {
		this.listaTecnicos = listaTecnicos;
	}

	public List<RegInsumo> getListaInsumos() {
		return listaInsumos;
	}

	public void setListaInsumos(List<RegInsumo> listaInsumos) {
		this.listaInsumos = listaInsumos;
	}

	public int getIdEstadoSeleccionado() {
		return idEstadoSeleccionado;
	}

	public void setIdEstadoSeleccionado(int idEstadoSeleccionado) {
		this.idEstadoSeleccionado = idEstadoSeleccionado;
	}

	public List<Detalle> getListaDetallesConsulta() {
		return listaDetallesConsulta;
	}

	public void setListaDetallesConsulta(List<Detalle> listaDetallesConsulta) {
		this.listaDetallesConsulta = listaDetallesConsulta;
	}

	public int getIdUsuarioSeleccionado() {
		return idUsuarioSeleccionado;
	}

	public void setIdUsuarioSeleccionado(int idUsuarioSeleccionado) {
		this.idUsuarioSeleccionado = idUsuarioSeleccionado;
	}

	public List<SegUsuario> getListaUsuariosNormales() {
		return listaUsuariosNormales;
	}

	public void setListaUsuariosNormales(List<SegUsuario> listaUsuariosNormales) {
		this.listaUsuariosNormales = listaUsuariosNormales;
	}

	public String getNombreDependenciaSeleccionada() {
		return nombreDependenciaSeleccionada;
	}

	public void setNombreDependenciaSeleccionada(String nombreDependenciaSeleccionada) {
		this.nombreDependenciaSeleccionada = nombreDependenciaSeleccionada;
	}

	public List<RegSolicitud> getListaSolicitudesConsulta() {
		return listaSolicitudesConsulta;
	}

	public void setListaSolicitudesConsulta(List<RegSolicitud> listaSolicitudesConsulta) {
		this.listaSolicitudesConsulta = listaSolicitudesConsulta;
	}

}
