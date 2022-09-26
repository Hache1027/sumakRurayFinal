package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the reg_solicitud database table.
 * 
 */
@Entity
@Table(name="reg_solicitud")
@NamedQuery(name="RegSolicitud.findAll", query="SELECT r FROM RegSolicitud r")
public class RegSolicitud implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="reg_id", unique=true, nullable=false)
	private Integer regId;

	@Column(name="fecha_modi")
	private Timestamp fechaModi;

	@Column(name="id_dependencia")
	private Integer idDependencia;

	@Column(name="id_tecnico")
	private Integer idTecnico;

	@Column(name="nombre_tecnico", length=100)
	private String nombreTecnico;

	@Column(name="reg_adjunto", length=100)
	private String regAdjunto;

	@Column(name="reg_codigo", length=15)
	private String regCodigo;

	@Column(name="reg_dependencia", length=50)
	private String regDependencia;

	@Column(name="reg_descripcion", length=500)
	private String regDescripcion;

	@Column(name="reg_fecha_creacion")
	private Timestamp regFechaCreacion;

	@Column(name="reg_fecha_fin")
	private Timestamp regFechaFin;

	@Column(name="reg_tiempofin", length=20)
	private String regTiempofin;

	@Column(name="usuario_crea", length=100)
	private String usuarioCrea;

	//bi-directional many-to-one association to Detalle
	@OneToMany(mappedBy="regSolicitud")
	private List<Detalle> detalles;

	//bi-directional many-to-one association to RegInsumo
	@ManyToOne
	@JoinColumn(name="ins_id")
	private RegInsumo regInsumo;

	//bi-directional many-to-one association to RegTipo
	@ManyToOne
	@JoinColumn(name="tip_id")
	private RegTipo regTipo;

	//bi-directional many-to-one association to SegUsuario
	@ManyToOne
	@JoinColumn(name="id_seg_usuario")
	private SegUsuario segUsuario;

	public RegSolicitud() {
	}

	public Integer getRegId() {
		return this.regId;
	}

	public void setRegId(Integer regId) {
		this.regId = regId;
	}

	public Timestamp getFechaModi() {
		return this.fechaModi;
	}

	public void setFechaModi(Timestamp fechaModi) {
		this.fechaModi = fechaModi;
	}

	public Integer getIdDependencia() {
		return this.idDependencia;
	}

	public void setIdDependencia(Integer idDependencia) {
		this.idDependencia = idDependencia;
	}

	public Integer getIdTecnico() {
		return this.idTecnico;
	}

	public void setIdTecnico(Integer idTecnico) {
		this.idTecnico = idTecnico;
	}

	public String getNombreTecnico() {
		return this.nombreTecnico;
	}

	public void setNombreTecnico(String nombreTecnico) {
		this.nombreTecnico = nombreTecnico;
	}

	public String getRegAdjunto() {
		return this.regAdjunto;
	}

	public void setRegAdjunto(String regAdjunto) {
		this.regAdjunto = regAdjunto;
	}

	public String getRegCodigo() {
		return this.regCodigo;
	}

	public void setRegCodigo(String regCodigo) {
		this.regCodigo = regCodigo;
	}

	public String getRegDependencia() {
		return this.regDependencia;
	}

	public void setRegDependencia(String regDependencia) {
		this.regDependencia = regDependencia;
	}

	public String getRegDescripcion() {
		return this.regDescripcion;
	}

	public void setRegDescripcion(String regDescripcion) {
		this.regDescripcion = regDescripcion;
	}

	public Timestamp getRegFechaCreacion() {
		return this.regFechaCreacion;
	}

	public void setRegFechaCreacion(Timestamp regFechaCreacion) {
		this.regFechaCreacion = regFechaCreacion;
	}

	public Timestamp getRegFechaFin() {
		return this.regFechaFin;
	}

	public void setRegFechaFin(Timestamp regFechaFin) {
		this.regFechaFin = regFechaFin;
	}

	public String getRegTiempofin() {
		return this.regTiempofin;
	}

	public void setRegTiempofin(String regTiempofin) {
		this.regTiempofin = regTiempofin;
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
		detalle.setRegSolicitud(this);

		return detalle;
	}

	public Detalle removeDetalle(Detalle detalle) {
		getDetalles().remove(detalle);
		detalle.setRegSolicitud(null);

		return detalle;
	}

	public RegInsumo getRegInsumo() {
		return this.regInsumo;
	}

	public void setRegInsumo(RegInsumo regInsumo) {
		this.regInsumo = regInsumo;
	}

	public RegTipo getRegTipo() {
		return this.regTipo;
	}

	public void setRegTipo(RegTipo regTipo) {
		this.regTipo = regTipo;
	}

	public SegUsuario getSegUsuario() {
		return this.segUsuario;
	}

	public void setSegUsuario(SegUsuario segUsuario) {
		this.segUsuario = segUsuario;
	}

}