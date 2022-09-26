package sumakruray.model.actividades.managers;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sumakruray.model.core.entities.Prioridad;
import sumakruray.model.core.entities.RegActividad;
import sumakruray.model.core.entities.RegInsAct;
import sumakruray.model.core.entities.RegInsumo;
import sumakruray.model.core.entities.RegUsuIn;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.core.managers.ManagerDAO;

/**
 * Session Bean implementation class ManagerActividades
 */
@Stateless
@LocalBean
public class ManagerInsumos {

	@EJB
	private ManagerDAO mDAO;
	
    /**
     * Default constructor. 
     */
    public ManagerInsumos() {

    }
    
    public List<RegInsumo> findAllInsumos(){
    	return mDAO.findAll(RegInsumo.class,"insId",true);
    }
    
    public List<Prioridad> findAllPrioridades(){
    	return mDAO.findAll(Prioridad.class,"priIntencidad",false);
    }
    
    public void insertarInsumo(RegInsumo nuevoInsumo) throws Exception {
    	mDAO.insertar(nuevoInsumo);
    }
    
    //Actualizar Insumos
    public void actualizarInsumo(RegInsumo edicionInsumo) throws Exception {
    	RegInsumo insumo=(RegInsumo) mDAO.findById(RegInsumo.class, edicionInsumo.getInsId());
		Timestamp tiempo=new Timestamp(System.currentTimeMillis());
		insumo.setFechaModi(tiempo);
		insumo.setInsNombre(edicionInsumo.getInsNombre());
		insumo.setInsDescripcion(edicionInsumo.getInsDescripcion());
		insumo.setInsEstado(edicionInsumo.getInsEstado());
    	mDAO.actualizar(insumo);
    }
    public RegInsumo findByIdInsumo(int idInsumo) throws Exception {
    	return (RegInsumo) mDAO.findById(RegInsumo.class, idInsumo);
    }
    
    @SuppressWarnings("unchecked")
	public void asignarActividadToInsumo(int idRegInsumo,int idRegActividad) throws Exception{
    	//Buscar los objetos primarios:
    	RegInsumo insumo=(RegInsumo)mDAO.findById(RegInsumo.class, idRegInsumo);
    	RegActividad actividad=(RegActividad)mDAO.findById(RegActividad.class, idRegActividad);
    	//verificar si ya existe:
    	String consulta="o.regActividad.actId="+idRegActividad+" and o.regInsumo.insId="+idRegInsumo;
    	List<RegInsAct> ins_actividades=mDAO.findWhere(RegInsAct.class, consulta, null);
    	if(ins_actividades==null || ins_actividades.size()==0) {
	    	//crear la relacion:
	    	RegInsAct ins_actividad=new RegInsAct();
	    	ins_actividad.setRegActividad(actividad);
	    	ins_actividad.setRegInsumo(insumo);
	    	//guardar la relación:
	    	mDAO.insertar(ins_actividad);
    	}else {
    		throw new Exception("Ya existe la asignación de insumo/actividad");
    	}
    }
    
    
    public void eliminarInsAct(int idInsAct) throws Exception {
    	mDAO.eliminar(RegInsAct.class, idInsAct);
    }
    
    
    @SuppressWarnings("unchecked")
	public void asignarInsumo(int idTecnico,int idInsumo, int idprioridad) throws Exception{
    	//Buscar los objetos primarios:
    	SegUsuario tecnico=(SegUsuario)mDAO.findById(SegUsuario.class, idTecnico);
    	RegInsumo insumo=(RegInsumo)mDAO.findById(RegInsumo.class, idInsumo);
    	Prioridad prioridad = (Prioridad)mDAO.findById(Prioridad.class, idprioridad);
    	//verificar si ya existe:
    	String consulta="o.regInsumo.insId="+idInsumo+" and o.segUsuario.idSegUsuario="+idTecnico;
    	List<RegUsuIn> asignaciones=mDAO.findWhere(RegUsuIn.class, consulta, null);
    	if(asignaciones==null || asignaciones.size()==0) {
	    	//crear la relacion:
	    	RegUsuIn asignacion=new RegUsuIn();
	    	asignacion.setRegInsumo(insumo);
	    	asignacion.setSegUsuario(tecnico);
	    	asignacion.setPrioridad(prioridad);
	    	//guardar la asignacion:
	    	mDAO.insertar(asignacion);
    	}else {
    		throw new Exception("Ya existe la asignación de USUARIO/INSUMO");
    	}
    }
    
    public List<RegUsuIn> findInsumosByUsuario(int idSegUsuario){
    	String consulta="o.segUsuario.idSegUsuario="+idSegUsuario;
		List<RegUsuIn> listaAsignaciones=mDAO.findWhere(RegUsuIn.class, consulta, "o.prioridad.priIntencidad desc");
		return listaAsignaciones;
    }
    
    public List<RegInsAct> findActividadesByInsumo(int idRegInsumo){
    	String consulta="o.regInsumo.insId="+idRegInsumo;
		List<RegInsAct> listains_act=mDAO.findWhere(RegInsAct.class, consulta, "o.regActividad.actDescripcion");
		return listains_act;
    }
    
    public void eliminarAsignacionInsumo(int idInsumoTecnico) throws Exception {
    	mDAO.eliminar(RegUsuIn.class, idInsumoTecnico);
    }

    public List<SegUsuario> findUsuariosTecnicos(){
    	List<SegUsuario> tecnicos = mDAO.findWhere(SegUsuario.class, "o.segRol.rolCodigo ='TC'", "o.idSegUsuario");
    	return tecnicos;
    }
}
