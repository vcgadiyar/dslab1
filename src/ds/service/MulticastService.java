package ds.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import util.MessagePasser;
import util.Node;
import ds.model.Constants.Kind;
import ds.model.*;

public class MulticastService {
	MessagePasser msgPasser = null;
	HashMap<String, ArrayList<HoldBackMessage>> holdbackMap = null;
	ReentrantLock hbQueueLock = null;

	public MulticastService() {
		try {
			msgPasser = MessagePasser.getInstance();
			holdbackMap = new HashMap<String, ArrayList<HoldBackMessage>>();
			hbQueueLock = new ReentrantLock();
			
			for (String grpName : msgPasser.groups.keySet()) {				
				ArrayList<HoldBackMessage> grpHoldbackQueue = new ArrayList<HoldBackMessage>();
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
	
	public void receiveMulticast(TimeStampedMessage mmsg)  {
		ArrayList<HoldBackMessage> hbQueue = holdbackMap.get(mmsg.getGroupName());
		
		/* If message is not already there, do this */
		HoldBackMessage hmsg = this.checkExists(mmsg);
		if (hmsg == null)
		{
			Group selectedGroup = msgPasser.groups.get(mmsg.getGroupName());
			hmsg = new HoldBackMessage(mmsg, (selectedGroup.numOfMembers()-1));
			hmsg.addAck(mmsg.getSrc());
			
			/* Take the Lock and re-order Arraylist before adding into HBQ */
			hbQueue.add(hmsg);			
		}
		
		else
		{
			/* Else just decrement the counter for the message received */
			hmsg.addAck(mmsg.getSrc());		
		}		
		
		/* Deliver to Receive Buffer if counter is zero */
		if (hmsg.isReadyToBeDelivered() == true)
		{
			int index = hbQueue.indexOf(hmsg);
			HoldBackMessage reqMsg = hbQueue.remove(index);
			TimeStampedMessage reqTs = reqMsg.getMessage();
			/* Add to recv buffer only if no other messages before this */
			msgPasser.addToRecvBuf(reqTs);
		}	
		
		/* Re-multicast if this is the original sent message */
		if (mmsg.getSrc().equals(mmsg.getOrigSrc()))
		{
			mmsg.setSrc(msgPasser.localName);
			this.multicast(mmsg);
		}
	}
	
	/* Check if a msg exists in the Arraylist */
	public HoldBackMessage checkExists(TimeStampedMessage msg)
	{
		ArrayList<HoldBackMessage> hbQueue = holdbackMap.get(msg.getGroupName());
		
		for (HoldBackMessage hmsg: hbQueue)
		{
			boolean ret = false;
			ret = this.compareTS(msg.getGroupTimeStamp().getVector(), hmsg.getMessage().getGroupTimeStamp().getVector());
			if (msg.getOrigSrc().equals(hmsg.getMessage().getOrigSrc()) && ret &&	msg.getGroupName().equals(hmsg.getMessage().getGroupName()))
			{
				return hmsg;
			}
		}
		/* Not found */
		return null;		
	}
	
	public boolean compareTS(int [] a1, int [] a2)
	{
		if (a1.length != a2.length)
		{
			return false;
		}
		
		for (int i=0; i<a1.length ; i++)
		{
			if (a1[i] != a2[i])
			{
				return false;
			}
		}
		return true;
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
		ArrayList<HoldBackMessage> hbQueue = this.holdbackMap.get(mmsg.getGroupName());
		
		//TODO - Not all messages should go to the queue. Also keep track of which ones reached and which didn't.
		//hbQueue.add(mmsg);
		
		//msgPasser.send(mmsg);
	}

	public HashMap<String, ArrayList<HoldBackMessage>> getHoldbackMap() {
		return holdbackMap;
	}
}
