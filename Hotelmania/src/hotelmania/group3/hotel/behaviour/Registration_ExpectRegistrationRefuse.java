// Processes the REJECTION answer to request

package hotelmania.group3.hotel.behaviour;

import hotelmania.group3.hotel.AgHotel3;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class Registration_ExpectRegistrationRefuse extends CyclicBehaviour {
	
	public Registration_ExpectRegistrationRefuse(Agent agent) {
		super(agent);
	}

	public void action()
	{
		// Waits for estimation rejections
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchProtocol(AgHotel3.REGISTRATION_SERVICE), 
				 						 MessageTemplate.MatchPerformative(ACLMessage.REFUSE)));
		if (msg != null)
		{
			// If a rejection arrives...
			System.out.println(myAgent.getLocalName()+": received request REFUSE from "+(msg.getSender()).getLocalName());
		}
		else
		{
			// If no message arrives
			block();
		}

	}
}
