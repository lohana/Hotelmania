// Processes the ACCOUNT answer to request

package hotelmania.group3.platform.agency.behaviuor;

import hotelmania.group3.platform.AgAgency3;
import hotelmania.group3.platform.AgBank3;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ChargetoAccount_ExpectforMessages extends CyclicBehaviour {

	public ChargetoAccount_ExpectforMessages(Agent agent)
	{
		super(agent);
	}

	public void action()
	{
		AgAgency3 agent = (AgAgency3)myAgent;
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), 
				MessageTemplate.and(MessageTemplate.MatchOntology(agent.ontology.getName()),
						MessageTemplate.MatchProtocol(AgBank3.CHARGE_ACCOUNT_SERVICE))));
		if (msg != null)
		{
			int AclMessage = msg.getPerformative();


			if (AclMessage == ACLMessage.AGREE) {


			System.out.println(myAgent.getLocalName()+": received AGREE from "+(msg.getSender()).getLocalName());    
					
				



			}else if(AclMessage == ACLMessage.REFUSE)
			{

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
