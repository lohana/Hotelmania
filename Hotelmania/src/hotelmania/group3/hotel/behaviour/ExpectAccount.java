// Processes the ACCOUNT answer to request
package hotelmania.group3.hotel.behaviour;

import hotelmania.group3.hotel.AgHotel3;
import hotelmania.group3.platform.AgBank3;
import hotelmania.ontology.Hotel;
import hotelmania.ontology.RegistrationRequest;
import jade.content.*;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.*;
import jade.content.onto.basic.*;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import hotelmania.ontology.*;

@SuppressWarnings("serial")
public class ExpectAccount extends CyclicBehaviour {

	public ExpectAccount(Agent agent)
	{
		super(agent);
	}
	
	public void action()
	{
		AgHotel3 agent = (AgHotel3)myAgent;
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), 
				MessageTemplate.and(MessageTemplate.MatchOntology(agent.ontology.getName()),
						MessageTemplate.MatchProtocol(AgHotel3.CREATEACCOUNT_SERVICE))));
		if (msg != null)
		{
			
			ContentElement ce = null;
			try {
				ce = myAgent.getContentManager().extractContent(msg);
			} catch (UngroundedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// We expect an action inside the message
			if (ce instanceof AccountStatus){
				
				Concept conc = ((AccountStatus) ce).getAccount();
				// If the action is RegistrationRequest...
				if (conc instanceof Account){
					System.out.println(myAgent.getLocalName()+": received bank account from "+(msg.getSender()).getLocalName() + ": id-" + ((Account)conc).getId_account());    
				}
			}
		}
		else
		{
			// If no message arrives
			block();
		}

	}

}
