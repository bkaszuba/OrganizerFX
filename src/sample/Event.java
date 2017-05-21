package sample;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Kaszuba on 17.05.2017.
 */
public class Event {

    private LocalDate date;
    private String hour;
    private String description;

    public Event(LocalDate date, String hour, String description){
        this.date= date;
        this.description = description;
        this.hour = hour;
    }
    public Event(){
        this.date= null;
        this.description = null;
        this.hour = null;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
