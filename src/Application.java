import java.util.List;
import java.util.Scanner;

import ds.model.Message;


public class Application
{
	public static void main(String args[]) throws Exception
	{
		if(args.length != 2)
		{
			System.out.println("Usage : Please pass the Config File Name and Local Name as arguments");
			System.exit(-1);
		}
		else
		{
			MessagePasser.createInstance(args[0], args[1]);
		}
		MessagePasser msgPasser = MessagePasser.getInstance();
		
		List<Node> nodes = msgPasser.getNodeList();
		Node loggerNode=null;
		for(Node node:nodes)
		{
			if(node.getName().equals("logger"))
			{
				loggerNode = node;
				break;
			}
		}
		
		
		System.out.println("Welcome to the COMMUNICATOR!");
		System.out.println("---------------------");
		Scanner reader = new Scanner(System.in);
		while(true)
		{
			System.out.println("Please select one of the following options");
			System.out.println("1> Send");
			System.out.println("2> Receive");
			System.out.println("3> Exit");
			
			int option = 0;
			while( option!=1 && option!=2 && option!=3 )
			{
				System.out.print("Select Option(1 or 2 or 3) : ");
				option = reader.nextInt();
			}
			reader.nextLine();
			System.out.println();
			
			switch(option)
			{
				case 1:
				{
					System.out.println("Please select a destination");
					int i=0;
					for(Node node:nodes)
					{
						i++;
						System.out.println(i+"> "+node.getName());
					}
					option = 0;
					while(option<1 || option>i)
					{
						System.out.print("Select Destination: ");
						option = reader.nextInt();
					}
					reader.nextLine();
					System.out.println();
					
					String kind="";
					while(kind.toString().equals(""))
					{
						System.out.print("Kind of message: ");
						kind = reader.nextLine();
					}
					System.out.println();
					
					String shouldLog = "";
					while( !shouldLog.equals("y") && !shouldLog.equals("n") )
					{
						System.out.print("Should the message be logged (y for yes and n for no): ");
						shouldLog = reader.nextLine();
					}
					System.out.println();
					
					
					String message="";
					while(message.toString().equals(""))
					{
						System.out.print("Message text: ");
						message = reader.nextLine();
					}
					System.out.println();
					
 
					TimeStampedMessage msg1 = new TimeStampedMessage(nodes.get(option-1).getName(), kind, message);
					msgPasser.send(msg1);
					
					if(shouldLog.equals("y"))
					{
						//String logMessage = " sent message to "+msg1.getDest()+": "+message;
						Object logMessage = msg1;
						TimeStampedMessage logMsg = new TimeStampedMessage(loggerNode.getName(),"log", logMessage );
						msgPasser.send(logMsg);
					}
					
					System.out.println("Finished sending message.");
					System.out.println();
				}
				break;
					
				case 2:
				{
					TimeStampedMessage msg1 = msgPasser.receive();
					
					if(msg1!=null)
					{
						System.out.println(msg1.getData());
						System.out.println();
						String shouldLog = "";
						while( !shouldLog.equals("y") && !shouldLog.equals("n") )
						{
							System.out.print("Should the received message be logged (y for yes and n for no): ");
							shouldLog = reader.nextLine();
						}
						System.out.println();
						if(shouldLog.equals("y"))
						{
							//String logMessage = " received message from "+msg1.getSrc()+": "+msg1.getData().toString();
							Object logMessage = msg1;
							TimeStampedMessage logMsg = new TimeStampedMessage(loggerNode.getName(),"log", logMessage );
							msgPasser.send(logMsg);
						}
					}
					else
						System.out.println("No pending messages");
					
					
					
					System.out.println();
				}
					
				break;
				
				
				case 3:
				{
					System.out.println("==========================");
					System.out.println("  Have a Good Day!! :)");
					System.out.println("==========================");
					System.exit(0);
				}
			}
			
		}
		
		
		
		
	}
}
