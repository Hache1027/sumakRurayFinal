package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the seg_roles_accesos database table.
 * 
 */
@Entity
@Table(name="seg_roles_accesos")
@NamedQuery(name="SegRolesAcceso.findAll", query="SELECT s FROM SegRolesAcceso s")
public class SegRolesAcceso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_acceso", unique=true, nullable=false)
	private Integer idAcceso;

	@Column(name="fecha_creacion")
	private Timestamp fechaCreacion;

	@Column(name="fecha_modi")
	private Timestamp fechaModi;

	@Column(name="usuario_crea", length=100)
	private String usuarioCrea;

	//bi-directional many-to-one association to SegPerfil
	@ManyToOne
	@JoinColumn(name="id_seg_perfil")
	private SegPerfil segPerfil;

	//bi-directional many-to-one association to SegRol
	@ManyToOne
	@JoinColumn(name="id_seg_rol")
	private SegRol segRol;

	public SegRolesAcceso() {
	}

	public Integer getIdAcceso() {
		return this.idAcceso;
	}

	public void setIdAcceso(Integer idAcceso) {
		this.idAcceso = idAcceso;
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

	public SegPerfil getSegPerfil() {
		return this.segPerfil;
	}

	public void setSegPerfil(SegPerfil segPerfil) {
		this.segPerfil = segPerfil;
	}

	public SegRol getSegRol() {
		return this.segRol;
	}

	public void setSegRol(SegRol segRol) {
		this.segRol = segRol;
	}

}