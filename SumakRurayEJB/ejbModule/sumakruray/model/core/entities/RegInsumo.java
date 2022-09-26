package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the reg_insumo database table.
 * 
 */
@Entity
@Table(name="reg_insumo")
@NamedQuery(name="RegInsumo.findAll", query="SELECT r FROM RegInsumo r")
public class RegInsumo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ins_id", unique=true, nullable=false)
	private Integer insId;

	@Column(name="fecha_creacion")
	private Timestamp fechaCreacion;

	@Column(name="fecha_modi")
	private Timestamp fechaModi;

	@Column(name="ins_descripcion", length=500)
	private String insDescripcion;

	@Column(name="ins_estado")
	private Boolean insEstado;

	@Column(name="ins_nombre", length=100)
	private String insNombre;

	@Column(name="usuario_crea", length=100)
	private String usuarioCrea;

	//bi-directional many-to-one association to Detalle
	@OneToMany(mappedBy="regInsumo")
	private List<Detalle> detalles;

	//bi-directional many-to-one association to RegInsAct
	@OneToMany(mappedBy="regInsumo")
	private List<RegInsAct> regInsActs;

	//bi-directional many-to-one association to RegCategoria
	@ManyToOne
	@JoinColumn(name="cat_id")
	private RegCategoria regCategoria;

	//bi-directional many-to-one association to RegSolicitud
	@OneToMany(mappedBy="regInsumo")
	private List<RegSolicitud> regSolicituds;

	//bi-directional many-to-one association to RegUsuIn
	@OneToMany(mappedBy="regInsumo")
	private List<RegUsuIn> regUsuIns;

	public RegInsumo() {
	}

	public Integer getInsId() {
		return this.insId;
	}

	public void setInsId(Integer insId) {
		this.insId = insId;
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

	public String getInsDescripcion() {
		return this.insDescripcion;
	}

	public void setInsDescripcion(String insDescripcion) {
		this.insDescripcion = insDescripcion;
	}

	public Boolean getInsEstado() {
		return this.insEstado;
	}

	public void setInsEstado(Boolean insEstado) {
		this.insEstado = insEstado;
	}

	public String getInsNombre() {
		return this.insNombre;
	}

	public void setInsNombre(String insNombre) {
		this.insNombre = insNombre;
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
		detalle.setRegInsumo(this);

		return detalle;
	}

	public Detalle removeDetalle(Detalle detalle) {
		getDetalles().remove(detalle);
		detalle.setRegInsumo(null);

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
		regInsAct.setRegInsumo(this);

		return regInsAct;
	}

	public RegInsAct removeRegInsAct(RegInsAct regInsAct) {
		getRegInsActs().remove(regInsAct);
		regInsAct.setRegInsumo(null);

		return regInsAct;
	}

	public RegCategoria getRegCategoria() {
		return this.regCategoria;
	}

	public void setRegCategoria(RegCategoria regCategoria) {
		this.regCategoria = regCategoria;
	}

	public List<RegSolicitud> getRegSolicituds() {
		return this.regSolicituds;
	}

	public void setRegSolicituds(List<RegSolicitud> regSolicituds) {
		this.regSolicituds = regSolicituds;
	}

	public RegSolicitud addRegSolicitud(RegSolicitud regSolicitud) {
		getRegSolicituds().add(regSolicitud);
		regSolicitud.setRegInsumo(this);

		return regSolicitud;
	}

	public RegSolicitud removeRegSolicitud(RegSolicitud regSolicitud) {
		getRegSolicituds().remove(regSolicitud);
		regSolicitud.setRegInsumo(null);

		return regSolicitud;
	}

	public List<RegUsuIn> getRegUsuIns() {
		return this.regUsuIns;
	}

	public void setRegUsuIns(List<RegUsuIn> regUsuIns) {
		this.regUsuIns = regUsuIns;
	}

	public RegUsuIn addRegUsuIn(RegUsuIn regUsuIn) {
		getRegUsuIns().add(regUsuIn);
		regUsuIn.setRegInsumo(this);

		return regUsuIn;
	}

	public RegUsuIn removeRegUsuIn(RegUsuIn regUsuIn) {
		getRegUsuIns().remove(regUsuIn);
		regUsuIn.setRegInsumo(null);

		return regUsuIn;
	}

}