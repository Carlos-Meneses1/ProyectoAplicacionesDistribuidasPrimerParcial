/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author pc
 */
@Entity
@Table(name = "DET_PRESTAMO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetPrestamo.findAll", query = "SELECT d FROM DetPrestamo d")
    , @NamedQuery(name = "DetPrestamo.findByIdDet", query = "SELECT d FROM DetPrestamo d WHERE d.idDet = :idDet")
    , @NamedQuery(name = "DetPrestamo.findByCantidad", query = "SELECT d FROM DetPrestamo d WHERE d.cantidad = :cantidad")
    , @NamedQuery(name = "DetPrestamo.findByFechaEntrega", query = "SELECT d FROM DetPrestamo d WHERE d.fechaEntrega = :fechaEntrega")})
public class DetPrestamo implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_DET")
    private BigDecimal idDet;
    @Column(name = "CANTIDAD")
    private BigInteger cantidad;
    @Column(name = "FECHA_ENTREGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @JoinColumn(name = "NUMERO", referencedColumnName = "NUMERO")
    @ManyToOne
    private CabPrestamo numero;
    @JoinColumn(name = "ISBN", referencedColumnName = "ISBN")
    @ManyToOne
    private Libro isbn;

    public DetPrestamo() {
    }

    public DetPrestamo(BigDecimal idDet) {
        this.idDet = idDet;
    }

    public BigDecimal getIdDet() {
        return idDet;
    }

    public void setIdDet(BigDecimal idDet) {
        this.idDet = idDet;
    }

    public BigInteger getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigInteger cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public CabPrestamo getNumero() {
        return numero;
    }

    public void setNumero(CabPrestamo numero) {
        this.numero = numero;
    }

    public Libro getIsbn() {
        return isbn;
    }

    public void setIsbn(Libro isbn) {
        this.isbn = isbn;
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
        if (!(object instanceof DetPrestamo)) {
            return false;
        }
        DetPrestamo other = (DetPrestamo) object;
        if ((this.idDet == null && other.idDet != null) || (this.idDet != null && !this.idDet.equals(other.idDet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capa_persistencia.DetPrestamo[ idDet=" + idDet + " ]";
    }
    
}
