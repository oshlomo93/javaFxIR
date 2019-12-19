package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main (String [] args){
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Model model = new Model();
        ViewModel viewModel = new ViewModel(model);
        primaryStage.setTitle("IR");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getClassLoader().getResource("myView.fxml").openStream());
        Scene scene = new Scene(root);
        View myViewController = fxmlLoader.getController();
        myViewController.setViewModel(viewModel);
        viewModel.addObserver(myViewController);
        primaryStage.setScene(scene);
        SetStageCloseEvent(primaryStage, myViewController);
        primaryStage.show();
        model.addObserver(viewModel);
    }

    private void SetStageCloseEvent(Stage primaryStage, View myViewController) {
        primaryStage.setOnCloseRequest(windowEvent -> {
            myViewController.exitCorrectly(primaryStage);
            windowEvent.consume();
        });
    }



}
