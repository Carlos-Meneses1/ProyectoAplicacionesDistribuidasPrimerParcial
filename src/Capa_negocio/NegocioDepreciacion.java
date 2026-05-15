/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_negocio;
import Capa_persistencia.Activo;
import Capa_persistencia.CabDepreciacion;
import Capa_persistencia.DetDepreciacion;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import Capa_persistencia.CabComprobante;
import Capa_persistencia.DetComprobante;
import Capa_persistencia.Cuenta;
/**
 *
 * @author pc
 */
public class NegocioDepreciacion {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
    EntityManager em = factory.createEntityManager();

    public String siguienteNumero() {
        try {
            String ultimo = (String) em.createQuery(
                    "SELECT MAX(c.numero) FROM CabDepreciacion c").getSingleResult();

            if (ultimo == null) {
                return "D001";
            }

            String numero = ultimo.replaceAll("[^0-9]", "");

            if (numero.isEmpty()) {
                return "D001";
            }

            int num = Integer.parseInt(numero) + 1;
            return String.format("D%03d", num);

        } catch (Exception e) {
            return "D001";
        }
    }

    public BigDecimal siguienteIdDet() {
        try {
            BigDecimal ultimo = (BigDecimal) em.createQuery(
                    "SELECT MAX(d.idDet) FROM DetDepreciacion d").getSingleResult();

            if (ultimo == null) {
                return BigDecimal.ONE;
            }

            return ultimo.add(BigDecimal.ONE);

        } catch (Exception e) {
            return BigDecimal.ONE;
        }
    }

