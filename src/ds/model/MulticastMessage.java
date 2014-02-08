package ds.model;

import ds.model.Constants.Kind;
import ds.model.Message;
import ds.model.TimeStamp;
import ds.model.TimeStampedMessage;

public class MulticastMessage extends TimeStampedMessage {
	protected String groupName;
	protected TimeStamp groupTimeStamp;
	protected Kind type;

	public MulticastMessage(String name, String kind, Object message, String groupName, String type) {
		super(name, kind, message);
		this.groupName = groupName;
		//TODO - generate timeStamp;
		this.type = Kind.valueOf(type);
	}
	
	public MulticastMessage(Message msg, String groupName, String type) {
		super(msg);
		this.groupName = groupName;
		//TODO - generate timeStamp;
		this.type = Kind.valueOf(type);
	}
	
	public MulticastMessage(TimeStampedMessage msg, String groupName, Kind type) {
		super(msg.getDest(), msg.getKind(), msg.getData());
		super.setTimeStamp(msg.getTimeStamp());
		this.groupName = groupName;
		//TODO - generate timeStamp;
		this.type = type;
	}

	public String getGroupName() {
		return groupName;
	}

	public TimeStamp getGroupTimeStamp() {
		return groupTimeStamp;
	}

	public void setGroupTimeStamp(TimeStamp groupTimeStamp) {
		this.groupTimeStamp = groupTimeStamp;
	}

	public Kind getType() {
		return type;
	}

	public void setType(Kind type) {
		this.type = type;
	}
}