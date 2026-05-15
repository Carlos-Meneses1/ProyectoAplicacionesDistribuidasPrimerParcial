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
@Table(name = "CAB_COMPROBANTE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CabComprobante.findAll", query = "SELECT c FROM CabComprobante c")
    , @NamedQuery(name = "CabComprobante.findByNumero", query = "SELECT c FROM CabComprobante c WHERE c.numero = :numero")
    , @NamedQuery(name = "CabComprobante.findByFecha", query = "SELECT c FROM CabComprobante c WHERE c.fecha = :fecha")
    , @NamedQuery(name = "CabComprobante.findByObservaciones", query = "SELECT c FROM CabComprobante c WHERE c.observaciones = :observaciones")})
public class CabComprobante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "NUMERO")
    private String numero;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 200)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @OneToMany(mappedBy = "numero")
    private List<DetComprobante> detComprobanteList;

    public CabComprobante() {
    }

    public CabComprobante(String numero) {
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @XmlTransient
    public List<DetComprobante> getDetComprobanteList() {
        return detComprobanteList;
    }

    public void setDetComprobanteList(List<DetComprobante> detComprobanteList) {
        this.detComprobanteList = detComprobanteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numero != null ? numero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CabComprobante)) {
            return false;
        }
        CabComprobante other = (CabComprobante) object;
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capa_persistencia.CabComprobante[ numero=" + numero + " ]";
    }
    
}
