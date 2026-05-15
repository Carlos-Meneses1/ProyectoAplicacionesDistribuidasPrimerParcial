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
@Table(name = "DET_MANTENIMIENTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetMantenimiento.findAll", query = "SELECT d FROM DetMantenimiento d")
    , @NamedQuery(name = "DetMantenimiento.findByIdDet", query = "SELECT d FROM DetMantenimiento d WHERE d.idDet = :idDet")
    , @NamedQuery(name = "DetMantenimiento.findByValor", query = "SELECT d FROM DetMantenimiento d WHERE d.valor = :valor")})
public class DetMantenimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_DET")
    private BigDecimal idDet;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @JoinColumn(name = "COD_ACTIVIDAD", referencedColumnName = "CODIGO")
    @ManyToOne
    private Actividad codActividad;
    @JoinColumn(name = "COD_ACTIVO", referencedColumnName = "CODIGO")
    @ManyToOne
    private ActivoMant codActivo;
    @JoinColumn(name = "NUMERO", referencedColumnName = "NUMERO")
    @ManyToOne
    private CabMantenimiento numero;

    public DetMantenimiento() {
    }

    public DetMantenimiento(BigDecimal idDet) {
        this.idDet = idDet;
    }

    public BigDecimal getIdDet() {
        return idDet;
    }

    public void setIdDet(BigDecimal idDet) {
        this.idDet = idDet;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Actividad getCodActividad() {
        return codActividad;
    }

    public void setCodActividad(Actividad codActividad) {
        this.codActividad = codActividad;
    }

    public ActivoMant getCodActivo() {
        return codActivo;
    }

    public void setCodActivo(ActivoMant codActivo) {
        this.codActivo = codActivo;
    }

    public CabMantenimiento getNumero() {
        return numero;
    }

    public void setNumero(CabMantenimiento numero) {
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
        if (!(object instanceof DetMantenimiento)) {
            return false;
        }
        DetMantenimiento other = (DetMantenimiento) object;
        if ((this.idDet == null && other.idDet != null) || (this.idDet != null && !this.idDet.equals(other.idDet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capa_persistencia.DetMantenimiento[ idDet=" + idDet + " ]";
    }
    
}
