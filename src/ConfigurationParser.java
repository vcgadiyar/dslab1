import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

public class ConfigurationParser
{
	private static long lastModified;
	
	public static void parseConfigurationFile(String fileName, String localName, Node localNode, List<Node> nodeList, List<Rule> sendRuleList, List<Rule> receiveRuleList)
	{
		try
		{
			File file = new File(fileName);
			lastModified = file.lastModified();
			InputStream input = new FileInputStream(file);
		    Yaml yaml = new Yaml();
		    Object data = yaml.load(input);
		    
		    LinkedHashMap<String,ArrayList> level1Map = (LinkedHashMap)data;
		    
		    ArrayList<HashMap> nodes = (ArrayList)level1Map.get("configuration");
		    ArrayList<HashMap> sendRules = (ArrayList)level1Map.get("sendRules");
		    ArrayList<HashMap> receiveRules = (ArrayList)level1Map.get("receiveRules");
	
		    Node node ;
		    for(HashMap<String,Object> nodeProps : nodes)
	    	{
		    	node = new Node(nodeProps.get("name").toString(),nodeProps.get("ip").toString(),(Integer)nodeProps.get("port"));
		    	if(nodeProps.get("name").toString().equals(localName))
		    	{
		    		//localNode = node;
		    		localNode.setName(node.getName());
		    		localNode.setIp(node.getIp());
		    		localNode.setPort(node.getPort());
		    	}
		    	else
		    	{
		    		nodeList.add(node);
		    	}
	    	}
		    
		    Rule rule;
		    for(HashMap<String,Object> sendRule : sendRules)
	    	{
		    	String src = sendRule.get("src") != null ? sendRule.get("src").toString() : null ; 
		    	String dest = sendRule.get("dest") != null ? sendRule.get("dest").toString() : null ; 
		    	String kind = sendRule.get("kind") != null ? sendRule.get("kind").toString() : null ;
		    	String action = sendRule.get("action") != null ? sendRule.get("action").toString() : null ;
		    	int seqNum = sendRule.get("seqNum") != null ? (Integer)sendRule.get("seqNum") : -1 ;
		    	Boolean dup = sendRule.get("duplicate")!=null ? new Boolean((boolean)Boolean.valueOf(sendRule.get("duplicate").toString())) : null;
	
				rule = new Rule(src,dest,kind,seqNum,dup,action);
				sendRuleList.add(rule);
	    	}
	
		    for(HashMap<String,Object> receiveRule : receiveRules)
	    	{
		    	String src = receiveRule.get("src") != null ? receiveRule.get("src").toString() : null ; 
		    	String dest = receiveRule.get("dest") != null ? receiveRule.get("dest").toString() : null ; 
		    	String kind = receiveRule.get("kind") != null ? receiveRule.get("kind").toString() : null ;
		    	String action = receiveRule.get("action") != null ? receiveRule.get("action").toString() : null ;
		    	int seqNum = receiveRule.get("seqNum") != null ? (Integer)receiveRule.get("seqNum") : -1 ;
		    	Boolean dup = receiveRule.get("duplicate")!=null ? new Boolean((boolean)Boolean.valueOf(receiveRule.get("duplicate").toString())) : null;
	
				rule = new Rule(src,dest,kind,seqNum,dup,action);
	    		receiveRuleList.add(rule);
	    	}
	   
	    
			input.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void checkAndUpdateRulesIfChanged(String fileName, List<Rule> sendRuleList, List<Rule> receiveRuleList)
	{
		try
		{
			File file = new File(fileName);
			long modified = file.lastModified();
			
			if(modified<=lastModified)
			{
				return;
			}
			
			InputStream input = new FileInputStream(file);
		    Yaml yaml = new Yaml();
		    Object data = yaml.load(input);
		    
		    LinkedHashMap<String,ArrayList> level1Map = (LinkedHashMap)data;
		    
		   
		    ArrayList<HashMap> sendRules = (ArrayList)level1Map.get("sendRules");
		    ArrayList<HashMap> receiveRules = (ArrayList)level1Map.get("receiveRules");
		    
		    sendRuleList.clear();
		    receiveRuleList.clear();
		    
		    Rule rule;
		    for(HashMap<String,Object> sendRule : sendRules)
	    	{
		    	String src = sendRule.get("src") != null ? sendRule.get("src").toString() : null ; 
		    	String dest = sendRule.get("dest") != null ? sendRule.get("dest").toString() : null ; 
		    	String kind = sendRule.get("kind") != null ? sendRule.get("kind").toString() : null ;
		    	String action = sendRule.get("action") != null ? sendRule.get("action").toString() : null ;
		    	int seqNum = sendRule.get("seqNum") != null ? (Integer)sendRule.get("seqNum") : -1 ;
		    	Boolean dup = sendRule.get("duplicate")!=null ? new Boolean((boolean)Boolean.valueOf(sendRule.get("duplicate").toString())) : null;
	
				rule = new Rule(src,dest,kind,seqNum,dup,action);
				sendRuleList.add(rule);
	    	}
	
		    for(HashMap<String,Object> receiveRule : receiveRules)
	    	{
		    	String src = receiveRule.get("src") != null ? receiveRule.get("src").toString() : null ; 
		    	String dest = receiveRule.get("dest") != null ? receiveRule.get("dest").toString() : null ; 
		    	String kind = receiveRule.get("kind") != null ? receiveRule.get("kind").toString() : null ;
		    	String action = receiveRule.get("action") != null ? receiveRule.get("action").toString() : null ;
		    	int seqNum = receiveRule.get("seqNum") != null ? (Integer)receiveRule.get("seqNum") : -1 ;
		    	Boolean dup = receiveRule.get("duplicate")!=null ? new Boolean((boolean)Boolean.valueOf(receiveRule.get("duplicate").toString())) : null;
	
				rule = new Rule(src,dest,kind,seqNum,dup,action);
	    		receiveRuleList.add(rule);
	    	}
	   
	    
			input.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
