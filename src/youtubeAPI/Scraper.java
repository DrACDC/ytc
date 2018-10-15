package youtubeAPI;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jaunt.*;

import twitter4j.TwitterException;

public class Scraper {
	
	static Logger LOGGER = Logger.getLogger(Logger.class.getName());

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws NotFound, IOException {

		  try{
			  UserAgent userAgent = new UserAgent(); //create new userAgent (headless browser).
			  userAgent.sendGET("https://www.googleapis.com/youtube/v3/videos" +
					  "?key=AIzaSyAwBpR_XiTmp7mmY3Bgzt0NGpwcLeS5M1Q&fields=" +
					  "items(contentDetails(contentRating),snippet" +
					  "(title,description,tags,categoryId))&part=" +
					  "snippet&id=bek1y2uiQGA"); //send request
			  System.out.println(userAgent.json);
			  JNode categoryId = userAgent.json.findFirst("categoryId");
			  System.out.println("categoryId: " + categoryId);
			  YoutubeMapping ym= new YoutubeMapping();
			  Map hm= YoutubeMapping.getHm();
			  

			  System.out.println("The category is: " + hm.get(categoryId.toInt()));
			  String youtubeURL="https://www.youtube.com/watch?v=bek1y2uiQGA";
			  JNode tags = userAgent.json.findFirst("tags");
			  System.out.println("tags: " + tags);
			  
			  JNode title= userAgent.json.findFirst("title");
			  System.out.println("title: " + title);
			  //getTweets(title.toString());
			  getTweets(youtubeURL);
			  	
			  }
		  catch(JauntException e){
			  LOGGER.log(Level.SEVERE, "an exception was thrown", e);
				  System.err.println(e);
		  }
	}

	// case-insensitivity is important

    // was id, now url
	String getTags(String video_url){
		try{
		    String video_id = getYoutubeId(video_url);
		    //String video_id = getYoutubeId(video_url);
			UserAgent userAgent = new UserAgent(); //create new userAgent (headless browser).
			userAgent.sendGET("https://www.googleapis.com/youtube/v3/videos?key=" +
					"AIzaSyAwBpR_XiTmp7mmY3Bgzt0NGpwcLeS5M1Q&fields=items(" +
					"contentDetails(contentRating),snippet(title,description,tags,categoryId))" +
					"&part=snippet&id=" + video_id); //send request
			//System.out.println(userAgent.json);
			JNode categoryId = userAgent.json.findFirst("categoryId");
			// System.out.println("categoryId: " + categoryId);
			YoutubeMapping ym= new YoutubeMapping();
			Map hm= YoutubeMapping.getHm();
			// System.out.println("The category is: " + hm.get(categoryId.toInt()));

			JNode tags = userAgent.json.findFirst("tags");
			System.out.println("tags: " + tags);
			return tags.toString().toLowerCase();
		}
		catch(JauntException e){
			System.err.println(e);
			LOGGER.log(Level.SEVERE, "an exception was thrown", e);
		}
		return "ERR";
	}

	String getTitle(String video_url){
        String[] retList = {"",""};
        try{
            String video_id = getYoutubeId(video_url);
            UserAgent userAgent = new UserAgent(); //create new userAgent (headless browser).
            userAgent.sendGET("https://www.googleapis.com/youtube/v3/videos?key=" +
                    "AIzaSyAwBpR_XiTmp7mmY3Bgzt0NGpwcLeS5M1Q&fields=items(" +
                    "contentDetails(contentRating),snippet(title,description,tags,categoryId))" +
                    "&part=snippet&id=" + video_id); //send request

            //System.out.println(userAgent.json);
            JNode title = userAgent.json.findFirst("title");
            //System.out.println("Title is " + title.toString());
            return title.toString().toLowerCase();
        }
        catch(JauntException e){
            System.err.println(e);
            LOGGER.log(Level.SEVERE, "an exception was thrown", e);
        }
        return "ERR";
    }

	String[] getTitleAndTags(String video_url){
		String[] retList = {"",""};
		try{
		    String video_id = getYoutubeId(video_url);
			UserAgent userAgent = new UserAgent(); //create new userAgent (headless browser).
			userAgent.sendGET("https://www.googleapis.com/youtube/v3/videos?key=" +
					"AIzaSyAwBpR_XiTmp7mmY3Bgzt0NGpwcLeS5M1Q&fields=items(" +
					"contentDetails(contentRating),snippet(title,description,tags,categoryId))" +
					"&part=snippet&id=" + video_id); //send request
			//System.out.println(userAgent.json);
			//JNode categoryId = userAgent.json.findFirst("categoryId");
			// System.out.println("categoryId: " + categoryId);
			YoutubeMapping ym= new YoutubeMapping();
			Map hm= YoutubeMapping.getHm();


			// System.out.println("The category is: " + hm.get(categoryId.toInt()));

			JNode title = userAgent.json.findFirst("title");
			//System.out.println("title: " + title);
			retList[0] = title.toString().toLowerCase();

			JNode tags = userAgent.json.findFirst("tags");
			//System.out.println("tags: " + tags);
			retList[1] = tags.toString().toLowerCase();
			return retList;
		}
		catch(JauntException e){
			System.err.println(e);
			LOGGER.log(Level.SEVERE, "an exception was thrown", e);
		}
		retList[0] = "ERR";
		retList[1] = "ERR";
		return retList;
	}

