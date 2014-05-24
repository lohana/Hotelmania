// Expects account status

package hotelmania.group3.platform.bank.behaviour;

import hotelmania.group3.platform.AgBank3;
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
public class SubscribeFrEndSimulation_ExpectforMessages extends CyclicBehaviour {

	public SubscribeFrEndSimulation_ExpectforMessages(Agent agent) {

		super(agent);
	}

	public void action() {
		AgBank3 agent = (AgBank3) this.myAgent;

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
