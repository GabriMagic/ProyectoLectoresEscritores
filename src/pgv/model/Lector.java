package pgv.model;

import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;

public class Lector extends Thread {

	private String nombre;
	private ListProperty<String> listaEspera;
	private ListProperty<String> listaHabitacion;

	private Semaphore noReaders, noWriters;
	private LightSwitch readSwitch = new LightSwitch();

	public Lector(Semaphore noReaders, Semaphore noWriters, LightSwitch readSwitch, ListProperty<String> listaEspera,
			ListProperty<String> listaHabitacion) {

		this.listaEspera = listaEspera;
		this.listaHabitacion = listaHabitacion;
		this.noReaders = noReaders;
		this.noWriters = noWriters;
		this.readSwitch = readSwitch;
	}

	@Override
	public void run() {

		try {
			sleep(500);
			noReaders.acquire();
			readSwitch.lock(noWriters);
			noReaders.release();

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					listaEspera.remove(getName());
					listaHabitacion.add(getName());
				}
			});

			sleep(1000);

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					listaHabitacion.remove(getName());
				}
			});

			readSwitch.unlock(noWriters);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return getNombre();
	}
}
