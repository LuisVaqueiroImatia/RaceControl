package racecontrol;

import java.io.Serializable;

public class Coche implements Serializable, Comparable<Coche> {

    private String id;
    private String marca;
    private String modelo;
    private String escuderia;

    // Solo se usará en carreras, una vez finalizada la carrera, poner a 0
    private int distanciaPercorridaKm;

    // solo usado en torneos, vuelve a 0 al finalizar el torneo
    private int puntos;

    public Coche() {
        puntos = 0;
        distanciaPercorridaKm = 0;
    }

    public Coche(String id, String marca, String modelo, String escuderia) {

        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.escuderia = escuderia;
        puntos = 0;
        distanciaPercorridaKm = 0;

    }

    // GS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEscuderia() {
        return escuderia;
    }

    public void setEscuderia(String escuderia) {
        this.escuderia = escuderia;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getDistanciaPercorridaKm() {
        return distanciaPercorridaKm;
    }

    public void setDistanciaPercorridaKm(int distanciaPercorrida) {
        this.distanciaPercorridaKm = distanciaPercorrida;
    }

    @Override
    public String toString() {
        return "Coche [id=" + id + ", marca=" + marca + ", modelo=" + modelo + ", escuderia=" + escuderia + "]";
    }

    /*
     Para poder comparar las distancias percorridas    
     */
    @Override
    public int compareTo(Coche that) {
        return Integer.compare(that.distanciaPercorridaKm, this.distanciaPercorridaKm);
    }

}
