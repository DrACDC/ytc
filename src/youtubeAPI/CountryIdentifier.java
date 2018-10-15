package youtubeAPI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.jaunt.Element;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;
import com.jaunt.component.Table;

public class CountryIdentifier {
	
	public static void getCountry(String url){
		
		String cleanurl=cleanurl(url);
		String searchUrl="https://check-host.net/ip-info?host="+cleanurl;
		
        try {
            UserAgent userAgent = new UserAgent();        
            userAgent.visit(searchUrl);

            Table table = userAgent.doc.getTable("<table class=\"hostinfo result\" cellspacing=\"0\" cellpadding=\"0\">");

            Element element =table.getRowRightOf("Country");
            //System.out.println(element.outerHTML());
            //System.out.println(element.findFirst("<strong>").innerHTML());
            String country=element.findFirst("<strong>").innerHTML();
            System.out.println(country);

        }catch(JauntException e){
            System.err.println(e);
        }
    }

	static String cleanurl(String url)
	{
		int slashNum=0;
		int strlen=url.length();
		char arr[]= url.toCharArray();
		for(int i=0; i<strlen; i++){
		    if(arr[i]=='/'){
		    	slashNum ++;    
		    }
		}

		int newlen=strlen+(slashNum*3);
		char newurl[];
		newurl= new char[newlen];
		int j=0;
		for(int i=0; i<strlen; i++)
		{
			if(arr[i]=='/')
			{
				newurl[j]='%';
				j++;
				newurl[j]='2';
				j++;
				newurl[j]='F';
			}
			else
			{
				newurl[j]=arr[i];
			}
			j++;
		}

		return url;
	}

    public static void main(String[] args) throws IOException{
    	//String url= "www.codecademy.com/ru/tracks/twitter";
    	System.out.println("Please enter an URL: ");
    	BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));

    	String url= bin.readLine();
  
    	//remember to replace all slashes with %2F
    	getCountry(url);
    }

}
