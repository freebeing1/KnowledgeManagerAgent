# Knowledge Manager

## KM agent code

### Environment setting

* OS : **Ubuntu 14.04 LTS**
* Install **ROS Indigo**
	* Setup your sources.list
		
		```
		sudo sh -c 'echo "deb http://packages.ros.org/ros/ubuntu $(lsb_release -sc) main" > /etc/apt/sources.list.d/ros-latest.list'
		```
	
	* Setup your key
		
		```
		sudo apt-key adv --keyserver hkp://ha.pool.sks-keyservers.net --recv-key 0xB01FA116
		```
	
	* Installation
	
		```
		sudo apt-get update
		sudo apt-get upgrade
		sudo apt-get install ros-indigo-desktop-full
		```	
		* If you fail to install with the command above, execute following command
			
			```
			sudo apt-get install libsdformat1
			```
	
	* Initialize rosdep
	
		```
		sudo rosdep init
		rosdep update
		```
	
	* Environment setup
	
		```
		echo "source /opt/ros/indigo/setup.bash" >> ~/.bashrc
		source ~/.bashrc
		```
	
	* Getting rosinstall

		```
		sudo apt-get install python-rosinstall
		```

	* Check roscore
		
		```
		roscore
		```
		```
		PARAMETERS
		 *  /rosdistro: indigo
		 *  /rosversion: 1.11.21
		
		NODES
		
		auto-starting new master
		process[master]: started with pid [...]
		ROS_MASTER_URI=http://.../
		
		setting /run_id to ...
		process[rosout-1] started with pid [...]
		started core service [/rosout]
		...
		```

	

* Install **rosjava_core**

	* Prerequisites

		```
		sudo pip install --upgrade sphinx Pygments 
		```
	* Non-ROS Installation
		
		```
		git clone https://github.com/rosjava/rosjava_core
		cd rosjava_core
		git checkout -b indigo origin/indigo
		```
		
	* Building
		
		To build rosjava_core and install it to your local Maven respository, execute gradle wrapper:
		
		```
		cd rosjava_core
		./gradlew install
		```
		
		To build the documentation:
	
		```
		./gradlew docs
		```
		
		To run the tests:
	
		```
		./gradlew test
		```
		
		To generate the Eclipse project files:
	
		```
		./gradlew eclipse
		```

* Install **Apache Jena**
	* Download Apache jena [[here]](https://jena.apache.org/download/index.cgi)

* Install Redis
	
	```
	sudo add-apt-repository ppa:chris-lea/redis-server
	sudo apt-get update
	sudo apt-get install build-essential
	sudo apt-get install redis-server
	```

* Install Turtlebot2 package
	* ... 


* Import **rosjava_core**
	* apache\_xmlrpc\_client
	* apache\_xmlrpc\_common
	* apache\_xmlrpc\_server
	* rosjava
	
* Import **ARBIFramework-0.8**

	
	
### Usage

* Ros node usage
	* NodeMain implementation

```
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
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

* Test DummyTaskManager
	* run roscore on terminal
	
		```
		roscore
		```
	* run Launcher.java (ARBIFramework-0.8)
	* run KmStarter.java (KnowledgeManager)
	* run DummyTmAgent.java (DummyTaskManager)