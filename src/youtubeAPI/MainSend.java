package youtubeAPI;
import java.io.IOException;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MainSend {

    private static final String SEND_EXCHANGE_NAME = "URLExchange";
    private static final String RECV_EXCHANGE_NAME = "CatExchange";
    private static String URL;
    private static String message;
    private static boolean isRunning = true;

    private static String getUserInput() throws IOException {
       /* System.out.println("Enter full YouTube URL for classification, or QUIT to quit program: ");
        BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));
        String input = bin.readLine();
        return input;
        */
        JFrame userFrame = new JFrame();
        String URL= JOptionPane.showInputDialog(userFrame, "Enter a YouTube URL for classification:");
        return URL;
    }
    
    private static boolean checkYouTubeURL(String URL) {
    	if(URL.contains("youtube") && URL.contains("watch?v=")) 
    		return true;
    	else {
        	 JFrame userFrame = new JFrame();
             JOptionPane.showMessageDialog(userFrame, "Error: Must be a proper YouTube video URL.");
             return false;
    	}
    }

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(SEND_EXCHANGE_NAME, "fanout");
        
        new ModuleManager();
        //DatabaseAccess.DatabaseSetup();
        
        //start up thread to receive categorizations
		MainReceive mainReceiveThread = new MainReceive(RECV_EXCHANGE_NAME);
		
		// threaded
		Thread t = new Thread(mainReceiveThread);
		t.start();

        while (isRunning == true) {

                URL = getUserInput(); 
                
                if (URL.equalsIgnoreCase("QUIT")) {
                    channel.close();
                    connection.close();
                    System.exit(0);
                }
                
                /*String classification = DatabaseAccess.getVideoClassification("YouTube", URL.substring(URL.length() - 11));
                if (classification.equals("NA")) {
                	//communicate with main send -- skip over queue
        		}*/
                
                if (!checkYouTubeURL(URL)) {
                	continue;
                }                	
                
                for (ModuleType m : ModuleType.values()) {
                	message = m + ":" + URL;
                	channel.basicPublish(SEND_EXCHANGE_NAME, "", null, message.getBytes());
                    System.out.println(" [x] Forwarding '" + URL + "' to module " + m);
                }
        }
    }
}