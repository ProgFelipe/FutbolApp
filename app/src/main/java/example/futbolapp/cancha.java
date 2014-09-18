package example.futbolapp;

import java.io.File;

/**
 * Created by Usuario on 12/09/2014.
 */
public class cancha {

    private String nombre;
    private String descripcion;
    private int telefono;
    private double latitud;
    private double longitud;
    private File marcador;

    public cancha(String nombre, double latitud, double longitud, String descripcion, int telefono, File marcador){

        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.marcador = marcador;
    }

    public void comentar(String comentario)
    {

    }
}
