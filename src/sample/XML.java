package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.*;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaszuba on 04.06.2017.
 */
public class XML {
    public static void saveToXML(String path, ObservableList<Event> allEvents) {
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(path)));
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("ERROR: While Creating or Opening the File");
        }
        encoder.setPersistenceDelegate(LocalDate.class,
                new PersistenceDelegate() {
                    @Override
                    protected Expression instantiate(Object localDate, Encoder out) {
                        return new Expression(localDate,
                                LocalDate.class,
                                "parse",
                                new Object[]{localDate.toString()});
                    }
                });
        encoder.writeObject(new ArrayList<Event>(allEvents));
        encoder.close();
    }

    public static void readFromXML(String path, ObservableList<Event> allNow) {
        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found");
        }
        List<Event> list = (List<Event>) decoder.readObject();
        ObservableList<Event> tmp = FXCollections.observableArrayList();
        tmp.addAll(list);
        Boolean isThere;
        for (int i = 0; i < tmp.size(); i++) {
            isThere = false;
            for (int j = 0; j < allNow.size(); j++) {
                if ((allNow.get(j).getDate().equals(tmp.get(i).getDate())) &&
                        (allNow.get(j).getDescription().equals(tmp.get(i).getDescription()))) {
                    isThere = true;
                }
            }
            if (isThere == false) {
                allNow.add(tmp.get(i));
                String stringDate = tmp.get(i).getDate().toString();
                try {
                    SQLConnection.addDataToDB(stringDate, tmp.get(i).getHour(), tmp.get(i).getDescription());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        decoder.close();
    }
}

