package com.royalblades.modelo;

public class Barbero {
    private int idBarbero;
    private int idUsuario;
    private String nombreCompleto;
    private String correo;
    private boolean enTurno;
    private double porcentajeComision;

    public int getIdBarbero() { return idBarbero; }
    public void setIdBarbero(int idBarbero) { this.idBarbero = idBarbero; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public boolean isEnTurno() { return enTurno; }
    public void setEnTurno(boolean enTurno) { this.enTurno = enTurno; }

    public double getPorcentajeComision() { return porcentajeComision; }
    public void setPorcentajeComision(double porcentajeComision) { this.porcentajeComision = porcentajeComision; }
}
