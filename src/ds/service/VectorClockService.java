package ds.service;
import ds.model.*;

class VectorClockService extends ClockService {
	
	/* Indicator of the current Process index, only for Vector TimeStamp */
	private int index;
	
	/* Constructor for Vector Clock, args: process count from yaml and current process index */
	VectorClockService(int numberOfProcesses, int processIndex)
	{
		timeStamp = new VectorTimeStamp(numberOfProcesses);
		index = processIndex;
	}
	
	/* Returns current local TimeStamp for the process */
	public TimeStamp getCurrentTimeStamp()
	{
		return timeStamp;
	}
	
	/* Update Logical TimeStamp while receiving a message */
	public void updateOnRecv(TimeStamp recvTs)
	{
		VectorTimeStamp timestamp = (VectorTimeStamp)timeStamp;
		VectorTimeStamp recvTS = (VectorTimeStamp)recvTs;
		
		for (int i=0; i < timestamp.getVectorLength() ; i++)
		{
			if (timestamp.getVector()[i] < recvTS.getVector()[i])
			{
				timestamp.setVector(i, recvTS.getVector()[i]);
			}
		}
		timestamp.setVector(index, (timestamp.getVector()[index] + 1));
	}	
	
	/* Update Vector TimeStamp before sending the message */
	public void updateOnSend()
	{
		VectorTimeStamp timestamp = (VectorTimeStamp)timeStamp;
		timestamp.setVector(index, (timestamp.getVector()[index] + 1));
	}
}
