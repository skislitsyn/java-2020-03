package skislitsyn.utils;

import java.lang.management.GarbageCollectorMXBean;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.management.ListenerNotFoundException;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import com.sun.management.GarbageCollectionNotificationInfo;

public class GCMonitor {
    private final static HashMap<String, GCStatistics> gcGlobalStatistics = new HashMap<>();
    private final static HashMap<NotificationEmitter, NotificationListener> listenersRegistered = new HashMap<>();

    public static void enableGCMonitoring() {
	System.out.println("Starting GC monitoring at " + getTimeStamp());
	List<GarbageCollectorMXBean> gcBeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
	for (GarbageCollectorMXBean gcBean : gcBeans) {
	    System.out.println("GC name: " + gcBean.getName());

	    NotificationEmitter emitter = (NotificationEmitter) gcBean;

	    if (listenersRegistered.containsKey(emitter)) {
		System.out.println("GC monitoring already enabled");
		return;
	    }

	    NotificationListener listener = new NotificationListener() {

		@Override
		public void handleNotification(Notification notification, Object handback) {
		    if (notification.getType()
			    .equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
			GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo
				.from((CompositeData) notification.getUserData());
			String timeStamp = getTimeStamp();

			GCStatistics gcStatistics;
			if (gcGlobalStatistics.containsKey(timeStamp)) {
			    gcStatistics = gcGlobalStatistics.get(timeStamp);
			} else {
			    gcStatistics = new GCMonitor.GCStatistics();
			}

			gcStatistics.setGcDuration(gcStatistics.getGcDuration() + info.getGcInfo().getDuration());
			switch (info.getGcAction()) {
			case "end of minor GC":
			    gcStatistics.setYoungGCCount(gcStatistics.getYoungGCCount() + 1);
			    break;
			case "end of major GC":
			    gcStatistics.setOldGCCount(gcStatistics.getOldGCCount() + 1);
			    break;
			}

			gcGlobalStatistics.put(timeStamp, gcStatistics);
		    }
		}
	    };

	    // Add the listener
	    emitter.addNotificationListener(listener, null, null);
	    listenersRegistered.put(emitter, listener);
	}
    }

    public static void disableGCMonitoring() {
	System.out.println("Stopping GC monitoring at " + getTimeStamp());
	listenersRegistered.forEach((k, v) -> {
	    try {
		k.removeNotificationListener(v);
	    } catch (ListenerNotFoundException e) {
		e.printStackTrace();
	    }
	});
    }

    public static void printStatistics(String timeStamp) {
	System.out.println(timeStamp + " " + gcGlobalStatistics.get(timeStamp));
    }

    static class GCStatistics {
	private long youngGCCount = 0;
	private long oldGCCount = 0;
	private long gcDuration = 0;

	public long getYoungGCCount() {
	    return youngGCCount;
	}

	public void setYoungGCCount(long youngGCCount) {
	    this.youngGCCount = youngGCCount;
	}

	public long getOldGCCount() {
	    return oldGCCount;
	}

	public void setOldGCCount(long oldGCCount) {
	    this.oldGCCount = oldGCCount;
	}

	public long getGcDuration() {
	    return gcDuration;
	}

	public void setGcDuration(long gcDuration) {
	    this.gcDuration = gcDuration;
	}

	@Override
	public String toString() {
	    return "youngGCCount=" + String.valueOf(youngGCCount) + "; oldGCCount=" + String.valueOf(oldGCCount)
		    + "; gcDuration=" + String.valueOf(gcDuration);
	}
    }

    private static String getTimeStamp() {
	return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());
    }
}
