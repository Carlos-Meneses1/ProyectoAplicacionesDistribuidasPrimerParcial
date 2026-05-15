/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_negocio;

import Capa_persistencia.CabPrestamo;
import Capa_persistencia.DetPrestamo;
import Capa_persistencia.Libro;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import Capa_persistencia.CabComprobante;
import Capa_persistencia.DetComprobante;
import Capa_persistencia.Cuenta;
import java.math.BigInteger;

/**
 *
 * @author pc
 */
public class NegocioPrestamo {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
    EntityManager em = factory.createEntityManager();

    public String siguienteNumero() {
        try {
            String ultimo = (String) em.createQuery(
                    "SELECT MAX(c.numero) FROM CabPrestamo c").getSingleResult();

            if (ultimo == null) {
                return "P001";
            }

            String numero = ultimo.replaceAll("[^0-9]", "");

            if (numero.isEmpty()) {
                return "P001";
            }

            int num = Integer.parseInt(numero) + 1;
            return String.format("P%03d", num);

        } catch (Exception e) {
            return "P001";
        }
    }

    public BigDecimal siguienteIdDet() {
        try {
            BigDecimal ultimo = (BigDecimal) em.createQuery(
                    "SELECT MAX(d.idDet) FROM DetPrestamo d").getSingleResult();

            if (ultimo == null) {
                return BigDecimal.ONE;
            }

            return ultimo.add(BigDecimal.ONE);

        } catch (Exception e) {
            return BigDecimal.ONE;
        }
    }

    public List<Libro> listarLibros() {
        try {
            em.clear();
            return em.createQuery("SELECT l FROM Libro l").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Libro buscarLibro(String isbn) {
        try {
            return em.find(Libro.class, isbn);
        } catch (Exception e) {
            return null;
        }
    }

    public int guardarCabeceraDetalle(String numero, Date fecha, String descripcion, List<DetPrestamo> detalles) {
        try {
            em.getTransaction().begin();

            // 1. Guardar cabecera del préstamo
            CabPrestamo cab = new CabPrestamo();
            cab.setNumero(numero);
            cab.setFecha(fecha);
            cab.setDescripcion(descripcion);

            em.persist(cab);

            // 2. Guardar detalles del préstamo y calcular total
            BigDecimal totalPrestamo = BigDecimal.ZERO;

            for (DetPrestamo det : detalles) {
                det.setNumero(cab);
                em.persist(det);

                if (det.getIsbn() != null && det.getIsbn().getValorPrestamo() != null && det.getCantidad() != null) {
                    BigDecimal valorLibro = det.getIsbn().getValorPrestamo();
                    BigDecimal cantidad = new BigDecimal(det.getCantidad());

                    totalPrestamo = totalPrestamo.add(valorLibro.multiply(cantidad));
                }
            }

            // 3. Buscar cuentas contables
            Cuenta cuentaCobrar = buscarCuentaPorTexto("cobrar");
            Cuenta inventarioLibros = buscarCuentaPorTexto("inventario libros");

            if (cuentaCobrar == null || inventarioLibros == null) {
                em.getTransaction().rollback();
                return -2;
            }

            // 4. Crear cabecera del comprobante contable
            CabComprobante comp = new CabComprobante();
            comp.setNumero("BIB" + numero);
            comp.setFecha(fecha);
            comp.setObservaciones("Préstamo de biblioteca " + numero + " - " + descripcion);

            em.persist(comp);

            // 5. Crear detalle DEBE: Cuenta por cobrar
            BigDecimal idComprobante = siguienteIdDetComprobante();

            DetComprobante detDebe = new DetComprobante();
            detDebe.setIdDet(idComprobante);
            detDebe.setNumero(comp);
            detDebe.setCodCuenta(cuentaCobrar);
            detDebe.setDebe(totalPrestamo);
            detDebe.setHaber(BigDecimal.ZERO);

            em.persist(detDebe);

// 6. Crear detalle HABER: Inventario libros
            DetComprobante detHaber = new DetComprobante();
            detHaber.setIdDet(idComprobante.add(BigDecimal.ONE));
            detHaber.setNumero(comp);
            detHaber.setCodCuenta(inventarioLibros);
            detHaber.setDebe(BigDecimal.ZERO);
            detHaber.setHaber(totalPrestamo);

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

    public CabPrestamo buscarCabecera(String numero) {
        try {
            return em.find(CabPrestamo.class, numero);
        } catch (Exception e) {
            return null;
        }
    }

    public List<CabPrestamo> listarCabeceras() {
        try {
            em.clear();
            return em.createQuery("SELECT c FROM CabPrestamo c").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DetPrestamo> listarDetalles(String numero) {
        try {
            em.clear();
            return em.createQuery(
                    "SELECT d FROM DetPrestamo d WHERE d.numero.numero = :num")
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

            List<DetPrestamo> detalles = em.createQuery(
                    "SELECT d FROM DetPrestamo d WHERE d.numero.numero = :num")
                    .setParameter("num", numero)
                    .getResultList();

            for (DetPrestamo d : detalles) {
                em.remove(em.merge(d));
            }

            CabPrestamo cab = em.find(CabPrestamo.class, numero);

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

    public List<Object[]> reporteLibrosEntregarPorDia(String fechaDesde, String fechaHasta) throws Exception {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        java.util.Date dDesde = sdf.parse(fechaDesde);
        java.util.Date dHasta = sdf.parse(fechaHasta);

        try {
            em.clear();

            String jpql = "SELECT d.fechaEntrega, SUM(d.cantidad) "
                    + "FROM DetPrestamo d "
                    + "WHERE d.fechaEntrega BETWEEN :desde AND :hasta "
                    + "GROUP BY d.fechaEntrega "
                    + "ORDER BY d.fechaEntrega";

            return em.createQuery(jpql)
                    .setParameter("desde", dDesde, javax.persistence.TemporalType.DATE)
                    .setParameter("hasta", dHasta, javax.persistence.TemporalType.DATE)
                    .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al generar reporte: " + e.getMessage());
        }
    }

    public List<Capa_persistencia.Autor> obtenerAutores() throws Exception {
        try {
            em.clear();
            return em.createQuery("SELECT a FROM Autor a ORDER BY a.nombre, a.apellido").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al obtener autores: " + e.getMessage());
        }
    }

   public List<Object[]> reporteMatrizLibrosAutores(String fechaDesde, String fechaHasta) throws Exception {
    try {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        java.util.Date dDesde = sdf.parse(fechaDesde);
        java.util.Date dHasta = sdf.parse(fechaHasta);

        em.clear();

        String jpql = "SELECT l.titulo, a.nombre, a.apellido, SUM(d.cantidad) "
                + "FROM DetPrestamo d "
                + "JOIN d.isbn l "
                + "JOIN l.codAutor a "
                + "WHERE d.numero.fecha >= :desde AND d.numero.fecha <= :hasta "
                + "GROUP BY l.titulo, a.nombre, a.apellido "
                + "ORDER BY a.apellido, a.nombre, l.titulo";

        return em.createQuery(jpql)
                .setParameter("desde", dDesde, javax.persistence.TemporalType.DATE)
                .setParameter("hasta", dHasta, javax.persistence.TemporalType.DATE)
                .getResultList();

    } catch (Exception e) {
        e.printStackTrace();
        throw new Exception("Error al generar reporte libros vs autores: " + e.getMessage());
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
