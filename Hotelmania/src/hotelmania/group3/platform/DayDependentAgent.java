/**
 * Abstract class representing agents that need to be subscribed to the day event.
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/04 00:28 $ $Revision: 1.0 $
 **/
package hotelmania.group3.platform;

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
}
