package pgv.model;

import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;

public class Escritor extends Thread {

	private String nombre;
	private ListProperty<String> listaEspera, listaHabitacion;

	int num = 1;

	private Semaphore mutex, noReaders, noWriters;
	private LightSwitch readSwitch = new LightSwitch();
	private LightSwitch writeSwitch = new LightSwitch();

	public Escritor(Semaphore mutex, Semaphore noReaders, Semaphore noWriters, LightSwitch readSwitch,
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

		noWriters.release();
		try {
			/**
			 * ENTRAR EN LA COLA OK
			 */
			mutex.acquire();
			nombre = "Escritor " + num;
			num++;
			System.out.println("Entrando Escritor...");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					listaEspera.add(nombre);
				}
			});

			sleep(500);
			mutex.release();
			///////////////////////////////////////////

			writeSwitch.lock(noReaders);
			noWriters.acquire();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						sleep(1000);

						listaEspera.remove(0);
						listaHabitacion.add(0, nombre);
						System.out.println("Escribiendo");
						
						sleep(2000);
						
						System.out.println("HAY "+listaHabitacion.size()+" escritores dentro.");
						listaHabitacion.remove(0);
						noWriters.release();
						writeSwitch.unlock(noReaders);
						
					} catch (InterruptedException e) {
					}
				}
			});

			// Platform.runLater(new Runnable() {
			// @Override
			// public void run() {
			// listaHabitacion.remove(0);
			// }
			// });

		} catch (Exception e) {
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
