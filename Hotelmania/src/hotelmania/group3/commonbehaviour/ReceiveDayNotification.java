/**
 * Processes the day notification
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/04 00:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.commonbehaviour;

import hotelmania.group3.platform.DayDependentAgent;
import hotelmania.ontology.DayEvent;
import hotelmania.ontology.NotificationDayEvent;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ReceiveDayNotification extends CyclicBehaviour {

	public ReceiveDayNotification(Agent agent)
	{
		super(agent);
	}
	
	public void action()
	{
		// Waits for evaluation
		DayDependentAgent agent = (DayDependentAgent)this.myAgent;
		
		// Waits for estimation requests
		ACLMessage msg = agent.receive(MessageTemplate.and(
									   MessageTemplate.MatchLanguage(agent.codec.getName()), 
									   MessageTemplate.and(MessageTemplate.MatchProtocol(DayDependentAgent.SUBSCRIBETODAYEVENT),
									   MessageTemplate.MatchOntology(agent.ontology.getName()))));
		
		if(msg!=null)
		{
			try
			{
				ContentElement content = null;
				int message = msg.getPerformative();
				
				if (message == ACLMessage.INFORM) 
				{
					content = agent.getContentManager().extractContent(msg);
					if (content instanceof NotificationDayEvent)
					{
						NotificationDayEvent n = (NotificationDayEvent)content;
						DayEvent e = n.getDayEvent();
						int day = e.getDay();
						agent.currentDay = day;
						agent.ChangesOnDayChange();
					}
				}
			}
			catch (CodecException e){
				e.printStackTrace();
			}
			catch (OntologyException oe){
				oe.printStackTrace();
			}
		}
		else
		{
			// If no message arrives
			block();
		}
	}

}
