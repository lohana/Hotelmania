// Processes the ACEPTATION answer to request

package hotelmania.group3.platform.client.behaviour;

import hotelmania.group3.platform.AgClient3;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class Booking_ExpectAccept extends CyclicBehaviour {

	public Booking_ExpectAccept(Agent agent)
	{
		super(agent);
	}
	
	public void action()
	{
		
		// Waits for sign contract acceptation
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.AGREE),MessageTemplate.MatchProtocol(AgClient3.BOOKAROOM_REQUEST)) );
		
		//ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.AGREE));
		if (msg != null)
		{
			// If an acceptation arrives...
			System.out.println(myAgent.getLocalName()+": received BOOK A ROOM Request ACCEPTATION from "+(msg.getSender()).getLocalName());
			
			AgClient3 agent = (AgClient3)this.myAgent;
			agent.sethasbooked(true);
 
		}
		else
		{
			// If no message arrives
			block();
		}

	}

}
