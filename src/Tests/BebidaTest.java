package Tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.*;
import modelo.producto.*;

class BebidaTest {
	
	private  static final int ID_BEBIDA = 001;
    private static final String NOMBRE_BEBIDA= "Burunganda";
    private static final int precio_ = 7999;
    private static final String TEMPERATURA= "Fría";
    private static final boolean ALCOHOL= true;

    
 // Constantes para pruebas de temperatura inválida
    private static final String TEMPERATURA_INVALIDA = "Tibio";
    private static final String TEMPERATURA_NUMEROS = "123";
    private static final String TEMPERATURA_SIMBOLOS = "¿?";
    private static final String TEMPERATURA_ESPACIOS = "   ";
    
    private Bebida bebidaFriaSinAlcohol;
    private Bebida bebidaCalienteConAlcohol;
    private Bebida bebidaPorDefecto;
    
    @BeforeEach
    void setUp() throws ProductosException {
        bebidaFriaSinAlcohol = new Bebida(ID_BEBIDA, precio_, 
        		NOMBRE_BEBIDA, TEMPERATURA, false);
        
        bebidaCalienteConAlcohol = new Bebida(ID_BEBIDA + 1, precio_, 
            "Café Caliente", "Caliente", ALCOHOL);
        
        bebidaPorDefecto = new Bebida(ID_BEBIDA + 2, precio_, 
            "Bebida Default");
    }
    
    

}
