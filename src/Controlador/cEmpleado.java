package Controlador;

import Vista.vEmpleado;
import Modelo.DatabaseConnection;
import Vista.General;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class cEmpleado {
    private DefaultTableModel modelo;
    private vEmpleado vista;
    private DatabaseConnection dbConnection;

    public cEmpleado(vEmpleado vista) {
        this.vista = vista;
        this.dbConnection = new DatabaseConnection();
        this.modelo = new DefaultTableModel();
        configurarTabla();
        cargarTabla();
        agregarListeners();
        agregarAcciones();
    }

    private void configurarTabla() {
       
        modelo.addColumn("Nombres");
        modelo.addColumn("Apellidos");
        modelo.addColumn("Salario");
       // modelo.addColum("Departamento");
        vista.tabla.setModel(modelo);
    }

    private void cargarTabla() {
        try (Connection conn = dbConnection.conectar();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM empleado");
             ResultSet rs = ps.executeQuery()) {

            modelo.setRowCount(0); // Limpiar tabla antes de llenar
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getInt("idempleado");
                fila[1] = rs.getString("nombres");
                fila[2] = rs.getString("apellidos");
                fila[3] = rs.getString("salario");
               
                modelo.addRow(fila);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarEmpleado() {
        String nombres = vista.txtNombres.getText();
        String apellidos = vista.txtApellidos.getText();
        String salario = vista.txtSalario.getText();
        

        String query = "INSERT INTO empleado (nombres, apellidos, salario, idturno, iddepartamento, idusuario) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.conectar();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, nombres);
            ps.setString(2, apellidos);
            ps.setString(3, salario);
           
            ps.executeUpdate();

            JOptionPane.showMessageDialog(vista, "Empleado insertado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void actualizarEmpleado() {
        int idEmpleado = Integer.parseInt(vista.txtIdEmpleado.getText());
        String nombres = vista.txtNombres.getText();
        String apellidos = vista.txtApellidos.getText();
        String salario = vista.txtSalario.getText();
        

        String query = "UPDATE empleado SET nombres = ?, apellidos = ?, salario = ?, idturno = ?, iddepartamento = ?, idusuario = ? WHERE idempleado = ?";

        try (Connection conn = dbConnection.conectar();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, nombres);
            ps.setString(2, apellidos);
            ps.setString(3, salario);
            ps.setInt(4, idEmpleado);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(vista, "Empleado actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarEmpleado() {
        int idEmpleado = Integer.parseInt(vista.txtIdEmpleado.getText());

        String query = "DELETE FROM empleado WHERE idempleado = ?";

        try (Connection conn = dbConnection.conectar();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idEmpleado);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(vista, "Empleado eliminado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void buscarEmpleado() {
        String textoBuscar = vista.txtBuscar.getText();
        DefaultTableModel modelo = (DefaultTableModel) vista.tabla.getModel();
        modelo.setRowCount(0);

        String query = "SELECT * FROM empleado WHERE nombres LIKE ? OR apellidos LIKE ?";

        try (Connection conn = dbConnection.conectar();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + textoBuscar + "%");
            ps.setString(2, "%" + textoBuscar + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("idempleado"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("salario"),
                    rs.getInt("idturno"),
                    rs.getInt("iddepartamento"),
                    rs.getInt("idusuario")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void agregarListeners() {
        vista.tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && vista.tabla.getSelectedRow() != -1) {
                    int filaSeleccionada = vista.tabla.getSelectedRow();
                    vista.txtIdEmpleado.setText(vista.tabla.getValueAt(filaSeleccionada, 0).toString());
                    vista.txtNombres.setText(vista.tabla.getValueAt(filaSeleccionada, 1).toString());
                    vista.txtApellidos.setText(vista.tabla.getValueAt(filaSeleccionada, 2).toString());
                    vista.txtSalario.setText(vista.tabla.getValueAt(filaSeleccionada, 3).toString());
                    
                }
            }
        });
    }

    private void agregarAcciones() {
        vista.btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarEmpleado();
            }
        });

        vista.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarEmpleado();
            }
        });

        vista.btnInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertarEmpleado();
            }
        });

        vista.btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarEmpleado();
            }
        });
        vista.btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new General().setVisible(true);
                vista.dispose();
            }
        });
    }
}
