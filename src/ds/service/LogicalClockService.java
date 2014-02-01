package ds.service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ds.model.LogicalTimeStamp;
import ds.model.TimeStamp;

public class LogicalClockService extends ClockService
{
	private Lock csLock;
	public LogicalClockService()
	{
		this.timeStamp = new LogicalTimeStamp();
		this.csLock = new ReentrantLock();
	}
	
	public void updateTimeForSend()
	{
		this.csLock.lock();
		((LogicalTimeStamp)this.timeStamp).incrementTime();
		this.csLock.unlock();
	}
	
	public void updateTimeStampForRcv(TimeStamp rcvTime)
	{
		LogicalTimeStamp ltp = (LogicalTimeStamp)rcvTime;
		this.csLock.lock();
		int timeToSet = Math.max(ltp.getTime(), ((LogicalTimeStamp)this.timeStamp).getTime()+1);
		((LogicalTimeStamp)this.timeStamp).setTime(timeToSet);
		this.csLock.unlock();
	}
	
	
}
