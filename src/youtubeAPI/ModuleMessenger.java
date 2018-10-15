package youtubeAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class ModuleMessenger {
	private static final String EXCHANGE_NAME = "CatExchange"; 
	private static Channel channel;
	private static Connection connection;
	
	public static void setupConnection() throws IOException, TimeoutException {
		 ConnectionFactory factory = new ConnectionFactory();
	     factory.setHost("localhost");
	     connection = factory.newConnection();
	     channel = connection.createChannel();
	     channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	}

	public static void sendClassification(ModuleType module, String classification, String URL) throws IOException {
    	String message = module + ":" + classification + ":" + URL;
    	channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println(" [x] Returning '" + classification + "' of URL '" + URL + "' from module " + module);
	}
	
	public static void closeConnection() throws IOException, TimeoutException {
		channel.close();
        connection.close();
	}
	
}
