package ds.model;

import ds.model.Message;
import ds.model.TimeStamp;
import ds.model.Constants.Kind;


public class TimeStampedMessage extends Message 
{
	protected TimeStamp timeStamp;
	protected String origSrc;
	protected String groupName;
	protected TimeStamp groupTimeStamp;

	public TimeStampedMessage(Message msg) 
	{
		super(msg);
	}

	public TimeStampedMessage(String name, String kind, Object message) 
	{
		super(name,kind,message);
	}

	public TimeStampedMessage(String name, String kind, Object message, String groupName) 
	{
		super(name,kind,message);

		this.groupName = groupName;
		this.origSrc = super.getSrc();
		//TODO - Time stamp for group
	}

	public TimeStamp getTimeStamp() 
	{
		return timeStamp;
	}

	public void setTimeStamp(TimeStamp timeStamp) 
	{
		this.timeStamp = timeStamp;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getOrigSrc() {
		return origSrc;
	}

	public void setOrigSrc(String origSrc) {
		this.origSrc = origSrc;
	}
}
