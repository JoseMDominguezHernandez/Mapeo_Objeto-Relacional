package es.florida.AE05_GUI;

public class Principal {

	public static void main(String[] args) {
		
		Vista vista = new Vista();
		Modelo modelo = new Modelo();
		Controlador controlador = new Controlador(modelo, vista);
	}
}