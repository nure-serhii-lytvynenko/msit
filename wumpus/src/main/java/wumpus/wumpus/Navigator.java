package wumpus.wumpus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Navigator extends Agent {
	
	public static String AGENT_RECEIVER_NAME = "Navigator";
	public static String FROM_SPELEOLOG = "FROM_SPELEOLOG";
	public static String FROM_NAVIGATOR = "FROM_NAVIGATOR";
	
	
	private WampusEnviroment wampusEnviroment = new WampusEnviroment();
	class my3StepBehaviour extends CyclicBehaviour {
	

		public my3StepBehaviour(Agent a) {
			super(a);
		}

		public void action() {

			
				op1();
			
			/*
			 * switch (state) { case FIRST: { if (op1()) state = SECOND; else
			 * state = FIRST; break; } case SECOND: { op2(); state = THIRD;
			 * break; } case THIRD: { op3(); state = FIRST; finished = true;
			 * break; } }
			 */

		}

		private void op1() {
			System.out.println( "\n ==> Agent "+getLocalName()+" is waiting for a message");
					 MessageTemplate m1 =
					 MessageTemplate.MatchPerformative(ACLMessage.INFORM);
					 MessageTemplate m2 =
					 MessageTemplate.MatchLanguage(Navigator.FROM_SPELEOLOG);
					 MessageTemplate m3 =
					 MessageTemplate.MatchOntology("ReceiveTest");
					 MessageTemplate m1andm2 = MessageTemplate.and(m1,m2);
					 MessageTemplate notm3 = MessageTemplate.not(m3);
					 MessageTemplate m1andm2_and_notm3 =
					 MessageTemplate.and(m1andm2, notm3);

					 //The agent waits for a specific message. If it doesn't arrive
					 // the behaviour is suspended until a new message arrives.
					 ACLMessage msg = blockingReceive(MessageTemplate.and(m1, m2));
					 
					 if (msg != null) {
							System.out.println("\nAgent " + getLocalName() + "received the following message"
									+ msg.toString());
							
					 System.out.println("==> Navigator try to found decision ...");
					String decision = this.makeDecision(msg.getContent());
					String answer = this.createMessageAboutChangingPosition(decision);
					ACLMessage msgResp = new ACLMessage(ACLMessage.INFORM);
					msgResp.setLanguage(Navigator.FROM_NAVIGATOR);
					msgResp.addReceiver(new AID(Speleo.AGENT_RECEIVER_NAME, false));
					msgResp.setContent(answer);
					doWait(3000);
					send(msgResp);
					
			System.out.println("98989898==>"+answer);
			
				
				
			} else {
				System.out.println("\nAgent " + getLocalName()+"No message received");
				//block();
				
			}
					 
					 
		}

		private String makeDecision(String content) {
			if(content.contains(WampusCellStates.WAMPUS.toString())){
				System.out.println("You are with wampus in one room. Game OVER");
				
				
				return "You are with wampus in one room. Game OVER";
			}
			else if(content.contains(WampusCellStates.DIG.toString())){
				System.out.println("You are fall in dig. Game OVER");
				
				
				return "You are fall in dig. Game OVER";
			}
			else if(content.contains(WampusCellStates.GLITTER.toString())){
				System.out.println("You are with gold in one room. CONGRATULATIONS!!!");
				
				return("You are with gold in one room. CONGRATULATIONS!!!");
			}
			else if(content.contains(WampusCellStates.NONE.toString())){
				if(wampusEnviroment.getPos_i()<4){
					wampusEnviroment.getVisibleField()[wampusEnviroment.getPos_i()+1][wampusEnviroment.getPos_j()] = WampusCellStates.OK.name();
				}
				if(wampusEnviroment.getPos_j()<4){
					wampusEnviroment.getVisibleField()[wampusEnviroment.getPos_i()][wampusEnviroment.getPos_j()+1] = WampusCellStates.OK.name();
				}
				if(wampusEnviroment.getPos_i()<4){
					wampusEnviroment.setPos_i(wampusEnviroment.getPos_i()+1);;
				}
				else if(wampusEnviroment.getPos_j()<4){
					wampusEnviroment.setPos_j(wampusEnviroment.getPos_j()+1);
				}
				else {
					wampusEnviroment.setPos_j(wampusEnviroment.getPos_j()-1);
				}
				return createMessageAboutChangingPosition(wampusEnviroment.getSpeleoPosition());
			}
			else if(content.contains(WampusCellStates.STENCH.toString())){
				if(wampusEnviroment.getPos_i()<4){
					wampusEnviroment.getVisibleField()[wampusEnviroment.getPos_i()+1][wampusEnviroment.getPos_j()] = WampusCellStates.Q.name();
				}
				if(wampusEnviroment.getPos_j()<4){
					wampusEnviroment.getVisibleField()[wampusEnviroment.getPos_i()][wampusEnviroment.getPos_j()+1] = WampusCellStates.Q.name();
				}
				if(wampusEnviroment.getPos_j()<4){
					wampusEnviroment.setPos_j(wampusEnviroment.getPos_i()+1);;
				}
				else if(wampusEnviroment.getPos_i()<4){
					wampusEnviroment.setPos_i(wampusEnviroment.getPos_j()+1);
				}
				else {
					wampusEnviroment.setPos_j(wampusEnviroment.getPos_j()-1);
				}
				return createMessageAboutChangingPosition(wampusEnviroment.getSpeleoPosition());
			}
			
			return createMessageAboutChangingPosition("1-1");
			
		}

		public String createMessageAboutChangingPosition(String position){
			
			
			return "Speleo, change position to -->"+position;
		}
	
	} // End of my3StepBehaviour class

	protected void setup() {
		System.out.println("Hello! This is " + getLocalName());
		my3StepBehaviour mybehaviour = new my3StepBehaviour(this);
		addBehaviour(mybehaviour);

	}
}