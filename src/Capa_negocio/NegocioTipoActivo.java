/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_negocio;
import Capa_persistencia.TipoActivo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
/**
 *
 * @author pc
 */
public class NegocioTipoActivo {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
    EntityManager em = factory.createEntityManager();

    public String siguienteId() {
        try {
            String ultimo = (String) em.createQuery(
                    "SELECT MAX(t.codigo) FROM TipoActivo t").getSingleResult();

            if (ultimo == null) {
                return "T001";
            }

            String numero = ultimo.replaceAll("[^0-9]", "");
            int num = Integer.parseInt(numero) + 1;
            return String.format("T%03d", num);

        } catch (Exception e) {
            return "T001";
        }
    }

    public int insertar(String codigo, String nombre) {
        try {
            TipoActivo t = new TipoActivo();
            t.setCodigo(codigo);
            t.setNombre(nombre);

            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();

            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return -1;
        }
    }

    public int modificar(String codigo, String nombre) {
        try {
            TipoActivo t = em.find(TipoActivo.class, codigo);

            if (t == null) {
                return -1;
            }

            t.setNombre(nombre);

            em.getTransaction().begin();
            em.merge(t);
            em.getTransaction().commit();

            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return -1;
        }
    }

    public int eliminar(String codigo) {
        try {
            TipoActivo t = em.find(TipoActivo.class, codigo);

            if (t == null) {
                return -1;
            }

            em.getTransaction().begin();
            em.remove(t);
            em.getTransaction().commit();

            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return -1;
        }
    }

    public TipoActivo buscar(String codigo) {
        try {
            return em.find(TipoActivo.class, codigo);
        } catch (Exception e) {
            return null;
        }
    }

    public List<TipoActivo> listar() {
        try {
            em.clear();
            return em.createQuery("SELECT t FROM TipoActivo t").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
