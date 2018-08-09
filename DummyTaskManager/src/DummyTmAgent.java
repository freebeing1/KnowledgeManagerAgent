import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import geometry_msgs.PoseStamped;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import move_base_msgs.MoveBaseActionResult;

public class DummyTmAgent extends ArbiAgent implements NodeMain{

	public static final String JMS_BROKER_URL = "tcp://127.0.0.1:61616";
	public static final String TASKMANAGER_ADDRESS = "agent://www.arbi.com/taskManager";
	public static final String CONTEXTMANAGER_ADDRESS = "agent://www.arbi.com/contextManager";
	public static final String KNOWLEDGEMANAGER_ADDRESS = "agent://www.arbi.com/knowledgeManager";

	public static final boolean MODE_TEST = false;
	public static final boolean MODE_RUN = true;

	public static int SCENE_NUMBER = 5;
	
	public static boolean SCENE1_COMPLETE = false;
	public static boolean SCENE2_COMPLETE = false;
	public static boolean SCENE3_COMPLETE = false;
	public static boolean SCENE4_COMPLETE = false;
	public static boolean SCENE5_COMPLETE = false;
	
	public static String FACE_ID = "";
	
	public static String SITUATION = "";
	public static String EXPRESSION = "";
	public static String USER = "http://www.robot-arbi.kr/ontologies/DemoKM.owl#Person001";
	
	public static boolean actionComplete = false;
	public static boolean EVOpen = false;
	public static boolean guideStart = false;
	public static boolean speechStart = false;
	public static boolean actionAborted = false;
	public static boolean getOnEV = false;
	public static boolean userLost = false;
	public static boolean userFound = false;
	public static geometry_msgs.PoseWithCovarianceStamped robotCurrentPose = null;
	
	// IRIs
	public static final String isro_IRI = "http://www.robot-arbi.kr/ontologies/isro.owl#";
	public static final String isro_social_IRI = "http://www.robot-arbi.kr/ontologies/isro_social.owl#";
	public static final String isro_map_IRI = "http://www.robot-arbi.kr/ontologies/isro_map.owl#";
	public static final String DemoKM_IRI = "http://www.robot-arbi.kr/ontologies/DemoKM.owl#";
	public static final String rdf_IRI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String knowrob_IRI = "http://knowrob.org/kb/knowrob.owl#";
	
	
	
