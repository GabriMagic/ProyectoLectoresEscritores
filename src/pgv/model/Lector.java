package pgv.model;

import java.util.concurrent.Semaphore;
import javafx.collections.ObservableList;

public class Lector extends Thread {

	private String nombre;
	private Semaphore mutex, barreraEscritor;
	private ObservableList<String> lista;

	public Lector(Semaphore mutex, Semaphore barreraEscritor, ObservableList<String> lista) {
		nombre = "Lector";
		this.mutex = mutex;
		this.lista = lista;
		this.barreraEscritor = barreraEscritor;
	}

	@Override
	public void run() {
		super.run();

		try {
			mutex.acquire();
			System.out.println("Leyendo...");
			lista.add(getNombre());
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
