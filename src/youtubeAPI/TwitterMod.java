package youtubeAPI;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.*;

public class TwitterMod extends ModuleMessenger {

	private static final ModuleType MODULE_TYPE = ModuleType.TWITTER_MOD;
	
	static Logger LOGGER = Logger.getLogger(Logger.class.getName());
	static Twitter twitter;

	static void setupTwitter() throws IOException
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(new ConfigReader().configTwitter()[0]);
		cb.setOAuthConsumerSecret(new ConfigReader().configTwitter()[1]);
		cb.setOAuthAccessToken(new ConfigReader().configTwitter()[2]);
		cb.setOAuthAccessTokenSecret(new ConfigReader().configTwitter()[3]);

		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}

	static List<String> getTweets(String searchquery) throws TwitterException, IOException{
		setupTwitter();
        List<String> list = new LinkedList<>();

        try {
			twitter4j.Query query = new twitter4j.Query(searchquery);
			//query.setCount(10);
			QueryResult result;

            result = twitter.search(query);
            java.util.List<twitter4j.Status> tweets = result.getTweets();
            int tweetsSize=tweets.size();
            for (int i=0; i<25 && i<tweetsSize; i++) {
                twitter4j.Status tweet= tweets.get(i);
                //System.out.println(/*"@" + tweet.getUser().getScreenName().toLowerCase()
                //        + " - " + */tweet.getText().toLowerCase());
                list.add(tweet.getText().toLowerCase());
            }
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			LOGGER.log(Level.SEVERE, "an exception was thrown", te);
			
		}
		return list;
	}

	private static String getCategoryFromTweets(List<String> tweets){
	    TextTagsTitleMod classifier = new TextTagsTitleMod();
        return classifier.classify(tweets);
    }

    public static String classifyTwitterFeed(String url){
        Scraper scraper = new Scraper();
        //String title = scraper.getTitle(url);
        String title= "s djjjebxfhdhbhjdbjbdhabashjbfhebhfbhjsbdjkcd m";
        List<String> tweets = null;
        try{
            tweets = getTweets(title);
        } catch (Exception e){
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "an exception was thrown", e);
        }
        String category = getCategoryFromTweets(tweets);
        return category;
    }

    public static void getClassification(String video_url) throws IOException, TimeoutException{
        String category = classifyTwitterFeed(video_url);
        setupConnection();
        sendClassification(MODULE_TYPE, category, video_url);
        closeConnection();
    }

    TwitterMod(){}

	public static void main(String[] args) throws TwitterException, IOException {
		/*String searchquery="Chronicles of Narnia";
		List<String> tweets = getTweets(searchquery);
        getCategoryFromTweets(tweets);*/
		classifyTwitterFeed("https://www.youtube.com/watch?v=qxKGB13nPCs");

        System.out.println("\n\n");
        classifyTwitterFeed("https://www.youtube.com/watch?v=A9294exNvJQ");
	}
}
