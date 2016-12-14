package pgv.model;

import java.util.concurrent.Semaphore;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GenerarLectoresEscritores {

	private ListProperty<String> listaEspera, listaHabitacion;

	private Semaphore noReaders, noWriters;
	private LightSwitch readSwitch = new LightSwitch();
	private LightSwitch writeSwitch = new LightSwitch();
	private int numLector = 1;
	private int numEscritor = 1;

	private int tareasSpinner;
	private Double lectoresSpinner;

	public GenerarLectoresEscritores() {
		noReaders = new Semaphore(1);
		noWriters = new Semaphore(1);

		listaEspera = new SimpleListProperty<>(this, "listaEspera", FXCollections.observableArrayList());
		listaHabitacion = new SimpleListProperty<>(this, "listaHabitacion", FXCollections.observableArrayList());

		// escritor = new Escritor(noReaders, noWriters, writeSwitch,
		// listaEspera, listaHabitacion);

		empezar();
	}

	public void empezar() {

		Thread hilo = new Thread();

		for (int i = 0; i < tareasSpinner; i++) {

			// try {
			// Thread.sleep(1);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }

			if (lectoresSpinner > Math.random()) { // CAMBIAR A ">"
				hilo = new Lector(noReaders, noWriters, readSwitch, listaEspera, listaHabitacion);
				hilo.setName("Lector " + numLector);
				numLector++;
				System.out.println("Entrando Lector...");

				listaEspera.add(hilo.getName());
			} else {
				hilo = new Escritor(noReaders, noWriters, writeSwitch, listaEspera, listaHabitacion);
				hilo.setName("Escritor " + numEscritor);
				numEscritor++;

				System.out.println("Entrando escritor...");

				listaEspera.add(hilo.getName());
			}
			hilo.start();
		}

	}

	public Semaphore getNoReaders() {
		return noReaders;
	}

	public void setNoReaders(Semaphore noReaders) {
		this.noReaders = noReaders;
	}

	public Semaphore getNoWriters() {
		return noWriters;
	}

	public void setNoWriters(Semaphore noWriters) {
		this.noWriters = noWriters;
	}

	public LightSwitch getReadSwitch() {
		return readSwitch;
	}

	public void setReadSwitch(LightSwitch readSwitch) {
		this.readSwitch = readSwitch;
	}

	public LightSwitch getWriteSwitch() {
		return writeSwitch;
	}

	public void setWriteSwitch(LightSwitch writeSwitch) {
		this.writeSwitch = writeSwitch;
	}

	public int getTareasSpinner() {
		return tareasSpinner;
	}

	public void setTareasSpinner(int tareasSpinner) {
		this.tareasSpinner = tareasSpinner;
	}

	public Double getLectoresSpinner() {
		return lectoresSpinner;
	}

	public void setLectoresSpinner(Double lectoresSpinner) {
		this.lectoresSpinner = lectoresSpinner;
	}

	public ListProperty<String> listaEsperaProperty() {
		return this.listaEspera;
	}

	public ObservableList<String> getListaEspera() {
		return this.listaEsperaProperty().get();
	}

	public void setListaEspera(final ObservableList<String> listaEspera) {
		this.listaEsperaProperty().set(listaEspera);
	}

	public ListProperty<String> listaHabitacionProperty() {
		return this.listaHabitacion;
	}

	public ObservableList<String> getListaHabitacion() {
		return this.listaHabitacionProperty().get();
	}

	public void setListaHabitacion(final ObservableList<String> listaHabitacion) {
		this.listaHabitacionProperty().set(listaHabitacion);
	}

}
