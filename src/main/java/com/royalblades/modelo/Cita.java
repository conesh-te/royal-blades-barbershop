package com.royalblades.modelo;

public class Cita {
    private int idCita;
    private String nombreCliente;
    private String correoCliente;
    private String telefonoCliente;
    private String nombreBarbero;
    private String nombreServicio;
    private String fechaHora;
    private boolean asignacionManual;
    private String estado;
    private double montoCobrado;
    private double comisionBarbero;

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }

    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }

    public String getNombreBarbero() { return nombreBarbero; }
    public void setNombreBarbero(String nombreBarbero) { this.nombreBarbero = nombreBarbero; }

    public String getNombreServicio() { return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public boolean isAsignacionManual() { return asignacionManual; }
    public void setAsignacionManual(boolean asignacionManual) { this.asignacionManual = asignacionManual; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getMontoCobrado() { return montoCobrado; }
    public void setMontoCobrado(double montoCobrado) { this.montoCobrado = montoCobrado; }

    public double getComisionBarbero() { return comisionBarbero; }
    public void setComisionBarbero(double comisionBarbero) { this.comisionBarbero = comisionBarbero; }
}
