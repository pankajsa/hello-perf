package co.eventmesh.samples.helloperf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solacesystems.jcsmp.ConsumerFlowProperties;
import com.solacesystems.jcsmp.Context;
import com.solacesystems.jcsmp.EndpointProperties;
import com.solacesystems.jcsmp.FlowReceiver;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.Queue;

public class ConsumeThread {
	static private Logger logger = LoggerFactory.getLogger(ConsumeThread.class);
	
	private String queueName;
		
    private String messageText;
    private int repeatCount = 0;

    
    public ConsumeThread(
    		String queueName){
    	this.queueName = queueName;
    }
    
     
    
    @Override
    public String toString(){
        return this.messageText + " " + this.repeatCount;
    }

	public void setConsumerListener(ConsumeMessageListener handler) throws JCSMPException, InterruptedException {
		
    	ConsumerFlowProperties flow_prop = new ConsumerFlowProperties();
		Queue listenQueue = JCSMPFactory.onlyInstance().createQueue(this.queueName);

    	flow_prop.setEndpoint(listenQueue);
    	flow_prop.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);
    	
    	EndpointProperties endpoint_props = new EndpointProperties();
    	endpoint_props.setAccessType(EndpointProperties.ACCESSTYPE_NONEXCLUSIVE);

    	Context defaultContext = JCSMPFactory.onlyInstance().getDefaultContext();
//    	logger.info("DefaultContext: " + defaultContext );
    	Context context = JCSMPFactory.onlyInstance().createContext(null);
    	JCSMPSession session = JCSMPFactory.onlyInstance().createSession(SolaceConsumerFactory.getProperties(), context);
    	FlowReceiver cons = session.createFlow(handler, flow_prop, endpoint_props);  
//    	logger.info(Thread.currentThread().getName()+" - No waiting to proceeed.");
//		this.startSignal.await();

    	cons.start();
				
	}
}