package com.example.jololai.entidades;

public class ComConcierto {

    private int id;
    private String nombre_usuario;
    private int id_concierto;
    private int asistentes;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public int getId_concierto() {
        return id_concierto;
    }

    public void setId_concierto(int id_concierto) {
        this.id_concierto = id_concierto;
    }
}
