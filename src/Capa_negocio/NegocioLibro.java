/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_negocio;
import Capa_persistencia.Autor;
import Capa_persistencia.Libro;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author pc
 */
public class NegocioLibro {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
    EntityManager em = factory.createEntityManager();

    public int insertar(String isbn, String titulo, String codAutor, BigDecimal valorPrestamo) {
        try {
            Autor autor = em.find(Autor.class, codAutor);

            if (autor == null) {
                return -1;
            }

            Libro l = new Libro();
            l.setIsbn(isbn);
            l.setTitulo(titulo);
            l.setCodAutor(autor);
            l.setValorPrestamo(valorPrestamo);

            em.getTransaction().begin();
            em.persist(l);
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

    public int modificar(String isbn, String titulo, String codAutor, BigDecimal valorPrestamo) {
        try {
            Libro l = em.find(Libro.class, isbn);

            if (l == null) {
                return -1;
            }

            Autor autor = em.find(Autor.class, codAutor);

            if (autor == null) {
                return -1;
            }

            l.setTitulo(titulo);
            l.setCodAutor(autor);
            l.setValorPrestamo(valorPrestamo);

            em.getTransaction().begin();
            em.merge(l);
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

    public int eliminar(String isbn) {
        try {
            Libro l = em.find(Libro.class, isbn);

            if (l == null) {
                return -1;
            }

            em.getTransaction().begin();
            em.remove(l);
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

    public Libro buscar(String isbn) {
        try {
            return em.find(Libro.class, isbn);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Libro> listar() {
        try {
            em.clear();
            return em.createQuery("SELECT l FROM Libro l").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Autor> listarAutores() {
        try {
            em.clear();
            return em.createQuery("SELECT a FROM Autor a").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
