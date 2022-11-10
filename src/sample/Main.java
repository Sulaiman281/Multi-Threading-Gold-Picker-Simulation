package sample;

import controller.Singleton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.GraphicView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Singleton singleton = Singleton.getInstance();
        Scene scene = new Scene(new GraphicView().getRoot());
        primaryStage.setTitle("Thread Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Button getButton(String btn_name, String id,EventHandler<ActionEvent> onClick){
        Button btn = new Button(btn_name);
        btn.setOnAction(onClick);
        btn.setId(id);
        return btn;
    }

    public static Image load_image(String path){
        return new Image(Main.class.getResource(path).toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}