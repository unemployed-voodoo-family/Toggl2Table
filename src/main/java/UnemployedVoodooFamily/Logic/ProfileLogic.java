package UnemployedVoodooFamily.Logic;

import ch.simas.jtoggl.User;

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
}