    public List<Activo> listarActivos() {
        try {
            em.clear();
            return em.createQuery("SELECT a FROM Activo a").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int siguientePeriodoActivo(String idActivo) {
        try {
            BigInteger ultimo = (BigInteger) em.createQuery(
                    "SELECT MAX(d.nroPeriodo) FROM DetDepreciacion d WHERE d.idActivo.id = :id")
                    .setParameter("id", idActivo)
                    .getSingleResult();

            if (ultimo == null) {
                return 1;
            }

            return ultimo.intValue() + 1;

        } catch (Exception e) {
            return 1;
        }
    }

    public int guardarCabeceraDetalle(String numero, Date fecha, String observaciones,
            String responsable, List<DetDepreciacion> detalles) {

          try {
        em.getTransaction().begin();

        // 1. Guardar cabecera de depreciación
        CabDepreciacion cab = new CabDepreciacion();
        cab.setNumero(numero);
        cab.setFecha(fecha);
        cab.setObservaciones(observaciones);
        cab.setResponsable(responsable);

        em.persist(cab);

        // 2. Guardar detalles y calcular total depreciado
        BigDecimal totalDepreciacion = BigDecimal.ZERO;

        for (DetDepreciacion det : detalles) {
            det.setNumero(cab);
            em.persist(det);

            if (det.getValorDepreciacion() != null) {
                totalDepreciacion = totalDepreciacion.add(det.getValorDepreciacion());
            }
        }

        // 3. Buscar cuentas contables
        Cuenta gastoDepreciacion = buscarCuentaPorTexto("gasto depreciación");
        Cuenta depreciacionAcumulada = buscarCuentaPorTexto("depreciación acumulada");

        if (gastoDepreciacion == null || depreciacionAcumulada == null) {
            em.getTransaction().rollback();
            return -2;
        }

        // 4. Crear cabecera del comprobante contable
        CabComprobante comp = new CabComprobante();
        comp.setNumero("DEPR" + numero);
        comp.setFecha(fecha);
        comp.setObservaciones("Depreciación de activos " + numero + " - " + observaciones);

        em.persist(comp);

        BigDecimal idComprobante = siguienteIdDetComprobante();

        // 5. Detalle DEBE: Gasto depreciación
        DetComprobante detDebe = new DetComprobante();
        detDebe.setIdDet(idComprobante);
        detDebe.setNumero(comp);
        detDebe.setCodCuenta(gastoDepreciacion);
        detDebe.setDebe(totalDepreciacion);
        detDebe.setHaber(BigDecimal.ZERO);

        em.persist(detDebe);

        // 6. Detalle HABER: Depreciación acumulada
        DetComprobante detHaber = new DetComprobante();
        detHaber.setIdDet(idComprobante.add(BigDecimal.ONE));
        detHaber.setNumero(comp);
        detHaber.setCodCuenta(depreciacionAcumulada);
        detHaber.setDebe(BigDecimal.ZERO);
        detHaber.setHaber(totalDepreciacion);

        em.persist(detHaber);

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

    public CabDepreciacion buscarCabecera(String numero) {
        try {
            return em.find(CabDepreciacion.class, numero);
        } catch (Exception e) {
            return null;
        }
    }

    public List<CabDepreciacion> listarCabeceras() {
        try {
            em.clear();
            return em.createQuery("SELECT c FROM CabDepreciacion c").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DetDepreciacion> listarDetalles(String numero) {
        try {
            em.clear();
            return em.createQuery(
                    "SELECT d FROM DetDepreciacion d WHERE d.numero.numero = :num")
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

            List<DetDepreciacion> detalles = em.createQuery(
                    "SELECT d FROM DetDepreciacion d WHERE d.numero.numero = :num")
                    .setParameter("num", numero)
                    .getResultList();

            for (DetDepreciacion d : detalles) {
                em.remove(em.merge(d));
            }

            CabDepreciacion cab = em.find(CabDepreciacion.class, numero);

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

    public Activo buscarActivo(String id) {
        try {
            return em.find(Activo.class, id);
        } catch (Exception e) {
            return null;
        }
    }
    public List<Object[]> reporteValoresDepreciadosPorActivo(String fechaDesde, String fechaHasta) throws Exception {
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
    java.util.Date dDesde = sdf.parse(fechaDesde);
    java.util.Date dHasta = sdf.parse(fechaHasta);

    try {
        String jpql = "SELECT d.idActivo.nombre, SUM(d.valorDepreciacion) "
                + "FROM DetDepreciacion d "
                + "WHERE d.numero.fecha >= :desde AND d.numero.fecha <= :hasta "
                + "GROUP BY d.idActivo.nombre "
                + "ORDER BY d.idActivo.nombre";

        return em.createQuery(jpql)
                .setParameter("desde", dDesde, javax.persistence.TemporalType.DATE)
                .setParameter("hasta", dHasta, javax.persistence.TemporalType.DATE)
                .getResultList();

    } catch (Exception e) {
        throw new Exception("Error al generar reporte: " + e.getMessage());
    }
}
    
  public List<Object[]> reporteNumeroActivosPorTipo(String fechaDesde, String fechaHasta) throws Exception {
    try {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.util.Date dDesde = sdf.parse(fechaDesde);
        java.util.Date dHasta = sdf.parse(fechaHasta);

        String jpql =
                "SELECT d.idActivo.codTipo.nombre, COUNT(DISTINCT d.idActivo.id) "
                + "FROM DetDepreciacion d "
                + "WHERE d.numero.fecha >= :desde AND d.numero.fecha <= :hasta "
                + "GROUP BY d.idActivo.codTipo.nombre "
                + "ORDER BY d.idActivo.codTipo.nombre";

        return em.createQuery(jpql)
                .setParameter("desde", dDesde, javax.persistence.TemporalType.DATE)
                .setParameter("hasta", dHasta, javax.persistence.TemporalType.DATE)
                .getResultList();

    } catch (Exception e) {
        throw new Exception("Error en reporte activos por tipo: " + e.getMessage());
    }
}
    private Cuenta buscarCuentaPorTexto(String texto) {
    try {
        List<Cuenta> cuentas = em.createQuery("SELECT c FROM Cuenta c").getResultList();

        for (Cuenta c : cuentas) {
            if (c.getNombre() != null && c.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                return c;
            }
        }

        return null;

    } catch (Exception e) {
        return null;
    }
}

private BigDecimal siguienteIdDetComprobante() {
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
}