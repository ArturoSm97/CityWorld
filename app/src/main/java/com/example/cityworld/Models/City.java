package com.example.cityworld.Models;

import com.example.cityworld.App.MyApp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class City extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private String nombre;
    @Required
    private String description;
    @Required
    private String link;
    private float puntuacion;

    public City(){}
    public City(String nombre, String description, String link, float puntuacion){
        this.id = MyApp.CityId.incrementAndGet();
        this.nombre = nombre;
        this.description = description;
        this.link = link;
        this.puntuacion = puntuacion;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public float getPuntuacion() {
        return puntuacion;
    }
}
