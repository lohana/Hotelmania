// Processes the ACEPTATION answer to request
package hotelmania.group3.hotel.behaviour;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ExpectReqistrationAgree extends CyclicBehaviour {

	public ExpectReqistrationAgree(Agent agent)
	{
		super(agent);
	}
	
	public void action()
	{
		// Waits for estimation acceptations
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.AGREE));
		if (msg != null)
		{
			// If an acceptation arrives...
			System.out.println(myAgent.getLocalName()+": received request AGREE from "+(msg.getSender()).getLocalName());
		}
		else
		{
			// If no message arrives
			block();
		}

	}

}
