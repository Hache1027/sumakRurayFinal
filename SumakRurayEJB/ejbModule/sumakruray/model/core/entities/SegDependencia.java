package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the seg_dependencia database table.
 * 
 */
@Entity
@Table(name="seg_dependencia")
@NamedQuery(name="SegDependencia.findAll", query="SELECT s FROM SegDependencia s")
public class SegDependencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_seg_dependencia", unique=true, nullable=false)
	private Integer idSegDependencia;

	@Column(name="dep_codigo", length=5)
	private String depCodigo;

	@Column(name="dep_descripcion", nullable=false, length=100)
	private String depDescripcion;

	private Boolean estado;

	@Column(name="fecha_creacion")
	private Timestamp fechaCreacion;

	@Column(name="fecha_modi")
	private Timestamp fechaModi;

	@Column(name="usuario_crea", length=100)
	private String usuarioCrea;

	//bi-directional many-to-one association to Accesorio
	@OneToMany(mappedBy="segDependencia")
	private List<Accesorio> accesorios;

	//bi-directional many-to-one association to Detalle
	@OneToMany(mappedBy="segDependencia")
	private List<Detalle> detalles;

	//bi-directional many-to-one association to Equipo
	@OneToMany(mappedBy="segDependencia")
	private List<Equipo> equipos;

	//bi-directional many-to-one association to SegUsuario
	@OneToMany(mappedBy="segDependencia")
	private List<SegUsuario> segUsuarios;

	public SegDependencia() {
	}

	public Integer getIdSegDependencia() {
		return this.idSegDependencia;
	}

	public void setIdSegDependencia(Integer idSegDependencia) {
		this.idSegDependencia = idSegDependencia;
	}

	public String getDepCodigo() {
		return this.depCodigo;
	}

	public void setDepCodigo(String depCodigo) {
		this.depCodigo = depCodigo;
	}

	public String getDepDescripcion() {
		return this.depDescripcion;
	}

	public void setDepDescripcion(String depDescripcion) {
		this.depDescripcion = depDescripcion;
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

	public List<Accesorio> getAccesorios() {
		return this.accesorios;
	}

	public void setAccesorios(List<Accesorio> accesorios) {
		this.accesorios = accesorios;
	}

	public Accesorio addAccesorio(Accesorio accesorio) {
		getAccesorios().add(accesorio);
		accesorio.setSegDependencia(this);

		return accesorio;
	}

	public Accesorio removeAccesorio(Accesorio accesorio) {
		getAccesorios().remove(accesorio);
		accesorio.setSegDependencia(null);

		return accesorio;
	}

	public List<Detalle> getDetalles() {
		return this.detalles;
	}

	public void setDetalles(List<Detalle> detalles) {
		this.detalles = detalles;
	}

	public Detalle addDetalle(Detalle detalle) {
		getDetalles().add(detalle);
		detalle.setSegDependencia(this);

		return detalle;
	}

	public Detalle removeDetalle(Detalle detalle) {
		getDetalles().remove(detalle);
		detalle.setSegDependencia(null);

		return detalle;
	}

	public List<Equipo> getEquipos() {
		return this.equipos;
	}

	public void setEquipos(List<Equipo> equipos) {
		this.equipos = equipos;
	}

	public Equipo addEquipo(Equipo equipo) {
		getEquipos().add(equipo);
		equipo.setSegDependencia(this);

		return equipo;
	}

	public Equipo removeEquipo(Equipo equipo) {
		getEquipos().remove(equipo);
		equipo.setSegDependencia(null);

		return equipo;
	}

	public List<SegUsuario> getSegUsuarios() {
		return this.segUsuarios;
	}

	public void setSegUsuarios(List<SegUsuario> segUsuarios) {
		this.segUsuarios = segUsuarios;
	}

	public SegUsuario addSegUsuario(SegUsuario segUsuario) {
		getSegUsuarios().add(segUsuario);
		segUsuario.setSegDependencia(this);

		return segUsuario;
	}

	public SegUsuario removeSegUsuario(SegUsuario segUsuario) {
		getSegUsuarios().remove(segUsuario);
		segUsuario.setSegDependencia(null);

		return segUsuario;
	}

}