package youtubeAPI;

/**
 * Created by ericmilton on 4/11/17.
 */

// https://developers.google.com/youtube/v3/docs/channels

public class ChannelEvaluationMod {

    public static boolean checkChannel(String url){
        Scraper scraper = new Scraper();
        String channelId = scraper.getChannelID(url);
        return scraper.checkIfReasonableChannel(channelId);
    }

    public static void getClassification(String video_url){
        boolean validChannel = checkChannel(video_url);

        // SARA DO THIS - this one is different bc it returns if a channel is reasonable or not
    }

    ChannelEvaluationMod(String url){
        checkChannel(url);
    }

    ChannelEvaluationMod(){}

    public static void ChannelInfo(String url){
        Scraper scraper = new Scraper();
        String channelId = scraper.getChannelID(url);
        System.out.println(channelId);
        System.out.println();
        scraper.getChannelInfo(channelId);

        if(scraper.checkIfReasonableChannel(channelId)){
            System.out.println("Trustable");
        } else {
            System.out.println("Not Necessarily");
        }
    }

    public static void main(String[] args){
        //ChannelInfo("https://www.youtube.com/watch?v=uMChtnYO-0Y");
        ChannelInfo("https://www.youtube.com/watch?v=uVG-l8eVaB0");
        if (checkChannel("https://www.youtube.com/watch?v=uVG-l8eVaB0")){
            System.out.println("True");
        } else {
            System.out.println("False");
        }
    }
}
