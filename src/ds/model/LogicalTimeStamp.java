package ds.model;

public class LogicalTimeStamp extends TimeStamp 
{
	private int time;
	
	public LogicalTimeStamp()
	{
		this.time = 0;
	}
	
	public int getTime()
	{
		return this.time;
	}
	
	public int incrementTime()
	{
		this.time++;
		return this.time;
	}

	public void setTime(int timeToSet) 
	{	
		this.time = timeToSet;
	}

}
