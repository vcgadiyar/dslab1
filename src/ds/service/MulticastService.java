package ds.service;

import util.MessagePasser;
import util.Node;
import ds.model.Group;
import ds.model.TimeStampedMessage;

public class MulticastService {
	MessagePasser msgPasser = null;

	public MulticastService() {
		try {
			msgPasser = MessagePasser.getInstance();
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

			mmsg.setDest(node.getName());
			msgPasser.send(mmsg);
		}
	}
}
