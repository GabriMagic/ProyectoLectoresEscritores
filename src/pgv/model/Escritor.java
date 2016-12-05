package pgv.model;

import java.util.concurrent.Semaphore;
import javafx.collections.ObservableList;

public class Escritor extends Thread {

	private String nombre;
	private Semaphore mutex;
	private ObservableList<String> lista;

	public Escritor(Semaphore mutex, ObservableList<String> lista) {
		nombre = "Escritor";
		this.mutex = mutex;
		this.lista = lista;
	}

	@Override
	public void run() {
		super.run();

		try {
			mutex.acquire();
			System.out.println("Escribiendo...");
			lista.add(getName());
			Thread.sleep(100);
			mutex.release();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getNombre() {
		return nombre;
	}
}