	// publisher
	public static Publisher<std_msgs.String> publisher_conversationRequest;
	public static Publisher<std_msgs.String> publisher_conversationResponse;
	public static Publisher<std_msgs.String> publisher_robotSpeech;
	public static Publisher<std_msgs.String> publisher_robotAction;
	public static Publisher<geometry_msgs.PoseStamped> publisher_robotNavigation;
	public static Publisher<geometry_msgs.Twist> publisher_robotUserLost;
	
	
	public DummyTmAgent() {
		ArbiAgentExecutor.execute(JMS_BROKER_URL, TASKMANAGER_ADDRESS, this);
		Scanner sc = new Scanner(System.in);

		boolean mode = MODE_TEST;

		new Thread() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {

//				while(true) {
//					
//					System.out.println("Enter msg type 1.Request 2.Query");
//
//					int input = sc.nextInt();
//
//					System.out.println("Enter msg Content:");
//					sc.nextLine();
//					String msg = sc.nextLine();
//					
//					String result;
//
//					if (input == 1) {
//						result = request(KNOWLEDGEMANAGER_ADDRESS, msg);
//						System.out.println("<Request msg>:"+msg);
//
//					} else {
//						result = query(KNOWLEDGEMANAGER_ADDRESS, msg);
//						System.out.println("<Query msg>:"+msg);
//					}
//
//					System.out.println("<Result>:"+result);
//					System.out.println("\n\n");
//				}
				
				while(true) {
					
					try
					{
						Thread.sleep(2500);
					}
					catch (InterruptedException e2)
					{
						e2.printStackTrace();
					}
					
					if (SCENE_NUMBER == 1 && SCENE1_COMPLETE == false) {
						String glString = "(queryMultiRelation (tripleSet (triple $s \""+isro_social_IRI+"faceID\" \""+FACE_ID+"\") (triple $s \""+rdf_IRI+"type\" \""+knowrob_IRI+"Person\")) $result)";
						//(queryMultiRelation (tripleSet (triple $s "isro_social:faceID" "001") (triple $s "rdf:type" "knowrob:Person")) $result)
						String res = query(KNOWLEDGEMANAGER_ADDRESS, glString);
						System.out.println("<Query msg>:" + glString);
						System.out.println("<Result>:"+res);
						System.out.println("\n\n");
						
						GeneralizedList resultGL = null; 
						try {
							resultGL = GLFactory.newGLFromGLString(res);
							//(queryMultiRelation (tripleSet (triple $s "isro_social:faceID" "001") (triple $s "rdf:type" "knowrob:Person")) (result (tripleSet (triple "DemoKM:Person001" "isro_social:faceID" "001") (triple "DemoKM:Person001" "rdf:type" "knowrob:Person"))))
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
						// 사용자 확인
//						USER = resultGL.getExpression(1).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asValue().stringValue();
						 
						SITUATION = isro_social_IRI+"Greeting";
						EXPRESSION = isro_IRI+"NeutralExpression";
						
						std_msgs.String requestMsg;
						requestMsg = publisher_conversationRequest.newMessage();
						JSONObject jsonObject_requestConversation = new JSONObject();
						jsonObject_requestConversation.put("situation", SITUATION);
						jsonObject_requestConversation.put("expression", EXPRESSION);
						requestMsg.setData(jsonObject_requestConversation.toJSONString());
						publisher_conversationRequest.publish(requestMsg);
						System.out.println("/Dialog/RequestConversationContent published!");
						System.out.println(jsonObject_requestConversation.toJSONString());
						System.out.println();
						
						
						SCENE1_COMPLETE = true;
						
					}
					if(SCENE_NUMBER == 2 && SCENE2_COMPLETE == false) {
						SITUATION = isro_social_IRI+"StartConversation";
						EXPRESSION = isro_IRI+"EmpatheticExpression";
						
						std_msgs.String requestMsg;
						requestMsg = publisher_conversationRequest.newMessage();
						JSONObject jsonObject_requestConversation = new JSONObject();
						jsonObject_requestConversation.put("situation", SITUATION);
						jsonObject_requestConversation.put("expression", EXPRESSION);
						requestMsg.setData(jsonObject_requestConversation.toJSONString());
						publisher_conversationRequest.publish(requestMsg);
						System.out.println("/Dialog/RequestConversationContent published!");
						System.out.println(jsonObject_requestConversation.toJSONString());
						System.out.println();
						
						
						SCENE2_COMPLETE = true;
						
					}
					if(SCENE_NUMBER == 3 && SCENE3_COMPLETE == false) {
						SITUATION = isro_IRI+"Speaking";
						EXPRESSION = isro_social_IRI+"EmpatheticExpression";
						
						std_msgs.String requestMsg;
						requestMsg = publisher_conversationRequest.newMessage();
						JSONObject jsonObject_requestConversation = new JSONObject();
						jsonObject_requestConversation.put("situation", SITUATION);
						jsonObject_requestConversation.put("expression", EXPRESSION);
						requestMsg.setData(jsonObject_requestConversation.toJSONString());
						publisher_conversationRequest.publish(requestMsg);
						System.out.println("/Dialog/RequestConversationContent published!");
						System.out.println(jsonObject_requestConversation.toJSONString());
						System.out.println();
						
						SCENE3_COMPLETE = true;
					}
					if(SCENE_NUMBER == 4 && SCENE4_COMPLETE == false) {
						
						
						String glString = "(requestPredicateAnchoring \"" + USER + "\" \"걸리다\" \"독감\")";
						//(requestPredicateAnchoring "DemoKM:Person001" "걸리다" "독감")
						String res = request(KNOWLEDGEMANAGER_ADDRESS, glString);
						System.out.println("<Request msg>:" + glString);
						System.out.println("<Result>:"+res);
						System.out.println("\n\n");
						
						SITUATION = isro_IRI + "MedicallyHandling";
						EXPRESSION = isro_social_IRI+"EmpatheticExpression";
						
						std_msgs.String requestMsg;
						requestMsg = publisher_conversationRequest.newMessage();
						JSONObject jsonObject_requestConversation = new JSONObject();
						jsonObject_requestConversation.put("situation", SITUATION);
						jsonObject_requestConversation.put("expression", EXPRESSION);
						requestMsg.setData(jsonObject_requestConversation.toJSONString());
						publisher_conversationRequest.publish(requestMsg);
						System.out.println("/Dialog/RequestConversationContent published!");
						System.out.println(jsonObject_requestConversation.toJSONString());
						System.out.println();
						
						SCENE4_COMPLETE = true;
					}
					
					if(SCENE_NUMBER == 5 && SCENE5_COMPLETE == false) {
						
						while(!speechStart) {
							
							try
							{
								Thread.sleep(200);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
						
						
						SITUATION = isro_IRI + "InitDirectGuide";
						EXPRESSION = isro_social_IRI+"_NeutralExpression";
						
						std_msgs.String requestMsg;
						requestMsg = publisher_conversationRequest.newMessage();
						JSONObject jsonObject_requestConversation = new JSONObject();
						jsonObject_requestConversation.put("situation", SITUATION);
						jsonObject_requestConversation.put("expression", EXPRESSION);
						requestMsg.setData(jsonObject_requestConversation.toJSONString());
						publisher_conversationRequest.publish(requestMsg);
						
						System.out.println("/Dialog/RequestConversationContent published!");
						System.out.println(jsonObject_requestConversation.toJSONString());
						System.out.println();
						
						while(!guideStart) {
							
							try
							{
								Thread.sleep(200);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
						guideStart = false;
						
						String glString = "(queryRelation \""+DemoKM_IRI+"InternalMedicineDepartment001\" \""+knowrob_IRI+"locatedAt\" $o $result)";
						String res = query(KNOWLEDGEMANAGER_ADDRESS, glString);
						System.out.println("<Query msg>:" + glString);
						System.out.println("<Result>:"+res);
						System.out.println("\n\n");
						GeneralizedList resGL = null;
						try
						{
							resGL = GLFactory.newGLFromGLString(res);
						}
						catch (ParseException e1)
						{
							e1.printStackTrace();
						}
						// (queryRelation "http://www.robot-arbi.kr/ontologies/DemoKM.owl#InternalMedicineDepartment001" "http://knowrob.org/kb/knowrob.owl#locatedAt" $o (result (triple "http://www.robot-arbi.kr/ontologies/DemoKM.owl#InternalMedicineDepartment001" "http://knowrob.org/kb/knowrob.owl#locatedAt" "http://www.robot-arbi.kr/ontologies/isro_map.owl#HospitalRoom001")))
						String destination = resGL.getExpression(3).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(2).asValue().stringValue();
						
						glString = "(requestPath \"action\" (currentPoint 0.182 -0.0775 0.0) \""+isro_map_IRI+"ReceptionRoom001\" \""+destination+"\")";
						res = request(KNOWLEDGEMANAGER_ADDRESS, glString);
						System.out.println("<Request msg>:" + glString);
						System.out.println("<Result>:"+res);
						System.out.println("\n\n");
						
						
						
						
						GeneralizedList pathGL = null;
						try {
							pathGL = GLFactory.newGLFromGLString(res);
							// pathGL = (requestPath "action" (result (action "moveTo({5.65 0.82 0.0})") (action "moveTo({6.62 0.58 0.0})") (action "moveTo({19.8 -1.27 0.0})") (action "moveTo({21.1 -5.88 0.0})") (action "translocateLevel([6] [3])") (action "moveTo({19.65 -2.99 0.0})") (action "moveTo({19.75 -1.84 0.0})")))
						} catch (ParseException e) {
							e.printStackTrace();
						}
						int pathSize = pathGL.getExpression(1).asGeneralizedList().getExpressionsSize();
						for(int i=0; i<pathSize; i++) {
							String temp = pathGL.getExpression(1).asGeneralizedList().getExpression(i).asGeneralizedList().getExpression(0).asValue().stringValue();
							// temp = "moveTo({2.0 7.0 0.0})"
							// temp = "translocateLevel([3] [5])"
							
							Pattern patternMove = Pattern.compile("\\{(.*?)\\}");
							Matcher matcherMove = patternMove.matcher(temp);
							if(matcherMove.find()) {
								String endCoord = matcherMove.group(1);
								
								double e_x = Double.parseDouble(endCoord.split(" ")[0]);
								double e_y = Double.parseDouble(endCoord.split(" ")[1]);
								double e_z = Double.parseDouble(endCoord.split(" ")[2]);
								if(i == 3) {
									robotNavigation(e_x, e_y, e_z, 0.99, 0.01);
								}else {
									robotNavigation(e_x, e_y, e_z, 0, 1);
								}
							}
							
							Pattern patternLevel = Pattern.compile("\\[(.*?)\\]");
							Matcher matcherLevel = patternLevel.matcher(temp);
							if(matcherLevel.find()) {
								int startLevel = Integer.parseInt(matcherLevel.group(1));
								
								matcherLevel.find();
								int endLevel = Integer.parseInt(matcherLevel.group(1));
									
								robotTranslocateLevel(startLevel, endLevel);
								
							}

							while(!actionComplete) {
								try {
									Thread.sleep(200);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								if(userLost) {
									
									double x = robotCurrentPose.getPose().getPose().getPosition().getX();
									double y = robotCurrentPose.getPose().getPose().getPosition().getY();
									double z = robotCurrentPose.getPose().getPose().getPosition().getZ();
									robotNavigation(x, y, z, 0, 1);
									
									requestMsg = publisher_robotSpeech.newMessage();
									requestMsg.setData("어디 계신가요? 잘 따라 오고 있으신건가요?");
									publisher_robotSpeech.publish(requestMsg);
									System.out.println("/Action/RequestRobotSpeech published!");
									System.out.println("어디 계신가요? 잘 따라 오고 있으신건가요?");
									System.out.println();
									
									geometry_msgs.Twist msg = publisher_robotUserLost.newMessage();
									for(int j=0;j<5;j++) {
										msg.getAngular().setZ(0.5);
										publisher_robotUserLost.publish(msg);
										System.out.println("/Action/UserFindingAction published!");
										System.out.println();
									}
									
									while(!userFound) {
										try
										{
											Thread.sleep(200);
										}
										catch (InterruptedException e)
										{
											e.printStackTrace();
										}
									}
									requestMsg = publisher_robotSpeech.newMessage();
									requestMsg.setData("거기 계셨군요. 저를 잘 따라 오세요.");
									publisher_robotSpeech.publish(requestMsg);
									System.out.println("/Action/RequestRobotSpeech published!");
									System.out.println("거기 계셨군요. 저를 잘 따라 오세요.");
									System.out.println();
									i--;
									System.out.println("i = " + i );
									break;
								}
							}
							actionComplete = false;
							
							while(getOnEV) {
								try
								{
									Thread.sleep(200);
								}
								catch (InterruptedException e)
								{
									e.printStackTrace();
								}
							}
						}
						
						SITUATION = isro_IRI + "CompleteDirectGuide";
						EXPRESSION = isro_social_IRI+"_NeutralExpression";
						
						requestMsg = publisher_conversationRequest.newMessage();
						jsonObject_requestConversation = new JSONObject();
						jsonObject_requestConversation.put("situation", SITUATION);
						jsonObject_requestConversation.put("expression", EXPRESSION);
						requestMsg.setData(jsonObject_requestConversation.toJSONString());
						publisher_conversationRequest.publish(requestMsg);
						
						System.out.println("/Dialog/RequestConversationContent published!");
						System.out.println(jsonObject_requestConversation.toJSONString());
						System.out.println();
						
						SCENE5_COMPLETE = true;
						
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}

			@SuppressWarnings("unchecked")
			private void robotTranslocateLevel(int startLevel, int endLevel) {
				if(startLevel > endLevel) {
					SITUATION = isro_IRI + "requestPushEVButton(\"Down\")";
				} else {
					SITUATION = isro_IRI + "requestPushEVButton(\"Up\")";
				}
				EXPRESSION = isro_social_IRI+"_NeutralExpression";
				
				std_msgs.String requestMsg;
				requestMsg = publisher_conversationRequest.newMessage();
				JSONObject jsonObject_requestConversation = new JSONObject();
				jsonObject_requestConversation.put("situation", SITUATION);
				jsonObject_requestConversation.put("expression", EXPRESSION);
				requestMsg.setData(jsonObject_requestConversation.toJSONString());
				publisher_conversationRequest.publish(requestMsg);
				System.out.println("/Dialog/RequestConversationContent published!");
				System.out.println(jsonObject_requestConversation.toJSONString());
				System.out.println();
				
				while(!getOnEV) {
					try
					{
						Thread.sleep(200);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}

				String ev_IRI = "http://www.robot-arbi.kr/ontologies/isro_map.owl#Elevator001";
				String ev_Point = "(queryMultiRelation (tripleSet (triple \""+ev_IRI+"\" \""+isro_IRI+"hasInnerPoint\" $IP) (triple $IP \""+knowrob_IRI+"xCoord\" $IPx) (triple $IP \""+knowrob_IRI+"yCoord\" $IPy) (triple $IP \""+knowrob_IRI+"zCoord\" $IPz) (triple \""+ev_IRI+"\" \""+isro_IRI+"hasEntrancePoint\" $EP) (triple $EP \""+knowrob_IRI+"xCoord\" $EPx) (triple $EP \""+knowrob_IRI+"yCoord\" $EPy) (triple $EP \""+knowrob_IRI+"zCoord\" $EPz)) $result)";
				String res = query(KNOWLEDGEMANAGER_ADDRESS, ev_Point);
				System.out.println(res);
				GeneralizedList evGL = null;
				try
				{
					evGL = GLFactory.newGLFromGLString(res);
//					System.out.println("not");
				}
				catch (ParseException e2)
				{
					e2.printStackTrace();
				}
				System.out.println(evGL.toString());
				double evIP_x = Double.parseDouble(evGL.getExpression(1).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(1).asGeneralizedList().getExpression(2).asValue().stringValue());
				double evIP_y = Double.parseDouble(evGL.getExpression(1).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(2).asGeneralizedList().getExpression(2).asValue().stringValue());
				double evIP_z = Double.parseDouble(evGL.getExpression(1).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(3).asGeneralizedList().getExpression(2).asValue().stringValue());
				System.out.println("EV Inner point : ("+evIP_x+", "+evIP_y+", "+evIP_z+")");
				double evEP_x = Double.parseDouble(evGL.getExpression(1).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(5).asGeneralizedList().getExpression(2).asValue().stringValue());
				double evEP_y = Double.parseDouble(evGL.getExpression(1).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(6).asGeneralizedList().getExpression(2).asValue().stringValue());
				double evEP_z = Double.parseDouble(evGL.getExpression(1).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(7).asGeneralizedList().getExpression(2).asValue().stringValue());
				System.out.println("EV Entrance point : ("+evIP_x+", "+evIP_y+", "+evIP_z+")");
				
				robotNavigation(evIP_x, evIP_y, evIP_z, 0, 1); // 엘리베이터 내부로 이동
				requestMsg = publisher_robotSpeech.newMessage();
				requestMsg.setData("제가 엘리베이터에 탈때까지 문을 연채로 잠시만 기다려주세요");
				publisher_robotSpeech.publish(requestMsg);
				System.out.println("/Action/RequestRobotSpeech published!");
				System.out.println("제가 엘리베이터에 탈때까지 문을 연채로 잠시만 기다려주세요");
				System.out.println();
				
				while(!actionComplete) {
					if(actionAborted) { // 문이 닫혀있어서 action aborted되면 문이 열릴때까지 계속 이동요청
						robotNavigation(evIP_x, evIP_y, evIP_z, 0, 1);
						actionAborted = false;
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				requestMsg = publisher_robotSpeech.newMessage();
				requestMsg.setData("기다려주셔서 감사합니다");
				publisher_robotSpeech.publish(requestMsg);
				System.out.println("/Action/RequestRobotSpeech published!");
				System.out.println("기다려주셔서 감사합니다");
				System.out.println();
				
				SITUATION = isro_IRI + "requestPushEVButton(\""+endLevel+"\")";
				
				requestMsg = publisher_conversationRequest.newMessage();
				jsonObject_requestConversation = new JSONObject();
				jsonObject_requestConversation.put("situation", SITUATION);
				jsonObject_requestConversation.put("expression", EXPRESSION);
				requestMsg.setData(jsonObject_requestConversation.toJSONString());
				publisher_conversationRequest.publish(requestMsg);
				System.out.println("/Dialog/RequestConversationContent published!");
				System.out.println(jsonObject_requestConversation.toJSONString());
				System.out.println();
				
				try
				{
					Thread.sleep(15000);
				}
				catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
				
				robotNavigation(evEP_x, evEP_y, evEP_z, 0, 1); // 엘리베이터 외부로 이동
				while(!actionComplete) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(actionAborted) { // 문이 닫혀있어서 action aborted되면 문이 열릴때까지 계속 이동요청
						robotNavigation(evEP_x, evEP_y, evEP_z, 0, 1);
						actionAborted = false;
					}
				}
					
				SITUATION = isro_IRI + "InitDirectGuide";
				EXPRESSION = isro_social_IRI+"_NeutralExpression";
				
				
				requestMsg = publisher_conversationRequest.newMessage();
				jsonObject_requestConversation = new JSONObject();
				jsonObject_requestConversation.put("situation", SITUATION);
				jsonObject_requestConversation.put("expression", EXPRESSION);
				requestMsg.setData(jsonObject_requestConversation.toJSONString());
				publisher_conversationRequest.publish(requestMsg);
				
				System.out.println("/Dialog/RequestConversationContent published!");
				System.out.println(jsonObject_requestConversation.toJSONString());
				System.out.println();
				
				while(!guideStart) {
					try
					{
						Thread.sleep(200);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				guideStart = false;
				
				getOnEV = false;
			}

			private void robotNavigation(double e_x, double e_y, double e_z, double q_z, double q_w) {

				geometry_msgs.PoseStamped msg = publisher_robotNavigation.newMessage();
				msg.getHeader().setFrameId("map");
				msg.getHeader().setSeq(1);
				msg.getPose().getPosition().setX(e_x);
				msg.getPose().getPosition().setY(e_y);
				msg.getPose().getPosition().setZ(e_z);
				
				msg.getPose().getOrientation().setX(0);
				msg.getPose().getOrientation().setY(0);
				msg.getPose().getOrientation().setZ(q_z);
				msg.getPose().getOrientation().setW(q_w);
				System.out.println("Go to : (" + e_x + ", " + e_y + ", "+ e_z +", "+q_z+", "+q_w+")");
				publisher_robotNavigation.publish(msg);
				System.out.println("/move_base_simple/goal published!");
				
			}

		}.start();

	}

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("/DummyTaskManager");
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		System.out.println("+++++++++++++++++++++++++++");
		System.out.println("+ Task Manager Start +");
		System.out.println("+++++++++++++++++++++++++++");
		// /Vision/FaceRecognition "(recognitionData (resolution $width $height) (coordinate $x $y $z) (faceID \"001\"))"
		publisher_conversationRequest = connectedNode.newPublisher("/Dialog/RequestConversationContent", std_msgs.String._TYPE);
		publisher_conversationResponse = connectedNode.newPublisher("/Dialog/ResponseConversationInfo", std_msgs.String._TYPE);
		publisher_robotSpeech = connectedNode.newPublisher("/Action/RequestRobotSpeech", std_msgs.String._TYPE);
		publisher_robotAction = connectedNode.newPublisher("/Action/RequestRobotAction", std_msgs.String._TYPE);
		publisher_robotNavigation = connectedNode.newPublisher("/move_base_simple/goal", geometry_msgs.PoseStamped._TYPE);
		publisher_robotUserLost = connectedNode.newPublisher("/Action/UserFindingAction", geometry_msgs.Twist._TYPE);
		
		Subscriber<std_msgs.String> subscriber_faceRecognition = connectedNode.newSubscriber("/Vision/FaceRecognition", std_msgs.String._TYPE);
		Subscriber<std_msgs.String> subscriber_conversationInfo = connectedNode.newSubscriber("/Dialog/RequestConversationInfo", std_msgs.String._TYPE);
		Subscriber<std_msgs.String> subscriber_conversationContent = connectedNode.newSubscriber("/Dialog/ResponseConversationContent", std_msgs.String._TYPE);
//		Subscriber<std_msgs.String> subscriber_resolution = connectedNode.newSubscriber("/Vision/Resolution", std_msgs.String._TYPE); // (resolution $width $height)
		Subscriber<std_msgs.String> subscriber_speechAnalysis = connectedNode.newSubscriber("/Dialog/SpeechAnalysis", std_msgs.String._TYPE);
		Subscriber<move_base_msgs.MoveBaseActionResult> subscriber_navigationResult = connectedNode.newSubscriber("/move_base/result", move_base_msgs.MoveBaseActionResult._TYPE);
		Subscriber<std_msgs.String> subscriber_EV = connectedNode.newSubscriber("/Elevator", std_msgs.String._TYPE);
		Subscriber<std_msgs.String> subscriber_guide = connectedNode.newSubscriber("/Action/RequestRobotSpeech", std_msgs.String._TYPE);
		Subscriber<std_msgs.String> subscriber_speechStart = connectedNode.newSubscriber("/Voice/SpeechRecognition", std_msgs.String._TYPE);
		Subscriber<std_msgs.String> subscrbier_lostUser = connectedNode.newSubscriber("/Vision/UserLost", std_msgs.String._TYPE);
		Subscriber<geometry_msgs.PoseWithCovarianceStamped> subscriber_robotGeometryStatus = connectedNode.newSubscriber("/amcl_pose", geometry_msgs.PoseWithCovarianceStamped._TYPE);
		
		subscriber_robotGeometryStatus.addMessageListener(new MessageListener<geometry_msgs.PoseWithCovarianceStamped>() {

			@Override
			public void onNewMessage(geometry_msgs.PoseWithCovarianceStamped arg)
			{
				robotCurrentPose = arg;
			}
			
		});
		
		subscrbier_lostUser.addMessageListener(new MessageListener<std_msgs.String>() {

			@Override
			public void onNewMessage(std_msgs.String arg)
			{
				String msg = arg.getData();
				System.out.println("/Vision/UserLost subscribed!");
				System.out.println();
				if(msg.equals("LostUser")) {
					System.out.println(msg);
					userFound = false;
					userLost = true;
				} else if (msg.equals("NewUser")) {
					System.out.println(msg);
					userLost = false;
					userFound = true;
				}
			}
			
		});
		
		subscriber_speechStart.addMessageListener(new MessageListener<std_msgs.String>()
		{

			@Override
			public void onNewMessage(std_msgs.String arg)	{
				String msg = arg.getData();
				if(msg.contains("안내")) {
					speechStart = true;
				}
				
			}
		});
		
		subscriber_guide.addMessageListener(new MessageListener<std_msgs.String>()
		{

			@Override
			public void onNewMessage(std_msgs.String arg)	{
				String msg = arg.getData();
				if(msg.contains("저를 따라")) {
					guideStart = true;
				}
				if(msg.contains("버튼을")) {
					getOnEV = true;
				}
				
			}
		});
		
		subscriber_EV.addMessageListener(new MessageListener<std_msgs.String>() {

			@Override
			public void onNewMessage(std_msgs.String arg)
			{
				String msg = arg.getData();
				if(msg.contains("열립니다")) {
					EVOpen = true;
				}
				
			}
			
		});
		
		subscriber_navigationResult.addMessageListener(new MessageListener<MoveBaseActionResult>() {

			@Override
			public void onNewMessage(MoveBaseActionResult arg) {
				byte result = arg.getStatus().getStatus();
				if(result == 3) {
					actionComplete = true;
					actionAborted = false;
				} else if(result == 4) {
					actionComplete = false;
					actionAborted = true;
				}
			}
		});
		
		subscriber_speechAnalysis.addMessageListener(new MessageListener<std_msgs.String>() {

			@Override
			public void onNewMessage(std_msgs.String arg) {
				/*
				 * rostopic : /Dialog/SpeechAnalysis
				 * messages : {
				 *              "speechPredicate":"괜찮다지다",
				 * 			      "speechSubject":"화자",
				 *              "speechIntention":"_AgreementIntention",
				 *              "speechObject":"",
				 *              "speechContents":"응 괜찮아졌어"
				 *            } 
				 */
				String msg = arg.getData();
				System.out.println("/Dialog/SpeechAnalysis subscribed!");
				System.out.println(msg);
				System.out.println();
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = null;
				
				try {
					jsonObject = (JSONObject) jsonParser.parse(msg);
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				
				Expression exp1 = GLFactory.newValueExpression(isro_IRI+"SpeechRecognition");
				Expression exp2 = null;
				Expression exp3 = null;
				Expression exp4 = null;
				
				try {
					exp2 = GLFactory.newExpression(GLFactory.newGLFromGLString("(predicate \""+isro_social_IRI+"containIntention\" \""+isro_social_IRI+"_"+jsonObject.get("speechIntention").toString()+"\")"));		
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				try {
					exp3 = GLFactory.newExpression(GLFactory.newGLFromGLString("(predicate \""+isro_IRI+"hasContents\" \""+jsonObject.get("speechContents").toString()+"\")"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				String timePoint = sdf.format(date);
				
				try {
					exp4 = GLFactory.newExpression(GLFactory.newGLFromGLString("(predicate \""+knowrob_IRI+"startTime\" \""+timePoint+"\")"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				GeneralizedList gl = GLFactory.newGL("createRelation", exp1, exp2, exp3, exp4);
				// (createRelation "isro:SpeechRecognition" (predicate "isro_social:containIntention" "isro_social:_AgreementIntention") (predicate isro_social:speechContent "응 괜찮아졌어") ... )
				
				String result = request(KNOWLEDGEMANAGER_ADDRESS, gl.toString());
				System.out.println("<Request msg>:" + gl.toString());
				System.out.println("<Result>:"+result);
				System.out.println("\n\n");
				
				SCENE_NUMBER++;
				
			}
		});
		
		subscriber_conversationContent.addMessageListener(new MessageListener<std_msgs.String>() {

			@Override
			public void onNewMessage(std_msgs.String arg) {
				/*
				 * rostopic : /Dialog/ResponseConversationContent
				 * messages : "안녕하세요?"
				 */
				
				String msg = arg.getData();
				System.out.println("/Dialog/ResponseConversationContent subscribed!");
				System.out.println(msg);
				System.out.println();
				// 로봇 행동 질의
				// 이건 나중에 구현하기로.....
				String robotAction = "";
				String robotEmotion = "";
//				String queryMsg = "(queryOnRestriction \""+SITUATION+"\" ";
				
				if(SITUATION.contains("Greeting")) {
					robotAction = isro_social_IRI+"BowingCasually001";
					robotEmotion = isro_social_IRI+"Neutrality";
				}
				if(SITUATION.contains("StartConversation")) {
					robotAction = isro_IRI+"SpeakingGesture";
					robotEmotion = isro_social_IRI+"Sorrow";
				}
				
				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("action", robotAction);
				jsonResponse.put("emotion", robotEmotion);
				
				std_msgs.String resultSpeech;
				resultSpeech = publisher_robotSpeech.newMessage();
				resultSpeech.setData(msg);
				publisher_robotSpeech.publish(resultSpeech);
				System.out.println("/Action/RequestRobotSpeech published!");
				System.out.println(msg);
				System.out.println();
				
				std_msgs.String resultAction;
				resultAction = publisher_robotAction.newMessage();
				resultAction.setData(jsonResponse.toJSONString());
				publisher_robotAction.publish(resultAction);
				System.out.println("/Action/RequestRobotAction published!");
				System.out.println(jsonResponse.toJSONString());
				System.out.println();
			}
		});
		
		
		subscriber_conversationInfo.addMessageListener(new MessageListener<std_msgs.String>() {
			/*
			 * rostopic : /Dialog/RequestConversationInfo
			 * messages : { 
			 *              "key1" : 
			 *                { 
			 *                  "S" : "화자", 
			 *                  "P" : "isro_social:visitFreq", 
			 *                  "O" : "$result" 
			 *                }, 
			 *              "key2" : 
			 *                { 
			 *                  "S" : "화자", 
			 *                  "P" : "isro:fullName", 
			 *                  "O" : "$result"
			 *                }, 
			 *              "key3" : 
			 *                { 
			 *                  "S" : "화자", 
			 *                  "P" : "isro_social:hasAppellation", 
			 *                  "O" : "$result"
			 *                }, 
			 *              "key4" : 
			 *                { 
			 *                  "S" : "화자", 
			 *                  "P" : "isro_social:isAged", 
			 *                  "O" : "$result"
			 *                }, 
			 *              "key5" : 
			 *                { 
			 *                  "S" : "화자", 
			 *                  "P" : "isro:hasEmotionalState", 
			 *                  "O" : "$result"
			 *                }, 
			 *              "mul_key1" : 
			 *                { 
			 *                	"header" : "VisitorPreviousHealth",
			 *                  "triple1" : 
			 *                    { 
			 *                      "S" : "$medicalRecord", 
			 *                      "P" : "isro:targetPerson", 
			 *                      "O" : "화자"
			 *                    }, 
			 *                  "triple2" : 
			 *                    { 
			 *                      "S" : "$medicalRecord", 
			 *                      "P" : "isro:targetDisease", 
			 *                      "O" : "$disease"
			 *                    }, 
			 *                  "triple3" : 
			 *                    { 
			 *                      "S" : "$medicalRecord", 
			 *                      "P" : "knowrob:startTime", 
			 *                      "O" : "$timePoint"
			 *                    }
			 *                }
			 *            }
			 * 
			 */
			
			@SuppressWarnings("unchecked")
			@Override
			public void onNewMessage(std_msgs.String arg) {
				
				String msg = arg.getData();
				System.out.println("/Dialog/RequestConversationInfo subscribed!");
				System.out.println(msg);
				System.out.println();
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = null;
				JSONObject jsonResponse = new JSONObject();
				
				try {
					jsonObject = (JSONObject) jsonParser.parse(msg);
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				
				
				int i = 1;
				while(jsonObject.containsKey("key"+i)) {
					JSONObject triple = (JSONObject) jsonObject.get("key"+i);
					
//					System.out.println(USER);
					String sbj = triple.get("S").toString();
					if(sbj.equals("화자")) { // TM은 현 상황에서 화자가 DeomKM:Person001 인 것을 알고 있어야 한다.
						sbj = USER;
					}
					if(sbj.equals("독감")) { // TM은 현 상황에서 독감이 isro_social:_Influenza 인 것을 알고 있어야 한다.
						sbj = "http://www.robot-arbi.kr/ontologies/isro_social.owl#_Influenza";
					}
					String pre = triple.get("P").toString();
					String obj = triple.get("O").toString();
					if(obj.equals("화자")) {
						obj = USER;
					}
					
					if(sbj.contains("$result")) {
						sbj = "$s";
					} else {
						sbj = "\""+sbj+"\"";
					}
					if(pre.contains("$result")) {
						pre = "$p";
					} else {
						pre = "\""+pre+"\"";
					}
					if(obj.contains("$result")) {
						obj = "$o";
					} else {
						obj = "\""+obj+"\"";
					}
					
					String queryMsg = "(queryRelation "+sbj+" "+pre+" "+obj+" $result)";
					String result = query(KNOWLEDGEMANAGER_ADDRESS, queryMsg);
					System.out.println("<Request msg>:"+queryMsg);
					System.out.println("<Result>:"+result);
					System.out.println("\n\n");
					
					GeneralizedList gl = null;
					try {
						gl = GLFactory.newGLFromGLString(result);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					// S P O
					sbj = gl.getExpression(3).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asValue().stringValue();
					if(sbj.equals(USER)) {
						sbj = "화자";
					}
					pre = gl.getExpression(3).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(1).asValue().stringValue();
					obj = gl.getExpression(3).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(2).asValue().stringValue();
					
					JSONObject jsonSPO = new JSONObject();
					jsonSPO.put("S", sbj);
					jsonSPO.put("P", pre);
					jsonSPO.put("O", obj);
					
					jsonResponse.put("key"+i, jsonSPO);
					
					i++;
				}
				
				i = 1;
				while(jsonObject.containsKey("mul_key"+i)) {
					/*
					 *  "mul_key1" : 
					 *    { 
					 *      "header" : "VisitorPreviousHealth",
					 *      "triple1" : 
					 *        { 
					 *          "S" : "$medicalRecord", 
					 *          "P" : "isro:targetPerson", 
					 *          "O" : "화자"
					 *        }, 
					 *      "triple2" : 
					 *        { 
					 *          "S" : "$medicalRecord", 
					 *          "P" : "isro:targetDisease", 
					 *          "O" : "$disease"
					 *        }, 
					 *      "triple3" : 
					 *        { 
					 *          "S" : "$medicalRecord", 
					 *          "P" : "knowrob:startTime", 
					 *          "O" : "$timePoint"
					 *        }
					 *    }
					 */
					
					JSONObject jsonTripleSet = (JSONObject) jsonObject.get("mul_key"+i);
					JSONObject resultTripleSet = new JSONObject();
					
					GeneralizedList multiGL = null;
					
					String queryMsg = "(queryMultiRelation (tripleSet ";
					int j = 1;
					
					String header = jsonTripleSet.get("header").toString();
					resultTripleSet.put("header", header);
					
					String sbj = "";
					String pre = "";
					String obj = "";
					String triple = "";
					
					while(jsonTripleSet.containsKey("triple"+j)) {
						JSONObject jsonTriple = (JSONObject) jsonTripleSet.get("triple"+j);
					
						sbj = jsonTriple.get("S").toString();
						if(sbj.equals("화자")) {
							sbj = USER;
						}
						if(sbj.equals("독감")) {
							sbj = "http://www.robot-arbi.kr/ontologies/isro_social.owl#_Influenza";
						}
						if(sbj.equals("내과")) {
							sbj = "http://www.robot-arbi.kr/ontologies/DemoKM.owl#InternalMedicineDepartment001";
						}
						pre = jsonTriple.get("P").toString();
						obj = jsonTriple.get("O").toString();
						if(obj.equals("화자")) {
							obj = USER;
						}
						
						if(!sbj.contains("$")) {
							sbj = "\""+sbj+"\"";
						}
						if(!pre.contains("$")) {
							pre = "\""+pre+"\"";
						}
						if(!obj.contains("$")) {
							obj = "\""+obj+"\"";
						}
						
						triple = "(triple "+sbj+" "+pre+" "+obj+") ";
//						System.out.println(triple);
						queryMsg = queryMsg + triple;
						j++;
					}
					
					queryMsg = queryMsg + ") $result)";
					System.out.println(queryMsg);
					
					String result = query(KNOWLEDGEMANAGER_ADDRESS, queryMsg);
					System.out.println("<Request msg>:"+queryMsg);
					System.out.println("<Result>:"+result);
					System.out.println("\n\n");
					
					GeneralizedList gl = null;
					try {
						gl = GLFactory.newGLFromGLString(result);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					
					for(int k = 0;k<j-1;k++) {
						JSONObject resultTriple = new JSONObject();
						sbj = gl.getExpression(1).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(k).asGeneralizedList().getExpression(0).asValue().stringValue();
						
						pre = gl.getExpression(1).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(k).asGeneralizedList().getExpression(1).asValue().stringValue();
						obj = gl.getExpression(1).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(k).asGeneralizedList().getExpression(2).asValue().stringValue();
						if(sbj.equals(USER)) {
							sbj = "화자";
						}
						if(obj.equals(USER)) {
							obj = "화자";
						}
						if(sbj.equals("http://www.robot-arbi.kr/ontologies/DemoKM.owl#InternalMedicineDepartment001")) {
							sbj = "내과";
						}
						if(sbj.equals("http://www.robot-arbi.kr/ontologies/isro_social.owl#_Influenza")) {
							sbj = "독감";
						}
						
						resultTriple.put("S", sbj);
						resultTriple.put("P", pre);
						resultTriple.put("O", obj);
						
						resultTripleSet.put("triple"+(k+1), resultTriple);
						System.out.println(resultTripleSet.toJSONString());
						
					}
					
					jsonResponse.put("mul_key"+i, resultTripleSet);
					System.out.println(jsonResponse.toJSONString());
					
					i++;
				}
				
				std_msgs.String resultMsg;
				resultMsg = publisher_conversationResponse.newMessage();
				resultMsg.setData(jsonResponse.toJSONString());
				publisher_conversationResponse.publish(resultMsg);
				System.out.println("/Dialog/ResponseConversationInfo published!");
				System.out.println(jsonResponse.toJSONString());
				System.out.println();
			}
		});
		
		
		
		
		subscriber_faceRecognition.addMessageListener(new MessageListener<std_msgs.String>() {
			/*
			 * rostopic : /Vision/FaceRecognition
			 * messages : { 
			 * 			    "recognitionData" : 
			 * 			      { 
			 *                  "resolution" : [$width, $height],
			 *                  "coordinate" : [$x, $y, $z],
			 *                  "faceID" : "001"
			 *                }
			 *            }
			 */
			@SuppressWarnings("unchecked")
			@Override
			public void onNewMessage(std_msgs.String arg) {


				String msg = arg.getData();
				
				System.out.println("/Vision/FaceRecognition subscribed!");
				System.out.println(msg);
				System.out.println();
				
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = null;
				
				try {
					jsonObject = (JSONObject) jsonParser.parse(msg);
				} catch (org.json.simple.parser.ParseException e1) {
					e1.printStackTrace();
				}
				
				JSONObject recData = (JSONObject) jsonObject.get("recognitionData");
				
				// $subject
				Expression exp1 = GLFactory.newValueExpression(isro_IRI+"FaceRecognition");
//				System.out.println(exp1);
				// (predicate $p $o)
				Expression exp2 = null;
				Expression exp3 = null;
				Expression exp4 = null;

				
				FACE_ID = recData.get("faceID").toString();
				try {
					exp2 = GLFactory.newExpression(GLFactory.newGLFromGLString("(predicate \""+isro_social_IRI+"faceID\" \""+FACE_ID+"\")"));
//					System.out.println(exp2);
				} catch (ParseException e) {
					e.printStackTrace();
				} // face ID

				String fe = "_Neutrality";
				try {
					exp3 = GLFactory.newExpression(GLFactory.newGLFromGLString("(predicate \""+isro_social_IRI+"facialEmotion\" \""+isro_social_IRI+fe+"\")"));
//					System.out.println(exp3);
				} catch (ParseException e) {
					e.printStackTrace();
				} // facial emotion


				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				String timePoint = sdf.format(date);

				try {
					exp4 = GLFactory.newExpression(GLFactory.newGLFromGLString("(predicate \""+knowrob_IRI+"startTime\" \""+timePoint+"\")"));
//					System.out.println(exp4);
				} catch (ParseException e) {
					e.printStackTrace();
				} // start time

				GeneralizedList gl = GLFactory.newGL("createRelation", exp1, exp2, exp3, exp4);
//				System.out.println(gl);
				String result = request(KNOWLEDGEMANAGER_ADDRESS, gl.toString());
				System.out.println("<Request msg>:" + gl.toString());
				System.out.println("<Result>:"+result);
				System.out.println("\n\n");
				try
				{
					Thread.sleep(75);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				@SuppressWarnings("unchecked")
				ArrayList<Long> coordinate = (ArrayList<Long>) recData.get("coordinate");
				long depth = coordinate.get(2); 
				if(depth > 120) { // 거리가 멀때는 일단 대기
					SCENE_NUMBER = 0;
				} else if (depth > 80) { // 거리가 일정거리(120cm) 이내가 되면 주의끌기 행동 요청 및 신원 질의  
					SCENE_NUMBER = 1;
				} else if (depth > 0) { // 거리가 일정거리(80cm) 이내가 되면 대화 시작
					SCENE_NUMBER = 2;
				}
				
			}

		});

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

		DummyTmAgent dummyTmAgent = new DummyTmAgent();

		NodeMain commander = dummyTmAgent;
		NodeConfiguration conf = NodeConfiguration.newPrivate();
		NodeMainExecutor executor = DefaultNodeMainExecutor.newDefault();
		executor.execute(commander, conf);
		executor.shutdown();
	}

}