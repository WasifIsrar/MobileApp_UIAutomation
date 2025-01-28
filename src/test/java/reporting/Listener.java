package reporting;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.LoggerFactory;
import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import ch.qos.logback.classic.Logger;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import utils.AppiumUtils;

public class Listener extends AppiumUtils implements ITestListener,IConfigurationListener{
	private static final ExtentReports extent=ExtentReporterNG.getReporterObject();
    private ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();
    private ThreadLocal<AppiumDriver> driverThreadLocal = new ThreadLocal<>();

	@Override
	public void onTestStart(ITestResult result) {
		ExtentTest test=extent.createTest(result.getTestClass().getName().substring(6)+"."+result.getMethod().getMethodName());
		testThreadLocal.set(test);
	    try {
	    	driverThreadLocal.set((AppiumDriver) result.getTestClass().getRealClass().getField("driver1")
	                    .get(result.getInstance()));
		} catch (Exception e) {
		}
	    try {
	    	((CanRecordScreen) driverThreadLocal.get()).startRecordingScreen();
	    }
	    catch(Exception e) {
	    }
	}
		
	@Override
	public void onTestSuccess(ITestResult result) {
		testThreadLocal.get().log(Status.PASS, "Test Passed");
		((CanRecordScreen) driverThreadLocal.get()).stopRecordingScreen();
	}
	
	@Override
	public void onTestFailure(ITestResult result) {
		testThreadLocal.get().fail(result.getThrowable());
		testThreadLocal.get().addScreenCaptureFromBase64String(getScreenshotAsBase64(driverThreadLocal.get()),"Screenshot");

		try {
			stopRecording(result.getMethod().getMethodName());
			}
			catch(Exception e) {
			}
	}
	
	@Override
	public void onTestSkipped(ITestResult result) {
		testThreadLocal.get().skip(result.getThrowable());
	}
	
	@Override
	public void onFinish(ITestContext context) {
		extent.flush();
	}
	
	public void stopRecording(String methodName) throws IOException {

	       String media = ((CanRecordScreen) driverThreadLocal.get()).stopRecordingScreen();
	       String dirPath =   System.getProperty("user.dir")+File.separator+"videos";
	       File videoDir = new File(dirPath);
	       FileOutputStream stream = null;
	       
	    try {
	      stream = new FileOutputStream(videoDir + File.separator +methodName+".mp4");
	            stream.write(Base64.decodeBase64(media));
	            stream.close();

	        } catch (Exception e) {
	        } finally {
	            if(stream != null) {
	                stream.close();
	            }
	        }
	    }
}


