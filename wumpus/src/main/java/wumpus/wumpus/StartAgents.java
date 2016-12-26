package wumpus.wumpus;

import java.util.Scanner;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class StartAgents {

	public static void main(String[] args){
	
		String host;
		int port;
		String platform = null;		//default name
		boolean main = true;
	
		host = "localhost";
		port = -1;			//default-port 1099
	
		Runtime runtime = Runtime.instance();
	
		Profile profile = null;
		AgentContainer container = null;
	
		profile = new ProfileImpl(host, port, platform, main);
	
		//Container erzeugen
		container = runtime.createMainContainer(profile);

//		Agent rmagent = new jade.tools.rma.rma();
//		// Remote Monitoring Agenten erzeugen
//		try {
//			AgentController rma = container.acceptNewAgent(
//	                	"RMA",
//	                    rmagent
//	                    );
//			rma.start();
//		} catch(StaleProxyException e) {
//			throw new RuntimeException(e);
//		}
	
		
		// Agenten erzeugen und startet - oder aussteigen.
		AgentController agent;
		AgentController agent2;
		try {
			 agent = container.createNewAgent(
	                	Speleo.AGENT_RECEIVER_NAME,
	                    Speleo.class.getName(),
	                    args);
			agent.start();
	    } catch(StaleProxyException e) {
	        throw new RuntimeException(e);
	    }
	
	
		try {
			 agent2 = container.createNewAgent(
	                	Navigator.AGENT_RECEIVER_NAME,
	                    Navigator.class.getName(),
	                    args);
			agent2.start();
	    } catch(StaleProxyException e) {
	        throw new RuntimeException(e);
	    }
	/* Scanner in = new Scanner(System.in);
     
         String a =in.nextLine();
         ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
 		msg.setContent( "Ping" );
 		
 			msg.addReceiver( agent. );


*/
		
      
	}
}
