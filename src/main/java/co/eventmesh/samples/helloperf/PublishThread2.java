package co.eventmesh.samples.helloperf;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solacesystems.jcsmp.DeliveryMode;
import com.solacesystems.jcsmp.Destination;
import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.JCSMPStreamingPublishEventHandler;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.Topic;
import com.solacesystems.jcsmp.XMLMessageProducer;

public class PublishThread2 implements Runnable {
	static private Logger logger = LoggerFactory.getLogger(PublishThread2.class);
	  
    private String messageText;
    private int repeatCount = 1000;
    private XMLMessageProducer prod = null;
	private JCSMPStreamingPublishEventHandler handler;
	private TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);

	private LinkedBlockingQueue<String> mqueue = new LinkedBlockingQueue<String>();
	
	private Topic topic;

	private String topicName;
    
    public PublishThread2(String topicName){
    	this.topicName = topicName;
    	this.topic = JCSMPFactory.onlyInstance().createTopic(topicName);
    	
    	
    	
//    	int repeatCount = Configuration.PUBLISH_COUNT;
//    	if (Configuration.getDefaults().get("publishcount") != null) {
//    		repeatCount = Integer.parseInt(Configuration.getDefaults().get("publishcount"));
//    	}

//    	PublishThread publishThread = new PublishThread(publishTopic, repeatCount, messageStr);
    	PublishEventHandler handler = new PublishEventHandler();
    	XMLMessageProducer prod;
		
		JCSMPSession session;
		try {
			session = JCSMPFactory.onlyInstance().createSession(getProperties());
			prod = session.getMessageProducer(handler);
	    	setProducerListener(prod, handler);
		} catch (InvalidPropertiesException e) {
			e.printStackTrace();
		} catch (JCSMPException e) {
			e.printStackTrace();
		}
    	
//    	new Thread(publishThread, "publish-thread-" + threadNo).start();    	

    	
    	
    	
    }
    
    public void setProducerListener(XMLMessageProducer prod, JCSMPStreamingPublishEventHandler handler) {
    	this.prod = prod;
    	this.handler = handler;
    	
    }

    
    public void addMessage(String message) {
    	try {
			mqueue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    }
    
    private void sendMessage() throws JCSMPException {
    	msg.reset();
    	
    	
        try {
			msg.setText(mqueue.take());
	        msg.setDeliveryMode(DeliveryMode.PERSISTENT);
	        prod.send(msg, topic);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    
    
    @Override
    public void run() {
//    	logger.info(this.toString() + " - Started");
        logger.info(Thread.currentThread().getName()+" - Started.");

    	Stopwatch watch = new Stopwatch("PublishThread2 ");
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
        return "PublishThread2";
    }
    private static JCSMPProperties getProperties() {
    	HashMap<String, String> hmap = Configuration.getDefaults();
//    	logger.info(hmap.toString());

    	JCSMPProperties properties = new JCSMPProperties();
//    	properties.setProperty(JCSMPProperties.HOST, "localhost");
//    	properties.setProperty(JCSMPProperties.USERNAME, "admin");
//    	properties.setProperty(JCSMPProperties.PASSWORD, "admin");
//    	properties.setProperty(JCSMPProperties.VPN_NAME, "default");

    	properties.setProperty(JCSMPProperties.HOST, hmap.get("hostname"));
    	properties.setProperty(JCSMPProperties.USERNAME, hmap.get("username"));
    	properties.setProperty(JCSMPProperties.PASSWORD, hmap.get("password"));
    	properties.setProperty(JCSMPProperties.VPN_NAME, hmap.get("vpn"));

    	
    	properties.setProperty(JCSMPProperties.PUB_ACK_WINDOW_SIZE, 255);
    	
//    	logger.info(properties.toString());


    	return(properties);
    }

    
}