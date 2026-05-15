/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_negocio;
import Capa_persistencia.Activo;
import Capa_persistencia.TipoActivo;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
/**
 *
 * @author pc
 */
public class NegocioActivoDep {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
    EntityManager em = factory.createEntityManager();

    public String siguienteId() {
        try {
            String ultimo = (String) em.createQuery(
                    "SELECT MAX(a.id) FROM Activo a").getSingleResult();

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

    public int insertar(String id, String nombre, int periodos, BigDecimal valorCompra, String codTipo) {
        try {
            TipoActivo tipo = em.find(TipoActivo.class, codTipo);

            if (tipo == null) {
                return -1;
            }

            Activo a = new Activo();
            a.setId(id);
            a.setNombre(nombre);
            a.setPeriodosDepreciacion(BigInteger.valueOf(periodos));
            a.setValorCompra(valorCompra);
            a.setCodTipo(tipo);

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

    public int modificar(String id, String nombre, int periodos, BigDecimal valorCompra, String codTipo) {
        try {
            Activo a = em.find(Activo.class, id);

            if (a == null) {
                return -1;
            }

            TipoActivo tipo = em.find(TipoActivo.class, codTipo);

            if (tipo == null) {
                return -1;
            }

            a.setNombre(nombre);
            a.setPeriodosDepreciacion(BigInteger.valueOf(periodos));
            a.setValorCompra(valorCompra);
            a.setCodTipo(tipo);

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

    public int eliminar(String id) {
        try {
            Activo a = em.find(Activo.class, id);

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

    public Activo buscar(String id) {
        try {
            return em.find(Activo.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Activo> listar() {
        try {
            em.clear();
            return em.createQuery("SELECT a FROM Activo a").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<TipoActivo> listarTiposActivo() {
        try {
            em.clear();
            return em.createQuery("SELECT t FROM TipoActivo t").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
