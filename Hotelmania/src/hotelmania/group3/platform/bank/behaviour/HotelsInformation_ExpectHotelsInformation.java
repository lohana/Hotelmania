package hotelmania.group3.platform.bank.behaviour;

import hotelmania.group3.platform.AgBank3;
import hotelmania.group3.platform.AgHotelmania3;
import hotelmania.ontology.HotelInformation;
import jade.content.ContentElementList;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class HotelsInformation_ExpectHotelsInformation extends CyclicBehaviour {

	public HotelsInformation_ExpectHotelsInformation(Agent agent) {
		super(agent);
	}

	public void action() {
		AgBank3 agent = (AgBank3) this.myAgent;

		// Waits for estimation requests
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate
				.MatchLanguage(agent.codec.getName()), MessageTemplate.and(
						MessageTemplate.MatchOntology(agent.ontology.getName()),
						MessageTemplate.MatchProtocol(AgHotelmania3.HOTEL_INFORMATION))));

		if (msg != null) {

			int AclMessage = msg.getPerformative();
			ACLMessage reply = msg.createReply();
			reply.setProtocol(AgHotelmania3.HOTEL_INFORMATION);

			if (AclMessage == ACLMessage.INFORM) {

				try {

					if (this.myAgent.getContentManager().extractContent(msg) instanceof HotelInformation) {

						HotelInformation hi = (HotelInformation) myAgent
								.getContentManager().extractContent(msg);
						agent.addHotelInformation(hi);
					}

					if (this.myAgent.getContentManager().extractContent(msg) instanceof ContentElementList) {

						ContentElementList li = (ContentElementList) myAgent
								.getContentManager().extractContent(msg);

						HotelInformation h1 = (HotelInformation) li.get(1);
						agent.addHotelInformation(h1);

						System.out.println(myAgent.getLocalName()
								+ ": received HotelInformation from Hotelmania HOTEL: "
								+ h1.getHotel().getHotel_name()
								+ "RATE " + h1.getRating()
								+ (msg.getSender()).getLocalName());
					}

				} catch (UngroundedException e) {
					e.printStackTrace();
				} catch (CodecException e) {
					e.printStackTrace();
				} catch (OntologyException e) {
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

		} else {
			// If no message arrives
			// block();
		}
	}

}

