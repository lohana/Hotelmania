/**
 * This action sends day notification to all registered agents.
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/04 00:28 $ $Revision: 1.0 $
 **/
package hotelmania.group3.platform.simulator.behaviour;

import hotelmania.group3.platform.AgSimulator3;
import hotelmania.ontology.DayEvent;
import hotelmania.ontology.NotificationDayEvent;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class SendDayChange extends TickerBehaviour {

	public SendDayChange(Agent agent, int milliseconds) {
		super(agent, milliseconds);
	}

	@Override
	protected void onTick() {
		AgSimulator3 agent = (AgSimulator3) this.myAgent;

		if (agent.getCurrentDay() == 0) {
			try {
				Thread.sleep(agent.initialDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (agent.getRegisteredAgents().size() > 0) {
			agent.changeDay();
			if (agent.get_isover()) {
				return;
			}
			for (AID ag : agent.getRegisteredAgents()) {

				try {
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.setProtocol(AgSimulator3.TIMECHANGE_SERVICE);
					msg.addReceiver(ag);
					msg.setLanguage(agent.codec.getName());
					msg.setOntology(agent.ontology.getName());

					DayEvent e = new DayEvent();
					e.setDay(agent.getCurrentDay());

					NotificationDayEvent n = new NotificationDayEvent();
					n.setDayEvent(e);

					agent.getContentManager().fillContent(msg, n);
					agent.send(msg);
					System.out.println(String.format("%s: Day %d sent to %s.",
							agent.getLocalName(), agent.getCurrentDay(),
							ag.getLocalName()));
				} catch (CodecException ce) {
					ce.printStackTrace();
				} catch (OntologyException oe) {
					oe.printStackTrace();
				}
			}
		}
	}
}
