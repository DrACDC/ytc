package youtubeAPI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ericmilton on 3/20/17.
 */
public class TextTagsTitleMod extends ModuleMessenger {
	
	public static final ModuleType MODULE_TYPE = ModuleType.TEXT_TAGS_TITLE_MOD; 

	static Logger LOGGER = Logger.getLogger(Logger.class.getName());
    //static private List<String> musicWords = null;
    //static private List<String> cartoonWords = null;

    static private List<String> sportsWords = null;
    static private List<String> gameWords = null;
    static private List<String> artsandCultureWords = null;
    static private List<String> newsMediaWords = null;
    static private List<String> alcoholWords = null;
    static private List<String> tobaccoWords = null;
    static private List<String> politicalOrganizationsWords = null;
    static private List<String> abortionWords = null;
    static private List<String> advocacyOrganizationWords = null;
    static private List<String> alternativeBeliefWords = null;
    static private List<String> armedForcesWords = null;
    static private List<String> marijuanaWords = null;
    static private List<String> sexEducationWords = null;
    static private List<String> entertainmentWords = null;

    static Scraper scraper = new Scraper();

    // uses populateList to fill each of the categorization lists
    public static void setup(){
        //musicWords = populateList("music.txt");
        //cartoonWords = populateList("cartoon.txt");

        gameWords = populateList("Games.txt");
        sportsWords = populateList("Sports.txt");
        newsMediaWords = populateList("NewsandMedia.txt");
        artsandCultureWords = populateList("ArtsandCulture.txt");
        alcoholWords = populateList("Alcohol.txt");
        tobaccoWords = populateList("Tobacco.txt");
        politicalOrganizationsWords = populateList("PoliticalOrganizations.txt");
        advocacyOrganizationWords = populateList("AdvocacyOrganizations.txt");
        abortionWords = populateList("Abortion.txt");
        alternativeBeliefWords = populateList("AlternativeBeliefs.txt");
        armedForcesWords = populateList("ArmedForces.txt");
        marijuanaWords = populateList("Marijuana.txt");
        sexEducationWords = populateList("SexEducation.txt");
        entertainmentWords = populateList("Entertainment.txt");
    }

