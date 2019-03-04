package co.eventmesh.samples.helloperf;

import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPStreamingPublishEventHandler;

public class PublishEventHandler implements JCSMPStreamingPublishEventHandler {


	@Override
	public void handleError(String messageID, JCSMPException e, long timestamp) {
        System.out.printf("Producer received error for msg: %s@%s - %s%n", messageID, timestamp, e);
		
	}

	@Override
	public void responseReceived(String messageID) {
//      System.out.println("Producer received response for msg: " + messageID);
		
	}

	
	
}
