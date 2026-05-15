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

import Capa_persistencia.ActivoMant;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Clase de negocio para gestionar los Activos del módulo de Mantenimiento.
 * Maneja las operaciones CRUD sobre la tabla ACTIVO_MANT.
 */
public class NegocioActivo {

    // Conexión a la unidad de persistencia definida en persistence.xml
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProyectoAppDisPU");
    EntityManager em = factory.createEntityManager();

    /**
     * Genera el siguiente código automático para un nuevo activo. Formato:
     * A001, A002, A003...
     */
    public String siguienteId() {
        try {
            String ultimo = (String) em.createQuery(
                    "SELECT MAX(a.codigo) FROM ActivoMant a").getSingleResult();
            if (ultimo == null) {
                return "A001";
            }
            String numero = ultimo.replaceAll("[^0-9]", "");
            int num = Integer.parseInt(numero) + 1;
            return String.format("A%03d", num);
        } catch (Exception e) {
            return "A001";
        }
    }

    /**
     * Inserta un nuevo activo en la base de datos. Retorna 1 si fue exitoso, -1
     * si hubo error.
     */
    public int insertar(String codigo, String nombre, Date fechaCompra) {
        try {
            ActivoMant a = new ActivoMant();
            a.setCodigo(codigo);
            a.setNombre(nombre);
            a.setFechaCompra(fechaCompra);
            em.getTransaction().begin();
            em.persist(a);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Modifica un activo existente buscándolo por su código. Retorna 1 si fue
     * exitoso, -1 si no existe o hubo error.
     */
    public int modificar(String codigo, String nombre, Date fechaCompra) {
        try {
            ActivoMant a = em.find(ActivoMant.class, codigo);
            if (a == null) {
                return -1;
            }
            a.setNombre(nombre);
            a.setFechaCompra(fechaCompra);
            em.getTransaction().begin();
            em.merge(a);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Elimina un activo de la base de datos por su código. Retorna 1 si fue
     * exitoso, -1 si no existe o hubo error.
     */
    public int eliminar(String codigo) {
        try {
            ActivoMant a = em.find(ActivoMant.class, codigo);
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

    /**
     * Busca y retorna un activo por su código. Retorna null si no existe.
     */
    public ActivoMant buscar(String codigo) {
        try {
            return em.find(ActivoMant.class, codigo);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retorna la lista completa de activos registrados en ACTIVO_MANT.
     */
    public List<ActivoMant> listar() {
        try {
            return em.createQuery("SELECT a FROM ActivoMant a").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
