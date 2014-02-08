package ds.model;
import java.util.ArrayList;

import util.*;



public class Group {
	private String name;
	private ArrayList<Node> members;
	
	public Group(String name)
	{
		this.name = name;
		this.members = new ArrayList<Node>();
	}
	
	public void addToGroup(Node n)
	{
		this.members.add(n);
	}
	
	public int getIndexOf(Node n)
	{
		int index = 0;
		
		index = this.members.indexOf(n);
		return index;
	}
	
	public ArrayList<Node> getMemberArray()
	{
		return this.members;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void printGroupInfo()
	{
		System.out.println("Printing Group Info for Group: "+this.name);
		for (Node a : this.members)
		{
			System.out.println("Name: "+a.getName());
		}
	}
	
	public boolean isMember(String nodeName)
	{
		for (Node node : this.members) {
			if (node.getName().equals(nodeName))
				return true;
		}
		
		return false;
	}
}