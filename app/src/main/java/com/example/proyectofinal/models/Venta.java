package com.example.proyectofinal.models;

public class Venta {

    private int id;
    private String tituloLibro;
    private int cantidad;
    private double precioUnitario;
    private String fecha;
    private String cliente;

    public Venta() {
    }

    public Venta(int id, String tituloLibro, int cantidad,
                 double precioUnitario, String fecha, String cliente) {
        this.id = id;
        this.tituloLibro = tituloLibro;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.fecha = fecha;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    // 🔥 ESTE MÉTODO SALVA TU APP
    public double getTotal() {
        return cantidad * precioUnitario;
    }
}
