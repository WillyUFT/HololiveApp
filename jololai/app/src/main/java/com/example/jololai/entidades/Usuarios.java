package com.example.jololai.entidades;

public class Usuarios {

    private String usuario;
    private int compras_realizadas;
    private String contraseña;

    public String getUsuario(){
        return usuario;
    }

    public void setUsuario(String usuario){
        this.usuario = usuario;
    }

    public int getCompras_realizadas(){
        return compras_realizadas;
    }

    public void setCompras_realizadas(int compras_realizadas){
        this.compras_realizadas = compras_realizadas;
    }

    public String getContraseña(){
        return contraseña;
    }

    public void setContraseña(String contraseña){
        this.contraseña = contraseña;
    }
}
