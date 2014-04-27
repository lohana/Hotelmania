// Processes the REJECTION answer to request
package hotelmania.group3.hotel.behaviour;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ExpectRejection extends CyclicBehaviour {
	
	public ExpectRejection(Agent agent) {
		super(agent);
	}

	public void action()
	{
		// Waits for estimation rejections
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL));
		if (msg != null)
		{
			// If a rejection arrives...
			System.out.println(myAgent.getLocalName()+": received request REJECTION from "+(msg.getSender()).getLocalName());
		}
		else
		{
			// If no message arrives
			block();
		}

	}
}