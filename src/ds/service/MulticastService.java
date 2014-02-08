package ds.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import util.MessagePasser;
import util.Node;
import ds.model.Group;
import ds.model.TimeStampedMessage;
import ds.model.Constants.Kind;

public class MulticastService {
	MessagePasser msgPasser = null;
	HashMap<String, ArrayList<TimeStampedMessage>> holdbackMap = null;
	ReentrantLock hbQueueLock = null;

	public MulticastService() {
		try {
			msgPasser = MessagePasser.getInstance();
			holdbackMap = new HashMap<String, ArrayList<TimeStampedMessage>>();
			hbQueueLock = new ReentrantLock();
			
			for (String grpName : msgPasser.groups.keySet()) {				
				ArrayList<TimeStampedMessage> grpHoldbackQueue = new ArrayList<TimeStampedMessage>();
				holdbackMap.put(grpName, grpHoldbackQueue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void multicast(TimeStampedMessage mmsg) {
		Group currGrp = msgPasser.groups.get(mmsg.getGroupName());

		for (Node node : currGrp.getMemberArray()) {
			//TODO - Fix this issue
			if (node.getName().equals(msgPasser.localName))
				continue;
			TimeStampedMessage temp = new TimeStampedMessage(mmsg);
			temp.setDest(node.getName());
			msgPasser.send(temp);
		}
	}
	
	public void receiveMulticast(TimeStampedMessage mmsg) {
		ArrayList<TimeStampedMessage> hbQueue = holdbackMap.get(mmsg.getGroupName());
		
		//TODO - Not all messages should go to the queue. Also keep track of which ones reached and which didn't.
		hbQueue.add(mmsg);
		
		/* Re-multicast if this is the original sent message */
		if (mmsg.getSrc().equals(mmsg.getOrigSrc()))
			multicast(mmsg);
	}
	
	public void unicast(TimeStampedMessage msg) {
		TimeStampedMessage newMsg = new TimeStampedMessage(msg);
		
		newMsg.setSrc(msgPasser.localName);
		msgPasser.send(newMsg);
	}
	
	public void receiveUnicast(TimeStampedMessage msg) {
		TimeStampedMessage newMsg = new TimeStampedMessage(msg);
		
		newMsg.setSrc(msgPasser.localName);
		newMsg.setKind(Kind.ACK.toString());
		msgPasser.send(newMsg);
	}
	
	public void receiveAck(TimeStampedMessage mmsg) {
		ArrayList<TimeStampedMessage> hbQueue = this.holdbackMap.get(mmsg.getGroupName());
		
		//TODO - Not all messages should go to the queue. Also keep track of which ones reached and which didn't.
		hbQueue.add(mmsg);
		
		//msgPasser.send(mmsg);
	}
}
