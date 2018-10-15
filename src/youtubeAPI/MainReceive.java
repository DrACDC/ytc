package youtubeAPI;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class MainReceive implements Runnable {
    private static String EXCHANGE_NAME;
    private static ArrayList<Classification> classificationList = new ArrayList<Classification>();
    private static int receiveCounter = 0;
    
	@Override
	public void run() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
	        factory.setHost("localhost");
	        Connection connection = factory.newConnection();
	        Channel channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
						
		    String queueName = channel.queueDeclare().getQueue();
		    channel.queueBind(queueName, EXCHANGE_NAME, "");
	        Component frame = null;

		    Consumer consumer = new DefaultConsumer(channel) {
			      @Override
			      public void handleDelivery(String consumerTag, Envelope envelope,
			                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
			        String message = new String(body, "UTF-8");
			        String[] msgComponents = message.split(":");
			        ModuleType module = ModuleType.valueOf(msgComponents[0]);
			        String classification = msgComponents[1];
			        String URL = "";
			        for (int i = 2; i < msgComponents.length; i++)
			        	URL = URL + msgComponents[i];
			        System.out.println(" [x] Received '" + classification + "' of URL '" + URL + "' from module " + module);
			        //DatabaseAccess.putVideoEntry("YouTube", URL.substring(URL.length() - 11));
			        classificationList.add(new Classification(classification, module, URL));
			        String popup = module.toString() + " classified this video as: " + classification;
					JOptionPane.showMessageDialog(frame,  popup);
					receiveCounter++;
					
					if (receiveCounter == 5)
					{
						//get final classification
						String finalClassification = null; //update
						popup = "Final classification for " + URL + ": " + finalClassification;
						JOptionPane.showMessageDialog(frame,  popup);
						//database put video entry function for final classification? 
						receiveCounter = 0;
					}
	
			      }
			    };
			channel.basicConsume(queueName, true, consumer);
			
			} catch (IOException | TimeoutException e) {
				e.printStackTrace();
			}
	}
	
	public MainReceive(String EXCHANGE_NAME) {
		this.EXCHANGE_NAME = EXCHANGE_NAME;	
	}
}
