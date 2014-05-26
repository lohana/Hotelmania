package hotelmania.group3.platform.agency.behaviuor;

import hotelmania.group3.platform.AgAgency3;
import hotelmania.group3.platform.AgClient3;
import jade.content.ContentElement;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class SendHotelStaff extends CyclicBehaviour {

	public SendHotelStaff(Agent agente) {
		super(agente);
	}

	@Override
	public void action() {

		AgAgency3 agent = (AgAgency3) this.myAgent;

		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate
				.MatchLanguage(agent.codec.getName()), MessageTemplate.and(
				MessageTemplate.MatchOntology(agent.ontology.getName()),
				MessageTemplate.MatchProtocol(AgClient3.STAFF_QUERY_REF))));

		if (msg != null) {

			ContentElement ce = null;
			int AclMessage = msg.getPerformative();
			ACLMessage reply = msg.createReply();
			reply.setProtocol(AgClient3.STAFF_QUERY_REF);

			if (AclMessage == ACLMessage.QUERY_REF) {

				System.out.println(myAgent.getLocalName()
						+ ": received HotelStaff from   "
						+ (msg.getSender()).getLocalName());

				reply.setPerformative(ACLMessage.INFORM);

				myAgent.send(reply);
				System.out.println(myAgent.getLocalName()
						+ ":  answer sent -> INFORM HotelStaff");

			} else if (AclMessage == ACLMessage.REFUSE) {
				System.out.println(myAgent.getLocalName()
						+ ": received REFUSE  from "
						+ (msg.getSender()).getLocalName());

			} else if (AclMessage == ACLMessage.NOT_UNDERSTOOD) {
				System.out.println(myAgent.getLocalName()
						+ ": received NOT_UNDERSTOOD  from "
						+ (msg.getSender()).getLocalName());

			}
		} else {
			// block();
		}
	}
}
