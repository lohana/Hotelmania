// Receives Subscription answer agree
package hotelmania.group3.commonbehaviour;

import hotelmania.group3.platform.DayDependentAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ReceiveSubscriptionAgree extends CyclicBehaviour {

	public ReceiveSubscriptionAgree(Agent agent)
	{
		super(agent);
	}
	
	public void action()
	{
		ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchProtocol(DayDependentAgent.SUBSCRIBETODAYEVENT), 
										 MessageTemplate.MatchPerformative(ACLMessage.AGREE)));
		if (msg != null)
		{
			System.out.println(myAgent.getLocalName() + ": received AGREE from " + (msg.getSender()).getName());
		}
		else
		{
			// If no message arrives
			block();
		}
	}
}
