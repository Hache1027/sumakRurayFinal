package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the seg_perfil database table.
 * 
 */
@Entity
@Table(name="seg_perfil")
@NamedQuery(name="SegPerfil.findAll", query="SELECT s FROM SegPerfil s")
public class SegPerfil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_seg_perfil", unique=true, nullable=false)
	private Integer idSegPerfil;

	@Column(nullable=false)
	private Boolean estado;

	@Column(name="fecha_creacion")
	private Timestamp fechaCreacion;

	@Column(name="fecha_modi")
	private Timestamp fechaModi;

	@Column(name="nombre_perfil", nullable=false, length=50)
	private String nombrePerfil;

	@Column(name="ruta_acceso", nullable=false, length=50)
	private String rutaAcceso;

	@Column(name="usuario_crea", length=100)
	private String usuarioCrea;

	//bi-directional many-to-one association to SegModulo
	@ManyToOne
	@JoinColumn(name="id_seg_modulo")
	private SegModulo segModulo;

	//bi-directional many-to-one association to SegRolesAcceso
	@OneToMany(mappedBy="segPerfil")
	private List<SegRolesAcceso> segRolesAccesos;

	public SegPerfil() {
	}

	public Integer getIdSegPerfil() {
		return this.idSegPerfil;
	}

	public void setIdSegPerfil(Integer idSegPerfil) {
		this.idSegPerfil = idSegPerfil;
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

	public String getNombrePerfil() {
		return this.nombrePerfil;
	}

	public void setNombrePerfil(String nombrePerfil) {
		this.nombrePerfil = nombrePerfil;
	}

	public String getRutaAcceso() {
		return this.rutaAcceso;
	}

	public void setRutaAcceso(String rutaAcceso) {
		this.rutaAcceso = rutaAcceso;
	}

	public String getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(String usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public SegModulo getSegModulo() {
		return this.segModulo;
	}

	public void setSegModulo(SegModulo segModulo) {
		this.segModulo = segModulo;
	}

	public List<SegRolesAcceso> getSegRolesAccesos() {
		return this.segRolesAccesos;
	}

	public void setSegRolesAccesos(List<SegRolesAcceso> segRolesAccesos) {
		this.segRolesAccesos = segRolesAccesos;
	}

	public SegRolesAcceso addSegRolesAcceso(SegRolesAcceso segRolesAcceso) {
		getSegRolesAccesos().add(segRolesAcceso);
		segRolesAcceso.setSegPerfil(this);

		return segRolesAcceso;
	}

	public SegRolesAcceso removeSegRolesAcceso(SegRolesAcceso segRolesAcceso) {
		getSegRolesAccesos().remove(segRolesAcceso);
		segRolesAcceso.setSegPerfil(null);

		return segRolesAcceso;
	}

}