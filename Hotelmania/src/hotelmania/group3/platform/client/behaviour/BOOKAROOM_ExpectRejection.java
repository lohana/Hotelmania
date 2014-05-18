// Processes the REJECTION answer to request

package hotelmania.group3.platform.client.behaviour;

import hotelmania.group3.platform.AgClient3;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class BOOKAROOM_ExpectRejection extends CyclicBehaviour {
	
	public BOOKAROOM_ExpectRejection(Agent agent) {
		super(agent);
	}

	public void action()
	{
		// Waits for estimation rejections
		//ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL));
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REFUSE),MessageTemplate.MatchProtocol(AgClient3.BOOKAROOM_REQUEST)) );
		
		if (msg != null)
		{
			// If a rejection arrives...
			System.out.println(myAgent.getLocalName()+": received BOOK A ROOM Request REFUSE from "+(msg.getSender()).getLocalName());
		}
		else
		{
			// If no message arrives
			block();
		}

	}
}
