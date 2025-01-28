package reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;

public class SecondListener implements ITestListener{
	AppiumDriver driver;
	
	@Override
	public void onTestStart(ITestResult result) {
		try {
			driver=(AppiumDriver) result.getTestClass().getRealClass().getField("driver2")
					.get(result.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    try {
	    	((CanRecordScreen) driver).startRecordingScreen();
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	@Override
	public void onTestSuccess(ITestResult result) {
		((CanRecordScreen) driver).stopRecordingScreen();
	}
	
	@Override
	public void onTestFailure(ITestResult result) {
		try {
			stopRecording(result.getMethod().getMethodName());
			}
			catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	
	public void stopRecording(String methodName) throws IOException {

	       String media = ((CanRecordScreen) driver).stopRecordingScreen();
	       String dirPath =   System.getProperty("user.dir")+File.separator+"videos";
	       File videoDir = new File(dirPath);
	       FileOutputStream stream = null;
	       
	    try {
	      stream = new FileOutputStream(videoDir + File.separator +methodName+"_KDS.mp4");
	            stream.write(Base64.decodeBase64(media));
	            stream.close();

	        } catch (Exception e) {
	        	e.printStackTrace();
	        } finally {
	            if(stream != null) {
	                stream.close();
	            }
	        }
	    }
}
