package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Kaszuba on 19.05.2017.
 */
public class AboutWindow {

        static int onedata;

        public static void display(String message, int width, int height, Stage PrimaryStage){
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("About Program");
            Label label = new Label();
            label.setText(message);

            Button close = new Button("Close");
            close.setOnAction( e-> {
                window.close();
            });

            VBox layout = new VBox(20);
            layout.getChildren().addAll(label, close);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout, width, height);
            window.setScene(scene);
            window.showAndWait();
        }
    }

