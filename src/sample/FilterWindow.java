package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.joda.time.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by Kaszuba on 29.05.2017.
 */
public class FilterWindow {
    static DatePicker dateFrom;
    static DatePicker dateTo;
    public static void display(Stage stage, ObservableList<Event> AllEvents) throws SQLException {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Filter");
         dateFrom = new DatePicker();
         dateTo = new DatePicker();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse("2017-05-03", formatter);
        dateTo.setValue(localDate);
        dateFrom.setValue(localDate);

        TableColumn<Event, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(70);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        //Hour columnt
        TableColumn<Event, Integer> hourColumn = new TableColumn<>("Hour");
        hourColumn.setMinWidth(70);
        hourColumn.setCellValueFactory(new PropertyValueFactory<>("hour"));

        //Description columnt
        TableColumn<Event, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(240);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableView<Event> table = new TableView<>();
        table.setItems(getEvents(AllEvents));
        System.out.println(AllEvents.size());
        table.getColumns().addAll(dateColumn,hourColumn,descriptionColumn);
        //table.setItems(getEvents(AllEvents, dateFrom, dateTo));

        dateTo.setDayCellFactory(Main.colorAllDates(AllEvents));
        dateFrom.setDayCellFactory(Main.colorAllDates(AllEvents));
        dateFrom.setOnAction(e -> {
            try {
                table.setItems(getEvents(AllEvents));
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        dateTo.setOnAction( e -> {
            try {
                table.setItems(getEvents(AllEvents));
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        HBox datesLayout = new HBox( 20);
        datesLayout.getChildren().addAll(dateFrom, dateTo);
        datesLayout.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(20);
        mainLayout.getChildren().addAll(datesLayout, table);


        Scene scene = new Scene(mainLayout, 400, 250);
        window.setScene(scene);
        window.showAndWait();
    }

    public static ObservableList<Event> getEvents(ObservableList<Event> allEvents) throws SQLException {
        ObservableList<Event> events = FXCollections.observableArrayList();
        events = SQLConnection.getSpecificDates(dateFrom, dateTo);
        return events;
    }


}
