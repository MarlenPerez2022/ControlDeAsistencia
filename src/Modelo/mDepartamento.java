/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author gmarl
 */
public class mDepartamento {
  private int iddepartamento;
    private String nombre;
    private String descripcion;

    public mDepartamento() {
    }

    public mDepartamento(int iddepartamento, String nombre, String descripcion) {
        this.iddepartamento = iddepartamento;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIddepartamento() {
        return iddepartamento;
    }

    public void setIddepartamento(int iddepartamento) {
        this.iddepartamento = iddepartamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
