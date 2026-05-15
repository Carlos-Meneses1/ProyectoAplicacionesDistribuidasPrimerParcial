/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_persistencia;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author pc
 */
@Entity
@Table(name = "ACTIVO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Activo.findAll", query = "SELECT a FROM Activo a")
    ,
    @NamedQuery(name = "Activo.findById", query = "SELECT a FROM Activo a WHERE a.id = :id")
})
public class Activo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ID")
    private String id;

    @Size(max = 50)
    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "FECHA_COMPRA")
    @Temporal(TemporalType.DATE)
    private Date fechaCompra;

    // Estos 3 campos existen en Oracle, los declaramos para que JPA no falle
    // No los usamos en la pantalla de Mantenimiento
    @Column(name = "PERIODOS_DEPRECIACION")
    private java.math.BigInteger periodosDepreciacion;

    @Column(name = "VALOR_COMPRA")
    private java.math.BigDecimal valorCompra;

    @JoinColumn(name = "COD_TIPO", referencedColumnName = "CODIGO")
    @ManyToOne
    private TipoActivo codTipo;

    public Activo() {
    }

    public Activo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public java.math.BigInteger getPeriodosDepreciacion() {
        return periodosDepreciacion;
    }

    public void setPeriodosDepreciacion(java.math.BigInteger p) {
        this.periodosDepreciacion = p;
    }

    public java.math.BigDecimal getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(java.math.BigDecimal v) {
        this.valorCompra = v;
    }

    public TipoActivo getCodTipo() {
        return codTipo;
    }

    public void setCodTipo(TipoActivo codTipo) {
        this.codTipo = codTipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Activo)) {
            return false;
        }
        Activo other = (Activo) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capa_persistencia.Activo[ id=" + id + " ]";
    }

}
