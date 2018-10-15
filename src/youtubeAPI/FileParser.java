package youtubeAPI;

import java.io.*;

/**
 * Created by ericmilton on 3/2/17.
 */
public class FileParser {
    public static void main(String[] args) throws IOException{
        Scraper scraper = new Scraper();

        String str, tags, fileName;

        BufferedWriter bout = null;
        FileWriter fout = null; // FileWriter
        File f = null;

        try{
            FileReader fin = new FileReader("YoutubeURLs.txt");
            BufferedReader bin = new BufferedReader(fin);
            while((str = bin.readLine()) != null){
                if(!str.substring(0,4).equals("http")){ // if not reading a website
                    if(bout != null){
                        bout.close(); // closes prev file
                    }
                    fileName = str + "_cat.txt";
                    System.out.println(fileName + " is the filename");
                    bout = new BufferedWriter(new FileWriter(fileName));
                }
                else{
                    if(bout == null){
                        System.out.println("PROBLEM!");
                        //exit();
                    }else{
                        System.out.println(str + " is the input string");
                        tags = scraper.getTags(getYoutubeId(str));
                        bout.write(tags + "\n");
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(bout != null){
                bout.close();
            }
            System.out.println("DONE!");
        }
        //Scraper.FindTags();
    }

    public static String getYoutubeId(String urlName){
        return urlName.substring(urlName.length() - 11);
    }
}
// case-insensitive changes!
