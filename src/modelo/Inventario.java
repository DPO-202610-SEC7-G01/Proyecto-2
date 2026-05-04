package modelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import modelo.producto.Bebida;
import modelo.producto.Juego;
import modelo.producto.Platillo;
import modelo.usuario.Usuario;

public class Inventario {

	private HashMap<Calendar, HashMap<Usuario,Juego>> historialUsoJuegos;// 
	public ArrayList<Juego> juegosVenta; // 
	public ArrayList<Platillo> menuPlatillos; //
	public ArrayList<Bebida> menuBebidas; //
	public ArrayList<Juego> juegosPrestamo; //
	
	
	public Inventario() {
		super();
		this.juegosVenta = new ArrayList<Juego>();
		this.menuBebidas = new ArrayList<Bebida>();
		this.juegosPrestamo = new ArrayList<Juego>();
		this.menuPlatillos = new ArrayList<Platillo>();
		this.historialUsoJuegos = new HashMap<Calendar, HashMap<Usuario, Juego>>();

	}


	public HashMap<Calendar, HashMap<Usuario, Juego>> getHistorialUsoJuegos() {
		return historialUsoJuegos;
	}


	public void setHistorialUsoJuegos(HashMap<Calendar, HashMap<Usuario, Juego>> historialUsoJuegos) {
		this.historialUsoJuegos = historialUsoJuegos;
	}


	public ArrayList<Juego> getJuegosVenta() {
		return juegosVenta;
	}


	public void setJuegosVenta(ArrayList<Juego> juegosVenta) {
		this.juegosVenta = juegosVenta;
	}


	public ArrayList<Platillo> getMenuPlatillos() {
		return menuPlatillos;
	}


	public void setMenuPlatillos(ArrayList<Platillo> menuPlatillos) {
		this.menuPlatillos = menuPlatillos;
	}


	public ArrayList<Bebida> getMenuBebidas() {
		return menuBebidas;
	}


	public void setMenuBebidas(ArrayList<Bebida> menuBebidas) {
		this.menuBebidas = menuBebidas;
	}


	public ArrayList<Juego> getJuegosPrestamo() {
		return juegosPrestamo;
	}


	public void setJuegosPrestamo(ArrayList<Juego> juegosPrestamo) {
		this.juegosPrestamo = juegosPrestamo;
	}
	
	
	
	
}
