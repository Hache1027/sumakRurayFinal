package sumakruray.model.actividades.managers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

import sumakruray.model.core.entities.Detalle;
import sumakruray.model.core.entities.RegCodSolicitud;
import sumakruray.model.core.entities.RegInsumo;
import sumakruray.model.core.entities.RegSolicitud;
import sumakruray.model.core.entities.RegTipo;
import sumakruray.model.core.entities.RegUsuIn;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.core.managers.ManagerDAO;

/**
 * Manager para consultas a las entidades para generar reportes
 * @author rfern
 *
 */

@Stateless
@LocalBean
public class ManagerReportes {

	@EJB
	private ManagerDAO mDAO;

	/**
	 * Default constructor.
	 */
	public ManagerReportes() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Método para buscar todas las Solicitudes
	 * @return Lista de todas las Solicitudes
	 */
	public List<RegSolicitud> findAllSolicitudes() {
		return mDAO.findAll(RegSolicitud.class, "regId", true);
	}

	/**
	 * Método para buscar todos los Detalles
	 * @return Lista de todos los Detalles
	 */
	public List<Detalle> findAllDetalles() {
		return mDAO.findAll(Detalle.class, "detId", true);
	}

	/**
	 * Hace la consulta select a la tabla Detalle(Actividades realizadas por los técnicos)
	 * @param consulta Los filtros que se hayan ingresado
	 * @param fechaInicio: filtro de fecha de inicio
	 * @param fechaFin: filtro de fecha de fin
	 * @return Lista de Detalles que hayan pasado el filtro
	 */
	public List<Detalle> findWithConsultaActividades(String consulta, Date fechaInicio,Date fechaFin){
    	SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	String consultaCom;
    	if(consulta.equals("")) {
    		consultaCom = "SELECT o FROM Detalle o WHERE "+consulta +" o.detFechaIni between :fechaInicio and :fechaFin order by detId";
    	}else {
    		consultaCom = "SELECT o FROM Detalle o WHERE "+consulta +" AND o.detFechaIni between :fechaInicio and :fechaFin order by detId";
    	}
		Query q=mDAO.getEntityManager().createQuery(consultaCom, Detalle.class);
		q.setParameter("fechaInicio", new Timestamp(fechaInicio.getTime()));
    	q.setParameter("fechaFin", new Timestamp(fechaFin.getTime()));
		return q.getResultList();
	}
	
	/**
	 * Hace la consulta select a la tabla de solocitudes a la base de datos
	 * @param consulta: los filtros que se hayan ingresado
	 * @param fechaInicio: filtro de fecha de inicio
	 * @param fechaFin: filtro de fecha de fin
	 * @return Lista de Solicitudes que hayan pasado el filtro
	 */
	public List<RegSolicitud> findWithConsultaSolicitudes(String consulta, Date fechaInicio,Date fechaFin){
    	SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	String consultaCom;
    	if(consulta.equals("")) {
    		consultaCom = "SELECT o FROM RegSolicitud o WHERE "+consulta +" o.regFechaCreacion between :fechaInicio and :fechaFin order by regId";
    	}else {
    		consultaCom = "SELECT o FROM RegSolicitud o WHERE "+consulta +" AND o.regFechaCreacion between :fechaInicio and :fechaFin order by regId";
		}
		Query q=mDAO.getEntityManager().createQuery(consultaCom, RegSolicitud.class);
		q.setParameter("fechaInicio", new Timestamp(fechaInicio.getTime()));
    	q.setParameter("fechaFin", new Timestamp(fechaFin.getTime()));
		return q.getResultList();
	}

}
