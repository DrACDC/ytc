package youtubeAPI;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;
import com.jaunt.component.Form;

import java.util.HashMap;

/**
 * Created by ericmilton on 4/10/17.
 */
public class FortiGuardLeverage {

    public static String fortiClassify(String URL) throws JauntException {
        // go to fortiguard url
        // obtain classification
        // return classification

        String fortiUrl = "https://fortiguard.com/webfilter";
        UserAgent userAgent = new UserAgent();         //create new userAgent (headless browser)
        userAgent.visit(fortiUrl);          //visit google
        Form form = userAgent.doc.getForm(1);
        form.setTextField("q", URL);
        form.submit();
        Element cat = userAgent.doc.findFirst("<h4 class=info_title>");
        String category = cat.getText();
        category = category.substring(10);
        return(category);
    }

    public static void main(String[] args){
        try{
            System.out.println(fortiClassify("www.google.com"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

