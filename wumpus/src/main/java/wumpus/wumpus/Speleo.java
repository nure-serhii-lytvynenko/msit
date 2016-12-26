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
import jade.util.Logger;

public class Speleo extends Agent {

	// Logger logger = jade.util.Logger.getMyLogger(this.getClass().getName());
	public static String AGENT_RECEIVER_NAME = "Speleo";

	private static final long serialVersionUID = 1L;

	public static String field[][] = { { WampusCellStates.NONE.toString(), WampusCellStates.STENCH.toString(),
			WampusCellStates.WAMPUS.toString(), WampusCellStates.STENCH.toString(), },

			{ WampusCellStates.BREEZE.toString(),
					WampusCellStates.NONE.toString(), WampusCellStates.GLITTER.toString() + " "
							+ WampusCellStates.STENCH.toString() + " " + WampusCellStates.BREEZE.toString(),
					WampusCellStates.NONE.toString(), },

			{ WampusCellStates.DIG.toString(), WampusCellStates.BREEZE.toString(), WampusCellStates.DIG.toString(),
					WampusCellStates.BREEZE.toString(), },

			{ WampusCellStates.BREEZE.toString(), WampusCellStates.NONE.toString(), WampusCellStates.BREEZE.toString(),
					WampusCellStates.DIG.toString(), } };
	private String agentPosition = "0-0";

	public String getCellValue(String index) {
		String[] a = index.split("-");
		if (a.length == 2) {

			System.out.println(this.field[Integer.parseInt(a[0])][Integer.parseInt(a[1])]);

			return this.field[Integer.parseInt(a[0])][Integer.parseInt(a[1])];

		}
		return "";
	}

	protected void setup() {

		System.out.println("Hello Eclipse! This is " + getLocalName());
		for (String[] strings : field) {
			for (String string : strings) {
				System.out.print(string + "--");
			}
			System.out.println();
		}

		addBehaviour(new CyclicBehaviour(this) {
			private boolean finished = false;

			public void action() {
				if(finished){
					block();
				}
				System.out.println("\nPlease enter current enviroments state: ");
				// BufferedReader buff = new BufferedReader(new
				// InputStreamReader(System.in));
				String enviromentState = agentPosition + " " + getCellValue(agentPosition);// buff.readLine();

				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setLanguage(Navigator.FROM_SPELEOLOG);
				msg.setOntology("ReceiveTest");
				msg.addReceiver(new AID(Navigator.AGENT_RECEIVER_NAME, false));
				msg.setContent(enviromentState);
				send(msg);

				doWait(3000);
				System.out.println("\n ==> Agent " + getLocalName() + "is waiting for decision ...");
				MessageTemplate m1 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
				MessageTemplate m2 = MessageTemplate.MatchLanguage(Navigator.FROM_NAVIGATOR);

				// The agent waits for a specific message. If it doesn't arrive
				// the behaviour is suspended until a new message arrives.

				ACLMessage msgResponse = blockingReceive(MessageTemplate.and(m1, m2));

				if (msgResponse != null) {
					System.out.println(
							"\nAgent " + getLocalName() + "received the following message" + msgResponse.toString());
					changePositionFromResponseMessage(msgResponse.getContent());
				} else {
					System.out.println("\nAgent " + getLocalName() + " No message received");
					block();

				}
				doWait(1000);
			}

			private void changePositionFromResponseMessage(String content) {
				String phrase = "Speleo, change position to -->";
				if (content.contains(phrase)) {
					agentPosition = content.replace(phrase, "");
				}else if (content.contains("OVER") || content.contains("CONGRATULATIONS")){
					System.out.println(content);
					
					System.out.println();
					System.out.println("NEW GAME STARTED");
					agentPosition = "0-0";
				}
			}

		});
	}

}

/*
 * System.out.println("\nFirst INFORM sent"); doWait(5000);
 * msg.setLanguage("PlainText"); msg.setContent("SecondInform"); send(msg);
 * System.out.println("\nSecond INFORM sent"); doWait(5000); // same that second
 * msg.setContent("\nThirdInform"); send(msg);
 * System.out.println("\nThird INFORM sent"); doWait(1000);
 * msg.setOntology("ReceiveTest"); msg.setContent("FourthInform"); send(msg);
 * System.out.println("\nFourth INFORM sent"); finished = true;
 * myAgent.doDelete();
 */
