/**
 * Abstract class representing agents that need to be subscribed to the day event.
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/04 00:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform;

import hotelmania.group3.commonbehaviour.DayEvent_ReceiveDayNotification;
import hotelmania.group3.commonbehaviour.DayEvent_ExpectSubscriptionAgree;
import hotelmania.group3.commonbehaviour.DayEvent_ExpectSubscriptionNotUnderstood;
import hotelmania.group3.commonbehaviour.DayEvent_ExpectSubscriptionRefuse;
import hotelmania.group3.commonbehaviour.DayEvent_SubscribeForDayNotification;
import hotelmania.ontology.SharedAgentsOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;

@SuppressWarnings("serial")
public abstract class DayDependentAgent extends Agent {
	
	public int currentDay;
	
	public static final String SUBSCRIBEDAYEVENT_SERVICE = "SubscribeToDayEvent";
	public static final String SUBSCRIBETODAYEVENT = "SubscribeToDayEvent";
	
	public Codec codec = new SLCodec();
	public Ontology ontology = SharedAgentsOntology.getInstance();
	
	public abstract void ChangesOnDayChange();
	
	public void addDayBehaviour()
	{
    	// Adds a behavior to subscribe for day event
    	addBehaviour(new  DayEvent_SubscribeForDayNotification(this));
		
    	// Adds a behavior to process day notification
    	addBehaviour(new  DayEvent_ReceiveDayNotification(this));
    	
    	// Adds a behavior to process subscription answer receive
    	addBehaviour(new  DayEvent_ExpectSubscriptionAgree(this));
    	
    	// Adds a behavior to process subscription answer receive
    	addBehaviour(new  DayEvent_ExpectSubscriptionRefuse(this));
    	
    	// Adds a behavior to process subscription answer receive
    	addBehaviour(new  DayEvent_ExpectSubscriptionNotUnderstood(this));
	}
}
