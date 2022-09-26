package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the reg_actividad database table.
 * 
 */
@Entity
@Table(name="reg_actividad")
@NamedQuery(name="RegActividad.findAll", query="SELECT r FROM RegActividad r")
public class RegActividad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="act_id", unique=true, nullable=false)
	private Integer actId;

	@Column(name="act_descripcion", length=500)
	private String actDescripcion;

	@Column(name="act_estato")
	private Boolean actEstato;

	@Column(name="fecha_creacion")
	private Timestamp fechaCreacion;

	@Column(name="fecha_modi")
	private Timestamp fechaModi;

	@Column(name="usuario_crea", length=100)
	private String usuarioCrea;

	//bi-directional many-to-one association to Detalle
	@OneToMany(mappedBy="regActividad")
	private List<Detalle> detalles;

	//bi-directional many-to-one association to RegInsAct
	@OneToMany(mappedBy="regActividad")
	private List<RegInsAct> regInsActs;

	public RegActividad() {
	}

	public Integer getActId() {
		return this.actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	public String getActDescripcion() {
		return this.actDescripcion;
	}

	public void setActDescripcion(String actDescripcion) {
		this.actDescripcion = actDescripcion;
	}

	public Boolean getActEstato() {
		return this.actEstato;
	}

	public void setActEstato(Boolean actEstato) {
		this.actEstato = actEstato;
	}

	public Timestamp getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Timestamp getFechaModi() {
		return this.fechaModi;
	}

	public void setFechaModi(Timestamp fechaModi) {
		this.fechaModi = fechaModi;
	}

	public String getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(String usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public List<Detalle> getDetalles() {
		return this.detalles;
	}

	public void setDetalles(List<Detalle> detalles) {
		this.detalles = detalles;
	}

	public Detalle addDetalle(Detalle detalle) {
		getDetalles().add(detalle);
		detalle.setRegActividad(this);

		return detalle;
	}

	public Detalle removeDetalle(Detalle detalle) {
		getDetalles().remove(detalle);
		detalle.setRegActividad(null);

		return detalle;
	}

	public List<RegInsAct> getRegInsActs() {
		return this.regInsActs;
	}

	public void setRegInsActs(List<RegInsAct> regInsActs) {
		this.regInsActs = regInsActs;
	}

	public RegInsAct addRegInsAct(RegInsAct regInsAct) {
		getRegInsActs().add(regInsAct);
		regInsAct.setRegActividad(this);

		return regInsAct;
	}

	public RegInsAct removeRegInsAct(RegInsAct regInsAct) {
		getRegInsActs().remove(regInsAct);
		regInsAct.setRegActividad(null);

		return regInsAct;
	}

}