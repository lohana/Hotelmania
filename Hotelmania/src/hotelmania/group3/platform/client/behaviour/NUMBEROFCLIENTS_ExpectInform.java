// Processes the ACEPTATION answer to request

package hotelmania.group3.platform.client.behaviour;

import hotelmania.group3.platform.AgClient3;
import hotelmania.ontology.NumberOfClients;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class NUMBEROFCLIENTS_ExpectInform extends CyclicBehaviour {

	public NUMBEROFCLIENTS_ExpectInform(Agent agent)
	{
		super(agent);
	}
	
	public void action()
	{
		
		// Waits for sign contract acceptation
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),MessageTemplate.MatchProtocol(AgClient3.NUMBEROFCLIENTS_QUERY)) );
		
		//ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.AGREE));
		if (msg != null)
		{
			 try {
			// If an acceptation arrives...
			NumberOfClients numberOfclientsInformed = (NumberOfClients) myAgent.getContentManager().extractContent(msg);
			((AgClient3)this.myAgent).storeNumberOfClients(numberOfclientsInformed);
			
			System.out.println(myAgent.getLocalName()+": received Number of clients INFORM from "+(msg.getSender()).getLocalName() + "Result: " + numberOfclientsInformed.getNum_clients() );
			 } catch (OntologyException | CodecException oe){
					oe.printStackTrace();
			}
		}
		else
		{
			// If no message arrives
			block();
		}

	}

}
