/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author pc
 */
@Entity
@Table(name = "LIBRO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Libro.findAll", query = "SELECT l FROM Libro l")
    , @NamedQuery(name = "Libro.findByIsbn", query = "SELECT l FROM Libro l WHERE l.isbn = :isbn")
    , @NamedQuery(name = "Libro.findByTitulo", query = "SELECT l FROM Libro l WHERE l.titulo = :titulo")
    , @NamedQuery(name = "Libro.findByValorPrestamo", query = "SELECT l FROM Libro l WHERE l.valorPrestamo = :valorPrestamo")})
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ISBN")
    private String isbn;
    @Size(max = 100)
    @Column(name = "TITULO")
    private String titulo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VALOR_PRESTAMO")
    private BigDecimal valorPrestamo;
    @OneToMany(mappedBy = "isbn")
    private List<DetPrestamo> detPrestamoList;
    @JoinColumn(name = "COD_AUTOR", referencedColumnName = "CODIGO")
    @ManyToOne
    private Autor codAutor;

    public Libro() {
    }

    public Libro(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public BigDecimal getValorPrestamo() {
        return valorPrestamo;
    }

    public void setValorPrestamo(BigDecimal valorPrestamo) {
        this.valorPrestamo = valorPrestamo;
    }

    @XmlTransient
    public List<DetPrestamo> getDetPrestamoList() {
        return detPrestamoList;
    }

    public void setDetPrestamoList(List<DetPrestamo> detPrestamoList) {
        this.detPrestamoList = detPrestamoList;
    }

    public Autor getCodAutor() {
        return codAutor;
    }

    public void setCodAutor(Autor codAutor) {
        this.codAutor = codAutor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (isbn != null ? isbn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Libro)) {
            return false;
        }
        Libro other = (Libro) object;
        if ((this.isbn == null && other.isbn != null) || (this.isbn != null && !this.isbn.equals(other.isbn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capa_persistencia.Libro[ isbn=" + isbn + " ]";
    }
    
}
