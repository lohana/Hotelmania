package hotelmania.group3.platform.client.behaviour;

import hotelmania.group3.platform.AgClient3;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class GetHotelStaff_ExpectforMessages extends CyclicBehaviour {

	public GetHotelStaff_ExpectforMessages(Agent agent) {

		super(agent);
	}

	@SuppressWarnings("static-access")
	public void action() {
		AgClient3 agent = (AgClient3) this.myAgent;

		// Waits for estimation requests
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate
				.MatchLanguage(agent.codec.getName()), MessageTemplate.and(
				MessageTemplate.MatchOntology(agent.ontology.getName()),
				MessageTemplate.MatchProtocol(agent.STAFF_QUERY_REF))));

		if (msg != null) {

			int AclMessage = msg.getPerformative();

			if (AclMessage == ACLMessage.INFORM) {

				System.out.println(myAgent.getLocalName()
						+ ": received HotelStaff from    : "
						+ msg.getSender().getName());

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
			// block();
		}

	}

}
