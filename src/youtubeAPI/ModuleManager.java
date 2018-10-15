package youtubeAPI;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ModuleManager {
  private static final String SEND_EXCHANGE_NAME = "URLExchange";
  private static String URL = null;
  private static ModuleType module = null;
  
  public ModuleManager() throws Exception {
	  callModules();
  }

  //look into this!
  public static void startModules() throws IOException, TimeoutException {
  	new TextTagsTitleMod();
  	new DescriptionSitesMod();
  	new RelatedVideosMod();
  	new TwitterMod();
  	new GoogleSearchMod();
  }
  
  public static void callModules() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(SEND_EXCHANGE_NAME, "fanout");
    String queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, SEND_EXCHANGE_NAME, "");
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        module = ModuleType.valueOf(message.substring(0, message.indexOf(':')));
        URL = message.substring(message.indexOf(':')+1);

        System.out.println(" [x] Sending '" + URL + "' to module " + module);
        if (URL.equalsIgnoreCase("NO"))
        	return;
        
        try {
			startModules();
		} catch (TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        switch (module) {
    	case TEXT_TAGS_TITLE_MOD   : try {
									 	TextTagsTitleMod.getClassification(URL);
								     } catch (TimeoutException e) { e.printStackTrace(); }
		                             break;
		                             
    	case DESCRIPTION_SITES_MOD : try {
    								 	DescriptionSitesMod.getClassification(URL);
    								 } catch (TimeoutException e) { e.printStackTrace(); }	
    								 break;
    								 
    	case RELATED_VIDEOS_MOD	   : try {
    								 	RelatedVideosMod.getClassification(URL);
    								 } catch (TimeoutException e) { e.printStackTrace(); }	
    								 break;
    								 
    	case TWITTER_MOD      	   : try {
			 					     	TwitterMod.getClassification(URL);
			 					     } catch (TimeoutException e) { e.printStackTrace(); }	
			 					     break;
    	case GOOGLE_SEARCH_MOD     : try {
		     							GoogleSearchMod.getClassification(URL);
		     					     } catch (TimeoutException e) { e.printStackTrace(); }	
    							     break;
			 					   
    	default                    : return;
        }
      }
    };
    channel.basicConsume(queueName, true, consumer);
  }
}