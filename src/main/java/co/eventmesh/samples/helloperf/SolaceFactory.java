package co.eventmesh.samples.helloperf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.ConsumerFlowProperties;
import com.solacesystems.jcsmp.DeliveryMode;
import com.solacesystems.jcsmp.EndpointProperties;
import com.solacesystems.jcsmp.FlowReceiver;
import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.JCSMPStreamingPublishEventHandler;
import com.solacesystems.jcsmp.Queue;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.Topic;
import com.solacesystems.jcsmp.XMLMessageConsumer;
import com.solacesystems.jcsmp.XMLMessageListener;
import com.solacesystems.jcsmp.XMLMessageProducer;
import com.solacesystems.jcsmp.JCSMPSendMultipleEntry;

public class SolaceFactory{
	static private Logger logger = LoggerFactory.getLogger(SolaceFactory.class);
    private JCSMPSession session = null;
    private XMLMessageProducer prod = null;
    private TextMessage msg = null;
    private Topic topic = null;
    private Queue listenQueue = null;
//    private XMLMessageConsumer cons = null;


	private static SolaceFactory instance = null;
    private SolaceFactory(){
        System.out.println("Config Created");
    }
    
    public static SolaceFactory getInstance() {
    	if (instance == null) {
    		instance = new SolaceFactory();
    	}
    	return(instance);
    	
    }
    
    public JCSMPSession getSession() throws InvalidPropertiesException {
    	logger.info("Creating Session");
    	if (session == null)
    		this.session = JCSMPFactory.onlyInstance().createSession(SolaceFactory.getInstance().getProperties());
    	

    	
    	return this.session;
    }
    
    public void createTopic(String topicName) {
    	if (topic == null){
            topic = JCSMPFactory.onlyInstance().createTopic("tutorial/topic");
    	}
    	if (listenQueue == null){
    		listenQueue = JCSMPFactory.onlyInstance().createQueue("q1");
    	}
    	
    }
    
    
    public XMLMessageProducer getProducer() throws JCSMPException {
    	if (prod == null) {
        	prod = session.getMessageProducer(new JCSMPStreamingPublishEventHandler() {
                @Override
                public void responseReceived(String messageID) {
//                    System.out.println("Producer received response for msg: " + messageID);
                }

                @Override
                public void handleError(String messageID, JCSMPException e, long timestamp) {
                    System.out.printf("Producer received error for msg: %s@%s - %s%n", messageID, timestamp, e);
                }
            });    		
    	}
		return prod;
    }
    
    public void startConsumer() throws JCSMPException {
    	
    	ConsumerFlowProperties flow_prop = new ConsumerFlowProperties();
    	flow_prop.setEndpoint(listenQueue);
    	flow_prop.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);
    	
    	EndpointProperties endpoint_props = new EndpointProperties();
    	endpoint_props.setAccessType(EndpointProperties.ACCESSTYPE_NONEXCLUSIVE);
    	

    	
    	
    	FlowReceiver cons = session.createFlow(new XMLMessageListener() {
    	    @Override
    	    public void onReceive(BytesXMLMessage msg) {
    	        if (msg instanceof TextMessage) {
//    	            System.out.printf("TextMessage received: '%s'%n", ((TextMessage) msg).getText());
    	        } else {
//    	            System.out.println("Message received.");
    	        }
    	        //System.out.printf("Message Dump:%n%s%n", msg.dump());
//                System.out.printf("Message Dump: %s %s",((TextMessage) msg).getText(), msg.getDeliveryMode());

    	        
    	        // When the ack mode is set to SUPPORTED_MESSAGE_ACK_CLIENT,
    	        // guaranteed delivery messages are acknowledged after
    	        // processing
    	        msg.ackMessage();
    	    }

    	    @Override
    	    public void onException(JCSMPException e) {
    	        System.out.printf("Consumer received exception: %s%n", e);
    	    }
    	}, flow_prop, endpoint_props);    
    	cons.start();
    	

    }
    
   /* 
    public void startConsumer() throws JCSMPException {
    	cons = session.getMessageConsumer(new XMLMessageListener() {
            @Override
            public void onReceive(BytesXMLMessage msg) {
                if (msg instanceof TextMessage) {
                    System.out.printf("TextMessage received: '%s'%n",
                            ((TextMessage)msg).getText());
                } else {
                    System.out.println("Message received.");
                }
                System.out.printf("Message Dump:%n%s%n",msg.dump());
//                latch.countDown();  // unblock main thread
            }

            @Override
            public void onException(JCSMPException e) {
                System.out.printf("Consumer received exception: %s%n",e);
//                latch.countDown();  // unblock main thread
            }
        }); 
        session.addSubscription(topic);
        System.out.println("Connected. Awaiting message...");
        cons.start();
        // Consume-only session is now hooked up and running!

    }
*/
    public void send(String topicName, String text) throws JCSMPException {
    	if (msg == null) {
        	this.msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);    		
    	}
    	msg.reset();
        msg.setText(text);
        msg.setDeliveryMode(DeliveryMode.PERSISTENT);
//        System.out.printf("Connected. About to send message '%s' to topic '%s'...%n", text, topic);
//        logger.info(msg.toString());
        prod.send(msg, topic);
//        System.out.printf("Connected. ----- to send message '%s' to topic '%s'...%n", text, msg.getMessageId());
    }
    
//    public void sendMany(String topicName, String text) throws JCSMPException {
//    	MultipleSendEntry entries[] = new MultipleSendEntry[1];
//
//    	MultipleSendEntry entry = new MultipleSendEntry();
//    	if (msg == null) {
//        	this.msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);    		
//    	}
//    	msg.reset();
//        msg.setText(text);
//        msg.setDeliveryMode(DeliveryMode.PERSISTENT);
//    	entry.setMessage(msg);
//    	if (topic == null){
//            topic = JCSMPFactory.onlyInstance().createTopic(topicName);
//    	}
//    	entry.setDestination(topic);
//    	entries[0] = entry;
//    	
//        prod.sendMultiple(entries, 0, 1, 0);
//
//    }
    
    private JCSMPProperties getProperties() {
    	JCSMPProperties properties = new JCSMPProperties();
//    	properties.setProperty(JCSMPProperties.HOST, "localhost");
//    	properties.setProperty(JCSMPProperties.USERNAME, "admin");
//    	properties.setProperty(JCSMPProperties.PASSWORD, "admin");
//    	properties.setProperty(JCSMPProperties.VPN_NAME, "default");

    	properties.setProperty(JCSMPProperties.HOST, "192.168.42.110");
    	properties.setProperty(JCSMPProperties.USERNAME, "pankaj");
    	properties.setProperty(JCSMPProperties.PASSWORD, "solace123");
    	properties.setProperty(JCSMPProperties.VPN_NAME, "vpn4pankaj");

    	
    	properties.setProperty(JCSMPProperties.PUB_ACK_WINDOW_SIZE, 255);


    	return(properties);
    }

    
    
}