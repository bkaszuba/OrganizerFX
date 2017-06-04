package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Kaszuba on 19.05.2017.
 */
public class SettingsWindow {

    public static void display(Stage stage, TableView<Event> table){
        double getDefaultWidth = 616;
        double getDefaultHeight = 489;

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Settings");
        Label label = new Label();
        label.setText("Set size of main window");
        label.setId("EditLabel");
        TextField height = new TextField();
        height.setPromptText("Type height");
        TextField width = new TextField();
        width.setPromptText("Type width");

        Button applyButton = new Button("Apply");
        Button closeButton = new Button("Close");
        Button getDefultButton = new Button("Default");

        applyButton.setOnAction( e-> {
            stage.setWidth(Double.parseDouble(width.getText()));
            stage.setHeight(Double.parseDouble(height.getText()));
        });

        closeButton.setOnAction( e-> window.close());

        getDefultButton.setOnAction( e-> {
            System.out.println(stage.getWidth()+"  "+stage.getHeight());
            stage.setWidth(getDefaultWidth);
            stage.setHeight(getDefaultHeight);
        });

        CheckBox dateColumnCheck = new CheckBox("Date Column");
        dateColumnCheck.setSelected(true);
        dateColumnCheck.setOnAction( e-> {
            if(dateColumnCheck.isSelected() == true){
                table.getColumns().get(0).setVisible(true);
            }
            else table.getColumns().get(0).setVisible(false);
        });

        CheckBox hourColumnCheck = new CheckBox("Hour Column");
        hourColumnCheck.setSelected(true);
        hourColumnCheck.setOnAction( e-> {
            if(hourColumnCheck.isSelected() == true){
                table.getColumns().get(1).setVisible(true);
            }
            else table.getColumns().get(1).setVisible(false);
        });

        CheckBox descriptionColumnCheck  = new CheckBox("Description Column");
        descriptionColumnCheck.setSelected(true);
        descriptionColumnCheck.setOnAction( e-> {
            if(descriptionColumnCheck.isSelected() == true){
                table.getColumns().get(2).setVisible(true);
            }
            else table.getColumns().get(2).setVisible(false);
        });

        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);

        HBox textFieldsLayout = new HBox(20);
        textFieldsLayout.getChildren().addAll(height, width);
        textFieldsLayout.setAlignment(Pos.CENTER);

        HBox buttonsLayout = new HBox(10);
        buttonsLayout.getChildren().addAll(applyButton,getDefultButton, closeButton);
        buttonsLayout.setAlignment(Pos.BOTTOM_RIGHT);
        buttonsLayout.setPadding(new Insets(10,10,10,10));

        HBox checklayout = new HBox(10);
        checklayout.getChildren().addAll(dateColumnCheck, hourColumnCheck, descriptionColumnCheck);
        checklayout.setAlignment(Pos.CENTER);



        mainLayout.getChildren().addAll(label, textFieldsLayout, checklayout,buttonsLayout);

        Scene scene = new Scene(mainLayout, 400, 200);
        Main.readCSSFile(scene);
        window.setScene(scene);
        window.showAndWait();

    }
}
