package skislitsyn;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import skislitsyn.testfw.After;
import skislitsyn.testfw.Before;
import skislitsyn.testfw.Test;

public class MySimpleCalendarTest {
    private MySimpleCalendar mySimpleCalendar;

    @Before
    public void initializeTest() {
	System.out.println("Starting new test");
	mySimpleCalendar = new MySimpleCalendar();
    }

    @After
    public void finalizeTest() {
	System.out.println("Test completed");
    }

    @Test
    public void shouldReturnValidDate() {
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.DAY_OF_YEAR, 1); // tomorrow
	String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
	if (currentDate.equals(mySimpleCalendar.getDate())) {
	    System.out.println("Date retruned is valid");
	} else {
	    System.out.println("Date retruned is invalid");
	    throw new AssertionError("Date retruned is invalid");
	}
    }

    @Test
    public void shouldReturnValidDateTime() {
	Calendar calendar = Calendar.getInstance();
	String currentDateTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(calendar.getTime());
	if (currentDateTime.equals(mySimpleCalendar.getDateTime())) {
	    System.out.println("DateTime retruned is valid");
	} else {
	    System.out.println("DateTime retruned is invalid");
	    throw new AssertionError("DateTime retruned is invalid");
	}
    }
}
