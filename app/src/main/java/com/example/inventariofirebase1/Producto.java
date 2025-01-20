package com.example.inventariofirebase1;

public class Producto {
    private String codigo;
    private String nombre; // Cambiado de nombreProducto a nombre
    private int stock;
    private double precioVenta;
    private double precioCosto; // Nuevo campo para reflejar precioCosto en la base de datos

    public Producto() {
        // Constructor vacío requerido por Firebase
    }

    public Producto(String codigo, String nombre, int stock, double precioVenta, double precioCosto) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.precioVenta = precioVenta;
        this.precioCosto = precioCosto;
    }

    // Getters y setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    public double getPrecioCosto() { return precioCosto; }
    public void setPrecioCosto(double precioCosto) { this.precioCosto = precioCosto; }
}