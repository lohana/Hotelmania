// Processes the ACEPTATION answer to request

package hotelmania.group3.hotel.behaviour;

import hotelmania.group3.hotel.AgHotel3;
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
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchProtocol(AgHotel3.REGISTRATION_SERVICE), 
										 MessageTemplate.MatchPerformative(ACLMessage.AGREE)));
		if (msg != null)
		{
			System.out.println(myAgent.getLocalName()+": received request AGREE from "+(msg.getSender()).getLocalName());
		}
		else
		{
			// If no message arrives
			block();
		}

	}

}
