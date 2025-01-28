package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import basePage.BasePage;

public class FileUtility {
    private static final Logger baseLogger = LoggerFactory.getLogger(BasePage.class);

	public static String readPinFromFile() {
        StringBuilder content = new StringBuilder();
        String filePath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"data"+File.separator+"pin.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
        	baseLogger.error("IOException on Reading Pin from File "+e);
        }
        return content.toString();
    }
	
	public static String readMposPinFromFile() {
        StringBuilder content = new StringBuilder();
        String filePath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"data"+File.separator+"mPosPin.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
        	baseLogger.error("IOException on Reading Pin from File "+e);
        }
        return content.toString();
    }
	
	public static String readEnvironmentFromFile() {
        StringBuilder content = new StringBuilder();
        String filePath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"data"+File.separator+"environment.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int character;
            while ((character = reader.read()) != -1) {
                content.append((char)character);
            }
        } catch (IOException e) {
        	baseLogger.error("IOException on Reading Environment From File "+e);
        }
        return content.toString();
    }
}
