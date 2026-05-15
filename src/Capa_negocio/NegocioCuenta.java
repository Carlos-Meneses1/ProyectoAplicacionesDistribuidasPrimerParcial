/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_negocio;

import Capa_persistencia.Cuenta;
import Capa_persistencia.TipoCuenta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author pc
 */
public class NegocioCuenta {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
    EntityManager em = factory.createEntityManager();

  public String siguienteCodigo() {
    try {
        em.clear();

        java.util.List<String> codigos = em.createQuery(
                "SELECT c.codigo FROM Cuenta c").getResultList();

        int mayor = 0;

        for (String codigo : codigos) {
            String numero = codigo.replaceAll("[^0-9]", "");

            if (!numero.isEmpty()) {
                int n = Integer.parseInt(numero);

                if (n > mayor) {
                    mayor = n;
                }
            }
        }

        int siguiente = mayor + 1;
        return String.format("C%03d", siguiente);

    } catch (Exception e) {
        e.printStackTrace();
        return "C001";
    }
}

    public int insertar(String codigo, String nombre, String codTipo) {
        try {
            TipoCuenta tipo = em.find(TipoCuenta.class, codTipo);

            if (tipo == null) {
                return -1;
            }

            Cuenta c = new Cuenta();
            c.setCodigo(codigo);
            c.setNombre(nombre);
            c.setCodTipo(tipo);

            em.getTransaction().begin();
            em.persist(c);
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

    public int modificar(String codigo, String nombre, String codTipo) {
        try {
            Cuenta c = em.find(Cuenta.class, codigo);

            if (c == null) {
                return -1;
            }

            TipoCuenta tipo = em.find(TipoCuenta.class, codTipo);

            if (tipo == null) {
                return -1;
            }

            c.setNombre(nombre);
            c.setCodTipo(tipo);

            em.getTransaction().begin();
            em.merge(c);
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
            Cuenta c = em.find(Cuenta.class, codigo);

            if (c == null) {
                return -1;
            }

            em.getTransaction().begin();
            em.remove(c);
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

    public Cuenta buscar(String codigo) {
        try {
            return em.find(Cuenta.class, codigo);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Cuenta> listar() {
        try {
            em.clear();
            return em.createQuery("SELECT c FROM Cuenta c").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<TipoCuenta> listarTiposCuenta() {
        try {
            em.clear();
            return em.createQuery("SELECT t FROM TipoCuenta t").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
