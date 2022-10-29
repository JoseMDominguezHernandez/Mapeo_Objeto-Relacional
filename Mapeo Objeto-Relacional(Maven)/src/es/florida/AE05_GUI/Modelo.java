package es.florida.AE05_GUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import es.florida.AE05.Libro;

public class Modelo {

	
	/* METODO:	consultaCatalogo()
	 * ACTION:	crea la configuracion y abre la sesion para acceder a la BDD. Obtiene una Lista
	 * con los objetos que contiene la BBD realizando una consulta HQL. Obtiene la 'id ' y el 
	 * título de cada objeto los mete en un ArrayList que devolverá cuando la invoquen
	 * INPUT:	ninguno
	 * OUTPUT:	ArrayList con la id y el titulo de los libros que contiene la BDD*/
	public ArrayList<String> consultaCatalogo() {

		ArrayList<String> catalogo = new ArrayList<String>();

		Configuration configuration = new Configuration().configure("hibernate.cfg2.xml");
		configuration.addClass(Libro.class);
		ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
				.build();
		SessionFactory sessionFactory = configuration.buildSessionFactory(registry);
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		List<Libro> listaLibros = new ArrayList();
		listaLibros = session.createQuery("FROM Libro").list();

		String lib;
		for (Object obj : listaLibros) {
			Libro libro = (Libro) obj;
			lib = libro.getId() + ". " + libro.getTitulo();
			catalogo.add(lib);
		}
		session.getTransaction().commit();
		session.close();
		return catalogo;
	}
	
	
	/* METODO:	consultaLibro()
	 * ACTION:	crea la configuracion y abre la sesion para acceder a la BDD. Crea el objeto Libro correspondiente
	 * a la id que le entra como parámetro. Verifica que se ha creado el objeto y si es así obtiene todos los atributos
	 * del objeto y los pasa a un Array que devolverá cuando invoquen al método.
	 * INPUT:	id del objeto a consultar
	 * OUTPUT:	Array de String con los atributos del objeto; en la posicion 0 va el chivato de si se ha creado o no el 
	 * objeto y así lanzar mendsaje de error desde control().*/
	public String[] consultaLibro(int id_consulta) {

		String[] detalleLibro = new String[7];

		Configuration configuration = new Configuration().configure("hibernate.cfg2.xml");
		configuration.addClass(Libro.class);
		ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
				.build();
		SessionFactory sessionFactory = configuration.buildSessionFactory(registry);
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Libro libro = (Libro) session.get(Libro.class, id_consulta);
		if (libro == null)
			detalleLibro[0] = "Error";	// Chivato para control() se lance un error si libro es null
		else {
			detalleLibro[0] = "Ok";				
			detalleLibro[1] = libro.getTitulo().toString();
			detalleLibro[2] = libro.getAutor().toString();
			detalleLibro[3] = libro.getAnyo_nacimiento().toString();
			detalleLibro[4] = libro.getAnyo_publicacion().toString();
			detalleLibro[5] = libro.getEditorial().toString();
			detalleLibro[6] = libro.getNum_paginas().toString();
		}
		session.getTransaction().commit();
		session.close();
		return detalleLibro;
	}
	
	/* METODO:	nuevoLibro()
	 * ACTION:	crea la configuracion y abre la sesion para acceder a la BDD. Crea un 
	 * nuevo registro en la BDD con el objeto Libro que recibe al ser invocada.
	 * INPUT:	Objeto Libro sin id (la id la asigna la BBD)
	 * OUTPUT:	Integer con la 'id' asignada al nuevo objeto registrado. Se obtiene con Serializable id */
	public int nuevoLibro(Libro libro) {

		Configuration configuration = new Configuration().configure("hibernate.cfg2.xml");
		configuration.addClass(Libro.class);
		ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
				.build();
		SessionFactory sessionFactory = configuration.buildSessionFactory(registry);
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Serializable id = session.save(libro);

		session.getTransaction().commit();
		session.close();
		return (int) id;
	}
	
	
	/* METODO:	borrarLibro()
	 * ACTION:	crea la configuracion y abre la sesion para acceder a la BDD.
	 * Borra el registro de la BDD correspondiente al id que se le pasa por 
	 * parámetro
	 * INPUT:	Integer con la id del objeto que se quiere borrar
	 * OUTPUT:	No devuelve nada. Borra el objeto*/
	public void borrarLibro(int id) {

		Configuration configuration = new Configuration().configure("hibernate.cfg2.xml");
		configuration.addClass(Libro.class);
		ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
				.build();
		SessionFactory sessionFactory = configuration.buildSessionFactory(registry);
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Libro libro = new Libro();		// se crea un objeto vacio al que se le asigna la id que nos han pasado
		libro.setId(id);
		session.delete(libro);			// se borra de la BDD

		session.getTransaction().commit();
		session.close();
	}
	
	
	/* METODO:	crea la configuracion y abre la sesion para acceder a la BDD. Obtiene los
	 * atributos con las modificaciones del objeto que se le pasa por parámetro al invocarlo. Se obtiene de la 
	 * BDD el objeto correspondiente a la id y escribe los nuevos parámetros en el objeto obtenidso dela BDD.
	 * INPUT:	Objeto Libro con los textos modificados en la GUI como atributos.
	 * OUTPUT:	actualiza el objeto de la BDD con los nuevos atributos*/
	public void editarLibro(Libro libro) {		//Objeto Libro con 'id'

		Configuration configuration = new Configuration().configure("hibernate.cfg2.xml");
		configuration.addClass(Libro.class);
		ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
				.build();
		SessionFactory sessionFactory = configuration.buildSessionFactory(registry);
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		//Atributos del objeto craedo a partir de los campos de la GUI (Modificaciones)
		String titulo = libro.getTitulo();
		String autor = libro.getAutor();
		String anyonacimiento = libro.getAnyo_nacimiento();
		String anyopublicacion = libro.getAnyo_publicacion();
		String editorial = libro.getEditorial();
		String paginas = libro.getNum_paginas();
		
		// Objeto de la BDD obtenido por la id
		Libro lib = (Libro) session.load(Libro.class, libro.getId());

		// Sustitucion de los atributos del objeto de la BDD.
		lib.setTitulo(titulo);
		lib.setAutor(autor);
		lib.setAnyo_nacimiento(anyonacimiento);
		lib.setAnyo_publicacion(anyopublicacion);
		lib.setEditorial(editorial);
		lib.setNum_paginas(paginas);

		session.update(lib);		// Update del objeto en la BDD

		session.getTransaction().commit();
		session.close();
	}
}
