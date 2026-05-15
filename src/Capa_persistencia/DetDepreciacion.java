/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author pc
 */
@Entity
@Table(name = "DET_DEPRECIACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetDepreciacion.findAll", query = "SELECT d FROM DetDepreciacion d")
    , @NamedQuery(name = "DetDepreciacion.findByIdDet", query = "SELECT d FROM DetDepreciacion d WHERE d.idDet = :idDet")
    , @NamedQuery(name = "DetDepreciacion.findByNroPeriodo", query = "SELECT d FROM DetDepreciacion d WHERE d.nroPeriodo = :nroPeriodo")
    , @NamedQuery(name = "DetDepreciacion.findByValorDepreciacion", query = "SELECT d FROM DetDepreciacion d WHERE d.valorDepreciacion = :valorDepreciacion")})
public class DetDepreciacion implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_DET")
    private BigDecimal idDet;
    @Column(name = "NRO_PERIODO")
    private BigInteger nroPeriodo;
    @Column(name = "VALOR_DEPRECIACION")
    private BigDecimal valorDepreciacion;
    @JoinColumn(name = "ID_ACTIVO", referencedColumnName = "ID")
    @ManyToOne
    private Activo idActivo;
    @JoinColumn(name = "NUMERO", referencedColumnName = "NUMERO")
    @ManyToOne
    private CabDepreciacion numero;

    public DetDepreciacion() {
    }

    public DetDepreciacion(BigDecimal idDet) {
        this.idDet = idDet;
    }

    public BigDecimal getIdDet() {
        return idDet;
    }

    public void setIdDet(BigDecimal idDet) {
        this.idDet = idDet;
    }

    public BigInteger getNroPeriodo() {
        return nroPeriodo;
    }

    public void setNroPeriodo(BigInteger nroPeriodo) {
        this.nroPeriodo = nroPeriodo;
    }

    public BigDecimal getValorDepreciacion() {
        return valorDepreciacion;
    }

    public void setValorDepreciacion(BigDecimal valorDepreciacion) {
        this.valorDepreciacion = valorDepreciacion;
    }

    public Activo getIdActivo() {
        return idActivo;
    }

    public void setIdActivo(Activo idActivo) {
        this.idActivo = idActivo;
    }

    public CabDepreciacion getNumero() {
        return numero;
    }

    public void setNumero(CabDepreciacion numero) {
        this.numero = numero;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDet != null ? idDet.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetDepreciacion)) {
            return false;
        }
        DetDepreciacion other = (DetDepreciacion) object;
        if ((this.idDet == null && other.idDet != null) || (this.idDet != null && !this.idDet.equals(other.idDet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capa_persistencia.DetDepreciacion[ idDet=" + idDet + " ]";
    }
    
}
