/**
 * 
 */
package youtubeAPI;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jaunt.JauntException;

/**
 * @author danielcantrelle
 *
 */
public class GoogleSearchMod extends ModuleMessenger {
	
	public static final ModuleType MODULE_TYPE = ModuleType.GOOGLE_SEARCH_MOD; 
	
	static Logger LOGGER = Logger.getLogger(Logger.class.getName());

	public static void getClassification(String video_url) throws IOException, TimeoutException{
	    Scraper scraper = new Scraper();
        String category = null;
	    try{
	        category = GoogleSearchClassification.classify(scraper.getTitle(video_url));
        } catch(JauntException e){
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "an exception was thrown in GoogleSearchMod getClassification ", e);
        }
        setupConnection();
        sendClassification(MODULE_TYPE, category, video_url);
        closeConnection();
    }

	GoogleSearchMod(){}

	public static void main(String[] args) {
		try {
			String result;
			result = GoogleSearchClassification.classify("Benjamin Zander");
			System.out.print("\n\nFinal Classification: " + result);
		} catch (JauntException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, "an exception was thrown in GoogleSearchMod main", e);
		}
	}

}
