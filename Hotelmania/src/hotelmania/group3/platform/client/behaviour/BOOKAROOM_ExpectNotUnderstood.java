// Processes the NOT_UNDERSTOOD answer to request
package hotelmania.group3.platform.client.behaviour;

import hotelmania.group3.hotel.AgHotel3;
import hotelmania.group3.platform.AgClient3;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class BOOKAROOM_ExpectNotUnderstood extends CyclicBehaviour {
	
	public BOOKAROOM_ExpectNotUnderstood(Agent agent) {
		super(agent);
	}
	
	public void action()
	{
		// Waits for estimations not understood
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.NOT_UNDERSTOOD),MessageTemplate.MatchProtocol(AgClient3.BOOKAROOM_REQUEST)) );
		
		if (msg != null)
		{
			// If a not understood message arrives...
			System.out.println(myAgent.getLocalName()+": received BOOK A ROOM Request NOT_UNDERSTOOD from "+(msg.getSender()).getLocalName());
		}
		else
		{
			// If no message arrives
			block();
		}

	}
}
