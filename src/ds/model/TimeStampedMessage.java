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
	}
	
	public TimeStampedMessage(TimeStampedMessage orig)
	{
		super(orig.getDest(), orig.getKind(), orig.getData());
		this.groupName = orig.getGroupName();
		this.origSrc = orig.getOrigSrc();
		this.timeStamp = orig.getTimeStamp();
		this.groupTimeStamp = new VectorTimeStamp(orig.getTimeStamp());
	}

	public TimeStamp getTimeStamp() 
	{
		return timeStamp;
	}

	public void setTimeStamp(TimeStamp timeStamp) 
	{
		this.timeStamp = timeStamp;
	}
	
	/* Get and set Group TimeStamp */
	public TimeStamp getGroupTimeStamp()
	{
		return this.groupTimeStamp;
	}
	
	public void setGroupTimeStamp(TimeStamp ts)
	{
		this.groupTimeStamp = ts;
	}
	
	public void setKind(String kind)
	{
		this.kind = kind;
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
