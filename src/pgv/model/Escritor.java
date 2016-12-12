package pgv.model;

import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;

public class Escritor extends Cosa {

	private String nombre;
	private Semaphore mutex;
	private ObservableList<Cosa> lista, listaHabitacion;

	public Escritor(Semaphore mutex, ListProperty<Cosa> listaEspera, ListProperty<Cosa> listaHabitacion2) {
		nombre = "Escritor";
		this.mutex = mutex;
		this.lista = listaEspera;
		this.listaHabitacion = listaHabitacion2;
	}

	@Override
	public void run() {
		super.run();

		try {
			mutex.acquire();
			System.out.println("Entrando Escritor...");
			lista.add(this);
			Thread.sleep(100);
			mutex.release();

			System.out.println("Escribiendo");
			lista.remove(this);
			listaHabitacion.add(this);
			System.out.println();

			Thread.sleep(1000);

			listaHabitacion.remove(this);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getNombre() {
		return nombre;
	}

	@Override
	public String toString() {
		return getNombre();
	}
}
