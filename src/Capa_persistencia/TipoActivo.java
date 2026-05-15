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
@Table(name = "TIPO_ACTIVO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoActivo.findAll", query = "SELECT t FROM TipoActivo t")
    , @NamedQuery(name = "TipoActivo.findByCodigo", query = "SELECT t FROM TipoActivo t WHERE t.codigo = :codigo")
    , @NamedQuery(name = "TipoActivo.findByNombre", query = "SELECT t FROM TipoActivo t WHERE t.nombre = :nombre")})
public class TipoActivo implements Serializable {

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
    @OneToMany(mappedBy = "codTipo")
    private List<Activo> activoList;

    public TipoActivo() {
    }

    public TipoActivo(String codigo) {
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
    public List<Activo> getActivoList() {
        return activoList;
    }

    public void setActivoList(List<Activo> activoList) {
        this.activoList = activoList;
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
        if (!(object instanceof TipoActivo)) {
            return false;
        }
        TipoActivo other = (TipoActivo) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capa_persistencia.TipoActivo[ codigo=" + codigo + " ]";
    }
    
}
