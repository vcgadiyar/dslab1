package ds.service;

import ds.model.LogicalTimeStamp;
import ds.model.TimeStamp;

public class LogicalClockService extends ClockService
{
	public LogicalClockService()
	{
		this.timeStamp = new LogicalTimeStamp();
	}
	
	public void updateTimeForSend()
	{
		((LogicalTimeStamp)this.timeStamp).incrementTime(); 
	}
	
	public void updateTimeStampForRcv(TimeStamp rcvTime)
	{
		LogicalTimeStamp ltp = (LogicalTimeStamp)rcvTime;
		int timeToSet = Math.max(ltp.getTime(), ((LogicalTimeStamp)this.timeStamp).getTime()+1);
		((LogicalTimeStamp)this.timeStamp).setTime(timeToSet);
	}
	
	
}
