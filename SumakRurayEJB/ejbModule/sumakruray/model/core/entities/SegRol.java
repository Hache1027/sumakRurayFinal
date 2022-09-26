package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the seg_rol database table.
 * 
 */
@Entity
@Table(name="seg_rol")
@NamedQuery(name="SegRol.findAll", query="SELECT s FROM SegRol s")
public class SegRol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_seg_rol", unique=true, nullable=false)
	private Integer idSegRol;

	private Boolean estado;

	@Column(name="fecha_creacion")
	private Timestamp fechaCreacion;

	@Column(name="fecha_modi")
	private Timestamp fechaModi;

	@Column(name="rol_codigo", length=5)
	private String rolCodigo;

	@Column(name="rol_descripcion", length=50)
	private String rolDescripcion;

	@Column(name="usuario_crea", length=100)
	private String usuarioCrea;

	//bi-directional many-to-one association to SegRolesAcceso
	@OneToMany(mappedBy="segRol")
	private List<SegRolesAcceso> segRolesAccesos;

	//bi-directional many-to-one association to SegUsuario
	@OneToMany(mappedBy="segRol")
	private List<SegUsuario> segUsuarios;

	public SegRol() {
	}

	public Integer getIdSegRol() {
		return this.idSegRol;
	}

	public void setIdSegRol(Integer idSegRol) {
		this.idSegRol = idSegRol;
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

	public String getRolCodigo() {
		return this.rolCodigo;
	}

	public void setRolCodigo(String rolCodigo) {
		this.rolCodigo = rolCodigo;
	}

	public String getRolDescripcion() {
		return this.rolDescripcion;
	}

	public void setRolDescripcion(String rolDescripcion) {
		this.rolDescripcion = rolDescripcion;
	}

	public String getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(String usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public List<SegRolesAcceso> getSegRolesAccesos() {
		return this.segRolesAccesos;
	}

	public void setSegRolesAccesos(List<SegRolesAcceso> segRolesAccesos) {
		this.segRolesAccesos = segRolesAccesos;
	}

	public SegRolesAcceso addSegRolesAcceso(SegRolesAcceso segRolesAcceso) {
		getSegRolesAccesos().add(segRolesAcceso);
		segRolesAcceso.setSegRol(this);

		return segRolesAcceso;
	}

	public SegRolesAcceso removeSegRolesAcceso(SegRolesAcceso segRolesAcceso) {
		getSegRolesAccesos().remove(segRolesAcceso);
		segRolesAcceso.setSegRol(null);

		return segRolesAcceso;
	}

	public List<SegUsuario> getSegUsuarios() {
		return this.segUsuarios;
	}

	public void setSegUsuarios(List<SegUsuario> segUsuarios) {
		this.segUsuarios = segUsuarios;
	}

	public SegUsuario addSegUsuario(SegUsuario segUsuario) {
		getSegUsuarios().add(segUsuario);
		segUsuario.setSegRol(this);

		return segUsuario;
	}

	public SegUsuario removeSegUsuario(SegUsuario segUsuario) {
		getSegUsuarios().remove(segUsuario);
		segUsuario.setSegRol(null);

		return segUsuario;
	}

}