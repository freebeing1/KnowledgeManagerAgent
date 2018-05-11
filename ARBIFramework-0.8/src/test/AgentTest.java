package test;

import org.omg.PortableServer.RequestProcessingPolicyOperations;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentTest extends ArbiAgent{
	
	public void onStop(){}
	public String onRequest(String sender, String request){return "Ignored";}
	public String onQuery(String sender, String query){return "Ignored";}
	public void onData(String sender, String data){}
	public String onSubscribe(String sender, String subscribe){return "Ignored";}
	public void onUnsubscribe(String sender, String subID){}
	public void onNotify(String sender, String notification){}
	
	AgentTest(){
		ArbiAgentExecutor.execute("Agent1.xml", this);
		System.out.println("agent1");
	}
	
	public void onStart(){
		System.out.println("start");
		//request("agent://AgentTest2", "hello");
		DataSource dc = new DataSource();
		dc.connect("tcp://localhost:61616", "dc://testdc1");
		dc.assertFact("(Robot_position (time 467015366.818170342) (position 8.2118526332695385 -9.5512692050022654))");
		//System.out.println(dc.retrieveFact("(Robot_position $time $position)"));
	}
	
	public static void main(String[] args) {
		new AgentTest();
	}
}