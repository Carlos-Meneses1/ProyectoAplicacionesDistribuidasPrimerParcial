/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_persistencia;

import java.io.Serializable;
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
@Table(name = "CUENTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuenta.findAll", query = "SELECT c FROM Cuenta c")
    , @NamedQuery(name = "Cuenta.findByCodigo", query = "SELECT c FROM Cuenta c WHERE c.codigo = :codigo")
    , @NamedQuery(name = "Cuenta.findByNombre", query = "SELECT c FROM Cuenta c WHERE c.nombre = :nombre")})
public class Cuenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "CODIGO")
    private String codigo;
    @Size(max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
    @OneToMany(mappedBy = "codCuenta")
    private List<DetComprobante> detComprobanteList;
    @JoinColumn(name = "COD_TIPO", referencedColumnName = "CODIGO")
    @ManyToOne
    private TipoCuenta codTipo;

    public Cuenta() {
    }

    public Cuenta(String codigo) {
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

    @XmlTransient
    public List<DetComprobante> getDetComprobanteList() {
        return detComprobanteList;
    }

    public void setDetComprobanteList(List<DetComprobante> detComprobanteList) {
        this.detComprobanteList = detComprobanteList;
    }

    public TipoCuenta getCodTipo() {
        return codTipo;
    }

    public void setCodTipo(TipoCuenta codTipo) {
        this.codTipo = codTipo;
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
        if (!(object instanceof Cuenta)) {
            return false;
        }
        Cuenta other = (Cuenta) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capa_persistencia.Cuenta[ codigo=" + codigo + " ]";
    }
    
}
