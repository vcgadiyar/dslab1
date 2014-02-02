class Reader extends Thread
{
	public void run()
	{
		MessagePasser msgPasser;
		try {
			msgPasser = MessagePasser.getInstance();
		
		while(true)
		{
			TimeStampedMessage rMsg = (TimeStampedMessage)msgPasser.receive();
			
			Logger.addToArray(rMsg);
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}