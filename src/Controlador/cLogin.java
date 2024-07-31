/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Vista.Login;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import Modelo.DatabaseConnection;
import Modelo.mUsuario;
import Vista.General;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author gmarl
 */
public class cLogin {
    private Login vista;
    private DatabaseConnection dbConnection;
    private mUsuario usuario;

    public cLogin(Login vista) {
        this.vista = vista;
        this.dbConnection = new DatabaseConnection();

        // Configurar eventos
        this.vista.btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             new General().setVisible(true);
             vista.dispose();
            }
        });

        this.vista.btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.vista.mtrContraseña.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePasswordVisibility();
            }
        });

        this.vista.jmiRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(vista, "Registrar", "Registro", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    private void login() {
    String correo = vista.txtCorreoElec.getText();
    String contraseña = new String(vista.Contraseña.getPassword());

    try (Connection conn = dbConnection.conectar();
         PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM usuario WHERE correo = ? AND contra = ?")) {
        ps.setString(1, correo);
        ps.setString(2, contraseña);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            mUsuario usuario = new mUsuario(
                rs.getInt("idusuario"),
                rs.getString("contra"),
                rs.getString("correo"),
                rs.getString("telefono")
            );
            JOptionPane.showMessageDialog(vista, "Bienvenido", "Inicio de Sesión", JOptionPane.INFORMATION_MESSAGE);
            // Aquí puedes utilizar el objeto `usuario` para otras acciones
        } else {
            JOptionPane.showMessageDialog(vista, "Correo o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

     private void togglePasswordVisibility() {
        JCheckBox checkBox = vista.mtrContraseña;
        JPasswordField passwordField = vista.Contraseña;
        if (checkBox.isSelected()) {
            passwordField.setEchoChar((char) 0); // Muestra la contraseña
        } else {
            passwordField.setEchoChar('*'); // Oculta la contraseña
        }
    }

    
}
