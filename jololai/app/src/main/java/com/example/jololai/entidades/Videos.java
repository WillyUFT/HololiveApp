package com.example.jololai.entidades;

public class Videos {

    private int id;
    private String nombreVideo;
    private String genero_video;
    private String promocional;
    private byte[] imagen_video;
    private String link;
    private int id_idol;
    private String imagenVideoString;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreVideo() {
        return nombreVideo;
    }

    public void setNombreVideo(String nombreVideo) {
        this.nombreVideo = nombreVideo;
    }

    public String getGenero_video() {
        return genero_video;
    }

    public void setGenero_video(String genero_video) {
        this.genero_video = genero_video;
    }

    public String getPromocional() {
        return promocional;
    }

    public void setPromocional(String promocional) {
        this.promocional = promocional;
    }

    public Integer getId_idol() {
        return id_idol;
    }

    public void setId_idol(Integer id_idol) {
        this.id_idol = id_idol;
    }

    public byte[] getImagen_video() {
        return imagen_video;
    }

    public void setImagen_video(byte[] imagen_video) {
        this.imagen_video = imagen_video;
    }

    public String getLink(){
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImagenVideoString() {
        return imagenVideoString;
    }

    public void setImagenVideoString(String imagenVideoString) {
        this.imagenVideoString = imagenVideoString;
    }
}
