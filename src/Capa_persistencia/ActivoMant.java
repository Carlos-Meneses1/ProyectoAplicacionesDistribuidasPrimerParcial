/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_persistencia;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author pc
 */
@Entity
@Table(name = "ACTIVO_MANT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActivoMant.findAll", query = "SELECT a FROM ActivoMant a")
    , @NamedQuery(name = "ActivoMant.findByCodigo", query = "SELECT a FROM ActivoMant a WHERE a.codigo = :codigo")
    , @NamedQuery(name = "ActivoMant.findByNombre", query = "SELECT a FROM ActivoMant a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "ActivoMant.findByFechaCompra", query = "SELECT a FROM ActivoMant a WHERE a.fechaCompra = :fechaCompra")})
public class ActivoMant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "CODIGO")
    private String codigo;
    @Size(max = 50)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "FECHA_COMPRA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCompra;
    @OneToMany(mappedBy = "codActivo")
    private List<DetMantenimiento> detMantenimientoList;

    public ActivoMant() {
    }

    public ActivoMant(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    @XmlTransient
    public List<DetMantenimiento> getDetMantenimientoList() {
        return detMantenimientoList;
    }

    public void setDetMantenimientoList(List<DetMantenimiento> detMantenimientoList) {
        this.detMantenimientoList = detMantenimientoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActivoMant)) {
            return false;
        }
        ActivoMant other = (ActivoMant) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capa_persistencia.ActivoMant[ codigo=" + codigo + " ]";
    }
    
}
