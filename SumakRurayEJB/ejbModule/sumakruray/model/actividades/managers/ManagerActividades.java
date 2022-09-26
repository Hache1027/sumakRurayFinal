package sumakruray.model.actividades.managers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

import sumakruray.model.core.entities.RegActividad;
import sumakruray.model.core.entities.RegTipo;
import sumakruray.model.core.entities.SegRol;
import sumakruray.model.core.managers.ManagerDAO;
import sumakruray.model.seguridades.dtos.LoginDTO;

/**
 * @author rfern
 *
 */
@Stateless
@LocalBean
public class ManagerActividades {

	@EJB
	private ManagerDAO mDAO;

    /**
     * Default constructor. 
     */
    public ManagerActividades() {
        // TODO Auto-generated constructor stub
    }
    
    public List<RegActividad> findAllActividades(){
    	return mDAO.findAll(RegActividad.class,"actId",true);
    }
    
	public RegActividad findActividadById(int id) throws Exception{
		return (RegActividad) mDAO.findById(RegActividad.class,id);
	}
    
    public void insertarActividad(RegActividad nuevaActividad) throws Exception {
    	mDAO.insertar(nuevaActividad);
    }
    
    public List<RegActividad> findActividadByFecha(Date fechaInicio,Date fechaFin){
    	SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	System.out.println("fecha inicio: "+format.format(fechaInicio));
    	System.out.println("fecha fin: "+format.format(fechaFin));
    	String consulta="select b from RegActividad b where b.fechaCreacion between :fechaInicio and :fechaFin order by b.fechaCreacion";
    	Query q=mDAO.getEntityManager().createQuery(consulta, RegActividad.class);
    	q.setParameter("fechaInicio", new Timestamp(fechaInicio.getTime()));
    	q.setParameter("fechaFin", new Timestamp(fechaFin.getTime()));
    	return q.getResultList();
    	
    }
    
    
    //Actualizar Actividad en la base de datos
    public void actualizarActividad(RegActividad edicionActividad) throws Exception {
    	RegActividad actividad=(RegActividad) mDAO.findById(RegActividad.class, edicionActividad.getActId());
		Timestamp tiempo=new Timestamp(System.currentTimeMillis());
		actividad.setFechaModi(tiempo);
    	actividad.setActDescripcion(edicionActividad.getActDescripcion());
    	actividad.setActEstato(edicionActividad.getActEstato());
    	mDAO.actualizar(actividad);
    }

}
