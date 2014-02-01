package ds.service;

import ds.model.TimeStamp;

public class ClockService {
	
	protected TimeStamp timeStamp;
	
	public TimeStamp getCurrentTime()
	{
		return this.timeStamp;
	}

}
