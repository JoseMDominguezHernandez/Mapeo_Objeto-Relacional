package es.florida.AE05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class App {

	static String titulo, autor, anyonacimiento, anyopublicacion, editorial, paginas;
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	
	/*METODO: main()
	 * ACTION: 	Despliega el menú de opciones y según la opción que elijamos ejecuta las instrucciones.
	 * Las opciones están dentro de un bucle que muestra el menú tras finalizar cada tarea; cada vez que 
	 * ejecutamos una tarea y para no perder informacion en caso de error de introduccion se hace commit y 
	 * se cierra la sesion, creando una sesion nueva antes de volver a mostrar el menú de opciones de nuevo.
	 * INPUT: 	según opción recoge por consola la opcion, los atributos del objeto a editar o crear o id 
	 * del objeto según la opción que elijamos.
	 * OUTPUT: 	información por consola sobre las operaciones efectuadas con los objetos de la BBD, objetos 
	 * añadidos, editados o eliminados.    */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws IOException {

		String[] menu = { 
				" 1. Mostrar catálogo",
				" 2. Información libro", 
				" 3. Crear un libro", 
				" 4. Actualizar libro",
				" 5. Borrar libro", 
				" 6. Cerrar" 
				};

		int opcion = 0;
		boolean flag = false;
		while (flag == false) {

			Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
			configuration.addClass(Libro.class);
			ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			SessionFactory sessionFactory = configuration.buildSessionFactory(registry);

			Session session = sessionFactory.openSession();
			session.beginTransaction();
			
			// Presenta Menú
			System.out.println("\n***********************\n    MENU BIBLIOTECA\n***********************");
			
			for (int i = 0; i < menu.length; i++) {
				System.out.println(menu[i]);
			}
			
			System.out.println("\n***********************");
			System.out.print(" Indique una opción: ");
			
			try {
				opcion = Integer.parseInt(br.readLine());

			} catch (NumberFormatException e) {
				System.out.println(" Error. Debe introducir un número");
			}

			// Condiciones para ejecutar instrucciones según opción elegida
			// Consultar todos los títulos de la biblioteca
			if (opcion == 1) {

				List<Libro> listaLibros = new ArrayList();
				listaLibros = session.createQuery("FROM Libro").list();
				System.out.println("\n\n Catálogo biblioteca:\n --------------------");
				for (Object obj : listaLibros) {
					Libro lib = (Libro) obj;
					System.out.println(" " + lib.getId() + ". " + lib.getTitulo());
				}
				System.out.println("\n");

					
			// Consulta completa (todos los atributos de un objeto) por 'id'
			} else if (opcion == 2) {

				System.out.print("\n Indique la id del libro a consultar: ");
				int id;
				try {
					id = Integer.parseInt(br.readLine());
					Libro libro = (Libro) session.get(Libro.class, id);
					
					if (libro == null) System.out.println(" Error. ID inexistente. Indique una 'Id' valida\n");
					else System.out.println(" Detalle libro con Id " + id + ":\n ------------------------" + libro.getLibroText());

				} catch (NumberFormatException e) {
					System.out.println(" Error. Debe introducir un número");
				}
					
				
			// Crear una nueva entrada
			} else if (opcion == 3) {

				System.out.println("\n Introduzca los datos del nuevo libro:");
				insertarDato();

				Libro lib = new Libro(titulo, autor, anyonacimiento, anyopublicacion, editorial, paginas);
				Serializable id = session.save(lib);
				System.out.println("\n Creado nuevo registro con Id: " + id + "\n");

				
			// Editar atributos por 'id'. Si pulsamos 'Enter' y dejamos vacío toma el dato original
			} else if (opcion == 4) {

				System.out.print("\n Indique la id del libro que desea editar: ");
				try {
					int id;
					id = Integer.parseInt(br.readLine());
					Libro lib = (Libro) session.load(Libro.class, id);

					System.out.println("\n Detalle actual libro Id " + id + ":\n ---------------------------" + lib.getLibroText());
					System.out.println("\n Introduzca las modificaciones ('enter' para saltar):\n");

					insertarDato();

					if (titulo == "") titulo = lib.getTitulo();
					if (autor == "") autor = lib.getAutor();
					if (anyonacimiento == "") anyonacimiento = lib.getAnyo_nacimiento();
					if (anyopublicacion == "") anyopublicacion = lib.getAnyo_publicacion();
					if (editorial == "") editorial = lib.getEditorial();
					if (paginas == "") paginas = lib.getNum_paginas();

					lib.setTitulo(titulo);
					lib.setAutor(autor);
					lib.setAnyo_nacimiento(anyonacimiento);
					lib.setAnyo_publicacion(anyopublicacion);
					lib.setEditorial(editorial);
					lib.setNum_paginas(paginas);

					session.update(lib);
					System.out.println("\n Datos Id " + lib.getId() + " actualizados:\n -------------------------" + lib.getLibroText());

				} catch (NumberFormatException e) {
					System.out.println(" Error. Debe introducir un número\n");
				} catch (ObjectNotFoundException e) {
					System.out.println(" Error. ID inexistente. Indique una 'Id' valida.\n");
				}

				
			// Borrar registro por 'id'
		} else if (opcion == 5) {

			System.out.print("\n Indique la id del libro que desea borrar: ");
			try {
				int id = Integer.parseInt(br.readLine());

				Libro lib = new Libro(); // se crea un objeto vacio al que se le asigna la id que nos han pasado
				lib.setId(id);
				session.delete(lib);
				System.out.println("\n El elemento con Id " + lib.getId() + " se ha borrado correctamente.\n");

			} catch (NumberFormatException | IOException e) {
				System.out.println(" Error. Debe introducir un número");
			}

			// Salir de la plicación. Sale del bucle
			} else if (opcion == 6) {

				System.out.println("\n Fin de la aplicación");
				flag = true;

				
			// Condicion que detecta si la opción tecleada es valida
			} else {
				System.out.println(" Ha indicado una opción incorrecta. Teclee una opción valida.\n");
				opcion = 0;
			}

			
			// Cuando terminan de ejecutarse las instrucciones de la opción elegida hacemos
			// commit y cerramos la sesion
			session.getTransaction().commit();
			session.close();
		}
		br.close();
	}

	
	/*METODO: insertarDato()
	 * ACTION: Solicita por consola los datos para crear un nuevo registro o modificarlo, 
	 * a la vez que verifica que el formato sea correcto y coincida con el de la BDD para 
	 * que no nos de error al hacer el commit.
	 * INPUT: atributos del objeto libro por teclado
	 * OUTPUT: nada. Guarda en memoria los valores de los atributos*/
	public static void insertarDato() throws IOException {
	
		String mensajeError = " Error de formato. Intentelo de nuevo";
		boolean flag = false;
	
		while (!flag) {
			System.out.print(" Título: ");
			titulo = br.readLine();
			if (titulo.length() > 60) System.out.println(mensajeError);
			else flag = true;
		}
		
		flag = false;
		while (!flag) {
			System.out.print(" Autor: ");
			autor = br.readLine();
			if (autor.length() > 50) System.out.println(mensajeError);
			else flag = true;
		}
		
		flag = false;
		while (!flag) {
			System.out.print(" Año nacimiento: ");
			anyonacimiento = br.readLine();
			if (anyonacimiento.length() > 4) System.out.println(mensajeError);
			else flag = true;
		}
		
		flag = false;
		while (!flag) {
			System.out.print(" Año publicación: ");
			anyopublicacion = br.readLine();
			if (anyopublicacion.length() > 4) System.out.println(mensajeError);
			else flag = true;
		}
	
		flag = false;
		while (!flag) {
			System.out.print(" Editorial: ");
			editorial = br.readLine();
			if (editorial.length() > 50) System.out.println(mensajeError);
			else flag = true;
		}
	
		flag = false;
		while (!flag) {
			System.out.print(" Número páginas: ");
			paginas = br.readLine();
			if (paginas.length() > 5) System.out.println(mensajeError);
			else flag = true;
		}
	}
}

