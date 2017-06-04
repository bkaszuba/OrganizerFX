package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Kaszuba on 26.05.2017.
 */
public class SQLConnection {
    static LocalDate localDate;
    public static void sendDataFromDBToProgram(ObservableList<Event> AllEvents) throws SQLException {
        //Connection
        String connectionString =
                "jdbc:sqlserver://DESKTOP-CO27LSN\\SQLEXPRESS;"
                +"databaseName=Events;"+
                        "integratedSecurity=true;";
        Connection connection = null;
        connection = DriverManager.getConnection(connectionString);

        //Kwerenda
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM Events.dbo.event";
        ResultSet rs = statement.executeQuery(sql);
        //Read from DB
        while (rs.next())
        {
            Date date = rs.getDate(2);
            AllEvents.add(new Event(localDate=date.toLocalDate(), rs.getString(3), rs.getString(4)));
        }
        //Close all
        rs.close();
        statement.close();
        connection.close();
    }

    //INSERT QUERY
    public static void addDataToDB(String date, String hourEvent, String description) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = (LocalDate.parse(date, formatter));
        Date dateToDB = Date.valueOf(localDate);

        //Connection
        String connectionString =
                "jdbc:sqlserver://DESKTOP-CO27LSN\\SQLEXPRESS;"
                        +"databaseName=Events;"+
                        "integratedSecurity=true;";
        Connection connection = null;
        PreparedStatement insertEvent = null;
        connection = DriverManager.getConnection(connectionString);

        //Kwerenda
        Statement statement = connection.createStatement();
        String sql = "INSERT INTO Events.dbo.event" +
                " VALUES('"+dateToDB+"', '"+hourEvent+"', '"+description+"');";

        insertEvent = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS);
        insertEvent.execute();

        connection.close();
        statement.close();
        insertEvent.close();
    }


    public static void updateData(ObservableList<Event> selectedEvent,String date, String hourEvent, String description) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = (LocalDate.parse(date, formatter));
        Date dateToDB = Date.valueOf(localDate);

        //Connection
        String connectionString =
                "jdbc:sqlserver://DESKTOP-CO27LSN\\SQLEXPRESS;"
                        +"databaseName=Events;"+
                        "integratedSecurity=true;";
        Connection connection = null;
        PreparedStatement insertEvent = null;
        connection = DriverManager.getConnection(connectionString);

        //Kwerenda
        Statement statement = connection.createStatement();
        String sql = "Update Events.dbo.event" +
                " SET eventDate = '"+dateToDB+"', eventHour = '"+hourEvent+"', eventDescription = '"+description+"' " +
                "WHERE eventDate = '"+selectedEvent.get(0).getDate()+"' AND eventHour = '"+selectedEvent.get(0).getHour()+"' AND eventDescription = '"+selectedEvent.get(0).getDescription()+"'";
        insertEvent = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS);
        insertEvent.execute();

        connection.close();
        statement.close();
        insertEvent.close();
    }


    //DELETE QUERY
    public static void deleteDataFromDB(LocalDate date, String hourEvent, String description) throws SQLException{

        Date dateToDB = Date.valueOf(date);

        //Connection
        String connectionString =
                "jdbc:sqlserver://DESKTOP-CO27LSN\\SQLEXPRESS;"
                        +"databaseName=Events;"+
                        "integratedSecurity=true;";
        Connection connection = null;
        PreparedStatement deleteEvent = null;
        connection = DriverManager.getConnection(connectionString);

        Statement x;
        //Kwerenda
        Statement statement = connection.createStatement();
        String sql = "DELETE FROM Events.dbo.event" +
                " WHERE(eventDate='"+dateToDB+"' AND eventHour='"+hourEvent+"' AND eventDescription='"+description+"');";

        deleteEvent = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS);
        deleteEvent.execute();

        connection.close();
        statement.close();
        deleteEvent.close();

    }

    public static ObservableList<Event> getSpecificDates(DatePicker from, DatePicker to) throws SQLException {

        ObservableList<Event> AllEvents = FXCollections.observableArrayList();;
        //Connection
        String connectionString =
                "jdbc:sqlserver://DESKTOP-CO27LSN\\SQLEXPRESS;"
                        +"databaseName=Events;"+
                        "integratedSecurity=true;";
        Connection connection = null;
        connection = DriverManager.getConnection(connectionString);

        //Kwerenda
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM Events.dbo.event " +
                "WHERE eventDate BETWEEN '"+from.getValue()+"' AND '"+to.getValue()+"';";
        ResultSet rs = statement.executeQuery(sql);
        //Read from DB
        while (rs.next())
        {
            Date date = rs.getDate(2);
            AllEvents.add(new Event(localDate=date.toLocalDate(), rs.getString(3), rs.getString(4)));
        }
        //Close all
        rs.close();
        statement.close();
        connection.close();
        return AllEvents;
    }

}
