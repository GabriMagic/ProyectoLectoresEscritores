package pgv.controller;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class MainController {

	@FXML
	private BorderPane view;

	@FXML
	private Button crearButton;

	@FXML
	private Button resetButton;

	@FXML
	private ListView<String> listView;

	@FXML
	private Spinner<Double> lectoresSpinner;

	@FXML
	private Spinner<Integer> tareasSpinner;

	private Escritor escritor;
	private Lector lector;
	private Semaphore mutex;
	private ObservableList<String> lista;

	public MainController() {

		mutex = new Semaphore(1);
		lista = FXCollections.observableArrayList();
		lector = new Lector(mutex, lista);
		escritor = new Escritor(mutex, lista);

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/pgv/view/MainView.fxml"));
			loader.setController(this);
			view = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		System.out.println("\n");

		for (int i = 0; i < tareasSpinner.getValue(); i++) {
			if (lectoresSpinner.getValue() > Math.random()) {
				lector.run();
			} else {
				escritor.run();
			}
		}
		listView.scrollTo(lista.size());
	}

	@FXML
	void onReset(ActionEvent event) {
		lista.removeAll(lista);
	}

	public BorderPane getView() {
		return view;
	}
	

}
