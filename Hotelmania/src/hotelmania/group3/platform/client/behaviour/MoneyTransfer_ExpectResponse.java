package hotelmania.group3.platform.client.behaviour;

import hotelmania.group3.platform.AgBank3;
import hotelmania.group3.platform.AgClient3;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class MoneyTransfer_ExpectResponse extends CyclicBehaviour {

	public MoneyTransfer_ExpectResponse(Agent agent){
		super(agent);
	}

	public void action()
	{
		AgClient3 client = (AgClient3)myAgent;
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(client.codec.getName()), 
				MessageTemplate.and(MessageTemplate.MatchOntology(client.ontology.getName()),
						MessageTemplate.MatchProtocol(AgBank3.PAYMENTS))));
		if (msg != null)
		{
			int AclMessage = msg.getPerformative();
			if (AclMessage == ACLMessage.AGREE) {
				System.out.println(myAgent.getLocalName()+": received AGREE from "+(msg.getSender()).getLocalName());    	
			}else if(AclMessage == ACLMessage.REFUSE){
				System.out.println(myAgent.getLocalName()
						+ ": received REFUSE  from "
						+ (msg.getSender()).getLocalName());
			}else{
				System.out.println(myAgent.getLocalName()
						+ ": received NOT_UNDERSTOOD  from "
						+ (msg.getSender()).getLocalName());
			}
		}
		else
		{
			// If no message arrives
			block();
		}

	}

}
