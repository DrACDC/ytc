package youtubeAPI;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by ericmilton on 2/16/17.
 */
public class YoutubeAccessor {

    static String API_key = "AIzaSyAwBpR_XiTmp7mmY3Bgzt0NGpwcLeS5M1Q";

    static String[] web_version = {"https://www.googleapis.com/youtube/v3/videos?key=",
            "&fields=items(snippet(title,description,tags,categoryId))&part=snippet&id="};

    public static void main (String[] args){
        BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));

        StringBuilder strBuilder; // = new StringBuilder().append(web_version[0]).append(API_key).append(web_version[1]);
        String video_id, video_url;

        File f;
        Scanner s;

        while(true) {
            strBuilder = new StringBuilder().append(web_version[0]).append(API_key).append(web_version[1]);
            System.out.println("Input video url: ");

            try {
                video_url = bin.readLine();

                if(video_url == "QUIT"){
                    System.out.println("Ending program");
                    return;
                }
                else if(video_url.contains("youtube") == false){
                    System.out.println("Not a Youtube Video, ending program");
                    return;
                }

                video_id = getYoutubeId(video_url);
                strBuilder.append(video_id);

                f = new File("YTVideo_" + video_id + ".txt");
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(f));

                //System.out.println(strBuilder.toString());
                URL url = new URL(strBuilder.toString());

                s = new Scanner(url.openStream());

                // iterates it into a file
                while(s.hasNext()) {
                    fileWriter.write(s.nextLine() + "\n");
                }
                s.close();
                fileWriter.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // takes in Youtube URL and returns the videoId
    public static String getYoutubeId(String urlName){
        // System.out.println(urlName.substring(urlName.length() - 11));
        return urlName.substring(urlName.length() - 11);
    }

    public static List<String> getMetadata(String video_url){
        BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));

        StringBuilder strBuilder; // = new StringBuilder().append(web_version[0]).append(API_key).append(web_version[1]);
        String video_id;

        File f;
        Scanner s;

        while(true) {
            strBuilder = new StringBuilder().append(web_version[0]).append(API_key).append(web_version[1]);

            try {
                if(video_url.contains("youtube") == false){
                    System.out.println("Not a Youtube Video, ending program");
                    return null;
                }

                video_id = getYoutubeId(video_url);
                strBuilder.append(video_id);

                f = new File("YTVideo_" + video_id + ".txt");
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(f));

                //System.out.println(strBuilder.toString());
                URL url = new URL(strBuilder.toString());

                s = new Scanner(url.openStream());

                // iterates it into a file
                while(s.hasNext()) {
                    fileWriter.write(s.nextLine() + "\n");
                }
                s.close();
                fileWriter.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
