package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the detalle database table.
 * 
 */
@Entity
@Table(name="detalle")
@NamedQuery(name="Detalle.findAll", query="SELECT d FROM Detalle d")
public class Detalle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="det_id", unique=true, nullable=false)
	private Integer detId;

	@Column(name="det_fecha_fin")
	private Timestamp detFechaFin;

	@Column(name="det_fecha_ini")
	private Timestamp detFechaIni;

	@Column(name="det_mot_traspaso", length=100)
	private String detMotTraspaso;

	@Column(name="det_observacion", length=500)
	private String detObservacion;

	@Column(name="det_tiempo", length=20)
	private String detTiempo;

	@Column(name="det_usu_anterior", length=100)
	private String detUsuAnterior;

	@Column(name="estado_mostrar")
	private Boolean estadoMostrar;

	@Column(name="fecha_modi")
	private Timestamp fechaModi;

	@Column(name="usuario_crea", length=100)
	private String usuarioCrea;

	//bi-directional many-to-one association to RegActividad
	@ManyToOne
	@JoinColumn(name="act_id")
	private RegActividad regActividad;

	//bi-directional many-to-one association to RegInsumo
	@ManyToOne
	@JoinColumn(name="ins_id")
	private RegInsumo regInsumo;

	//bi-directional many-to-one association to RegSolicitud
	@ManyToOne
	@JoinColumn(name="reg_id")
	private RegSolicitud regSolicitud;

	//bi-directional many-to-one association to RegTipo
	@ManyToOne
	@JoinColumn(name="tip_id")
	private RegTipo regTipo;

	//bi-directional many-to-one association to SegDependencia
	@ManyToOne
	@JoinColumn(name="id_seg_dependencia")
	private SegDependencia segDependencia;

	//bi-directional many-to-one association to SegUsuario
	@ManyToOne
	@JoinColumn(name="seg_id_seg_usuario")
	private SegUsuario segUsuario;

	public Detalle() {
	}

	public Integer getDetId() {
		return this.detId;
	}

	public void setDetId(Integer detId) {
		this.detId = detId;
	}

	public Timestamp getDetFechaFin() {
		return this.detFechaFin;
	}

	public void setDetFechaFin(Timestamp detFechaFin) {
		this.detFechaFin = detFechaFin;
	}

	public Timestamp getDetFechaIni() {
		return this.detFechaIni;
	}

	public void setDetFechaIni(Timestamp detFechaIni) {
		this.detFechaIni = detFechaIni;
	}

	public String getDetMotTraspaso() {
		return this.detMotTraspaso;
	}

	public void setDetMotTraspaso(String detMotTraspaso) {
		this.detMotTraspaso = detMotTraspaso;
	}

	public String getDetObservacion() {
		return this.detObservacion;
	}

	public void setDetObservacion(String detObservacion) {
		this.detObservacion = detObservacion;
	}

	public String getDetTiempo() {
		return this.detTiempo;
	}

	public void setDetTiempo(String detTiempo) {
		this.detTiempo = detTiempo;
	}

	public String getDetUsuAnterior() {
		return this.detUsuAnterior;
	}

	public void setDetUsuAnterior(String detUsuAnterior) {
		this.detUsuAnterior = detUsuAnterior;
	}

	public Boolean getEstadoMostrar() {
		return this.estadoMostrar;
	}

	public void setEstadoMostrar(Boolean estadoMostrar) {
		this.estadoMostrar = estadoMostrar;
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

	public RegActividad getRegActividad() {
		return this.regActividad;
	}

	public void setRegActividad(RegActividad regActividad) {
		this.regActividad = regActividad;
	}

	public RegInsumo getRegInsumo() {
		return this.regInsumo;
	}

	public void setRegInsumo(RegInsumo regInsumo) {
		this.regInsumo = regInsumo;
	}

	public RegSolicitud getRegSolicitud() {
		return this.regSolicitud;
	}

	public void setRegSolicitud(RegSolicitud regSolicitud) {
		this.regSolicitud = regSolicitud;
	}

	public RegTipo getRegTipo() {
		return this.regTipo;
	}

	public void setRegTipo(RegTipo regTipo) {
		this.regTipo = regTipo;
	}

	public SegDependencia getSegDependencia() {
		return this.segDependencia;
	}

	public void setSegDependencia(SegDependencia segDependencia) {
		this.segDependencia = segDependencia;
	}

	public SegUsuario getSegUsuario() {
		return this.segUsuario;
	}

	public void setSegUsuario(SegUsuario segUsuario) {
		this.segUsuario = segUsuario;
	}

}