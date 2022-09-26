package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the seg_usuario database table.
 * 
 */
@Entity
@Table(name="seg_usuario")
@NamedQuery(name="SegUsuario.findAll", query="SELECT s FROM SegUsuario s")
public class SegUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_seg_usuario", unique=true, nullable=false)
	private Integer idSegUsuario;

	@Column(length=50)
	private String apellidos;

	@Column(length=15)
	private String cedula;

	@Column(length=100)
	private String contrasenia;

	@Column(length=100)
	private String correo;

	@Column(length=100)
	private String direccion;

	private Boolean estado;

	@Column(name="fecha_creacion")
	private Timestamp fechaCreacion;

	@Column(name="fecha_ultima_modificacion")
	private Timestamp fechaUltimaModificacion;

	@Column(length=50)
	private String nombres;

	@Column(length=20)
	private String telefono;

	@Column(name="usuario_crea", length=100)
	private String usuarioCrea;

	//bi-directional many-to-one association to Detalle
	@OneToMany(mappedBy="segUsuario")
	private List<Detalle> detalles;

	//bi-directional many-to-one association to RegSolicitud
	@OneToMany(mappedBy="segUsuario")
	private List<RegSolicitud> regSolicituds;

	//bi-directional many-to-one association to RegUsuIn
	@OneToMany(mappedBy="segUsuario")
	private List<RegUsuIn> regUsuIns;

	//bi-directional many-to-one association to SegContrasenia
	@OneToMany(mappedBy="segUsuario")
	private List<SegContrasenia> segContrasenias;

	//bi-directional many-to-one association to SegDependencia
	@ManyToOne
	@JoinColumn(name="id_seg_dependencia")
	private SegDependencia segDependencia;

	//bi-directional many-to-one association to SegRol
	@ManyToOne
	@JoinColumn(name="id_seg_rol", nullable=false)
	private SegRol segRol;

	public SegUsuario() {
	}

	public Integer getIdSegUsuario() {
		return this.idSegUsuario;
	}

	public void setIdSegUsuario(Integer idSegUsuario) {
		this.idSegUsuario = idSegUsuario;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getContrasenia() {
		return this.contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Boolean getEstado() {
		return this.estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Timestamp getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Timestamp getFechaUltimaModificacion() {
		return this.fechaUltimaModificacion;
	}

	public void setFechaUltimaModificacion(Timestamp fechaUltimaModificacion) {
		this.fechaUltimaModificacion = fechaUltimaModificacion;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
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
		detalle.setSegUsuario(this);

		return detalle;
	}

	public Detalle removeDetalle(Detalle detalle) {
		getDetalles().remove(detalle);
		detalle.setSegUsuario(null);

		return detalle;
	}

	public List<RegSolicitud> getRegSolicituds() {
		return this.regSolicituds;
	}

	public void setRegSolicituds(List<RegSolicitud> regSolicituds) {
		this.regSolicituds = regSolicituds;
	}

	public RegSolicitud addRegSolicitud(RegSolicitud regSolicitud) {
		getRegSolicituds().add(regSolicitud);
		regSolicitud.setSegUsuario(this);

		return regSolicitud;
	}

	public RegSolicitud removeRegSolicitud(RegSolicitud regSolicitud) {
		getRegSolicituds().remove(regSolicitud);
		regSolicitud.setSegUsuario(null);

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
		regUsuIn.setSegUsuario(this);

		return regUsuIn;
	}

	public RegUsuIn removeRegUsuIn(RegUsuIn regUsuIn) {
		getRegUsuIns().remove(regUsuIn);
		regUsuIn.setSegUsuario(null);

		return regUsuIn;
	}

	public List<SegContrasenia> getSegContrasenias() {
		return this.segContrasenias;
	}

	public void setSegContrasenias(List<SegContrasenia> segContrasenias) {
		this.segContrasenias = segContrasenias;
	}

	public SegContrasenia addSegContrasenia(SegContrasenia segContrasenia) {
		getSegContrasenias().add(segContrasenia);
		segContrasenia.setSegUsuario(this);

		return segContrasenia;
	}

	public SegContrasenia removeSegContrasenia(SegContrasenia segContrasenia) {
		getSegContrasenias().remove(segContrasenia);
		segContrasenia.setSegUsuario(null);

		return segContrasenia;
	}

	public SegDependencia getSegDependencia() {
		return this.segDependencia;
	}

	public void setSegDependencia(SegDependencia segDependencia) {
		this.segDependencia = segDependencia;
	}

	public SegRol getSegRol() {
		return this.segRol;
	}

	public void setSegRol(SegRol segRol) {
		this.segRol = segRol;
	}

}