package reporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;


public class ExtentReporterNG {
static ExtentReports extent;
private static final Logger baseLogger = LoggerFactory.getLogger(ExtentReporterNG.class);

	
	/*
	 *Stores extent report in report.hmtl
	 *returns extent report object 
	 */
	public static synchronized ExtentReports getReporterObject() {
		if(extent==null) {
		String path=System.getProperty("user.dir")+File.separator+"reports"+File.separator+"report("+getCurrentTimestamp()+")"+".html";
		ExtentSparkReporter reporter=new ExtentSparkReporter(path);
		reporter.config().setReportName("KitchenFlow Automation Results");
		reporter.config().setDocumentTitle("Test Results");
		extent=new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Environment", readEnvironmentFromFile());
		}
		return extent;
	}
	
	 private static String getCurrentTimestamp() {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
	        return LocalDateTime.now().format(formatter);
	    }
	
	private static String readEnvironmentFromFile() {
        StringBuilder content = new StringBuilder();
        String filePath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"data"+File.separator+"environment.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
        	baseLogger.error("IOException on reading environment "+e);
        }
        return content.toString();
    }
}
