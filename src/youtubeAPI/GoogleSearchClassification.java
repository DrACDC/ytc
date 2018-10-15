/**
 * 
 */
package youtubeAPI;

import java.util.HashMap;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

/**
 * @author danielcantrelle
 *
 */
public class GoogleSearchClassification {
	public static String cleanURL(String str) {
		int start = 0, end = 0;
		if ((str.contains("www.") || str.contains("http://") || str.contains("https://")) && (str.contains(".com") || str.contains(".net") || str.contains(".org"))) {
			if (str.contains("www.")) {
				start = str.indexOf("www.");
			} else if (str.contains("http://")) {
				start = str.indexOf("http://");
			} else if (str.contains("https://")) {
				start = str.indexOf("https://");
			}

			if (str.contains(".com")) {
				end = str.indexOf(".com");
				end += 4;
			} else if (str.contains(".net")) {
				end = str.indexOf(".net");
				end += 4;
			} else if (str.contains(".org")) {
				end = str.indexOf(".org");
				end += 4;
			}
		}
		return(str.substring(start, end));
	}

	public static String classify(String name) throws JauntException {
		UserAgent userAgent = new UserAgent();         //create new userAgent (headless browser)
		userAgent.visit("http://google.com");          //visit google
		userAgent.doc.apply(name);            //apply form input (starting at first editable field)
		userAgent.doc.submit("Google Search");         //click submit button labelled "Google Search"

		HashMap<String, Integer> results = new HashMap<String, Integer>();
		String result;
		String bestResult = "";
		int maxValue = 0;
		String URL;

		Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>");  //find search result links
		for(Element hrefLink : links) {      //print results
			String href = hrefLink.getAt("href");
			if (href.contains("www.google.com/search") || href.contains("youtube.com")) {
				continue;
			}

			userAgent.visit(href);
			Element link = userAgent.doc.findFirst("<div class=\"_jFe\">").findFirst("<a>");
			URL = link.getText();
			//System.out.println(URL);
			URL = cleanURL(URL);
			//System.out.println(URL);

			if (URL.equals("")) {
				continue;
			}

			result = FortiGuardLeverage.fortiClassify(URL);
			//System.out.println(result + "\n");
			if (result.equals("Search Engines and Portals")) {
				continue;
			}
			if (results.containsKey(result)) { // result already in HashMap
				Integer oldValue = results.get(result);
				Integer newValue = new Integer(oldValue.intValue() + 1);
				results.replace(result, oldValue, newValue);

				if (newValue.intValue() > maxValue) {
					maxValue = newValue.intValue();
					bestResult = result;
				}
			} else { //add new result to HashMap
				results.put(result, new Integer(1));

				if (maxValue == 0) {
					maxValue = 1;
					bestResult = result;
				}
			}
		}
		//return most common classification
		return(bestResult);
	}
}
