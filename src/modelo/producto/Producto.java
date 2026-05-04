package modelo.producto;

//exceptions
import exceptions.*;

public abstract class Producto {
    private int id;
    private int precio;
    private String nombre;
    public final double IVA = 0.19;
    public final double IMPUESTOCONSUMO = 0.08;

    public Producto(int id, int precio, String nombre) throws ProductosException {
        if (id <= 0) {
            throw new ProductosException(this, "id", 
                "El ID del producto debe ser positivo. Valor recibido: " + id);
        }
        this.id = id;
        
      
        if (precio <= 0) {
            throw new ProductosException(this, "precio", 
                "El precio del producto debe ser positivo. Valor recibido: " + precio);
        }
        this.precio = precio;
        
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ProductosException(this, "nombre", 
                "El nombre del producto no puede estar vacío.");
        }
        if (nombre.matches(".*\\d.*")) {
            throw new ProductosException(this, "nombre", 
                "El nombre del producto no puede contener números. Valor recibido: '" + nombre + "'");
        }
        this.nombre = nombre;
    }
    
    // Getters y Setters con validaciones
    public int getId() {
        return id;
    }
    
    public void setId(int id) throws ProductosException {
        if (id <= 0) {
            throw new ProductosException(this, "id", 
                "El ID del producto debe ser positivo. Valor recibido: " + id);
        }
        this.id = id;
    }
    
    public int getPrecio() {
        return precio;
    }
    
    public void setPrecio(int precio) throws ProductosException {
        if (precio <= 0) {
            throw new ProductosException(this, "precio", 
                "El precio del producto debe ser positivo. Valor recibido: " + precio);
        }
        this.precio = precio;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) throws ProductosException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ProductosException(this, "nombre", 
                "El nombre del producto no puede estar vacío.");
        }
        if (nombre.matches(".*\\d.*")) {
            throw new ProductosException(this, "nombre", 
                "El nombre del producto no puede contener números. Valor recibido: '" + nombre + "'");
        }
        this.nombre = nombre;
    }
    
    // Métodos
    public abstract double getTasaImpuesto();
    
    public double calcularPrecioFinal() {
        return this.precio * (1 + getTasaImpuesto());
    }
}
