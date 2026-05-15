/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_negocio;

import Capa_persistencia.CabMantenimiento;
import Capa_persistencia.DetMantenimiento;
import Capa_persistencia.ActivoMant;
import Capa_persistencia.Actividad;
import Capa_persistencia.CabComprobante;
import Capa_persistencia.DetComprobante;
import Capa_persistencia.Cuenta;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TemporalType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NegocioMantenimiento {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
    EntityManager em = factory.createEntityManager();

    // Genera siguiente numero de cabecera: M001, M002...
    public String siguienteNumero() {
        try {
            String ultimo = (String) em.createQuery(
                    "SELECT MAX(c.numero) FROM CabMantenimiento c").getSingleResult();
            if (ultimo == null) {
                return "M001";
            }
            String numero = ultimo.replaceAll("[^0-9]", "");
            int num = Integer.parseInt(numero) + 1;
            return String.format("M%03d", num);
        } catch (Exception e) {
            return "M001";
        }
    }

    // Genera siguiente numero de comprobante contable
    public String siguienteNumeroComp() {
        try {
            String ultimo = (String) em.createQuery(
                    "SELECT MAX(c.numero) FROM CabComprobante c").getSingleResult();
            if (ultimo == null) {
                return "C001";
            }
            String numero = ultimo.replaceAll("[^0-9]", "");
            int num = Integer.parseInt(numero) + 1;
            return String.format("C%03d", num);
        } catch (Exception e) {
            return "C001";
        }
    }

    // Genera siguiente ID de detalle de mantenimiento
    public BigDecimal siguienteIdDet() {
        try {
            BigDecimal ultimo = (BigDecimal) em.createQuery(
                    "SELECT MAX(d.idDet) FROM DetMantenimiento d").getSingleResult();
            if (ultimo == null) {
                return BigDecimal.ONE;
            }
            return ultimo.add(BigDecimal.ONE);
        } catch (Exception e) {
            return BigDecimal.ONE;
        }
    }

    // Genera siguiente ID de detalle de comprobante
    public BigDecimal siguienteIdDetComp() {
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

    // Insertar cabecera + detalles + comprobante contable automático
    public int insertarCabConIntegracion(String numero, Date fecha, String responsable,
            List<DetMantenimiento> detalles, BigDecimal totalGasto) {
        try {
            em.getTransaction().begin();

            // 1. Guardar cabecera de mantenimiento
            CabMantenimiento cab = new CabMantenimiento();
            cab.setNumero(numero);
            cab.setFecha(fecha);
            cab.setResponsable(responsable);
            em.persist(cab);

            // 2. Guardar detalles de mantenimiento
            BigDecimal idMant = siguienteIdDet();
            int contadorMant = 0;

            for (DetMantenimiento det : detalles) {
                det.setIdDet(idMant.add(new BigDecimal(contadorMant)));
                det.setNumero(cab);
                em.persist(det);
                contadorMant++;
            }

            // 3. Generar comprobante contable automático (Gasto vs Bancos)
            String numComp = siguienteNumeroCompMant();

            CabComprobante comp = new CabComprobante();
            comp.setNumero(numComp);
            comp.setFecha(fecha);
            comp.setObservaciones("Mantenimiento " + numero + " - " + responsable);
            em.persist(comp);

            Cuenta cuentaGasto = buscarCuentaPorTexto("gasto mantenimiento");
            Cuenta cuentaBancos = buscarCuentaPorTexto("bancos");

            if (cuentaGasto == null) {
                cuentaGasto = em.find(Cuenta.class, "GASTO001");
            }

            if (cuentaBancos == null) {
                cuentaBancos = em.find(Cuenta.class, "BANCO001");
            }

            if (cuentaGasto == null || cuentaBancos == null) {
                em.getTransaction().rollback();
                return -2;
            }

            BigDecimal idComp = siguienteIdDetComp();

// Línea DEBE: Gasto de mantenimiento
            DetComprobante detGasto = new DetComprobante();
            detGasto.setIdDet(idComp);
            detGasto.setDebe(totalGasto);
            detGasto.setHaber(BigDecimal.ZERO);
            detGasto.setNumero(comp);
            detGasto.setCodCuenta(cuentaGasto);
            em.persist(detGasto);

// Línea HABER: Bancos
            DetComprobante detBancos = new DetComprobante();
            detBancos.setIdDet(idComp.add(BigDecimal.ONE));
            detBancos.setDebe(BigDecimal.ZERO);
            detBancos.setHaber(totalGasto);
            detBancos.setNumero(comp);
            detBancos.setCodCuenta(cuentaBancos);
            em.persist(detBancos);

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

    // Listar cabeceras
    public List<CabMantenimiento> listarCab() {
        try {
            em.clear();
            return em.createQuery("SELECT c FROM CabMantenimiento c").getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    // Listar detalles por cabecera
    public List<DetMantenimiento> listarDet(String numero) {
        try {
            em.clear();
            return em.createQuery(
                    "SELECT d FROM DetMantenimiento d WHERE d.numero.numero = :num")
                    .setParameter("num", numero)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    // Eliminar cabecera y sus detalles
    public int eliminarCab(String numero) {
        try {
            em.getTransaction().begin();
            List<DetMantenimiento> detalles = listarDet(numero);
            if (detalles != null) {
                for (DetMantenimiento d : detalles) {
                    em.remove(em.merge(d));
                }
            }
            CabMantenimiento cab = em.find(CabMantenimiento.class, numero);
            if (cab != null) {
                em.remove(cab);
            }
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return -1;
        }
    }

    // Listar activos para el combo
    public List<ActivoMant> listarActivos() {
        try {
            return em.createQuery("SELECT a FROM ActivoMant a").getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    // Listar actividades para el combo
    public List<Actividad> listarActividades() {
        try {
            return em.createQuery("SELECT a FROM Actividad a").getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    // Buscar cabecera por numero
    public CabMantenimiento buscarCab(String numero) {
        try {
            return em.find(CabMantenimiento.class, numero);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Object[]> reporteValoresPorActivo(String fechaDesde, String fechaHasta) throws Exception {
        List<Object[]> lista = new ArrayList<>();

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
        EntityManager em = factory.createEntityManager();

        try {
            String jpql = "SELECT d.codActivo.nombre, SUM(d.valor) "
                    + "FROM DetMantenimiento d "
                    + "WHERE d.numero.fecha >= :fechaDesde AND d.numero.fecha <= :fechaHasta "
                    + "GROUP BY d.codActivo.nombre "
                    + "ORDER BY d.codActivo.nombre";

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dDesde = sdf.parse(fechaDesde);
            Date dHasta = sdf.parse(fechaHasta);

            lista = em.createQuery(jpql)
                    .setParameter("fechaDesde", dDesde, TemporalType.DATE)
                    .setParameter("fechaHasta", dHasta, TemporalType.DATE)
                    .getResultList();

        } catch (Exception e) {
            throw new Exception("Error en reporte: " + e.getMessage());
        } finally {
            em.close();
            factory.close();
        }

        return lista;
    }
    // Obtiene todas las actividades (para las columnas)

    public List<Actividad> obtenerActividades() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
        EntityManager em = factory.createEntityManager();
        List<Actividad> lista = new ArrayList<>();
        try {
            lista = em.createNamedQuery("Actividad.findAll").getResultList();
        } finally {
            em.close();
            factory.close();
        }
        return lista;
    }

// Obtiene los datos para la matriz
    public List<Object[]> reporteMatrizActivosActividades(String fechaDesde, String fechaHasta) throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
        EntityManager em = factory.createEntityManager();
        List<Object[]> lista = new ArrayList<>();
        try {
            String jpql = "SELECT d.codActivo.nombre, d.codActividad.nombre, SUM(d.valor) "
                    + "FROM DetMantenimiento d "
                    + "WHERE d.numero.fecha >= :fechaDesde AND d.numero.fecha <= :fechaHasta "
                    + "GROUP BY d.codActivo.nombre, d.codActividad.nombre "
                    + "ORDER BY d.codActivo.nombre, d.codActividad.nombre";

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dDesde = sdf.parse(fechaDesde);
            Date dHasta = sdf.parse(fechaHasta);

            lista = em.createQuery(jpql)
                    .setParameter("fechaDesde", dDesde, TemporalType.DATE)
                    .setParameter("fechaHasta", dHasta, TemporalType.DATE)
                    .getResultList();
        } finally {
            em.close();
            factory.close();
        }
        return lista;
    }

    private Cuenta buscarCuentaPorTexto(String texto) {
        try {
            List<Cuenta> cuentas = em.createQuery("SELECT c FROM Cuenta c").getResultList();

            for (Cuenta c : cuentas) {
                if (c.getNombre() != null
                        && c.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                    return c;
                }
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    private String siguienteNumeroCompMant() {
        try {
            List<String> numeros = em.createQuery(
                    "SELECT c.numero FROM CabComprobante c WHERE c.numero LIKE 'MANT%'")
                    .getResultList();

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

            return String.format("MANT%03d", mayor + 1);

        } catch (Exception e) {
            return "MANT001";
        }
    }
}
