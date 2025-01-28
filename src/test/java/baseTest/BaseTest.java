package baseTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class BaseTest {
	public String appEnvironment;
    private static final Logger baseLogger = LoggerFactory.getLogger(BaseTest.class);
	/*
	 * Gets environment from xml file
	 * Stores environment to file
	 * Creates object of Environment interface
	 * Stores in testEnvironment variable
	 * Stores pin to file
	 */
	@BeforeTest(alwaysRun=true)
	@Parameters({"environment"})
	public void beforeTest(String environment){
		baseLogger.info("ENVIRONMENT IS:"+environment);
		appEnvironment=environment;
		writeToFile(appEnvironment,System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"data"+File.separator+"environment.txt");
	}
	
	/*
	 * Writes String to file
	 */
	public void writeToFile(String data,String path) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(data);
        } catch (IOException e) {
            baseLogger.error("Write To File Failed "+e);
        }
	}
	
	/*
	 * Sends report.html as email attachment
	 */
	@AfterSuite(alwaysRun=true)
	public void sendEmail(ITestContext testContext) {
		String suiteName=testContext.getSuite().getName();
		try {
		createZip();
		}
		catch(Exception e) {
			baseLogger.error("Zip Creation Failed "+e);
		}
		final String username = System.getProperty("from");
        final String Emailpassword = System.getProperty("password");
        
        // create properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587"); // TLS port
        props.put("mail.debug", "true");

        // authenticate and create session
        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, Emailpassword);
                    }
                });      	
        String recipients = System.getProperty("to");
        if (recipients == null || recipients.isEmpty()) {
            baseLogger.info("No recipient defined in the environment variable.");
            return;
        }

        String[] recipientAddresses = recipients.split(",");
        InternetAddress[] addresses = new InternetAddress[recipientAddresses.length];
        for (int i = 0; i < recipientAddresses.length; i++) {
            try {
                addresses[i] = new InternetAddress(recipientAddresses[i].trim());
            } catch (AddressException e) {
            	baseLogger.error("Invalid Address "+e);
            }
        }
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject(testContext.getSuite().getName() +" "+appEnvironment.toUpperCase()+" Environment"+  " Automated Tests Results");

            // Create the HTML content
            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><body>");
            htmlContent.append("<p>").append(suiteName).append(" Automated Tests Results "+appEnvironment.toUpperCase()+" Environment"+"</p>");
            htmlContent.append("<p>Find Report Here: <a href='https://qa-automations-artefacts.s3.us-west-2.amazonaws.com/reports/")
                       .append(getHtmlFile(System.getProperty("user.dir") + File.separator + "reports"))
                       .append("'>Report</a></p>");
            htmlContent.append("<p>Find Recording Of Failed Tests Here: <a href='https://qa-automations-artefacts.s3.us-west-2.amazonaws.com/testrecording/")
                       .append(getZipFile(System.getProperty("user.dir") + File.separator + "videos"))
                       .append("'>Recording</a></p>");
            htmlContent.append("</body></html>");

            // Set the email content
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlContent.toString(), "text/html; charset=UTF-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            baseLogger.error("Email Not Sent " + e);
        }

    } 
	       	
	public void createZip() throws IOException{
		List<String> files;
		try (Stream<Path> walk = Files.walk(Paths.get(System.getProperty("user.dir")+File.separator+"videos"))) {
            			files= walk.filter(Files::isRegularFile)
                       .filter(path -> path.toString().toLowerCase().endsWith(".mp4"))
                       .map(Path::toString)
                       .collect(Collectors.toList());
        }
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(System.getProperty("user.dir")+File.separator+"videos"+File.separator+"recording("+getCurrentTimestamp()+").zip"))) {
	        for (String file : files) {	        	
	            try (FileInputStream fis = new FileInputStream(file)) {
	            // Create a zip entry for each file.
	            ZipEntry zipEntry = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
	            zos.putNextEntry(zipEntry);

	            // Read file content and write it to the zip output stream.
	            byte[] bytes = new byte[1024];
	            int length;
	            while ((length = fis.read(bytes)) >= 0) {
	                zos.write(bytes, 0, length);
	            }

	            // Close the current entry and move to the next one.
	            zos.closeEntry();
	            fis.close();
	            }
	        }
		}
	}
	
	public static String getHtmlFile(String directoryPath) {
        Path dirPath = Paths.get(directoryPath);
        
        if (!Files.exists(dirPath)) {
            System.out.println("Directory does not exist: " + directoryPath);
            return null;
        }
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath,"*.html")) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    return entry.getFileName().toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static String getZipFile(String directoryPath) {
        Path dirPath = Paths.get(directoryPath);
        
        if (!Files.exists(dirPath)) {
            System.out.println("Directory does not exist: " + directoryPath);
            return null;
        }
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath,"*.zip")) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    return entry.getFileName().toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	private static String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return LocalDateTime.now().format(formatter);
    }

	protected Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }
	
	public double calculateTax(double amount) {
		double tax1=roundOff(amount*0.01);
		double tax2=roundOff(amount*0.01875);
		double tax3=roundOff(amount*0.0025);
		double tax4=roundOff(amount*0.0025);
		double tax5=roundOff(amount*0.06);
		return roundHalfUp(tax1+tax2+tax3+tax4+tax5);
	}
	public double calculateTakeoutTax(double amount) {
		double tax1=roundOff(amount*0.1);
		System.out.println("Tax1 wro:"+amount*0.01);

		System.out.println("Tax1:"+tax1);
		double tax2=roundOff(amount*0.01875);
		System.out.println("Tax2 wro:"+amount*0.01875);

		System.out.println("Tax2:"+tax2);
		double tax3=roundOff(amount*0.0025);
		System.out.println("Tax3 wro:"+amount*0.0025);

		System.out.println("Tax1:"+tax3);
		double tax4=roundOff(amount*0.0025);
		System.out.println("Tax4 wro:"+amount*0.0025);

		System.out.println("Tax1:"+tax4);
//		double tax5=roundOff(amount*0.06);
//		System.out.println("Tax5 wro:"+amount*0.06);
//
//		System.out.println("Tax1:"+tax5);
		double tax5=roundOff(amount*0.01);
		System.out.println("Tax5 wro:"+amount*0.01);

		System.out.println("Tax5:"+tax5);
		return roundHalfUp(tax1+tax2+tax3+tax4+tax5);
	}
	public double calculateTakeoutServiceTax(double amount) {
		double tax1=roundHalfUp(amount*0.1);
		System.out.println("Tax1 wro:"+amount*0.01);

		System.out.println("Tax1:"+tax1);
		double tax2=roundHalfUp(amount*0.01875);
		System.out.println("Tax2 wro:"+amount*0.01875);

		System.out.println("Tax2:"+tax2);
		double tax3=roundHalfUp(amount*0.0025);
		System.out.println("Tax3 wro:"+amount*0.0025);

		System.out.println("Tax1:"+tax3);
		double tax4=roundHalfUp(amount*0.0025);
		System.out.println("Tax4 wro:"+amount*0.0025);

		System.out.println("Tax1:"+tax4);
		double tax5=roundHalfUp(amount*0.01);
		System.out.println("Tax5 wro:"+amount*0.01);

		System.out.println("Tax1:"+tax5);
		return roundHalfUp(tax1+tax2+tax3+tax4+tax5);
	}
	public double calculateServiceTax(double amount) {
		double tax1=roundHalfUp(amount*0.01);
		double tax2=roundHalfUp(amount*0.01875);
		double tax3=roundHalfUp(amount*0.0025);
		double tax4=roundHalfUp(amount*0.0025);
		double tax5=roundHalfUp(amount*0.06);
		return roundHalfUp(tax1+tax2+tax3+tax4+tax5);
	}
	
	public double roundHalfEven(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }
	
	public double roundOff(double amount) {
		String[] parts=Double.toString(amount).split("\\.");
		if(parts.length>1&&parts[1].length()>2&&parts[1].charAt(2) == '5') {
			 char secondDecimalPlace = Double.toString(amount).split("\\.")[1].charAt(1);
	            if (Character.getNumericValue(secondDecimalPlace) % 2 == 0) {
	                return Math.floor(amount * 100) / 100.0;
	            } else {
	                return Math.ceil(amount * 100) / 100.0;
	            }
	        } else {
	            return Math.round(amount * 100) / 100.0;
	        }
	}
	
	 
	 public double roundHalfUp(double value) {
	        BigDecimal bd = new BigDecimal(Double.toString(value));
	        bd = bd.setScale(2, RoundingMode.HALF_UP);
	        return bd.doubleValue();
	 }
}
