package com.example.firestoresimpleapp.model;

public class modelMhs {

    private String nama, id, foto;

    public modelMhs(String nama, String foto) {
        this.nama = nama;
        this.foto = foto;

    }

    public modelMhs() {
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
