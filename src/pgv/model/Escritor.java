package pgv.model;

import java.util.concurrent.Semaphore;
import javafx.collections.ObservableList;

public class Escritor extends Thread implements Runnable {

	private Semaphore mutex;
	private String nombre;

	public Escritor(Semaphore mutex, ObservableList<String> lista) {
		nombre = "ESCRITOR";
		this.mutex = mutex;
		// lista.add(nombre);
	}

	@Override
	public void run() {
		super.run();

		try {
			mutex.acquire();
			System.out.println("Escribiendo...");
			Thread.sleep(500);
			mutex.release();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getNombre() {
		return nombre;
	}

}
