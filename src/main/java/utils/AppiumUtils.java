package utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;

import io.appium.java_client.AppiumDriver;

public class AppiumUtils {
	public String getScreenshotPath(String testCaseName,AppiumDriver driver) throws IOException{
		File source=driver.getScreenshotAs(OutputType.FILE);
		String destinationFile=System.getProperty("user.dir") + File.separator + "reports" + File.separator + testCaseName + ".png";
		FileUtils.copyFile(source, new File(destinationFile));
		return destinationFile;
	}
	
	public String getScreenshotAsBase64(AppiumDriver driver) {
		return driver.getScreenshotAs(OutputType.BASE64);
	}
}
