import ds.model.Constants;
import ds.model.Message;

class Reader extends Thread
{
	public void run()
	{
		MessagePasser msgPasser;
		try {
			msgPasser = MessagePasser.getInstance();
		
		while(true)
		{
			Message message = msgPasser.receive();
			
			if (msgPasser.tsType == Constants.TimeStampType.VECTOR)
			{
				TimeStampedMessage rMsg;
				if (message != null)
				{
					rMsg = (TimeStampedMessage)message.getData();
					Logger.addToArray(rMsg);
				}
			}
			else if (msgPasser.tsType == Constants.TimeStampType.LOGICAL)
			{
				TimeStampedMessage rMsg;
				if (message != null)
				{
					rMsg = (TimeStampedMessage)message.getData();
					if (!(Logger.getHashMap().containsKey(message.getSrc())))
					{
						Logger.putInHashMap(message.getSrc(), rMsg);
					}
					else
					{
						Logger.addToHashMap(message.getSrc(), rMsg);
					}
				}
			}
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}