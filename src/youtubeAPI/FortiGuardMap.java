package youtubeAPI;

import java.util.*;

public class FortiGuardMap {

   @SuppressWarnings("rawtypes")
public static void main(String args[]) {
   
      // Create a hash map
      HashMap<Integer, String> hm = new HashMap<Integer, String>();
      
      // Put elements to the map
       hm.put(1, "Film & Animation");
      hm.put(2, "Personal Vehicles"); // Youtube - Cars
      hm.put(10, "Entertainment"); //Youtube - Music
      hm.put(15, "Pets & Animals"); 
	  hm.put(17, "Sports"); //Youtube -Sports
	  hm.put(19, "Travel & Events");//Youtube - Travel & Events
	  hm.put(20, "Games"); //Youtube- Gaming
	  hm.put(22, "Personal Websites and Blogs"); // Youtube - People & Blogs
	  hm.put(23, "Entertainment"); //Youtube - Comedy
	  hm.put(24, "Entertainment"); //Youtube -Entertainment
	  hm.put(25, "News and Media"); //Youtube - News & Politics
	  hm.put(26, "How-to & Style"); //Youtube -
	  hm.put(27, "Education"); //Youtube - Education
	  hm.put(28, "Science & Technology"); //Youtube - 
	  hm.put(29, "Non-profits & Activism"); //Youtube -
      
      // Get a set of the entries
      Set<?> set = hm.entrySet();
      
      // Get an iterator
      Iterator<?> i = set.iterator();
      
      // Display elements
      while(i.hasNext()) {
         Map.Entry me = (Map.Entry)i.next();
         System.out.print(me.getKey() + ": ");
         System.out.println(me.getValue());
      }
      System.out.println();
      
   }
}