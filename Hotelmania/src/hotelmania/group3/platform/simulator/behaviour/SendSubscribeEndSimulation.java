/**
 * This action sends day notification to all registered agents.
 **/
package hotelmania.group3.platform.simulator.behaviour;

import hotelmania.group3.platform.AgSimulator3;
import hotelmania.ontology.NotificationEndSimulation;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class SendSubscribeEndSimulation extends SimpleBehaviour {
	
	private boolean end = false;

	public SendSubscribeEndSimulation(Agent agent) {
		super(agent);
	}

	@Override
	public void action() {
		AgSimulator3 agent = (AgSimulator3) this.myAgent;
		if (agent.getRegisteredAgents_EndSimulation().size() > 0) {
			int currentDay = agent.getCurrentDay();

			int max_days = agent.getLastDay();

			if (currentDay > max_days) {

				for (AID ag : agent.getRegisteredAgents_EndSimulation()) {

					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.setProtocol(AgSimulator3.END_SIMULATION);
					msg.addReceiver(ag);
					msg.setLanguage(agent.codec.getName());
					msg.setOntology(agent.ontology.getName());

					NotificationEndSimulation n = new NotificationEndSimulation();
					try {
						agent.getContentManager().fillContent(msg, n);
					} catch (CodecException | OntologyException e) {
						e.printStackTrace();
					}
					agent.send(msg);
					System.out.println(String.format(
							"%s: Simulation %d Over  to %s.",
							agent.getLocalName(), agent.getCurrentDay(),
							ag.getLocalName()));
				}
				agent.doDelete();
				end = true;
			}
		}
	}

	@Override
	public boolean done() {
		return end;
	}
}
