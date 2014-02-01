package ds.service;

public class FactoryService 
{

	public static int clockServiceType = -1;
	public static ClockService clockService = null;
	
	public static void setClockServiceType(int type)
	{
		clockServiceType = type;
	}
	
	
	public static ClockService getClockService(int numProcesses,int currProcIndex)
	{
		if(clockServiceType==-1)
			return null;
		
		if(clockService == null)
		{
			if(clockServiceType == 0)
			{
				clockService = new LogicalClockService();
			}
			else
			{
				clockService = new VectorClassService(numProcesses,currProcIndex);
			}
		}
		return clockService;
	}
}