    // working as intended
    public static List<String> populateList(String filename){
        List<String> set = new LinkedList<>();

        try{
            FileReader fin = new FileReader(new File(filename));
            BufferedReader bin = new BufferedReader(fin);

            String line;
            while((line = bin.readLine()) != null){
                set.add(line);
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return set;
    }

    public static void addToCatValList(List<CatValue> list, CatValue catValue){
        for(int i = 0; i < list.size(); i += 1){
            if(catValue.getCategoryValue() > list.get(i).getCategoryValue()){
                list.add(i,catValue);
                return;
            }
        }
        list.add(catValue);
        return;
    }

    public static String classify(String videoURL){
        LinkedList<CatValue> list = new LinkedList<CatValue>();

        String[] info = scraper.getTitleAndTags(videoURL);
        if(info[0].equals("ERR")){
            System.out.println("Error detected for " + videoURL);
            LOGGER.log(Level.SEVERE, "Error detected for ", videoURL);
            return "NA";
        }

        List<String> parsedTags = parseTags(info[1]);
        parsedTags.add(info[0]);

        /*System.out.println("Printing Tags and Title");
        for(String word:parsedTags){
            System.out.println(word);
        }
        System.out.println("Done printing Tags and Title");*/

        if(parsedTags != null) {

            //int musicScore = doComparison(parsedTags, musicWords);
            //int cartoonScore = doComparison(parsedTags, cartoonWords);
            int gamingScore = doComparison(parsedTags, gameWords);
            int entertainmentScore = doComparison(parsedTags,entertainmentWords);
            int sportsScore = doComparison(parsedTags, sportsWords);
            int newsMediaScore = doComparison(parsedTags, newsMediaWords);
            int artsandCultureScore = doComparison(parsedTags, artsandCultureWords);
            int alcoholScore = doComparison(parsedTags, alcoholWords);
            int tobaccoScore = doComparison(parsedTags, tobaccoWords);
            int politicalOrganizationScore = doComparison(parsedTags, politicalOrganizationsWords);
            int advocacyOrganizationScore = doComparison(parsedTags, advocacyOrganizationWords);
            int abortionScore = doComparison(parsedTags, abortionWords);
            int alternativeBeliefScore = doComparison(parsedTags, alternativeBeliefWords);
            int armedForcesScore = doComparison(parsedTags, armedForcesWords);
            int marijuanaScore = doComparison(parsedTags, marijuanaWords);
            int sexEducationScore = doComparison(parsedTags, sexEducationWords);

            //addToCatValList(list, new CatValue("Music", musicScore));
            //addToCatValList(list, new CatValue("Cartoon", cartoonScore));

            addToCatValList(list, new CatValue("Games", gamingScore));
            addToCatValList(list, new CatValue("Sports", sportsScore));
            addToCatValList(list, new CatValue("News and Media", newsMediaScore));
            addToCatValList(list, new CatValue("Arts and Culture", artsandCultureScore));
            addToCatValList(list, new CatValue("Alcohol", alcoholScore));
            addToCatValList(list, new CatValue("Tobacco", tobaccoScore));
            addToCatValList(list, new CatValue("Political Organizations", politicalOrganizationScore));
            addToCatValList(list, new CatValue("Advocacy Organizations", advocacyOrganizationScore));
            addToCatValList(list, new CatValue("Abortion", abortionScore));
            addToCatValList(list, new CatValue("Alternative Beliefs", alternativeBeliefScore));
            addToCatValList(list, new CatValue("Armed Forces", armedForcesScore));
            addToCatValList(list, new CatValue("Marijuana", marijuanaScore));
            addToCatValList(list, new CatValue("Sex Education", sexEducationScore));
            addToCatValList(list, new CatValue("Entertainment", entertainmentScore));

            Category category = new Category(list.get(0).getCategoryName(), list.get(0).getCategoryValue(),
                    list.get(1).getCategoryName(), list.get(1).getCategoryValue(),
                    list.get(2).getCategoryName(), list.get(2).getCategoryValue(),
                    parsedTags.size()); // modify

            return category.getPrimaryCategory();
        } else{
            System.out.println("NULL ISSUE IN TEXTTITLTETAGESFD");
            LOGGER.log(Level.SEVERE, "NULL ISSUE IN TEXTTITLTETAGESFD ");
            return "ERR";
        }
    }

    public String classify(List<String> keywords){
        LinkedList<CatValue> list = new LinkedList<CatValue>();

        int gamingScore = doComparison(keywords,gameWords);
        int entertainmentScore = doComparison(keywords,entertainmentWords);
        int sportsScore = doComparison(keywords,sportsWords);
        int newsMediaScore = doComparison(keywords,newsMediaWords);
        int artsandCultureScore = doComparison(keywords,artsandCultureWords);
        int alcoholScore = doComparison(keywords,alcoholWords);
        int tobaccoScore = doComparison(keywords,tobaccoWords);
        int politicalOrganizationScore = doComparison(keywords,politicalOrganizationsWords);
        int advocacyOrganizationScore = doComparison(keywords, advocacyOrganizationWords);
        int abortionScore = doComparison(keywords, abortionWords);
        int alternativeBeliefScore = doComparison(keywords, alternativeBeliefWords);
        int armedForcesScore = doComparison(keywords, armedForcesWords);
        int marijuanaScore = doComparison(keywords, marijuanaWords);
        int sexEducationScore = doComparison(keywords, sexEducationWords);

        addToCatValList(list,new CatValue("Gaming",gamingScore));
        addToCatValList(list,new CatValue("Sports",sportsScore));
        addToCatValList(list,new CatValue("News and Media",newsMediaScore));
        addToCatValList(list,new CatValue("Arts and Culture",artsandCultureScore));
        addToCatValList(list,new CatValue("Alcohol",alcoholScore));
        addToCatValList(list,new CatValue("Tobacco",tobaccoScore));
        addToCatValList(list,new CatValue("Political Organizations",politicalOrganizationScore));
        addToCatValList(list, new CatValue("Advocacy Organizations", advocacyOrganizationScore));
        addToCatValList(list, new CatValue("Abortion", abortionScore));
        addToCatValList(list, new CatValue("Alternative Beliefs", alternativeBeliefScore));
        addToCatValList(list, new CatValue("Armed Forces", armedForcesScore));
        addToCatValList(list, new CatValue("Marijuana", marijuanaScore));
        addToCatValList(list, new CatValue("Sex Education", sexEducationScore));
        addToCatValList(list, new CatValue("Entertainment", entertainmentScore));


        Category category = new Category(list.get(0).getCategoryName(), list.get(0).getCategoryValue(),
                list.get(1).getCategoryName(), list.get(1).getCategoryValue(),
                list.get(2).getCategoryName(), list.get(2).getCategoryValue(),
                keywords.size()); // modify

        return category.getPrimaryCategory();
    }

    public static int doComparison(List<String> parsedTags, List<String> categoryListOfWords){
        int counter = 0;

        for (String tag:parsedTags) {
            for(String word: categoryListOfWords){
                if(tag.equals(word) || tag.contains(word)){
                    counter += 1;
                    break;
                }
            }
        }

        return counter;
    }

    // converts tag into a List of tags to be iterated for comparison
    public static List<String> parseTags(String tags){
        List<String> parsedTags = new LinkedList<String>();
        int start = 0;
        for(int i = 0; i < tags.length(); i += 1){
            if(tags.charAt(i) == '"'){
                start = i + 1;
                i += 1;
                while(tags.charAt(i) != '"' && i < tags.length()){
                    i += 1;
                }
                parsedTags.add(tags.substring(start,i));
            }
        }
        return parsedTags;
    }

    public static void getClassification(String video_url) throws IOException, TimeoutException{
        String category = classify(video_url);
        setupConnection();
        sendClassification(MODULE_TYPE, category, video_url);
        closeConnection();
    }

    TextTagsTitleMod() {
        setup();
    }

    public static void main(String[] args) {
        setup();
        System.out.println(classify("https://www.youtube.com/watch?v=m1zaz3oJ3FQ"));

        System.out.println("\n\n\n");
        System.out.println(classify("https://www.youtube.com/watch?v=itSTzV29bS0"));

        System.out.println("\n\n\n");
        System.out.println(classify("https://www.youtube.com/watch?v=71KrI-K3PGs"));
    }
}