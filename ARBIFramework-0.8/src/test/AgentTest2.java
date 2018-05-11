package test;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentTest2 extends ArbiAgent{
	
	public void onStop(){}
	
	public String onQuery(String sender, String query){return "Ignored";}
	public void onData(String sender, String data){}
	public String onSubscribe(String sender, String subscribe){return "Ignored";}
	public void onUnsubscribe(String sender, String subID){}
	public void onNotify(String sender, String notification){}
	
	AgentTest2(){
		ArbiAgentExecutor.execute("Agent2.xml", this);
		System.out.println("agent2");
	}
	
	public void onStart(){
		DataSource dc = new DataSource();
		dc.connect("tcp://localhost:61616", "dc://testdc2");
		System.out.println(dc.retrieveFact("(Robot_position $time $position)"));
		System.out.println(dc.match("(Robot_position $time $position)"));
	}
	
	public String onRequest(String sender, String request){
		System.out.println("ok");
		return "null";}
	
	public static void main(String[] args) {
		new AgentTest2();
	}
}
