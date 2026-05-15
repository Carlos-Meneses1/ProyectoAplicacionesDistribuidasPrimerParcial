/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_negocio;

import Capa_persistencia.Actividad;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author pc
 */
public class NegocioActividad {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
    EntityManager em = factory.createEntityManager();

    // Obtener siguiente codigo automatico
    public String siguienteCodigo() {
        try {

            String ultimo = (String) em.createQuery(
                    "SELECT MAX(a.codigo) FROM Actividad a")
                    .getSingleResult();

            if (ultimo == null) {
                return "0001";
            }

            int numero = Integer.parseInt(ultimo);
            numero++;

            return String.format("%04d", numero);

        } catch (Exception e) {

            return "0001";
        }
    }

    // Insertar
    public int insertar(String as_codigo, String as_nombre) {
          try {

        Actividad a = new Actividad();

        a.setCodigo(as_codigo);

        a.setNombre(as_nombre);

        em.getTransaction().begin();

        em.persist(a);

        em.getTransaction().commit();

        System.out.println("INSERTADO CORRECTAMENTE");

        return 1;

    } catch (Exception e) {

        em.getTransaction().rollback();

        e.printStackTrace();

        return -1;
    }
    }

    // Eliminar
    public int eliminar(String as_codigo) {
        try {

            Actividad a = em.find(Actividad.class, as_codigo);

            if (a == null) {
                return -1;
            }

            em.getTransaction().begin();
            em.remove(a);
            em.getTransaction().commit();

            return 1;

        } catch (Exception e) {

            em.getTransaction().rollback();
            return -1;
        }
    }

    // Modificar
    public int modificar(String as_codigo, String as_nombre) {
        try {

            Actividad a = em.find(Actividad.class, as_codigo);

            if (a == null) {
                return -1;
            }

            a.setNombre(as_nombre);

            em.getTransaction().begin();
            em.merge(a);
            em.getTransaction().commit();

            return 1;

        } catch (Exception e) {

            em.getTransaction().rollback();
            return -1;
        }
    }

    // Buscar por codigo
    public Actividad buscar(String as_codigo) {
        try {
            return em.find(Actividad.class, as_codigo);
        } catch (Exception e) {
            return null;
        }
    }

    // Listar todos
    public List<Actividad> listar() {
        try {

        List<Actividad> lista =
                em.createQuery("SELECT a FROM Actividad a")
                .getResultList();

        System.out.println("TOTAL REGISTROS: " + lista.size());

        return lista;

    } catch (Exception e) {

        e.printStackTrace();

        return null;
    }
    }
}
