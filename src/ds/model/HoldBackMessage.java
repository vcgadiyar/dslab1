package ds.model;

public class HoldBackMessage implements Comparable<HoldBackMessage>{
	
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
	
	public int compareTo(HoldBackMessage hbm)
	{
		int returnVal = 0;
		VectorTimeStamp vts = (VectorTimeStamp)(hbm.getMessage().getGroupTimeStamp());
		VectorTimeStamp current = (VectorTimeStamp)(this.getMessage().getGroupTimeStamp());
		for(int i=0; i<vts.getVectorLength(); i++)
		{
			if(current.getVector()[i] > vts.getVector()[i] )
			{
				if(returnVal==0)
				{
					returnVal = 1;
				}
				else if (returnVal<0)
				{
					return 0;
				}
			}
			else if(current.getVector()[i] < vts.getVector()[i] )
			{
				if(returnVal == 0)
				{
					returnVal = -1;
				}
				else if(returnVal>0)
				{
					return 0;
				}
			}
			
		}
		return returnVal;
	}	
}