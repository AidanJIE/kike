package com.example.proyectofinal.models;

public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private double precio;
    private int cantidad;
    private String categoria;
    private boolean disponible;

    // Constructores
    public Libro() {
        // Constructor vacío necesario para la base de datos
    }

    public Libro(int id, String titulo, String autor, String isbn,
                 double precio, int cantidad, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.precio = precio;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.disponible = cantidad > 0;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.disponible = cantidad > 0;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}