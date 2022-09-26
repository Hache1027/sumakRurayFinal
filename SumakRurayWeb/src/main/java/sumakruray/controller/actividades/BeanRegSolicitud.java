package sumakruray.controller.actividades;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;

import sumakruray.controller.JSFUtil;
import sumakruray.controller.seguridades.BeanSegLogin;
import sumakruray.model.actividades.managers.ManagerInsumos;
import sumakruray.model.actividades.managers.ManagerSolicitudes;
import sumakruray.model.core.entities.RegCodSolicitud;
import sumakruray.model.core.entities.RegInsumo;
import sumakruray.model.core.entities.RegPath;
import sumakruray.model.core.entities.RegSolicitud;

import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.seguridades.managers.ManagerSeguridades;

@Named
@SessionScoped
public class BeanRegSolicitud implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private ManagerSolicitudes managerSolicitudes;

	@EJB
	private ManagerInsumos managerInsumos;

	@EJB
	private ManagerSeguridades managerSeguridades;

	private List<RegSolicitud> listaSolicitudes;
	private List<RegInsumo> listaInsumoCategoria;
	private RegSolicitud nuevaSolicitud;
	private List<RegSolicitud> listaSolicitudesByUser;
	private SegUsuario tecnicoEncargado;

	private List<SegUsuario> listaTecnicos;
	private List<SegUsuario> listaUsuarios;

	private RegCodSolicitud edicionCodigo;

	private UploadedFile archivo;
	private String path;

	private int idcategoriaSeleccionado;
	private int idInsumoSeleccionado;
	private int idTecnicoSeleccionado;
	private int idUsuarioSolicita;

	@Inject
	private BeanSegLogin beanSegLogin;

	public BeanRegSolicitud() {

	}

	@PostConstruct
	public void inicializar() {
		nuevaSolicitud = new RegSolicitud();
		edicionCodigo = new RegCodSolicitud();
		listaTecnicos = managerSolicitudes.findUsuariosTecnicos();
		listaUsuarios = managerSolicitudes.findUsuariosNormales();
		listaSolicitudes = managerSolicitudes.findAllSolicitudes();
		listaSolicitudesByUser = managerSolicitudes
				.findAllSolicitudesByUser(beanSegLogin.getLoginDTO().getIdSegUsuario());
	}

	public String actionMenuRegistrarSolicitud() {
		listaUsuarios = managerSolicitudes.findUsuariosNormales();
		listaSolicitudes = managerSolicitudes.findAllSolicitudes();
		return "solicitud";
	}

	public void actionListenerInsertarNuevaSolicitud() {
		try {
			// Encontrar datos persona
			int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
			SegUsuario persona;
			Timestamp tiempo = new Timestamp(System.currentTimeMillis());
			persona = managerSeguridades.findByIdSegUsuario(id_user);

			nuevaSolicitud.setRegFechaCreacion(tiempo);
			nuevaSolicitud.setUsuarioCrea(persona.getNombres() + " " + persona.getApellidos());
			nuevaSolicitud.setIdDependencia(persona.getSegDependencia().getIdSegDependencia());
			nuevaSolicitud.setRegDependencia(persona.getSegDependencia().getDepDescripcion());
			nuevaSolicitud
					.setSegUsuario(managerSeguridades.findByIdSegUsuario(beanSegLogin.getLoginDTO().getIdSegUsuario()));
			nuevaSolicitud.setRegTipo(managerSolicitudes.findTipoEstadoPendiente().get(0));
			nuevaSolicitud.setRegInsumo(managerInsumos.findByIdInsumo(idInsumoSeleccionado));
			tecnicoEncargado = managerSolicitudes.findTecnicosByInsumo(idInsumoSeleccionado);
			nuevaSolicitud.setIdTecnico(tecnicoEncargado.getIdSegUsuario());
			nuevaSolicitud.setNombreTecnico(tecnicoEncargado.getApellidos() + " " + tecnicoEncargado.getNombres());
			nuevaSolicitud.setRegCodigo(GenerarCodigoSolicitud());
			if (archivo.getFileName() != null) {
				guardarArchivo();
				nuevaSolicitud.setRegAdjunto(path);
			}
			managerSolicitudes.insertarSolicitud(nuevaSolicitud);

			listaSolicitudes = managerSolicitudes.findAllSolicitudes();
			listaSolicitudesByUser = managerSolicitudes
					.findAllSolicitudesByUser(beanSegLogin.getLoginDTO().getIdSegUsuario());
			nuevaSolicitud = new RegSolicitud();
			idcategoriaSeleccionado = 0;
			idInsumoSeleccionado = 0;
			archivo = null;
			JSFUtil.crearMensajeINFO("Solicitud enviada correctamente");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	public void actionListenerInsertarNuevaSolicitudTecnicos() {
		try {
			int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
			SegUsuario persona_cre;
			persona_cre = managerSeguridades.findByIdSegUsuario(id_user);

			SegUsuario usuario;
			SegUsuario tecnico;
			Timestamp tiempo = new Timestamp(System.currentTimeMillis());
			usuario = managerSeguridades.findByIdSegUsuario(idUsuarioSolicita);
			tecnico = managerSeguridades.findByIdSegUsuario(idTecnicoSeleccionado);

			nuevaSolicitud.setRegFechaCreacion(tiempo);
			nuevaSolicitud.setUsuarioCrea(persona_cre.getNombres() + " " + persona_cre.getApellidos());
			nuevaSolicitud.setIdDependencia(usuario.getSegDependencia().getIdSegDependencia());
			nuevaSolicitud.setRegDependencia(usuario.getSegDependencia().getDepDescripcion());
			nuevaSolicitud.setSegUsuario(usuario);
			nuevaSolicitud.setRegTipo(managerSolicitudes.findTipoEstadoPendiente().get(0));
			nuevaSolicitud.setRegInsumo(managerInsumos.findByIdInsumo(idInsumoSeleccionado));
			nuevaSolicitud.setIdTecnico(tecnico.getIdSegUsuario());
			nuevaSolicitud.setNombreTecnico(tecnico.getApellidos() + " " + tecnico.getNombres());
			nuevaSolicitud.setRegCodigo(GenerarCodigoSolicitud());
			if (archivo.getFileName() != null) {
				guardarArchivo();
				nuevaSolicitud.setRegAdjunto(path);
			}
			managerSolicitudes.insertarSolicitud(nuevaSolicitud);

			listaSolicitudes = managerSolicitudes.findAllSolicitudes();
			listaSolicitudesByUser = managerSolicitudes
					.findAllSolicitudesByUser(beanSegLogin.getLoginDTO().getIdSegUsuario());
			nuevaSolicitud = new RegSolicitud();
			idcategoriaSeleccionado = 0;
			idInsumoSeleccionado = 0;
			idTecnicoSeleccionado = 0;
			idUsuarioSolicita = 0;
			archivo = null;
			JSFUtil.crearMensajeINFO("Solicitud enviada correctamente");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	public String GenerarCodigoSolicitud() {
		try {
			String codigocompleto = "";
			String cod = "SOL";
			int ultimo;
			Date date = new Date();

			SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
			String anio = getYearFormat.format(date);
			if (managerSolicitudes.findUltimoNumeroSolicitud(anio).size() != 0) {
				edicionCodigo = managerSolicitudes.findUltimoNumeroSolicitud(anio).get(0);
				ultimo = edicionCodigo.getCodUltNumero();
				ultimo++;
				String salida = String.format("%05d", ultimo);
				codigocompleto = cod + "-" + anio + "-" + salida;
				edicionCodigo.setCodUltNumero(ultimo);
				managerSolicitudes.actualizarRegCodigoSolicitud(edicionCodigo);
			} else {
				RegCodSolicitud nuevoRegCodigo = new RegCodSolicitud();
				nuevoRegCodigo.setCodUltNumero(1);
				nuevoRegCodigo.setCodAnio(anio);
				managerSolicitudes.insertarRegCodSolicitud(nuevoRegCodigo);
				String salida = String.format("%05d", 1);
				codigocompleto = cod + "-" + anio + "-" + salida;
			}
			return codigocompleto;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return "ERROR??????";
	}

	public UploadedFile getOriginalFile() {
		return archivo;
	}

	public boolean guardarArchivo() {
		try {
			String cod_direccion = "ADJ_S";
			RegPath direccion = managerSolicitudes.findPathByCodigo(cod_direccion).get(0);
			int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
			SegUsuario persona_cre;
			persona_cre = managerSeguridades.findByIdSegUsuario(id_user);
			Date date = new Date();
			SimpleDateFormat formato = new SimpleDateFormat("yyyymmdd HHmmss");
			String nombre = FilenameUtils.getName(archivo.getFileName());
			String nombreArchivo = persona_cre.getIdSegUsuario() + formato.format(date) + "."
					+ FilenameUtils.getExtension(nombre);
			path = direccion.getPaDireccion() + nombreArchivo;

			File file = new File(direccion.getPaDireccion() + nombreArchivo);
			Files.createFile(file.toPath());
			InputStream inputStream = archivo.getInputStream();
			Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public void actionListenerSeleccionarCategoria() {
		listaInsumoCategoria = managerSolicitudes.findInsumosByCategoria(idcategoriaSeleccionado);
	}

	public List<RegSolicitud> getListaSolicitudes() {
		return listaSolicitudes;
	}

	public void setListaSolicitudes(List<RegSolicitud> listaSolicitudes) {
		this.listaSolicitudes = listaSolicitudes;
	}

	public int getIdInsumoSeleccionado() {
		return idInsumoSeleccionado;
	}

	public void setIdInsumoSeleccionado(int idInsumoSeleccionado) {
		this.idInsumoSeleccionado = idInsumoSeleccionado;
	}

	public List<RegInsumo> getListaInsumoCategoria() {
		return listaInsumoCategoria;
	}

	public void setListaInsumoCategoria(List<RegInsumo> listaInsumoCategoria) {
		this.listaInsumoCategoria = listaInsumoCategoria;
	}

	public int getIdcategoriaSeleccionado() {
		return idcategoriaSeleccionado;
	}

	public void setIdcategoriaSeleccionado(int idcategoriaSeleccionado) {
		this.idcategoriaSeleccionado = idcategoriaSeleccionado;
	}

	public RegSolicitud getNuevaSolicitud() {
		return nuevaSolicitud;
	}

	public void setNuevaSolicitud(RegSolicitud nuevaSolicitud) {
		this.nuevaSolicitud = nuevaSolicitud;
	}

	public List<RegSolicitud> getListaSolicitudesByUser() {
		return listaSolicitudesByUser;
	}

	public void setListaSolicitudesByUser(List<RegSolicitud> listaSolicitudesByUser) {
		this.listaSolicitudesByUser = listaSolicitudesByUser;
	}

	public List<SegUsuario> getListaTecnicos() {
		return listaTecnicos;
	}

	public void setListaTecnicos(List<SegUsuario> listaTecnicos) {
		this.listaTecnicos = listaTecnicos;
	}

	public List<SegUsuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<SegUsuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public int getIdTecnicoSeleccionado() {
		return idTecnicoSeleccionado;
	}

	public void setIdTecnicoSeleccionado(int idTecnicoSeleccionado) {
		this.idTecnicoSeleccionado = idTecnicoSeleccionado;
	}

	public int getIdUsuarioSolicita() {
		return idUsuarioSolicita;
	}

	public void setIdUsuarioSolicita(int idUsuarioSolicita) {
		this.idUsuarioSolicita = idUsuarioSolicita;
	}

	public UploadedFile getArchivo() {
		return archivo;
	}

	public void setArchivo(UploadedFile archivo) {
		this.archivo = archivo;
	}

}
