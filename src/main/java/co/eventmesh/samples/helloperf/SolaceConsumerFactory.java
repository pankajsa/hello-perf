package co.eventmesh.samples.helloperf;

import java.util.HashMap;

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

public class SolaceConsumerFactory{
	static private Logger logger = LoggerFactory.getLogger(SolaceConsumerFactory.class);
	
	
    
    
    public static JCSMPProperties getProperties() {
    	HashMap<String, String> hmap = Configuration.getDefaults();
//    	logger.info(hmap.toString());

    	JCSMPProperties properties = new JCSMPProperties();
//    	properties.setProperty(JCSMPProperties.HOST, "solacea");
//    	properties.setProperty(JCSMPProperties.USERNAME, "admin");
//    	properties.setProperty(JCSMPProperties.PASSWORD, "admin");
//    	properties.setProperty(JCSMPProperties.VPN_NAME, "default");

    	properties.setProperty(JCSMPProperties.HOST, hmap.get("hostname"));
    	properties.setProperty(JCSMPProperties.USERNAME, hmap.get("username"));
    	properties.setProperty(JCSMPProperties.PASSWORD, hmap.get("password"));
    	properties.setProperty(JCSMPProperties.VPN_NAME, hmap.get("vpn"));

    	
//    	properties.setProperty(JCSMPProperties.PUB_ACK_WINDOW_SIZE, 255);
    	
//    	logger.info(properties.toString());


    	return(properties);
    }

    
    
}