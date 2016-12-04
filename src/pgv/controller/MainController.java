package pgv.controller;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import pgv.model.Escritor;
import pgv.model.Lector;

public class MainController {

	@FXML
	private BorderPane view;

	@FXML
	private TextField tareasText;

	@FXML
	private Button crearButton;

	@FXML
	private ListView<String> listView;

	@FXML
	private Spinner<Double> spinner;

	private Escritor escritor;
	private Lector lector;
	private Semaphore mutex;
	private ObservableList<String> lista;

	public MainController() {

		mutex = new Semaphore(1);

		lector = new Lector(mutex, listView);
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
		spinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<Double>(doubles));

		crearButton.disableProperty()
				.bind(spinner.valueFactoryProperty().isNull().or(tareasText.textProperty().isEmpty()));

	}

	@FXML
	void onCrear(ActionEvent event) {
		System.out.println("\n");

		for (int i = 0; i < Integer.parseInt(tareasText.getText()); i++) {
			if (spinner.getValue() > Math.random()) {
				lector.run();
			} else {
				escritor.run();
			}
		}

	}

	public BorderPane getView() {
		return view;
	}

}
