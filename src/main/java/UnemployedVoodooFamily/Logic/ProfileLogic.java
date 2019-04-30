package UnemployedVoodooFamily.Logic;

import ch.simas.jtoggl.User;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ProfileLogic {


    public String getFirstDayOfWeek(long weekDay){
        if(weekDay == 0){
            return "Sunday";
        }
        else if(weekDay == 1){
            return "Monday";
        }
        else if(weekDay == 2){
            return "Tuesday";
        }
        else if(weekDay == 3){
            return "Wednesday";
        }
        else if(weekDay == 4){
            return "Thursday";
        }
        else if(weekDay == 5){
            return "Friday";
        }
        else if(weekDay == 6){
            return "Saturday";
        }
        else{
            return "no date specified";
        }
    }

    public String getTimeFormat(String format){
        if(format.equals("h:mm A")){
            return "12-hour";
        }
        else if(format.equals("H:mm")){
            return "24-hour";
        }
        else{
            return "no format specified";
        }
    }

    public void browseTogglProfile() {
            String url = "https://toggl.com/app/profile";

            if(Desktop.isDesktopSupported()){
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(url));
                } catch (IOException | URISyntaxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec("xdg-open " + url);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
    }
}
