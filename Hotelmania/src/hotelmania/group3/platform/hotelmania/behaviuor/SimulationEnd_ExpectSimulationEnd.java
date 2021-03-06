// Expects end of simulation

package hotelmania.group3.platform.hotelmania.behaviuor;

import hotelmania.group3.platform.AgHotelmania3;
import hotelmania.group3.platform.AgSimulator3;
import hotelmania.ontology.NotificationEndSimulation;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class SimulationEnd_ExpectSimulationEnd extends CyclicBehaviour {

	public SimulationEnd_ExpectSimulationEnd(Agent agent) {

		super(agent);
	}

	public void action() {
		AgHotelmania3 agent = (AgHotelmania3) this.myAgent;

		// Waits for estimation requests
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate
				.MatchLanguage(agent.codec.getName()), MessageTemplate.and(
				MessageTemplate.MatchOntology(agent.ontology.getName()),
				MessageTemplate.MatchProtocol(AgSimulator3.END_SIMULATION))));

		if (msg != null) {
			ContentElement ce = null;
			int AclMessage = msg.getPerformative();
			

			if (AclMessage == ACLMessage.INFORM) {

				try {
					ce = agent.getContentManager().extractContent(msg);

					if (ce instanceof NotificationEndSimulation) {

						System.out.println(myAgent.getLocalName()
								+ ": received END_SIMULATION from  "
								+ msg.getSender().getLocalName());
						myAgent.doDelete();

					}

				} catch (CodecException | OntologyException e) {
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
