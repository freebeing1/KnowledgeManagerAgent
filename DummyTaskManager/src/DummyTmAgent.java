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
	public static String USER = "";
	
	public static boolean actionComplete = false;
	public static boolean EVOpen = false;
	
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
////					String msg = "(requestPath \"action\" (currentPoint 5.25 0.8065 0.0) \""+isro_map_IRI+"ReceptionRoom001\" \""+isro_map_IRI+"HospitalRoom001\")";
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
						USER = resultGL.getExpression(1).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asValue().stringValue();
						 
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
						
						
						String glString = "(requestPredicateAnchoring " + USER + " \"걸리다\" \"독감\")";
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
						try
						{
							Thread.sleep(1500);
						}
						catch (InterruptedException e1)
						{
							e1.printStackTrace();
						}
						
						String glString = "(requestPath \"action\" (currentPoint 5.25 0.8065 0.0) \""+isro_map_IRI+"ReceptionRoom001\" \""+isro_map_IRI+"HospitalRoom001\")";
						String res = request(KNOWLEDGEMANAGER_ADDRESS, glString);
						System.out.println("<Request msg>:" + glString);
						System.out.println("<Result>:"+res);
						System.out.println("\n\n");
						
						
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
						
						GeneralizedList pathGL = null;
						try {
							pathGL = GLFactory.newGLFromGLString(res);
							// pathGL = (requestPath "action" (result (action "moveTo({5.25 0.81 0.0}, {5.65 0.82 0.0})") (action "moveTo({5.65 0.82 0.0}, {6.62 0.58 0.0})") (action "moveTo({6.62 0.58 0.0}, {19.8 -1.27 0.0})") (action "moveTo({19.8 -1.27 0.0}, {21.1 -5.88 0.0})") (action "translocateLevel(3, 5)") (action "moveTo({21.1 -5.88 0.0} {19.65 -2.99 0.0})") (action "moveTo({19.65 -2.99 0.0} {19.75 -1.84 0.0})")))
						} catch (ParseException e) {
							e.printStackTrace();
						}
						int pathSize = pathGL.getExpression(1).asGeneralizedList().getExpressionsSize();
						for(int i=0; i<pathSize; i++) {
							String temp = pathGL.getExpression(1).asGeneralizedList().getExpression(i).asGeneralizedList().getExpression(0).asValue().stringValue();
							// temp = "moveTo({0.0 0.0 0.0}, {2.0 7.0 0.0})"
							// temp = "translocateLevel([3], [5])"
							Pattern patternMove = Pattern.compile("\\{(.*?)\\}");
							Matcher matcherMove = patternMove.matcher(temp);
							
							matcherMove.find();
							String startCoord = matcherMove.group(1);
							
							matcherMove.find();
							String endCoord = matcherMove.group(1);
								
							double s_x = Double.parseDouble(startCoord.split(" ")[0]); 
							double s_y = Double.parseDouble(startCoord.split(" ")[1]);
							double s_z = Double.parseDouble(startCoord.split(" ")[2]);
								
							double e_x = Double.parseDouble(endCoord.split(" ")[0]);
							double e_y = Double.parseDouble(endCoord.split(" ")[1]);
							double e_z = Double.parseDouble(endCoord.split(" ")[2]);
								
							robotNavigation(e_x, e_y, e_z);
							
							
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
							}
							actionComplete = false;
							
						}
						
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
					SITUATION = isro_IRI + "requestPuchEVButton(\"Down\")";
				} else {
					SITUATION = isro_IRI + "requestPuchEVButton(\"Up\")";
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
				
				// EV 문 열림 인식 후 들어가서 endLevel+"층 입니다." 인식 후 내리는 것 까지.... 어떡하쥬?
				while(!EVOpen) { // 문 열릴 때("문이 열립니다" 소리날 때)까지 대기
					
					try
					{
						Thread.sleep(200);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					
				}
				robotNavigation(21.6, -6.1, 0.0); // 엘리베이터 내부로 이동
				
				// "5층을 눌러주세요."
				
				// "5층입니다. 문이 열립니다."인식 후 문앞까지 나가기
				
				
			}

			private void robotNavigation(double e_x, double e_y, double e_z) {

				geometry_msgs.PoseStamped msg = publisher_robotNavigation.newMessage();
				msg.getHeader().setFrameId("map");
				msg.getHeader().setSeq(1);
				msg.getPose().getPosition().setX(e_x);
				msg.getPose().getPosition().setY(e_y);
				msg.getPose().getPosition().setZ(e_z);
				
				msg.getPose().getOrientation().setX(0);
				msg.getPose().getOrientation().setY(0);
				msg.getPose().getOrientation().setZ(0.36);
				msg.getPose().getOrientation().setW(0.93);
				System.out.println("Go to : (" + e_x + ", " + e_y + ", "+ e_z +")");
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
		System.out.println("+   Task Manager Start    +");
		System.out.println("+++++++++++++++++++++++++++");
		// /Vision/FaceRecognition "(recognitionData (resolution $width $height) (coordinate $x $y $z) (faceID \"001\"))"
		publisher_conversationRequest = connectedNode.newPublisher("/Dialog/RequestConversationContent", std_msgs.String._TYPE);
		publisher_conversationResponse = connectedNode.newPublisher("/Dialog/ResponseConversationInfo", std_msgs.String._TYPE);
		publisher_robotSpeech = connectedNode.newPublisher("/Action/RequestRobotSpeech", std_msgs.String._TYPE);
		publisher_robotAction = connectedNode.newPublisher("/Action/RequestRobotAction", std_msgs.String._TYPE);
		publisher_robotNavigation = connectedNode.newPublisher("/move_base_simple/goal", geometry_msgs.PoseStamped._TYPE);
		
		Subscriber<std_msgs.String> subscriber_faceRecognition = connectedNode.newSubscriber("/Vision/FaceRecognition", std_msgs.String._TYPE);
		Subscriber<std_msgs.String> subscriber_conversationInfo = connectedNode.newSubscriber("/Dialog/RequestConversationInfo", std_msgs.String._TYPE);
		Subscriber<std_msgs.String> subscriber_conversationContent = connectedNode.newSubscriber("/Dialog/ResponseConversationContent", std_msgs.String._TYPE);
//		Subscriber<std_msgs.String> subscriber_resolution = connectedNode.newSubscriber("/Vision/Resolution", std_msgs.String._TYPE); // (resolution $width $height)
		Subscriber<std_msgs.String> subscriber_speechAnalysis = connectedNode.newSubscriber("/Dialog/SpeechAnalysis", std_msgs.String._TYPE);
		Subscriber<move_base_msgs.MoveBaseActionResult> subscriber_navigationResult = connectedNode.newSubscriber("/move_base/result", move_base_msgs.MoveBaseActionResult._TYPE);
		Subscriber<std_msgs.String> subscriber_EVOpen = connectedNode.newSubscriber("/Elevator", std_msgs.String._TYPE);
		
		subscriber_EVOpen.addMessageListener(new MessageListener<std_msgs.String>() {

			@Override
			public void onNewMessage(std_msgs.String arg)
			{
				String msg = arg.getData();
				if(msg.contains("열")) {
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
					exp2 = GLFactory.newExpression(GLFactory.newGLFromGLString("(predicate \""+isro_social_IRI+"containIntention\" \""+isro_social_IRI+"_"+jsonObject.get("speechIntention").toString()+"Intention\")"));		
					
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