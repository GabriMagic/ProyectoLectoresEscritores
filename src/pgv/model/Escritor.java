package pgv.model;

import java.util.concurrent.Semaphore;

import javafx.collections.ObservableList;

public class Escritor extends Thread {

	private String nombre;
	private Semaphore mutex, barreraEscritor;
	private ObservableList<String> lista;

	public Escritor(Semaphore mutex, Semaphore barreraEscritor, ObservableList<String> lista) {
		nombre = "Escritor";
		this.mutex = mutex;
		this.lista = lista;
		this.barreraEscritor = barreraEscritor;
	}

	@Override
	public void run() {
		super.run();

		try {
			mutex.acquire();
			System.out.println("Entrando Escritor...");
			Thread.sleep(100);
			mutex.release();
			lista.add(getNombre());

			barreraEscritor.acquire();
			System.out.print("Escribiendo");
			for (int i = 0; i <= Math.random()*10; i++) {
				System.out.print(".");
				Thread.sleep(500);
			}
			System.out.println();
			barreraEscritor.release();
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getNombre() {
		return nombre;
	}
}
