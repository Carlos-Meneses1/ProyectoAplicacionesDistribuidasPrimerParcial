/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_negocio;

/**
 *
 * @author pc
 */
import Capa_persistencia.Autor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class NegocioAutor {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
    EntityManager em = factory.createEntityManager();

    public String siguienteCodigo() {
        try {
            String ultimo = (String) em.createQuery(
                    "SELECT MAX(a.codigo) FROM Autor a").getSingleResult();

            if (ultimo == null) {
                return "A001";
            }

            String numero = ultimo.replaceAll("[^0-9]", "");

            if (numero.isEmpty()) {
                return "A001";
            }

            int num = Integer.parseInt(numero) + 1;
            return String.format("A%03d", num);

        } catch (Exception e) {
            return "A001";
        }
    }

    public int insertar(String codigo, String nombre, String apellido) {
        try {
            Autor a = new Autor();
            a.setCodigo(codigo);
            a.setNombre(nombre);
            a.setApellido(apellido);

            em.getTransaction().begin();
            em.persist(a);
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

    public int modificar(String codigo, String nombre, String apellido) {
        try {
            Autor a = em.find(Autor.class, codigo);

            if (a == null) {
                return -1;
            }

            a.setNombre(nombre);
            a.setApellido(apellido);

            em.getTransaction().begin();
            em.merge(a);
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
            Autor a = em.find(Autor.class, codigo);

            if (a == null) {
                return -1;
            }

            em.getTransaction().begin();
            em.remove(a);
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

    public Autor buscar(String codigo) {
        try {
            return em.find(Autor.class, codigo);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Autor> listar() {
        try {
            em.clear();
            return em.createQuery("SELECT a FROM Autor a").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
