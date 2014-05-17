package hotelmania.group3.platform.client.behaviour;

import hotelmania.group3.platform.AgClient3;
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
public class GetHotelInformation_ExpectforMessages extends CyclicBehaviour {

	public GetHotelInformation_ExpectforMessages(Agent agent) {

		super(agent);
	}

	@SuppressWarnings("static-access")
	public void action() {
		AgClient3 agent = (AgClient3) this.myAgent;
		AgHotelmania3 agentb = new AgHotelmania3();

		// Waits for estimation requests
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate
				.MatchLanguage(agent.codec.getName()), MessageTemplate.and(
						MessageTemplate.MatchOntology(agent.ontology.getName()),
						MessageTemplate.MatchProtocol(agentb.HOTEL_INFORMATION))));

		if (msg != null) {

			int AclMessage = msg.getPerformative();
			ACLMessage reply = msg.createReply();
			reply.setProtocol(agentb.HOTEL_INFORMATION);

			if (AclMessage == ACLMessage.INFORM) {

				try {

					if (this.myAgent.getContentManager().extractContent(msg) instanceof HotelInformation) {

						HotelInformation hi = (HotelInformation) myAgent
								.getContentManager().extractContent(msg);

						// We expect an action inside the message

						System.out
						.println(myAgent.getLocalName()
								+ ": received HotelInformation from Hotelmania ****HOTEL: "
								+ hi.getHotel().getHotel_name()
								+ "****** RANKIN " + hi.getRating()
								+ (msg.getSender()).getLocalName());

					}

					if (this.myAgent.getContentManager().extractContent(msg) instanceof ContentElementList) {

						ContentElementList li = (ContentElementList) myAgent
								.getContentManager().extractContent(msg); // ce
						// =
						// agentb.getContentManager().extractContent(msg);

						HotelInformation h1 = (HotelInformation) li.get(1);
						// We expect an action inside the message

						System.out
						.println(myAgent.getLocalName()
								+ ": received HotelInformation from Hotelmania ****HOTEL: "
								+ h1.getHotel().getHotel_name()
								+ "****** RANKIN " + h1.getRating()
								+ (msg.getSender()).getLocalName());

					}

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

		} else {
			// If no message arrives
			// block();
		}

	}

}
