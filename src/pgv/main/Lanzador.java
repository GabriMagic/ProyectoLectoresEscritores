package pgv.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pgv.controller.MainController;

public class Lanzador extends Application {

	private MainController maincontroller;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		maincontroller = new MainController();
		
		primaryStage.setTitle("Escritores / Lectores");
		primaryStage.setScene(new Scene(maincontroller.getView()));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
