package pgv.model;

import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;

public class Lector extends Cosa {

	private String nombre;
	private Semaphore mutex;
	ListProperty<Cosa> lista;
	private ListProperty<Cosa> listaHabitacion;

	public Lector(Semaphore mutex, ListProperty<Cosa> listaEspera, ListProperty<Cosa> listaHabitacion) {
		nombre = "Lector";
		this.mutex = mutex;
		this.lista = listaEspera;
		this.listaHabitacion = listaHabitacion;
	}

	@Override
	public void run() {
		super.run();

		try {
			mutex.acquire();
			try {
				System.out.println("Entrando Lector...");
				Thread.sleep(1000);
				lista.add(this);
				mutex.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.print("Leyendo");
			lista.remove(this);
			listaHabitacion.add(this);

			Thread.sleep(2000);

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
