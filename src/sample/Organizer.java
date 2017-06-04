package sample;

import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Created by Kaszuba on 19.05.2017.
 */
public class Organizer {

    public static void writeDataToFile(String filename, ObservableList<Event> AllEvents) throws IOException {
        PrintWriter pw;
        pw = new PrintWriter(new BufferedWriter(new FileWriter(filename, false)));
        for(int i=0;i<AllEvents.size();i++){
            pw.println(AllEvents.get(i).getDate());
            pw.println(AllEvents.get(i).getHour());
            pw.println(AllEvents.get(i).getDescription());
        }
        pw.close();
    }

    public static void readDataFromFile(String filename, ObservableList<Event> AllEvents) throws IOException
    {
        String tmpDate=null;
        String tmpHour=null;
        String tmpDescr=null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int howMuch=0;
        File file = new File(filename);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        while((line = bufferedReader.readLine()) != null)
        {
            if(howMuch==0) tmpDate = line;
            else if(howMuch==1) tmpHour=line;
            else if(howMuch==2){
                tmpDescr=line;
                AllEvents.add(new Event((LocalDate.parse(tmpDate, formatter)), tmpHour, tmpDescr));
                howMuch=-1;
            }
            howMuch++;
        }
        bufferedReader.close();
    }

}
