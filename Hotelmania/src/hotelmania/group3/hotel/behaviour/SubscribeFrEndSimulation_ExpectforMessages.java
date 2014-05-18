// Expects account status

package hotelmania.group3.hotel.behaviour;

import hotelmania.group3.hotel.AgHotel3;
import hotelmania.group3.platform.AgBank3;
import hotelmania.group3.platform.AgSimulator3;
import hotelmania.ontology.AccountStatus;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class SubscribeFrEndSimulation_ExpectforMessages extends CyclicBehaviour {

	public SubscribeFrEndSimulation_ExpectforMessages(Agent agent) {

		super(agent);
	}

	public void action() {
		AgHotel3 agent = (AgHotel3) this.myAgent;

		// Waits for estimation requests
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate
				.MatchLanguage(agent.codec.getName()), MessageTemplate.and(
				MessageTemplate.MatchOntology(agent.ontology.getName()),
				MessageTemplate.MatchProtocol(AgSimulator3.END_SIMULATION))));

		if (msg != null) {
			int AclMessage = msg.getPerformative();
			ACLMessage reply = msg.createReply();
			reply.setProtocol(AgSimulator3.END_SIMULATION);

			if (AclMessage == ACLMessage.INFORM) {

				System.out.println(myAgent.getLocalName()
						+ ": received END_SIMULATION from  "+ msg.getSender().getLocalName()) ;
	               myAgent.doDelete();                    


			} else if (AclMessage == ACLMessage.REFUSE) {
				System.out.println(myAgent.getLocalName()
						+ ": received FAILURE  from "
						+ (msg.getSender()).getLocalName());

			} else if (AclMessage == ACLMessage.NOT_UNDERSTOOD) {
				System.out.println(myAgent.getLocalName()
						+ ": received FAILURE  from "
						+ (msg.getSender()).getLocalName());

			}

		} else {
			// If no message arrives
			//block();
		}

	}

}