	String getDescription(String video_id){
		try{
			UserAgent userAgent = new UserAgent(); //create new userAgent (headless browser).
			userAgent.sendGET("https://www.googleapis.com/youtube/v3/videos?key=" +
					"AIzaSyAwBpR_XiTmp7mmY3Bgzt0NGpwcLeS5M1Q&fields=items(" +
					"contentDetails(contentRating),snippet(title,description,tags,categoryId))" +
					"&part=snippet&id=" + video_id); //send request

			JNode description = userAgent.json.findFirst("description");
			//System.out.println(description);
			return description.toString();
		}
		catch(JauntException e){
			System.err.println(e);
			LOGGER.log(Level.SEVERE, "an exception was thrown", e);
		}
		return "ERR";
	}


	String getChannelID(String video_url){
		try{
			String video_id = getYoutubeId(video_url);
		    UserAgent userAgent = new UserAgent(); //create new userAgent (headless browser).
			userAgent.sendGET("https://www.googleapis.com/youtube/v3/videos?key=" +
					"AIzaSyAwBpR_XiTmp7mmY3Bgzt0NGpwcLeS5M1Q&fields=items(" +
					"contentDetails(contentRating),snippet(channelId))" +
					"&part=snippet&id=" + video_id); //send request

			JNode channelId = userAgent.json.findFirst("channelId");
			// System.out.println(channelId);
			return channelId.toString();
		}
		catch(JauntException e){
			System.err.println(e);
			LOGGER.log(Level.SEVERE, "an exception was thrown", e);
		}
		return "ERR";
	}

	String getChannelInfo(String channel_id){
		try{
			UserAgent userAgent = new UserAgent(); //create new userAgent (headless browser).
			userAgent.sendGET("https://www.googleapis.com/youtube/v3/channels?" +
					"part=statistics&id=" + channel_id + "&key=" +
					"AIzaSyAwBpR_XiTmp7mmY3Bgzt0NGpwcLeS5M1Q"); //send request

			System.out.println(userAgent.json.toString());

			JNode hiddenSubscriberCount = userAgent.json.findFirst("hiddenSubscriberCount");
			if(hiddenSubscriberCount.toString().equals("true")){
				System.out.println("SubscriberCount:" +
						userAgent.json.findFirst("subscriberCount"));
			} else{
				System.out.println("SubscriberCount is hidden");
			}
			System.out.println("VideoCount:" + userAgent.json.findFirst("videoCount"));

			return channel_id.toString();
		}
		catch(JauntException e){
			System.err.println(e);
			LOGGER.log(Level.SEVERE, "an exception was thrown", e);
		}

		return "ERR";
	}

    boolean checkIfReasonableChannel(String channel_id){
        try{
            UserAgent userAgent = new UserAgent(); //create new userAgent (headless browser).
            userAgent.sendGET("https://www.googleapis.com/youtube/v3/channels?" +
                    "part=statistics&id=" + channel_id + "&key=" +
                    "AIzaSyAwBpR_XiTmp7mmY3Bgzt0NGpwcLeS5M1Q"); //send request

            System.out.println(userAgent.json.toString());

            JNode hiddenSubscriberCount = userAgent.json.findFirst("hiddenSubscriberCount");
            if(hiddenSubscriberCount.toString().equals("true")){
                if(userAgent.json.findFirst("subscriberCount").toInt() < 1000){
                    System.out.println("Too few Subscribers");
                    return false;
                }
                //System.out.println("SubscriberCount:" +
                //        userAgent.json.findFirst("subscriberCount"));
            } else{
                System.out.println("SubscriberCount is hidden");
            }
            System.out.println("VideoCount:" + userAgent.json.findFirst("videoCount"));
            if(userAgent.json.findFirst("videoCount").toInt() < 10) {
                System.out.println("Too few videos to trust fully without inspection");
                return false;
            }
        }
        catch(JauntException e){
            System.err.println(e);
            LOGGER.log(Level.SEVERE, "an exception was thrown", e);
        }

        return true;
    }


	// gives related videos list
	String[] getRelatedVideos(String video_id){
		try{
			UserAgent userAgent = new UserAgent(); //create new userAgent (headless browser).
			userAgent.sendGET("https://www.googleapis.com/youtube/v3/search?part=snippet&relatedToVideoId=" +
					video_id + "&type=video&key=" +
					"AIzaSyAwBpR_XiTmp7mmY3Bgzt0NGpwcLeS5M1Q"); //send request

			System.out.println(userAgent.json);
		}
		catch(JauntException e){
			System.err.println(e);
			LOGGER.log(Level.SEVERE, "an exception was thrown", e);
		}
		return null;
	}

	// look at this later - needs to be able to cut off
	String getYoutubeId(String url){
		if(url.contains("youtube") && url.contains("watch?v=")) {
			String[] split_url = url.split("/");
            for(String split_part : split_url){
                if(split_part.contains("watch?v")){
                    return split_part.substring(8,19);
                }
            }
            //return url.substring(url.length() - 11);
		}
		else{
			System.out.println("Improper video - must be youtube video url");
			LOGGER.log(Level.SEVERE, "Improper video - must be youtube video url");
			return "ERR";
		}
		return "ERR";
	}
	
	static void getTweets(String query) throws IOException{
		try {
			TwitterMod.getTweets(query);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, "an exception was thrown", e);
		}
	}
}
