package pgv.controller;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pgv.model.Escritor;
import pgv.model.Lector;

public class MainController extends Thread {

	@FXML
	private BorderPane view;

	@FXML
	private Button crearButton;

	@FXML
	private Button resetButton;

	@FXML
	private ListView<String> listView, habitacion;

	@FXML
	private Spinner<Double> lectoresSpinner;

	@FXML
	private Spinner<Integer> tareasSpinner;

	private Escritor escritor;
	private Lector lector;
	private Semaphore mutex, barreraEscritor;
	private ObservableList<String> lista;
	private Stage habitacionStage;

	public MainController() {

		mutex = barreraEscritor = new Semaphore(1);
		lista = FXCollections.observableArrayList();
		lector = new Lector(mutex, barreraEscritor, lista);
		escritor = new Escritor(mutex, barreraEscritor, lista);

		FXMLloads();

		habitacionStage = new Stage();
		habitacionStage.setScene(new Scene(habitacion));
		habitacionStage.setTitle("Habitación");

		ObservableList<Double> doubles = FXCollections.observableArrayList(0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8,
				0.9, 1.0);
		lectoresSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<Double>(doubles));
		tareasSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));

		crearButton.disableProperty()
				.bind(lectoresSpinner.valueFactoryProperty().isNull().or(tareasSpinner.valueProperty().isNull()));
		listView.itemsProperty().bind(new SimpleListProperty<>(lista));

	}

	@FXML
	void onCrear(ActionEvent event) {
		habitacionStage.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < tareasSpinner.getValue(); i++) {
					if (lectoresSpinner.getValue() > Math.random()) {
						lector.run();
					} else {
						escritor.run();
					}
				}
				listView.scrollTo(lista.size());
			}
		}).start();

	}

	@FXML
	void onReset(ActionEvent event) {
		lista.removeAll(lista);
	}

	@Override
	public void run() {
		super.run();
		System.out.println("RUN CONTROLLER");
	}

	private void FXMLloads() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/pgv/view/MainView.fxml"));
			loader.setController(this);
			view = loader.load();

			FXMLLoader loadHabitacion = new FXMLLoader(getClass().getResource("/pgv/view/Habitacion.fxml"));
			loadHabitacion.setController(this);
			habitacion = loadHabitacion.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BorderPane getView() {
		return view;
	}

}
