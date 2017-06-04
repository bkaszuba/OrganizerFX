package sample;
import com.independentsoft.msg.Message;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.HOUR;
import static java.util.Calendar.MINUTE;

/**
 * Created by Kaszuba on 02.06.2017.
 */
public class Outlook {

    public static void saveToOutloock(String path, ObservableList<Event> allEvents) {
        Calendar cl = Calendar. getInstance();
        try {
            for (int i = 0; i < allEvents.size(); i++) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = Date.from(allEvents.get(i).getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

                Message appointment = new Message();
                cl.setTime(date);
                int hour = Integer.parseInt(allEvents.get(i).getHour().substring(0, 2));
                int minute = Integer.parseInt(allEvents.get(i).getHour().substring(3));

                cl.add(HOUR, hour);
                cl.add(MINUTE, minute);

                appointment.setDisplayTo("Programowanie Komponentowe");
                appointment.setDisplayCc("Bartlomiej Kaszuba & Michal Madej");
                appointment.setMessageClass("IPM.Appointment" + i);
                appointment.setSubject("Appointment");
                appointment.setBody(allEvents.get(i).getDescription());
                appointment.setAppointmentStartTime(cl.getTime());
                appointment.save(path+ "apoin" + i + ".msg", true);
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void readfromOutloock(String sciezka, ObservableList<Event> allEvents)
    {

        try
        {
            Message appointment = new Message(sciezka);
            Date input = appointment.getAppointmentStartTime();
            Calendar cl = Calendar. getInstance();
            cl.setTime(input);
            int min = cl.get(MINUTE)+1;
            int hour = cl.get(HOUR);
            String stringHour = hour+":"+min;
            LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Event fromFileEvent = new Event(date, stringHour,appointment.getBody());
            System.out.println(fromFileEvent);
            String stringDate = date.toString();
            Boolean isThere=false;
            for(int i=0;i<allEvents.size();i++){
                if(allEvents.get(i).getDate().equals(fromFileEvent.getDate()) &&
                        allEvents.get(i).getHour().equals(fromFileEvent.getHour()) &&
                        allEvents.get(i).getDescription().equals(fromFileEvent.getDescription())){
                    isThere=true;
                }
            }
            if(isThere==false){
                allEvents.add(fromFileEvent);
                try {
                    SQLConnection.addDataToDB(stringDate, stringHour, appointment.getBody());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}





