package Controlador;

import Vista.vAsistenciav2;
import Modelo.DatabaseConnection;
import Vista.General;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

public class cAsistencia {

    private DefaultTableModel modelo;
    private vAsistenciav2 vista;
    private DatabaseConnection dbConnection;
    private static final String HORA_LIMITE = "08:00:00"; 
    public cAsistencia(vAsistenciav2 vista) {
        this.vista = vista;
        this.dbConnection = new DatabaseConnection();
        this.modelo = new DefaultTableModel();
        configurarTabla();
        llenarTabla();
        agregarListeners();
        agregarAcciones();
        inicializarFechaHora();
    }
    

    private void configurarTabla() {
        modelo.addColumn("ID Empleado");
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora");
        modelo.addColumn("Estado");
        modelo.addColumn("Descripción");
        vista.tabla.setModel(modelo);
        
        vista.tabla.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Cambiar el color de fondo basado en el estado
            if (column == 3) { // Columna "Estado"
                String estado = (String) value;
                if ("A tiempo".equals(estado)) {
                   
                    c.setForeground(Color.GREEN);
                } else if ("Retardo".equals(estado)) {
                  
                    c.setForeground(Color.RED);
                } 
            }
            return c;
        }
    });
    }
   private void determinarEstadoYGuardarAsistencia() {
    try {
        int idEmpleado = Integer.parseInt(vista.txtIDEmpleado.getText());
        Date fecha = vista.txtFecha.getDate();
        java.sql.Time hora = new java.sql.Time(new Date().getTime()); // Hora actual
        String estado = calcularEstado(hora); // Calcular estado basado en la hora límite
        String descripcion = vista.txtdescripcion.getText();

        String query = "INSERT INTO asistencia (idempleado, fecha, hora, estado, descripcion) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.conectar();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idEmpleado);
            ps.setDate(2, new java.sql.Date(fecha.getTime()));
            ps.setTime(3, hora);
            ps.setString(4, estado);
            ps.setString(5, descripcion);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(vista, "Asistencia registrada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            llenarTabla();
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(vista, "ID de empleado inválido", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private String calcularEstado(java.sql.Time horaActual) {
    String horaLimiteStr = "08:00:00";
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    try {
        java.util.Date horaLimiteDate = sdf.parse(horaLimiteStr);
        java.util.Date horaActualDate = new java.util.Date(horaActual.getTime());
        
        if (horaActualDate.before(horaLimiteDate)) {
            return "A tiempo";
        } else {
            return "Retardo";
        }
    } catch (Exception e) {
        e.printStackTrace();
        return "Desconocido";
    }
}
    private void llenarTabla() {
        String query = "SELECT * FROM asistencia";
        try (Connection conn = dbConnection.conectar();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            modelo.setRowCount(0); // Limpiar tabla antes de llenar
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("idempleado");
                fila[1] = rs.getDate("fecha");
                fila[2] = rs.getTime("hora");
                fila[3] = rs.getString("estado");
                fila[4] = rs.getString("descripcion");
                modelo.addRow(fila);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private void insertarAsistencia() {
        int idEmpleado = Integer.parseInt(vista.txtIDEmpleado.getText());
        Date fecha = vista.txtFecha.getDate();
        java.sql.Time hora = new java.sql.Time(new Date().getTime()); // Hora actual
        String estado = vista.cboEstado.getSelectedItem().toString();
        String descripcion = vista.txtdescripcion.getText();

        String estadoFinal = verificarEstado(hora);
        
        String query = "INSERT INTO asistencia (idempleado, fecha, hora, estado, descripcion) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.conectar();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idEmpleado);
            ps.setDate(2, new java.sql.Date(fecha.getTime()));
            ps.setTime(3, hora);
            ps.setString(4, estado);
            ps.setString(5, descripcion);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(vista, "Asistencia registrada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            llenarTabla();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void actualizarAsistencia() {
        int idEmpleado = Integer.parseInt(vista.txtIDEmpleado.getText());
        Date fecha = vista.txtFecha.getDate();
        java.sql.Time hora = new java.sql.Time(new Date().getTime()); // Hora actual
        String estado = vista.cboEstado.getSelectedItem().toString();
        String descripcion = vista.txtdescripcion.getText();

        String estadoFinal = verificarEstado(hora);
        String query = "UPDATE asistencia SET fecha=?, hora=?, estado=?, descripcion=? WHERE idempleado=?";

        try (Connection conn = dbConnection.conectar();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDate(1, new java.sql.Date(fecha.getTime()));
            ps.setTime(2, hora);
            ps.setString(3, estado);
            ps.setString(4, descripcion);
            ps.setInt(5, idEmpleado);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(vista, "Asistencia actualizada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            llenarTabla();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String verificarEstado(java.sql.Time hora) {
        // Definir el límite de hora
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        String horaActualStr = formatoHora.format(hora);
        
        if (horaActualStr.compareTo(HORA_LIMITE) <= 0) {
            return "A tiempo";
        } else {
            return "Retardo";
        }
    }

    private void agregarListeners() {
        vista.tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && vista.tabla.getSelectedRow() != -1) {
                    int filaSeleccionada = vista.tabla.getSelectedRow();
                    vista.txtIDEmpleado.setText(vista.tabla.getValueAt(filaSeleccionada, 0).toString());
                    vista.txtFecha.setDate((java.util.Date) vista.tabla.getValueAt(filaSeleccionada, 1));
                    vista.txtHora.setText(vista.tabla.getValueAt(filaSeleccionada, 2).toString());
                    vista.cboEstado.setSelectedItem(vista.tabla.getValueAt(filaSeleccionada, 3).toString());
                    vista.txtdescripcion.setText(vista.tabla.getValueAt(filaSeleccionada, 4).toString());
                }
            }
        });
    }

    private void agregarAcciones() {
        vista.btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarAsistencia();
            }
        });

       

        vista.btnInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //insertarAsistencia();
                determinarEstadoYGuardarAsistencia();
            }
        });

        vista.btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarAsistencia();
            }
        });
        
        vista.txtIDEmpleado.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //insertarAsistencia(); // True para inserción
                    determinarEstadoYGuardarAsistencia(); 
                }
                
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
    private void inicializarFechaHora() {
        // Inicializar el JDateChooser con la fecha actual
        vista.txtFecha.setDate(new Date());
        
        // Actualizar el JLabel con la hora actual
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        vista.txtHora.setText(formatoHora.format(new Date()));
        
        // Actualizar el JLabel cada segundo
        new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vista.txtHora.setText(formatoHora.format(new Date()));
            }
        }).start();
    }
    private void buscarAsistencia() {
        String textoBuscar = vista.txtBuscar.getText();
        DefaultTableModel modelo = (DefaultTableModel) vista.tabla.getModel();
        modelo.setRowCount(0);

        String query = "SELECT * FROM asistencia WHERE idempleado = ?";

        try (Connection conn = dbConnection.conectar();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, Integer.parseInt(textoBuscar));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("idempleado"),
                    rs.getDate("fecha"),
                    rs.getTime("hora"),
                    rs.getString("estado"),
                    rs.getString("descripcion")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
}
