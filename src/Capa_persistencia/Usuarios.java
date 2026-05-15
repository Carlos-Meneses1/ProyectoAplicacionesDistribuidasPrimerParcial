package Capa_persistencia;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "USUARIOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u")
    , @NamedQuery(name = "Usuarios.findByIdUser", query = "SELECT u FROM Usuarios u WHERE u.idUser = :idUser")
    , @NamedQuery(name = "Usuarios.findByUsuario", query = "SELECT u FROM Usuarios u WHERE u.usuario = :usuario")
    , @NamedQuery(name = "Usuarios.findByPassword", query = "SELECT u FROM Usuarios u WHERE u.password = :password")})
public class Usuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID_USER")
    private Long idUser;
    
    @Basic(optional = false)
    @Column(name = "USUARIO")
    private String usuario;
    
    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;
    
    public Usuarios() {}
    
    public Usuarios(Long idUser) {
        this.idUser = idUser;
    }
    
    public Long getIdUser() { return idUser; }
    public void setIdUser(Long idUser) { this.idUser = idUser; }
    
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuarios)) return false;
        Usuarios other = (Usuarios) object;
        return !((this.idUser == null && other.idUser != null) || 
                 (this.idUser != null && !this.idUser.equals(other.idUser)));
    }
    
    @Override
    public String toString() {
        return "Capa_persistencia.Usuarios[ idUser=" + idUser + " ]";
    }
}