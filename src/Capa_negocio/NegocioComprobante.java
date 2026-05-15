/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_negocio;
import Capa_persistencia.CabComprobante;
import Capa_persistencia.Cuenta;
import Capa_persistencia.DetComprobante;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
/**
 *
 * @author pc
 */
public class NegocioComprobante {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
    EntityManager em = factory.createEntityManager();

    public String siguienteNumero() {
        try {
            em.clear();

            List<String> numeros = em.createQuery(
                    "SELECT c.numero FROM CabComprobante c").getResultList();

            int mayor = 0;

            for (String codigo : numeros) {
                String numero = codigo.replaceAll("[^0-9]", "");

                if (!numero.isEmpty()) {
                    int n = Integer.parseInt(numero);

                    if (n > mayor) {
                        mayor = n;
                    }
                }
            }

            return String.format("COMP%03d", mayor + 1);

        } catch (Exception e) {
            return "COMP001";
        }
    }

    public BigDecimal siguienteIdDet() {
        try {
            BigDecimal ultimo = (BigDecimal) em.createQuery(
                    "SELECT MAX(d.idDet) FROM DetComprobante d").getSingleResult();

            if (ultimo == null) {
                return BigDecimal.ONE;
            }

            return ultimo.add(BigDecimal.ONE);

        } catch (Exception e) {
            return BigDecimal.ONE;
        }
    }

    public List<Cuenta> listarCuentas() {
        try {
            em.clear();
            return em.createQuery("SELECT c FROM Cuenta c").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Cuenta buscarCuenta(String codigo) {
        try {
            return em.find(Cuenta.class, codigo);
        } catch (Exception e) {
            return null;
        }
    }

    public int guardarCabeceraDetalle(String numero, Date fecha, String observaciones, List<DetComprobante> detalles) {
        try {
            em.getTransaction().begin();

            CabComprobante cab = new CabComprobante();
            cab.setNumero(numero);
            cab.setFecha(fecha);
            cab.setObservaciones(observaciones);

            em.persist(cab);

            for (DetComprobante det : detalles) {
                det.setNumero(cab);
                em.persist(det);
            }

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

    public CabComprobante buscarCabecera(String numero) {
        try {
            return em.find(CabComprobante.class, numero);
        } catch (Exception e) {
            return null;
        }
    }

    public List<CabComprobante> listarCabeceras() {
        try {
            em.clear();
            return em.createQuery("SELECT c FROM CabComprobante c").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DetComprobante> listarDetalles(String numero) {
        try {
            em.clear();
            return em.createQuery(
                    "SELECT d FROM DetComprobante d WHERE d.numero.numero = :num")
                    .setParameter("num", numero)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int eliminarCabecera(String numero) {
        try {
            em.getTransaction().begin();

            List<DetComprobante> detalles = em.createQuery(
                    "SELECT d FROM DetComprobante d WHERE d.numero.numero = :num")
                    .setParameter("num", numero)
                    .getResultList();

            for (DetComprobante d : detalles) {
                em.remove(em.merge(d));
            }

            CabComprobante cab = em.find(CabComprobante.class, numero);

            if (cab != null) {
                em.remove(cab);
            }

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
    public java.util.List<Object[]> reporteBalanceGeneral() throws Exception {
    try {
        em.clear();

        String jpql = "SELECT c.codTipo.nombre, c.nombre, "
                + "SUM(CASE WHEN d.debe IS NULL THEN 0 ELSE d.debe END), "
                + "SUM(CASE WHEN d.haber IS NULL THEN 0 ELSE d.haber END) "
                + "FROM DetComprobante d "
                + "JOIN d.codCuenta c "
                + "WHERE c.codTipo.nombre IN ('Activo', 'Pasivo', 'Patrimonio') "
                + "GROUP BY c.codTipo.nombre, c.nombre "
                + "ORDER BY c.codTipo.nombre, c.nombre";

        return em.createQuery(jpql).getResultList();

    } catch (Exception e) {
        e.printStackTrace();
        throw new Exception("Error al generar balance general: " + e.getMessage());
    }
}
    public java.util.List<Object[]> reporteEstadoResultados() throws Exception {
    try {
        em.clear();

        String jpql = "SELECT c.codTipo.nombre, c.nombre, "
                + "SUM(CASE WHEN d.debe IS NULL THEN 0 ELSE d.debe END), "
                + "SUM(CASE WHEN d.haber IS NULL THEN 0 ELSE d.haber END) "
                + "FROM DetComprobante d "
                + "JOIN d.codCuenta c "
                + "WHERE c.codTipo.nombre IN ('Ingreso', 'Egreso') "
                + "GROUP BY c.codTipo.nombre, c.nombre "
                + "ORDER BY c.codTipo.nombre, c.nombre";

        return em.createQuery(jpql).getResultList();

    } catch (Exception e) {
        e.printStackTrace();
        throw new Exception("Error al generar estado de resultados: " + e.getMessage());
    }
}
    public List<Object[]> reporteBalanceGeneral(String fechaDesde, String fechaHasta) throws Exception {
    try {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.util.Date dDesde = sdf.parse(fechaDesde);
        java.util.Date dHasta = sdf.parse(fechaHasta);

        String jpql =
                "SELECT c.codTipo.nombre, c.nombre, SUM(d.debe), SUM(d.haber), "
                + "(SUM(d.debe) - SUM(d.haber)) "
                + "FROM DetComprobante d "
                + "JOIN d.codCuenta c "
                + "WHERE d.numero.fecha >= :desde AND d.numero.fecha <= :hasta "
                + "AND (LOWER(c.codTipo.nombre) LIKE '%activo%' "
                + "OR LOWER(c.codTipo.nombre) LIKE '%pasivo%' "
                + "OR LOWER(c.codTipo.nombre) LIKE '%patrimonio%') "
                + "GROUP BY c.codTipo.nombre, c.nombre "
                + "ORDER BY c.codTipo.nombre, c.nombre";

        return em.createQuery(jpql)
                .setParameter("desde", dDesde, javax.persistence.TemporalType.DATE)
                .setParameter("hasta", dHasta, javax.persistence.TemporalType.DATE)
                .getResultList();

    } catch (Exception e) {
        throw new Exception("Error en balance general: " + e.getMessage());
    }
}
    public List<Object[]> reporteEstadoResultados(String fechaDesde, String fechaHasta) throws Exception {
    try {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.util.Date dDesde = sdf.parse(fechaDesde);
        java.util.Date dHasta = sdf.parse(fechaHasta);

        String jpql =
                "SELECT c.codTipo.nombre, c.nombre, SUM(d.debe), SUM(d.haber), "
                + "(SUM(d.haber) - SUM(d.debe)) "
                + "FROM DetComprobante d "
                + "JOIN d.codCuenta c "
                + "WHERE d.numero.fecha >= :desde AND d.numero.fecha <= :hasta "
                + "AND (LOWER(c.codTipo.nombre) LIKE '%ingreso%' "
                + "OR LOWER(c.codTipo.nombre) LIKE '%egreso%') "
                + "GROUP BY c.codTipo.nombre, c.nombre "
                + "ORDER BY c.codTipo.nombre, c.nombre";

        return em.createQuery(jpql)
                .setParameter("desde", dDesde, javax.persistence.TemporalType.DATE)
                .setParameter("hasta", dHasta, javax.persistence.TemporalType.DATE)
                .getResultList();

    } catch (Exception e) {
        throw new Exception("Error en estado de resultados: " + e.getMessage());
    }
}
}
