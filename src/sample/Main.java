package sample;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main extends Application {
    TableView<Event> table;
    TextField dateInput, hourInput, descriptionInput;
    DatePicker datePicker;
    Scene sceneAboutProgram;

    @Override
    public void start(Stage primaryStage) throws Exception{
        ObservableList<Event> AllEvents = FXCollections.observableArrayList();;
        /*AllEvents.add(new Event(LocalDate.of(2016, 12, 10), "20:15", "Ciekawy opis"));
        AllEvents.add(new Event(LocalDate.of(2017, 11, 22), "11:50", "Dlugi opis dla"));
        AllEvents.add(new Event(LocalDate.of(2036, 3,  5), "7:45", "Budzik"));
        AllEvents.add(new Event(LocalDate.of(2017, 5,  19), "8:30", "Technologie Programowania"));
        AllEvents.add(new Event(LocalDate.of(2017, 5,  19), "10:15", "Podstawy Sieci"));
        AllEvents.add(new Event(LocalDate.of(2017, 5,  19), "12:00", "Inteligentna Analiza Danych"));
        AllEvents.add(new Event(LocalDate.of(2017, 5,  19), "13:30", "Siłka!!!"));
        */Organizer.readDataFromFile("allData.txt", AllEvents);

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Organizer Alpha");

        //Date columnt
        TableColumn<Event, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(150);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        //Hour columnt
        TableColumn<Event, Integer> hourColumn = new TableColumn<>("Hour");
        hourColumn.setMinWidth(150);
        hourColumn.setCellValueFactory(new PropertyValueFactory<>("hour"));

        //Description columnt
        TableColumn<Event, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(150);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        //TextFields to enter date
        dateInput = new TextField();
        dateInput.setPromptText("Enter Date");
        dateInput.setMinWidth(100);
        //TextFields to enter hour
        hourInput = new TextField();
        hourInput.setPromptText("Enter hour");
        hourInput.setMinWidth(100);
        //TextFields to enter description
        descriptionInput = new TextField();
        descriptionInput.setPromptText("Enter Description");
        descriptionInput.setMaxWidth(300);

        datePicker = new DatePicker();
        datePicker.setOnAction( e-> table.setItems(getEvents(AllEvents)));
        HBox upHBox = new HBox();
        upHBox.setPadding(new Insets(10,10,10,10));
        upHBox.setAlignment(Pos.CENTER);
        upHBox.getChildren().add(datePicker);

        //Button
        Button addButton = new Button("Add");
        addButton.setOnAction( e-> {
            addButtonClicked(AllEvents);
            table.setItems(getEvents(AllEvents)); // update data
        });
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction( e-> {
            deleteButtonClicked(AllEvents);
            table.setItems(getEvents(AllEvents)); //update data
        });

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(dateInput, hourInput, descriptionInput, addButton, deleteButton);

        //Menu
        Menu view = new Menu("_View");
        //menu items
        MenuItem settingOption = new MenuItem("Settings...");

        //settingOption.setDisable(true);
        view.getItems().add(settingOption);
        view.getItems().add(new SeparatorMenuItem());
        MenuItem aboutOption = new MenuItem("About program...");

        view.getItems().add(aboutOption);
        //Main menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(view);





        table = new TableView<>();
        table.setItems(getEvents(AllEvents));
        table.getColumns().addAll(dateColumn,hourColumn,descriptionColumn);
        descriptionColumn.setPrefWidth(298); // adjust width to match down buttons



        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuBar,upHBox,table, hBox);

        Scene scene = new Scene(vBox, 600,450);
        settingOption.setOnAction(e->SettingsWindow.display(primaryStage, table));
        aboutOption.setOnAction(e->{
            AboutWindow.display("This beta version of organizer was made by BKaszuba & MMadej", 400,150, primaryStage);
        });
        Label infoAboutProgrma = new Label("Program was made By Bartlomiej Kaszuba & Michal Madej");
        StackPane infoAboutProgramLayout = new StackPane();
        infoAboutProgramLayout.getChildren().add(infoAboutProgrma);
        sceneAboutProgram = new Scene(infoAboutProgramLayout, 50,50);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            try {
                Organizer.writeDataToFile("allData.txt", AllEvents);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void addButtonClicked(ObservableList<Event> allEvents) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Event event = new Event();
        event.setDate(LocalDate.parse(dateInput.getText(), formatter));
        event.setHour(hourInput.getText());
        event.setDescription(descriptionInput.getText());
        allEvents.add(event);
        dateInput.clear();
        hourInput.clear();
        descriptionInput.clear();
    }

    private void deleteButtonClicked(ObservableList<Event> AllEvents) {
        ObservableList<Event> eventSelected,tmp;
        tmp = FXCollections.observableArrayList();
        //AllEvents = table.getItems();
        eventSelected = table.getSelectionModel().getSelectedItems();
        //System.out.println(eventSelected.get(0).getDescription());
        for(int i=0;i<AllEvents.size();i++){
            if (eventSelected.get(0).getDescription().equals(AllEvents.get(i).getDescription())) {

                //eventSelected.remove(0);
                AllEvents.remove(i);
            }
        }

    }

    //getAllEvents
    public ObservableList<Event> getEvents(ObservableList<Event> allEvents){
        ObservableList<Event> events = FXCollections.observableArrayList();
        for(int i=0; i<allEvents.size();i++){
            if(allEvents.get(i).getDate().equals(datePicker.getValue())){
                events.add(allEvents.get(i));
            }
        }
        return events;

    }


    public static void main(String[] args) {
        launch(args);
    }
}
