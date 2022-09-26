package sumakruray.model.actividades.managers;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sumakruray.model.core.entities.RegCodSolicitud;
import sumakruray.model.core.entities.RegInsumo;
import sumakruray.model.core.entities.RegPath;
import sumakruray.model.core.entities.RegSolicitud;
import sumakruray.model.core.entities.RegTipo;
import sumakruray.model.core.entities.RegUsuIn;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.core.managers.ManagerDAO;

@Stateless
@LocalBean
public class ManagerSolicitudes {

	@EJB
	private ManagerDAO mDAO;

	/**
	 * Default constructor.
	 */
	public ManagerSolicitudes() {
		// TODO Auto-generated constructor stub
	}

	public List<RegSolicitud> findAllSolicitudes() {
		return mDAO.findAll(RegSolicitud.class, "regId", true);
	}

	public List<RegSolicitud> findAllSolicitudesByUser(int userid) {
		return mDAO.findWhere(RegSolicitud.class, "o.segUsuario.idSegUsuario=" + userid, "o.regId");
	}
	
	public List<RegTipo> findTipoEstadoPendiente() {
		return mDAO.findWhere(RegTipo.class, "o.tipCodigo='PD'", "o.tipId");
	}

	public void insertarSolicitud(RegSolicitud nuevaSolicitud) throws Exception {
		mDAO.insertar(nuevaSolicitud);
	}

	// Actualizar Solicitud (cambiar de técnico asignado)
	public void actualizarTecnicoSolicitud(RegSolicitud edicionSolicitud) throws Exception {
		RegSolicitud solicitud = (RegSolicitud) mDAO.findById(RegSolicitud.class, edicionSolicitud.getRegId());
		Timestamp tiempo = new Timestamp(System.currentTimeMillis());
		solicitud.setFechaModi(tiempo);
		solicitud.setNombreTecnico(edicionSolicitud.getNombreTecnico());
		solicitud.setIdTecnico(edicionSolicitud.getIdTecnico());
		mDAO.actualizar(solicitud);
	}

	public List<RegSolicitud> findSolicitudesByUsuario(int idUsuario) {
		List<RegSolicitud> listado = mDAO.findWhere(RegSolicitud.class, "o.idUsuarioCrea=" + idUsuario, "o.regId");
		return listado;
	}

	public List<RegInsumo> findInsumosByCategoria(int idcategoria) {
		String consulta = "o.regCategoria.catId=" + idcategoria;
		List<RegInsumo> listacat_ins = mDAO.findWhere(RegInsumo.class, consulta, "o.insDescripcion");
		return listacat_ins;
	}
	
	public List<RegPath> findPathByCodigo(String codigo) {
		String consulta = "o.paCodigo='" + codigo+"'";
		List<RegPath> listapath = mDAO.findWhere(RegPath.class, consulta, "o.idPath");
		return listapath;
	}

	public SegUsuario findTecnicosByInsumo(int idInsumo) {
		List<RegUsuIn> lista_usuario_insumo = mDAO.findWhere(RegUsuIn.class, "o.regInsumo.insId=" + idInsumo,
				"o.prioridad.priIntencidad desc");
		SegUsuario tecnicoEncargado = new SegUsuario();
		int cont = 0;
		// Asigna al usuario con mayor prioridad
		for (int i = 0; i < lista_usuario_insumo.size(); i++) {
			for (int j = lista_usuario_insumo.get(i).getSegUsuario().getDetalles().size() - 1; j >= 0; j--) {
				if (lista_usuario_insumo.get(i).getSegUsuario().getDetalles().get(j).getRegTipo() != null &&
						lista_usuario_insumo.get(i).getSegUsuario().getDetalles().get(j).getRegTipo().getTipCodigo().equals("EC")) {
					cont = 1;
					break;
				}
			}
			if (cont == 0) {
				tecnicoEncargado = lista_usuario_insumo.get(i).getSegUsuario();
				return tecnicoEncargado;
			} else {
				cont = 0;
			}
			tecnicoEncargado = lista_usuario_insumo.get(0).getSegUsuario();
		}
		return tecnicoEncargado;
	}
	
	public List<RegCodSolicitud> findUltimoNumeroSolicitud(String anio) throws Exception{
		List<RegCodSolicitud> listado = (List<RegCodSolicitud>) mDAO.findWhere(RegCodSolicitud.class, "o.codAnio=" +"'"+ anio+"'", "o.codSolId");
		return listado;
	}
	
	public void insertarRegCodSolicitud(RegCodSolicitud nuevaRegCod) throws Exception {
		mDAO.insertar(nuevaRegCod);
	}
	
	// Actualizar CodigoSolicitud
	public void actualizarRegCodigoSolicitud(RegCodSolicitud edicionRegCod) throws Exception {
		RegCodSolicitud codsolicitud = (RegCodSolicitud) mDAO.findById(RegCodSolicitud.class, edicionRegCod.getCodSolId());
		codsolicitud.setCodUltNumero(edicionRegCod.getCodUltNumero());
		mDAO.actualizar(codsolicitud);
	}
	
	/**
	 * Cambier de estado a la solicitud a FINALIZADA
	 * @param edicionSolicitud Solicitud que se va a Finalizar
	 * @throws Exception Si no encuentra la solicitud
	 */
	public void cambiarEstadoSolicitud(RegSolicitud edicionSolicitud) throws Exception {
		RegSolicitud solicitud = (RegSolicitud) mDAO.findById(RegSolicitud.class, edicionSolicitud.getRegId());
		solicitud.setRegTipo(edicionSolicitud.getRegTipo());
		solicitud.setFechaModi(edicionSolicitud.getFechaModi());
		solicitud.setRegFechaFin(edicionSolicitud.getRegFechaFin());
		solicitud.setRegTiempofin(edicionSolicitud.getRegTiempofin());
		mDAO.actualizar(solicitud);
	}
	
	public List<RegTipo> findTipoEstadoFinalizada() {
		return mDAO.findWhere(RegTipo.class, "o.tipCodigo='FN'", "o.tipId");
	}
	
	public List<RegTipo> findAllTipoEstados() {
		return mDAO.findAll(RegTipo.class,"tipId");
	}
	
	public List<RegSolicitud> findSolicitudByCodigo(String codigo){
		List<RegSolicitud> listado = (List<RegSolicitud>) mDAO.findWhere(RegSolicitud.class, "o.regCodigo=" +"'"+ codigo+"'", "o.regId");
		return listado;
	}
	
    public List<SegUsuario> findUsuariosTecnicos(){
    	List<SegUsuario> tecnicos = mDAO.findWhere(SegUsuario.class, "o.segRol.rolCodigo ='TC'", "o.idSegUsuario");
    	return tecnicos;
    }
    
    public List<SegUsuario> findUsuariosNormales(){
    	List<SegUsuario> tecnicos = mDAO.findWhere(SegUsuario.class, "o.segRol.rolCodigo ='UN'", "o.idSegUsuario");
    	return tecnicos;
    }

}
