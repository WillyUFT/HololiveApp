package com.example.jololai.entidades;

public class Conciertos {

    private int ID_Concierto;
    private String Nombre_Concierto;
    private int ID_Idol;
    private String Fecha_Concierto;
    private int Duracion_Minutos;

    public int getID_Concierto() {
        return ID_Concierto;
    }

    public void setID_Concierto(int ID_Concierto) {
        this.ID_Concierto = ID_Concierto;
    }

    public String getNombre_Concierto() {
        return Nombre_Concierto;
    }

    public void setNombre_Concierto(String Nombre_Concierto) {
        this.Nombre_Concierto = Nombre_Concierto;
    }

    public int getID_Idol() {
        return ID_Idol;
    }

    public void setID_Idol(int ID_Idol) {this.ID_Idol = ID_Idol; }

    public String getFecha_Concierto() {
        return Fecha_Concierto;
    }

    public void setFecha_Concierto(String Fecha_Concierto) {
        this.Fecha_Concierto = Fecha_Concierto;
    }

    public Integer getDuracion_Minutos() {
        return Duracion_Minutos;
    }

    public void setDuracion_Minutos(Integer Duracion_Minutos) {
        this.Duracion_Minutos = Duracion_Minutos;
    }
}