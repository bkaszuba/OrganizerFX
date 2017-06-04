package sample;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Kaszuba on 01.06.2017.
 */
public class EditWindow {

    static TextField dateInput,hourInput,descriptionInput;
    public static void display(Stage stage, ObservableList<Event> allEvents,ObservableList<Event> selectedEvent, TableView<Event> table){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit");
        Label label = new Label("Insert new Values");
        label.setId("EditLabel");

        dateInput = new TextField();
        dateInput.setText(selectedEvent.get(0).getDate().toString());
        hourInput = new TextField();
        hourInput.setText(selectedEvent.get(0).getHour());
        descriptionInput = new TextField();
        descriptionInput.setText(selectedEvent.get(0).getDescription());

        Button save = new Button("Save");
        save.setOnAction( e -> {
            try {
                SQLConnection.updateData(selectedEvent, dateInput.getText(), hourInput.getText(), descriptionInput.getText());
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            searchAndReplace(allEvents,selectedEvent);
            Main.refreshData(allEvents, table);
        });
        Button close = new Button("Close");
        close.setOnAction( e-> {
            Main.refreshData(allEvents, table);
            window.close();
        });

        HBox info = new HBox(20);
        info.getChildren().add(label);
        info.setPadding(new Insets(15, 10, 0, 10));
        info.setAlignment(Pos.CENTER);

        HBox texts = new HBox(20);
        texts.getChildren().addAll(dateInput, hourInput, descriptionInput);
        texts.setPadding(new Insets(10, 10,10,10));
        texts.setAlignment(Pos.CENTER);

        HBox buttons = new HBox(20);
        buttons.getChildren().addAll(save, close);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);
        buttons.setPadding(new Insets(10, 10, 10 ,10));

        VBox mainLayout = new VBox(20);
        mainLayout.getChildren().addAll(info, texts, buttons);

        Scene scene = new Scene(mainLayout, 300, 155);
        Main.readCSSFile(scene);
        window.setScene(scene);
        window.showAndWait();

    }
    public static void searchAndReplace(ObservableList<Event> allEvents, ObservableList<Event> selectedEvent){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(int i=0;i<allEvents.size();i++){
            if((allEvents.get(i).getDate().equals(selectedEvent.get(0).getDate())) &&
                    (allEvents.get(i).getHour().equals(selectedEvent.get(0).getHour())) &&
                    allEvents.get(i).getDescription().equals(selectedEvent.get(0).getDescription())){
                allEvents.get(i).setDate(LocalDate.parse(dateInput.getText(),formatter));
                allEvents.get(i).setHour(hourInput.getText());
                allEvents.get(i).setDescription(descriptionInput.getText());
            }
        }
    }

}
