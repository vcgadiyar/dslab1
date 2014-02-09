package ds.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
		
		hbQueueLock.lock();
		ArrayList<HoldBackMessage> hbQueue = holdbackMap.get(mmsg.getGroupName());
		Group selectedGroup = msgPasser.groups.get(mmsg.getGroupName());
		
		/* If message is not already there, do this */
		HoldBackMessage hmsg = this.checkExists(mmsg);
		if (hmsg == null)
		{
			hmsg = new HoldBackMessage(mmsg, (selectedGroup.numOfMembers()-1));
			hmsg.addAck(mmsg.getSrc());
			
			/* Take the Lock and re-order Arraylist after adding into HBQ */
			hbQueue.add(hmsg);
			Collections.sort(hbQueue);
		}
		
		else
		{
			/* Else just decrement the counter for the message received */
			hmsg.addAck(mmsg.getSrc());		
		}		
		
		/* Deliver to Receive Buffer if counter is zero */
		if (hmsg.isReadyToBeDelivered() == true)
		{
			this.causalOrder(selectedGroup.getName());
		}	
		
		/* Re-multicast if this is the original sent message */
		if (mmsg.getSrc().equals(mmsg.getOrigSrc()))
		{
			mmsg.setSrc(msgPasser.localName);
			this.multicast(mmsg);
		}
		hbQueueLock.unlock();
		
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
	
	/* Function to causal order to check whether
	 * any message from the Hold-Back queue can be inserted into
	 * the receive buffer.
	 */
	public void causalOrder(String groupName)
	{
		ArrayList<HoldBackMessage> hbqueue = holdbackMap.get(groupName);
		Group selectedGroup = msgPasser.groups.get(groupName);
		VectorTimeStamp cmpTS = (VectorTimeStamp)selectedGroup.getCurrentGroupTimeStamp();
		
		//for (HoldBackMessage hbm : hbqueue)
		for (Iterator<HoldBackMessage> it = hbqueue.iterator(); it.hasNext(); ) 
		{
			HoldBackMessage hbm = it.next();
			int result = 0;
			result = this.getTSDiff(cmpTS, hbm.getMessage().getGroupTimeStamp());
			
			/* Add to receive buffer on satisfaction of these 2 conditions */
			if (hbm.isReadyToBeDelivered() && (result <= 1))
			{
				int index = hbqueue.indexOf(hbm);
				HoldBackMessage reqMsg = hbqueue.get(index);
				it.remove();
				TimeStampedMessage reqTs = reqMsg.getMessage();
	
				/* Add to recv buffer only if no other messages before this */
				msgPasser.addToRecvBuf(reqTs);

				/* Update the TimeStamp after putting in recv buffer */ 
				selectedGroup.updateGroupTSOnRecv(hbm.getMessage().getGroupTimeStamp(), msgPasser.localName);				
			}			
		}
	}
	
	/* Get TimeStamp difference between 2 Vector TimeStamps */
	public int getTSDiff(VectorTimeStamp t1, VectorTimeStamp t2)
	{
		int result = 0;
		
		for (int i=0; i< t1.getVectorLength(); i++)
		{
			result = result + (t1.getVector()[i] - t2.getVector()[i]);
		}
		
		return result;		
	}
	
	public void unicast(TimeStampedMessage msg) 
	{
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
