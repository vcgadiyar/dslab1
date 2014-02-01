import ds.model.TimeStamp;


public class TimeStampedMessage extends Message 
{
	protected TimeStamp timeStamp;
	
	public TimeStampedMessage(Message msg) 
	{
		super(msg);
	}

	public TimeStamp getTimeStamp() 
	{
		return timeStamp;
	}

	public void setTimeStamp(TimeStamp timeStamp) 
	{
		this.timeStamp = timeStamp;
	}
	
	
}
