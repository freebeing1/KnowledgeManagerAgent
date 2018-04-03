# Knowledge Manager

## KM agent code

**Environment setting**

* OS : Ubuntu 14.04 LTS
* Install RosJava
	* version : indigo
 	
		```
		sudo pip install --upgrade sphinx Pygments 
		```
		```
		git clone https://github.com/rosjava/rosjava_core
		cd rosjava_core
		git checkout -b indigo origin/indigo
		```
		```
		cd rosjava_core
		./gradlew install
		```
			
		```
		./gradlew docs
		./gradlew test
		./gradlew eclipse
		```

* Import rosjava project after installation 
	
* Import ArbiFramework project

* Install Apache Jena
	* download Apache jena [here] (https://jena.apache.org/download/index.cgi)

* Install Turtlebot2 package
	
	
**Usage**

* Ros node usage
	* NodeMain implementation

```
import org.ros.namespace.GraphName;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

public class MyNode implements NodeMain {

	public MyNode(){
	
	}
	
	@Override
	public GraphName getDefaultNodeName() {
		return new GraphName("my_node");
	}

	@Override
	public void onStart(ConnectedNode node) {
	
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
}

public static void main(String[] args) {
	MyNode testNode = new MyNode();
	
	NodeMain commander = testNode;
	NodeConfiguration conf = NodeConfiguration.newPrivate();
	NodeMainExecutor executor = DefaultNodeMainExecutor.newDefault();
	executor.execute(commander, conf);
	executor.shutdown();
	
}

```

* KM code usage
	* run roscore on terminal
	
		```
		roscore
		```
	* run Launcher.java (ARBIFramework-0.8)
	* run KmStarter.java (KnowledgeManager)

	# KnowledgeManagerAgent
