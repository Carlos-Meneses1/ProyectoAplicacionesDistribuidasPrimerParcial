/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_presentacion.contabilidad;
import Capa_negocio.NegocioComprobante;
import Capa_persistencia.CabComprobante;
import Capa_persistencia.Cuenta;
import Capa_persistencia.DetComprobante;
import java.math.BigDecimal;
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
public class MantenimientoComprobante extends javax.swing.JFrame {
NegocioComprobante negocio = new NegocioComprobante();
    /**
     * Creates new form MantenimientoComprobante
     */
   public MantenimientoComprobante() {
    initComponents();
    setLocationRelativeTo(null);
    txtNumero.setEditable(false);
    cargarComboCuentas();
    nuevo();
    cargarCabeceras();
}
private void nuevo() {
    txtNumero.setText(negocio.siguienteNumero());

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    txtFecha.setText(sdf.format(new Date()));

    txtObservaciones.setText("");
    txtDebe.setText("");
    txtHaber.setText("");

    if (cmbCuenta.getItemCount() > 0) {
        cmbCuenta.setSelectedIndex(0);
    }

    limpiarDetalle();
    txtObservaciones.requestFocus();
}

private void limpiarDetalle() {
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("ID Det");
    modelo.addColumn("Código cuenta");
    modelo.addColumn("Cuenta");
    modelo.addColumn("Debe");
    modelo.addColumn("Haber");

    tblDetalle.setModel(modelo);
}

private void cargarComboCuentas() {
    cmbCuenta.removeAllItems();

    List<Cuenta> lista = negocio.listarCuentas();

    if (lista != null) {
        for (Cuenta c : lista) {
            cmbCuenta.addItem(c.getCodigo() + " - " + c.getNombre());
        }
    }
}

private String obtenerCodigoCuentaSeleccionada() {
    String texto = cmbCuenta.getSelectedItem().toString();
    return texto.substring(0, texto.indexOf(" - "));
}

private boolean validarCabecera() {
    if (txtFecha.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese la fecha");
        txtFecha.requestFocus();
        return false;
    }

    if (txtObservaciones.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese las observaciones");
        txtObservaciones.requestFocus();
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
    if (cmbCuenta.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(this, "Primero debe registrar una cuenta");
        return false;
    }

    String debe = txtDebe.getText().trim();
    String haber = txtHaber.getText().trim();

    if (debe.isEmpty()) {
        debe = "0";
    }

    if (haber.isEmpty()) {
        haber = "0";
    }

    try {
        BigDecimal valorDebe = new BigDecimal(debe);
        BigDecimal valorHaber = new BigDecimal(haber);

        if (valorDebe.compareTo(BigDecimal.ZERO) == 0 && valorHaber.compareTo(BigDecimal.ZERO) == 0) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor en debe o haber");
            return false;
        }

        if (valorDebe.compareTo(BigDecimal.ZERO) > 0 && valorHaber.compareTo(BigDecimal.ZERO) > 0) {
            JOptionPane.showMessageDialog(this, "No puede ingresar debe y haber en la misma línea");
            return false;
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Debe y haber deben ser valores numéricos");
        return false;
    }

    return true;
}

private Date convertirFecha(String fecha) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    sdf.setLenient(false);
    return sdf.parse(fecha);
}

private BigDecimal totalDebe() {
    BigDecimal total = BigDecimal.ZERO;

    for (int i = 0; i < tblDetalle.getRowCount(); i++) {
        total = total.add(new BigDecimal(tblDetalle.getValueAt(i, 3).toString()));
    }

    return total;
}

private BigDecimal totalHaber() {
    BigDecimal total = BigDecimal.ZERO;

    for (int i = 0; i < tblDetalle.getRowCount(); i++) {
        total = total.add(new BigDecimal(tblDetalle.getValueAt(i, 4).toString()));
    }

    return total;
}

private void cargarCabeceras() {
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("Número");
    modelo.addColumn("Fecha");
    modelo.addColumn("Observaciones");

    List<CabComprobante> lista = negocio.listarCabeceras();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    if (lista != null) {
        for (CabComprobante c : lista) {
            String fecha = "";

            if (c.getFecha() != null) {
                fecha = sdf.format(c.getFecha());
            }

            modelo.addRow(new Object[]{
                c.getNumero(),
                fecha,
                c.getObservaciones()
            });
        }
    }

    tblCabecera.setModel(modelo);
}

private void cargarDetalles(String numero) {
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("ID Det");
    modelo.addColumn("Código cuenta");
    modelo.addColumn("Cuenta");
    modelo.addColumn("Debe");
    modelo.addColumn("Haber");

    List<DetComprobante> lista = negocio.listarDetalles(numero);

    if (lista != null) {
        for (DetComprobante d : lista) {
            String codigo = "";
            String cuenta = "";

            if (d.getCodCuenta() != null) {
                codigo = d.getCodCuenta().getCodigo();
                cuenta = d.getCodCuenta().getNombre();
            }

            modelo.addRow(new Object[]{
                d.getIdDet(),
                codigo,
                cuenta,
                d.getDebe(),
                d.getHaber()
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnAgregarDetalle = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCabecera = new javax.swing.JTable();
        txtNumero = new javax.swing.JTextField();
        txtFecha = new javax.swing.JTextField();
        txtObservaciones = new javax.swing.JTextField();
        cmbCuenta = new javax.swing.JComboBox<>();
        txtDebe = new javax.swing.JTextField();
        txtHaber = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalle = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setText("Gestión de comprobantes contables");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setText("Número:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setText("Fecha:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setText("Observaciones:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setText("Cuenta");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel6.setText("Debe:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel7.setText("Haber:");

        btnNuevo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnAgregarDetalle.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnAgregarDetalle.setText("Agregar Detalle");
        btnAgregarDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarDetalleActionPerformed(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnBuscar.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

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

        txtNumero.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroActionPerformed(evt);
            }
        });

        txtFecha.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaActionPerformed(evt);
            }
        });

        txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtObservaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtObservacionesActionPerformed(evt);
            }
        });

        cmbCuenta.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        cmbCuenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCuentaActionPerformed(evt);
            }
        });

        txtDebe.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtDebe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDebeActionPerformed(evt);
            }
        });

        txtHaber.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtHaber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHaberActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel8.setText("Detalle");

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

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel9.setText("Cabecera");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(228, 228, 228)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel2))
                                        .addGap(75, 75, 75)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel5)
                                                    .addComponent(jLabel6)
                                                    .addComponent(jLabel7))
                                                .addGap(70, 70, 70))
                                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtDebe, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtHaber, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(87, 87, 87)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnAgregarDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE))
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane1)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(721, 721, 721)
                        .addComponent(jLabel9)))
                .addContainerGap(175, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(316, 316, 316))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(726, 726, 726))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(86, 86, 86)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregarDetalle)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardar)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDebe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel7))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtHaber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 50, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(27, 27, 27)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroActionPerformed

    private void txtFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaActionPerformed

    private void txtObservacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtObservacionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtObservacionesActionPerformed

    private void cmbCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCuentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCuentaActionPerformed

    private void txtDebeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDebeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDebeActionPerformed

    private void txtHaberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHaberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHaberActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        nuevo();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnAgregarDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarDetalleActionPerformed
        // TODO add your handling code here:
        if (!validarDetalle()) {
        return;
    }

    DefaultTableModel modelo = (DefaultTableModel) tblDetalle.getModel();

    BigDecimal idDet = negocio.siguienteIdDet().add(new BigDecimal(modelo.getRowCount()));
    String codigoCuenta = obtenerCodigoCuentaSeleccionada();
    Cuenta cuenta = negocio.buscarCuenta(codigoCuenta);

    String debe = txtDebe.getText().trim();
    String haber = txtHaber.getText().trim();

    if (debe.isEmpty()) {
        debe = "0";
    }

    if (haber.isEmpty()) {
        haber = "0";
    }

    modelo.addRow(new Object[]{
        idDet,
        codigoCuenta,
        cuenta.getNombre(),
        new BigDecimal(debe),
        new BigDecimal(haber)
    });

    txtDebe.setText("");
    txtHaber.setText("");
    txtDebe.requestFocus();
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

    BigDecimal debe = totalDebe();
    BigDecimal haber = totalHaber();

    if (debe.compareTo(haber) != 0) {
        JOptionPane.showMessageDialog(this, "El comprobante está descuadrado. Debe: " + debe + " Haber: " + haber);
        return;
    }

    try {
        List<DetComprobante> detalles = new ArrayList<DetComprobante>();

        for (int i = 0; i < tblDetalle.getRowCount(); i++) {
            BigDecimal idDet = new BigDecimal(tblDetalle.getValueAt(i, 0).toString());
            String codigoCuenta = tblDetalle.getValueAt(i, 1).toString();
            BigDecimal valorDebe = new BigDecimal(tblDetalle.getValueAt(i, 3).toString());
            BigDecimal valorHaber = new BigDecimal(tblDetalle.getValueAt(i, 4).toString());

            Cuenta cuenta = negocio.buscarCuenta(codigoCuenta);

            DetComprobante det = new DetComprobante();
            det.setIdDet(idDet);
            det.setCodCuenta(cuenta);
            det.setDebe(valorDebe);
            det.setHaber(valorHaber);

            detalles.add(det);
        }

        int r = negocio.guardarCabeceraDetalle(
                txtNumero.getText(),
                convertirFecha(txtFecha.getText().trim()),
                txtObservaciones.getText().trim(),
                detalles
        );

        if (r == 1) {
            JOptionPane.showMessageDialog(this, "Comprobante guardado correctamente");
            cargarCabeceras();
            nuevo();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar el comprobante");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
         String numero = JOptionPane.showInputDialog(this, "Ingrese el número del comprobante:");

    if (numero != null && !numero.trim().isEmpty()) {
        CabComprobante c = negocio.buscarCabecera(numero.trim());

        if (c != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            txtNumero.setText(c.getNumero());

            if (c.getFecha() != null) {
                txtFecha.setText(sdf.format(c.getFecha()));
            } else {
                txtFecha.setText("");
            }

            txtObservaciones.setText(c.getObservaciones());

            cargarDetalles(c.getNumero());

        } else {
            JOptionPane.showMessageDialog(this, "No existe el comprobante");
        }
    }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        String numero = txtNumero.getText().trim();

    if (numero.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Seleccione o busque un comprobante");
        return;
    }

    int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Seguro que desea eliminar este comprobante?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION
    );

    if (opcion == JOptionPane.YES_OPTION) {
        int r = negocio.eliminarCabecera(numero);

        if (r == 1) {
            JOptionPane.showMessageDialog(this, "Comprobante eliminado correctamente");
            cargarCabeceras();
            nuevo();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar");
        }
    }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void tblDetalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetalleMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDetalleMouseClicked

    private void tblCabeceraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCabeceraMouseClicked
        // TODO add your handling code here:
        int fila = tblCabecera.getSelectedRow();

    if (fila >= 0) {
        String numero = tblCabecera.getValueAt(fila, 0).toString();

        CabComprobante c = negocio.buscarCabecera(numero);

        if (c != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            txtNumero.setText(c.getNumero());

            if (c.getFecha() != null) {
                txtFecha.setText(sdf.format(c.getFecha()));
            } else {
                txtFecha.setText("");
            }

            txtObservaciones.setText(c.getObservaciones());

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
            java.util.logging.Logger.getLogger(MantenimientoComprobante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenimientoComprobante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenimientoComprobante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenimientoComprobante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MantenimientoComprobante().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarDetalle;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cmbCuenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblCabecera;
    private javax.swing.JTable tblDetalle;
    private javax.swing.JTextField txtDebe;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtHaber;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtObservaciones;
    // End of variables declaration//GEN-END:variables
}
