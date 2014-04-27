// Processes the NOT_UNDERSTOOD answer to request
package hotelmania.group3.hotel.behaviour;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ExpectNotUnderstood extends CyclicBehaviour {
	
	public ExpectNotUnderstood(Agent agent) {
		super(agent);
	}
	
	public void action()
	{
		// Waits for estimations not understood
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.NOT_UNDERSTOOD));
		if (msg != null)
		{
			// If a not understood message arrives...
			System.out.println(myAgent.getLocalName()+": received NOT_UNDERSTOOD response from "+(msg.getSender()).getLocalName());
		}
		else
		{
			// If no message arrives
			block();
		}

	}
}
