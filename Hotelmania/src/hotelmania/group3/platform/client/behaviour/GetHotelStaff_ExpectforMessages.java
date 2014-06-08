package hotelmania.group3.platform.client.behaviour;

import hotelmania.group3.platform.AgClient3;
import hotelmania.ontology.HotelContract;
import jade.content.ContentElement;
import jade.content.ContentElementList;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class GetHotelStaff_ExpectforMessages extends CyclicBehaviour {

	public GetHotelStaff_ExpectforMessages(Agent agent) {
		super(agent);
	}

	public void action() {
		AgClient3 agent = (AgClient3) this.myAgent;

		// Waits for estimation requests
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate
				.MatchLanguage(agent.codec.getName()), MessageTemplate.and(
				MessageTemplate.MatchOntology(agent.ontology.getName()),
				MessageTemplate.MatchProtocol(AgClient3.STAFF_QUERY_REF))));

		if (msg != null) {

			int AclMessage = msg.getPerformative();

			if (AclMessage == ACLMessage.INFORM) {
				
				try {

					if (this.myAgent.getContentManager().extractContent(msg) instanceof ContentElementList) {
						ContentElementList li = (ContentElementList) myAgent
								.getContentManager().extractContent(msg);
						for (ContentElement ce : li.toArray()) {
							agent.storeContract((HotelContract)ce);
						}
					}

					else if (this.myAgent.getContentManager().extractContent(msg) instanceof HotelContract) {
						HotelContract hc = (HotelContract) myAgent
								.getContentManager().extractContent(msg);
						agent.storeContract(hc);
					}

				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
				}

				/*try {
					if (this.myAgent.getContentManager().extractContent(msg) instanceof ContentElementList) {

						ContentElementList li = (ContentElementList) myAgent
								.getContentManager().extractContent(msg);
						for (ContentElement ce : li.toArray()) {
							agent.storeContract((HotelContract)ce);
						}
					}
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
				}
				*/
				
				
				
/*				
				
				
				

				try {

					if (this.myAgent.getContentManager().extractContent(msg) instanceof ContentElementList) {

						ContentElementList li = (ContentElementList) myAgent
								.getContentManager().extractContent(msg); 
						for (ContentElement ce : li.toArray()) {
							agent.storeContract((HotelContract)ce);
						}
						
						/* // ce
						// =
						// agentb.getContentManager().extractContent(msg);

						HotelContract h1 = (HotelContract) li.get(1);
						// We expect an action inside the message

						System.out
								.println(myAgent.getLocalName()
										+ ": received HotelStaff ++++++++++++++++++++++++++++++ "
										+ h1.getHotel().getHotel_name()
										+ "day " + h1.getContract().getDay()
										+ (msg.getSender()).getLocalName());//
					}

					else if (this.myAgent.getContentManager().extractContent(
							msg) instanceof HotelContract) {

						HotelContract hc = (HotelContract) myAgent
								.getContentManager().extractContent(msg);
						
						agent.storeContract(hc);

						/* // We expect an action inside the message
						System.out
								.println(myAgent.getLocalName()
										+ ": received HotelStaff ******************************** "
										+ hc.getHotel().getHotel_name()
										+ " Day " + hc.getContract().getDay()
										+ (msg.getSender()).getLocalName());//
					}
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
				}

				try {
					if (this.myAgent.getContentManager().extractContent(msg) instanceof ContentElementList) {

						ContentElementList li = (ContentElementList) myAgent
								.getContentManager().extractContent(msg); // ce
						// =
						// agentb.getContentManager().extractContent(msg);

						HotelContract h1 = (HotelContract) li.get(1);
						// We expect an action inside the message

						System.out
								.println(myAgent.getLocalName()
										+ ": received HotelStaff ***************************** "
										+ h1.getHotel().getHotel_name()
										+ "Day " + h1.getContract().getDay()
										+ (msg.getSender()).getLocalName());
					}
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
				}*/

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
