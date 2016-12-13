package pgv.controller;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import pgv.model.Escritor;
import pgv.model.Lector;
import pgv.model.LightSwitch;

public class MainController {

	@FXML
	private BorderPane view;

	@FXML
	private Button crearButton;

	@FXML
	private Button resetButton;

	@FXML
	private Spinner<Double> lectoresSpinner;

	@FXML
	private Spinner<Integer> tareasSpinner;

	@FXML
	private ListView<String> listView, habitacion;

	private Escritor escritor;
	private Lector lector;
	private ListProperty<String> listaEspera, listaHabitacion;

	private Semaphore mutex, noReaders, noWriters;
	LightSwitch readSwitch = new LightSwitch();
	LightSwitch writeSwitch = new LightSwitch();
	int num = 0;

	public MainController() {

		mutex = new Semaphore(1);
		noReaders = new Semaphore(1);
		noWriters = new Semaphore(1);

		listaEspera = new SimpleListProperty<>(this, "listaEspera", FXCollections.observableArrayList());
		listaHabitacion = new SimpleListProperty<>(this, "listaEspera", FXCollections.observableArrayList());

		lector = new Lector(mutex, noReaders, noWriters, readSwitch, writeSwitch, listaEspera, listaHabitacion);
		escritor = new Escritor(mutex, noReaders, noWriters, readSwitch, writeSwitch, listaEspera, listaHabitacion);

		FXMLloads();

		ObservableList<Double> doubles = FXCollections.observableArrayList(0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8,
				0.9, 1.0);
		lectoresSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<Double>(doubles));
		tareasSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));

		crearButton.disableProperty()
				.bind(lectoresSpinner.valueFactoryProperty().isNull().or(tareasSpinner.valueProperty().isNull()));

		listView.itemsProperty().bind(listaEspera);
		habitacion.itemsProperty().bind(listaHabitacion);

	}

	@FXML
	void onCrear(ActionEvent event) {
		onReset(event);

		final Service thread = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {

				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						new Thread(new Runnable() {

							@Override
							public void run() {
								entrarhabitacion();
							}

						}).start();
						return null;
					}
				};
			}
		};

		thread.start();

	}

	private void entrarhabitacion() {
		for (int i = 0; i < tareasSpinner.getValue(); i++) {
			// CAMBIAR A ">"
			if (lectoresSpinner.getValue() > Math.random()) {
				// ENTRAR A LA COLA
				lector.setNombre("Lector " + num);
				num++;

				System.out.println("Entrando Lector...");

				listaEspera.add(lector.getNombre());
				lector.run();
			} else {
				// ENTRAR A LA COLA
				lector.setNombre("Escritor " + num);
				num++;

				System.out.println("Entrando escritor...");

				listaEspera.add(lector.getNombre());
				escritor.run();
			}
		}
	}

	@FXML
	void onReset(ActionEvent event) {
		listaEspera.removeAll(listaEspera);
	}

	private void FXMLloads() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/pgv/view/MainView.fxml"));
			loader.setController(this);
			view = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BorderPane getView() {
		return view;
	}

}
