package pgv.model;

import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;

public class Escritor extends Thread {

	private String nombre;
	private ListProperty<String> listaEspera, listaHabitacion;

	int num = 1;

	private Semaphore noReaders, noWriters;
	private LightSwitch writeSwitch = new LightSwitch();

	public Escritor(Semaphore noReaders, Semaphore noWriters, LightSwitch writeSwitch, ListProperty<String> listaEspera,
			ListProperty<String> listaHabitacion) {

		this.listaEspera = listaEspera;
		this.listaHabitacion = listaHabitacion;
		this.noReaders = noReaders;
		this.noWriters = noWriters;
		this.writeSwitch = writeSwitch;
	}

	@Override
	public void run() {

		try {
			writeSwitch.lock(noReaders);
			noWriters.acquire();
			sleep(500);

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					listaHabitacion.add(getName());
					listaEspera.remove(getName());
				}
			});

			sleep(1000);
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					listaHabitacion.remove(getName());
				}
			});

			noWriters.release();
			writeSwitch.unlock(noReaders);

		} catch (InterruptedException e) {
		}

	}

	@Override
	public String toString() {
		return "Escritor";
	}

	public String getNombre() {
		return nombre;
	}
}
