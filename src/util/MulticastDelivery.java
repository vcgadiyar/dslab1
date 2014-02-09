package util;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import ds.model.HoldBackMessage;
import ds.model.TimeStampedMessage;
import ds.service.FactoryService;

public class MulticastDelivery extends Thread {
	private String groupName;
	
	public MulticastDelivery(String groupName){
		super();
		this.groupName = groupName;
	}
	
	public boolean checkIfOkToDeliver(HoldBackMessage hbMsg, ArrayList<HoldBackMessage> hbQueue) {
		for (HoldBackMessage holdBackMessage : hbQueue) {
			if (hbMsg.compareTo(holdBackMessage) == 1)
				return false;
		}
		
		return true;
	}
	
	public void run(){
		while(true) {
			HashMap<String, ArrayList<HoldBackMessage>> holdbackMap = FactoryService.getMultiCastService().getHoldbackMap();
			ArrayList<HoldBackMessage> hbQueue = holdbackMap.get(groupName);
			ArrayList<String> unicastList = new ArrayList<String>();
			
			Collections.sort(hbQueue);
			
			for (HoldBackMessage holdBackMessage : hbQueue) {
				if (holdBackMessage.isReadyToBeDelivered())
				{
					if (checkIfOkToDeliver(holdBackMessage, hbQueue))
					{
						hbQueue.remove(holdBackMessage);
						try {
							MessagePasser.getInstance().addToRecvBuf(holdBackMessage.getMessage());
							continue;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				unicastList = holdBackMessage.getRemainingAckList();
				
				for (String name : unicastList) {
					//TODO - Unicast a message
				}
			}
			
			try {
				sleep(Application.intervalTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
