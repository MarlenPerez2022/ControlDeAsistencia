/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author gmarl
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection con;
    
    private static final String URL = "jdbc:mysql://localhost:3306/gestionpersonal";
    private static final String USER = "root";
    private static final String PASSWORD = "123456789";

   public DatabaseConnection(){}
    public Connection conectar(){
        try {
            con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("La conexion a la base de datos fue un exito");
        } catch (Exception e) {
            System.out.println("Error en la conexion de base de datos" + e);
        }
        return con;
    }   
}
