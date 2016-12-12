package pgv.model;

import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;

public class Lector extends Thread {

	private String nombre;
	private ListProperty<String> listaEspera;
	private ListProperty<String> listaHabitacion;

	int num = 1;

	private Semaphore mutex, noReaders, noWriters;
	private LightSwitch readSwitch = new LightSwitch();
	private LightSwitch writeSwitch = new LightSwitch();

	public Lector(Semaphore mutex, Semaphore noReaders, Semaphore noWriters, LightSwitch readSwitch,
			LightSwitch writeSwitch, ListProperty<String> listaEspera, ListProperty<String> listaHabitacion) {

		this.listaEspera = listaEspera;
		this.listaHabitacion = listaHabitacion;
		this.mutex = mutex;
		this.noReaders = noReaders;
		this.noWriters = noWriters;
		this.readSwitch = readSwitch;
		this.writeSwitch = writeSwitch;
	}

	@Override
	public void run() {

		noReaders.release();

		try {
			// ENTRAR A LA COLA
			mutex.acquire();
			nombre = "Lector " + num;
			num++;
			System.out.println("Entrando Lector...");

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					listaEspera.add(nombre);
				}
			});
			sleep(500);
			mutex.release();

			noReaders.acquire();
			readSwitch.lock(noWriters);
			noReaders.release();

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					try {
						listaEspera.remove(0);
						listaHabitacion.add(nombre);
						System.out.println("HAY "+listaHabitacion.size()+" lectores dentro.");
						System.out.println("Leyendo");
						sleep(1000);
						readSwitch.unlock(noWriters);
						listaHabitacion.remove(0);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
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
