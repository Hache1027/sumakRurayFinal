package sumakruray.model.actividades.managers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

import sumakruray.model.core.entities.Detalle;
import sumakruray.model.core.entities.RegActividad;
import sumakruray.model.core.entities.RegInsumo;
import sumakruray.model.core.entities.RegSolicitud;
import sumakruray.model.core.entities.RegTipo;
import sumakruray.model.core.entities.RegUsuIn;
import sumakruray.model.core.entities.SegDependencia;
import sumakruray.model.core.entities.SegRol;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.core.managers.ManagerDAO;

/**
 * Session Bean implementation class ManagerActividades
 */
@Stateless
@LocalBean
public class ManagerDetalleSolicitudes {

	@EJB
	private ManagerDAO mDAO;

	/**
	 * Default constructor.
	 */
	public ManagerDetalleSolicitudes() {
		// TODO Auto-generated constructor stub
	}

	public List<Detalle> findAllDetalles() {
		return mDAO.findAll(Detalle.class, "detId", true);
	}

	public List<RegTipo> findAllTipos() {
		return mDAO.findAll(RegTipo.class, "tipId", true);
	}

	public List<Detalle> findDetallesByTecnico(int tecid) {
		return mDAO.findWhere(Detalle.class, "o.segUsuario.idSegUsuario=" + tecid, "o.detId");
	}

	public List<RegSolicitud> findSolicitudesByTecnico(int tecid) {
		return mDAO.findWhere(RegSolicitud.class, "o.idTecnico=" + tecid, "o.regId");
	}

	public void insertarDetalle(Detalle nuevoDetalle) throws Exception {
		mDAO.insertar(nuevoDetalle);
	}

	public List<Detalle> findDetallesBySolicitud(int idSolicitud) {
		List<Detalle> listado = mDAO.findWhere(Detalle.class, "o.regSolicitud.regId=" + idSolicitud, "o.detId");
		return listado;
	}

	public RegTipo findByIdTipo(int idTipo) throws Exception {
		return (RegTipo) mDAO.findById(RegTipo.class, idTipo);
	}

	public SegDependencia findByIdDependencia(int idDependencia) throws Exception {
		return (SegDependencia) mDAO.findById(SegDependencia.class, idDependencia);
	}

	// Actualizar Solicitud (cambiar de técnico asignado)
	public void actualizarDetalleActividad(Detalle edicionDetalle) throws Exception {
		Detalle detalle = (Detalle) mDAO.findById(Detalle.class, edicionDetalle.getDetId());
		detalle.setFechaModi(edicionDetalle.getFechaModi());
		detalle.setDetObservacion(edicionDetalle.getDetObservacion());
		detalle.setDetMotTraspaso(edicionDetalle.getDetMotTraspaso());
		detalle.setRegTipo(edicionDetalle.getRegTipo());
		detalle.setDetTiempo(edicionDetalle.getDetTiempo());
		detalle.setRegActividad(edicionDetalle.getRegActividad());
		detalle.setSegDependencia(edicionDetalle.getSegDependencia());
		detalle.setRegInsumo(edicionDetalle.getRegInsumo());
		detalle.setDetFechaIni(edicionDetalle.getDetFechaIni());
		detalle.setDetFechaFin(edicionDetalle.getDetFechaFin());
		mDAO.actualizar(detalle);
	}

	// Cambia el estado de mostrar Detalle
	public int actualizarDetallesMostrar(int idsolicitud) {
		Timestamp tiempo = new Timestamp(System.currentTimeMillis());
		String consultaCom;
		consultaCom = "UPDATE Detalle o SET o.estadoMostrar = 'false', o.fechaModi = :fechaModi "
				+ " WHERE o.regSolicitud.regId = :idsolicitud";
		Query q = mDAO.getEntityManager().createQuery(consultaCom);
		q.setParameter("fechaModi", tiempo);
		q.setParameter("idsolicitud", idsolicitud);
		return q.executeUpdate();
	}

}
