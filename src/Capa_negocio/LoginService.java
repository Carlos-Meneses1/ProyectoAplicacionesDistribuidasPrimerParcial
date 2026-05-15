/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_negocio;
import Capa_persistencia.Usuarios;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author pc
 */
public class LoginService {

    public int validar(String as_usuario, String as_password) {
        int li_retorno = -1;
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
        EntityManager em = factory.createEntityManager();

        try {
            Usuarios u = (Usuarios) em.createNamedQuery("Usuarios.findByUsuario")
                    .setParameter("usuario", as_usuario)
                    .getSingleResult();
            if (u != null) {
                if (u.getPassword().equals(as_password)) {
                    li_retorno = 1;
                }
            }
        } catch (Exception ex) {
            li_retorno = -1;
        }

        em.close();
        factory.close();
        return li_retorno;
    }
}
