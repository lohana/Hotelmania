package hotelmania.group3.hotel.behaviour;

import hotelmania.group3.hotel.AgHotel3;
import hotelmania.group3.platform.AgHotelmania3;
import hotelmania.ontology.AccountStatus;
import hotelmania.ontology.AccountStatusQueryRef;
import hotelmania.ontology.NumberOfClients;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class GetAccountStatus_ExpectforMessages extends CyclicBehaviour {

	public GetAccountStatus_ExpectforMessages(Agent agent) {

		super(agent);
	}

	public void action() {
		AgHotel3 agent = (AgHotel3) this.myAgent;

		// Waits for estimation requests
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate
				.MatchLanguage(agent.codec.getName()), MessageTemplate.and(
				MessageTemplate.MatchOntology(agent.ontology.getName()),
				MessageTemplate.MatchProtocol(agent.ACCOUNT_INFO))));
		//ACLMessage msg = agentb.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), MessageTemplate.and(
	//	MessageTemplate.MatchOntology(agentb.ontology.getName()),MessageTemplate.MatchProtocol(agent.ACCOUNT_INFO))));
		
		//MessageTemplate msgTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),MessageTemplate.MatchProtocol("Registration"));

		if (msg != null) {

			ContentElement ce = null;
			int AclMessage = msg.getPerformative();
			ACLMessage reply = msg.createReply();
			reply.setProtocol(agent.ACCOUNT_INFO);

			if (AclMessage == ACLMessage.INFORM) {

				
				
				
				try {
					
 
					AccountStatus as = (AccountStatus) myAgent.getContentManager().extractContent(msg);

 					 
					System.out.println(myAgent.getLocalName()
							+ ": received AccountStatus from Bank  "
							+ (msg.getSender()).getLocalName() + "Balance: " + as.getAccount().getBalance());
					
					
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
				

			} else if (AclMessage == ACLMessage.REFUSE) {
				System.out.println(myAgent.getLocalName()
						+ ": received FAILURE  from "
						+ (msg.getSender()).getLocalName());

			} else if (AclMessage == ACLMessage.NOT_UNDERSTOOD) {
				System.out.println(myAgent.getLocalName()
						+ ": received FAILURE  from "
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
