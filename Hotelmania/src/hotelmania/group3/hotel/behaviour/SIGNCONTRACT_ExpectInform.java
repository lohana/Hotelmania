// Processes the ACEPTATION answer to request

package hotelmania.group3.hotel.behaviour;


import hotelmania.group3.hotel.AgHotel3;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class SIGNCONTRACT_ExpectInform extends CyclicBehaviour {

	public SIGNCONTRACT_ExpectInform(Agent agent)
	{
		super(agent);
	}
	
	public void action()
	{
		
		// Waits for sign contract acceptation
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),MessageTemplate.MatchProtocol(AgHotel3.SIGN_CONTRACT)) );
		
		//ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.AGREE));
		if (msg != null)
		{
			// If an acceptation arrives...
			System.out.println(myAgent.getLocalName()+": received Sign Contract INFORM from "+(msg.getSender()).getLocalName());
		}
		else
		{
			// If no message arrives
			block();
		}

	}

}
