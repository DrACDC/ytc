
package youtubeAPI;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Logging {
	
	static Logger LOGGER = Logger.getLogger(Logger.class.getName());
	
	public static void setupLogger() throws SecurityException, IOException{
		
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss") ;
		FileHandler filehandler= new FileHandler("log_"+ dateFormat.format(date) + ".txt");
		SimpleFormatter formattertxt;
		
		LOGGER.setLevel(Level.INFO);
		LOGGER.setUseParentHandlers(false);
		formattertxt= new SimpleFormatter();
		filehandler.setFormatter(formattertxt);
		LOGGER.addHandler(filehandler);
		
	}
	
	public void doSomeThingAndLog() {

        LOGGER.setLevel(Level.INFO);
        LOGGER.severe("Info Log");

        
        Exception e1 = new Exception();
        Exception e2 = new Exception(e1);
        LOGGER.log(Level.SEVERE, "an exception was thrown", e2);
	}
	
	Logging() {
		try {
			setupLogger();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	
	public static void main(String[] args) throws SecurityException, IOException{
		Logging tester= new Logging();
		
		Logging.setupLogger();
	
		tester.doSomeThingAndLog();
	
	}
}
