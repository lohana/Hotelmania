/**
 * This action sends day notification to all registered agents.
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/04 00:28 $ $Revision: 1.0 $
 **/
package hotelmania.group3.platform.simulator.behaviour;

import hotelmania.group3.platform.AgSimulator3;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class SendSubscribeEndSimulation extends SimpleBehaviour {
	
	public SendSubscribeEndSimulation(Agent agent) {
		super(agent);
	} 

	@Override
	public void action() {
		AgSimulator3 agent = (AgSimulator3)this.myAgent;
		if (agent.getRegisteredAgents_EndSimulation().size() > 0)
		{
			int currentDay = agent.getCurrentDay();
			
			if (currentDay == 30){
			
			for (AID ag : agent.getRegisteredAgents_EndSimulation())
			{
			
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setProtocol(AgSimulator3.END_SIMULATION);
				msg.addReceiver(ag);
				msg.setLanguage(agent.codec.getName());
				msg.setOntology(agent.ontology.getName());

				agent.send(msg);
				System.out.println(String.format("%s: Simulation %d Over  to %s.", agent.getLocalName(), agent.getCurrentDay(), ag.getLocalName()));
			}
			
			}
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
}