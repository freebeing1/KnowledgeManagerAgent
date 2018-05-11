import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class DummyTmAgent extends ArbiAgent implements NodeMain{

	public static final String JMS_BROKER_URL = "tcp://127.0.0.1:61616";
	public static final String TASKMANAGER_ADDRESS = "agent://www.arbi.com/taskManager";
	public static final String CONTEXTMANAGER_ADDRESS = "agent://www.arbi.com/contextManager";
	public static final String KNOWLEDGEMANAGER_ADDRESS = "agent://www.arbi.com/knowledgeManager";
	
	public static final boolean MODE_TEST = false;
	public static final boolean MODE_RUN = true;
	
	public DummyTmAgent() {
		ArbiAgentExecutor.execute(JMS_BROKER_URL, TASKMANAGER_ADDRESS, this);
		Scanner sc = new Scanner(System.in);
		
		
		
		
		boolean mode = MODE_TEST;
		
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(mode) {
					while(true) {
						System.out.println("Enter msg type 1.Request 2.Query");

						int input = sc.nextInt();
						
						System.out.println("Enter msg Content:");
						sc.nextLine();
						String msg = sc.nextLine();


						String result;

						if (input == 1) {
							result = request(KNOWLEDGEMANAGER_ADDRESS, msg);
							System.out.println("<Request msg>:"+msg);
		
						} else {
							result = query(KNOWLEDGEMANAGER_ADDRESS, msg);
							System.out.println("<Query msg>:"+msg);
						}
		
						System.out.println("<Result>:"+result);
						System.out.println("\n\n");
					}
				}
				else {
					while(true) {
						
						System.out.println("Enter msg type 1.Request 2.Query");
						sc.nextLine();
						
						int input = 2;
						
						System.out.println(input);
						System.out.println("Enter msg Content:");
						
						String msg = "(queryRelation \"http://www.robot-arbi.kr/ontologies/DemoKM.owl#Person001\" $p $o)";
						
						System.out.println(msg);
						
						String result;
						
						if (input == 1) {
							result = request(KNOWLEDGEMANAGER_ADDRESS, msg);
							System.out.println("<Request msg>:"+msg);
		
						} else {
							result = query(KNOWLEDGEMANAGER_ADDRESS, msg);
							System.out.println("<Query msg>:"+msg);
						}
		
						System.out.println("<Result>:"+result);
						System.out.println("\n\n");
						
					}
				}
			}
		}.start();
	}
	
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("DummyTaskManager");
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShutdown(Node node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShutdownComplete(Node node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Node node, Throwable throwable) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DummyTmAgent dummyTmAgent = new DummyTmAgent();
		
		NodeMain commander = dummyTmAgent;
		NodeConfiguration conf = NodeConfiguration.newPrivate();
		NodeMainExecutor executor = DefaultNodeMainExecutor.newDefault();
		executor.execute(commander, conf);
		executor.shutdown();
	}

}
