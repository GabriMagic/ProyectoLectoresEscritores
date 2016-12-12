package pgv.model;

import java.util.concurrent.Semaphore;

public class LightSwitch {

	private int contador;
	private Semaphore mutex;

	public LightSwitch() {
		contador = 0;
		mutex = new Semaphore(1);
	}

	public void lock(Semaphore semaphore) {
		try {
			mutex.acquire();
			contador++;

			if (contador == 1) {
				semaphore.acquire();
			}

			mutex.release();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void unlock(Semaphore semaphore) {
		try {
			mutex.acquire();
			contador--;

			if (contador == 0) {
				semaphore.release();
			}
			mutex.release();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
