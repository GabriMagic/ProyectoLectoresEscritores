package pgv.model;

import java.util.concurrent.Semaphore;

import javafx.scene.control.ListView;

public class Lector extends Thread {

	private String nombre;
	private Semaphore mutex;

	public Lector(Semaphore mutex, ListView<String> listView) {
		this.mutex = mutex;
		nombre = "LECTOR";
		listView.getItems().add(nombre);
	}

	@Override
	public void run() {
		super.run();

		try {
			mutex.acquire();
			System.out.println("Leyendo...");
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
