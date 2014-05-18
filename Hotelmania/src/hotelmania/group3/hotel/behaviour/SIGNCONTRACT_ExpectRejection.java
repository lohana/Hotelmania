// Processes the REJECTION answer to request

package hotelmania.group3.hotel.behaviour;

import hotelmania.group3.hotel.AgHotel3;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class SIGNCONTRACT_ExpectRejection extends CyclicBehaviour {
	
	public SIGNCONTRACT_ExpectRejection(Agent agent) {
		super(agent);
	}

	public void action()
	{
		// Waits for estimation rejections
		//ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL));
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REFUSE),MessageTemplate.MatchProtocol(AgHotel3.SIGN_CONTRACT)) );
		
		if (msg != null)
		{
			// If a rejection arrives...
			System.out.println(myAgent.getLocalName()+": received Sign Contract Request REFUSE "+(msg.getSender()).getLocalName());
		}
		else
		{
			// If no message arrives
			block();
		}

	}
}
