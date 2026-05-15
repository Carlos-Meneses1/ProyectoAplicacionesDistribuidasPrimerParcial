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
@Table(name = "CAB_PRESTAMO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CabPrestamo.findAll", query = "SELECT c FROM CabPrestamo c")
    , @NamedQuery(name = "CabPrestamo.findByNumero", query = "SELECT c FROM CabPrestamo c WHERE c.numero = :numero")
    , @NamedQuery(name = "CabPrestamo.findByFecha", query = "SELECT c FROM CabPrestamo c WHERE c.fecha = :fecha")
    , @NamedQuery(name = "CabPrestamo.findByDescripcion", query = "SELECT c FROM CabPrestamo c WHERE c.descripcion = :descripcion")})
public class CabPrestamo implements Serializable {

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
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(mappedBy = "numero")
    private List<DetPrestamo> detPrestamoList;

    public CabPrestamo() {
    }

    public CabPrestamo(String numero) {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<DetPrestamo> getDetPrestamoList() {
        return detPrestamoList;
    }

    public void setDetPrestamoList(List<DetPrestamo> detPrestamoList) {
        this.detPrestamoList = detPrestamoList;
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
        if (!(object instanceof CabPrestamo)) {
            return false;
        }
        CabPrestamo other = (CabPrestamo) object;
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capa_persistencia.CabPrestamo[ numero=" + numero + " ]";
    }
    
}
