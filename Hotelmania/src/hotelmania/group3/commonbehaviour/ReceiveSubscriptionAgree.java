// Receives Subscription answer agree
package hotelmania.group3.commonbehaviour;

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
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.AGREE));
		if (msg != null)
		{
			System.out.println(myAgent.getLocalName()+": received AGREE from Simulator");
		}
		else
		{
			// If no message arrives
			block();
		}
	}
}
