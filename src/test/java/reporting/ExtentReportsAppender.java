package reporting;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class ExtentReportsAppender extends AppenderBase<ILoggingEvent>{	
	private static ExtentTest test;

	public void setExtentTest(ExtentTest testInstance) {
        test = testInstance;
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (test != null) {
            switch (event.getLevel().levelInt) {
                case Level.INFO_INT:
                    test.log(Status.INFO, event.getFormattedMessage());
                    break;
                case Level.WARN_INT:
                    test.log(Status.WARNING, event.getFormattedMessage());
                    break;
            }
        }
    }
}

