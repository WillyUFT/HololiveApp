package com.example.jololai.entidades;


public class Canciones {

    private int id;
    private String nombre;
    private String tipo_cancion;
    private String letra_cancion;
    private byte[] imagen_cancion;
    private int id_idol;
    private String imagen_cancionString;

    public Canciones() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo_cancion() {
        return tipo_cancion;
    }

    public void setTipo_cancion(String tipo_cancion) {
        this.tipo_cancion = tipo_cancion;
    }

    public String getLetra_cancion() {
        return letra_cancion;
    }

    public void setLetra_cancion(String letra_cancion) {
        this.letra_cancion = letra_cancion;
    }

    public Integer getId_idol() {
        return id_idol;
    }

    public void setId_idol(Integer id_idol) {
        this.id_idol = id_idol;
    }

    public byte[] getImagen_cancion() {
        return imagen_cancion;
    }

    public void setImagen_cancion(byte[] imagen_cancion) {
        this.imagen_cancion = imagen_cancion;
    }

    public String getImagen_cancionString() {
        return imagen_cancionString;
    }

    public void setImagen_cancionString(String imagen_cancionString) {
        this.imagen_cancionString = imagen_cancionString;
    }
}


