package hotelmania.group3.platform.client.behaviour;

import jade.lang.acl.ACLMessage;
import hotelmania.group3.platform.AgClient3;
import hotelmania.ontology.Hotel;
import hotelmania.ontology.HotelInformation;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

@SuppressWarnings("serial")
public class GetHotelStaff extends CyclicBehaviour {

	AID ag;
	AID agency = new AID();;

	public GetHotelStaff(Agent agent) {
		super(agent);
	}

	public void action() {

		AgClient3 agent = (AgClient3) this.myAgent;
		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);

		if (agent.gethasbooked()) {

			if (agent.getHotelAID() != null) {
				ag = agent.getHotelAID();
				msg.setLanguage(agent.codec.getName());
				msg.setOntology(agent.ontology.getName());
				msg.setProtocol(AgClient3.STAFF_QUERY_REF);

				Hotel h = new Hotel();
				h.setHotel_name(ag.getLocalName());
				h.setHotelAgent(ag);

				HotelInformation hi = new HotelInformation();
				hi.setHotel(h);
				hi.setRating(0);

				agency.setLocalName("Agency");

				msg.addReceiver(agency);

				try {
					agent.getContentManager().fillContent(msg, hi);
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
				}

				agent.send(msg);
				System.out.println(agent.getLocalName()
						+ ": QUERY_REF  HotelStaff to   " + ag.getName());
			} else {
				System.out.println("Error, agent booked Without Hotel AID");
			}

		}

	}

}