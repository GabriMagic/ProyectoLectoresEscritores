package pgv.controller;

import java.io.IOException;

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
import pgv.model.GenerarLectoresEscritores;

public class MainController {

	@FXML
	private BorderPane view;

	@FXML
	private Button crearButton;

	@FXML
	private Spinner<Double> lectoresSpinner;

	@FXML
	private Spinner<Integer> tareasSpinner;

	@FXML
	private ListView<String> listView, habitacion;

	private GenerarLectoresEscritores generador;

	public MainController() {

		FXMLloads();

		generador = new GenerarLectoresEscritores();

		ObservableList<Double> doubles = FXCollections.observableArrayList(0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8,
				0.9, 1.0);
		lectoresSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<Double>(doubles));
		tareasSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));

		crearButton.disableProperty()
				.bind(lectoresSpinner.valueFactoryProperty().isNull().or(tareasSpinner.valueProperty().isNull()));

		// Platform.runLater(new Runnable() {
		// @Override
		// public void run() {
		listView.itemsProperty().bind(generador.listaEsperaProperty());
		habitacion.itemsProperty().bind(generador.listaHabitacionProperty());
		// }
		// });
	}

	@FXML
	void onCrear(ActionEvent event) {
		generador.setTareasSpinner(tareasSpinner.getValue());
		generador.setLectoresSpinner(lectoresSpinner.getValue());
		generador.empezar();
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
