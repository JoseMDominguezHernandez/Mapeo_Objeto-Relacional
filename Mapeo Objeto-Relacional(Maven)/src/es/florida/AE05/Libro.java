package es.florida.AE05;

public class Libro {
	
	private String titulo, autor, editorial, anyo_nacimiento, anyo_publicacion, num_paginas;
	private int id;

	public Libro() {
	}

	public Libro(String titulo, String autor, String anyo_nacimiento, String anyo_publicacion, String editorial,
			String num_paginas) {

		this.titulo = titulo;
		this.autor = autor;
		this.anyo_nacimiento = anyo_nacimiento;
		this.anyo_publicacion = anyo_publicacion;
		this.editorial = editorial;
		this.num_paginas = num_paginas;
	}

	public Libro(int id, String titulo, String autor, String anyo_nacimiento, String anyo_publicacion, String editorial,
			String num_paginas) {

		this.id = id;
		this.titulo = titulo;
		this.autor = autor;
		this.anyo_nacimiento = anyo_nacimiento;
		this.anyo_publicacion = anyo_publicacion;
		this.editorial = editorial;
		this.num_paginas = num_paginas;
	}

	public int getId() {
		return id;
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public String getTitulo() {
		return titulo;
	}
	

	public void setTitulo (String titulo) {
		this.titulo = titulo;
	}
	
	public String getAutor() {
		return autor;
	}
	
	public void setAutor(String autor) {
		this.autor = autor;
	}
	
	public String getAnyo_nacimiento () {
		return anyo_nacimiento;
	}
		
	public void setAnyo_nacimiento(String anyo_nacimineto) {
		this.anyo_nacimiento = anyo_nacimineto;
	}
	
	public String getAnyo_publicacion () {
		return anyo_publicacion;
	}
	
	public void setAnyo_publicacion (String anyo_publicacion) {
		this.anyo_publicacion = anyo_publicacion;
	}
	
	public String getEditorial() {
		return editorial;
	}
	
	public void setEditorial(String editorial) {
			this.editorial = editorial;
	}
	
	public String getNum_paginas() {
		return num_paginas;
	}
	
	public void setNum_paginas (String num_paginas) {
		this.num_paginas = num_paginas;
	}
	
	public String getLibroText () {
		String libro = ("\n Titulo:\t" + getTitulo() + "\n Autor:\t\t" + getAutor() + " (" + getAnyo_nacimiento() + ")\n Año:\t\t" 
	+ getAnyo_publicacion() + "\n Editorial:\t" + getEditorial() + "\n Páginas:\t" + getNum_paginas() + "\n"); 
		
		return libro;
	}
}
