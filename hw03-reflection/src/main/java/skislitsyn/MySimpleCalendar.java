package skislitsyn;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MySimpleCalendar {
    public String getDate() {
	return new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
    }

    public String getDateTime() {
	return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
