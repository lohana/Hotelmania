package hotelmania.group3.platform.agency.behaviuor;

import hotelmania.group3.platform.AgAgency3;
import hotelmania.group3.platform.AgClient3;
import hotelmania.ontology.HotelContract;
import hotelmania.ontology.HotelInformation;
import jade.content.ContentElementList;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class StaffContracts_SendHotelStaffContracts extends CyclicBehaviour {

	public StaffContracts_SendHotelStaffContracts(Agent agente) {
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

			int AclMessage = msg.getPerformative();
			ACLMessage reply = msg.createReply();
			reply.setProtocol(AgClient3.STAFF_QUERY_REF);

			if (AclMessage == ACLMessage.QUERY_REF) {

				try {
					if (this.myAgent.getContentManager().extractContent(msg) instanceof HotelInformation) {

						try {
							HotelInformation hi = (HotelInformation) myAgent
									.getContentManager().extractContent(msg);

							ContentElementList cel = new ContentElementList();

							for (int i = 0; i < agent.signedContracts.size(); i++) {
								if (i != 0) {
									String hnm = agent.signedContracts.get(i)
											.getHotel().getHotel_name()
											.toLowerCase();
									String hhim = hi.getHotel().getHotel_name()
											.toLowerCase();

									if (hnm.compareTo(hhim) == 0) {
										HotelContract hc = new HotelContract();
										hc.setContract(agent.signedContracts
												.get(i).getContract());
										hc.setHotel(agent.signedContracts
												.get(i).getHotel());
										cel.add(hc);
									}
								}
							}

							myAgent.getContentManager().fillContent(reply, cel);

							System.out.println(myAgent.getLocalName()
									+ ": received HotelStaff from   "
									+ (msg.getSender()).getLocalName());

							reply.setPerformative(ACLMessage.INFORM);

							myAgent.send(reply);
							System.out.println(myAgent.getLocalName()
									+ ":  answer sent -> INFORM HotelStaff");

						} catch (CodecException | OntologyException e) {
							e.printStackTrace();
						}

					} else {
						System.out.println(myAgent.getLocalName()
								+ ": received NOT_UNDERSTOOD  from "
								+ (msg.getSender()).getLocalName());
					}
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
				}

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
