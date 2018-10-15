package youtubeAPI;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by ericmilton on 4/4/17.
 * This is the Description Module.
 * Seems to work, may need more testing.
 */
public class DescriptionSitesMod extends ModuleMessenger {
	
	private static final ModuleType MODULE_TYPE = ModuleType.DESCRIPTION_SITES_MOD;

    // www.ietf.org/rfc/rfc1738.txt hsa characters that are reserved
	static Logger LOGGER = Logger.getLogger(Logger.class.getName());
	
    private static boolean isASite(String item){
        if(item.length() < 3){
            return false;
        }
        // illegal characters in strings
        if(item.contains("{") || item.contains("}") || item.contains("|") || item.contains("~")
                || item.contains("^") || item.contains("[") || item.contains("]") || item.contains("'"))
            return false;

        if(item.contains(".")){
            String[] parts = item.split("\\.");
            if(parts.length < 2)
                return false;
            if(item.contains("http://www.") || item.contains("https://www."))
                return true;
            if(item.contains(".tv") || item.contains(".gov") || item.contains(".net")
                    || item.contains(".com") || item.contains(".nz") || item.contains(".org")
                    || item.contains(".int") || item.contains(".edu") || item.contains(".mil")
                    || item.contains(".uk") || item.contains("cn") || item.contains("de")
                    || item.contains(".fr"))
                return true;
        }

        return false;
    }

    private static List<String> getURLsFromDescription(String description){
        List<String> urls = new LinkedList<>();

        String [] parts = description.split("\\s+"); // splits up description based on spaces; URLs dont have spaces
        // System.out.println("parts.length is " + parts.length);

        // Attempt to convert each item into an URL.
        for( String item : parts ) try {
            if(item.contains("http")) {
                URL url = new URL(item);
                urls.add(item);
                //System.out.println("Item added: " + item + "\n");
            }else{
                if(isASite(item)){
                    urls.add(item);
                    //System.out.println("Item added: " + item + "\n");
                }
            }
        } catch (MalformedURLException e) {
            //System.err.println("Error in function: getURLsFromDescription");
            //e.printStackTrace();
        	LOGGER.log(Level.SEVERE, "an exception was thrown getURLsFromDescription", e);
            //these are to remain ignored, as they are essentially just making bad URLs to check
        }
        try {
            for (int i = 0; i < urls.size(); i += 1) {
                //System.out.println(urls.get(i));
                String temp = urls.get(i);
                urls.remove(i);
                if (temp.contains("\\n")) {
                    temp = temp.substring(0, temp.indexOf("\\n"));//temp.split("\\n", 2)[0];
                }

                urls.add(i, temp.replace("\\", "")); // get rid of dashes

                String expandedUrl;
                if(urls.get(i).contains("bit.ly")) {
                    expandedUrl = expandUrl(urls.get(i));
                    // System.out.println("Expanded = " + expandedUrl);
                    urls.remove(i);
                    urls.add(i,expandedUrl);
                }
                //System.out.println(urls.get(i));
            }
        }
        catch(Exception e){
            System.err.println("Error in function: getURLsFromDescription");
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "an exception was thrown in getURLsFromDescription ", e);
        }

        if(urls.size() == 0){
            System.out.println("No URLs in description of video");
        }

        return urls;
    }

    // actually visits site - may want to evaluate this
    private static String expandUrl(String shortenedUrl) throws IOException {
        URL url = new URL(shortenedUrl);
        //System.out.println("Shortened URL: " + shortenedUrl);

        // open connection
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
        httpURLConnection.setInstanceFollowRedirects(false);

        // extract location header containing the actual destination URL
        String expandedURL = httpURLConnection.getHeaderField("Location");
        httpURLConnection.disconnect();
        return expandedURL;
    }

    public static String getRelatedWebsites(String url){
        Scraper scraper = new Scraper();

        String youtubeID = scraper.getYoutubeId(url);
        List<String> urlsFromDesc = getURLsFromDescription(scraper.getDescription(youtubeID));
        List<String> classifications = new LinkedList<>();

        for(String subURL : urlsFromDesc){
            //System.out.println(subURL);
            try{
                if(subURL.contains("youtube") && subURL.contains("watch?v")){
                    classifications.add(TextTagsTitleMod.classify(subURL)); // needed for YouTube
                }
                else
                    classifications.add(FortiGuardLeverage.fortiClassify(subURL));
            } catch (Exception e){
                System.err.println("Error in function: getRelatedWebsites");
                e.printStackTrace();
                LOGGER.log(Level.SEVERE, "an exception was thrown in getRelatedWebsites ", e);
            }
        }

        /*if(classifications.contains(<<Dangerous_Tags Here>>)){
            return Dangerous_Tags;
        }*/

        Map<String, Long> occurrences =
                classifications.stream().collect(Collectors.groupingBy(w  -> w, Collectors.counting()));

        String mostFreq = "NA";
        int occurCount = 0;

        for(String s: occurrences.keySet()){
            if(occurrences.get(s) > occurCount) {
                occurCount = occurrences.get(s).intValue();
                mostFreq = s;
            }
        }
        return mostFreq;
    }

    public static void getClassification(String video_url) throws IOException, TimeoutException{
        String category = getRelatedWebsites(video_url);
        setupConnection();
        sendClassification(MODULE_TYPE, category, video_url);
        closeConnection();
    }

    // Just for direct testing
    DescriptionSitesMod(){}

    public static void main(String[] args) throws IOException, TimeoutException{
        DescriptionSitesMod de = new DescriptionSitesMod();
        System.out.println(de.getRelatedWebsites("https://www.youtube.com/watch?v=pMqrMC1VZhA"));
        System.out.println("\n");
        System.out.println(de.getRelatedWebsites("https://www.youtube.com/watch?v=itSTzV29bS0"));


        getClassification("https://www.youtube.com/watch?v=og9_51uRKsQ");
    }
}
