// Receives Subscription answer not understood

package hotelmania.group3.commonbehaviour;

import hotelmania.group3.platform.DayDependentAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class DayEvent_ExpectSubscriptionNotUnderstood extends CyclicBehaviour {

	public DayEvent_ExpectSubscriptionNotUnderstood(Agent agent)
	{
		super(agent);
	}
	
	public void action()
	{
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchProtocol(DayDependentAgent.SUBSCRIBETODAYEVENT), 
				 						 MessageTemplate.MatchPerformative(ACLMessage.NOT_UNDERSTOOD)));
		if (msg != null)
		{
			System.out.println(myAgent.getLocalName()+": received NOT_UNDERSTOOD from " + (msg.getSender()).getName());
		}
		else
		{
			// If no message arrives
			block();
		}
	}
}
