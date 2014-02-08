package ds.service;

import util.MessagePasser;
import ds.model.TimeStampedMessage;

public class MulticastService {
	MessagePasser msgPasser = null;
	public void multicast(TimeStampedMessage mmsg) {
		try {
			msgPasser = MessagePasser.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
