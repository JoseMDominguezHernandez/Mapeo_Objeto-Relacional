package es.florida.AE05_GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import es.florida.AE05.Libro;

public class Controlador {

	private Modelo modelo;
	private Vista vista;
	private ActionListener actionListenerCatalogo, actionListenerLibro, actionListenerLimpiar,
			actionListenerEditarLibro, actionListenerNuevoLibro, actionListenerBorrar;

	// Constructor pasamos los parametros desde main en principal
	public Controlador(Modelo modelo, Vista vista) {
		this.modelo = modelo;
		this.vista = vista;
		control(); 						
	}

	
	/* METODO:	control()
	 * ACTION: 	Asocia las funciones a los botones de la interfaz gr�fica para que se 
	 * ejecuten las instrucciones al pulsarlos. Inicialmente presenta por defecto el cat�logo 
	 * de libros que hay en ese momento en BDD, marcando la hora de la �ltima vez que se ha efectuado 
	 * una modificaci�n en la BDD. Se puede consultar los detalles de una referencia, modificarla 
	 * o borrarla; tambi�n se puede crear una nueva referencia y guardarla en la BDD.
	 * INPUT: 	seg�n la funci�n recoge los textos de los JTextField para usarlos en las funciones
	 * OUTPUT: 	modifca y presenta los atributos o mensajes seg�n la funci�n que aplique */
	public void control() {

		mostrarCatalogo(); // Mostramos listado de libros en TextArea al abrir la GUI

		// Actualiza el listado de la base de datos que se muestra
		actionListenerCatalogo = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				mostrarCatalogo();
			}
		};
		vista.getBtnCatalogo().addActionListener(actionListenerCatalogo);

		// Detalle libro: presenta los datos de la referencia solicitada en el campo correspondiente
		actionListenerLibro = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				int id = Integer.parseInt(vista.getTextIdConsulta().getText()); // pasa id a integer para poder pasarla a las funciones
				String[] lib = modelo.consultaLibro(id);						//devuelve un Array

				// Si se ha pasado una ID correcta coloca cada posicion del Array en su TextField correspondiente si no, mensaje error
				if (lib[0].equals("Ok")) {			
					vista.getTextTitulo().setText(lib[1]);
					vista.getTextAutor().setText(lib[2]);
					vista.getTextNacimnineto().setText(lib[3]);
					vista.getTextPublicacion().setText(lib[4]);
					vista.getTextEditorial().setText(lib[5]);
					vista.getTextPaginas().setText(lib[6]);
				} else {
					JOptionPane.showMessageDialog(null, "Error. ID inexistente");
				}
			}
		};
		vista.getBtnConsulta().addActionListener(actionListenerLibro);

		
		// Nuevo libro: crea y guarda en la BDD una nueva referencia, actualiza el cat�logo
		actionListenerNuevoLibro = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				
				boolean error = false;
				Libro libro = capturarTexto(0);		// captura texto de los JTextFields. 0 > Objeto sin Id
				
				// Controla si se escribe alg�n campo en formato diferente al de la BDD
				try {
					modelo.nuevoLibro(libro);
				}catch (Exception e){
					JOptionPane.showMessageDialog(null, "�Error!\nNo se ha podido crear la nueva referencia.\nRevise los datos");
					error = true;
				}
				if (error == false) {				// Si da error al crearlo, no ense�a el mensaje ni borra pantalla
					limpiarCampos();
					JOptionPane.showMessageDialog(null,
							"Libro a�adido correctamente.\nNueva referencia:\n\t\"" + libro.getId() + ". " + libro.getTitulo() + "\"");
					mostrarCatalogo();
				}
			}
		};
		vista.getBtnAgregar().addActionListener(actionListenerNuevoLibro);

		
		// Editar libro: edita cualquier atributo del libro directamente en el JTextField luego sustituye esta info en la BDD 
		actionListenerEditarLibro = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				
				boolean error = false;
				Libro libro = capturarTexto(1);		// 1 > Crea objeto con 'id'
				
				try {
					modelo.editarLibro(libro);		// Si da error muestra mensaje y se para dejando modificar y volver a presionar el bot�n
				}catch (Exception e){
					JOptionPane.showMessageDialog(null, "�Error!\nLa informaci�n no se ha podido actualizar.\nRevise los datos");
					error = true;
				}
				if (error == false) {
					limpiarCampos();
					JOptionPane.showMessageDialog(null, "Libro \"" + libro.getId() + ". " + libro.getTitulo() + "\" actualizado correctamente.");
					mostrarCatalogo();
				}
			}
		};
		vista.getBtnActualizar().addActionListener(actionListenerEditarLibro);

		
		// Borrar libro: borra registro de la BDD con solo indicar la ID
		actionListenerBorrar = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				int id = Integer.parseInt(vista.getTextIdConsulta().getText());
				modelo.borrarLibro(id);

				limpiarCampos();
				JOptionPane.showMessageDialog(null, "Libro con id \"" + id + "\" borrado correctamente");
				mostrarCatalogo();
			}
		};
		vista.getBtnBorrar().addActionListener(actionListenerBorrar);

		
		// Limpiar campos: borra todos los campos para dejarlos preparados para escribir
		actionListenerLimpiar = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				limpiarCampos();
			}
		};
		vista.getBtnLimpiar().addActionListener(actionListenerLimpiar);
	}

	
	// FUNCIONES AUXILIARES
	// --------------------
	
	/* METODO: 	capturarTexto()
	 * ACTION:	extrae los valores de los JTextField, los convierte a String y crea un 
	 * objeto libro empleando un constructor diferente en funci�n de la informaci�n que
	 * necesitemos.
	 * INPUT: 	recibe un Integer que indica el constructor a usar; 1 > constructor con 'id' / 2 > constructor sin 'id'
	 * OUTPUT: 	devuelve un objeto Libro creado con la informaci�n delos JTextField de la GUI*/
	public Libro capturarTexto(int indicador) {

		Libro libro;
		String titulo = vista.getTextTitulo().getText().toString();
		String autor = vista.getTextAutor().getText().toString();
		String anyonacimiento = vista.getTextNacimnineto().getText().toString();
		String anyopublicacion = vista.getTextPublicacion().getText().toString();
		String editorial = vista.getTextEditorial().getText().toString();
		String paginas = vista.getTextPaginas().getText().toString();
		if (indicador != 0) {
			int id = Integer.parseInt(vista.getTextIdConsulta().getText());
			libro = new Libro(id, titulo, autor, anyonacimiento, anyopublicacion, editorial, paginas);
		} else
			libro = new Libro(titulo, autor, anyonacimiento, anyopublicacion, editorial, paginas);
		return libro;
	}

	
	/* METODO:	fechaActual()
	 * ACTION: 	obtiene la fecha y hora del momneto en que es invocado
	 * INPUT:	nada
	 * OUTPUT:	String formateado con la fecha y hora actuales*/
	public String fechaActual() {

		LocalDateTime localDate = LocalDateTime.now();
		String fecha = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth() + " "
				+ localDate.getHour() + ":" + localDate.getMinute() + ":" + localDate.getSecond();
		return fecha;
	}

	
	/* METODO: 	mostrarCatalogo()
	 * ACTION:	presenta el contenido de la BDD en el TextArea de la GUI
	 * INPUT:	ArrayList de String con el contenido de la BDD
	 * OUTPUT:	Listado de objetos de la BDD en el TextArea. Fecha y Hora de la �ltima actualizaci�n*/
	public void mostrarCatalogo() {

		vista.getTextArea().setText("\n");
		ArrayList<String> listaLibros = modelo.consultaCatalogo();
		for (String libro : listaLibros) {
			vista.getTextArea().append(" " + libro + "\n");
		}
		vista.getTextArea().append("\n Ultima actualizaci�n:\n\t\t" + fechaActual());
	}
	
	
	/*
	 * METODO:	limpiarCampos() 
	 * ACTION:	borra el contenido de los JTextField escribiendo en blanco en cada uno de ellos(""). 
	 * El campo de ID s�lo lo borra si tiene texto si no, provoca un error. 
	 * INPUT:	nada. 
	 * OUTPUT: 	borra el contenido de los JTextField escribiendo ("") en cada uno
	 */
	public void limpiarCampos() {

		if (!vista.getTextIdConsulta().getText().toString().equals(""))
			vista.getTextIdConsulta().setText("");
		vista.getTextTitulo().setText("");
		vista.getTextAutor().setText("");
		vista.getTextNacimnineto().setText("");
		vista.getTextPublicacion().setText("");
		vista.getTextEditorial().setText("");
		vista.getTextPaginas().setText("");
	}

}