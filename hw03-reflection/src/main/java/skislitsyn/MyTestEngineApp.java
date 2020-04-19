package skislitsyn;

import skislitsyn.testfw.MyTestEngine;

public class MyTestEngineApp {

    public static void main(String[] args) {
	MyTestEngine myTestEngine = new MyTestEngine(MySimpleCalendarTest.class);
	myTestEngine.doTesting();
    }
}
