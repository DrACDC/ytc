package youtubeAPI;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationBuilder;

public class ConfigReader {
	
	static String[] configTwitter() throws IOException
	{
		Properties prop=new Properties();
		InputStream input= new FileInputStream("config.properties");
		prop.load(input);
		
		String OAuthConsumerKey = prop.getProperty("OAuthConsumerKey");
		String OAuthConsumerSecret  = prop.getProperty("OAuthConsumerSecret");
		String OAuthAccessToken = prop.getProperty("OAuthAccessToken");
		String OAuthAccessTokenSecret = prop.getProperty("OAuthAccessTokenSecret");

		//System.out.println(OAuthConsumerKey);
		
		return new String[] { OAuthConsumerKey, OAuthConsumerSecret, OAuthAccessToken, OAuthAccessTokenSecret};
	}
	
	static String configYoutube() throws IOException
	{
		Properties prop=new Properties();
		InputStream input= new FileInputStream("config.properties");
		prop.load(input);
		
		String apiKey = prop.getProperty("GoogleKey");
		
		//System.out.println(apiKey);
		
		return apiKey;
	}
	
	public static void main(String[] args) throws IOException
	{
		configTwitter();
		configYoutube();
	}

}
