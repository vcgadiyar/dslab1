import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.w3c.dom.ls.LSLoadEvent;

import ds.model.Constants;
import ds.model.LogicalTimeStamp;
import ds.model.Message;
import ds.model.TimeStamp;
import ds.model.VectorTimeStamp;


class Logger 
{
	private static ArrayList<TimeStampedMessage> msgArray = new ArrayList<TimeStampedMessage>();
	public static Lock logLock = new ReentrantLock();
	
	public ArrayList<TimeStampedMessage> getArray() {
		return msgArray;
	}
	
	public static void addToArray(TimeStampedMessage tmsg) {
		
		msgArray.add(tmsg);
	}
	
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
		Reader r = new Reader();
		r.start();
		
		System.out.println("Welcome to the LOGGER!");
		System.out.println("---------------------");
		Scanner reader = new Scanner(System.in);
		while(true)
		{
			if (MessagePasser.tsType == Constants.TimeStampType.VECTOR)
			{
				System.out.println("Please select one of the following options");
				System.out.println("1> Info about single message");
				System.out.println("2> Info about all messages");
				System.out.println("3> Discard Logs");
				System.out.println("4> Exit");
			
				int option = 0;
				while( option!=1 && option!=2 && option!=3 && option!=4)
				{
					System.out.print("Select Option(1 or 2 or 3 or 4) : ");
					option = reader.nextInt();
				}
				reader.nextLine();
				System.out.println();
			
				switch(option)
				{
					case 1:
					{
						if (msgArray.size() == 0)
						{
							System.out.println("No messages to display");
						}
						else
						{
							System.out.println("List of Messages");
							int i=0;
							for(TimeStampedMessage msg: msgArray)
							{
								i++;
								VectorTimeStamp vin = (VectorTimeStamp)msg.getTimeStamp();
								System.out.println(i+"> Source: "+msg.getSrc()+ Arrays.toString(vin.getVector())+": "+msg.getData().toString());
							}
							option = 0;
							while(option<1 || option>i)
							{
								System.out.print("Select a Message: ");
								option = reader.nextInt();
							}
							option = option - 1;
							reader.nextLine();
							System.out.println();
						
							printAllRelations(option);
							System.out.println();
						}
					}
					break;
					case 2:
					{
						for (int j=0; j<msgArray.size(); j++)
						{
							System.out.println("Printing details for Message "+j);
							printAllRelations(j);
						}
					}
					break;
					case 3:
					{
						logLock.lock();
						msgArray.clear();
						logLock.unlock();
					}
					break;
					case 4:
					{
						System.exit(0);
					}
				}
			}
			else if (MessagePasser.tsType == Constants.TimeStampType.LOGICAL)
			{
				System.out.println("Please select one of the following options");
				System.out.println("1> Info about single message");
				System.out.println("2> Info about all messages");
				System.out.println("3> Discard Logs");
				System.out.println("4> Exit");
							
				int option = 0;
				while( option!=1 && option!=2 && option!=3)
				{
					System.out.print("Select Option(1 or 2 or 3) : ");
					option = reader.nextInt();
				}
				reader.nextLine();
				
				switch(option)
				{
					case 1:
					{
						if (msgArray.size() == 0)
						{
							System.out.println("No messages to display");
						}
						else
						{
							System.out.println("List of Messages");
							int i=0;
							for(TimeStampedMessage msg: msgArray)
							{
								i++;
								LogicalTimeStamp vin = (LogicalTimeStamp)msg.getTimeStamp();
								System.out.println(i+"> ["+ vin.getTime()+"]: "+msg.getData().toString());
							}
							option = 0;
							while(option<1 || option>i)
							{
								System.out.print("Select a Message: ");
								option = reader.nextInt();
							}
							option = option - 1;
							reader.nextLine();
							System.out.println();
						
							printAllLogicalRelations(option);
							System.out.println();
						}
					}
					break;
					
					case 2:
					{
						for (int j=0; j<msgArray.size(); j++)
						{
							System.out.println("Printing details for Message "+j);
							printAllLogicalRelations(j);
						}
					}
					break; 
					
					case 3:
					{
						logLock.lock();
						msgArray.clear();
						logLock.unlock();
					}
					break;
					
					case 4:
					{
						System.exit(0);
					}
				}			
			}			
		}
	}
	
	public static void printCompare(TimeStampedMessage cur, TimeStampedMessage test)
	{
		VectorTimeStamp curTS = (VectorTimeStamp)(cur.getTimeStamp());
		TimeStamp testTS = (test.getTimeStamp());
		VectorTimeStamp vTestTS = (VectorTimeStamp)testTS;
		
		int ret_val = curTS.compareTo(testTS);
		switch(ret_val)
		{
		case -1:
			System.out.println(Arrays.toString(curTS.getVector())+" -> "+Arrays.toString(vTestTS.getVector()));
			break;
		case 0:
			System.out.println(Arrays.toString(curTS.getVector())+" || "+ Arrays.toString(vTestTS.getVector()));
			break;
		case 1:
			System.out.println(Arrays.toString(curTS.getVector())+" <- "+ Arrays.toString(vTestTS.getVector()));
			break;
		}
	}
	
	public static void printLogicalCompare(TimeStampedMessage cur, TimeStampedMessage test)
	{
		LogicalTimeStamp curTS = (LogicalTimeStamp)(cur.getTimeStamp());
		TimeStamp testTS = (test.getTimeStamp());
		LogicalTimeStamp vTestTS = (LogicalTimeStamp)testTS;
		
		int ret_val = curTS.compareTo(vTestTS);
		
		if (ret_val < 0)
			System.out.println("["+curTS.getTime()+"] -> "+"["+ vTestTS.getTime() + "]");
		else if (ret_val == 0)
			System.out.println("["+curTS.getTime()+"] || "+"["+ vTestTS.getTime() + "]");
		else
			System.out.println("["+curTS.getTime()+"] <- "+"["+ vTestTS.getTime() + "]");
		
	}
	
	public static void printAllRelations(int option)
	{
		TimeStampedMessage curTstp = msgArray.get(option);
		
		for(int j=0; j<msgArray.size(); j++)
		{
			if (j == option)
				continue;
			printCompare(curTstp, msgArray.get(j));							
		}
	}
	
	public static void printAllLogicalRelations(int option)
	{
		TimeStampedMessage curTstp = msgArray.get(option);
		
		for(int j=0; j<msgArray.size(); j++)
		{
			if (j == option)
				continue;
			printLogicalCompare(curTstp, msgArray.get(j));							
		}
	}
}