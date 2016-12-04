package pgv.model;

import java.util.concurrent.Semaphore;

import javafx.collections.ObservableList;

public class Escritor extends Thread implements Runnable {

	private Semaphore mutex;
	private String nombre;
	private ObservableList<String> lista;

	public Escritor(Semaphore mutex, ObservableList<String> lista) {
		nombre = "ESCRITOR";
		this.mutex = mutex;
		this.lista = lista;
	}

	@Override
	public void run() {
		super.run();

		try {
			mutex.acquire();
			System.out.println("Escribiendo...");
			lista.add(nombre);
			Thread.sleep(0);
			mutex.release();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getNombre() {
		return nombre;
	}

}
