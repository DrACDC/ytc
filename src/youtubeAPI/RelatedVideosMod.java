package youtubeAPI;

import com.jaunt.JNode;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by ericmilton on 4/10/17.
 */
public class RelatedVideosMod extends ModuleMessenger {
	
	private static final ModuleType MODULE_TYPE = ModuleType.RELATED_VIDEOS_MOD;
	
	static Logger LOGGER = Logger.getLogger(Logger.class.getName());
	
    public static String getYoutubeId(String urlName){
        if(urlName.contains("youtube") && urlName.contains("watch?v"))
            return urlName.substring(urlName.length() - 11);
        else{
            System.out.println("Improper video - must be youtube url");
            return "ERR";
        }
    }

    public static List<String> getRelatedVideoIds(String relatedVideos){
        List<String> list = new LinkedList<>();

        int firstQuote = 0;

        for(int i = 0; i < relatedVideos.length(); i += 1){
            if(relatedVideos.charAt(i) == '\"'){
                if(firstQuote != 0){
                    list.add(relatedVideos.substring(firstQuote,i));
                    firstQuote = 0;
                }else{
                    firstQuote = i + 1;
                }
            }
        }

        // System.out.println(relatedVideos);

        return list;
    }

    public static String runRelatedClassification(String url){
        String videoId = getYoutubeId(url);
        String relatedVideos = getRelatedVideos(videoId);
        //System.out.println("Here: " + relatedVideos);

        List<String> relatedVideoIds = getRelatedVideoIds(relatedVideos);
        List<String> classifications = new LinkedList<>();

        for(String relVideoId: relatedVideoIds){
            //System.out.println(relVideoId);
            try{
                classifications.add(
                        TextTagsTitleMod.classify("https://www.youtube.com/watch?v=" + relVideoId));
            }
            catch(Exception e){
                System.out.println("Error in RelatedVideosMod");
                LOGGER.log(Level.SEVERE, "an exception was thrown in RelatedVideosMod", e);
                e.printStackTrace();
            }
        }

        Map<String, Long> occurrences =
                classifications.stream().collect(Collectors.groupingBy(w  -> w, Collectors.counting()));

        String mostFreq = "ERR";
        int occurCount = 0;

        for(String s: occurrences.keySet()){
            if(occurrences.get(s) > occurCount) {
                occurCount = occurrences.get(s).intValue();
                mostFreq = s;
            }
        }
        return mostFreq;
    }

    public static String getRelatedVideos(String video_id){
        try {
            UserAgent userAgent = new UserAgent(); //create new userAgent (headless browser).
            userAgent.sendGET("https://www.googleapis.com/youtube/v3/search?part=snippet&relatedToVideoId=" +
                    video_id + "&type=video&key=" +
                    "AIzaSyAwBpR_XiTmp7mmY3Bgzt0NGpwcLeS5M1Q"); //send request
            JNode relatedVideos = userAgent.json;
            JNode relatedVideosArr = relatedVideos.findEach("videoId");
            //System.out.println(relatedVideosArr.toString());
            return relatedVideosArr.toString();
        }
        catch(JauntException e){
            System.err.println("Error in function: getRelatedVideos");
            LOGGER.log(Level.SEVERE, "an exception was thrown in getRelatedVideos", e);
            System.err.println(e);
        }
        return null;
    }

    public static void getClassification(String video_url) throws IOException, TimeoutException{
        String category = runRelatedClassification(video_url);
        setupConnection();
        sendClassification(MODULE_TYPE, category, video_url);
        closeConnection();
    }

    RelatedVideosMod(){}

    public static void main(String[] args){
        String videoURL = "https://www.youtube.com/watch?v=lnJ3RhT47T0";
        System.out.println(runRelatedClassification(videoURL));
    }
}
