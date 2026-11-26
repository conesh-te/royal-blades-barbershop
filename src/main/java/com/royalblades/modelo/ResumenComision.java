package com.royalblades.modelo;

public class ResumenComision {
    private String nombreBarbero;
    private int citasAtendidas;
    private double totalGenerado;
    private double comisionBarbero;

    public String getNombreBarbero() { return nombreBarbero; }
    public void setNombreBarbero(String nombreBarbero) { this.nombreBarbero = nombreBarbero; }

    public int getCitasAtendidas() { return citasAtendidas; }
    public void setCitasAtendidas(int citasAtendidas) { this.citasAtendidas = citasAtendidas; }

    public double getTotalGenerado() { return totalGenerado; }
    public void setTotalGenerado(double totalGenerado) { this.totalGenerado = totalGenerado; }

    public double getComisionBarbero() { return comisionBarbero; }
    public void setComisionBarbero(double comisionBarbero) { this.comisionBarbero = comisionBarbero; }
}
