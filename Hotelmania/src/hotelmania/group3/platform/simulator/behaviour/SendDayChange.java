/**
 * This action sends day notification to all registered agents.
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/04 00:28 $ $Revision: 1.0 $
 **/
package hotelmania.group3.platform.simulator.behaviour;

import hotelmania.group3.platform.AgSimulator3;
import hotelmania.ontology.DayEvent;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class SendDayChange extends TickerBehaviour {
	
	public SendDayChange(Agent agent, int milliseconds) {
		// TODO Auto-generated constructor stub
		super(agent, milliseconds);
	}

	@Override
	protected void onTick() {
		AgSimulator3 agent = (AgSimulator3)this.myAgent;
		if (agent.getRegisteredAgents().size() > 0)
		{
			agent.changeDay();
			for (AID ag : agent.getRegisteredAgents())
			{
			
				try{
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.setProtocol(AgSimulator3.TIMECHANGE_SERVICE);
					msg.addReceiver(ag);
					msg.setLanguage(agent.codec.getName());
					msg.setOntology(agent.ontology.getName());
					DayEvent e = new DayEvent();
					e.setDay(agent.getCurrentDay());
							
					Action agAction = new Action(ag, e);
					agent.getContentManager().fillContent(msg, agAction);
					agent.send(msg);
					System.out.println(String.format("%s: Day %d sent to %s.", agent.getLocalName(), agent.getCurrentDay(), ag.getLocalName()));
				}
				catch (CodecException ce){
					ce.printStackTrace();
				}
				catch (OntologyException oe){
					oe.printStackTrace();
				}
			}
		}
	}
}
