/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_presentacion.biblioteca;

import Capa_negocio.NegocioPrestamo;
import Capa_persistencia.CabPrestamo;
import Capa_persistencia.DetPrestamo;
import Capa_persistencia.Libro;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pc
 */
public class MantenimientoPrestamo extends javax.swing.JFrame {

    NegocioPrestamo negocio = new NegocioPrestamo();

    /**
     * Creates new form MantenimientoPrestamo
     */
    public MantenimientoPrestamo() {
        initComponents();
        setLocationRelativeTo(null);
        txtNumero.setEditable(false);
        cargarComboLibros();
        nuevo();
        cargarCabeceras();
    }

    private void nuevo() {
        txtNumero.setText(negocio.siguienteNumero());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtFecha.setText(sdf.format(new Date()));
        txtFechaEntrega.setText(sdf.format(new Date()));

        txtDescripcion.setText("");
        txtCantidad.setText("");

        if (cmbLibro.getItemCount() > 0) {
            cmbLibro.setSelectedIndex(0);
        }

        limpiarDetalle();
        txtDescripcion.requestFocus();
    }

    private void limpiarDetalle() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Det");
        modelo.addColumn("ISBN");
        modelo.addColumn("Libro");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Fecha entrega");

        tblDetalle.setModel(modelo);
    }

    private void cargarComboLibros() {
        cmbLibro.removeAllItems();

        List<Libro> lista = negocio.listarLibros();

        if (lista != null) {
            for (Libro l : lista) {
                cmbLibro.addItem(l.getIsbn() + " - " + l.getTitulo());
            }
        }
    }

    private String obtenerIsbnSeleccionado() {
        String texto = cmbLibro.getSelectedItem().toString();
        return texto.substring(0, texto.indexOf(" - "));
    }

    private void seleccionarLibro(String isbn) {
        for (int i = 0; i < cmbLibro.getItemCount(); i++) {
            String item = cmbLibro.getItemAt(i).toString();

            if (item.startsWith(isbn + " - ")) {
                cmbLibro.setSelectedIndex(i);
                return;
            }
        }
    }

    private boolean validarCabecera() {
        if (txtFecha.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la fecha del préstamo");
            txtFecha.requestFocus();
            return false;
        }

        if (txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la descripción");
            txtDescripcion.requestFocus();
            return false;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            sdf.parse(txtFecha.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "La fecha debe tener formato dd/MM/yyyy");
            txtFecha.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validarDetalle() {
        if (cmbLibro.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Primero debe registrar un libro");
            return false;
        }

        if (txtCantidad.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la cantidad");
            txtCantidad.requestFocus();
            return false;
        }

        if (txtFechaEntrega.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la fecha de entrega");
            txtFechaEntrega.requestFocus();
            return false;
        }

        try {
            Integer.parseInt(txtCantidad.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero");
            txtCantidad.requestFocus();
            return false;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            sdf.parse(txtFechaEntrega.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "La fecha de entrega debe tener formato dd/MM/yyyy");
            txtFechaEntrega.requestFocus();
            return false;
        }

        return true;
    }

    private Date convertirFecha(String fecha) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        return sdf.parse(fecha);
    }

    private void cargarCabeceras() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Número");
        modelo.addColumn("Fecha");
        modelo.addColumn("Descripción");

        List<CabPrestamo> lista = negocio.listarCabeceras();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if (lista != null) {
            for (CabPrestamo c : lista) {
                String fecha = "";

                if (c.getFecha() != null) {
                    fecha = sdf.format(c.getFecha());
                }

                modelo.addRow(new Object[]{
                    c.getNumero(),
                    fecha,
                    c.getDescripcion()
                });
            }
        }

        tblCabecera.setModel(modelo);
    }

    private void cargarDetalles(String numero) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Det");
        modelo.addColumn("ISBN");
        modelo.addColumn("Libro");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Fecha entrega");

        List<DetPrestamo> lista = negocio.listarDetalles(numero);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if (lista != null) {
            for (DetPrestamo d : lista) {
                String isbn = "";
                String titulo = "";
                String fechaEntrega = "";

                if (d.getIsbn() != null) {
                    isbn = d.getIsbn().getIsbn();
                    titulo = d.getIsbn().getTitulo();
                }

                if (d.getFechaEntrega() != null) {
                    fechaEntrega = sdf.format(d.getFechaEntrega());
                }

                modelo.addRow(new Object[]{
                    d.getIdDet(),
                    isbn,
                    titulo,
                    d.getCantidad(),
                    fechaEntrega
                });
            }
        }

        tblDetalle.setModel(modelo);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        txtFecha = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cmbLibro = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtFechaEntrega = new javax.swing.JTextField();
        btnAgregarDetalle = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalle = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCabecera = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setText("Registro de préstamo");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Numero:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Fecha:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Descripcion:");

        txtNumero.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroActionPerformed(evt);
            }
        });

        txtFecha.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaActionPerformed(evt);
            }
        });

        txtDescripcion.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescripcionActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Libro:");

        cmbLibro.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cmbLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLibroActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Cantidad:");

        txtCantidad.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("FechaEntrega:");

        txtFechaEntrega.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtFechaEntrega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaEntregaActionPerformed(evt);
            }
        });

        btnAgregarDetalle.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnAgregarDetalle.setText("Agregar Detalle ");
        btnAgregarDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarDetalleActionPerformed(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnBuscar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnNuevo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        tblDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDetalle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDetalleMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDetalle);

        tblCabecera.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCabecera.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCabeceraMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblCabecera);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 673, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(192, 192, 192)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNumero)
                                    .addComponent(txtFecha)
                                    .addComponent(txtDescripcion)
                                    .addComponent(cmbLibro, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtCantidad)
                                    .addComponent(txtFechaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(111, 111, 111)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnAgregarDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap(277, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarDetalle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscar)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cmbLibro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtFechaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(btnEliminar)
                        .addGap(18, 18, 18)
                        .addComponent(btnNuevo)))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroActionPerformed

    private void txtFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaActionPerformed

    private void txtDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionActionPerformed

    private void cmbLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLibroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbLibroActionPerformed

    private void txtCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadActionPerformed

    private void txtFechaEntregaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaEntregaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaEntregaActionPerformed

    private void btnAgregarDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarDetalleActionPerformed
        // TODO add your handling code here:
        if (!validarDetalle()) {
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tblDetalle.getModel();

        BigDecimal idDet = negocio.siguienteIdDet().add(new BigDecimal(modelo.getRowCount()));
        String isbn = obtenerIsbnSeleccionado();
        Libro libro = negocio.buscarLibro(isbn);

        modelo.addRow(new Object[]{
            idDet,
            isbn,
            libro.getTitulo(),
            txtCantidad.getText().trim(),
            txtFechaEntrega.getText().trim()
        });

        txtCantidad.setText("");
        txtCantidad.requestFocus();

    }//GEN-LAST:event_btnAgregarDetalleActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        if (!validarCabecera()) {
            return;
        }

        if (tblDetalle.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un detalle");
            return;
        }

        try {
            List<DetPrestamo> detalles = new ArrayList<DetPrestamo>();

            for (int i = 0; i < tblDetalle.getRowCount(); i++) {
                BigDecimal idDet = new BigDecimal(tblDetalle.getValueAt(i, 0).toString());
                String isbn = tblDetalle.getValueAt(i, 1).toString();
                BigInteger cantidad = new BigInteger(tblDetalle.getValueAt(i, 3).toString());
                Date fechaEntrega = convertirFecha(tblDetalle.getValueAt(i, 4).toString());

                Libro libro = negocio.buscarLibro(isbn);

                DetPrestamo det = new DetPrestamo();
                det.setIdDet(idDet);
                det.setIsbn(libro);
                det.setCantidad(cantidad);
                det.setFechaEntrega(fechaEntrega);

                detalles.add(det);
            }

            int r = negocio.guardarCabeceraDetalle(
                    txtNumero.getText(),
                    convertirFecha(txtFecha.getText().trim()),
                    txtDescripcion.getText().trim(),
                    detalles
            );

            if (r == 1) {
                JOptionPane.showMessageDialog(this, "Préstamo guardado correctamente y comprobante contable generado");
                cargarCabeceras();
                nuevo();
            } else if (r == -2) {
                JOptionPane.showMessageDialog(this, "No se pudo guardar. Revise que existan las cuentas: Cuenta por cobrar e Inventario libros");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el préstamo");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        String numero = JOptionPane.showInputDialog(this, "Ingrese el número del préstamo:");

        if (numero != null && !numero.trim().isEmpty()) {
            CabPrestamo c = negocio.buscarCabecera(numero.trim());

            if (c != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                txtNumero.setText(c.getNumero());

                if (c.getFecha() != null) {
                    txtFecha.setText(sdf.format(c.getFecha()));
                } else {
                    txtFecha.setText("");
                }

                txtDescripcion.setText(c.getDescripcion());

                cargarDetalles(c.getNumero());

            } else {
                JOptionPane.showMessageDialog(this, "No existe el préstamo");
            }
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        String numero = txtNumero.getText().trim();

        if (numero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione o busque un préstamo");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que desea eliminar este préstamo?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            int r = negocio.eliminarCabecera(numero);

            if (r == 1) {
                JOptionPane.showMessageDialog(this, "Préstamo eliminado correctamente");
                cargarCabeceras();
                nuevo();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar");
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        nuevo();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void tblDetalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetalleMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDetalleMouseClicked

    private void tblCabeceraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCabeceraMouseClicked
        // TODO add your handling code here:
        int fila = tblCabecera.getSelectedRow();

        if (fila >= 0) {
            String numero = tblCabecera.getValueAt(fila, 0).toString();

            CabPrestamo c = negocio.buscarCabecera(numero);

            if (c != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                txtNumero.setText(c.getNumero());

                if (c.getFecha() != null) {
                    txtFecha.setText(sdf.format(c.getFecha()));
                } else {
                    txtFecha.setText("");
                }

                txtDescripcion.setText(c.getDescripcion());

                cargarDetalles(c.getNumero());
            }
        }
    }//GEN-LAST:event_tblCabeceraMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MantenimientoPrestamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenimientoPrestamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenimientoPrestamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenimientoPrestamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MantenimientoPrestamo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarDetalle;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cmbLibro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblCabecera;
    private javax.swing.JTable tblDetalle;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtFechaEntrega;
    private javax.swing.JTextField txtNumero;
    // End of variables declaration//GEN-END:variables
}
