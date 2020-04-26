package skislitsyn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import skislitsyn.utils.GCMonitor;

/*
 * JVM arguments for testing:
 *     -Xms1G
 *     -Xmx1G
 *     -XX:+UseG1GC or -XX:+UseParallelGC
 *     -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
*/

public class GCTestingApp {
    private static final int BATCH_SIZE = 100;
    private static final int BYTE_ARRAY_LENGTH = 100 * 1024;
    private static final int ITERATIONS_COUNT = 1000;
    private static final int SLEEP_DURATION = 1000;

    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private static boolean gcMonitoringOn = false;

    public static void main(String[] args) {
	enableGCMonitoring();

	ArrayList<byte[]> arrayList = new ArrayList<>();

	for (int i = 0; i < ITERATIONS_COUNT; i++) {
	    for (int j = 0; j < BATCH_SIZE; j++) {
		arrayList.add(getRandomByteArray(BYTE_ARRAY_LENGTH));
	    }

	    for (int j = 0; j < BATCH_SIZE / 2; j++) {
		arrayList.remove(arrayList.size() - 1);
	    }

//	    System.out.println("Current iteration: " + String.valueOf(i + 1));
//	    System.out.println("Current list size: " + arrayList.size());

	    try {
		Thread.sleep(SLEEP_DURATION);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}

	disableGCMonitoring();
    }

    private static void enableGCMonitoring() {
	GCMonitor.enableGCMonitoring();
	gcMonitoringOn = true;

	Thread mainThread = Thread.currentThread();
	Runnable task = () -> {
	    while (gcMonitoringOn) {
		if (!mainThread.isAlive()) {
		    gcMonitoringOn = false;
		}
		try {
		    Thread.sleep(60000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -1);
		GCMonitor.printStatistics(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(calendar.getTime()));
	    }

	};
	executor.execute(task);
	executor.shutdown();
    }

    private static void disableGCMonitoring() {
	GCMonitor.disableGCMonitoring();
	gcMonitoringOn = false;
    }

    private static byte[] getRandomByteArray(int length) {
	byte[] result = new byte[length];
	for (int i = 0; i < length; i++) {
	    result[i] = (byte) (128 * Math.random());
	}
	return result;
    }
}
