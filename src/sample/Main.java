package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

//import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Main extends Application {
    static TableView<Event> table;
    TextField dateInput, hourInput, descriptionInput;
    static DatePicker datePicker;
    Scene sceneAboutProgram;
    Scene scene;
    DirectoryChooser directoryChooser;
    FileChooser fileChooser;
    String directoryPath;
    String filePath;

    @Override
    public void start(Stage primaryStage) throws Exception{
        ObservableList<Event> AllEvents = FXCollections.observableArrayList();;


        SQLConnection.sendDataFromDBToProgram(AllEvents);
        //Outlook.saveToOutloock("", AllEvents);
        //Outlook.saveToOutloock("C:\\Users\\Kaszuba\\Desktop", AllEvents);
        //Organizer.readDataFromFile("allData.txt", AllEvents);

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
        dateInput.setText("2017-05-22");
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
        datePicker.setDayCellFactory(colorAllDates(AllEvents));
        HBox upHBox = new HBox();
        upHBox.setPadding(new Insets(10,10,10,10));
        upHBox.setAlignment(Pos.CENTER);
        upHBox.getChildren().add(datePicker);

        //Button
        Button addButton = new Button("Add");
        addButton.setOnAction( e-> {
            addButtonClicked(AllEvents);
            refreshData(AllEvents, table);
            //datePicker.setDayCellFactory(colorAllDates(AllEvents));
            //table.setItems(getEvents(AllEvents)); // update data
        });
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction( e-> {
            deleteButtonClicked(AllEvents);
            refreshData(AllEvents, table);
            //datePicker.setDayCellFactory(colorAllDates(AllEvents));
            //table.setItems(getEvents(AllEvents)); //update data
        });
        Button editButton= new Button("Edit");
        editButton.setOnAction( e-> {
            editButtonClicked(primaryStage, AllEvents, table);
            datePicker.setDayCellFactory(colorAllDates(AllEvents));
            table.setItems(getEvents(AllEvents)); //update data
        });

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(dateInput, hourInput, descriptionInput, addButton,deleteButton, editButton);

        //Menu
            Menu view = new Menu("_View");
            Menu outlook = new Menu("_Outlook");
            Menu xml = new Menu("_XML");
        //menu items
        MenuItem settingOption = new MenuItem("Settings...");
        //outlook
        MenuItem importOutlook = new MenuItem("Import Outlook...");
        MenuItem exportOutlook = new MenuItem("Export Outlook...");
        outlook.getItems().addAll(importOutlook,exportOutlook);
        //XML
        MenuItem importXML = new MenuItem("Import XML...");
        MenuItem exportXML = new MenuItem("Export XML...");
        xml.getItems().addAll(importXML, exportXML);
        //settingOption.setDisable(true);
        view.getItems().add(settingOption);
        view.getItems().add(new SeparatorMenuItem());
        MenuItem aboutOption = new MenuItem("About program...");

        view.getItems().add(aboutOption);
        view.getItems().add(new SeparatorMenuItem());
        MenuItem filterOption = new MenuItem("Filter...");
        view.getItems().add(filterOption);
        //Main menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(view, outlook, xml);


        table = new TableView<>();
        table.setItems(getEvents(AllEvents));
        table.getColumns().addAll(dateColumn,hourColumn,descriptionColumn);
        descriptionColumn.setPrefWidth(350); // adjust width to match down buttons



        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuBar,upHBox,table, hBox);

        scene = new Scene(vBox, 660,450);

        directoryChooser = new DirectoryChooser();
        exportOutlook.setOnAction( e-> {
            directoryPath = directoryChooser.showDialog(primaryStage).getAbsolutePath();
            directoryPath = directoryPath.replace("\\", "\\\\");
            directoryPath = directoryPath +"\\\\";
           Outlook.saveToOutloock(directoryPath, AllEvents);
        });
        fileChooser = new FileChooser();
        importOutlook.setOnAction( e-> {
            filePath = fileChooser.showOpenDialog(primaryStage).getAbsolutePath();
            filePath = filePath.replace("\\", "\\\\");
            Outlook.readfromOutloock(filePath, AllEvents);
            refreshData(AllEvents, table);
        });
        exportXML.setOnAction( e -> {
            filePath = fileChooser.showOpenDialog(primaryStage).getAbsolutePath();
            filePath = filePath.replace("\\", "\\\\");
            XML.saveToXML(filePath, AllEvents);
        });
        importXML.setOnAction( e-> {
            filePath = fileChooser.showOpenDialog(primaryStage).getAbsolutePath();
            filePath = filePath.replace("\\", "\\\\");
            XML.readFromXML(filePath, AllEvents);
            refreshData(AllEvents,table);
        });

        settingOption.setOnAction(e->SettingsWindow.display(primaryStage, table));
        aboutOption.setOnAction(e->{
            AboutWindow.display("This beta version of organizer was made by BKaszuba & MMadej", 400,150, primaryStage);
        });
        filterOption.setOnAction(e -> {
            try {
                FilterWindow.display(primaryStage, AllEvents);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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
        allEvents.add(event); // Add immediately to app and then save to DB;
        try {
            SQLConnection.addDataToDB(dateInput.getText(), hourInput.getText(), descriptionInput.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dateInput.clear();
        hourInput.clear();
        descriptionInput.clear();
    }

    private void editButtonClicked(Stage primaryStage, ObservableList<Event> allEvents, TableView<Event> table){
        ObservableList<Event> eventSelected;
        eventSelected = table.getSelectionModel().getSelectedItems();
        EditWindow.display(primaryStage,allEvents ,eventSelected, table);

    }


    private void deleteButtonClicked(ObservableList<Event> AllEvents) {
        ObservableList<Event> eventSelected;
        eventSelected = table.getSelectionModel().getSelectedItems();
        for(int i=0;i<AllEvents.size();i++){
            if (eventSelected.get(0).getDescription().equals(AllEvents.get(i).getDescription())) {
                AllEvents.remove(i);
            }
        }
        try {
            SQLConnection.deleteDataFromDB(eventSelected.get(0).getDate(), eventSelected.get(0).getHour(), eventSelected.get(0).getDescription());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //getAllEvents
    public static ObservableList<Event> getEvents(ObservableList<Event> allEvents){
        ObservableList<Event> events = FXCollections.observableArrayList();
        for(int i=0; i<allEvents.size();i++){
            if(allEvents.get(i).getDate().equals(datePicker.getValue())){
                events.add(allEvents.get(i));
            }
        }
        return events;

    }

    public static void refreshData(ObservableList<Event> AllEvents, TableView<Event> table){
        datePicker.setDayCellFactory(colorAllDates(AllEvents));
        table.setItems(getEvents(AllEvents)); //update data
    }
    public static Callback colorAllDates(ObservableList<Event> AllEvents){
        Callback<DatePicker,DateCell> dayCellFactory = new Callback<DatePicker, DateCell>(){
            public DateCell call(final DatePicker datePicker){
                return new DateCell(){
                    public void updateItem(LocalDate item, boolean empty){
                        super.updateItem(item, empty);
                        DayOfWeek day = DayOfWeek.from(item);
                        for(int i=0;i<AllEvents.size();i++){
                            if(item.equals(AllEvents.get(i).getDate())){
                                this.setTextFill(Color.RED);
                                //this.setStyle("-fx-background-color: #ffc0cb;");
                            }
                        }
                    }
                };
            }
        };
        return dayCellFactory;
    }
    public static void readCSSFile(Scene scene){
        File f = new File("CSSFile.css");
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

    }


    public static void main(String[] args) {
        launch(args);
    }
}
