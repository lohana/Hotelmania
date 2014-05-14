// Processes the REJECTION answer to request
package hotelmania.group3.platform.client.behaviour;

import hotelmania.group3.hotel.AgHotel3;
import hotelmania.group3.platform.AgClient;
import hotelmania.ontology.NumberOfClients;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class NUMBEROFCLIENTS_ExpectFailure extends CyclicBehaviour {
	
	public NUMBEROFCLIENTS_ExpectFailure(Agent agent) {
		super(agent);
	}

	public void action()
	{
		// Waits for sign contract acceptation
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.FAILURE),MessageTemplate.MatchProtocol(AgClient.NUMBEROFCLIENTS_QUERY)) );
		
		//ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.AGREE));
		if (msg != null)
		{
			
			System.out.println(myAgent.getLocalName()+": Query of Number of Clients FAILED. Sender: "+(msg.getSender()).getLocalName() );
			
		}
		else
		{
			// If no message arrives
			block();
				}

	}
}