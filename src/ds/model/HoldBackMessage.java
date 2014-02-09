package ds.model;

public class HoldBackMessage {
	
	TimeStampedMessage ts;
	int counter;
	
	public HoldBackMessage()
	{}
	
	public HoldBackMessage(TimeStampedMessage ts, int counter) {
		this.ts = new TimeStampedMessage(ts);
		this.counter = counter; 
	}
	
	public TimeStampedMessage getMessage()
	{
		return this.ts;
	}
	
	public void decrementCounter()
	{
		this.counter--;
	}
	
	public int getCounter()
	{
		return this.counter;
	}
}