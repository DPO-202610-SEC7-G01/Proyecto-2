package consola.interfaz;

import modelo.Cafe;
import modelo.producto.Producto;
import modelo.Transaccion;
import modelo.usuario.Cliente;
import modelo.usuario.Empleado;
import modelo.usuario.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

abstract class ConsolaAbstract {
    static protected Cafe miCafe;
    protected Scanner lector;
    protected Random aleatorio;

    public ConsolaAbstract(Cafe cafe) {
        ConsolaAbstract.miCafe = cafe;
        this.lector = new Scanner(System.in);
        this.aleatorio = new Random();
    }
    public Usuario buscarUsuario(String login) {
        for (Cliente c : miCafe.getClientes()) {
            if (c.getLogin().equals(login))
                return c;
        }
        for (Empleado e : miCafe.getEmpleados()) {
            if (e.getLogin().equals(login))
                return e;
        }
        if(login.equals(miCafe.getAdmin().getLogin())) {
            return miCafe.getAdmin();
        }
        return null;
    }
    
    public ArrayList<String> leerAlergenos(String alergenos) {
        if (alergenos.isBlank()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(alergenos.split("\\s*,\\s*")));
    }

    protected int leerEntero(String mensaje) {
        System.out.print(mensaje);
        int valor = lector.nextInt();
        lector.nextLine();
        return valor;
    }

    protected boolean mismoDia(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }

    protected boolean clienteEsAmigoDeAlgunEmpleado(Cliente cliente) {
        for (Empleado empleado : miCafe.getEmpleados()) {
            if (empleado.verificarSiEsAmigo(cliente)) {
                return true;
            }
        }
        return false;
    }

    protected void agregarProductoACarrito(List<? extends Producto> lista, List<Producto> carrito) {
        if (lista.isEmpty()) {
            System.out.println("No hay productos en esta categoria.");
            return;
        }

        for (int i = 0; i < lista.size(); i++) {
            Producto p = lista.get(i);
            System.out.println(i + ". " + p.getNombre() + " ($" + p.getPrecio() + ")");
        }

        int sel = leerEntero("Seleccione el numero del producto para agregar (o -1 para volver): ");

        if (sel >= 0 && sel < lista.size()) {
            carrito.add(lista.get(sel));
            System.out.println(lista.get(sel).getNombre() + " anadido al carrito.");
        }
    }

    protected void imprimirFactura(Transaccion t, Usuario u) {
        String verde = "\u001B[32m";
        String cursiva = "\u001B[3m";
        String reset = "\u001B[0m";

        System.out.println("\n========================================");
        System.out.println("           FACTURA DE VENTA           ");
        System.out.println("          ID: " + t.getId());
        System.out.println("========================================");
        System.out.println("Fecha: " + t.getFecha().getTime());
        System.out.println("Cliente: " + u.getNombre());
        System.out.println("----------------------------------------");

        double subtotalNeto = 0;
        double totalImpuestos = 0;

        for (Producto p : t.getProductos()) {
            double precioBase = p.getPrecio();
            double tasa = p.getTasaImpuesto();
            double impuestoProducto = precioBase * tasa;

            System.out.printf("- %-18s | $%d (Imp: %.0f%%)\n", p.getNombre(), (int) precioBase, tasa * 100);

            subtotalNeto += precioBase;
            totalImpuestos += impuestoProducto;
        }

        double totalConImpuestos = subtotalNeto + totalImpuestos;
        int totalPagar = t.calcularTotal();
        double ahorro = totalConImpuestos - totalPagar;

        System.out.println("----------------------------------------");
        System.out.println("Subtotal (Base):     $" + (int) subtotalNeto);
        System.out.println("Total Impuestos:     $" + (int) totalImpuestos);

        if (ahorro > 0) {
            System.out.println(cursiva + "Ahorro aplicado:    -$" + (int) ahorro + reset);
        }

        System.out.println("----------------------------------------");
        System.out.println(verde + "TOTAL A PAGAR:       $" + totalPagar + reset);
        System.out.println("========================================\n");
    }

    abstract void registrarUsuarioNuevo();
    abstract Usuario autenticarUsuario();
}
