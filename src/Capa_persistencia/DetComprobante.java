/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "DET_COMPROBANTE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetComprobante.findAll", query = "SELECT d FROM DetComprobante d")
    , @NamedQuery(name = "DetComprobante.findByIdDet", query = "SELECT d FROM DetComprobante d WHERE d.idDet = :idDet")
    , @NamedQuery(name = "DetComprobante.findByDebe", query = "SELECT d FROM DetComprobante d WHERE d.debe = :debe")
    , @NamedQuery(name = "DetComprobante.findByHaber", query = "SELECT d FROM DetComprobante d WHERE d.haber = :haber")})
public class DetComprobante implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_DET")
    private BigDecimal idDet;
    @Column(name = "DEBE")
    private BigDecimal debe;
    @Column(name = "HABER")
    private BigDecimal haber;
    @JoinColumn(name = "NUMERO", referencedColumnName = "NUMERO")
    @ManyToOne
    private CabComprobante numero;
    @JoinColumn(name = "COD_CUENTA", referencedColumnName = "CODIGO")
    @ManyToOne
    private Cuenta codCuenta;

    public DetComprobante() {
    }

    public DetComprobante(BigDecimal idDet) {
        this.idDet = idDet;
    }

    public BigDecimal getIdDet() {
        return idDet;
    }

    public void setIdDet(BigDecimal idDet) {
        this.idDet = idDet;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public CabComprobante getNumero() {
        return numero;
    }

    public void setNumero(CabComprobante numero) {
        this.numero = numero;
    }

    public Cuenta getCodCuenta() {
        return codCuenta;
    }

    public void setCodCuenta(Cuenta codCuenta) {
        this.codCuenta = codCuenta;
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
        if (!(object instanceof DetComprobante)) {
            return false;
        }
        DetComprobante other = (DetComprobante) object;
        if ((this.idDet == null && other.idDet != null) || (this.idDet != null && !this.idDet.equals(other.idDet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capa_persistencia.DetComprobante[ idDet=" + idDet + " ]";
    }
    
}
