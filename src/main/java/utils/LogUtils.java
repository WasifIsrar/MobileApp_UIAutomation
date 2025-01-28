package utils;

import java.util.stream.StreamSupport;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.appium.java_client.android.AndroidDriver;

public class LogUtils {
	private static final Logger logsLogger = LoggerFactory.getLogger(LogUtils.class);
	public static void captureLogcat(AndroidDriver driver) {
		LogEntries logs = driver.manage().logs().get("logcat");
		int count=logs.getAll().size();
		if(count>0) {
			if(count<10) {
				for(LogEntry log:logs) {
					logsLogger.info(log.toString());
		        }
			}
			else {
				logsLogger.info("First and last ten lines of log: ");
		        StreamSupport.stream(logs.spliterator(), false).limit(10).forEach(logItem -> logsLogger.info(logItem.toString()));
		        logsLogger.info("...");
		        StreamSupport.stream(logs.spliterator(), false).skip(logs.getAll().size() - 10).forEach(logItem -> logsLogger.info(logItem.toString()));
			}
		}
        
        
	}
}
