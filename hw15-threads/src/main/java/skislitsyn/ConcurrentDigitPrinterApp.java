package skislitsyn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConcurrentDigitPrinterApp {
    private static final Logger logger = LoggerFactory.getLogger(ConcurrentDigitPrinterApp.class);
    private String last = "Thread2";

    public static void main(String[] args) throws Exception {
	ConcurrentDigitPrinterApp app = new ConcurrentDigitPrinterApp();
	Thread t1 = new Thread(() -> app.printDigits());
	Thread t2 = new Thread(() -> app.printDigits());
	t1.setName("Thread1");
	t2.setName("Thread2");
	t1.start();
	t2.start();
    }

    private synchronized void printDigits() {
	DigitPrinter digitPrinter = new DigitPrinter();
	while (true) {
	    try {
		while (last.equals(Thread.currentThread().getName())) {
		    this.wait();
		}
		last = Thread.currentThread().getName();
		System.out.print(Thread.currentThread().getName() + ": ");
		digitPrinter.printNext();
		Thread.sleep(500);
		notifyAll();
	    } catch (InterruptedException e) {
		Thread.currentThread().interrupt();
	    }
	}
    }
}
