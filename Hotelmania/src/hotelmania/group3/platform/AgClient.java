/**
 * Client agent
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/04/27 20:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform;

import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import hotelmania.group3.commonbehaviour.ReceiveDayNotification;
import hotelmania.group3.commonbehaviour.ReceiveSubscriptionAgree;
import hotelmania.group3.commonbehaviour.ReceiveSubscriptionRefuse;
import hotelmania.group3.commonbehaviour.SubscribeForDayNotification;
import hotelmania.group3.ontology.Ontology3;
import hotelmania.group3.platform.client.behaviour.SendRate;
import hotelmania.ontology.SharedAgentsOntology;

@SuppressWarnings("serial")
public class AgClient extends DayDependentAgent {
	
	public static final String EVALUATION_SERVICE = "Evaluation";
	
	public Codec codec = new SLCodec();
	public Ontology innerOntology = Ontology3.getInstance();
	
	public String id;
	
	// Information about the hotel where the client stayed
	private String hotel = "";
	private int rate = 0;
	
	protected void setup(){
		
		ontology = SharedAgentsOntology.getInstance();
		Object[] pr = this.getArguments();
		this.id = (String)pr[0];
		
		System.out.println(getLocalName()+": New client entered");

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(innerOntology);	
        getContentManager().registerOntology(ontology);		
        
        // TOREMOVE
        // Random generation of rate, should be assign when going to the hotel 
        rate = randomValue();
        hotel = "Hotel3";
        
        // Adds a behavior to evaluate a hotel
        addBehaviour(new SendRate(this));
        
        // Adds a behavior to subscribe for day event
    	addBehaviour(new  SubscribeForDayNotification(this));
		
    	// Adds a behavior to process day notification
    	addBehaviour(new  ReceiveDayNotification(this));
    	
    	// Adds a behavior to process subscription answer receive
    	addBehaviour(new  ReceiveSubscriptionAgree(this));
    	
    	// Adds a behavior to process subscription answer receive
    	addBehaviour(new  ReceiveSubscriptionRefuse(this));
    }
	
	public int getRate()
	{
		return rate;
	}
	
	public String getHotel()
	{
		return hotel;
	}

	private int randomValue() {
		return (int)(Math.random() * 11);
	}
	
	public void ChangesOnDayChange()
    {
		System.out.println(getLocalName() + ": Day changed to " + currentDay);
    }
}
