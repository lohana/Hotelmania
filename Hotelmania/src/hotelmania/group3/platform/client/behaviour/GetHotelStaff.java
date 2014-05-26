package hotelmania.group3.platform.client.behaviour;

import jade.lang.acl.ACLMessage;
import hotelmania.group3.platform.AgClient3;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

@SuppressWarnings("serial")
public class GetHotelStaff extends CyclicBehaviour {

	AID ag;

	public GetHotelStaff(Agent agent) {

		super(agent);
	}

	@SuppressWarnings("static-access")
	public void action() {

		AgClient3 agent = (AgClient3) this.myAgent;
		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);

		if (agent.getisBooked()) {

			if (agent.getHotelAID() != null) {
				ag = agent.getHotelAID();
				msg.addReceiver(ag);
				msg.setLanguage(agent.codec.getName());
				msg.setOntology(agent.ontology.getName());
				msg.setProtocol(agent.STAFF_QUERY_REF);

				agent.send(msg);
				System.out.println(agent.getLocalName()
						+ ": QUERY_REF  HotelStaff to   " + ag.getName());

			} else {
				System.out.println("Error, agent booked Without Hotel AID");

			}

		}

	}

}