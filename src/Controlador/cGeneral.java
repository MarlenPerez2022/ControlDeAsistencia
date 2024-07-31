/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Vista.General;
import Vista.Login;
import Vista.vAsistenciav2;
import Vista.vDepartamento;
import Vista.vEmpleado;
import Vista.vTurno;
import Vista.vUsuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author gmarl
 */
public class cGeneral {
    private General vista;
    private String usuarioCorreo;

    public cGeneral(General vista, String usuarioCorreo) {
        this.vista = vista;
        this.usuarioCorreo = usuarioCorreo;

        // Configurar el texto del label lblUsuarioTipo
        this.vista.lblUsuarioTipo.setText(usuarioCorreo);

        // Configurar eventos para los botones
        this.vista.btnAsistencia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new vAsistenciav2().setVisible(true);
                 vista.dispose();
            }
        });

        this.vista.btnDepartamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new vDepartamento().setVisible(true);
                 vista.dispose();
            }
        });

        this.vista.btnTurno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new vTurno().setVisible(true);
                 vista.dispose();
            }
        });

        this.vista.btnRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new vUsuario().setVisible(true);
                 vista.dispose();
            }
        });

        this.vista.btnEmpleado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new vEmpleado().setVisible(true);
                 vista.dispose();
            }
        });

        this.vista.btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 new Login().setVisible(true);
                 vista.dispose();
                 
            }
        });
    }
}


   

