package co.eventmesh.samples.helloperf;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solacesystems.jcsmp.DeliveryMode;
import com.solacesystems.jcsmp.Destination;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPStreamingPublishEventHandler;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.Topic;
import com.solacesystems.jcsmp.XMLMessageProducer;

public class PublishThread implements Runnable {
	static private Logger logger = LoggerFactory.getLogger(PublishThread.class);
	  
    private String messageText;
    private int repeatCount = 0;
    private XMLMessageProducer prod = null;
	private JCSMPStreamingPublishEventHandler handler;
	private TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);

	private Topic topic;

	private String topicName;
	private DeliveryMode deliveryMode = DeliveryMode.DIRECT;
    
    public PublishThread(String topicName, int repeatCount, String messageText, String publishmode){
    	this.topicName = topicName;
        this.repeatCount= repeatCount;
        this.messageText = messageText;
        deliveryMode = "persistent".equals(publishmode) ? DeliveryMode.PERSISTENT:DeliveryMode.DIRECT;
        this.topic = JCSMPFactory.onlyInstance().createTopic(topicName);
    }
    
    public void setProducerListener(XMLMessageProducer prod, JCSMPStreamingPublishEventHandler handler) {
    	this.prod = prod;
    	this.handler = handler;
    	
    }

    
    private void sendMessage() throws JCSMPException {
    	msg.reset();
        msg.setText(this.messageText);
        msg.setDeliveryMode(deliveryMode);
        
        prod.send(msg, topic);
    }

    
    
    @Override
    public void run() {
//    	logger.info(this.toString() + " - Started");
        logger.info(Thread.currentThread().getName()+" - Started.");

    	Stopwatch watch = new Stopwatch("Published " + this.repeatCount + " messages");
        processCommand();
    	logger.info(watch.toString());
//        logger.info(Thread.currentThread().getName()+" - Terminated.");
//    	logger.info(this.toString() + " - Terminated");

    }

    private void processCommand() {
    	try {
    		while(this.repeatCount-- > 0)
    			sendMessage();
		} catch (JCSMPException e) {
			e.printStackTrace();
		}
    }

    @Override
    public String toString(){
        return this.messageText + " " + this.repeatCount;
    }
}