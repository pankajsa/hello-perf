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
	private TextMessage msg;

	private Topic topic;

	private String topicName;
    
    public PublishThread(String topicName, int repeatCount, String messageText){
    	this.topicName = topicName;
        this.repeatCount= repeatCount;
        this.messageText = messageText;
    }
    
    public void setProducerListener(XMLMessageProducer prod, JCSMPStreamingPublishEventHandler handler) {
    	this.prod = prod;
    	this.handler = handler;
    	
    }

    
    private void send() throws JCSMPException {
    	if (msg == null) {
        	this.msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);    		
    	}
    	if (topic == null) {
    		topic = JCSMPFactory.onlyInstance().createTopic(topicName);
    	}
    	msg.reset();
        msg.setText(this.messageText);
        msg.setDeliveryMode(DeliveryMode.PERSISTENT);
//        logger.info("Connected. About to send message " + this.messageText + " to topic " + topic);
		//        System.out.printf("Connected. About to send message '%s' to topic '%s'...%n", text, topic);
//        logger.info(msg.toString());
        prod.send(msg, topic);
//        System.out.printf("Connected. ----- to send message '%s' to topic '%s'...%n", text, msg.getMessageId());
    }

    
    
    @Override
    public void run() {
//    	logger.info(this.toString() + " - Started");
        logger.info(Thread.currentThread().getName()+" - Started.");

    	Stopwatch watch = new Stopwatch();
        processCommand();
    	logger.info("Processing Time:" + watch);
        logger.info(Thread.currentThread().getName()+" - Terminated.");
//    	logger.info(this.toString() + " - Terminated");

    }

    private void processCommand() {
    	try {
    		while(this.repeatCount-- > 0)
    			send();
		} catch (JCSMPException e) {
			e.printStackTrace();
		}
    }

    @Override
    public String toString(){
        return this.messageText + " " + this.repeatCount;
    }
}